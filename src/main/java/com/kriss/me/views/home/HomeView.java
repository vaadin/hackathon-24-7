package com.kriss.me.views.home;

import com.kriss.me.views.clock.ClockView;
import com.kriss.me.views.weather.WeatherView;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dashboard.Dashboard;
import com.vaadin.flow.component.dashboard.DashboardWidget;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.map.configuration.Coordinate;
import com.vaadin.flow.component.map.configuration.View;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Home")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.CHART_AREA_SOLID)
public class HomeView extends Main {

    public HomeView() {
        addClassName("home-view");
        add(new Paragraph("Live announcement:"));
        setSizeFull();

        Dashboard dashboard = new Dashboard();
        dashboard.setEditable(true);
        dashboard.setMinimumColumnWidth("150px");
        dashboard.setMaximumColumnCount(3);

        DashboardWidget visitors = new DashboardWidget("Visitors");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addToStart(new Button("Start"));
        horizontalLayout.addToEnd(new Button("End"));
        visitors.setContent(horizontalLayout);
        dashboard.add(visitors);

        DashboardWidget clock = new DashboardWidget("Clock");
        clock.setContent(new ClockView());
        dashboard.add(clock);

        DashboardWidget weather = new DashboardWidget("Weather");
        weather.setContent(new WeatherView());
        dashboard.add(weather);

        DashboardWidget helsinkiMap = new DashboardWidget("Helsinki Map");
        helsinkiMap.setContent(createMapContent());
        helsinkiMap.setRowspan(2);
        dashboard.add(helsinkiMap);

        DashboardWidget browserDistribution = new DashboardWidget("Browsers");
        browserDistribution.setContent(createWidgetContent());
        dashboard.add(browserDistribution);

        DashboardWidget catImage = new DashboardWidget("A kittykat!");
        catImage.setContent(createCatImageContent());
        dashboard.add(catImage);

        DashboardWidget visitorsByBrowser = new DashboardWidget(
                "Visitors by browser");
        visitorsByBrowser.setContent(createWidgetContent());
        visitorsByBrowser.setColspan(2);
        dashboard.add(visitorsByBrowser);

        // Live region for screen reader announcements
        Div liveRegion = new Div();
        liveRegion.getElement().setAttribute("aria-live", "polite");
        add(liveRegion);

        // Dashboard event listeners
        dashboard.addItemSelectedChangedListener(event -> {
            String title = ((DashboardWidget) event.getItem()).getTitle();
            String selected = event.isSelected() ? "selected" : "deselected";
            liveRegion.setText("Widget " + title + " " + selected);
        });

        dashboard.addItemMoveModeChangedListener(event -> {
            liveRegion.setText(event.isMoveMode() ? "Entered move mode" : "Exited move mode");
        });

        dashboard.addItemResizeModeChangedListener(event -> {
            liveRegion.setText(event.isResizeMode() ? "Entered resize mode" : "Exited resize mode");
        });

        dashboard.addItemMovedListener(event -> {
            int position = event.getItems().indexOf(event.getItem()) + 1;
            int total = event.getItems().size();
            String title = ((DashboardWidget) event.getItem()).getTitle();
            liveRegion.setText("Moved widget " + title + " to position " + position + " of " + total);
        });

        dashboard.addItemResizedListener(event -> {
            int colspan = event.getItem().getColspan();
            int rowspan = event.getItem().getRowspan();
            String title = event.getItem().getTitle();
            liveRegion.setText("Resized widget " + title + " to " + colspan + " columns, " + rowspan + " rows");
        });

        dashboard.addItemRemovedListener(event -> {
            String title = ((DashboardWidget) event.getItem()).getTitle();
            liveRegion.setText("Removed widget " + title);
        });

        add(dashboard);
    }

    private Div createWidgetContent() {
        Div content = new Div();
        content.setClassName("dashboard-widget-content");
        return content;
    }

    private Div createMapContent() {
        Div mapContainer = new Div();
        mapContainer.setClassName("dashboard-widget-content");
        mapContainer.getStyle().set("height", "100%");
        mapContainer.setMinHeight(300, Unit.PIXELS);

        // Create map centered on Helsinki
        Map map = new Map();
        // Helsinki coordinates: 60.1699° N, 24.9384° E
        Coordinate helsinkiCoordinates = new Coordinate(24.9384, 60.1699);
        
        // Set the view configuration
        View view = new View();
        view.setCenter(helsinkiCoordinates);
        view.setZoom(12); // City level zoom
        map.setView(view);
        
        // Make the map fill the container
        map.getElement().getStyle().set("width", "100%");
        map.getElement().getStyle().set("height", "100%");
        
        mapContainer.add(map);
        return mapContainer;
    }

    private VerticalLayout createCatImageContent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setClassName("dashboard-widget-content");
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setSpacing(true);
        layout.setPadding(true);

        // Create image from Unsplash (free to use)
        Image catImage = new Image(
            "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?auto=format&fit=crop&w=800&q=80",
            "Cute orange cat"
        );
        catImage.setWidth("100%");
        catImage.getStyle().set("border-radius", "8px");

        // Create attribution link
        Anchor attribution = new Anchor(
            "https://unsplash.com/photos/orange-tabby-cat-gKXKBY-C-Dk",
            "Photo by Alvan Nee on Unsplash"
        );
        attribution.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        attribution.getStyle().set("color", "var(--lumo-secondary-text-color)");

        layout.add(catImage, attribution);
        return layout;
    }
}
