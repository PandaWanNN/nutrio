package ch.nutrio.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

@CssImport("./styles/components/header.css")
public class Header {

  public static final Button EMPTY_BUTTON = new Button("", () -> {
  });

  public static final ButtonIcon EMPTY_BUTTON_ICON = new ButtonIcon(VaadinIcon.TOOLBOX, () -> {
  });
  private static final String CLASS_NAME = "header";

  public static FlexLayout create(final String title, final Button rightBtn, final Button leftBtn) {
    final FlexLayout mainLayout = initMainLayout();

    final Component leftLbl = createLabel(leftBtn.getText(), "__label__navigation__left");
    final Component centerLbl = createLabel(title, "__label__title");
    final Component rightLbl = createLabel(rightBtn.getText(), "__label__navigation__right");

    leftLbl.getElement().addEventListener("click", e -> leftBtn.getNavigationAction().run());
    rightLbl.getElement().addEventListener("click", e -> rightBtn.getNavigationAction().run());

    final FlexLayout leftLayout = new FlexLayout(leftLbl);
    final FlexLayout centerLayout = new FlexLayout(centerLbl);
    final FlexLayout rightLayout = new FlexLayout(rightLbl);
    initHeader(mainLayout, leftLayout, centerLayout, rightLayout);

    return mainLayout;
  }

  public static FlexLayout create(final String title, final ButtonIcon rightIcon, final ButtonIcon leftIcon) {
    final FlexLayout mainLayout = initMainLayout();

    final FlexLayout leftLayout = new FlexLayout();
    final FlexLayout centerLayout = new FlexLayout();
    final FlexLayout rightLayout = new FlexLayout();

    if (leftIcon != EMPTY_BUTTON_ICON) {
      final Icon iconLeft = new Icon(leftIcon.getIcon());
      setIconColor(leftIcon, iconLeft);
      iconLeft.addClassName(CLASS_NAME + "__label__navigation__left");
      iconLeft.getElement().addEventListener("click", e -> leftIcon.getNavigationAction().run());
      leftLayout.add(iconLeft);
    }
    final Component centerLbl = createLabel(title, "__label__title");
    centerLayout.add(centerLbl);

    if (rightIcon != EMPTY_BUTTON_ICON) {
      final Icon iconRight = new Icon(rightIcon.getIcon());
      setIconColor(rightIcon, iconRight);
      iconRight.addClassName(CLASS_NAME + "__label__navigation__right");
      iconRight.getElement().addEventListener("click", e -> rightIcon.getNavigationAction().run());
      rightLayout.add(iconRight);
    }

    initHeader(mainLayout, leftLayout, centerLayout, rightLayout);

    return mainLayout;
  }

  private static void setIconColor(final ButtonIcon buttonIcon, final Icon icon) {
    if (buttonIcon.getColor() != null) {
      icon.setColor(buttonIcon.getColor());
    }
  }

  private static FlexLayout initMainLayout() {
    final FlexLayout mainLayout = new FlexLayout();
    mainLayout.setWidthFull();
    mainLayout.setClassName(CLASS_NAME);
    mainLayout.setFlexDirection(FlexLayout.FlexDirection.ROW);
    return mainLayout;
  }

  private static void initHeader(final FlexLayout mainLayout, final FlexLayout leftLayout, final FlexLayout centerLayout, final FlexLayout rightLayout) {
    mainLayout.add(leftLayout);
    mainLayout.add(centerLayout);
    mainLayout.add(rightLayout);

    leftLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
    centerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    rightLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

    leftLayout.setAlignItems(FlexComponent.Alignment.CENTER);
    centerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
    rightLayout.setAlignItems(FlexComponent.Alignment.CENTER);

    mainLayout.setFlexGrow(1, leftLayout, rightLayout);
    mainLayout.setFlexGrow(2, centerLayout);
    mainLayout.setFlexShrink(1, leftLayout, centerLayout, rightLayout);
    mainLayout.setFlexBasis("0", leftLayout, centerLayout, rightLayout);
  }

  private static Component createLabel(final String s, final String label__title) {
    final Div div = new Div(new Text(s));
    div.addClassName(CLASS_NAME + label__title);
    return div;
  }

  public static class Button {

    private final String text;
    private final Runnable navigationAction;

    public Button(final String text, final Runnable navigationAction) {
      this.text = text;
      this.navigationAction = navigationAction;
    }

    public String getText() {
      return text;
    }

    public Runnable getNavigationAction() {
      return navigationAction;
    }
  }

  public static class ButtonIcon {

    private final VaadinIcon icon;
    private final Runnable navigationAction;
    private final String color;

    public ButtonIcon(final VaadinIcon icon, final Runnable navigationAction) {
      this(icon, navigationAction, null);
    }

    public ButtonIcon(final VaadinIcon icon, final Runnable navigationAction, final String color) {
      this.icon = icon;
      this.navigationAction = navigationAction;
      this.color = color;
    }

    public VaadinIcon getIcon() {
      return icon;
    }

    public String getColor() {
      return color;
    }

    public Runnable getNavigationAction() {
      return navigationAction;
    }
  }
}
