package com.uiu.kids.ui.home.contact.info;

import android.text.TextUtils;

import com.uiu.kids.Constant;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.model.User;
import com.uiu.kids.model.response.UploadProfileImageResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.ui.home.contact.ContactLoader;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ContactInfoPresenter implements ContactInfoContract.Presenter,Constant {

    private ContactInfoContract.View view;
    private Repository repository;
    private ContactEntity contact;
    private PreferenceUtil preferenceUtil;
    private boolean calledFromHome=true;

    public ContactInfoPresenter(ContactInfoContract.View view, ContactEntity contact,PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.contact = contact;
        this.repository = repository;
        this.preferenceUtil = preferenceUtil;
        this.view.setPresenter(this);
    }

    @Override
    public void loadContact() {
        if(contact!=null)
        view.onContactLoaded(contact);

    /*    if (!TextUtils.isEmpty(contact.getLookupId()) && !TextUtils.isEmpty(contact.getAndroidId())) {
            if (calledFromHome || !Util.isInternetAvailable()) {
                ContactEntity mContact = ContactLoader.buildContactFromDb(contact.getAndroidId(), contact.getLookupId(), KidsLauncherApp.getInstance());
                view.onContactLoaded(mContact);
            }
        }else{

            ContactEntity mContact=null;
            if(!contact.getPhoneNumbersList().isEmpty())
                mContact = ContactLoader.getInstance(KidsLauncherApp.getInstance()).getContactEntityByNumber(contact.getPhoneNumbersList().get(0).toString(), contact.getName());
            view.onContactLoaded(mContact==null?contact:new ContactEntity());
        }*/
    }

    @Override
    public void getContactType(ContactEntity contactEntity) {
        this.contact = contactEntity;
        if (!TextUtils.isEmpty(contactEntity.getMobileNumber())) {
            view.onContactTypeMobile();
            return;
        }
        //no mobile number, check home number
        if (!TextUtils.isEmpty(contactEntity.getmHomeNumber())) {
            //user has home number
            view.onContactTypeHome();
            return;
        }
        // no home number, check email
        if (!TextUtils.isEmpty(contactEntity.getEmail())) {
            //has email
            view.onContactTypeEmail();
            return;
        }
    }

    @Override
    public void updateContactPic(File imageFile) {
        if(!Util.isInternetAvailable())
            return;
        view.showProgress();
        RequestBody fBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("contact_icon",imageFile.getName(),fBody);

        RequestBody contactId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(contact.getId()));
        RequestBody slideId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(contact.getSlide_id()));

        HashMap<String, RequestBody> params = new HashMap<>();
        params.put("contact_id", contactId);
       // params.put("slide_id",slideId);
        repository.updateContactImage(params, body, new DataSource.GetResponseCallback<UploadProfileImageResponse>() {
            @Override
            public void onSuccess(UploadProfileImageResponse response) {
                if(response.getLink()!=null)
                {
                    contact.setBase64ProfilePic(response.getLink());
                    preferenceUtil.updateFavPeople(contact.getSlide_id(),contact);
                    view.onContactUpdated(contact);

                }
                view.hideProgress();
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });
    }

    @Override
    public void start() {

    }
}
