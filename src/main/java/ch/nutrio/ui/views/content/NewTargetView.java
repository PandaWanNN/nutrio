package ch.nutrio.ui.views.content;


import ch.nutrio.data.StoreKeeper;
import ch.nutrio.data.TargetData;
import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.FlexBoxLayout;
import ch.nutrio.ui.components.Footer;
import ch.nutrio.ui.components.Header;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@CssImport("./styles/components/newTarget.css")
@PageTitle("Neues Ziel")
@Route(value = "newTarget", layout = StartLayout.class)
public class NewTargetView extends FlexLayout {

  private static final String CLASS_NAME = "newTarget";
  private final Binder<TargetData> binder;

  public NewTargetView() {
    setId("newTarget");
    setClassName(CLASS_NAME);
    setSizeFull();

    binder = new Binder<>();
    binder.setBean(new TargetData());

    setFlexDirection(FlexLayout.FlexDirection.COLUMN);

    final Component header = initHeaderContainer();
    final HorizontalLayout footer = Footer.create(CLASS_NAME);

    final FlexBoxLayout contentLayout = new FlexBoxLayout();
    contentLayout.addClassName(CLASS_NAME + "__content");
    contentLayout.setFlexDirection(FlexLayout.FlexDirection.COLUMN);

    add(header);
    add(contentLayout);
    add(footer);

    final TextField title = new TextField("Titel");
    final TextArea description = new TextArea("Beschreibung");
    description.setHeight("200px");

    contentLayout.add(title, description);

    binder.forField(title)
          .asRequired("Bitte geben Sie einen Titel ein")
          .bind(TargetData::getTitle, TargetData::setTitle);

    binder.forField(title)
          .bind(TargetData::getDescription, TargetData::setDescription);
  }

  private Component initHeaderContainer() {
    return Header.create("Meine Erfolge",
                         new Header.Button("Erstellen", () -> {
                           if (binder.validate().isOk()) {
                             StoreKeeper.getStoreForSession().addTargetData(binder.getBean());
                             UI.getCurrent().getPage().getHistory().back();
                             Notification.show("Ziel gespeichert", 2000, Notification.Position.BOTTOM_END);
                           }
                         }),
                         new Header.Button("Abbrechen", () -> UI.getCurrent().getPage().getHistory().back()));
  }


}
