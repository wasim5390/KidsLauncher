package com.wiser.kids.ui.favorite.links;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.wiser.kids.model.LinksEntity;
import com.wiser.kids.model.SlideItem;
import com.wiser.kids.source.Repository;
import com.wiser.kids.util.PreferenceUtil;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FavoriteLinksPresenter implements FavoriteLinksContract.Presenter {

    public FavoriteLinksContract.View view;
    public SlideItem slideItem;
    public PreferenceUtil preferenceUtil;
    public Repository repository;
    public Uri uri;
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

        uri=Uri.parse("https://"+link.toString()+"/favicon.ico");
        Log.e("uri", String.valueOf(uri));
        retrieveLinkData task =new retrieveLinkData();

        task.execute(link);


    }
    class retrieveLinkData extends AsyncTask<String, String, String> {

        String title;

        protected String doInBackground(String... urls) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://"+urls[0]);
                ResponseHandler<String> resHandler = new BasicResponseHandler();
                String page = httpClient.execute(httpGet, resHandler);

                return page;
            } catch (Exception e) {

                return null;
            }
        }

        protected void onPostExecute(String data) {

            Log.e("data from browser","aa"+data);
            getTitle(data);

        }
    }

    private void getTitle(String data) {

        String title=data.substring(data.indexOf("<title>")+7,data.indexOf("</title>"));
        Log.e("title",title);
        onAddFavLinkList(title,uri);
    }

    private void onAddFavLinkList(String title, Uri uri) {
        LinksEntity nodeEntity=new LinksEntity(title,uri);
        LinksEntity addNewEntity= mFavLinkList.get(mFavLinkList.size()-1);
        nodeEntity.setFlagEmptylist(false);
        mFavLinkList.remove(addNewEntity);
        mFavLinkList.add(nodeEntity);
        mFavLinkList.add(addNewEntity);
        view.onFavoriteLinksLoaded(mFavLinkList);

    }

}
