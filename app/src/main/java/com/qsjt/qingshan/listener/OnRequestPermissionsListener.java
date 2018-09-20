package com.qsjt.qingshan.listener;

import java.util.List;

/**
 * @author LiYouGui.
 */
public interface OnRequestPermissionsListener {
    /**
     * 同意授权
     */
    void onGranted();

    /**
     * 拒绝授权
     *
     * @param deniedPermissions 拒绝的权限
     */
    void onDenied(List<String> deniedPermissions);
}
