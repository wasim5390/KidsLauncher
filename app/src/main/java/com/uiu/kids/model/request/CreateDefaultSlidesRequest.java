package com.uiu.kids.model.request;

import com.uiu.kids.model.SlideItem;

import java.util.List;

public class CreateDefaultSlidesRequest {

    public List<SlideItem> getSlides() {
        return slide;
    }

    List<SlideItem> slide;

    public void setDefaultSlides(List<SlideItem> slides) {
        this.slide = slides;
    }

}
