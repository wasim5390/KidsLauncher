package com.uiu.kids;

/**
 * Created by sidhu on 4/11/2018.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
    void showNoInternet();
    void showProgress();
    void hideProgress();
}
