package com.wiser.kids.ui.favorite.links;


import android.net.Uri;
import android.util.Log;

import com.wiser.kids.model.LinksEntity;
import com.wiser.kids.model.SlideItem;
import com.wiser.kids.model.request.FavLinkRequest;
import com.wiser.kids.model.response.GetFavLinkIconResponce;

import com.wiser.kids.model.response.GetFavLinkResponse;
import com.wiser.kids.source.DataSource;
import com.wiser.kids.source.Repository;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class FavoriteLinksPresenter implements FavoriteLinksContract.Presenter {

    public FavoriteLinksContract.View view;
    public SlideItem slideItem;
    public PreferenceUtil preferenceUtil;
    public Repository repository;
    public Uri uri;
    public boolean isLinkItemAdded = false;
    public List<LinksEntity> mFavLinkList = new ArrayList<LinksEntity>();


    public FavoriteLinksPresenter(FavoriteLinksContract.View view, SlideItem slideItem, PreferenceUtil preferenceUtil, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.preferenceUtil = preferenceUtil;
        this.mFavLinkList = new ArrayList<>();
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        LinksEntity linksEntity = new LinksEntity(null, null);
        linksEntity.setFlagEmptylist(true);
        mFavLinkList.add(linksEntity);
        view.onFavoriteLinksLoaded(mFavLinkList);
        loadFavLinks();
    }

    @Override
    public void loadFavLinks() {

        repository.getFavLinks(slideItem.getId(), new DataSource.GetDataCallback<GetFavLinkResponse>() {
            @Override
            public void onDataReceived(GetFavLinkResponse data) {
                if (data.isSuccess()) {
                    LinksEntity addNewEntity = mFavLinkList.get(mFavLinkList.size() - 1);
                    mFavLinkList.clear();
                    mFavLinkList.addAll(data.getFavLinkList());
                    mFavLinkList.add(addNewEntity);
                    view.onFavoriteLinksLoaded(mFavLinkList);
                } else {
                    view.onFavoriteLinksLoaded(mFavLinkList);

                }
            }

            @Override
            public void onFailed(int code, String message) {

                view.showMassage(message);
            }
        });
    }


    @Override
    public void updateFavLink(LinksEntity favLink) {
        for(LinksEntity linksEntity: mFavLinkList){
            if(linksEntity!=null &&linksEntity.getId()!=null && linksEntity.getId().equals(favLink.getId())){
                linksEntity.setRequestStatus(favLink.getRequestStatus());
                break;
            }
        }
        view.onFavoriteLinksLoaded(mFavLinkList);
    }

    @Override
    public void getFavLinkData(String link) {
        isLinkItemAdded = true;
        uri = Uri.parse("https://" + link.toString() + "/favicon.ico");
        Log.e("uri", String.valueOf(uri));

        for (int i = 0; i < mFavLinkList.size(); i++) {

            if (link.equalsIgnoreCase(mFavLinkList.get(i).getShort_url())) {
                isLinkItemAdded = false;
            }
        }
        if (isLinkItemAdded) {

            getIcon(link);

        } else {
            view.showMassage("You have already add this link");
        }

    }


    private void getIcon(String link) {

        String finalLink = "https://" + link;
        Log.e("link", finalLink);
        view.showProgressbar();
        repository.getFavLinkIcon(link, new DataSource.GetDataCallback<GetFavLinkIconResponce>() {
            @Override
            public void onDataReceived(GetFavLinkIconResponce data) {
                if (data != null) {
                    if (data.getIcons.size() > 0) {
                        uri = Uri.parse(data.getIcons.get(0).url);

                    }
                    onAddFavLinkList(link, finalLink, uri);
                }
            }

            @Override
            public void onFailed(int code, String message) {
                Log.e("error", message);
                onAddFavLinkList(link, finalLink, uri);

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

    private void onAddFavLinkList(String link, String completeLink, Uri uri) {


        LinksEntity nodeEntity = new LinksEntity(completeLink, uri);
        nodeEntity.setIcon_url(uri.toString());
        nodeEntity.setShort_url(link);
        nodeEntity.setRequestStatus(1);
        nodeEntity.setSlide_id(slideItem.getId());
        nodeEntity.setUser_id(preferenceUtil.getAccount().getId());
        nodeEntity.setFlagEmptylist(false);
        FavLinkRequest request = new FavLinkRequest();
        request.setLink(nodeEntity);

        repository.addFavLinkToSlide(request, new DataSource.GetDataCallback<GetFavLinkResponse>() {
            @Override
            public void onDataReceived(GetFavLinkResponse data) {
                LinksEntity addNewEntity = mFavLinkList.get(mFavLinkList.size() - 1);
                mFavLinkList.remove(addNewEntity);
                mFavLinkList.add(data.getLinkEntity());
                mFavLinkList.add(addNewEntity);
                view.onFavoriteLinksLoaded(mFavLinkList);
                view.hideProgressbar();
            }
            @Override
            public void onFailed(int code, String message) {
                view.hideProgressbar();
                view.showMassage(message);

            }
        });
    }
}
