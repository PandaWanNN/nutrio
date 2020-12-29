package ch.nutrio.data;

import java.io.Serializable;

public class BodyData implements Serializable {

  private Integer weight;
  private Integer height;
  private Integer tail;
  private Integer breast;
  private Integer hips;
  private Integer arm;
  private Integer leg;

  public BodyData(final Integer weight, final Integer height) {
    this.weight = weight;
    this.height = height;
  }

  public BodyData() {
  }

  public BodyData copy() {
    final BodyData bodyData = new BodyData();
    bodyData.setWeight(this.weight);
    bodyData.setHeight(this.height);
    bodyData.setTail(this.tail);
    bodyData.setBreast(this.breast);
    bodyData.setHips(this.hips);
    bodyData.setArm(this.arm);
    bodyData.setLeg(this.leg);
    return bodyData;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(final Integer weight) {
    this.weight = weight;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(final Integer height) {
    this.height = height;
  }

  public Integer getTail() {
    return tail;
  }

  public void setTail(final Integer tail) {
    this.tail = tail;
  }

  public Integer getBreast() {
    return breast;
  }

  public void setBreast(final Integer breast) {
    this.breast = breast;
  }

  public Integer getHips() {
    return hips;
  }

  public void setHips(final Integer hips) {
    this.hips = hips;
  }

  public Integer getArm() {
    return arm;
  }

  public void setArm(final Integer arm) {
    this.arm = arm;
  }

  public Integer getLeg() {
    return leg;
  }

  public void setLeg(final Integer leg) {
    this.leg = leg;
  }
}
