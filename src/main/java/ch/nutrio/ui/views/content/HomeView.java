package ch.nutrio.ui.views.content;

import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.FlexBoxLayout;
import ch.nutrio.ui.components.Footer;
import ch.nutrio.ui.components.Header;
import ch.nutrio.ui.components.Targets;
import ch.nutrio.ui.components.TimeLine;
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

@CssImport("./styles/components/home.css")
@CssImport(value = "./styles/vaadin-button-styles.css", themeFor = "vaadin-button")
@PageTitle("Home")
@Route(value = "home", layout = StartLayout.class)
public class HomeView extends FlexLayout {

  private static final String CLASS_NAME = "home";
  private final TimeLine timeLine;
  private final Targets targets;
  private final FlexBoxLayout contentLayout;

  public HomeView() {
    setId("home");
    setClassName(CLASS_NAME);
    setSizeFull();

    setFlexDirection(FlexDirection.COLUMN);

    final Component header = initHeaderContainer();
    final FlexLayout tabsLayout = initTabs();
    timeLine = new TimeLine();
    targets = new Targets();
    final HorizontalLayout footer = Footer.create(CLASS_NAME);

    contentLayout = new FlexBoxLayout();
    contentLayout.addClassName(CLASS_NAME + "__content");
    contentLayout.setFlexDirection(FlexDirection.COLUMN);
    contentLayout.add(tabsLayout, timeLine);

    add(header);
    add(contentLayout);
    add(footer);

    setAlignSelf(Alignment.CENTER, tabsLayout);

    setFlexGrow(1, timeLine);
  }

  private FlexLayout initTabs() {
    final FlexLayout tabsLayout = new FlexLayout();
    tabsLayout.setFlexDirection(FlexDirection.ROW);
    initButtonNavigation(tabsLayout);
    return tabsLayout;
  }

  private Component initHeaderContainer() {
    Header.ButtonIcon iconLeftButton = new Header.ButtonIcon(VaadinIcon.TOOLBOX, () -> UI.getCurrent().navigate("settings"));
    Header.ButtonIcon iconRightButton = new Header.ButtonIcon(VaadinIcon.STAR, () -> UI.getCurrent().navigate("success"), "gold");
    return Header.create("nutrio",
                         iconRightButton,
                         iconLeftButton);
  }

  private void initButtonNavigation(final FlexLayout tabLayout) {
    final Button timeLineBtn = createTabButtons("Aktivitäten", "square-bordered-primary", "square-bordered-left");
    final Button targetBtn = createTabButtons("Ziele", "square-bordered", "square-bordered-right");

    tabLayout.add(timeLineBtn, targetBtn);

    timeLineBtn.addClickListener(event -> {
      contentLayout.remove(targets);
      contentLayout.add(timeLine);
      timeLine.reInitialize();
      timeLineBtn.addClassName("square-bordered-primary");
      targetBtn.removeClassName("square-bordered-primary");
      timeLineBtn.removeClassName("square-bordered");
      targetBtn.addClassName("square-bordered");
    });

    targetBtn.addClickListener(event -> {
      contentLayout.remove(timeLine);
      contentLayout.add(targets);
      targets.reInitialize();
      targetBtn.addClassName("square-bordered-primary");
      timeLineBtn.removeClassName("square-bordered-primary");
      targetBtn.removeClassName("square-bordered");
      timeLineBtn.addClassName("square-bordered");
    });
  }

  private Button createTabButtons(final String text, final String s, final String s2) {
    final Button button = new Button(text);
    button.setWidth("100px");
    button.addThemeVariants(ButtonVariant.LUMO_SMALL);
    button.addClassName(s);
    button.addClassName(s2);
    return button;
  }

}
