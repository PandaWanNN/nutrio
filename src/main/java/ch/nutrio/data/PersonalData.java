package ch.nutrio.data;

import java.io.Serializable;
import java.time.LocalDate;

public class PersonalData implements Serializable {

  private String email;
  private String gender;
  private String firstname;
  private String name;
  private String birthdate;
  private String phone;
  private String password;
  private boolean isSmoker;
  private Double workout;

  public PersonalData() {
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(final String gender) {
    this.gender = gender;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(final String firstname) {
    this.firstname = firstname;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(final String birthdate) {
    this.birthdate = birthdate;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(final String phone) {
    this.phone = phone;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public boolean isSmoker() {
    return isSmoker;
  }

  public void setSmoker(final boolean smoker) {
    isSmoker = smoker;
  }

  public Double getWorkout() {
    return workout;
  }

  public void setWorkout(final Double workout) {
    this.workout = workout;
  }
}
