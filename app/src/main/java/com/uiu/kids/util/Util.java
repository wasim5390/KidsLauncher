package com.uiu.kids.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.location.Geofence;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.uiu.kids.Constant;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.R;
import com.uiu.kids.model.Setting;
import com.uiu.kids.model.response.APIError;
import com.uiu.kids.source.RetrofitHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

import static android.content.Context.VIBRATOR_SERVICE;


/**
 * Created on 23/10/2017.
 */

public class Util {

    public static final String DATE_FORMAT_1 = "MM/dd/yyyy hh:mm:ss";
    public static final String DATE_FORMAT_2 = "MMM dd";
    public static final String DATE_FORMAT_3 = "hh:mm a";
    public static final String DATE_FORMAT_4 = "MM/dd/yyyy hh:mm a";
    private static final String TAG = "Util";
    private static String Alarm_action="alarm_action";
    public static String encodeBase64(String s) throws UnsupportedEncodingException {
        byte[] data = s.getBytes("UTF-8");
        String encoded = Base64.encodeToString(data, Base64.NO_WRAP);
        return encoded;
    }

    public static String getAuthorizationHeader(Context context) throws UnsupportedEncodingException {
        String username = PreferenceUtil.getInstance(context).getUsername();
        String password = PreferenceUtil.getInstance(context).getPassword();

        if (username!=null&&!username.isEmpty()&&password!=null&&!password.isEmpty()) {
            return encodeBase64(username+":"+password);
        } else {
            return null;
        }
    }

    public static Drawable getFlagIconForCountry(Context context, String name) {
        try {
            int identifier = context.getResources().getIdentifier("ic_"+name.toLowerCase(), "drawable", context.getPackageName());
            if (identifier!=0) {
                return ResourcesCompat.getDrawable(
                        context.getResources(),
                        identifier,
                        null
                );
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String toMd5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getValidNumber(String number, String countryCode) {
        String formattedNumber = "";
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = PhoneNumberUtil.getInstance().parse(number, countryCode);
            formattedNumber = PhoneNumberUtil.getInstance().format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

            if (PhoneNumberUtil.getInstance().isValidNumber(phoneNumber)) {
                return formattedNumber;
            } else {
                return null;
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    public static Calendar formatDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(format.format(calendar.getTime())));
        return calendar;

    }

    public static String formatDateForChart(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_2);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(date) * 1000);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String formatDateForMessagesHistory(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_3);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(date) * 1000);
        return simpleDateFormat.format(calendar.getTime());
    }


    public static String formatDate(String format, long dateInMilli) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMilli);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static Date getDateFromMilliseconds(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendar.getTime();
    }

    public static int getDifferenceInDays(Date startDate, Date endDate) {
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;

        return (int) elapsedDays;
    }


    public static String getDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        if (elapsedDays>0) {
            return elapsedDays + " day(s) ago";
        }
        else if(elapsedHours>0){
            return elapsedHours + " hours ago";
        }
        else if(elapsedMinutes>0) {
            return elapsedMinutes + " minutes ago";
        }
        else if(elapsedSeconds>0) {
            return elapsedSeconds + " seconds ago";
        }else if(different<60){
            return " just now";
        }
        return "";
    }

    public static boolean isTimeOlder(String dateTimeInMilli){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(dateTimeInMilli));
        Calendar calendarCurrent = Calendar.getInstance();
        int diff = calendar.getTime().compareTo(calendarCurrent.getTime());
        if(diff>0)
            return false;
        return true;
    }

    public static boolean isTheSameDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        boolean isSameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        return isSameDay;
    }

    public static boolean isYesterdayDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterdayDate = calendar.getTime();

        return isTheSameDays(yesterdayDate, date);
    }

    public static boolean isWeekAgo (Date date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);
        boolean isSameMonth = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);

        if (isSameMonth) {
            int days = getDifferenceInDays(date, cal1.getTime());
            return days<=7;
        } else {
            return false;
        }
    }

    public static String getDeliveryStatus(Long dueDate){
        if(dueDate==null)
            return "Expired";
        Date mDueDate = getDateFromMilliseconds(dueDate);
        Date mCurrentDate = Calendar.getInstance().getTime();
        float differenceInDays = getDifferenceInDays(mCurrentDate,mDueDate);

        if(mCurrentDate.after(mDueDate))
            return "Expired";

        if(isTheSameDays(mDueDate,mCurrentDate))
            return "Expire in "+getDifference(mCurrentDate,mDueDate);

        if(differenceInDays<1)
            return "Due tomorrow";

        if(differenceInDays<7)
            return "Due in "+(int)Math.ceil((double) differenceInDays)+" day(s)";

        if(differenceInDays>=7)
            return "Due in "+(int)Math.ceil((double) differenceInDays/7.0)+" week(s)";
        return "";
    }

    public static String getSyncTime(Long lastSyncTime){
        if(lastSyncTime==null)
            return "Not synced yet";
        Date mElapsedDate = getDateFromMilliseconds(lastSyncTime);
        Date mCurrentDate = Calendar.getInstance().getTime();
        String difference = getDifference(mElapsedDate,mCurrentDate);
        if(difference.isEmpty()) return difference;
        return "Last synced "+difference;
    }

    public static String toCurrency (double price, String currency) {
        NumberFormat numberFormat = new DecimalFormat();
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        String formattedValue = numberFormat.format(price);
        return currency + formattedValue;
    }

    public static String toCurrency (double price, String currency, int minFraction, int maxFraction) {
        NumberFormat numberFormat = new DecimalFormat();
        numberFormat.setMinimumFractionDigits(minFraction);
        numberFormat.setMaximumFractionDigits(maxFraction);
        String formattedValue = numberFormat.format(price);
        return currency + formattedValue;
    }


    public static APIError parseError(Response<?> response) {

        Converter<ResponseBody, APIError> converter =
                RetrofitHelper.getInstance().retrofit.responseBodyConverter(APIError.class, new Annotation[0]);
        APIError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            e.printStackTrace();
            return new APIError();
        }

        return error;
    }

    public static boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) KidsLauncherApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean containsUnicode(String text) {
        char[] symbols = text.toCharArray();
        for (int i = 0; i < symbols.length; i++) {
            if (Character.UnicodeBlock.of(symbols[i])!= Character.UnicodeBlock.BASIC_LATIN) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        //final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static boolean isValidEmail(final String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public static void vibrateDevice(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(500);
        }
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static boolean isSystemPackage(ResolveInfo resolveInfo) {
        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
    public static Bitmap drawablToBitmap(Drawable icon)
    {
        Bitmap bitmap=null;
        if (icon!=null)
        {
            bitmap= ((BitmapDrawable)icon).getBitmap();
        }

        return bitmap;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        encoded = "data:image/png;base64,"+encoded;
        return encoded;
    }

    public static String fileToBase64(File file){
        if (file.exists() && file.length() > 0) {
            Bitmap bm = BitmapFactory.decodeFile(file.getPath());
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, bOut);
            String encoded = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
            encoded = "data:image/png;base64,"+encoded;
            return encoded;
        }
        return null;
    }

    public static void startFromPakage(String pkgName,Context context)
    {
        PackageManager pm = context.getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage(pkgName);
        if(launchIntent!=null) {
            context.startActivity(launchIntent);
        }
    }

    public static void startFromLink(String link ,Context context)
    {
        if(!link.startsWith("http"))
            link = "http://"+link;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        context.startActivity(browserIntent);
    }

    public static boolean isAvailable(Context ctx, Intent intent) {
        if (intent == null)
            return false;
        final PackageManager mgr = ctx.getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static Date convertStringDate(String dateC){
        String dtStart = dateC;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_1);
        Date date=null;
        try {
            date = format.parse(dtStart);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

//    public static void setAlarm(List<ReminderEntity> entities, Context context) {
//
//        AlarmManager[] alarmManager = new AlarmManager[entities.size()];
//       List<PendingIntent> pendingIntentList=new ArrayList<>();
//       for(int i=0;i<entities.size();i++) {
//           Intent intent = new Intent(this, ReminderReciever.class);
////           Bundle bundle = new Bundle();
////           bundle.putString("time", String.valueOf(entities.get(i).getdate().getTime()));
////           intent.putExtras(bundle);
//           PendingIntent pendingIntent = PendingIntent.getBroadcast(context
//                   , 1, intent, 0);
//           alarmManager[i]=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//           alarmManager[i].set(AlarmManager.ELAPSED_REALTIME_WAKEUP, entities.get(i).getdate().getTime(),
//                   pendingIntent);
//
//           Log.e("time", String.valueOf(entities.get(i).getTime()));
//
//           pendingIntentList.add(pendingIntent);
//       }
//    }


    public static int getOrientation(final String imagePath) {
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface
                    .ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;

    }

    public static Bitmap rotateBitmap(Bitmap source, int angle) {
        Matrix matrix = new Matrix(); matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true); }



    public static File copyFileOrDirectory(String srcDir, String dstDir) {

        File finalFile =null;
        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    return copyFileOrDirectory(src1, dst1);

                }
            } else {
                finalFile= copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalFile;
    }


    public static File copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }

        return destFile;
    }
    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public static int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }
    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     * */
    public static String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public static Geofence createGeofence(String id,double latitude,double longitude){
        int radius = 10; //meters
        // String id = UUID.randomUUID().toString();
        return new Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT|Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(latitude, longitude, radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setNotificationResponsiveness(0)
                .build();
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * Show dialog in the top of the screen
     *
     * @param activity    the activity which raising the dialog
     * @param contentView view for the dialog
     */
    public static Dialog showHeaderDialog(Activity activity, View contentView) {
        Log.i(TAG, "showFooterDialog");
        Dialog dialog = new Dialog(activity, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        return dialog;
    }

    /**
     * Show dialog in the center of the screen
     *
     * @param activity    the activity which raising the dialog
     * @param contentView view for the dialog
     */
    public static Dialog showDialog(Activity activity, View contentView) {
        Log.i(TAG, "showFooterDialog");
        Dialog dialog = new Dialog(activity, R.style.PopDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, activity.getResources().getDisplayMetrics());
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width = dimensionInDp;
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        return dialog;
    }

    /**
     * get uri from file path
     * @param path
     * @return
     */
    public static Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }

    /**
     * copy stream upto 2 mb
     * @param input
     * @param output
     * @throws IOException
     */
    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[2048];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


    public static void updateSystemSettings(Context context,Setting setting){
        SettingData.setBluetoothOnOff(setting.isBlueToothOn());
        //  SettingData.setBrightnessLevel(context,Math.round(setting.getBrightnessLevel()));
        SettingData.setGpsOnOff(context,setting.isLocationEnable());
        SettingData.setVolume(context,setting.getVolumeLevel());
        SettingData.setSoundState(context,setting.getSoundState());
        SettingData.setWifiConnected(context,setting.isWifiEnable());
        PreferenceUtil.getInstance(context).savePreference(Constant.PREF_KEY_SLEEP_MODE,setting.isSleepMode());
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }


    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public static File createVideoFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "VIDEO_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File file = File.createTempFile(
                "video",  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );
        return file;
    }
}
