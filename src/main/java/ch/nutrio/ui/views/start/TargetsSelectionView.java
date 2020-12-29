package ch.nutrio.ui.views.start;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ch.nutrio.data.StoreKeeper;
import ch.nutrio.data.TargetData;
import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.Header;
import ch.nutrio.ui.util.UIUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@CssImport("./styles/components/motivation.css")
@PageTitle("Nutrio")
@Route(value = "targets", layout = StartLayout.class)
public class TargetsSelectionView extends HorizontalLayout implements AfterNavigationObserver {

  private static final String CLASS_NAME = "motivation";
  private final List<TargetData> targetDataList;
  private VerticalLayout contentLayout;

  public TargetsSelectionView() {
    targetDataList = new ArrayList<>();
    setId("targets");
    setClassName(CLASS_NAME);

    setSizeFull();

    final VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.setSizeFull();
    mainLayout.setPadding(false);

    final Header.Button nextBtn = new Header.Button("Weiter", this::navigate);
    final Header.Button skipBtn = new Header.Button("Überspringen", this::navigate);
    mainLayout.add(Header.create("Motivation", nextBtn, skipBtn));
    final Div div = new Div();
    div.setHeight("8px");
    final Label h2Label = UIUtils.createH2Label("Wähle deine Ziele aus");
    mainLayout.add(div);
    mainLayout.add(h2Label);
    mainLayout.add(createContent());
    mainLayout.setHorizontalComponentAlignment(Alignment.CENTER, h2Label);

    add(mainLayout);
  }

  private void navigate() {
    targetDataList.forEach(targetData -> StoreKeeper.getStoreForSession().addTargetData(targetData));
    UI.getCurrent().navigate("applehealth");
  }


  private Component createContent() {
    contentLayout = new VerticalLayout();
    contentLayout.addClassName(CLASS_NAME + "__content");
    contentLayout.setSizeFull();

    return contentLayout;
  }


  private void addHealthTarget() {
    createTarget(contentLayout, createTarget("Mehr Schritte gehen pro Tag"));
    createTarget(contentLayout, createTarget("Wöchentliche Workouts"));
    createTarget(contentLayout, createTarget("Täglich ein Artikel zum Thema Ernährung lesen"));
  }

  private void addWeightTargets() {
    createTarget(contentLayout, createTarget("Mein Wunschgewicht erreichen"));
    createTarget(contentLayout, createTarget("Mehr Sport pro Woche"));
    createTarget(contentLayout, createTarget("Mehr Schritte gehen pro Tag"));
    createTarget(contentLayout, createTarget("Kaloriengrenze einhalten"));
  }

  private TargetData createTarget(final String title) {
    return new TargetData(title, "");
  }

  private void createTarget(final VerticalLayout contentLayout, final TargetData targetData) {
    final Component target = createButton(targetData, e -> {
    });
    contentLayout.add(target);
    contentLayout.setHorizontalComponentAlignment(Alignment.CENTER, target);
  }

  private Component createButton(final TargetData targetData, final DomEventListener clickListener) {
    final VerticalLayout layout = new VerticalLayout();
    layout.addClassName(CLASS_NAME + "__button");
    layout.setWidthFull();
    final HorizontalLayout textLayout = new HorizontalLayout();
    textLayout.setHeightFull();
    layout.add(textLayout);
    layout.setHorizontalComponentAlignment(Alignment.CENTER, textLayout);

    final Label label = new Label(targetData.getTitle());
    label.addClassName(CLASS_NAME + "__button__label");
    textLayout.add(label);
    textLayout.setVerticalComponentAlignment(Alignment.CENTER, label);

    layout.getElement().addEventListener("click", event -> {
      if (layout.getClassNames().contains(CLASS_NAME + "__button__selected")) {
        targetRemoved(layout, targetData);
      } else {
        targetAdded(layout, targetData);
      }
      clickListener.handleEvent(event);
    });


    return layout;
  }

  private void targetAdded(final VerticalLayout layout, final TargetData targetData) {
    layout.removeClassNames(CLASS_NAME + "__button__selected");
    layout.addClassName(CLASS_NAME + "__button__selected");
    targetDataList.add(targetData);
  }

  private void targetRemoved(final VerticalLayout layout, final TargetData targetData) {
    layout.removeClassNames(CLASS_NAME + "__button__selected");
    layout.addClassName(CLASS_NAME + "__button");
    targetDataList.remove(targetData);
  }

  @Override
  public void afterNavigation(final AfterNavigationEvent event) {
    getSelection(event).ifPresentOrElse(s -> {
      if (MotivationView.HEALTH_PARAMETER.equals(s)) {
        addHealthTarget();
      } else if (MotivationView.WEIGHT_PARAMETER.equals(s)) {
        addWeightTargets();
      } else {
        Notification.show("Unbekannte Motivation: " + s);
      }
    }, () -> Notification.show("Keine Motivatin ausgewählt"));
  }

  private Optional<String> getSelection(final AfterNavigationEvent event) {
    final Location location = event.getLocation();
    final Map<String, List<String>> parameters = location.getQueryParameters().getParameters();
    if (parameters.containsKey(MotivationView.PARAMETER_KEY)) {
      return Optional.ofNullable(parameters.get(MotivationView.PARAMETER_KEY).get(0));
    }

    return Optional.empty();

  }
}
