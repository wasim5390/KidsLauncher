package com.wiser.kids.util;

import android.net.Uri;

/**
 * This class represents a single record in the Call Log DB
 * @author shed
 *
 */
public class CallRecord {
	
	public static final int TYPE_KNOWN_CONTACT 	= 0;
	public static final int TYPE_KNOWN_NUMBER 	= 1; // we know the number, we don't have it in our address book
	public static final int TYPE_UNKNOWN	 	= 2; //unknown number
	public static final int TYPE_SEPERATOR		= 3;
	public static final int TYPE_PLACE_HOLDER	= 4; //you did not make any calls today...
	
	public String displayName;
	public String phoneNumber;
	public long timeStamp;
	public int direction; //incoming /outgoing / missed
	public String contactId;
	public int type;
	public Long photoId;
	public Uri uri;
	public String timeString;

}
