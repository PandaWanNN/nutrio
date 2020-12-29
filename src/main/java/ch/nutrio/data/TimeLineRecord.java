package ch.nutrio.data;

import java.io.Serializable;
import java.time.LocalDate;

import com.vaadin.flow.component.icon.VaadinIcon;

public class TimeLineRecord implements Serializable {

  private VaadinIcon icon;
  private String color;
  private String title;
  private String description;
  private String date;

  public TimeLineRecord(final VaadinIcon icon, final String color, final String title, final String description, final LocalDate date) {
    this.icon = icon;
    this.color = color;
    this.title = title;
    this.description = description;
    this.date = date.toString();
  }

  public TimeLineRecord() {
  }

  public VaadinIcon getIcon() {
    return icon;
  }

  public String getColor() {
    return color;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getDate() {
    return date;
  }
}
