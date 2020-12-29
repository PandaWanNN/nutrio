package ch.nutrio.ui.components;

import java.time.LocalDate;

import ch.nutrio.data.StoreKeeper;
import ch.nutrio.data.TargetData;
import ch.nutrio.data.TimeLineRecord;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./styles/components/targets.css")
public class Targets extends VerticalLayout {

  private static final String CLASS_NAME = "targets";

  public Targets() {
    setId("targets");
    setSizeFull();
    initContent();
    setClassName(CLASS_NAME);
  }

  private void initContent() {
    StoreKeeper.getStoreForSession()
               .getTargetDataList()
               .forEach(this::addTarget);
  }

  private void addTarget(final TargetData targetData) {
    final ListItem item = new ListItem(targetData.getTitle(), targetData.getDescription(), targetData.isDone());
    final Hr line = new Hr();
    line.addClassName(CLASS_NAME + "__line");
    add(item);
    add(line);

    ContextMenu contextMenu = new ContextMenu(item);
    contextMenu.setOpenOnClick(true);
    contextMenu.addItem("Erledigt", event -> {
      targetData.setDone(true);
      final TimeLineRecord timeLineRecord = new TimeLineRecord(VaadinIcon.CHECK, "green", "Ziel erledigt", targetData.getTitle(), LocalDate.now());
      StoreKeeper.getStoreForSession().addTimeLineRecord(timeLineRecord);
      reInitialize();
    });
    contextMenu.addItem("Löschen", event -> {
      StoreKeeper.getStoreForSession().removeTarget(targetData);
      reInitialize();
      Notification.show("Ziel wurde gelöscht");
    });
  }

  public void reInitialize() {
    removeAll();
    initContent();
  }

}
