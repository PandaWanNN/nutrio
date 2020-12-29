package ch.nutrio.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AppointmentData {

  private String specialist;
  private String date;
  private String time;
  private String location;

  public String getSpecialist() {
    return specialist;
  }

  public void setSpecialist(final String specialist) {
    this.specialist = specialist;
  }

  public String getDate() {
    return date;
  }

  public void setDate(final String date) {
    this.date = date;
  }

  public String getTime() {
    return time;
  }

  public void setTime(final String time) {
    this.time = time;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(final String location) {
    this.location = location;
  }

  @JsonIgnore
  public String getDisplayLocation() {
    if(location.equalsIgnoreCase("online")) {
      return location;
    }
    return "Lerchenstrasse 21, 9016 St. Gallen";
  }
}
