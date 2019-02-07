package com.uiu.kids.notifications;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.uiu.kids.Constant;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.event.notification.AppNotificationEvent;
import com.uiu.kids.event.notification.InviteNotificationEvent;
import com.uiu.kids.event.notification.LinkNotificationEvent;
import com.uiu.kids.event.notification.NotificationReceiveEvent;
import com.uiu.kids.event.notification.PeopleNotificationEvent;
import com.uiu.kids.event.notification.ReminderNotificationEvent;
import com.uiu.kids.event.notification.SafePlacesNotificationEvent;
import com.uiu.kids.model.Invitation;
import com.uiu.kids.model.LinksEntity;
import com.uiu.kids.model.LocalNotificationModel;
import com.uiu.kids.model.Location;
import com.uiu.kids.model.NotificationSender;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.User;
import com.uiu.kids.model.response.InvitationResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.dashboard.DashboardPresenter;
import com.uiu.kids.ui.home.apps.AppsEntity;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.ui.slides.reminder.ReminderEntity;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SlidesDataUpdater implements Constant{

    private static final String TAG = SlidesDataUpdater.class.getSimpleName();
    static SlidesDataUpdater mInstance;
    private PreferenceUtil preferenceUtil= PreferenceUtil.getInstance(KidsLauncherApp.getInstance());

    public static SlidesDataUpdater getInstance() {
        if(mInstance==null)
            mInstance = new SlidesDataUpdater();
        return mInstance;
    }

    protected void update(int notificationType,LocalNotificationModel notificationModel){
        switch (notificationType){
            case SLIDE_INDEX_FAV_PEOPLE:
                updateContactSlide(notificationModel);
                break;
            case SLIDE_INDEX_FAV_APP:
                updateAppsSlide(notificationModel);
                break;
            case SLIDE_INDEX_FAV_LINKS:
                updateLinkSlide(notificationModel);
                break;
            case SLIDE_INDEX_REMINDERS:
                updateReminderSlide(notificationModel);
                break;
            case SLIDE_INDEX_SOS:
                updateSOSList(notificationModel.getJsonObject(),notificationModel.getStatus());
                break;
           // case SLIDE_INDEX_DIRECTIONS:
           //     updatePlacesSlide(notificationModel);
           //     break;
            case INVITE_CODE:
                updateInvites(notificationModel);
                break;
            case SLIDE_CREATE_INDEX:
                addNewSlide(notificationModel);
                break;
            case SLIDE_REMOVE_INDEX:
                removeSlide(notificationModel);
                break;
        }
    }

    private void updateContactSlide(LocalNotificationModel localNotificationModel){

        try {
            int status = localNotificationModel.getStatus();
            JSONObject jsonObject = localNotificationModel.getJsonObject();
            ContactEntity entityToUpdate=null;
            String slideId = jsonObject.getString("slide_id");
            List<ContactEntity> favoriteContacts =
                    preferenceUtil.getFavPeopleList(slideId);
            favoriteContacts=favoriteContacts==null?new ArrayList<>():favoriteContacts;
            ContactEntity entity = new Gson().fromJson(jsonObject.toString(), ContactEntity.class);
            EventBus.getDefault().postSticky(new PeopleNotificationEvent(entity,localNotificationModel));
            for(ContactEntity contactEntity:favoriteContacts){
                if(contactEntity.getId().equals(entity.getId()))
                {
                    entityToUpdate = contactEntity;
                    break;
                }
            }

             if(entityToUpdate==null  && status==ACCEPTED) {
                 favoriteContacts.add(entity);
                 preferenceUtil.addFavPeopleInAllContacts(entity);
             }
            else if(entityToUpdate!=null && status!=REJECTED) {
                int index = favoriteContacts.indexOf(entityToUpdate);
                favoriteContacts.get(index).setRequestStatus(status);
                    if(status==ACCEPTED)
                 preferenceUtil.addFavPeopleInAllContacts(entity);

             }
            else if(entityToUpdate!=null && status==REJECTED) {
                 favoriteContacts.remove(entityToUpdate);
                 preferenceUtil.removeFavPeople(entityToUpdate);
             }


            preferenceUtil.saveFavPeople(slideId,favoriteContacts);

        }catch (JsonSyntaxException exception){
            Log.e(TAG,exception.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EventBus.getDefault().postSticky(new NotificationReceiveEvent(Constant.SLIDE_INDEX_FAV_PEOPLE,true));

    }

    private void updateAppsSlide(LocalNotificationModel localNotificationModel){

        try {
            int status = localNotificationModel.getStatus();
            JSONObject jsonObject = localNotificationModel.getJsonObject();
            AppsEntity entityToUpdate=null;
            String slideId = jsonObject.getString("slide_id");
            List<AppsEntity> favoriteApps =
                    preferenceUtil.getFavAppsList(slideId);
            favoriteApps=favoriteApps==null?new ArrayList<>():favoriteApps;
            AppsEntity entity = new Gson().fromJson(jsonObject.toString(), AppsEntity.class);

            EventBus.getDefault().postSticky(new AppNotificationEvent(entity,localNotificationModel));
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

    private void updateLinkSlide(LocalNotificationModel localNotificationModel){

        try {
            int status = localNotificationModel.getStatus();
            JSONObject jsonObject = localNotificationModel.getJsonObject();
            LinksEntity entityToUpdate=null;
            String slideId = jsonObject.getString("slide_id");
            List<LinksEntity> favoriteLinks =
                    preferenceUtil.getFavLinkList(slideId);
            favoriteLinks=favoriteLinks==null?new ArrayList<>():favoriteLinks;
            LinksEntity entity = new Gson().fromJson(jsonObject.toString(), LinksEntity.class);
            EventBus.getDefault().postSticky(new LinkNotificationEvent(entity,localNotificationModel));
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

    private void updateReminderSlide(LocalNotificationModel localNotificationModel){

        try {
            int status = localNotificationModel.getStatus();
            JSONObject jsonObject = localNotificationModel.getJsonObject();
            ReminderEntity entityToUpdate=null;
            String slideId = jsonObject.getString("slide_id");
            List<ReminderEntity> reminders =
                    preferenceUtil.getReminderList(slideId);
            reminders=reminders==null?new ArrayList<>():reminders;
            ReminderEntity entity = new Gson().fromJson(jsonObject.toString(), ReminderEntity.class);
            EventBus.getDefault().postSticky(new ReminderNotificationEvent(entity,localNotificationModel));
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

    private void updatePlacesSlide(LocalNotificationModel localNotificationModel){

        try {
            int status = localNotificationModel.getStatus();
            JSONObject jsonObject = localNotificationModel.getJsonObject();
            Location entityToUpdate=null;
            String slideId = jsonObject.getString("slide_id");
            List<Location> locations =
                    preferenceUtil.getSafePlacesList(slideId);
            locations=locations==null?new ArrayList<>():locations;
            Location entity = new Gson().fromJson(jsonObject.toString(), Location.class);
            EventBus.getDefault().postSticky(new SafePlacesNotificationEvent(entity,localNotificationModel));
            for(Location locationEntity:locations){
                if(locationEntity.getId().equals(entity.getId()))
                {
                    entityToUpdate = locationEntity;
                    break;
                }
            }
            if(entityToUpdate==null  && status==ACCEPTED)
                locations.add(entity);
            else if(entityToUpdate!=null && status!=REJECTED) {
                int index = locations.indexOf(entityToUpdate);
                locations.get(index).setRequestStatus(status);

            }
            else if(entityToUpdate!=null && status==REJECTED)
                locations.remove(entityToUpdate);
            preferenceUtil.saveSafePlaces(slideId,locations);



        }catch (JsonSyntaxException exception){
            Log.e(TAG,exception.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EventBus.getDefault().postSticky(new NotificationReceiveEvent(Constant.SLIDE_INDEX_DIRECTIONS,true));

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

    private void updateInvites(LocalNotificationModel localNotificationModel){
        int status = localNotificationModel.getStatus();
        JSONObject jsonObject = localNotificationModel.getJsonObject();
        Invitation invite = new Gson().fromJson(jsonObject.toString(), Invitation.class);


        Repository.getInstance().getInvites(preferenceUtil.getAccount().getId(),
                new DataSource.GetDataCallback<InvitationResponse>() {
                    @Override
                    public void onDataReceived(InvitationResponse data) {
                        if(data.isSuccess()) {
                            User user = preferenceUtil.getAccount();
                            user.setInvitations(data.getInvitationList());
                            preferenceUtil.saveAccount(user);
                           // EventBus.getDefault().postSticky(new NotificationReceiveEvent(Constant.INVITE_CODE,true));
                            EventBus.getDefault().postSticky(new InviteNotificationEvent(invite,localNotificationModel.getSender()));
                        }
                    }

                    @Override
                    public void onFailed(int code, String message) {

                    }
                });

   //     EventBus.getDefault().postSticky(new InviteNotificationEvent(invite,localNotificationModel.getSender()));

    }

    private void addNewSlide(LocalNotificationModel localNotificationModel){
        String userId = preferenceUtil.getAccount().getId();
        Slide entity =  new Gson().fromJson(localNotificationModel.getJsonObject().toString(),Slide.class);
        Slide inviteSlide = createLocalInviteSlide();
        List<Slide> slideItems = new ArrayList<>();
        slideItems.add(createClockSlide());
        slideItems.addAll(preferenceUtil.getUserSlideList(userId));
        slideItems.add(entity);
        List<Slide> slides = Util.getSortedList(slideItems);

        preferenceUtil.saveUserSlides(userId,slides);
       // User primaryHelper = preferenceUtil.getAccount().getPrimaryHelper();
       // if ( primaryHelper == null || !primaryHelper.isPrimaryConnected())
       //     slideItems.add(inviteSlide);
        EventBus.getDefault().postSticky(new NotificationReceiveEvent(Constant.SLIDE_CREATE_INDEX,true));
    }

    private void removeSlide(LocalNotificationModel localNotificationModel){
        String userId = preferenceUtil.getAccount().getId();
        Slide slide = new Gson().fromJson(localNotificationModel.getJsonObject().toString(),Slide.class);
        Slide slideToRemove=slide;
        List<Slide> slideItems = new ArrayList<>();
        List<Slide> prefList = preferenceUtil.getUserSlideList(userId);
        slideItems.add(createClockSlide());
        slideItems.addAll(prefList);
      //  User primaryHelper = preferenceUtil.getAccount().getPrimaryHelper();
      //  if ( primaryHelper == null || !primaryHelper.isPrimaryConnected())
      //      slideItems.add(createLocalInviteSlide());

        for(Slide slide1:slideItems){
            if(slide.getId().equals(slide1.getId()))
                slideToRemove = slide1;
        }
        prefList.remove(slideToRemove);
      //  slideItems.remove(slideToRemove);
        List<Slide> slides = Util.getSortedList(prefList);

        preferenceUtil.saveUserSlides(userId,slides);
        EventBus.getDefault().postSticky(new NotificationReceiveEvent(Constant.SLIDE_REMOVE_INDEX,true));
    }

    public Slide createLocalInviteSlide(){
        Slide slide = new Slide();
        slide.setName("Your Kid Helpers");
        slide.setType(SLIDE_INDEX_INVITE);
        return slide;
    }

    public Slide createClockSlide(){
        Slide slide = new Slide();
        slide.setType(SLIDE_INDEX_CLOCK);
        slide.setName(Constant.CLOCK);
        slide.setUser_id(preferenceUtil.getAccount().getId());
        return slide;
    }
}
