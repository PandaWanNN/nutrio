package ch.nutrio.ui.views.content;


import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.ChatComponent;
import ch.nutrio.ui.components.ConsultationComponent;
import ch.nutrio.ui.components.Footer;
import ch.nutrio.ui.components.Header;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@CssImport("./styles/components/praxis.css")
@CssImport(value = "./styles/vaadin-button-styles.css", themeFor = "vaadin-button")
@PageTitle("Meine Praxis")
@Route(value = "praxis", layout = StartLayout.class)
public class PraxisView extends FlexLayout {

  private static final String CLASS_NAME = "praxis";
  private final FlexLayout contentLayout;
  private final Component consultationLayout;
  private final Component chat;

  public PraxisView() {
    setId("praxis");
    setClassName(CLASS_NAME);
    setSizeFull();

    setFlexDirection(FlexDirection.COLUMN);

    final Component header = initHeaderContainer();
    final HorizontalLayout footer = Footer.create(CLASS_NAME);


    contentLayout = new FlexLayout();
    contentLayout.addClassName(CLASS_NAME + "__content");
    contentLayout.setFlexDirection(FlexDirection.COLUMN);

    final FlexLayout tabLayout = initTabs();
    contentLayout.add(tabLayout);
    contentLayout.setAlignSelf(Alignment.CENTER, tabLayout);

    consultationLayout = ConsultationComponent.create();
    chat = ChatComponent.create();

    contentLayout.add(consultationLayout);

    add(header);
    add(contentLayout);
    add(footer);
  }

  private FlexLayout initTabs() {
    final FlexLayout tabsLayout = new FlexLayout();
    tabsLayout.setFlexDirection(FlexDirection.ROW);
    initButtonNavigation(tabsLayout);
    return tabsLayout;
  }

  private Component initHeaderContainer() {
    return Header.create("Meine Praxis",
                         new Header.ButtonIcon(VaadinIcon.PLUS, () -> UI.getCurrent().navigate("newAppointment")),
                         Header.EMPTY_BUTTON_ICON);
  }

  private void initButtonNavigation(final FlexLayout tabLayout) {
    final Button consultationBtn = createTabButtons("Sprechstunde", "square-bordered-primary", "square-bordered-left");
    final Button chatBtn = createTabButtons("Chat", "square-bordered", "square-bordered-right");

    tabLayout.add(consultationBtn, chatBtn);

    consultationBtn.addClickListener(event -> {
      contentLayout.remove(chat);
      contentLayout.add(consultationLayout);
      consultationBtn.addClassName("square-bordered-primary");
      chatBtn.removeClassName("square-bordered-primary");
      consultationBtn.removeClassName("square-bordered");
      chatBtn.addClassName("square-bordered");
    });

    chatBtn.addClickListener(event -> {
      contentLayout.remove(consultationLayout);
      contentLayout.add(chat);
      chatBtn.addClassName("square-bordered-primary");
      consultationBtn.removeClassName("square-bordered-primary");
      chatBtn.removeClassName("square-bordered");
      consultationBtn.addClassName("square-bordered");
    });
  }

  private Button createTabButtons(final String text, final String s, final String s2) {
    final Button button = new Button(text);
    button.setWidth("130px");
    button.addThemeVariants(ButtonVariant.LUMO_SMALL);
    button.addClassName(s);
    button.addClassName(s2);
    return button;
  }


}
