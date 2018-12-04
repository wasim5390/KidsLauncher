package com.uiu.kids.event;

public class SlideEvent {

    int slideType;

    public boolean isCreateSlide() {
        return createSlide;
    }

    boolean createSlide=false;

    public SlideEvent(int slidType,boolean createSlide) {
        this.slideType = slidType;
        this.createSlide= createSlide;
    }

    public int getSlideType() {
        return slideType;
    }


}
