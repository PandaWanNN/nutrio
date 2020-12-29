package ch.nutrio.ui.views.content;

import ch.nutrio.backend.Payment;
import ch.nutrio.ui.StartLayout;
import ch.nutrio.ui.components.DataSeriesItemWithRadius;
import ch.nutrio.ui.components.FlexBoxLayout;
import ch.nutrio.ui.components.Footer;
import ch.nutrio.ui.components.Header;
import ch.nutrio.ui.layout.size.Bottom;
import ch.nutrio.ui.layout.size.Horizontal;
import ch.nutrio.ui.layout.size.Right;
import ch.nutrio.ui.layout.size.Top;
import ch.nutrio.ui.util.FontSize;
import ch.nutrio.ui.util.IconSize;
import ch.nutrio.ui.util.LumoStyles;
import ch.nutrio.ui.util.TextColor;
import ch.nutrio.ui.util.UIUtils;
import ch.nutrio.ui.util.css.BorderRadius;
import ch.nutrio.ui.util.css.BoxSizing;
import ch.nutrio.ui.util.css.Display;
import ch.nutrio.ui.util.css.Position;
import ch.nutrio.ui.util.css.Shadow;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.Background;
import com.vaadin.flow.component.charts.model.BackgroundShape;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.Pane;
import com.vaadin.flow.component.charts.model.PlotOptionsSolidgauge;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@CssImport("./styles/components/success.css")
@PageTitle("Erfolge")
@Route(value = "success", layout = StartLayout.class)

/**
 * https://labs.vaadin.com/business/statistics
 */
public class SuccessView extends FlexLayout {

  private static final String CLASS_NAME = "success";
  private final FlexBoxLayout contentLayout;

  public SuccessView() {
    setId("home");
    setClassName(CLASS_NAME);
    setSizeFull();

    setFlexDirection(FlexDirection.COLUMN);

    final Component header = initHeaderContainer();
    final HorizontalLayout footer = Footer.create(CLASS_NAME);

    contentLayout = new FlexBoxLayout();
    contentLayout.addClassName(CLASS_NAME + "__content");
    contentLayout.setFlexDirection(FlexDirection.COLUMN);

    add(header);
    add(contentLayout);
    add(footer);

    Component payments = createPayments();
    contentLayout.add(payments);
  }

  private Component createPayments() {
    FlexBoxLayout payments = new FlexBoxLayout(createPaymentsCharts());
    payments.setBoxSizing(BoxSizing.BORDER_BOX);
    payments.setDisplay(Display.BLOCK);
    payments.setMargin(Top.L);
    payments.setPadding(Horizontal.RESPONSIVE_L);
    payments.setWidthFull();
    return payments;
  }

  private Component createPaymentsCharts() {
    Row charts = new Row();
    UIUtils.setBackgroundColor(LumoStyles.Color.BASE_COLOR, charts);
    UIUtils.setBorderRadius(BorderRadius.S, charts);
    UIUtils.setShadow(Shadow.XS, charts);

    for (Payment.Status status : Payment.Status.values()) {
      charts.add(createPaymentChart(status));
    }

    return charts;
  }

  private Component createPaymentChart(Payment.Status status) {
    int value;

    switch (status) {
      case PENDING:
        value = 24;
        break;

      case SUBMITTED:
        value = 40;
        break;

      case CONFIRMED:
        value = 32;
        break;

      default:
        value = 4;
        break;
    }

    FlexBoxLayout textContainer = new FlexBoxLayout(
        UIUtils.createH2Label(Integer.toString(value)),
        UIUtils.createLabel(FontSize.S, "%"));
    textContainer.setAlignItems(FlexComponent.Alignment.BASELINE);
    textContainer.setPosition(Position.ABSOLUTE);
    textContainer.setSpacing(Right.XS);

    Chart chart = createProgressChart(status, value);

    FlexBoxLayout chartContainer = new FlexBoxLayout(chart, textContainer);
    chartContainer.setAlignItems(FlexComponent.Alignment.CENTER);
    chartContainer
        .setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    chartContainer.setPosition(Position.RELATIVE);
    chartContainer.setHeight("120px");
    chartContainer.setWidth("120px");

    FlexBoxLayout paymentChart = new FlexBoxLayout(
        new Label(status.getDesc()), chartContainer);
    paymentChart.addClassName(CLASS_NAME + "__payment-chart");
    paymentChart.setAlignItems(FlexComponent.Alignment.CENTER);
    paymentChart.setFlexDirection(FlexDirection.COLUMN);
    paymentChart.setPadding(Bottom.S, Top.M);
    return paymentChart;
  }

  private Chart createProgressChart(Payment.Status status, int value) {
    Chart chart = new Chart();
    chart.addClassName(status.getName().toLowerCase());
    chart.setSizeFull();

    Configuration configuration = chart.getConfiguration();
    configuration.getChart().setType(ChartType.SOLIDGAUGE);
    configuration.setTitle("");
    configuration.getTooltip().setEnabled(false);

    configuration.getyAxis().setMin(0);
    configuration.getyAxis().setMax(100);
    configuration.getyAxis().getLabels().setEnabled(false);

    PlotOptionsSolidgauge opt = new PlotOptionsSolidgauge();
    opt.getDataLabels().setEnabled(false);
    configuration.setPlotOptions(opt);

    DataSeriesItemWithRadius point = new DataSeriesItemWithRadius();
    point.setY(value);
    point.setInnerRadius("100%");
    point.setRadius("110%");
    configuration.setSeries(new DataSeries(point));

    Pane pane = configuration.getPane();
    pane.setStartAngle(0);
    pane.setEndAngle(360);

    Background background = new Background();
    background.setShape(BackgroundShape.ARC);
    background.setInnerRadius("100%");
    background.setOuterRadius("110%");
    pane.setBackground(background);

    return chart;
  }


  private Component initHeaderContainer() {
    Header.ButtonIcon iconLeftButton = new Header.ButtonIcon(VaadinIcon.ANGLE_LEFT, () -> UI.getCurrent().getPage().getHistory().back());

    return Header.create("Meine Erfolge",
                         Header.EMPTY_BUTTON_ICON,
                         iconLeftButton);
  }

}
