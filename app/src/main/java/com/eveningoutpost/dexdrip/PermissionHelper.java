package com.eveningoutpost.dexdrip;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionHelper {
    enum Permission {
        CAMERA(Manifest.permission.CAMERA),
        WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        ;

        public final String manifestPermissionName;

        Permission(String manifestPermissionName) {
            this.manifestPermissionName = manifestPermissionName;
        }
    }

    public static boolean hasAllPermissions(Activity activity, Permission ... permissions) {
        for (Permission permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission.manifestPermissionName) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean shouldShowRationale(Activity activity, Permission ... permissions) {
        for (Permission permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.manifestPermissionName)) {
                return true;
            }
        }
        return false;
    }

    public static void requestPermissions(Activity activity, Permission ... permissions) {
        String[] permissionNames = new String[permissions.length];
        for (int i = 0; i < permissions.length; ++i) {
            permissionNames[i] = permissions[i].manifestPermissionName;
        }

        ActivityCompat.requestPermissions(activity, permissionNames, /*code=*/100);
    }
}
