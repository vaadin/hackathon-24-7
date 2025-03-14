package com.kriss.me.data;

import jakarta.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class Bindge extends AbstractEntity {

    private LocalDateTime time;
    private String food;
    private String volume;
    private String place;
    private boolean wasTooMuch;
    private String context;

    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public String getFood() {
        return food;
    }
    public void setFood(String food) {
        this.food = food;
    }
    public String getVolume() {
        return volume;
    }
    public void setVolume(String volume) {
        this.volume = volume;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public boolean isWasTooMuch() {
        return wasTooMuch;
    }
    public void setWasTooMuch(boolean wasTooMuch) {
        this.wasTooMuch = wasTooMuch;
    }
    public String getContext() {
        return context;
    }
    public void setContext(String context) {
        this.context = context;
    }

}
