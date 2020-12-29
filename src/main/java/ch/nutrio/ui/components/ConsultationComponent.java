package ch.nutrio.ui.components;

import java.time.LocalDate;
import java.util.List;

import ch.nutrio.data.AppointmentData;
import ch.nutrio.data.StoreKeeper;
import ch.nutrio.ui.util.UIUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;

@CssImport("./styles/components/consultation.css")
public class ConsultationComponent {

  private static final String CLASS_NAME = "consultation";
  private FlexLayout content;

  private ConsultationComponent() {
  }

  public static Component create() {
    return new ConsultationComponent().get();
  }

  private Component get() {

    content = new FlexLayout();
    content.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
    content.setClassName(CLASS_NAME);

    reinitialize();

    final Scroller scroller = new Scroller();
    scroller.setContent(content);
    scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);

    return scroller;
  }

  private void reinitialize() {
    content.removeAll();
    final H3 title = new H3("Ihre nächste Sprechstunde");
    content.add(title);
    content.setAlignSelf(FlexComponent.Alignment.CENTER, title);

    final List<AppointmentData> appointmentDataList = StoreKeeper.getStoreForSession().getAppointmentDataList();
    appointmentDataList.forEach(appointmentData -> {
      content.add(createItem(appointmentData));
      content.add(createHr());
    });

    if (appointmentDataList.isEmpty()) {
      final Label label = new Label("Keine anstehenden Termine");
      content.add(label);
      content.setAlignSelf(FlexComponent.Alignment.CENTER, label);
    }
  }

  private Hr createHr() {
    final Hr line = new Hr();
    line.addClassName(CLASS_NAME + "__line");
    return line;
  }

  private ListItem createItem(final AppointmentData appointmentData) {
    final LocalDate date = LocalDate.parse(appointmentData.getDate());
    final ListItem item = new ListItem(UIUtils.formatDate(date) + ", " + appointmentData.getTime(), appointmentData.getDisplayLocation());

    if (appointmentData.getLocation().equals("online")) {
      item.setPrefix(new Icon(VaadinIcon.MOBILE_BROWSER));
      item.setSuffix(new Icon(VaadinIcon.EXTERNAL_LINK));
    } else {
      item.setPrefix(new Icon(VaadinIcon.BUILDING_O));
    }

    addContextMenu(item, appointmentData);

    return item;
  }

  private void addContextMenu(final ListItem item, final AppointmentData appointmentData) {
    ContextMenu contextMenu = new ContextMenu(item);
    contextMenu.setOpenOnClick(true);
    if (appointmentData.getLocation().equalsIgnoreCase("online")) {
      contextMenu.addItem("Teilnehmen", event -> Notification.show("Videocall wird gestartet..."));
    }
    contextMenu.addItem("Stornieren",
                        e -> {
                          StoreKeeper.getStoreForSession().removeAppointment(appointmentData);
                          reinitialize();
                          Notification.show("Der Termin wurde storniert", 3000,
                                            Notification.Position.BOTTOM_CENTER);
                        });
  }
}
