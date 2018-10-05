package com.uiu.kids.event;

import com.uiu.kids.model.Slide;

public class SlideCreateEvent  {


    Slide slide;

    public SlideCreateEvent(Slide slide) {
        this.slide = slide;
    }

    public Slide getSlide() {
        return slide;
    }
}
