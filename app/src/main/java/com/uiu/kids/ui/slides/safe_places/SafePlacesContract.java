package com.uiu.kids.ui.slides.safe_places;


import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.model.Location;
import com.uiu.kids.model.Slide;


import java.util.List;

public class SafePlacesContract {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onDirectionsLoaded(List<Location> list);
        void slideSerial(int serial, int count);
        void onSlideCreated(Slide slide);
        void onSlideRemoved(Slide slide);
    }

    interface Presenter extends BasePresenter {
        void loadDirections(String slideId);

    }
}
