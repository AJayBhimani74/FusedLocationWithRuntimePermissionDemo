package com.example.ajay.locationdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

/**
 * Created by ajay on 14/11/16.
 */

public abstract class MarshMallowSupportActivity extends AppCompatActivity {
    private Permission.PermissionCallback mPermissionCallback = null;
    private Permission mPermission = null;

    public MarshMallowSupportActivity() {
    }

    public void requestAppPermissions(@NonNull Permission permission) {
        this.mPermission = permission;
        this.mPermissionCallback = this.mPermission.getPermissionCallback();
        this.requestAppPermissions(this.mPermission.getRequestedPermissions(), this.mPermission.getRequestCode(), this.mPermission.getPermissionCallback());
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(this.verifyPermissions(grantResults)) {
            this.mPermissionCallback.onPermissionGranted(requestCode);
        } else {
            boolean showRationale = this.shouldShowRequestPermissionRationale(permissions);
            if(!showRationale) {
                this.doNotAskedEnable(requestCode);
            } else {
                this.showRationalMessage(requestCode);
            }
        }

    }

    private void requestAppPermissions(String[] requestedPermissions, int requestCode, @Nullable Permission.PermissionCallback permissionCallback) {
        if(!this.hasPermissions(requestedPermissions)) {
            if(this.shouldShowRequestPermissionRationale(requestedPermissions)) {
                this.showRationalMessage(requestCode);
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            this.mPermissionCallback.onPermissionGranted(requestCode);
        }

    }

    protected boolean hasPermissions(String[] permissions) {
        int length = permissions.length;

        for(int i = 0; i < length; ++i) {
            if(ContextCompat.checkSelfPermission(this, permissions[i]) != 0) {
                return false;
            }
        }

        return true;
    }

    public boolean shouldShowRequestPermissionRationale(String[] permissions) {
        int length = permissions.length;

        for(int i = 0; i < length; ++i) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                return true;
            }
        }

        return false;
    }

    private boolean verifyPermissions(int[] grantResults) {
        if(grantResults.length < 1) {
            return false;
        } else {
            int[] var2 = grantResults;
            int var3 = grantResults.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                int result = var2[var4];
                if(result != 0) {
                    return false;
                }
            }

            return true;
        }
    }

    private void showRequestPermissionDialog() {
        String message = this.mPermission.getRationalDialogMessage();
        String positiveButton = this.getString(R.string.rational_permission_proceed);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        if(!TextUtils.isEmpty(this.mPermission.getRationalDialogTitle())) {
            alertDialogBuilder.setTitle(this.mPermission.getRationalDialogTitle());
        }

        alertDialogBuilder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface arg0, int arg1) {
                ActivityCompat.requestPermissions(MarshMallowSupportActivity.this, mPermission.getRequestedPermissions(), mPermission.getRequestCode());
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void showRationalMessage(int requestCode) {
        if(this.mPermission.isShowCustomRationalDialog()) {
            this.mPermissionCallback.onPermissionDenied(requestCode);
        } else {
            this.showRequestPermissionDialog();
        }

    }

    private void doNotAskedEnable(int requestCode) {
        if(this.mPermission.isShowCustomSettingDialog()) {
            this.mPermissionCallback.onPermissionAccessRemoved(requestCode);
        } else {
            this.showSettingsPermissionDialog();
        }

    }

    private void showSettingsPermissionDialog() {
        String message = this.mPermission.getSettingDialogMessage();
        String positiveButton = this.getString(R.string.permission_string_btn);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        if(!TextUtils.isEmpty(this.mPermission.getSettingDialogTitle())) {
            alertDialogBuilder.setTitle(this.mPermission.getSettingDialogTitle());
        }

        alertDialogBuilder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                startSettingActivity();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.show();
    }

    public void startSettingActivity() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:" + this.getPackageName()));
        intent.addFlags(268435456);
        intent.addFlags(1073741824);
        intent.addFlags(8388608);
        this.startActivity(intent);
    }
}
