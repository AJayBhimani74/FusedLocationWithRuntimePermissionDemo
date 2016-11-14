package com.example.ajay.locationdemo;

import android.support.annotation.NonNull;

/**
 * Created by ajay on 14/11/16.
 */

public class Permission {
    private String[] requestedPermissions;
    private int requestCode;
    private Permission.PermissionCallback mPermissionCallback;
    private boolean showCustomRationalDialog;
    private String rationalDialogTitle;
    private String rationalDialogMessage;
    private boolean showCustomSettingDialog;
    private String settingDialogTitle;
    private String settingDialogMessage;

    private Permission(Permission.PermissionBuilder builder) {
        this.mPermissionCallback = null;
        this.showCustomRationalDialog = true;
        this.showCustomSettingDialog = true;
        this.requestedPermissions = builder.requestedPermissions;
        this.requestCode = builder.requestCode;
        this.mPermissionCallback = builder.mPermissionCallback;
        this.showCustomRationalDialog = builder.showCustomRationDialog;
        if(!this.showCustomRationalDialog) {
            this.rationalDialogMessage = builder.rationalDialogMessage;
            this.rationalDialogTitle = builder.rationalDialogTitle;
        }

        this.showCustomSettingDialog = builder.showCustomSettingDialog;
        if(!this.showCustomSettingDialog) {
            this.settingDialogMessage = builder.settingDialogMessage;
            this.settingDialogTitle = builder.settingDialogTitle;
        }

    }

    public String[] getRequestedPermissions() {
        return this.requestedPermissions;
    }

    public int getRequestCode() {
        return this.requestCode;
    }

    public Permission.PermissionCallback getPermissionCallback() {
        return this.mPermissionCallback;
    }

    public boolean isShowCustomRationalDialog() {
        return this.showCustomRationalDialog;
    }

    public String getRationalDialogTitle() {
        return this.rationalDialogTitle;
    }

    public String getRationalDialogMessage() {
        return this.rationalDialogMessage;
    }

    public boolean isShowCustomSettingDialog() {
        return this.showCustomSettingDialog;
    }

    public String getSettingDialogTitle() {
        return this.settingDialogTitle;
    }

    public String getSettingDialogMessage() {
        return this.settingDialogMessage;
    }

    public interface PermissionCallback {
        void onPermissionGranted(int var1);

        void onPermissionDenied(int var1);

        void onPermissionAccessRemoved(int var1);
    }

    public static class PermissionBuilder {
        private String[] requestedPermissions;
        private int requestCode;
        private boolean showCustomRationDialog = true;
        private String rationalDialogTitle;
        private String rationalDialogMessage;
        private boolean showCustomSettingDialog = true;
        private String settingDialogTitle;
        private String settingDialogMessage;
        public Permission.PermissionCallback mPermissionCallback = null;

        public PermissionBuilder(String[] requestedPermissions, int requestCode, @NonNull Permission.PermissionCallback listener) {
            this.requestedPermissions = requestedPermissions;
            this.requestCode = requestCode;
            this.mPermissionCallback = listener;
        }

        public Permission.PermissionBuilder enableDefaultRationalDialog(@NonNull String title, @NonNull String message) {
            this.showCustomRationDialog = false;
            this.rationalDialogMessage = message;
            this.rationalDialogTitle = title;
            return this;
        }

        public Permission.PermissionBuilder enableDefaultSettingDialog(@NonNull String title, @NonNull String message) {
            this.showCustomSettingDialog = false;
            this.settingDialogMessage = message;
            this.settingDialogTitle = title;
            return this;
        }

        public Permission build() {
            return new Permission(this);
        }
    }
}
