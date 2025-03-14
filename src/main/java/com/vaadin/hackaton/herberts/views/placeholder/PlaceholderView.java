package com.vaadin.hackaton.herberts.views.placeholder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Placeholder")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.FILE)
@RolesAllowed("USER")
public class PlaceholderView extends VerticalLayout {

    public PlaceholderView() {
        setSpacing(false);
        setSizeFull();

        MessageInput messageInput = new MessageInput();
        messageInput.addSubmitListener(submitEvent -> {
            Notification.show("Message received: " + submitEvent.getValue(),
                    3000, Notification.Position.MIDDLE);
        });

        var radioGroup = new RadioButtonGroup<>("Radio buttons");
        radioGroup.setItems("1", "2", "3");

        var richTextCustomField = new MyCustomRichField("My custom field");
        richTextCustomField.getStyle().setPaddingBottom("20px");

        var richTextEditor = new RichTextEditor();

        var textArea1 = new TextArea("Text Area 1");
        textArea1.setMinRows(1);
        var textArea2 = new TextArea("Text Area 2");
        textArea2.setMinRows(2);

        var regularButton = new Button("Regular Button");
        regularButton.setTooltipText("This is always enabled");

        var disabledButton = new Button("Disabled Button");
        disabledButton.setTooltipText("This is disabled for a reason;");
        disabledButton.setEnabled(false);
        disabledButton.addClickListener(event -> Notification.show("Disabled click"));

        add(regularButton, disabledButton);

        add(new TextField("Text field"),
                textArea1,
                textArea2,
                new EmailField("Email field"),
                new NumberField("Number field"),
                new PasswordField("Password field"),
                radioGroup,
                new Checkbox("Checkbox"),
                new Select<>("", event -> {}),
                new MultiSelectComboBox<>("Multi select"),
                new Button("Button"),
                new DatePicker("Date Picker"),
                new DateTimePicker("Date time picker"),
                new TimePicker("Time picker"),
                messageInput,
                richTextEditor,
                new MyCustomField("My custom field"),
                richTextCustomField
        );
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

    public static class MyCustomRichField extends CustomField<String> {

        private final RichTextEditor editor;

        public MyCustomRichField(String label) {
            setLabel(label);

            editor = new RichTextEditor();

            add(editor);
        }

        @Override
        protected String generateModelValue() {
            return editor.getValue();
        }

        @Override
        protected void setPresentationValue(String s) {
            editor.setValue(s);
        }
    }
}
