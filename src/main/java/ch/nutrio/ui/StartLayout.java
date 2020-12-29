package ch.nutrio.ui;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CssImport("./styles/shared-styles.css")
@PWA(name = "Nutrio", shortName = "Nutrio")
@Theme(value = Lumo.class)
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class StartLayout extends VerticalLayout implements RouterLayout {

  private static final Logger log = LoggerFactory.getLogger(StartLayout.class);

  public StartLayout() {
    VaadinSession.getCurrent()
                 .setErrorHandler((ErrorHandler) errorEvent -> {
                   log.error("Uncaught UI exception",
                             errorEvent.getThrowable());
                   Notification.show(
                       "We are sorry, but an internal error occurred");
                 });

    setSizeFull();
    setPadding(false);
  }
}
