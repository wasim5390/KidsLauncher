package com.uiu.kids.ui.home.camera;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;

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
