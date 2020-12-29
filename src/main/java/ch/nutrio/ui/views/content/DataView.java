package ch.nutrio.ui.views.content;


import java.util.List;

import ch.nutrio.data.StoreKeeper;
import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.FlexBoxLayout;
import ch.nutrio.ui.components.Footer;
import ch.nutrio.ui.components.Header;
import ch.nutrio.ui.util.UIUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@CssImport("./styles/components/data.css")
@CssImport(value = "./styles/vaadin-button-styles.css", themeFor = "vaadin-button")
@PageTitle("Meine Daten")
@Route(value = "data", layout = StartLayout.class)
public class DataView extends FlexLayout {

  private static final String CLASS_NAME = "data";
  private final FlexBoxLayout contentLayout;
  private final VerticalLayout bodyLayout;
  private final VerticalLayout nutrioLayout;
  private final VerticalLayout documentLayout;
  private Button bodyBtn;
  private Button nutrionBtn;
  private Button documentBtn;

  public DataView() {
    setId("data");
    setClassName(CLASS_NAME);
    setSizeFull();

    setFlexDirection(FlexDirection.COLUMN);

    final Component header = initHeaderContainer();
    final HorizontalLayout footer = Footer.create(CLASS_NAME);

    contentLayout = new FlexBoxLayout();
    contentLayout.addClassName(CLASS_NAME + "__content");
    contentLayout.setFlexDirection(FlexDirection.COLUMN);

    final FlexLayout tabLayout = initTabs();
    contentLayout.add(tabLayout);
    contentLayout.setAlignSelf(Alignment.CENTER, tabLayout);

    bodyLayout = new VerticalLayout();
    bodyLayout.setSizeFull();
    nutrioLayout = new VerticalLayout();
    documentLayout = new VerticalLayout();
    contentLayout.add(bodyLayout);

    initBody();
    initNutrio();
    initDocument();

    add(header);
    add(contentLayout);
    add(footer);
  }

  private void initNutrio() {
    final Icon icon = new Icon(VaadinIcon.CROSS_CUTLERY);
    icon.addClassName(CLASS_NAME + "__nutrio__icon");
    final Label description = new Label("Um Ernährungsdaten zu erfassen bitte eine App aus dem AppStore herunterladen und verknüpfen.");
    description.addClassName(CLASS_NAME + "__nutrio__description");
    final Button button = UIUtils.createPrimaryButton("Zum Appstore", VaadinIcon.EXTERNAL_LINK);
    button.addClickListener(event -> Notification.show("App Store würde jetzt geöffnet werden :-D"));

    nutrioLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    nutrioLayout.add(icon, description, button);
  }

  private void initBody() {
    StoreKeeper.getStoreForSession().getBodyDataList().stream().findFirst().ifPresentOrElse(bodyData -> {
      final List<Component> fields = List.of(
          createField("Gewicht", "kg", getValue(bodyData.getWeight())),
          createField("Grösse", "cm", getValue(bodyData.getHeight())),
          createField("Taillenumfang", "cm", getValue(bodyData.getTail())),
          createField("Brustumfang", "cm", getValue(bodyData.getBreast())),
          createField("Hüftumfang", "cm", getValue(bodyData.getHips())),
          createField("Armumfang", "cm", getValue(bodyData.getArm())),
          createField("Beinumfang", "cm", getValue(bodyData.getLeg())
          ));

      final Component bmiField = createField("", "BMI", calculateBMI(bodyData.getWeight(), bodyData.getHeight()));

      fields.forEach(bodyLayout::add);
      bodyLayout.add(new Hr());
      bodyLayout.add(bmiField);
    }, () -> bodyLayout.add(new Label("Es wurden noch keine Daten erfasst")));

  }

  private String getValue(final Integer value) {
    if (value != null) {
      return value.toString();
    }

    return "";
  }

  private String calculateBMI(final Integer weight, final Integer height) {
    if (weight != null && height != null) {
      final double inMeter = (double)height / 100;
      final int bmi = (int) (weight / (inMeter * inMeter));

      return String.valueOf(bmi);
    }

    return "";
  }

  private Component createField(final String label, final String unit, final String value) {
    final HorizontalLayout field = new HorizontalLayout();
    field.setWidthFull();

    final Label labelComponent = new Label(label);
    labelComponent.setWidthFull();
    field.add(labelComponent);

    if (unit.equals("BMI")) {
      final Icon icon = new Icon(VaadinIcon.CALC);
      final VerticalLayout iconLayout = new VerticalLayout();
      iconLayout.setPadding(false);
      iconLayout.add(icon);
      iconLayout.setHorizontalComponentAlignment(Alignment.END, icon);
      field.add(iconLayout);
    }

    final Label valueComponent = new Label(value + " " + unit);
    final VerticalLayout valueLayout = new VerticalLayout(valueComponent);
    valueLayout.setPadding(false);
    valueLayout.setHorizontalComponentAlignment(Alignment.END, valueComponent);
    field.add(valueLayout);

    return field;
  }

  private void initDocument() {
    documentLayout.add(new Label("Es sind keine Dokumente vorhanden"));
    documentLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
  }

  private FlexLayout initTabs() {
    final FlexLayout tabsLayout = new FlexLayout();
    tabsLayout.setFlexDirection(FlexDirection.ROW);
    initButtonNavigation(tabsLayout);
    return tabsLayout;
  }

  private void initButtonNavigation(final FlexLayout tabLayout) {
    bodyBtn = createTabButtons("Körper", "square-bordered-primary", "square-bordered-left");
    nutrionBtn = createTabButtons("Ernährung", "square-bordered", "square-bordered-middle");
    documentBtn = createTabButtons("Dokumente", "square-bordered", "square-bordered-right");

    tabLayout.add(bodyBtn, nutrionBtn, documentBtn);

    bodyBtn.addClickListener(event -> {
      contentLayout.remove(nutrioLayout, documentLayout);
      contentLayout.add(bodyLayout);

      removePrimary();
      bodyBtn.addClassName("square-bordered-primary");
      nutrionBtn.addClassName("square-bordered");
      documentBtn.addClassName("square-bordered");
    });

    nutrionBtn.addClickListener(event -> {
      contentLayout.remove(bodyLayout, documentLayout);
      contentLayout.add(nutrioLayout);

      removePrimary();
      nutrionBtn.addClassName("square-bordered-primary");
      bodyBtn.addClassName("square-bordered");
      documentBtn.addClassName("square-bordered");
    });

    documentBtn.addClickListener(event -> {
      contentLayout.remove(bodyLayout, nutrioLayout);
      contentLayout.add(documentLayout);

      removePrimary();
      documentBtn.addClassName("square-bordered-primary");
      bodyBtn.addClassName("square-bordered");
      nutrionBtn.addClassName("square-bordered");
    });
  }

  private void removePrimary() {
    bodyBtn.removeClassName("square-bordered-primary");
    nutrionBtn.removeClassName("square-bordered-primary");
    documentBtn.removeClassName("square-bordered-primary");
  }

  private Button createTabButtons(final String text, final String s, final String s2) {
    final Button button = new Button(text);
    button.setWidth("100px");
    button.addThemeVariants(ButtonVariant.LUMO_SMALL);
    button.addClassName(s);
    button.addClassName(s2);
    return button;
  }

  private Component initHeaderContainer() {
    Header.ButtonIcon iconLeftButton = new Header.ButtonIcon(VaadinIcon.ANGLE_LEFT, () -> UI.getCurrent().getPage().getHistory().back());
    Header.ButtonIcon iconRightButton = new Header.ButtonIcon(VaadinIcon.PLUS, () -> UI.getCurrent().navigate("newData"));

    return Header.create("Meine Daten",
                         iconRightButton,
                         iconLeftButton);
  }


}
