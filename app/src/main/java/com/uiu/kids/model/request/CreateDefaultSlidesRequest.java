package com.uiu.kids.model.request;

import com.uiu.kids.model.Slide;

import java.util.List;

public class CreateDefaultSlidesRequest {

    public List<Slide> getSlides() {
        return slide;
    }

    List<Slide> slide;

    public void setDefaultSlides(List<Slide> slides) {
        this.slide = slides;
    }

}
