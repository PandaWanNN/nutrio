package ch.nutrio.ui.views.start;

import ch.nutrio.data.Store;
import ch.nutrio.data.StoreKeeper;
import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.util.UIUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@CssImport("./styles/components/login.css")
@PageTitle("Nutrio")
@Route(value = "", layout = StartLayout.class)
public class LoginView extends HorizontalLayout implements BeforeEnterObserver {

  private static final String CLASS_NAME = "login";
  private Binder<Object> binder;

  public LoginView() {
    setId("login");

    setSizeFull();

    binder = new Binder<>();
    binder.setBean(new Object());

    final VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.setHeightFull();
    add(mainLayout);
    setVerticalComponentAlignment(Alignment.CENTER, mainLayout);

    final Label nutrioLbl = UIUtils.createH1Label("nutrio");
    mainLayout.add(nutrioLbl);
    mainLayout.setHorizontalComponentAlignment(Alignment.CENTER, nutrioLbl);

    final Component loginLayout = createContent();
    mainLayout.add(loginLayout);
    mainLayout.setHorizontalComponentAlignment(Alignment.CENTER, loginLayout);
  }

  private Component createContent() {
    final VerticalLayout contentLayout = new VerticalLayout();

    final TextField emailField = new TextField("E-Mail");
    emailField.setPrefixComponent(new Icon(VaadinIcon.ENVELOPE));
    final PasswordField passwordField = new PasswordField("Aktivierungscode / Passwort");
    passwordField.setPrefixComponent(new Icon(VaadinIcon.LOCK));
    final Button loginButton = UIUtils.createPrimaryButton("Login");
    final Button registerButton = UIUtils.createTertiaryButton("Registrieren");

    final Anchor link = new Anchor();
    link.addClassName(CLASS_NAME + "__anchor");
    link.setText("Wo finde ich den Aktivierungscode?");
    link.getElement().addEventListener("click", e -> {
      final Notification notification = Notification.show("Den Aktivierungscode erhälst du von deiner Beratungsperson.");
      notification.setPosition(Notification.Position.MIDDLE);
    });

    emailField.setWidthFull();
    passwordField.setWidthFull();
    loginButton.setWidthFull();
    registerButton.setWidthFull();

    contentLayout.add(emailField);
    contentLayout.add(passwordField);
    contentLayout.add(link);
    contentLayout.add(loginButton);
    contentLayout.add(registerButton);

    loginButton.addClickListener(buttonClickEvent -> {
      if (binder.validate().isOk()) {
        navigateAfterLogin(passwordField);
      }
    });
    registerButton.addClickListener(buttonClickEvent -> navigateToRegistration());

    binder.forField(emailField)
          .withValidator(new EmailValidator("Bitte geben sie eine gültige E-Mailadresse ein"))
          .asRequired("Bitte geben sie eine E-Mailadresse an")
          .bind(store -> "", (store, s) -> {
          });

    binder.forField(passwordField)
          .withValidator(StoreKeeper::storeExists, "Ungültige Code/Passwort")
          .asRequired("Bitte geben sie einen Code/Passwort ein")
          .bind(store -> "", (store, s) -> {
          });

    return contentLayout;
  }

  private void navigateToRegistration() {
    final Store store = new Store(StoreKeeper.generateStoreId());
    StoreKeeper.addStore(store);
    StoreKeeper.setStoreIdOnSession(store.getId());
    UI.getCurrent().navigate("registration");
  }

  private void navigateAfterLogin(final PasswordField passwordField) {
    final String storeId = passwordField.getValue();
    StoreKeeper.setStoreIdOnSession(storeId);

    if (StoreKeeper.storeExists(storeId)) {
      if (StoreKeeper.getStore(storeId).hasRegistered()) {
        UI.getCurrent().navigate("home");
      } else {
        UI.getCurrent().navigate("registration");
      }
    }
  }

  @Override
  public void beforeEnter(final BeforeEnterEvent event) {
    StoreKeeper.getStoreIdForSession().ifPresent(s -> event.forwardTo("home"));
  }
}
