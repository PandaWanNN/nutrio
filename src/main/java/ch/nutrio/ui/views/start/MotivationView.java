package ch.nutrio.ui.views.start;

import java.util.HashMap;

import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.Header;
import ch.nutrio.ui.util.UIUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

@CssImport("./styles/components/motivation.css")
@PageTitle("Nutrio")
@Route(value = "motivation", layout = StartLayout.class)
public class MotivationView extends HorizontalLayout {

  public static final String WEIGHT_PARAMETER = "weight";
  public static final String HEALTH_PARAMETER = "health";
  public static final String PARAMETER_KEY = "motivation";
  private static final String CLASS_NAME = "motivation";
  private String selection;
  private VerticalLayout button1;
  private VerticalLayout button2;

  public MotivationView() {
    setId("motivation");
    setClassName(CLASS_NAME);

    setSizeFull();

    final VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.setSizeFull();
    mainLayout.setPadding(false);

    final Header.Button nextBtn = new Header.Button("Weiter", this::navigate);
    final Header.Button skipBtn = new Header.Button("Überspringen", () -> UI.getCurrent().navigate("applehealth"));
    mainLayout.add(Header.create("Motivation", nextBtn, skipBtn));
    final Div div = new Div();
    div.setHeight("8px");
    final Label h2Label = UIUtils.createH2Label("Was ist deine Motivation?");
    mainLayout.add(div);
    mainLayout.add(h2Label);
    mainLayout.add(createContent());
    mainLayout.setHorizontalComponentAlignment(Alignment.CENTER, h2Label);

    add(mainLayout);
  }

  private Component createContent() {
    final VerticalLayout contentLayout = new VerticalLayout();
    contentLayout.addClassName(CLASS_NAME + "__content");
    contentLayout.setSizeFull();

    button1 = createButton("Gewicht verlieren", VaadinIcon.TRENDIND_DOWN, WEIGHT_PARAMETER);
    button2 = createButton("Gesünder leben", VaadinIcon.EXIT, HEALTH_PARAMETER);
    contentLayout.add(button1);
    contentLayout.add(button2);

    contentLayout.setHorizontalComponentAlignment(Alignment.CENTER, button1);
    contentLayout.setHorizontalComponentAlignment(Alignment.CENTER, button2);

    return contentLayout;
  }

  private void navigate() {
    if (selection != null) {
      final HashMap<String, String> parameters = new HashMap<>();
      parameters.put(PARAMETER_KEY, selection);
      UI.getCurrent().navigate("targets", QueryParameters.simple(parameters));
    }
  }

  private VerticalLayout createButton(final String text, final VaadinIcon vaadinIcon, final String selectionName) {
    final VerticalLayout layout = new VerticalLayout();
    layout.addClassName(CLASS_NAME + "__button");
    layout.setWidthFull();
    final HorizontalLayout textLayout = new HorizontalLayout();
    textLayout.setHeightFull();
    layout.add(textLayout);
    layout.setHorizontalComponentAlignment(Alignment.CENTER, textLayout);

    final Icon icon = new Icon(vaadinIcon);
    final Label label = new Label(text);
    icon.addClassName(CLASS_NAME + "__button__label");
    label.addClassName(CLASS_NAME + "__button__label");
    textLayout.add(icon);
    textLayout.add(label);
    textLayout.setVerticalComponentAlignment(Alignment.CENTER, icon);
    textLayout.setVerticalComponentAlignment(Alignment.CENTER, label);

    layout.getElement().addEventListener("click", event -> {
      if (selectionName.equals(selection)) {
        layout.removeClassNames(CLASS_NAME + "__button__selected");
        layout.addClassName(CLASS_NAME + "__button");
        selection = null;
      } else {
        button1.removeClassNames(CLASS_NAME + "__button__selected");
        button2.removeClassNames(CLASS_NAME + "__button__selected");
        layout.addClassName(CLASS_NAME + "__button__selected");
        selection = selectionName;

      }
    });


    return layout;
  }

}
