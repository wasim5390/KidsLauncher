package com.wiser.kids.ui.home.contact;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Awais on 07/05/18.
 */

public class ContactLoader {
    private static final String TAG = ContactLoader.class.getSimpleName();
    private static final Uri QUERY_CONTACT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private static final String SORT_ORDER = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

    private static ContactLoader instance;

    private Context mContext;

    // LinkedHashMap for speedup searching time when checking duplicate contact.
    private final LinkedHashMap<String, ContactEntity> mDeviceContactsMap = new LinkedHashMap<>();
    private final List<ContactEntity> mDeviceContactsList = new ArrayList<>();


    public static ContactLoader getInstance(Context ctx) {
        if (instance == null) {
            initialize(ctx);
        }
        return instance;
    }
    public static void initialize(Context context) {
        if (instance == null) {
            instance = new ContactLoader(context);
            synchronized (ContactLoader.class) {
                instance.getContext().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, instance.mObserver);
            }
        }

    }

    public ContactLoader(Context context) {
        this.mContext = context;
        startSyncContact();
    }

    public Context getContext() {
        return mContext;
    }

    private final ContentObserver mObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            startSyncContact();
        }
    };

    public List<ContactEntity> getDeviceContactsList() {
        if (mDeviceContactsList.size() == 0) {
            startSyncContact();
        }
        return mDeviceContactsList;
    }

    public void startSyncContactIfNeed() {
        if (mDeviceContactsList.size() == 0) {
            startSyncContact();
        }
    }

    public ContactEntity getContactEntityByNumber(String phoneNumber,String displayName){
        for(ContactEntity contactEntity: mDeviceContactsList){
            if(contactEntity.getmHomeNumber()!=null && contactEntity.getmHomeNumber().replaceAll("\\s+","").equals(phoneNumber)
                    && contactEntity.getName()!=null && contactEntity.getName().equals(displayName)){
                return contactEntity;
            }
            else if(contactEntity.getmPhoneNumber()!=null && contactEntity.getmPhoneNumber().replaceAll("\\s+","").equals(phoneNumber)
                    && contactEntity.getName()!=null && contactEntity.getName().equals(displayName)
                    ) {
                return contactEntity;
            }
        }
        return null;
    }

    public void startSyncContact() {
        // Check permission first!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        // Permission was Granted so load OK!
        mDeviceContactsMap.clear();
        mDeviceContactsList.clear();

        Cursor cursor = mContext.getContentResolver().query(
                QUERY_CONTACT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                        ContactsContract.CommonDataKinds.Phone.TYPE
                },
                null,
                null,
                SORT_ORDER
        );

        Log.d(TAG, "START LOAD DEVICE CONTACT HERE!");
        long t = System.currentTimeMillis();
        if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            try {
                do {
                    String androidID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String lookupID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String avatarUrl = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                    String type = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                    // SOS phone number has 3 characters (911, 101, 114,...) so it should be valid.
                    if (phoneNumber == null || "".equals(phoneNumber) == true || phoneNumber.length() < 3) continue;

                    ContactEntity newContact = new ContactEntity();
                    newContact.setName(name);
                    if(Integer.valueOf(type) == ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                        newContact.setmHomeNumber(phoneNumber);
                    else
                        newContact.setmPhoneNumber(phoneNumber);
                    newContact.setAndroidId(androidID);
                    newContact.setLookupId(lookupID);
                    newContact.setPhotoUri(avatarUrl);

                    mDeviceContactsMap.put(name, newContact);
                    //  }
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }
        t = System.currentTimeMillis() - t;

        // Just log for testing.
        int i = 0;
        for (Map.Entry<String, ContactEntity> contact : mDeviceContactsMap.entrySet()) {
            mDeviceContactsList.add(contact.getValue());
            //Log.d(TAG, String.format("%d | Name = %s | Phone = %s | Avatar = %s", i++, contact.getKey(), contact.getValue().getPhone(), contact.getValue().getAvatar()));
        }

        Log.d(TAG, "END LOAD DEVICE CONTACT WIDTH: " + mDeviceContactsMap.size() + " Contact(s)! | In total: " + t + " ms");
    }

    public static ContactEntity buildContactFromDb(String androidId, String lookupKey, Context ctx) {
        ContactEntity contact = new ContactEntity();
        updateContactFromDb(contact, androidId, lookupKey, ctx);
        return contact;
    }

    public static ContactEntity updateContactFromDb(ContactEntity contact, String androidId, String lookupKey, Context ctx) {

        String displayName = null;
        String androidID = null;
        String lookupID = null;
        String homeNubmer = null;
        String mobileNumber = null;
        String familyNamePhone = null;
        String firstNamePhone = null;
        String email = null;
        String photoUri = null;

        ContentResolver cr = ctx.getContentResolver();
        try{
            /**
             * Get the contact
             */
            Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts.LOOKUP_KEY + "=?", new String[] { lookupKey }, null);
            if (c.moveToNext()){
                displayName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                androidID = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                lookupID = c.getString(c.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                photoUri = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
            }
            c.close();
            /**
             * Get all phone numbers.
             */
            Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY + "=?", new String[] { lookupKey }, ContactsContract.CommonDataKinds.Phone.TYPE + " ASC");
            int typeIdx = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
            while (phones.moveToNext()){
                String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                int type = phones.getInt(typeIdx);
                switch (type){
                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                        homeNubmer = number;
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                        mobileNumber = number;
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                        homeNubmer = getValueIfEmpty(homeNubmer, number);
                        mobileNumber = getValueIfEmpty(homeNubmer, mobileNumber, number);
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                        homeNubmer = getValueIfEmpty(homeNubmer, number);
                        mobileNumber = getValueIfEmpty(homeNubmer, mobileNumber, number);
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                        homeNubmer = getValueIfEmpty(homeNubmer, number);
                        mobileNumber = getValueIfEmpty(homeNubmer, mobileNumber, number);
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                        homeNubmer = getValueIfEmpty(homeNubmer, number);
                        mobileNumber = getValueIfEmpty(homeNubmer, mobileNumber, number);
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                        homeNubmer = getValueIfEmpty(homeNubmer, number);
                        mobileNumber = getValueIfEmpty(homeNubmer, mobileNumber, number);
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:
                        homeNubmer = getValueIfEmpty(homeNubmer, number);
                        mobileNumber = getValueIfEmpty(homeNubmer, mobileNumber, number);
                        break;
                }
            }
            phones.close();
            /**
             * Get all email addresses.
             */
            String[] projection = new String[] { ContactsContract.CommonDataKinds.Email.DATA , ContactsContract.CommonDataKinds.Phone.TYPE};
            Cursor emails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection, ContactsContract.CommonDataKinds.Email.LOOKUP_KEY + "=?", new String[] { lookupKey }, null);
            while (emails.moveToNext()){
                String currentEmail = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                int type = emails.getInt(emails.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                switch (type){
                    case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                        email = currentEmail;
                        break;
                    default:
                        // do we want to do something with work email here?
                        if (TextUtils.isEmpty(email)) {
                            email = currentEmail;
                        }
                        break;
                }
            }
            emails.close();
            projection = new String[] { ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME};
            Cursor names = cr.query(ContactsContract.Data.CONTENT_URI, projection, ContactsContract.CommonDataKinds.StructuredName.LOOKUP_KEY + "=? AND " + ContactsContract.Data.MIMETYPE + " =? ", new String[] { lookupKey, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE}, null);
            while (names.moveToNext()){
                String familyName = names.getString(names.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                if (!TextUtils.isEmpty(familyName)){
                    familyNamePhone = familyName;
                }
                String firstName = names.getString(names.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                if (!TextUtils.isEmpty(firstName)){
                    firstNamePhone = firstName;
                }
            }
            names.close();
        }
        catch (Exception e){}
        /**
         * Set All Values in Model
         */
        contact.setLookupId(lookupKey);
        contact.setAndroidId(androidID);
        contact.setName(displayName);
        contact.setAndroidId(androidId);
        contact.setmHomeNumber(homeNubmer);
        contact.setmPhoneNumber(mobileNumber);
        contact.setLastName(familyNamePhone);
        contact.setFirstName(firstNamePhone);
        contact.setEmail(email);
        contact.setPhotoUri(photoUri);

        return contact;
    }
    /**
     * Retruns a value string if a field is empty.
     * Used to build contact homeNumber from DB based on phone type priorities
     * @param field if the field is not empty
     * @param value value to be stored / used if the field is empty
     * @return
     */
    private static String getValueIfEmpty (String field, String value) {
        if (TextUtils.isEmpty(field)) {
            return value;
        }
        else {
            return field;
        }
    }
    /**
     * Works like getValueIfEmpty but check a priority Field - if it equals the Value the entire check is ignored
     * @param priorityField
     * @param field
     * @param value
     * @return
     */
    private static String getValueIfEmpty (String priorityField, String field, String value) {
        if (value.equals(priorityField)) {
            return field;
        }
        if (TextUtils.isEmpty(field)) {
            return value;
        }
        else {
            return field;
        }
    }
}
