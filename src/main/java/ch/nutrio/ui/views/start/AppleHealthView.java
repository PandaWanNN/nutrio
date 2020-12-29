package ch.nutrio.ui.views.start;

import ch.nutrio.data.StoreKeeper;
import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.Header;
import ch.nutrio.ui.util.UIUtils;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@CssImport("./styles/components/applehealth.css")
@PageTitle("Nutrio")
@Route(value = "applehealth", layout = StartLayout.class)
public class AppleHealthView extends HorizontalLayout {

  private static final String CLASS_NAME = "applehealth";
  private ToggleButton toggleButton;

  public AppleHealthView() {
    setId("applehealth");

    setSizeFull();

    final VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.setPadding(false);

    final Header.Button nextBtn = new Header.Button("Fertig", () -> {
      StoreKeeper.getStoreForSession().isHasRegistered(true);
      UI.getCurrent().navigate("home");
      if (toggleButton.getValue()) {
        Notification.show("Die Daten aus Apple Health wurden verknüpft");
      }
    });
    mainLayout.add(Header.create("Persönliche Angaben", nextBtn, Header.EMPTY_BUTTON));
    mainLayout.add(createContent());

    add(mainLayout);
  }

  private Component createContent() {
    final VerticalLayout contentLayout = new VerticalLayout();
    contentLayout.setSizeFull();

    final Label nutrioLbl = UIUtils.createH1Label("nutrio");

    final Icon icon = new Icon(VaadinIcon.REFRESH);
    icon.addClassName(CLASS_NAME + "__icon");

    toggleButton = new ToggleButton("Apple Health");

    final Label description = new Label("Erlaubt nutrio den Zugriff auf die relevante Gesundheitsdaten von Apple Health");
    description.addClassName(CLASS_NAME + "__description");
    final Label hint = new Label("(Kann später jederzeit angepasst werden)");
    hint.addClassName(CLASS_NAME + "__description");

    contentLayout.add(nutrioLbl, icon, toggleButton, description, hint);
    contentLayout.setHorizontalComponentAlignment(Alignment.CENTER, nutrioLbl, icon);

    return contentLayout;
  }

}
