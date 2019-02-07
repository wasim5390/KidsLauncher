package com.uiu.kids.ui.home.contact;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.crashlytics.android.Crashlytics;
import com.uiu.kids.util.BitmapUtils;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * class to insert/update/delete a contact to android
 * @author UIU_AUTOMATION
 *
 */
public class AndroidContactsConnector {

	private static final String TAG = "ContactsConnector";

	/*	private static final String SIM_CARD_CONTACTS_URI = "content://icc/adn";
	private static final int DELETE_OPERATION = 1;
	private static final int UPDATE_OPERATION = 2;*/


	/*	private static final String COLUMN_SIM_INDEX = "index";
	private static final String COLUMN_SIM_NAME = "name";
	private static final String COLUMN_SIM_PHONE = "number";
	private static final String COLUMN_SIM_MOBILE = "additionalNumber";
	private static final String COLUMN_SIM_EMAIL = "emails";*/
	private static final String GOOGLE = "com.google";

	private static Account getEmailAccount(Context ctx) {
		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
		if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return null;
		}
		Account[] accounts = AccountManager.get(ctx).getAccounts();

		for (Account account : accounts) {
			System.out.println(account.name + ":"+account.type);
		}

		try {
			// if logged in to parse, try and get the matching account
			if (PreferenceUtil.getInstance(ctx).getAccount() != null) {
				String parseEmail = PreferenceUtil.getInstance(ctx).getAccount().getEmail();
				for (Account account : accounts) {
					if (emailPattern.matcher(account.name).matches()) {
						if (account.name.equals(parseEmail)) {
							return account;
						}
					}
				}
			}

		} catch (Exception e) {
			// don't really care about this exception
			e.printStackTrace();
		}

		// if still here, no parse account found.

		// If we are local, just get the first matching google email account.
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches() &&
					account.type.equals(GOOGLE)) {
				return account;
			}
		}

		// Still here? - just get the first matching email account.
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				return account;
			}
		}


//        // last check, if running in debug mode
//        if (BuildConfig.DEBUG) {
//            // do something for a debug build
//            Log.d(TAG, "Running in debug mode - adding hard coded account email address");
//            Account acc = new Account("joelkorn@hotmail.com", GOOGLE);
//            return acc;
//        }

		return null;
	}

	/**
	 * add new contact to the android db
	 * @param context
	 * @param newContact
	 * @return - new contact android id and lookup key
	 */
	public static String[] addAndroidContact(Context context, Contact newContact){

		String firstName = newContact.getFirstName();
		String lastName = newContact.getFamilyName();
		String displayName = newContact.getDisplayName();
		String mobileNumber = newContact.getMobileNumber();
		String homeNumber = newContact.getHomeNumber();
		String email = newContact.getEmail();
		String profilePicture = newContact.getPhotoID();

		Account contactAccount = getEmailAccount(context);

		if (null == contactAccount) {
			//the user does not have an account on his device 
			//TODO: think if we want to handle this edge case. 
			return null;
		}

		Log.d(TAG, "Adding new contact with type " + contactAccount.type + " and name : " + contactAccount.name);
		ArrayList <ContentProviderOperation> insertOperation = new ArrayList<>();

		//Adding insert operation to operations list 
		//insert a new raw contact in the table ContactsContract.RawContacts
		insertOperation.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, contactAccount.type)
				.withValue(RawContacts.ACCOUNT_NAME, contactAccount.name)
				.build());

		//withValueBackReference is used as the user hasn�t been created yet. It will use the RAW_CONTACT_ID that will be created in the previous operation
		//insert display name in the table ContactsContract.Data
		if(!TextUtils.isEmpty(displayName)){
			insertOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,displayName)
					.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstName)
					.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastName)
					.build());
		}

		//insert Mobile Number in the table ContactsContract.Data
		if(!TextUtils.isEmpty(mobileNumber)){
			insertOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
					.build());
		}

		//insert home number in the table ContactsContract.Data
		if(!TextUtils.isEmpty(homeNumber)){
			insertOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, homeNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
					.build());
		}

		//insert email in the table ContactsContract.Data
		if(!TextUtils.isEmpty(email)){
			insertOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
					.withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
					.build());
		}

		//insert Photo in the table ContactsContract.Data
		if(!TextUtils.isEmpty(profilePicture)){
			Bitmap profileBitmap = Util.base64ToBitmapDecode(profilePicture);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			profileBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			insertOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, baos.toByteArray())
					.build());
		}

		// Asking the Contact provider to create a new contact
		try {
			ContentProviderResult[] res;
			// Executing all the insert operations as a single database transaction
			res = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, insertOperation);


			if (res != null){

				String rawContactIndex = res[0].uri.getPath().substring(14);
				Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, Long.valueOf(rawContactIndex));
				Uri contactUri = RawContacts.getContactLookupUri(context.getContentResolver(), rawContactUri);


				Cursor contactCursor = context.getContentResolver().query(contactUri, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.LOOKUP_KEY}, null, null, null);

				contactCursor.moveToFirst();

				String contactIndex = "-1";
				String contactLookupUri = "-1";

				if(contactCursor.getCount() > 0){
					contactIndex = contactCursor.getString(0);
					contactLookupUri = contactCursor.getString(1);
				}

				return new String[]{contactIndex, contactLookupUri};
			}
		} catch (Exception e) {
			e.printStackTrace();Crashlytics.logException(e);
			Log.d(TAG, "Exception - " + e.getMessage());
		}
		return null;
	}



	/**
	 *
	 * @param context
	 * @param contactToDelete
	 */
	public static void deleteAndroidContact(Context context, Contact contactToDelete){

		try{

			Uri contactLookup = ContactsContract.Contacts.getLookupUri(Long.valueOf(contactToDelete.getAndroidID()), contactToDelete.getLookupKey());
			context.getContentResolver().delete(contactLookup, null, null);

		}catch(Exception e) {
			e.printStackTrace();Crashlytics.logException(e);
		}
	}


	/**
	 * update the name of the contact
	 * @param context
	 * @param updateContact
	 */
	public static void updateContactName(Context context, Contact updateContact){

		Log.d(TAG, "updateContactName");

		String androidId = updateContact.getAndroidID();
		String lookupKey = updateContact.getLookupKey();
		String displayName = updateContact.getDisplayName();
		String firstName = updateContact.getFirstName();
		String familyName = updateContact.getFamilyName();


		Cursor cursor = null;
		int rawContactId = -1;

		try {

			//check if we have update contact id and store it in androidId
			Uri lookupUri = ContactsContract.Contacts.getLookupUri(Long.valueOf(androidId), lookupKey);
			cursor = context.getContentResolver().query(lookupUri, new String[]{ContactsContract.Contacts._ID}, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				androidId = cursor.getString(0);
			}
		} catch (Exception e){
			Crashlytics.logException(e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		//get the raw contact id for insert in raw contacts table
		try{
			cursor = context.getContentResolver().query(RawContacts.CONTENT_URI, null, RawContacts.CONTACT_ID + "=?", new String[] {String.valueOf(androidId)}, null);
			if(cursor.moveToFirst()){
				rawContactId = cursor.getInt(cursor.getColumnIndex(RawContacts._ID));
			}
		}catch(Exception e){
			Crashlytics.logException(e);
		}finally{
			cursor.close();
		}

		ArrayList <ContentProviderOperation> updateOperation = new ArrayList<>();
		String where = ContactsContract.Data.CONTACT_ID + " =? AND " + ContactsContract.Data.MIMETYPE + " =?";

		//update the contact name
		if(firstName != null){

			try{
				cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data._ID}, where, new String[] {String.valueOf(androidId), ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE}, null);
				if(cursor != null && cursor.getCount() > 0){
					cursor.moveToFirst();
					updateOperation.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
							.withSelection(ContactsContract.Data._ID + " =?", new String[]{cursor.getString(0)})
							.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName)
							.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstName)
							.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, familyName)
							.build());
				}else{
					updateOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
							.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
							.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,displayName)
							.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstName)
							.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, familyName)
							.build());
				}
				cursor.close();

				applyUpdateBatch(context, updateOperation);


			}catch(Exception e){
				Log.d(TAG, "Exception - " + e.getMessage());Crashlytics.logException(e);
			}
		}
	}


	/**
	 * update the mobile number of the contact
	 * @param context
	 * @param updateContact
	 */
	public static void updateContactMobile(Context context, Contact updateContact){

		Log.d(TAG, "updateContactMobile");
		String androidId = updateContact.getAndroidID();
		String lookupKey = updateContact.getLookupKey();
		String mobileNumber = updateContact.getMobileNumber();

		Cursor cursor = null;

		//check if we have update contact id and store it in androidId
		Uri lookupUri = ContactsContract.Contacts.getLookupUri(Long.valueOf(androidId), lookupKey);
		try {
			cursor = context.getContentResolver().query(lookupUri, new String[]{ContactsContract.Contacts._ID}, null, null, null);
		}
		catch (NullPointerException e){
			Crashlytics.logException(e);
		}catch (IllegalStateException e1){}

		try{
			cursor.moveToFirst();
			androidId = cursor.getString(0);
			cursor.close();
		}catch(Exception e){
			Crashlytics.logException(e);
		}

		ArrayList <ContentProviderOperation> updateOperation = new ArrayList<>();

		String mobileWhere = ContactsContract.Data.CONTACT_ID + " =? AND " + ContactsContract.Data.MIMETYPE + " =? AND " + ContactsContract.CommonDataKinds.Phone.TYPE + " =?";
		cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data._ID}, mobileWhere, new String[] {String.valueOf(androidId), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)}, null);

		if(cursor != null && cursor.getCount() > 0){

			cursor.moveToFirst();

			//update existing data
			if(!TextUtils.isEmpty(mobileNumber)){

				updateOperation.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
						.withSelection(ContactsContract.Data._ID + " =?", new String[]{cursor.getString(0)})
						.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
						.build());
			}
			//delete existing data
			else{

				updateOperation.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
						.withSelection(ContactsContract.Data._ID + " =?", new String[]{cursor.getString(0)})
						.build());
			}

		}else{

			//insert existing data
			if(!TextUtils.isEmpty(mobileNumber)){

				int rawContactId = -1;

				//get the raw contact id for insert in raw contacts table
				try{
					cursor = context.getContentResolver().query(RawContacts.CONTENT_URI, null, RawContacts.CONTACT_ID + "=?", new String[] {String.valueOf(androidId)}, null);
					if(cursor.moveToFirst()){
						rawContactId = cursor.getInt(cursor.getColumnIndex(RawContacts._ID));
					}
				}catch(Exception e){
					Crashlytics.logException(e);
				}finally{
					cursor.close();
				}

				updateOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
						.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
						.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
						.build());
			}

		}

		cursor.close();

		applyUpdateBatch(context, updateOperation);
	}




	/**
	 * update the home number phone of the contact
	 * @param context
	 * @param updateContact
	 */
	public static void updateContactHome(Context context, Contact updateContact){

		String androidId = updateContact.getAndroidID();
		String lookupKey = updateContact.getLookupKey();
		String homeNumber = updateContact.getHomeNumber();

		Cursor cursor = null;

		//check if we have update contact id and store it in androidId
		Uri lookupUri = ContactsContract.Contacts.getLookupUri(Long.valueOf(androidId), lookupKey);
		try {
			cursor = context.getContentResolver().query(lookupUri, new String[]{ContactsContract.Contacts._ID}, null, null, null);
		}
		catch (NullPointerException e){
			Crashlytics.logException(e);
		}

		try{
			cursor.moveToFirst();
			androidId = cursor.getString(0);
		}catch(Exception e){
			Crashlytics.logException(e);
		}finally{
			cursor.close();
		}

		ArrayList <ContentProviderOperation> updateOperation = new ArrayList<>();

		String homeWhere = ContactsContract.Data.CONTACT_ID + " =? AND " + ContactsContract.Data.MIMETYPE + " =? AND " + ContactsContract.CommonDataKinds.Phone.TYPE + " =?";
		cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data._ID}, homeWhere, new String[] {String.valueOf(androidId), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)}, null);

		if(cursor != null && cursor.getCount() > 0){

			cursor.moveToFirst();

			//update existing data
			if(!TextUtils.isEmpty(homeNumber)){
				updateOperation.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
						.withSelection(ContactsContract.Data._ID + " =?", new String[]{cursor.getString(0)})
						.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, homeNumber)
						.build());
			}
			//delete existing data
			else{
				updateOperation.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
						.withSelection(ContactsContract.Data._ID + " =?", new String[]{cursor.getString(0)})
						.build());
			}

		}else{

			int rawContactId = -1;

			//get the raw contact id for insert in raw contacts table
			try{
				cursor = context.getContentResolver().query(RawContacts.CONTENT_URI, null, RawContacts.CONTACT_ID + "=?", new String[] {String.valueOf(androidId)}, null);
				if(cursor.moveToFirst()){
					rawContactId = cursor.getInt(cursor.getColumnIndex(RawContacts._ID));
				}
			}catch(Exception e){
				Crashlytics.logException(e);
			}finally{
				cursor.close();
			}

			//insert new data
			if(!TextUtils.isEmpty(homeNumber)){

				updateOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
						.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, homeNumber)
						.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
						.build());
			}
		}

		cursor.close();

		applyUpdateBatch(context, updateOperation);
	}

	/**
	 * update the email address of the contact
	 * @param context
	 * @param updateContact
	 */
	public static void updateContactEmail(Context context, Contact updateContact){

		Log.d(TAG, "updateContactEmail");

		String androidId = updateContact.getAndroidID();
		String lookupKey = updateContact.getLookupKey();
		String email = updateContact.getEmail();

		Cursor cursor = null;


		//check if we have update contact id and store it in androidId
		Uri lookupUri = ContactsContract.Contacts.getLookupUri(Long.valueOf(androidId), lookupKey);
		cursor = context.getContentResolver().query(lookupUri, new String[]{ContactsContract.Contacts._ID}, null, null, null);

		try{
			cursor.moveToFirst();
			androidId = cursor.getString(0);
		}catch(Exception e){
			Crashlytics.logException(e);
		}finally{
			cursor.close();
		}

		ArrayList <ContentProviderOperation> updateOperation = new ArrayList<>();
		String where = ContactsContract.Data.CONTACT_ID + " =? AND " + ContactsContract.Data.MIMETYPE + " =?";

		cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data._ID}, where, new String[] {String.valueOf(androidId), ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE}, null);

		if(cursor != null && cursor.getCount() > 0){

			cursor.moveToFirst();

			//update existing data
			if(!TextUtils.isEmpty(email)){

				updateOperation.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
						.withSelection(ContactsContract.Data._ID + " =?", new String[]{cursor.getString(0)})
						.withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
						.build());
			}
			//delete existing data
			else{
				updateOperation.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
						.withSelection(ContactsContract.Data._ID + " =?", new String[]{cursor.getString(0)})
						.build());
			}

		}else{

			//insert new data
			if(!TextUtils.isEmpty(email)){

				int rawContactId = -1;

				//get the raw contact id for insert in raw contacts table
				try{
					cursor = context.getContentResolver().query(RawContacts.CONTENT_URI, null, RawContacts.CONTACT_ID + "=?", new String[] {String.valueOf(androidId)}, null);
					if(cursor.moveToFirst()){
						rawContactId = cursor.getInt(cursor.getColumnIndex(RawContacts._ID));
					}
				}catch(Exception e){
					Crashlytics.logException(e);
				}finally{
					cursor.close();
				}

				updateOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
						.withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
						.withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
						.build());
			}
		}

		cursor.close();

		applyUpdateBatch(context, updateOperation);
	}




	public static void updateContactPhoto(Context context, Contact updateContact){

		String androidId = updateContact.getAndroidID();
		String androidLookupKey = updateContact.getLookupKey();
		String photoPath = updateContact.getPhotoPath();

		Cursor cursor = null;

		//check if we have update contact id and store it in androidId
		Uri lookupUri = ContactsContract.Contacts.getLookupUri(Long.valueOf(androidId), androidLookupKey);
		cursor = context.getContentResolver().query(lookupUri, new String[]{ContactsContract.Contacts._ID}, null, null, null);

		try{
			cursor.moveToFirst();
			androidId = cursor.getString(0);
		}catch(Exception e){
			Crashlytics.logException(e);
			e.printStackTrace();
		}finally{
			cursor.close();
		}


		ByteArrayOutputStream baos = null;


		ExifInterface exif;
		try {
			exif = new ExifInterface(photoPath);

			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			if(orientation == ExifInterface.ORIENTATION_ROTATE_90){
				orientation = 90;
			} else if(orientation == ExifInterface.ORIENTATION_ROTATE_180){
				orientation = 180;
			} else if(orientation == ExifInterface.ORIENTATION_ROTATE_270){
				orientation = 270;
			}


		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();Crashlytics.logException(e1);
		}

		if(!TextUtils.isEmpty(photoPath)){

			//Bitmap profileBitmap = BitmapFactory.decodeFile(photoPath);
			Bitmap profileBitmap = BitmapUtils.decodeBitmapFromFile(context, photoPath, 0, 0);

			if (null == profileBitmap) {
				//we could not decode the picture for some reason.
				//log and die
				Crashlytics.log(Log.WARN, TAG, "Could not load profileBitmap photoPath: " + photoPath );
				return;
			}
			int quality = 100;
			do {
				baos = new ByteArrayOutputStream();
				profileBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
				quality -=20;

			}while (baos.size() > 1000000);
		}

		ArrayList <ContentProviderOperation> updateOperation = new ArrayList<>();
		String where = ContactsContract.Data.CONTACT_ID + " =? AND " + ContactsContract.Data.MIMETYPE + " =?";

		cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data._ID},
				where, new String[] {String.valueOf(androidId), ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE}, null);
		if(cursor != null && cursor.getCount() > 0){

			cursor.moveToFirst();

			//update existing data
			if(baos != null){

				updateOperation.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
						.withSelection(ContactsContract.Data._ID + " =?", new String[]{cursor.getString(0)})
						.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, baos.toByteArray())
						.build());

			}
			//delete existing data
			else{
				updateOperation.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
						.withSelection(ContactsContract.Data._ID + " =?", new String[]{cursor.getString(0)})
						.build());
			}

		}else{

			//insert new data
			if(baos != null){

				int rawContactId = -1;

				//get the raw contact id for insert in raw contacts table
				try{
					cursor = context.getContentResolver().query(RawContacts.CONTENT_URI, null, RawContacts.CONTACT_ID + "=?", new String[] {String.valueOf(androidId)}, null);
					if(cursor.moveToFirst()){
						rawContactId = cursor.getInt(cursor.getColumnIndex(RawContacts._ID));
					}
				}catch(Exception e){
					e.printStackTrace();
					Crashlytics.logException(e);
				}finally{
					cursor.close();
				}

				updateOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
						.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, baos.toByteArray())
						.build());
			}

		}

		cursor.close();

		applyUpdateBatch(context, updateOperation);
	}




	public static void updateContactBitmap(Context context, Contact updateContact, Bitmap thumbnail){

		String androidId = updateContact.getAndroidID();
		String androidLookupKey = updateContact.getLookupKey();

		Cursor cursor = null;

		//check if we have update contact id and store it in androidId
		Uri lookupUri = ContactsContract.Contacts.getLookupUri(Long.valueOf(androidId), androidLookupKey);
		cursor = context.getContentResolver().query(lookupUri, new String[]{ContactsContract.Contacts._ID}, null, null, null);

		try{
			cursor.moveToFirst();
			androidId = cursor.getString(0);
		}catch(Exception e){

		}finally{
			cursor.close();
		}


		ArrayList <ContentProviderOperation> updateOperation = new ArrayList<>();
		String where = ContactsContract.Data.CONTACT_ID + " =? AND " + ContactsContract.Data.MIMETYPE + " =?";

		cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data._ID}, where, new String[] {String.valueOf(androidId), ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE}, null);

		ByteArrayOutputStream baos = null;


		if(thumbnail != null){
			baos = new ByteArrayOutputStream();
			thumbnail.compress(Bitmap.CompressFormat.PNG, 100, baos);
		}


		if(cursor != null && cursor.getCount() > 0){

			cursor.moveToFirst();

			//update existing data
			if(baos != null){

				updateOperation.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
						.withSelection(ContactsContract.Data._ID + " =?", new String[]{cursor.getString(0)})
						.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, baos.toByteArray())
						.build());

			}
			//delete existing data
			else{
				updateOperation.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
						.withSelection(ContactsContract.Data._ID + " =?", new String[]{cursor.getString(0)})
						.build());
			}

		}else{

			//insert new data
			if(baos != null){

				int rawContactId = -1;

				//get the raw contact id for insert in raw contacts table
				try{
					cursor = context.getContentResolver().query(RawContacts.CONTENT_URI, null, RawContacts.CONTACT_ID + "=?", new String[] {String.valueOf(androidId)}, null);
					if(cursor.moveToFirst()){
						rawContactId = cursor.getInt(cursor.getColumnIndex(RawContacts._ID));
					}
				}catch(Exception e){

				}finally{
					cursor.close();
				}

				updateOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
						.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, baos.toByteArray())
						.build());
			}

		}

		cursor.close();

		applyUpdateBatch(context, updateOperation);
	}




	/**
	 * update full contact with all the data
	 * @param context
	 * @param updateContact
	 */
	public static void updateAndroidContact(Context context, Contact updateContact){

		Log.d(TAG, "updateAndroidContact");

		updateContactName(context, updateContact);
		updateContactMobile(context, updateContact);
		updateContactHome(context, updateContact);
		updateContactEmail(context, updateContact);
		updateContactPhoto(context, updateContact);
	}


	private static void applyUpdateBatch(Context context, ArrayList <ContentProviderOperation> updateOperation){

		// Asking the Contact provider to update a contact                 
		try {
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, updateOperation);

		} catch (Exception e) {

			Log.d(TAG, "4444444444444444444444444444444");
			Log.d(TAG, "applyUpdateBatch - Exception - " + e.getMessage());
			e.printStackTrace();
		}
	}




	/*	public static boolean simContactsConnector(Context context, int operationType, Contact simContact){

		Log.e(TAG, "simContactsConnector");
		Uri simUri = Uri.parse(SIM_CARD_CONTACTS_URI);
		int changedLines = -1;

		//TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    //int simState = telMgr.getSimState();

	    //if(simState == TelephonyManager.SIM_STATE_READY){
		if(!simContact.getSimIndex().equals("-1")){
			switch(operationType){

				case DELETE_OPERATION:
					Log.e(TAG, "DELETE_OPERATION");
					changedLines = context.getContentResolver().delete(simUri, COLUMN_SIM_INDEX + " = " + Integer.parseInt(simContact.getSimIndex()), null);
					break;


				case UPDATE_OPERATION:
					Log.e(TAG, "UPDATE_OPERATION");
					SimContact preContact = getSimContact(context, simContact.getSimIndex());

				    ContentValues localContentValues = new ContentValues();
				    localContentValues.put("tag", preContact.name);
				    localContentValues.put(COLUMN_SIM_PHONE, preContact.number);
				    //localContentValues.put(COLUMN_SIM_MOBILE, preContact.additionalNumber);
				    //localContentValues.put(COLUMN_SIM_EMAIL, preContact.emails);
				    localContentValues.put("newTag", simContact.getDisplayName());
				    localContentValues.put("newNumber", simContact.getMobileNumber());
				    //localContentValues.put(COLUMN_SIM_MOBILE, simContact.getHomeNumber());

				    changedLines = context.getContentResolver().update(simUri, localContentValues, null, null);

					break;		
			}
			Log.e(TAG, "changedLines " + changedLines);
	    }
		return changedLines > 0;
	}*/
}