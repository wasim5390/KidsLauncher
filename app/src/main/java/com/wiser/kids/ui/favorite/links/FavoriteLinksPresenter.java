package com.wiser.kids.ui.favorite.links;

import android.os.AsyncTask;
import android.util.Log;

import com.wiser.kids.model.SlideItem;
import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.favorite.fav_apps.FavoriteAppContract;
import com.wiser.kids.ui.home.apps.AppsEntity;
import com.wiser.kids.util.PreferenceUtil;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteLinksPresenter implements FavoriteLinksContract.Presenter {

    public FavoriteLinksContract.View view;
    public SlideItem slideItem;
    public PreferenceUtil preferenceUtil;
    public Repository repository;
    public List<LinksEntity> mFavLinkList=new ArrayList<LinksEntity>();


    public FavoriteLinksPresenter(FavoriteLinksContract.View view, SlideItem slideItem, PreferenceUtil preferenceUtil, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.preferenceUtil=preferenceUtil;
        this.mFavLinkList = new ArrayList<>();
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        LinksEntity linksEntity = new LinksEntity(null,null);
        linksEntity.setFlagEmptylist(true);
        mFavLinkList.add(linksEntity);
        view.onFavoriteLinksLoaded(mFavLinkList);
    }


    @Override
    public void getFavLinkData(String link) {

//        OpenGraph testPage = new OpenGraph("http://uk.rottentomatoes.com/m/1217700-kick_ass", true);



    }
//    class retrieveLinkData extends AsyncTask<String, String, String> {
//
//        protected String doInBackground(String... urls) {
//            try {
//                Log.e("link of browser",urls[0]);
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                HttpGet httpGet = new HttpGet("https://"+urls[0]);
//                ResponseHandler<String> resHandler = new BasicResponseHandler();
//                String page = httpClient.execute(httpGet, resHandler);
//
//                return page;
//            } catch (Exception e) {
//
//                return null;
//            }
//        }
//
//        protected void onPostExecute(String data) {
//
//            Log.e("data from browser","aa"+data);
//
//        }
//    }

}
