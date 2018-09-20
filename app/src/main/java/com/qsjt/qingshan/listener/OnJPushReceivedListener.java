package com.qsjt.qingshan.listener;

import com.qsjt.qingshan.model.response.NotifyMessage;

/**
 * @author LiYouGui.
 */
public interface OnJPushReceivedListener {
    /**
     * 通知推送
     *
     * @param message 通知消息
     */
    void onJPReceived(NotifyMessage message);
}
