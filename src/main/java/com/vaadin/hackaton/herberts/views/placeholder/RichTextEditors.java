package com.vaadin.hackaton.herberts.views.placeholder;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Rich Text editors")
@Route("rich-text")
@Menu(order = 2, icon = LineAwesomeIconUrl.ENVELOPE_OPEN_TEXT_SOLID)
@RolesAllowed("USER")
public class RichTextEditors extends VerticalLayout {
    public RichTextEditors() {
        setSizeFull();

        add(new H2("On its own"));
        add(new RichTextEditor());

        add(new H2("Div height 6000px"));

        var nestedDiv = new Div();
        nestedDiv.setHeight("6000px");
        nestedDiv.setWidth("500px");
        nestedDiv.getStyle().setBackgroundColor("pink");
        add(nestedDiv);
    }
}
