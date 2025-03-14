package com.kriss.me.views.dailybinge;

import com.kriss.me.data.Bindge;
import com.kriss.me.services.BindgeService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.time.Duration;
import java.util.Optional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Daily Binge")
@Route("daily-binge/:bindgeID?/:action?(edit)")
@Menu(order = 2, icon = LineAwesomeIconUrl.FEATHER_ALT_SOLID)
@Uses(Icon.class)
public class DailyBingeView extends Div implements BeforeEnterObserver {

    private final String BINDGE_ID = "bindgeID";
    private final String BINDGE_EDIT_ROUTE_TEMPLATE = "daily-binge/%s/edit";

    private final Grid<Bindge> grid = new Grid<>(Bindge.class, false);

    private DateTimePicker time;
    private TextField food;
    private TextField volume;
    private TextField place;
    private Checkbox wasTooMuch;
    private TextField context;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Bindge> binder;

    private Bindge bindge;

    private final BindgeService bindgeService;

    public DailyBingeView(BindgeService bindgeService) {
        this.bindgeService = bindgeService;
        addClassNames("daily-binge-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("time").setAutoWidth(true);
        grid.addColumn("food").setAutoWidth(true);
        grid.addColumn("volume").setAutoWidth(true);
        grid.addColumn("place").setAutoWidth(true);
        LitRenderer<Bindge> wasTooMuchRenderer = LitRenderer.<Bindge>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", wasTooMuch -> wasTooMuch.isWasTooMuch() ? "check" : "minus").withProperty("color",
                        wasTooMuch -> wasTooMuch.isWasTooMuch()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(wasTooMuchRenderer).setHeader("Was Too Much").setAutoWidth(true);

        grid.addColumn("context").setAutoWidth(true);
        grid.setItems(query -> bindgeService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(BINDGE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(DailyBingeView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Bindge.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.bindge == null) {
                    this.bindge = new Bindge();
                }
                binder.writeBean(this.bindge);
                bindgeService.save(this.bindge);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(DailyBingeView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> bindgeId = event.getRouteParameters().get(BINDGE_ID).map(Long::parseLong);
        if (bindgeId.isPresent()) {
            Optional<Bindge> bindgeFromBackend = bindgeService.get(bindgeId.get());
            if (bindgeFromBackend.isPresent()) {
                populateForm(bindgeFromBackend.get());
            } else {
                Notification.show(String.format("The requested bindge was not found, ID = %s", bindgeId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(DailyBingeView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        H2 formTitle = new H2("Enter a new Binge");
        time = new DateTimePicker("Time");
        time.setStep(Duration.ofSeconds(1));
        food = new TextField("Food");
        volume = new TextField("Volume");
        place = new TextField("Place");
        wasTooMuch = new Checkbox("Was Too Much");
        context = new TextField("Context");
        formLayout.add(formTitle, time, food, volume, place, wasTooMuch, context);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Bindge value) {
        this.bindge = value;
        binder.readBean(this.bindge);

    }
}
