package ch.nutrio.ui.views.content;

import ch.nutrio.data.StoreKeeper;
import ch.nutrio.ui.MainLayout;
import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.Header;
import ch.nutrio.ui.util.UIUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Einstellungen")
@Route(value = "settings", layout = StartLayout.class)
public class SettingsView extends FlexLayout {

  public SettingsView() {
    setId("settings");

    setSizeFull();
    setFlexDirection(FlexLayout.FlexDirection.COLUMN);

    final Component header = initHeaderContainer();

    final VerticalLayout contentLayout = new VerticalLayout();
    contentLayout.setSizeFull();
    contentLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

    add(header);
    add(contentLayout);

    final Button logoutBtn = UIUtils.createPrimaryButton("Abmelden");
    logoutBtn.setWidthFull();
    logoutBtn.addClickListener(event -> {
      StoreKeeper.clearSession();
      UI.getCurrent().navigate("");
    });
    contentLayout.add(logoutBtn);
  }

  private Component initHeaderContainer() {
    return Header.create("Meine Einstellungen",
                         Header.EMPTY_BUTTON_ICON,
                         new Header.ButtonIcon(VaadinIcon.ANGLE_LEFT, () -> UI.getCurrent().getPage().getHistory().back()));
  }

}
