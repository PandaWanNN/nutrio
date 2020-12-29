package ch.nutrio.data;

import java.io.Serializable;

public class TargetData implements Serializable {

  private String title;
  private String description;
  private boolean done;

  public TargetData(final String title, final String description) {
    this.title = title;
    this.description = description;
  }

  public TargetData() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public boolean isDone() {
    return done;
  }

  public void setDone(final boolean done) {
    this.done = done;
  }
}
