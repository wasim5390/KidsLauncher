package com.uiu.kids.ui.slides.links;


import android.net.Uri;
import android.util.Log;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.Constant;
import com.uiu.kids.model.LinksEntity;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.request.CreateSlideRequest;
import com.uiu.kids.model.request.FavLinkRequest;
import com.uiu.kids.model.response.CreateSlideResponse;
import com.uiu.kids.model.response.GetFavLinkIconResponce;
import com.uiu.kids.model.response.GetFavLinkResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.apps.AppsEntity;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.ResponseListener;
import io.github.ponnamkarthik.richlinkpreview.RichPreview;

import static com.uiu.kids.Constant.FAV_APP_SLIDE_NAME;
import static com.uiu.kids.Constant.FAV_LINK_SLIDE_NAME;

public class FavoriteLinksPresenter implements FavoriteLinksContract.Presenter {

    public FavoriteLinksContract.View view;
    public Slide slideItem;
    public PreferenceUtil preferenceUtil;
    public Repository repository;
    public Uri uri;
    private boolean isLoading=false;
    public boolean isLinkItemAdded = false;
    public List<LinksEntity> mFavLinkList;
    public List<LinksEntity> mDataList;


    public FavoriteLinksPresenter(FavoriteLinksContract.View view, Slide slideItem, PreferenceUtil preferenceUtil, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.preferenceUtil = preferenceUtil;
        this.mFavLinkList = new ArrayList<>();
        this.mDataList = new ArrayList<>();
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        LinksEntity linksEntity = new LinksEntity();
        linksEntity.setFlagEmptylist(true);
        mFavLinkList.add(linksEntity);

        view.onFavoriteLinksLoaded(mFavLinkList);
        view.slideSerial(slideItem.getSerial(),slideItem.getCount());
        List<LinksEntity> localList= preferenceUtil.getFavLinkList(slideItem.getId());
        if(localList!=null)
        loadFromLocal(localList);
        else
        loadFavLinks();
    }

    @Override
    public void loadFavLinks() {
        if(!Util.isInternetAvailable()) {
            view.showNoInternet();
            return;
        }
        if(!isLoading) {
            isLoading = true;
        }else{
            return;
        }

        repository.getFavLinks(slideItem.getId(), new DataSource.GetDataCallback<GetFavLinkResponse>() {
            @Override
            public void onDataReceived(GetFavLinkResponse data) {
                isLoading=false;
                if (data.isSuccess()) {
                    preferenceUtil.saveLinkList(slideItem.getId(),data.getFavLinkList());
                    mDataList.clear();
                    mFavLinkList.clear();
                    mDataList.addAll(data.getFavLinkList());
                    mFavLinkList.addAll(mDataList);

                    if(mFavLinkList.size()<4)
                        mFavLinkList.add(new LinksEntity());
                    view.onFavoriteLinksLoaded(mFavLinkList);
                } else {
                    view.showMassage(data.getResponseMsg());

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
        if(preferenceUtil.getAccount().getPrimaryHelper()==null ||
                !preferenceUtil.getAccount().getPrimaryHelper().isPrimaryConnected())
            return;
        isLinkItemAdded = true;
        uri = Uri.parse("https://" + link + "/favicon.ico");
        Log.e("uri", String.valueOf(uri));

        for (int i = 0; i < mFavLinkList.size(); i++) {

            if (link.equalsIgnoreCase(mFavLinkList.get(i).getShort_url())) {
                isLinkItemAdded = false;
            }
        }
        if (isLinkItemAdded) {
            String prefix = "http";
            if(!link.startsWith(prefix))
                link = "http://"+link;
            getIcon(link);

        } else {
            view.showMassage("You have already add this link");
        }

    }


    private void getIcon(String link) {
        view.showProgress();
        RichPreview richPreview = new RichPreview(new ResponseListener() {


            @Override
            public void onData(MetaData metaData) {
                String imageUrl = metaData.getImageurl();
                String favIcon = metaData.getFavicon();
                if(imageUrl==null || imageUrl.isEmpty())
                    imageUrl = favIcon;
                //Implement your Layout
                LinksEntity linksEntity = composeLinkEntity(metaData.getUrl(), metaData.getTitle(), imageUrl,metaData.getDescription());
                view.onFavoriteLinkDataLoaded(metaData.getUrl(),linksEntity);
            }

            @Override
            public void onError(Exception e) {
                //handle error
                LinksEntity linksEntity = composeLinkEntity(link, "", "www.empty","");
                view.onFavoriteLinkDataLoaded(link,linksEntity);

            }
        });
        richPreview.getPreview(link);



    }

    private LinksEntity composeLinkEntity(String link,String title, String imageUrl, String description){
        LinksEntity nodeEntity = new LinksEntity(link,title,description, imageUrl);
        nodeEntity.setShort_url(link);
        nodeEntity.setRequestStatus(1);
        nodeEntity.setSlide_id(slideItem.getId());
        nodeEntity.setUser_id(preferenceUtil.getAccount().getId());
        nodeEntity.setFlagEmptylist(false);
        return nodeEntity;


    }
    @Override
    public void addFavLinkOnSlide(LinksEntity entity) {

        if(preferenceUtil.getAccount().getPrimaryHelper()==null ||
                !preferenceUtil.getAccount().getPrimaryHelper().isPrimaryConnected())
            return;

        if(isLastSlide() && mDataList.size()>=4){
            addNewSlide(entity);
            return;
        }else{
            entity.setSlide_id(slideItem.getId());
            saveLinkOnSlide(entity,null);
        }


    }

    @Override
    public boolean canAddOnSlide() {
        if(!isLastSlide() && mDataList.size()>=4)
        {
            view.showMassage(Constant.NO_SPACE_ON_SLIDE);
            return false;
        }
        return true;
    }

    private void saveLinkOnSlide(LinksEntity entity,Slide newSlide) {
        if(!Util.isInternetAvailable()) {
            view.showNoInternet();
            return;
        }
        if(entity.getLink().isEmpty())
            return;
        FavLinkRequest request = new FavLinkRequest();
        request.setLink(entity);

        repository.addFavLinkToSlide(request, new DataSource.GetDataCallback<GetFavLinkResponse>() {
            @Override
            public void onDataReceived(GetFavLinkResponse data) {
                view.hideProgress();
                if(data.isSuccess()) {
                    preferenceUtil.saveLink(entity.getSlide_id(),data.getLinkEntity());
                    mDataList.clear();
                    mFavLinkList.clear();
                    mDataList.addAll(preferenceUtil.getFavLinkList(entity.getSlide_id()));
                    mFavLinkList.addAll(mDataList);
                    if(mFavLinkList.size()<4)
                        mFavLinkList.add(new LinksEntity());

                    if(newSlide!=null)
                        view.itemAddedOnNewSlide(newSlide);
                    view.onFavoriteLinksLoaded(mFavLinkList);
                }else{
                    view.showMassage(data.getResponseMsg());
                }

            }
            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMassage(message);

            }
        });
    }

    public void addNewSlide(LinksEntity linksEntity){
        if(!Util.isInternetAvailable()) {
            view.showNoInternet();
            return;
        }
        Slide newSlide = new Slide();
        newSlide.setUser_id(preferenceUtil.getAccount().getId());
        newSlide.setType(Constant.SLIDE_INDEX_FAV_LINKS);
        newSlide.setName(FAV_LINK_SLIDE_NAME);
        CreateSlideRequest slideRequest = new CreateSlideRequest();
        slideRequest.setSlide(newSlide);
        repository.createSlide(slideRequest, new DataSource.GetDataCallback<CreateSlideResponse>() {
            @Override
            public void onDataReceived(CreateSlideResponse data) {
                if(data.isSuccess()){
                    view.onNewSlideCreated(data.getSlideItem());
                    linksEntity.setSlide_id(data.getSlideItem().getId());
                    saveLinkOnSlide(linksEntity,data.getSlideItem());
                    //
                }else
                    view.showMassage(data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {

            }
        });
    }
    private void loadFromLocal(List<LinksEntity> localList){

        mFavLinkList.clear();
        mFavLinkList.addAll(localList);
        mDataList.clear();
        mDataList.addAll(localList);
        if(mFavLinkList.size()<4)
            mFavLinkList.add(new LinksEntity());
        view.onFavoriteLinksLoaded(mFavLinkList);
    }


    private boolean isLastSlide(){
        Integer actualSerial = slideItem.getSerial()+1;
        return actualSerial>=slideItem.getCount();
    }
}
