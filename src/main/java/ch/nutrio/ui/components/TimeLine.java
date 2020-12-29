package ch.nutrio.ui.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ch.nutrio.data.StoreKeeper;
import ch.nutrio.data.TimeLineRecord;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./styles/components/timeline.css")
public class TimeLine extends FlexLayout {

  private static final String CLASS_NAME = "timeline";
  private Scroller scroller;

  public TimeLine() {
    setId("timeline");
    setFlexDirection(FlexDirection.COLUMN);
    setClassName(CLASS_NAME);
    scroller = new Scroller();
    add(scroller);
    initContent();
  }

  private void initContent() {
    final VerticalLayout content = new VerticalLayout();
    content.setSizeFull();
    content.setPadding(false);
    scroller.setContent(content);


    final Div firstLine = new Div();
    firstLine.addClassName(CLASS_NAME + "__line__first");
    content.add(firstLine);

    StoreKeeper.getStoreForSession().getTimeLineRecordList().forEach(timeLineRecord -> content.add(createTimeLineElement(timeLineRecord)));

    final Div lastLine = new Div();
    lastLine.addClassName(CLASS_NAME + "__line__last");
    content.add(lastLine);
  }

  public void reInitialize() {
    initContent();
  }

  private FlexLayout createTimeLineElement(final TimeLineRecord timeLineRecord) {
    final FlexLayout timeLineElement = new FlexLayout();
    timeLineElement.addClassName(CLASS_NAME + "__element");
    timeLineElement.setFlexDirection(FlexLayout.FlexDirection.ROW);

    final FlexLayout leftLayout = new FlexLayout();
    leftLayout.setFlexDirection(FlexDirection.COLUMN);
    leftLayout.setHeightFull();

    final FlexLayout rightLayout = new FlexLayout();
    rightLayout.setFlexDirection(FlexDirection.COLUMN);
    rightLayout.setSizeFull();

    timeLineElement.add(leftLayout);
    timeLineElement.add(rightLayout);

    final Div topLine = new Div();
    topLine.addClassName(CLASS_NAME + "__line__top");

    final Icon icon = new Icon(timeLineRecord.getIcon());
    icon.addClassName(CLASS_NAME + "__icon");
    if (timeLineRecord.getColor() != null) {
      icon.setColor(timeLineRecord.getColor());
    }

    final Div bottomLine = new Div();
    bottomLine.addClassName(CLASS_NAME + "__line__bottom");

    leftLayout.add(topLine, new Div(icon), bottomLine);
    leftLayout.setFlexGrow(1, icon);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
    String thirdLine = LocalDate.parse(timeLineRecord.getDate()).format(formatter);

    final FlexLayout timeLineBox = createTimelineBox(timeLineRecord.getTitle(), timeLineRecord.getDescription(), thirdLine);
    rightLayout.add(timeLineBox);

    return timeLineElement;
  }

  private FlexLayout createTimelineBox(final String title, final String secondLine, final String thirdLine) {
    final FlexLayout timeLineBox = new FlexLayout();
    timeLineBox.setFlexDirection(FlexDirection.COLUMN);
    timeLineBox.setClassName(CLASS_NAME + "__box");

    final FlexLayout labelLayout = new FlexLayout();
    labelLayout.setFlexDirection(FlexDirection.COLUMN);

    timeLineBox.add(createTitleLabel(title));
    timeLineBox.add(createLabel(secondLine));
    timeLineBox.add(createLabel(thirdLine));
    return timeLineBox;
  }

  private Label createTitleLabel(final String s) {
    final Label label = new Label(s);
    label.addClassName(CLASS_NAME + "__box__label__title");
    return label;
  }

  private Label createLabel(final String s) {
    final Label label = new Label(s);
    label.addClassName(CLASS_NAME + "__box__label");
    return label;
  }

}
