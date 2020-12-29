package ch.nutrio.ui.views.content;


import java.time.LocalDate;
import java.util.List;

import ch.nutrio.data.AppointmentData;
import ch.nutrio.data.StoreKeeper;
import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.FlexBoxLayout;
import ch.nutrio.ui.components.Header;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@CssImport("./styles/components/newAppointment.css")
@PageTitle("Neues Ziel")
@Route(value = "newAppointment", layout = StartLayout.class)
public class NewAppointmentView extends FlexLayout {

  private static final String CLASS_NAME = "newAppointment";
  private final Binder<AppointmentData> binder;

  public NewAppointmentView() {
    setId("newAppointment");
    setClassName(CLASS_NAME);
    setSizeFull();

    setFlexDirection(FlexDirection.COLUMN);

    final Component header = initHeaderContainer();

    final FlexBoxLayout contentLayout = new FlexBoxLayout();
    contentLayout.addClassName(CLASS_NAME + "__content");
    contentLayout.setFlexDirection(FlexDirection.COLUMN);

    add(header);
    add(contentLayout);

    final List<String> personList = List.of("Prof. Dr med. Bernd Schultes",
                                            "Dr med. Caroline Christoffel-Courtin",
                                            "Dr med. Vasiliki Bouronikou",
                                            "Dr. med. Wolfgang Nagel",
                                            "Dr. med. Stefan Frei",
                                            "Doris Giselbrecht");

    binder = new Binder<>();
    binder.setBean(new AppointmentData());

    final LocalDate today = LocalDate.now();
    final ComboBox<String> personSelection = new ComboBox<>("Behandler", personList);
    final Select<String> timeList = new Select<>(getAvailableTimes(today.getDayOfMonth()));
    timeList.setLabel("Verfügbare Termine");

    final DatePicker datePicker = new DatePicker("Datum", event -> {
      timeList.setItems(getAvailableTimes(event.getValue().getDayOfMonth()));
      timeList.clear();
      binder.getBean().setDate(event.getValue().toString());
    });

    final Select<String> online = new Select<>("Vor Ort", "Online");
    online.setLabel("Durchführung");
    online.setValue("Vor Ort");

    contentLayout.add(personSelection, datePicker, timeList, online);

    binder.forField(personSelection)
          .asRequired("Bitte wählen Sie einen Behandler aus")
          .bind(AppointmentData::getSpecialist, AppointmentData::setSpecialist);
    binder.forField(timeList)
          .asRequired("Bitte wählen Sie eine Zeit aus")
          .bind(AppointmentData::getTime, AppointmentData::setTime);
    binder.forField(datePicker)
          .asRequired("Bitte wählen Sie ein Datum aus")
          .bind(appointmentData -> {
            final String date = appointmentData.getDate();
            if (date == null || date.isEmpty()) {
              return LocalDate.now();
            }
            return LocalDate.parse(date);
          }, (appointmentData1, time) -> appointmentData1.setDate(time.toString()));
    binder.forField(online)
          .asRequired("Bitte aus, ob Sie den Termin vor Ort oder online buchen möchten")
          .bind(AppointmentData::getLocation, AppointmentData::setLocation);

  }

  private String[] getAvailableTimes(final int dayOfMonth) {
    if (dayOfMonth % 2 == 0) {
      return List.of("10.00 - 11.00 Uhr", "13.00 - 14.00 Uhr", "17.00 - 18.00 Uhr").toArray(new String[]{});
    } else {
      return List.of("08.00 - 09.00 Uhr", "15.00 - 16.00 Uhr").toArray(new String[]{});
    }
  }

  private Component initHeaderContainer() {
    return Header.create("Neuer Termin",
                         new Header.Button("Buchen", () -> {
                           if (binder.validate().isOk()) {
                             StoreKeeper.getStoreForSession().addAppointmentData(binder.getBean());
                             UI.getCurrent().getPage().getHistory().back();
                             Notification.show("Termin wurde gebucht", 2000, Notification.Position.BOTTOM_END);
                           }
                         }),
                         new Header.Button("Abbrechen", () -> UI.getCurrent().getPage().getHistory().back()));
  }


}
