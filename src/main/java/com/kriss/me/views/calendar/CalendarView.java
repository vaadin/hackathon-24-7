package com.kriss.me.views.calendar;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import java.time.LocalDate;

@PageTitle("Calendar")
@Route("calendar")
@Menu(order = 1, icon = LineAwesomeIconUrl.CALENDAR_ALT_SOLID)
public class CalendarView extends VerticalLayout {

    public CalendarView() {
        setSizeFull();

        FullCalendar calendar = FullCalendarBuilder.create().build();
        add(calendar);
        setFlexGrow(1, calendar);
        calendar.setWidthFull();

// Create a initial sample entry
        Entry entry = new Entry();
        entry.setTitle("Some event");
        entry.setColor("#ff3333");

// the given times will be interpreted as utc based - useful when the times are fetched from your database
        entry.setStart(LocalDate.now().withDayOfMonth(3).atTime(10, 0));
        entry.setEnd(entry.getStart().plusHours(2));

// FC uses a data provider concept similar to the Vaadin default's one, with some differences
// By default the FC uses a in-memory data provider, which is sufficient for most basic use cases.
        calendar.getEntryProvider().asInMemory().addEntries(entry);
    }
}
