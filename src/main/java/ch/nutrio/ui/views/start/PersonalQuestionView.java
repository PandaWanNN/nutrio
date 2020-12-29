package ch.nutrio.ui.views.start;

import java.util.List;

import ch.nutrio.data.BodyData;
import ch.nutrio.data.Store;
import ch.nutrio.data.StoreKeeper;
import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.Header;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Nutrio")
@Route(value = "personalqst", layout = StartLayout.class)
public class PersonalQuestionView extends HorizontalLayout {

  private final Store store;
  private NumberField weight;
  private NumberField height;
  private NumberField workout;
  private ToggleButton smoker;

  public PersonalQuestionView() {
    setId("personalQuestion");
    setSizeFull();

    store = StoreKeeper.getStoreForSession();
    final VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.setPadding(false);

    final Header.Button nextBtn = new Header.Button("Weiter", this::navigate);
    final Header.Button skipBtn = new Header.Button("Überspringen", this::navigate);
    mainLayout.add(Header.create("Persönliche Angaben", nextBtn, skipBtn));
    mainLayout.add(createContent());

    if (!store.getBodyDataList().isEmpty()) {
      final BodyData bodyData = store.getBodyDataList().get(0);
      weight.setValue(Double.valueOf(bodyData.getWeight()));
      height.setValue(Double.valueOf(bodyData.getHeight()));
    }
    workout.setValue(store.getPersonalData().getWorkout());
    smoker.setValue(store.getPersonalData().isSmoker());

    add(mainLayout);
  }

  private void navigate() {
    if (weight.getValue() != null || height.getValue() != null) {

      final Integer weight = this.weight.getValue() != null ? this.weight.getValue().intValue() : null;
      final Integer height = this.height.getValue() != null ? this.height.getValue().intValue() : null;
      final BodyData bodyData = new BodyData(weight, height);
      store.addBodyData(bodyData);
    }
    store.getPersonalData().setSmoker(smoker.getValue());
    store.getPersonalData().setWorkout(workout.getValue());

    if (store.getTargetDataList().isEmpty()) {
      UI.getCurrent().navigate("motivation");
    } else {
      UI.getCurrent().navigate("applehealth");
    }
  }

  private Component createContent() {
    final VerticalLayout contentLayout = new VerticalLayout();
    contentLayout.setSizeFull();

    weight = createField("Gewicht", "kg");
    height = createField("Körpergrösse", "cm");
    workout = createField("Einheiten Sport", "pro Woche");
    smoker = new ToggleButton("Raucher");
    List<Component> components = List.of(weight, height, workout, smoker);

    components.forEach(component -> {
      if (component instanceof HasSize) {
        ((HasSize) component).setWidthFull();
      }
      contentLayout.add(component);
    });

    return contentLayout;
  }

  private NumberField createField(final String name, final String unit) {
    final NumberField numberField = new NumberField(name);
    numberField.setSuffixComponent(new Label(unit));
    return numberField;
  }

}
