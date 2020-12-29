package ch.nutrio.admin;


import java.util.Map;

import ch.nutrio.data.Store;
import ch.nutrio.data.StoreKeeper;
import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.util.UIUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.olli.ClipboardHelper;

@PageTitle("Admin")
@Route(value = "admin", layout = StartLayout.class)
public class AdminView extends VerticalLayout {


  public AdminView() {
    setId("admin");
    setSizeFull();

    final Map<String, Store> allStores = StoreKeeper.getAllStores();
    final Grid<Store> grid = new Grid<>();
    grid.setItems(allStores.values());

    grid.addColumn(Store::getId, "Code");
    grid.addColumn(store -> store.getPersonalData().getName(), "Nachname");
    grid.addColumn(store -> store.getPersonalData().getFirstname(), "Vorname");
    grid.addColumn(new ComponentRenderer<>((SerializableFunction<Store, Button>) store -> {
      final Button button = new Button("Registrierung aktivieren");
      button.setEnabled(store.hasRegistered());
      button.addClickListener(event -> {
        store.isHasRegistered(false);
        grid.getDataProvider().refreshAll();
      });
      return button;
    }));

    add(grid);

    addDownloadButton();
  }

  private void addDownloadButton() {
    final Button downloadBtn = UIUtils.createPrimaryButton("Zwischenablage kopieren");
    ClipboardHelper clipboardHelper = new ClipboardHelper(getStoreAsBytes(), downloadBtn);
    add(clipboardHelper);
    downloadBtn.addClickListener(event -> Notification.show("kopiert"));
  }

  private String getStoreAsBytes() {
    try {
      final Map<String, Store> allStores = StoreKeeper.getAllStores();
      final ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(allStores);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }


}
