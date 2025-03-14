package com.kriss.me.views.clock;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("clock")
@PageTitle("Analog Clock")
public class ClockView extends VerticalLayout {
    
    public ClockView() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        
        ReactClockComponent clock = new ReactClockComponent();
        clock.setSize(300);
        add(clock);
    }
}
