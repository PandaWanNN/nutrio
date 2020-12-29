package ch.nutrio.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@CssImport("./styles/components/footer.css")
public class Footer {

  private static final String CLASS_NAME = "footer";

  public static HorizontalLayout create(final String selectedView) {
    final FlexLayout footer = new FlexLayout();

    footer.setClassName(CLASS_NAME);
    footer.setFlexDirection(FlexLayout.FlexDirection.ROW);
    footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    footer.setAlignItems(FlexComponent.Alignment.CENTER);
    footer.setAlignContent(FlexLayout.ContentAlignment.SPACE_BETWEEN);

    final Icon home = createIcon(VaadinIcon.DASHBOARD, "home", selectedView);
    final Icon dataIcon = createIcon(VaadinIcon.DATABASE, "data", selectedView);
    final Icon appointmentsIcon = createIcon(VaadinIcon.DOCTOR, "praxis", selectedView);
    final Icon settingsIcon = createIcon(VaadinIcon.BOOK, "knowledge", selectedView);

    footer.add(home);
    footer.add(dataIcon);
    footer.add(initAvatar());
    footer.add(appointmentsIcon);
    footer.add(settingsIcon);

    final HorizontalLayout layout = new HorizontalLayout();
    layout.addClassName(CLASS_NAME + "__container");
    layout.add(footer);
    return layout;
  }

  private static Icon createIcon(final VaadinIcon dashboard, final String viewName, final String selectedView) {
    final Icon icon = new Icon(dashboard);
    if (selectedView.equals(viewName)) {
      icon.addClassName(CLASS_NAME + "__icon__selected");
    } else {
      icon.addClassName(CLASS_NAME + "__icon");
    }
    icon.addClickListener(iconClickEvent -> {
      UI.getCurrent().navigate(viewName);
    });
    return icon;
  }

  private static Icon initAvatar() {
    final Icon icon = new Icon(VaadinIcon.PLUS);
    icon.addClassName(CLASS_NAME + "__avatar");
    ContextMenu contextMenu = new ContextMenu(icon);
    contextMenu.setOpenOnClick(true);
    contextMenu.addItem("Gewicht eintragen",
                        e -> UI.getCurrent().navigate("newData"));
    contextMenu.addItem("Termin erstellen",
                        e -> UI.getCurrent().navigate("newAppointment"));
    contextMenu.addItem("Ziel erstellen",
                        e -> UI.getCurrent().navigate("newTarget"));

    return icon;
  }

}
