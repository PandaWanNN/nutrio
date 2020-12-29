package ch.nutrio.ui.views.content;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.FlexBoxLayout;
import ch.nutrio.ui.components.Footer;
import ch.nutrio.ui.components.Header;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@CssImport(value = "./styles/vaadin-button-styles.css", themeFor = "vaadin-button")
@PageTitle("Wissensdatenbank")
@Route(value = "article", layout = StartLayout.class)
public class ArticleView extends FlexLayout implements AfterNavigationObserver {

  private static final String CLASS_NAME = "article";
  private final VerticalLayout contentLayout;

  public ArticleView() {
    setId("article");
    setClassName(CLASS_NAME);
    setSizeFull();
    setFlexDirection(FlexDirection.COLUMN);

    final Component header = initHeaderContainer();
    final HorizontalLayout footer = Footer.create(CLASS_NAME);

    contentLayout = new VerticalLayout();
    contentLayout.setSizeFull();
    final Scroller scroller = new Scroller();
    scroller.setSizeFull();
    scroller.setContent(contentLayout);

    add(header);
    add(scroller);
    add(footer);
  }

  private Optional<String> getArticleId(final AfterNavigationEvent event) {
    final Location location = event.getLocation();
    final Map<String, List<String>> parameters = location.getQueryParameters().getParameters();
    if (parameters.containsKey(KnowledgeView.ARTICLE_ID)) {
      return Optional.ofNullable(parameters.get(KnowledgeView.ARTICLE_ID).get(0));
    }

    return Optional.empty();

  }

  @Override
  public void afterNavigation(final AfterNavigationEvent event) {
    getArticleId(event).ifPresent(s -> {
      try (InputStream is = ArticleView.class.getClassLoader().getResourceAsStream(s)) {
        final Html html = new Html(Objects.requireNonNull(is));
        contentLayout.add(html);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  private Component initHeaderContainer() {
    Header.ButtonIcon iconLeftButton = new Header.ButtonIcon(VaadinIcon.ANGLE_LEFT, () -> UI.getCurrent().getPage().getHistory().back());
    return Header.create("Artikel",
                         Header.EMPTY_BUTTON_ICON,
                         iconLeftButton);
  }
}
