package com.wiser.kids.ui.home.camera;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

public class CameraContract {


    interface View extends BaseView<Presenter>
    {
        void cameraLoaded();
    }

    interface Presenter extends BasePresenter
    {
        void cameraLoad();
    }
}
