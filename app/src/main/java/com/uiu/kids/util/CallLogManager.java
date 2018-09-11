package com.uiu.kids.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;


import com.uiu.kids.R;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@SuppressLint("DefaultLocale")
public class CallLogManager {

    private static final int TIME_TODAY = 1;
    private static final int TIME_YESTERDAY = 2;

    private static final long ONE_DAY = 1000 * 60 * 60 * 24;
    private static final int NUM_OF_SEGMENTS = 3;


    private static CallLogManager mInstance;
    private Context mContext;

    public CallLogManager(Context c) {
        mContext = c;
    }

    public static CallLogManager getInstance(Context c) {
        //mContext = c;
        if (null == mInstance) {
            mInstance = new CallLogManager(c);
        }
        return mInstance;
    }

//    /**
//     * Utility function that will update the CallLog.Calls.CACHED_NAME column
//     *
//     * @param phoneNumber Phone number of calls that should be updated
//     * @param displayName The new name to be displayed.
//     */
//    public static void updateCallLogDb(Activity activity, String phoneNumber, String displayName) {
//        ContentValues cv = new ContentValues();
//        cv.put(CallLog.Calls.CACHED_NAME, displayName);
//
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int refdquestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            // NewHomeActivity.runtimePermissionHelper.addRequirePermission(Manifest.permission.WRITE_CALL_LOG);
//            return;
//        }
//        mContext.getContentResolver().update(CallLog.Calls.CONTENT_URI, cv, CallLog.Calls.NUMBER + " = ?", new String[]{phoneNumber});
//
//    }


    public static int getNumOfUnrededMissedCalls(Context c) {
        int num = 0;
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return 0;
            //NewHomeActivity.runtimePermissionHelper.addRequirePermission(Manifest.permission.READ_CALL_LOG);
        }
        Cursor cursor = c.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                new String[]{CallLog.Calls.IS_READ}, CallLog.Calls.IS_READ + " = 0 AND " + CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE, null, null);

//		cursor.moveToFirst();
        if(cursor !=null) { // Fabric# 185
            num = cursor.getCount();
            cursor.close();
        }
        return num;
    }

    public static void markAllMissedCallsAsRead(Context c) {
        ContentValues cv = new ContentValues();
        cv.put(CallLog.Calls.IS_READ, true);
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
			// If we dont have permission, dont attempt the action.
			return;
            //NewHomeActivity.runtimePermissionHelper.addRequirePermission(Manifest.permission.WRITE_CALL_LOG);
        }
        c.getContentResolver().update(CallLog.Calls.CONTENT_URI, cv, CallLog.Calls.IS_READ + "=0", null);
    }

    @SuppressLint("DefaultLocale")
    public ArrayList<CallRecord> getCallRecords() {

        int currentTimeSegment = 0;
        long nextTimeThreshold = System.currentTimeMillis();//calculateNextTimeThreshold(currentTimeSegment); //find the time stamp of TODAY's treshold
        Cursor c;
        try {
            //sony C2104 crashes when trying to execute the following query, with "no such column subscirption"
            // this is a safety so we won't crash - see crashlytics #396 (@link https://www.crashlytics.com/wiser/android/apps/com.wiser.home/issues/533dea35fabb27481b254d17)
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
				// If we dont have permission, dont attempt call.
				return new ArrayList<CallRecord>();
                // NewHomeActivity.runtimePermissionHelper.addRequirePermission(Manifest.permission.READ_CALL_LOG);
            }
            c = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        }
		catch (SQLiteException se) {
		//	Crashlytics.logException(se);
			return new ArrayList<CallRecord>();
		}
		final int number 	= c.getColumnIndex(CallLog.Calls.NUMBER);
		final int type 		= c.getColumnIndex(CallLog.Calls.TYPE);
		final int date 		= c.getColumnIndex(CallLog.Calls.DATE);
		final int name 		= c.getColumnIndex(CallLog.Calls.CACHED_NAME);
		HashMap<String, Long> photoIdsMap = new HashMap<String, Long>();

		ArrayList<CallRecord> res = new ArrayList<CallRecord>(c.getCount());
		CallRecord current;

		while (c.moveToNext()) {
			current = new CallRecord();
			current.phoneNumber = c.getString(number);
			current.direction = c.getInt(type);
			current.timeStamp = c.getLong(date);
			current.displayName = c.getString(name);
			if (TextUtils.isEmpty(current.displayName)) {
				//No display name, we have two options: unknown number or known number which is not in my contacts.
				if (TextUtils.isEmpty(current.phoneNumber)) {
					//unknown number
					current.type = CallRecord.TYPE_UNKNOWN;
					current.displayName = mContext.getString(R.string.unknown);
				}
				else {
					// known number, unknown contact
					current.type = CallRecord.TYPE_KNOWN_NUMBER;
					current.displayName = current.phoneNumber;
					
					//verify that the phone number is a valid phone number. 
					// some phone apps will put "-2" for unknown numbers etc.
					if (CallLog.Calls.OUTGOING_TYPE != current.direction) {
						if ( false == android.util.Patterns.PHONE.matcher(current.phoneNumber).matches()) {
							current.type = CallRecord.TYPE_UNKNOWN;
							current.displayName = mContext.getString(R.string.unknown);
						}
					}
				}
			}
			else {
				//known contact
				current.type = CallRecord.TYPE_KNOWN_CONTACT;
				if (photoIdsMap.containsKey(current.phoneNumber)) {
					current.photoId = photoIdsMap.get(current.phoneNumber);
				}
				else {
					current.photoId = getContactPhotoId(current.phoneNumber);
					photoIdsMap.put(current.phoneNumber, current.photoId);
				}
			}

			if (current.timeStamp < nextTimeThreshold) {
				String title ="";
				while (current.timeStamp < nextTimeThreshold) {
					// we need to change time segments (i.e. switch from TODAY to Yesterday to EARLIER
					nextTimeThreshold = calculateNextTimeThreshold(currentTimeSegment);

					currentTimeSegment++;

					switch (currentTimeSegment) {
					case TIME_TODAY:
						title = mContext.getString(R.string.today);
						break;
					case TIME_YESTERDAY:
						title = mContext.getString(R.string.yesterday);
						break;
					default:
						title = mContext.getString(R.string.earlier);
					}
					CallRecord tmp = new CallRecord();
					tmp.displayName = title;
					tmp.type = CallRecord.TYPE_SEPERATOR;
					res.add(tmp);
				}
			}

			setTimeString(currentTimeSegment, current);
            try {
                res.add(current);
            }catch (OutOfMemoryError e){
            	e.printStackTrace();
			}
                //Crashlytics.logException(e);}
		}

		c.close();

		//loop through the results and check if we have an "empty" section (i.e. "no calls today")
		int segmentsCount = 0 ;
		int index = 0;
		ArrayList<CallRecord> tmp = new ArrayList<CallRecord>(res.size());
		while (segmentsCount <= NUM_OF_SEGMENTS  && index < res.size()){
			tmp.add(res.get(index));

			if (res.get(index).type == CallRecord.TYPE_SEPERATOR ) {
				//separator
				segmentsCount++;
				//check if we have another item and it is separator as well
				if (res.size() > index+1 && res.get(index+1).type == CallRecord.TYPE_SEPERATOR) {
					// we have two separators in a row
					// need to add another dummy record that will say "you did not have any calls today / yesterday
					CallRecord dummy = new CallRecord();
					dummy.displayName = mContext.getString(R.string.you_didn_t_make_any_calls_) + res.get(index).displayName.toLowerCase();
					dummy.type = CallRecord.TYPE_PLACE_HOLDER;
					tmp.add(dummy);
				}


			}	
			index++;
		}


		return tmp;
	}

	private void setTimeString(int currentTimeSegment, CallRecord current) {

		//		SimpleDateFormat sdf;
		//		String format ="";
		switch (currentTimeSegment) {
		case TIME_TODAY:

			long delta = System.currentTimeMillis() - current.timeStamp;
			if (delta < (1000*60*60)) {
				//less than one hour ago
				//display XX min ago

				delta = delta / 1000;
				delta = delta / 60;
//				current.timeString = delta + " Minutes ago";
				Resources r = mContext.getResources();
				current.timeString  = r.getQuantityString(R.plurals.minutes_ago, (int)delta, delta);

			}
			else {
				//display hh:mmm
				current.timeString =  SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(new Date(current.timeStamp));//DateFormat.format("HH:mm", current.timeStamp).toString();
			}
			break;

		case TIME_YESTERDAY:
			current.timeString = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(new Date(current.timeStamp));//DateFormat.format("HH:mm", current.timeStamp).toString();
			break;

		default:
			current.timeString = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT,SimpleDateFormat.SHORT).format(new Date(current.timeStamp));//DateFormat.format("MMM dd, HH:mm", current.timeStamp).toString();
		}
	}

	private long calculateNextTimeThreshold (int currentTimeSegment) {
		long res = System.currentTimeMillis();

		// get rid of seconds and minutes to round hour
		//		res = res / 1000; //ms
		//		res = res / 60; //seconds
		//		res = res / 60; //minutes;
		//		res = res / 24; //hours

		res = res / ONE_DAY;

		res = res * ONE_DAY;

		switch (currentTimeSegment) {
		case 0:
			//calculate "TODAY" threshold - at 12am
			break;
			//			for debug only - to simulate a "Today" which is only one hour - thus make it easier to be in "no calls today" scenario. 
			//			return System.currentTimeMillis() - (1000 * 60 * 60);

		case TIME_TODAY:
			//calculate yesterday's thershold
			res = res - ONE_DAY; //one day
			break;


		default: 
			res = 0;
			break;
		}

		return res;
	}

	/**
     * Check for read android.permission.READ_CONTACTS in Android 6, or this method throws
     * a security exception. #158
	 * Get the contact's photo ID from phone number
	 * @param phoneNumber
	 * @return
	 */
	private long getContactPhotoId (String phoneNumber) {
//		long id = -1;
		long photoId = -1;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return photoId;
        }

		// query the contact
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        Log.e("Photo", String.valueOf(uri));

		Cursor c = mContext.getContentResolver().query(uri, new String[]{PhoneLookup.PHOTO_ID, PhoneLookup._ID}, null, null, null);

		try {
			if (c.moveToNext()) {
//			id = c.getLong(c.getColumnIndex(PhoneLookup._ID));
				photoId = c.getLong(c.getColumnIndex(PhoneLookup.PHOTO_ID));
				Log.e("photo uri", String.valueOf(photoId));
			}
		}catch (IllegalArgumentException e){}
		c.close();
		return photoId;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////

	private static class BitmapHolder {
		private static final int NEEDED = 0;
		private static final int LOADING = 1;
		private static final int LOADED = 2;

		int state;
		SoftReference<Bitmap> bitmapRef;
	}

	private final static ConcurrentHashMap<Long, BitmapHolder> mBitmapCache = new ConcurrentHashMap<Long, BitmapHolder>();

	/**
	 * A map from ImageView to the corresponding photo ID. Please note that this
	 * photo ID may change before the photo loading request is started.
	 */
	private final ConcurrentHashMap<ImageView, Long> mPendingRequests = new ConcurrentHashMap<ImageView, Long>();


	public boolean isPermissionAvailable(String permission) {
		boolean isPermissionAvailable = true;
		if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED){
			isPermissionAvailable = false;
		}
		return isPermissionAvailable;
	}

	public void loadPhoto(ImageView view, long photoId) {

		boolean loaded = loadCachedPhoto(view, photoId);

		/** check permissions here in the constructor. Later on, when requesting to load a photo
		 * only continue if the permission is granted.
		 * Fabric #1260
		 */
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (this.mContext != null) {

				if (isPermissionAvailable(Manifest.permission.READ_CONTACTS)) {
					// All permissions available. Go with the flow
				} else {
					// Few permissions not granted. Ask for ungranted permissions
					// runtimePermissionHelper.requestPermissionsIfDenied();
					return;
				}
			} else {
				//
			}
		} else {
			// SDK below API 23. Do nothing just go with the flow.
		}

		if (loaded) {
			mPendingRequests.remove(view);
		} else {
			mPendingRequests.put(view, photoId);
//			if (!mPaused) {
//				// Send a request to start loading photos
//				requestLoading();
//			}
		}

	}

	private boolean loadCachedPhoto(ImageView view, long photoId) {
		BitmapHolder holder = mBitmapCache.get(photoId);
		if (holder == null) {
			holder = new BitmapHolder();
			mBitmapCache.put(photoId, holder);
		} else if (holder.state == BitmapHolder.LOADED) {
			// Null bitmap reference means that database contains no bytes for the photo
			if (holder.bitmapRef == null) {
				view.setImageResource(R.mipmap.avatar_male2);
				return true;
			}

			Bitmap bitmap = holder.bitmapRef.get();
			if (bitmap != null) {
				view.setImageBitmap(bitmap);
				return true;
			}

			// Null bitmap means that the soft reference was released by the GC
			// and we need to reload the photo.
			holder.bitmapRef = null;
		}

		// The bitmap has not been loaded - should display the placeholder image.
		//view.setImageResource(mDefaultResourceId);
		holder.state = BitmapHolder.NEEDED;
		return false;
	}


}
