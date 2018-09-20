package com.qsjt.qingshan.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.qsjt.qingshan.application.MyApplication;

/**
 * @author LiYouGui.
 */
public class Config {

    private static Config instance;

    private final SharedPreferences mPreferences;

    private final SharedPreferences.Editor mEditor;

    public static Config getInstance() {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config(MyApplication.getMyApplication().getApplicationContext());
                }
            }
        }
        return instance;
    }

    @SuppressLint("CommitPrefEdits")
    public Config(Context context) {
        mPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    /**
     * @return IP
     */
    public String getIp() {
        return mPreferences.getString("ip", "39.107.113.157");
    }

    public void setIp(String ip) {
        mEditor.putString("ip", ip);
        mEditor.commit();
    }

    /**
     * @return port
     */
    public String getPort() {
        return mPreferences.getString("port", "8080");
    }

    public void setPort(String port) {
        mEditor.putString("port", port);
        mEditor.commit();
    }

    /**
     * @return 获取服务器地址
     */
    public String getServer() {
        return "http://" + getIp() + ":" + getPort() + "/";
    }

    /**
     * @return 获取API服务器地址
     */
    public String getApiServer() {
        return "http://" + getIp() + ":" + getPort() + "/";
    }

    /**
     * @return 访问令牌
     */
    public String getAccessToken() {
        return mPreferences.getString("accessToken", null);
    }

    public void setAccessToken(String accessToken) {
        mEditor.putString("accessToken", accessToken);
        mEditor.commit();
    }

    /**
     * @return 账户
     */
    public String getAccount() {
        return mPreferences.getString("account", null);
    }

    public void setAccount(String account) {
        mEditor.putString("account", account);
        mEditor.commit();
    }

    /**
     * @return 密码
     */
    public String getPassword() {
        return mPreferences.getString("password", null);
    }

    public void setPassword(String password) {
        mEditor.putString("password", password);
        mEditor.commit();
    }

    /**
     * @return 用户ID
     */
    public String getUserId() {
        return mPreferences.getString("userId", null);
    }

    public void setUserId(String userId) {
        mEditor.putString("userId", userId);
        mEditor.commit();
    }

    /**
     * @return 角色类型
     */
    public String getRoleType() {
        return mPreferences.getString("roleType", null);
    }

    public void setRoleType(String roleType) {
        mEditor.putString("roleType", roleType);
        mEditor.commit();
    }

    /**
     * @return 昵称
     */
    public String getNickname() {
        return mPreferences.getString("nickname", null);
    }

    public void setNickname(String nickname) {
        mEditor.putString("nickname", nickname);
        mEditor.commit();
    }

    /**
     * @return 头像URL
     */
    public String getAvatar() {
        return mPreferences.getString("avatar", null);
    }

    public void setAvatar(String avatar) {
        mEditor.putString("avatar", avatar);
        mEditor.commit();
    }
}
