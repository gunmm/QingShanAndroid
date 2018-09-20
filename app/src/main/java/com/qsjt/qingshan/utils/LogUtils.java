/*
 * Copyright (C) 2016 venshine.cn@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qsjt.qingshan.utils;

import android.util.Log;


/**
 * Log日志打印操作
 *
 * @author LiYouGui
 */
public class LogUtils {

    private static final boolean DEBUG = true;

    private static final String TAG = "log";

    /**
     * Drawing toolbox
     */
    private static final char TOP_LEFT_CORNER = '┌';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char MIDDLE_CORNER = '├';
    private static final char HORIZONTAL_LINE = '│';
    private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    /**
     * @return 获取当前类名
     */
    private static String getSimpleClassName(String className) {
        int lastIndex = className.lastIndexOf(".");
        return className.substring(lastIndex + 1);
    }

    /**
     * logChunk
     *
     * @param msg
     * @return log
     */
    private static String logChunk(String msg) {
        // 这里的数组的index，即2，是根据工具类的层级取的值，可根据需求改变
        StackTraceElement[] trace = new Exception().getStackTrace();
        StackTraceElement stack = trace[2];
        return "\t\n" + TOP_BORDER +
                "\n" + HORIZONTAL_LINE + getSimpleClassName(stack.getClassName()) +
                "." + stack.getMethodName() + " (" + stack.getFileName() +
                ":" + stack.getLineNumber() + ")" +
                "\n" + MIDDLE_BORDER +
                "\n" + HORIZONTAL_LINE + msg +
                "\n" + BOTTOM_BORDER;
    }

    /**
     * debug log
     *
     * @param msg
     */
    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, logChunk(msg));
        }
    }

    /**
     * debug log
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, logChunk(msg));
        }
    }

    /**
     * error log
     *
     * @param msg
     */
    public static void e(String msg) {
        if (DEBUG) {
            Log.e(TAG, logChunk(msg));
        }
    }

    /**
     * error log
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, logChunk(msg));
        }
    }

    /**
     * info log
     *
     * @param msg
     */
    public static void i(String msg) {
        if (DEBUG) {
            Log.i(TAG, logChunk(msg));
        }
    }

    /**
     * info log
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, logChunk(msg));
        }
    }

    /**
     * warn log
     *
     * @param msg
     */
    public static void w(String msg) {
        if (DEBUG) {
            Log.w(TAG, logChunk(msg));
        }
    }

    /**
     * warn log
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, logChunk(msg));
        }
    }
}
