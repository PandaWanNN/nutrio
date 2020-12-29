package ch.nutrio.ui.views.content;


import java.util.List;

import ch.nutrio.data.BodyData;
import ch.nutrio.data.StoreKeeper;
import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.FlexBoxLayout;
import ch.nutrio.ui.components.Footer;
import ch.nutrio.ui.components.Header;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@CssImport("./styles/components/data.css")
@CssImport(value = "./styles/vaadin-button-styles.css", themeFor = "vaadin-button")
@PageTitle("Neue Daten erfassen")
@Route(value = "newData", layout = StartLayout.class)
public class NewDataView extends FlexLayout {

  private static final String CLASS_NAME = "data";
  private final VerticalLayout bodyLayout;
  private final Binder<BodyData> binder;

  public NewDataView() {
    setId("newData");
    setClassName(CLASS_NAME);
    setSizeFull();

    setFlexDirection(FlexDirection.COLUMN);

    binder = new Binder<>(BodyData.class);
    final BodyData bodyData = StoreKeeper.getStoreForSession().getBodyDataList().stream()
                                         .findAny()
                                         .map(BodyData::copy)
                                         .orElse(new BodyData());
    binder.setBean(bodyData);

    final Component header = initHeaderContainer();
    final HorizontalLayout footer = Footer.create(CLASS_NAME);

    final FlexBoxLayout contentLayout = new FlexBoxLayout();
    contentLayout.addClassName(CLASS_NAME + "__content");
    contentLayout.setFlexDirection(FlexDirection.COLUMN);

    bodyLayout = new VerticalLayout();
    bodyLayout.setSizeFull();
    contentLayout.add(bodyLayout);

    initBody();

    add(header);
    add(contentLayout);
    add(footer);
  }

  private void initBody() {
    final List<Component> fields = List.of(
        createField("Gewicht", "kg", bodyData -> getValue(bodyData.getWeight()), (bodyData, s) -> bodyData.setWeight(getIntValue(s))),
        createField("Grösse", "cm", bodyData -> getValue(bodyData.getHeight()), (bodyData, s) -> bodyData.setHeight(getIntValue(s))),
        createField("Taillenumfang", "cm", bodyData -> getValue(bodyData.getTail()), (bodyData, s) -> bodyData.setTail(getIntValue(s))),
        createField("Brustumfang", "cm", bodyData -> getValue(bodyData.getBreast()), (bodyData, s) -> bodyData.setBreast(getIntValue(s))),
        createField("Hüftumfang", "cm", bodyData -> getValue(bodyData.getHips()), (bodyData, s) -> bodyData.setHips(getIntValue(s))),
        createField("Armumfang", "cm", bodyData -> getValue(bodyData.getArm()), (bodyData, s) -> bodyData.setArm(getIntValue(s))),
        createField("Beinumfang", "cm", bodyData -> getValue(bodyData.getLeg()), (bodyData, s) -> bodyData.setLeg(getIntValue(s)))
    );

    fields.forEach(bodyLayout::add);
  }

  private Integer getIntValue(final String s) {
    if (s == null || s.isEmpty()) {
      return null;
    }
    return Integer.valueOf(s);
  }

  private String getValue(final Integer value) {
    if (value == null) {
      return "";
    }
    return value.toString();
  }

  private Component createField(final String label,
                                final String unit,
                                final ValueProvider<BodyData, String> getter,
                                final Setter<BodyData, String> setter) {
    final HorizontalLayout field = new HorizontalLayout();
    field.setWidthFull();

    final Label labelComponent = new Label(label);
    labelComponent.setWidthFull();
    field.add(labelComponent);

    final TextField valueComponent = new TextField();
    valueComponent.setSuffixComponent(new Label(unit));
    final VerticalLayout valueLayout = new VerticalLayout(valueComponent);
    valueLayout.setPadding(false);
    valueLayout.setHorizontalComponentAlignment(Alignment.END, valueComponent);
    field.add(valueLayout);

    binder.forField(valueComponent)
          .bind(getter, setter);

    return field;
  }

  private Component initHeaderContainer() {

    return Header.create("Neue Daten erfassen",
                         new Header.Button("Speichern", () -> {
                           if (binder.validate().isOk()) {
                             StoreKeeper.getStoreForSession().addBodyData(binder.getBean());
                             UI.getCurrent().getPage().getHistory().back();
                             Notification.show("Die neuen Einträge wurden gespeichert");
                           }
                         }),
                         new Header.Button("Abbrechen", () -> UI.getCurrent().getPage().getHistory().back()));
  }


}
