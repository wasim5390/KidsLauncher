package com.uiu.kids.notifications;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.uiu.kids.Constant;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.event.NotificationReceiveEvent;
import com.uiu.kids.model.Invitation;
import com.uiu.kids.model.LinksEntity;
import com.uiu.kids.model.User;
import com.uiu.kids.model.response.InvitationResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.apps.AppsEntity;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.ui.slides.reminder.ReminderEntity;
import com.uiu.kids.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SlidesDataUpdater implements Constant{

    private static final String TAG = SlidesDataUpdater.class.getSimpleName();
    static SlidesDataUpdater mInstance;
    private PreferenceUtil preferenceUtil= PreferenceUtil.getInstance(KidsLauncherApp.getInstance());
    private User user = PreferenceUtil.getInstance(KidsLauncherApp.getInstance()).getAccount();
    public static SlidesDataUpdater getInstance() {
        if(mInstance==null)
            mInstance = new SlidesDataUpdater();
        return mInstance;
    }

    protected void update(JSONObject object,int notificationType, int status){
        switch (notificationType){
            case SLIDE_INDEX_FAV_PEOPLE:
                updateContactSlide(object,status);
                break;
            case SLIDE_INDEX_FAV_APP:
                updateAppsSlide(object,status);
                break;
            case SLIDE_INDEX_FAV_LINKS:
                updateLinkSlide(object,status);
                break;
            case SLIDE_INDEX_REMINDERS:
                updateReminderSlide(object,status);
                break;
            case SLIDE_INDEX_SOS:
                updateSOSList(object,status);
                break;
            case INVITE_CODE:
                updateInvites(object,status);
                break;
        }
    }

    private void updateContactSlide(JSONObject jsonObject,int status){

        try {
            ContactEntity entityToUpdate=null;
            String slideId = jsonObject.getString("slide_id");
            List<ContactEntity> favoriteContacts =
                    preferenceUtil.getFavPeopleList(slideId);
            favoriteContacts=favoriteContacts==null?new ArrayList<>():favoriteContacts;
            ContactEntity entity = new Gson().fromJson(jsonObject.toString(), ContactEntity.class);

            for(ContactEntity contactEntity:favoriteContacts){
                if(contactEntity.getId().equals(entity.getId()))
                {
                    entityToUpdate = contactEntity;
                    break;
                }
            }

             if(entityToUpdate==null  && status==ACCEPTED)
                favoriteContacts.add(entity);
            else if(entityToUpdate!=null && status!=REJECTED) {
                int index = favoriteContacts.indexOf(entityToUpdate);
                favoriteContacts.get(index).setRequestStatus(status);

             }
            else if(entityToUpdate!=null && status==REJECTED)
                favoriteContacts.remove(entityToUpdate);


            preferenceUtil.saveFavPeople(slideId,favoriteContacts);



        }catch (JsonSyntaxException exception){
            Log.e(TAG,exception.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EventBus.getDefault().postSticky(new NotificationReceiveEvent(Constant.SLIDE_INDEX_FAV_PEOPLE,true));

    }

    private void updateAppsSlide(JSONObject jsonObject,int status){

        try {
            AppsEntity entityToUpdate=null;
            String slideId = jsonObject.getString("slide_id");
            List<AppsEntity> favoriteApps =
                    preferenceUtil.getFavAppsList(slideId);
            favoriteApps=favoriteApps==null?new ArrayList<>():favoriteApps;
            AppsEntity entity = new Gson().fromJson(jsonObject.toString(), AppsEntity.class);

            for(AppsEntity appsEntity:favoriteApps){
                if(appsEntity.getId().equals(entity.getId()))
                {
                    entityToUpdate = appsEntity;
                    break;
                }
            }
            if(entityToUpdate==null  && status==ACCEPTED)
                favoriteApps.add(entity);
            else if(entityToUpdate!=null && status!=REJECTED) {
                int index = favoriteApps.indexOf(entityToUpdate);
                favoriteApps.get(index).setRequestStatus(status);

            }
            else if(entityToUpdate!=null && status==REJECTED)
                favoriteApps.remove(entityToUpdate);


            preferenceUtil.saveFavApps(slideId,favoriteApps);



        }catch (JsonSyntaxException exception){
            Log.e(TAG,exception.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EventBus.getDefault().postSticky(new NotificationReceiveEvent(Constant.SLIDE_INDEX_FAV_APP,true));

    }

    private void updateLinkSlide(JSONObject jsonObject,int status){

        try {
            LinksEntity entityToUpdate=null;
            String slideId = jsonObject.getString("slide_id");
            List<LinksEntity> favoriteLinks =
                    preferenceUtil.getFavLinkList(slideId);
            favoriteLinks=favoriteLinks==null?new ArrayList<>():favoriteLinks;
            LinksEntity entity = new Gson().fromJson(jsonObject.toString(), LinksEntity.class);

            for(LinksEntity appsEntity:favoriteLinks){
                if(appsEntity.getId().equals(entity.getId()))
                {
                    entityToUpdate = appsEntity;
                    break;
                }
            }
            if(entityToUpdate==null  && status==ACCEPTED)
                favoriteLinks.add(entity);
            else if(entityToUpdate!=null && status!=REJECTED) {
                int index = favoriteLinks.indexOf(entityToUpdate);
                favoriteLinks.get(index).setRequestStatus(status);

            }
            else if(entityToUpdate!=null && status==REJECTED)
                favoriteLinks.remove(entityToUpdate);

            preferenceUtil.saveLinkList(slideId,favoriteLinks);



        }catch (JsonSyntaxException exception){
            Log.e(TAG,exception.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EventBus.getDefault().postSticky(new NotificationReceiveEvent(Constant.SLIDE_INDEX_FAV_LINKS,true));

    }

    private void updateReminderSlide(JSONObject jsonObject,int status){

        try {
            ReminderEntity entityToUpdate=null;
            String slideId = jsonObject.getString("slide_id");
            List<ReminderEntity> reminders =
                    preferenceUtil.getReminderList(slideId);
            reminders=reminders==null?new ArrayList<>():reminders;
            ReminderEntity entity = new Gson().fromJson(jsonObject.toString(), ReminderEntity.class);

            for(ReminderEntity reminderEntity:reminders){
                if(reminderEntity.getId().equals(entity.getId()))
                {
                    entityToUpdate = reminderEntity;
                    break;
                }
            }
            if(entityToUpdate==null  && status==ACCEPTED)
                reminders.add(entity);
            else if(entityToUpdate!=null && status!=REJECTED) {
                int index = reminders.indexOf(entityToUpdate);
                reminders.get(index).setRequestStatus(status);

            }
            else if(entityToUpdate!=null && status==REJECTED)
                reminders.remove(entityToUpdate);
            preferenceUtil.saveReminders(slideId,reminders);



        }catch (JsonSyntaxException exception){
            Log.e(TAG,exception.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EventBus.getDefault().postSticky(new NotificationReceiveEvent(Constant.SLIDE_INDEX_REMINDERS,true));

    }

    private void updateSOSList(JSONObject jsonObject,int status){

        try {
            ContactEntity entityToUpdate=null;
            List<ContactEntity> sosList = preferenceUtil.getAllSosList();
            sosList=sosList==null?new ArrayList<>():sosList;
            ContactEntity entity = new Gson().fromJson(jsonObject.toString(), ContactEntity.class);

            for(ContactEntity sosEntity:sosList){
                if(sosEntity.getId().equals(entity.getId()))
                {
                    entityToUpdate = sosEntity;
                    break;
                }
            }
            if(entityToUpdate==null  && status==ACCEPTED)
                sosList.add(entity);
            else if(entityToUpdate!=null && status!=REJECTED) {
                int index = sosList.indexOf(entityToUpdate);
                sosList.get(index).setRequestStatus(status);

            }
            else if(entityToUpdate!=null && status==REJECTED)
                sosList.remove(entityToUpdate);
            preferenceUtil.saveAllSos(sosList);

        }catch (JsonSyntaxException exception){
            Log.e(TAG,exception.getMessage());
        }

        EventBus.getDefault().postSticky(new NotificationReceiveEvent(Constant.SLIDE_INDEX_SOS,true));

    }

    private void updateInvites(JSONObject jsonObject,int status){

        Repository.getInstance().getInvites(preferenceUtil.getAccount().getId(),
                new DataSource.GetDataCallback<InvitationResponse>() {
                    @Override
                    public void onDataReceived(InvitationResponse data) {
                        if(data.isSuccess()) {
                            User user = preferenceUtil.getAccount();
                            user.setInvitations(data.getInvitationList());
                            preferenceUtil.saveAccount(user);
                            EventBus.getDefault().postSticky(new NotificationReceiveEvent(Constant.INVITE_CODE,true));
                        }
                    }

                    @Override
                    public void onFailed(int code, String message) {

                    }
                });



    }
}
