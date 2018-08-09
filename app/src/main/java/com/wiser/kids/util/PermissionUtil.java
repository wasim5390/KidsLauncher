package com.wiser.kids.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

/**
 * Created by sidhu on 6/8/2018.
 */

public class PermissionUtil {

    /**
     * Requesting location permission
     * This uses multiple permission model from dexter
     * Once the permission granted, acts as required
     * On permanent denial opens settings dialog
     */
    public static void requestPermissions(Activity activity,PermissionCallback permissionCallback) {
        Dexter.withActivity(activity)
                .withPermissions(
                        new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS,
                                Manifest.permission.WRITE_CALL_LOG,Manifest.permission.WRITE_CONTACTS,})
                .withListener(new MultiplePermissionsListener() {


                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            permissionCallback.onPermissionsGranted();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            permissionCallback.onPermissionDenied();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(activity, "Error occurred!", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    /**
     * Requesting location permission
     * This uses single permission model from dexter
     * Once the permission granted, acts as required
     * On permanent denial opens settings dialog
     */
    public static void requestPermission(Activity activity,String permission,PermissionCallback permissionCallback) {
        Dexter.withActivity(activity)
                .withPermission(permission)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        permissionCallback.onPermissionsGranted(response.getPermissionName());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        permissionCallback.onPermissionDenied();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).withErrorListener(error -> Toast.makeText(activity, error.name(), Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    public static boolean isPermissionGranted(Context context,String permission){
        if (ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }


    public interface PermissionCallback{
        void onPermissionsGranted(String permission);
        void onPermissionsGranted();
        void onPermissionDenied();
    }
}
