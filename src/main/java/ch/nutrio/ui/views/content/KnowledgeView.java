package ch.nutrio.ui.views.content;


import java.util.HashMap;

import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.FlexBoxLayout;
import ch.nutrio.ui.components.Footer;
import ch.nutrio.ui.components.Header;
import ch.nutrio.ui.components.ListItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

@CssImport("./styles/components/knowledge.css")
@CssImport(value = "./styles/vaadin-button-styles.css", themeFor = "vaadin-button")
@PageTitle("Wissensdatenbank")
@Route(value = "knowledge", layout = StartLayout.class)
public class KnowledgeView extends FlexLayout {

  public static final String ARTICLE_ID = "articleId";
  private static final String CLASS_NAME = "knowledge";
  private final FlexBoxLayout contentLayout;
  private final FlexLayout articlesContent;
  private final FlexLayout documentLayout;
  private Button bodyBtn;
  private Button nutrionBtn;
  private Button documentBtn;
  private Button selectedButton;

  public KnowledgeView() {
    setId("knowledge");
    setClassName(CLASS_NAME);
    setSizeFull();

    setFlexDirection(FlexDirection.COLUMN);

    final Component header = initHeaderContainer();
    final HorizontalLayout footer = Footer.create(CLASS_NAME);

    contentLayout = new FlexBoxLayout();
    contentLayout.addClassName(CLASS_NAME + "__content");
    contentLayout.setFlexDirection(FlexDirection.COLUMN);

    final FlexLayout tabLayout = initTabs();
    contentLayout.add(tabLayout);
    contentLayout.setAlignSelf(Alignment.CENTER, tabLayout);

    articlesContent = new FlexLayout();
    articlesContent.setFlexDirection(FlexDirection.COLUMN);
    documentLayout = new FlexLayout(new Label("Dokumente"));
    contentLayout.add(articlesContent);

    selectedButton = bodyBtn;
    createArticlesContent();

    add(header);
    add(contentLayout);
    add(footer);
  }

  private static Hr createHr() {
    final Hr line = new Hr();
    line.addClassName(CLASS_NAME + "__line");
    return line;
  }

  private void createArticlesContent() {
    articlesContent.removeAll();
    if (selectedButton == bodyBtn) {
      articlesContent.add(
          createItem(true, "Warum auch weniger als 10.000 Schritte pro Tag genug sein können", "Lesedauer: 3 Minuten", "4.html"),
          createHr(),
          createItem(false, "HIIT-Training", "Lesedauer: 3 Minuten", "5.html"),
          createHr(),
          createItem(false, "Laufen für AnfängerInnen - 8 hilfreiche Tipps", "Lesedauer: 4 Minuten", "6.html")
      );
    } else if (selectedButton == nutrionBtn) {
      articlesContent.add(
          createItem(true, "Intervallfasten: Gesund abnehmen", "Lesedauer: 5 Minuten", "1.html"),
          createHr(),
          createItem(false, "Kaloriendefizit zum Abnehmen", "Lesedauer: 3 Minuten", "2.html"),
          createHr(),
          createItem(false, "Mikro- und Makronährstoffe", "Lesedauer: 4 Minuten", "3.html")
      );
    }
  }

  private ListItem createItem(final boolean newItem, final String title, final String duration, final String file) {
    final ListItem item = new ListItem(title, duration);
    item.addClickListener(event -> navigateToArticle(file));
    if (newItem) {
      final Icon icon = new Icon(VaadinIcon.CIRCLE);
      icon.setColor("lightcoral");
      icon.setSize("14px");
      item.setSuffix(icon);
    }
    return item;
  }

  private void navigateToArticle(final String file) {
    final HashMap<String, String> parameters = new HashMap<>();
    parameters.put(ARTICLE_ID, file);
    UI.getCurrent().navigate("article", QueryParameters.simple(parameters));
  }

  private FlexLayout initTabs() {
    final FlexLayout tabsLayout = new FlexLayout();
    tabsLayout.setFlexDirection(FlexDirection.ROW);
    initButtonNavigation(tabsLayout);
    return tabsLayout;
  }

  private void initButtonNavigation(final FlexLayout tabLayout) {
    bodyBtn = createTabButtons("Bewegung", "square-bordered-primary", "square-bordered-left");
    nutrionBtn = createTabButtons("Ernährung", "square-bordered", "square-bordered-middle");
    documentBtn = createTabButtons("Dokumente", "square-bordered", "square-bordered-right");

    tabLayout.add(bodyBtn, nutrionBtn, documentBtn);

    bodyBtn.addClickListener(event -> {
      selectedButton = bodyBtn;
      contentLayout.remove(documentLayout);
      contentLayout.add(articlesContent);

      removePrimary();
      changeButtonSelection(bodyBtn, nutrionBtn, documentBtn);
      createArticlesContent();
    });

    nutrionBtn.addClickListener(event -> {
      selectedButton = bodyBtn;
      contentLayout.remove(documentLayout);
      contentLayout.add(articlesContent);

      removePrimary();
      changeButtonSelection(nutrionBtn, bodyBtn, documentBtn);
      createArticlesContent();
    });

    documentBtn.addClickListener(event -> {
      contentLayout.remove(articlesContent);
      contentLayout.add(documentLayout);

      removePrimary();
      changeButtonSelection(documentBtn, bodyBtn, nutrionBtn);
    });
  }

  private void changeButtonSelection(final Button newSelection, final Button bodyBtn, final Button documentBtn) {
    selectedButton = newSelection;
    newSelection.addClassName("square-bordered-primary");
    bodyBtn.addClassName("square-bordered");
    documentBtn.addClassName("square-bordered");
  }

  private void removePrimary() {
    bodyBtn.removeClassName("square-bordered-primary");
    nutrionBtn.removeClassName("square-bordered-primary");
    documentBtn.removeClassName("square-bordered-primary");
  }

  private Button createTabButtons(final String text, final String s, final String s2) {
    final Button button = new Button(text);
    button.setWidth("100px");
    button.addThemeVariants(ButtonVariant.LUMO_SMALL);
    button.addClassName(s);
    button.addClassName(s2);
    return button;
  }

  private Component initHeaderContainer() {
    return Header.create("Wissensdatenbank",
                         Header.EMPTY_BUTTON_ICON,
                         Header.EMPTY_BUTTON_ICON);
  }


}
