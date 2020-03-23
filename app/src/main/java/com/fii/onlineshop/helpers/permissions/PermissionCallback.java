package com.fii.onlineshop.helpers.permissions;

public interface PermissionCallback {
    void onPermissionGranted();
    void onPermissionDenied();
}
