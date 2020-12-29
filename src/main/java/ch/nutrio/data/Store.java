package ch.nutrio.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vaadin.flow.component.icon.VaadinIcon;

public class Store implements Serializable {

  @JsonProperty
  private String id;
  @JsonProperty
  private PersonalData personalData;
  @JsonProperty
  private LinkedList<BodyData> bodyDataList;
  @JsonProperty
  private List<TargetData> targetDataList;
  @JsonProperty
  private LinkedList<TimeLineRecord> timeLineRecordList;
  @JsonProperty
  private List<AppointmentData> appointmentDataList;
  @JsonProperty
  private boolean hasRegistered;

  public Store(final String id) {
    this.id = id;
    personalData = new PersonalData();
    bodyDataList = new LinkedList<>();
    targetDataList = new ArrayList<>();
    timeLineRecordList = new LinkedList<>();
    appointmentDataList = new ArrayList<>();
  }

  public Store() {
  }

  public String getId() {
    return id;
  }

  public PersonalData getPersonalData() {
    return personalData;
  }

  public boolean hasRegistered() {
    return hasRegistered;
  }

  public void isHasRegistered(final boolean hasRegistered) {
    this.hasRegistered = hasRegistered;
    if (hasRegistered) {
      final boolean hasTimeLineRecord = getTimeLineRecordList().stream()
                                                               .anyMatch(timeLineRecord -> VaadinIcon.HEART.equals(timeLineRecord.getIcon()));

      if (!hasTimeLineRecord) {
        final TimeLineRecord timeLineRecord = new TimeLineRecord(VaadinIcon.HEART,
                                                                 "red",
                                                                 "Herzlich Willkommen " + StoreKeeper.getStoreForSession().getPersonalData().getFirstname(),
                                                                 "",
                                                                 LocalDate.now());
        addTimeLineRecord(timeLineRecord);
      }
    }
  }

  public void addBodyData(final BodyData bodyData) {
    bodyDataList.addFirst(bodyData);
  }

  public List<BodyData> getBodyDataList() {
    return Collections.unmodifiableList(bodyDataList);
  }

  public void addTargetData(final TargetData targetData) {
    targetDataList.add(targetData);
    final TimeLineRecord timeLineRecord = new TimeLineRecord(VaadinIcon.CROSSHAIRS, "gold", "Neues Ziel hinzugefügt", targetData.getTitle(), LocalDate.now());
    addTimeLineRecord(timeLineRecord);
  }

  public List<TargetData> getTargetDataList() {
    return Collections.unmodifiableList(targetDataList);
  }

  public void addTimeLineRecord(final TimeLineRecord timeLineRecord) {
    timeLineRecordList.addFirst(timeLineRecord);
  }

  public List<TimeLineRecord> getTimeLineRecordList() {
    return Collections.unmodifiableList(timeLineRecordList);
  }

  public void removeTarget(final TargetData targetData) {
    targetDataList.remove(targetData);
  }

  public void addAppointmentData(final AppointmentData appointmentData) {
    appointmentDataList.add(appointmentData);
    final TimeLineRecord timeLineRecord = new TimeLineRecord(VaadinIcon.CALENDAR_CLOCK,
                                                             "cornflowerblue",
                                                             "Neuer Termin",
                                                             appointmentData.getDisplayLocation(),
                                                             LocalDate.parse(appointmentData.getDate()));
    addTimeLineRecord(timeLineRecord);
  }

  public List<AppointmentData> getAppointmentDataList() {
    return Collections.unmodifiableList(appointmentDataList);
  }

  public void removeAppointment(final AppointmentData appointmentData) {
    appointmentDataList.remove(appointmentData);
  }
}
