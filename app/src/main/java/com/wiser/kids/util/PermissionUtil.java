package com.wiser.kids.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

/**
 * Created by sidhu on 6/8/2018.
 */

public class PermissionUtil {

    /**
     * Requesting location permission
     * This uses single permission model from dexter
     * Once the permission granted, show's accurate location
     * On permanent denial opens settings dialog
     */
    public static void requestLocationPermission(Activity activity,PermissionCallback permissionCallback) {
        Dexter.withActivity(activity)
                .withPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION})
                .withListener(new MultiplePermissionsListener() {


                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            permissionCallback.onPermissionGranted();
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

    public static boolean isLocationPermissionGranted(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public interface PermissionCallback{
        void onPermissionGranted();
        void onPermissionDenied();
    }
}
