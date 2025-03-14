package com.vaadin.hackaton.herberts.views.placeholder;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.textfield.TextField;
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


        add(new H2("In vertical layout"));

        var verticalLayout = new VerticalLayout();
        verticalLayout.add(new RichTextEditor());

        add(verticalLayout);

        add(new H2("In vertical layout but not last"));

        MessageInput messageInput = new MessageInput();
        messageInput.addSubmitListener(submitEvent -> {
            Notification.show("Message received: " + submitEvent.getValue(),
                    3000, Notification.Position.MIDDLE);
        });

        var verticalLayout2 = new VerticalLayout();
        verticalLayout2.add(new TextField("first item in VL"));
        verticalLayout2.add(messageInput);
        verticalLayout2.add(new RichTextEditor());
        verticalLayout2.add(new MyCustomField("My custom field"));
        verticalLayout2.add(new TextField("last item in VL"));

        add(verticalLayout2);

    }


    public static class MyCustomField extends CustomField<String> {

        private final TextField textField1;
        private final TextField textField2;

        public MyCustomField(String label) {
            setLabel(label);

            textField1 = new TextField();
            textField2 = new TextField();

            add(textField1, textField2);
        }

        @Override
        protected String generateModelValue() {
            return textField1.getValue() + ";" + textField2.getValue();
        }

        @Override
        protected void setPresentationValue(String s) {
            if (s == null) {
                textField1.setValue("");
                textField2.setValue("");
                return;
            }

            var values = s.split(";");
            if (values.length == 2) {
                textField1.setValue(values[0]);
                textField2.setValue(values[1]);
            } else if (values.length == 1) {
                textField1.setValue(values[0]);
                textField2.setValue("");
            } else {
                textField1.setValue("");
                textField2.setValue("");
            }
        }
    }
}
