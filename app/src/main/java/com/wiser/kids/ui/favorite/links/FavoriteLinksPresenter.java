package com.wiser.kids.ui.favorite.links;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.model.LinksEntity;
import com.wiser.kids.model.SlideItem;
import com.wiser.kids.model.response.GetFavLinkResponce;
import com.wiser.kids.source.DataSource;
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
    public boolean isLinkItemAdded=true;
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
        isLinkItemAdded=true;
        uri=Uri.parse("https://"+link.toString()+"/favicon.ico");
        Log.e("uri", String.valueOf(uri));

        for (int i=0;i<mFavLinkList.size();i++) {
            if (link.equals(mFavLinkList.get(i).link))
            {
                isLinkItemAdded=false;
            }
        }
        if (isLinkItemAdded)
        {

            getIcon(link);
            view.showProgressbar();
            }
        else
        {
        view.showMassage("You have already add this link");
        }

    }

    private void getIcon(String link) {

        link="https://"+link;
        String finalLink = link;
        Log.e("link",finalLink);
        repository.getFavLinkIcon(link, new DataSource.GetDataCallback<GetFavLinkResponce>() {
            @Override
            public void onDataReceived(GetFavLinkResponce data) {
                if (data!=null)
                {
                    if (data.getIcons.size()>0) {
                        uri = Uri.parse(data.getIcons.get(0).url);

                    }
                    onAddFavLinkList(finalLink,uri);
                }
            }

            @Override
            public void onFailed(int code, String message) {
                Log.e("error",message);
                onAddFavLinkList(finalLink,uri);

            }
        });

    }


//    class retrieveLinkData extends AsyncTask<String, String, String> {
//
//        String title;
//
//        protected String doInBackground(String... urls) {
//            try {
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                HttpGet httpGet = new HttpGet("https://"+urls[0]);
//                ResponseHandler<String> resHandler = new BasicResponseHandler();
//                String page = httpClient.execute(httpGet, resHandler);
//
//                return page;
//            } catch (Exception e) {
//
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        protected void onPostExecute(String data) {
//
//            Log.e("data from browser","aa"+data);
//            getTitle(data);
//
//        }
//    }
//
//    private void getTitle(String data) {
//
//        view.hideProgressbar();
//
//        String title=data.substring(data.indexOf("<title>")+7,data.indexOf("</title>"));
//        Log.e("title",title);
//        onAddFavLinkList(title,uri);
//    }

//
//    public void getShortenerLink(String link)
//    {
//        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLink(Uri.parse("https://www.example.com/"))
//                .setDynamicLinkDomain("example.page.link")
//                // Set parameters
//                // ...
//                .buildShortDynamicLink()
//                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
//                    @Override
//                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
//                        if (task.isSuccessful()) {
//                            // Short link created
//                            Uri shortLink = task.getResult().getShortLink();
//                            Uri flowchartLink = task.getResult().getPreviewLink();
//                        } else {
//                            // Error
//                            // ...
//                        }
//                    }
//                });
//    }

    private void onAddFavLinkList(String link, Uri uri) {
        view.hideProgressbar();
        LinksEntity nodeEntity=new LinksEntity(link,uri);
        LinksEntity addNewEntity= mFavLinkList.get(mFavLinkList.size()-1);
        nodeEntity.setFlagEmptylist(false);
        mFavLinkList.remove(addNewEntity);
        mFavLinkList.add(nodeEntity);
        mFavLinkList.add(addNewEntity);
        view.onFavoriteLinksLoaded(mFavLinkList);

    }

}
