package com.kriss.me.views.clock;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

@Tag("react-clock")
@NpmPackage(value = "react", version = "18.2.0")
@NpmPackage(value = "react-dom", version = "18.2.0")
@JsModule("./components/react-clock-wrapper.ts")
public class ReactClockComponent extends Component {
    
    public ReactClockComponent() {
        // The actual implementation is in the TypeScript wrapper
    }
    
    public void setSize(int size) {
        getElement().setProperty("size", size);
    }
}
