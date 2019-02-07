package com.uiu.kids.ui.home.contact;


/**
 * A Class which represents ParseContact it has all contact fields
 * 
 */
public class Contact 
{
	private static final String TAG = "ParseContact";

	private String mAndroidID = "-1";

	private String mLookupKey;
	private String mDisplayName;
	private String mPhotoID;
	private String mFirstName;
	private String mFamilyName;
	private String mMiddleName;
	private String mMobileNumber;
	private String mHomeNubmer;
	private String mEmail;
	private String mPhotoPath;
	private String mPhotoUri;
	private long mSlideID = -1;
	private String parseId;

	public String getParseId() {
		return parseId;
	}

	public void setParseId(String parseId) {
		this.parseId = parseId;
	}

	public String getAndroidID()
	{
		return mAndroidID;
	}

	public String getPhotoUri() {
		return mPhotoUri;
	}

	public void setPhotoUri(String photoUri) {
		mPhotoUri = photoUri;
	}

	public void setAndroidID(String androidID)
	{
		mAndroidID = androidID;
	}

	public String getLookupKey()
	{
		return mLookupKey;
	}

	public void setLookupKey(String lookupKey)
	{
		mLookupKey = lookupKey;
	}

	public String getDisplayName()
	{
		return mDisplayName;
	}

	public void setDisplayName(String displayName)
	{
		mDisplayName = displayName;
	}

	public String getPhotoID()
	{
		return mPhotoID;
	}

	public void setPhotoID(String photoID)
	{
		mPhotoID = photoID;
	}

	public String getFirstName()
	{
		return mFirstName;
	}

	public void setFirstName(String firstName)
	{
		mFirstName = firstName;
	}

	public String getFamilyName()
	{
		return mFamilyName;
	}

	public void setFamilyName(String familyName)
	{
		mFamilyName = familyName;
	}

	public String getMiddleName()
	{
		return mMiddleName;
	}

	public void setMiddleName(String middleName)
	{
		mMiddleName = middleName;
	}

	public String getMobileNumber()
	{
		return mMobileNumber;
	}

	public void setMobileNumber(String mobileNumber)
	{
		mMobileNumber = mobileNumber;
	}

	public String getHomeNumber()
	{

		return mHomeNubmer;
	}

	public void setHomeNubmer(String homeNubmer)
	{
		mHomeNubmer = homeNubmer;
	}

	public String getEmail()
	{
		return mEmail;
	}

	public void setEmail(String email)
	{
		mEmail = email;
	}

	public String getPhotoPath()
	{
		return mPhotoPath;
	}

	public void setPhotoPath(String photoPath)
	{
		mPhotoPath = photoPath;
	}

	
	

	public long getSlideID()
	{
		return mSlideID;
	}


	public void setSlideID(long mSlideID)
	{
		this.mSlideID = mSlideID;
	}


	@Override
	public String toString()
	{

		return "Name :" + mDisplayName + " LookupID :" + mLookupKey + "androidID" + mAndroidID+"\n"+
				"first Name :" + mFirstName+ " familiy name:" + mFamilyName+ " middle" + mMiddleName+"\n"+
		"home number :" + mHomeNubmer+ " Mobile number:" + mMobileNumber+ " email" + mEmail+"\n";
	}


}
