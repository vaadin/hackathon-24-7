package com.vaadin.hackaton.herberts.views.masterdetail;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridLazyDataView;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.hackaton.herberts.data.SamplePerson;
import com.vaadin.hackaton.herberts.services.SamplePersonService;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.Set;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.util.CollectionUtils;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Master-Detail")
@Route("master-detail")
@Menu(order = 1, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class MasterDetailView extends Div {

    private final String SAMPLEPERSON_ID = "samplePersonID";

    private final Grid<SamplePerson> grid;

    private TextField occupation;
    private TextField role;
    private Checkbox important;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<MultiUserEditorModel> binder;

    private Set<SamplePerson> selectedPeople;

    private final SamplePersonService samplePersonService;

    private SamplePerson rangeStartItem;

    public MasterDetailView(SamplePersonService samplePersonService) {
        this.samplePersonService = samplePersonService;
        addClassNames("master-detail-view");

        grid = createGrid();

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);


        add(splitLayout);

        // Configure Form
        binder = new BeanValidationBinder<>(MultiUserEditorModel.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            binder.readBean(null);
            refreshGrid();
        });

        save.addClickListener(e -> onSave());
    }

    private Grid<SamplePerson> createGrid() {
        var grid = new Grid<>(SamplePerson.class, false);
        grid.setTooltipPosition(Tooltip.TooltipPosition.TOP);
        // Configure Grid
        grid.addColumn("firstName").setAutoWidth(true).setTooltipGenerator(SamplePerson::getFirstName);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("phone").setAutoWidth(true);
        grid.addColumn("dateOfBirth").setAutoWidth(true);
        grid.addColumn("occupation").setAutoWidth(true);
        grid.addColumn("role").setAutoWidth(true);
        LitRenderer<SamplePerson> importantRenderer = LitRenderer.<SamplePerson>of(
                        "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", important -> important.isImportant() ? "check" : "minus").withProperty("color",
                        important -> important.isImportant()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(importantRenderer).setHeader("Important").setAutoWidth(true);

        grid.setItems(query -> samplePersonService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.getLazyDataView().setItemIndexProvider((item, query) -> {
            return samplePersonService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream().toList().indexOf(item);
        });

        var gridSelection = (GridMultiSelectionModel<SamplePerson>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        addRangeSelectionSupport(gridSelection);

        // when a row is selected or deselected, populate form
        grid.asMultiSelect().addValueChangeListener(event -> {
            if (!CollectionUtils.isEmpty(event.getValue())) {
                this.selectedPeople = event.getValue();
            } else {
                binder.readBean(null);
                UI.getCurrent().navigate(MasterDetailView.class);
            }
        });
        return grid;
    }

    private void addRangeSelectionSupport(GridMultiSelectionModel<SamplePerson> selectionModel) {
        selectionModel.addClientItemToggleListener(event -> {
            SamplePerson item = event.getItem();

            // If the anchor point isn't set, set it to the current item
            if (rangeStartItem == null) {
                rangeStartItem = item;
            }

            if (event.isShiftKey()) {
                // Calculcate the range of items between the anchor
                // point and the current item
                GridLazyDataView<SamplePerson> dataView = grid.getLazyDataView();
                int rangeStart = dataView.getItemIndex(rangeStartItem).get();
                int rangeEnd = dataView.getItemIndex(item).get();
                SamplePerson[] rangeItems = dataView.getItems()
                        .skip(Math.min(rangeStart, rangeEnd))
                        .limit(Math.abs(rangeStart - rangeEnd) + 1)
                        .toArray(SamplePerson[]::new);

                // Update the selection state of items within the range
                // based on the state of the current item
                if (event.isSelected()) {
                    selectionModel.selectItems(rangeItems);
                } else {
                    selectionModel.deselectItems(rangeItems);
                }
            }

            // Update the anchor point to the current item
            rangeStartItem = item;
        });
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        var layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing("20px");

        occupation = new TextField("Occupation");
        role = new TextField("Role");
        important = new Checkbox("Important");

        layout.add(occupation, role, important);

        editorDiv.add(layout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidth("100%");
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.addToStart(save);
        buttonLayout.addToEnd(cancel);
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

    private void onSave() {
        {
            try {
                if (CollectionUtils.isEmpty(this.selectedPeople)) {
                    Notification.show("Please select a person");
                }
                var tempEditorModel = new MultiUserEditorModel();

                binder.writeBean(tempEditorModel);

                for (var person : this.selectedPeople) {
                    person.setRole(tempEditorModel.getRole());
                    person.setOccupation(tempEditorModel.getOccupation());
                    person.setImportant(tempEditorModel.isImportant());
                    samplePersonService.save(person);
                }

                refreshGrid();
                Notification.show("Data updated");
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        }
    }

    public static class MultiUserEditorModel {
        private String occupation;
        private String role;
        private boolean important;

        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(String occupation) {
            this.occupation = occupation;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public boolean isImportant() {
            return important;
        }

        public void setImportant(boolean important) {
            this.important = important;
        }
    }
}
