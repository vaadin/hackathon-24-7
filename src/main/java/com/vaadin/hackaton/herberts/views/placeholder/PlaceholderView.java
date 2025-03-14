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

        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

        H2 header = new H2("This place intentionally left empty");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph("It’s a place where you can grow your own UI 🤗"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

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
        richTextEditor.getStyle().setPaddingBottom("20px").setMarginBottom("20px");


        add(new TextField("Text field"),
                new TextArea("Text Area"),
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
