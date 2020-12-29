package ch.nutrio.ui;

import java.util.List;

import ch.nutrio.ui.components.Header;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/components/charts.css", themeFor = "vaadin-chart", include = "vaadin-chart-default-theme")
@CssImport(value = "./styles/components/floating-action-button.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/components/grid.css", themeFor = "vaadin-grid")
@CssImport("./styles/lumo/border-radius.css")
@CssImport("./styles/lumo/icon-size.css")
@CssImport("./styles/lumo/margin.css")
@CssImport("./styles/lumo/padding.css")
@CssImport("./styles/lumo/shadow.css")
@CssImport("./styles/lumo/spacing.css")
@CssImport("./styles/lumo/typography.css")
@CssImport("./styles/misc/box-shadow-borders.css")
@CssImport(value = "./styles/styles.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge")

@CssImport("./styles/components/main.css")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class MainLayout extends VerticalLayout implements RouterLayout, PageConfigurator {

  private static final Logger log = LoggerFactory.getLogger(MainLayout.class);
  private static final String CLASS_NAME = "main";
  private final FlexLayout contentContainer;
  private final FlexLayout footerContainer;
  private FlexLayout headerContainer;
  private List<Icon> menuList;

  public MainLayout() {
    setErrorHandler();

    setClassName(CLASS_NAME);
    setSizeFull();
    setPadding(false);

    contentContainer = new FlexLayout();
    footerContainer = new FlexLayout();

//    initHeaderContainer();
    initContentContainer();
    initFooterContainer();

//    add(headerContainer);
    add(contentContainer);
    add(footerContainer);

//    setFlexDirection(FlexDirection.COLUMN);
//    setFlexGrow(1, contentContainer);
  }

  private void setErrorHandler() {
    VaadinSession.getCurrent()
                 .setErrorHandler((ErrorHandler) errorEvent -> {
                   log.error("Uncaught UI exception",
                             errorEvent.getThrowable());
                   Notification.show(
                       "We are sorry, but an internal error occurred");
                 });
  }

  @Override
  public void showRouterLayoutContent(HasElement content) {
    this.contentContainer.getElement().appendChild(content.getElement());
  }

  private void initContentContainer() {
    contentContainer.addClassName(CLASS_NAME + "__content");
  }

  private void initFooterContainer() {
    footerContainer.addClassName(CLASS_NAME + "__footer");
    footerContainer.setFlexDirection(FlexLayout.FlexDirection.ROW);
    footerContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    footerContainer.setAlignItems(FlexComponent.Alignment.CENTER);
    footerContainer.setAlignContent(FlexLayout.ContentAlignment.SPACE_BETWEEN);

    final Icon home = createIcon(VaadinIcon.DASHBOARD, "home");
    final Icon dataIcon = createIcon(VaadinIcon.DATABASE, "data");
    final Icon appointmentsIcon = createIcon(VaadinIcon.DOCTOR, "appointments");
    final Icon settingsIcon = createIcon(VaadinIcon.BOOK, "settings");
    menuList = List.of(home, dataIcon, appointmentsIcon, settingsIcon);

    footerContainer.add(home);
    footerContainer.add(dataIcon);
    footerContainer.add(initAvatar());
    footerContainer.add(appointmentsIcon);
    footerContainer.add(settingsIcon);

    home.setClassName(CLASS_NAME + "__footer__icon__selected");
  }

  private Icon createIcon(final VaadinIcon dashboard, final String viewName) {
    final Icon icon = new Icon(dashboard);
    icon.setClassName(CLASS_NAME + "__footer__icon");
    icon.addClickListener(iconClickEvent -> {
      menuList.forEach(icon1 -> icon1.setClassName(CLASS_NAME + "__footer__icon"));
      icon.setClassName(CLASS_NAME + "__footer__icon__selected");
      UI.getCurrent().navigate(viewName);
    });
    return icon;
  }

  private Icon initAvatar() {
    final Icon icon = new Icon(VaadinIcon.PLUS);
    icon.setClassName(CLASS_NAME + "__avatar");
    ContextMenu contextMenu = new ContextMenu(icon);
    contextMenu.setOpenOnClick(true);
    contextMenu.addItem("Gewicht eintragen",
                        e -> Notification.show("Not implemented yet.", 3000,
                                               Notification.Position.BOTTOM_CENTER));
    contextMenu.addItem("Termin erstellen",
                        e -> Notification.show("Not implemented yet.", 3000,
                                               Notification.Position.BOTTOM_CENTER));
    contextMenu.addItem("Ziel erstellen",
                        e -> Notification.show("Not implemented yet.", 3000,
                                               Notification.Position.BOTTOM_CENTER));

    return icon;
  }

  private void initHeaderContainer() {
    boolean showSettingsButton = true;
    Header.ButtonIcon iconLeftButton;
    Header.ButtonIcon iconRightButton;
    if (showSettingsButton) {
      iconLeftButton = new Header.ButtonIcon(VaadinIcon.TOOLBOX, () -> UI.getCurrent().navigate("settings"));
      iconRightButton = new Header.ButtonIcon(VaadinIcon.STAR, () -> UI.getCurrent().navigate("success"));
    } else {
      iconLeftButton = Header.EMPTY_BUTTON_ICON;
      iconRightButton = Header.EMPTY_BUTTON_ICON;
    }
    headerContainer = Header.create("nutrio",
                                    iconRightButton,
                                    iconLeftButton);
  }

  @Override
  public void configurePage(InitialPageSettings settings) {
    settings.addMetaTag("apple-mobile-web-app-capable", "yes");
    settings.addMetaTag("apple-mobile-web-app-status-bar-style", "black");

    settings.addFavIcon("icon", "frontend/images/favicons/favicon.ico",
                        "256x256");
  }
}
