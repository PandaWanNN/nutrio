package ch.nutrio.ui.views.start;

import java.time.LocalDate;
import java.util.List;

import ch.nutrio.data.Store;
import ch.nutrio.data.StoreKeeper;
import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.Header;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Nutrio")
@Route(value = "registration", layout = StartLayout.class)
public class RegistrationView extends HorizontalLayout {

  private final Binder<Store> binder;

  public RegistrationView() {
    setId("registration");
    setSizeFull();

    final Store store = StoreKeeper.getStoreForSession();
    binder = new Binder<>();
    binder.setBean(store);

    final VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.setPadding(false);

    final Header.Button finishButton = new Header.Button("Fertig", () -> {
      if (binder.validate().isOk()) {
        UI.getCurrent().navigate("personalqst");
      }
    });
    mainLayout.add(Header.create("Registration", finishButton, Header.EMPTY_BUTTON));
    mainLayout.add(createContent());

    final Scroller scroller = new Scroller(mainLayout);
    scroller.setSizeFull();
    scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
    add(scroller);
  }

  private static void setPhoneType(TextField textField) {
    textField.getElement().getNode().runWhenAttached(ui -> ui.getPage().executeJs("$0.focusElement.type=$1", textField, "tel"));
  }

  private Component createContent() {
    final VerticalLayout contentLayout = new VerticalLayout();
    contentLayout.setSizeFull();

    final Select<String> gender = new Select<>();
    gender.setItems("Männlich", "Weiblich", "Keine Angaben");
    gender.setPlaceholder("Geschlecht");

    final TextField firstname = createField("Vorname");
    final TextField name = createField("Name");
    final EmailField email = getEmailField();
    final DatePicker birthDate = createDatePicker();
    final TextField phone = createNumberField();
    final PasswordField password = createPasswordField("Passwort");
    final PasswordField passwordValidation = createPasswordField("Passwort wiederholen");

    List<Component> components = List.of(gender, firstname, name, email, birthDate, phone, password, passwordValidation);

    components.forEach(component -> {
      if (component instanceof HasSize) {
        ((HasSize) component).setWidthFull();
      }
      contentLayout.add(component);
    });

    binder.forField(gender)
          .bind(store1 -> store1.getPersonalData().getGender(), (store1, s) -> store1.getPersonalData().setGender(s));

    binder.forField(firstname)
          .asRequired("bitte geben Sie den Vornamen an")
          .bind(store1 -> store1.getPersonalData().getFirstname(), (store1, s) -> store1.getPersonalData().setFirstname(s));

    binder.forField(name)
          .asRequired("bitte geben Sie den Nachnamen an")
          .bind(store1 -> store1.getPersonalData().getName(), (store1, s) -> store1.getPersonalData().setName(s));

    binder.forField(email)
          .asRequired("bitte geben Sie die E-MailAdresse an")
          .bind(store1 -> store1.getPersonalData().getEmail(), (store1, s) -> store1.getPersonalData().setEmail(s));

    binder.forField(birthDate)
          .bind(store1 -> {
            final String birthdate = store1.getPersonalData().getBirthdate();
            if (birthdate != null && !birthdate.isEmpty()) {
              return LocalDate.parse(birthdate);
            } else {
              return null;
            }
          }, (store1, s) -> {
            if (s != null) {
              store1.getPersonalData().setBirthdate(s.toString());
            } else {
              store1.getPersonalData().setBirthdate("");
            }
          });

    binder.forField(phone)
          .bind(store1 -> store1.getPersonalData().getPhone(), (store1, s) -> store1.getPersonalData().setPhone(s));

    final String passwordError = "Die Passworteingaben stimmen nicht überein";
    binder.forField(password)
          .asRequired("bitte geben Sie ein Passwort ein")
          .withValidator(getPasswordValidator(password, passwordValidation), passwordError)
          .bind(store1 -> store1.getPersonalData().getPassword(), (store1, s) -> store1.getPersonalData().setPassword(s));

    binder.forField(passwordValidation)
          .asRequired("bitte geben Sie ein Passwort ein")
          .withValidator(getPasswordValidator(passwordValidation, password), passwordError)
          .bind(store1 -> "", (store1, s) -> {
          });

    return contentLayout;
  }

  private SerializablePredicate<String> getPasswordValidator(final PasswordField password, final PasswordField passwordValidation) {
    return s -> {
      final boolean validationEmpty = passwordValidation.getValue() == null || passwordValidation.getValue().isEmpty();
      if (!validationEmpty) {
        return passwordValidation.getValue().equals(password.getValue());
      }
      return true;
    };
  }

  private PasswordField createPasswordField(final String passwort) {
    final PasswordField passwordField = new PasswordField("", passwort);
    passwordField.setPrefixComponent(new Icon(VaadinIcon.LOCK));
    return passwordField;
  }

  private TextField createNumberField() {
    final TextField numberField = new TextField("", "Telefon");
    setPhoneType(numberField);
    numberField.setPrefixComponent(new Icon(VaadinIcon.PHONE));
    return numberField;
  }

  private DatePicker createDatePicker() {
    final DatePicker birthDate = new DatePicker();
    birthDate.setPlaceholder("Geburtsdatum");
    return birthDate;
  }

  private EmailField getEmailField() {
    final EmailField email = new EmailField("", "E-Mail");
    email.setPrefixComponent(new Icon(VaadinIcon.ENVELOPE));
    return email;
  }

  private TextField createField(final String name) {
    final TextField firstname = new TextField("", name);
    firstname.setPrefixComponent(new Icon(VaadinIcon.USER));
    return firstname;
  }

}
