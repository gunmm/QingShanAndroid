package com.qsjt.qingshan.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.qsjt.qingshan.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author LiYouGui
 */

public class MapUtils {


    /**
     * 百度坐标系BD-09 转 火星坐标系GCJ-02
     *
     * @param bdLat 百度纬度
     * @param bdLng 百度经度
     * @return 火星坐标系经纬度
     */
    public static LatLng bd2Gcj(double bdLat, double bdLng) {
        double PI = 3.1415926535897932384626;
        double xPi = PI * 3000.0 / 180.0;

        double x = bdLng - 0.0065;
        double y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * xPi);

        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * xPi);
        double gcjLng = z * Math.cos(theta);
        double gcjLat = z * Math.sin(theta);

        return new LatLng(gcjLat, gcjLng);
    }


    /**
     * 根据经纬度计算需要偏转的角度
     */
    public static float getRotate(LatLng curPos, LatLng nextPos) {
        double x1 = curPos.latitude;
        double x2 = nextPos.latitude;
        double y1 = curPos.longitude;
        double y2 = nextPos.longitude;

        return (float) (Math.atan2(y2 - y1, x2 - x1) / Math.PI * 180);
    }


    /**
     * 移动地图到坐标点
     */
    public static void animateMapStatus(BaiduMap mMap, LatLng latLng) {
        MapStatusUpdate update = MapStatusUpdateFactory.
                newLatLng(latLng);
        mMap.animateMapStatus(update);
    }


    /**
     * 移动地图到坐标点
     */
    public static void animateMapStatus(BaiduMap mMap, LatLng latLng, float zoom) {
        MapStatusUpdate update = MapStatusUpdateFactory.
                newLatLngZoom(latLng, zoom);
        mMap.animateMapStatus(update);
    }


    /**
     * 隐藏百度logo
     */
    public static void hideMapLogo(ViewGroup mMapView) {
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.GONE);
        }
    }


    /**
     * 加载markerOptions
     *
     * @param mContext 上下文对象
     * @param mStr     marker顶上文字
     * @param resId    marker图标资源ID
     * @return MarkerOptions
     */
    public static MarkerOptions initOptions(Context mContext, String mStr, int resId, int iconWidth, int iconHeight) {

        View view = View.inflate(mContext, R.layout.map_item_marker, null);

        if (!TextUtils.isEmpty(mStr)) {
            TextView tvMarker = view.findViewById(R.id.tv_marker_text);
            tvMarker.setVisibility(View.VISIBLE);
            tvMarker.setText(mStr);
        }

        ImageView imgMarker = view.findViewById(R.id.iv_marker_icon);
        imgMarker.setImageResource(resId);

        ViewGroup.LayoutParams params = imgMarker.getLayoutParams();
        params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconWidth, mContext.getResources().getDisplayMetrics());
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconHeight, mContext.getResources().getDisplayMetrics());

        imgMarker.setLayoutParams(params);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromView(view);
        return new MarkerOptions().icon(bitmapDescriptor).zIndex(9)
                .draggable(false);
    }

    public static MarkerOptions initOptions(Context mContext, int resId, int iconWidth, int iconHeight) {
        return initOptions(mContext, null, resId, iconWidth, iconHeight);
    }

    public static MarkerOptions initOptions(Context mContext, String mStr, int resId, int iconSize) {
        return initOptions(mContext, mStr, resId, iconSize, iconSize);
    }

    public static MarkerOptions initOptions(Context mContext, int resId, int iconSize) {
        return initOptions(mContext, null, resId, iconSize);
    }


    /**
     * 根据两点自适应缩放
     */
    public static void setZoomAndPoint(BaiduMap mMap, LatLng point1, LatLng point2) {

        int zoom = 10;

        double lng1 = point1.longitude;
        double lat1 = point1.latitude;

        double lng2 = point2.longitude;
        double lat2 = point2.latitude;

        double pointLng = (lng1 + lng2) / 2;
        double pointLat = (lat1 + lat2) / 2;

        LatLng point = new LatLng(pointLat, pointLng);

        int[] zooms = {50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 25000, 50000,
                100000, 200000, 500000, 1000000, 2000000};

        double distance = DistanceUtil.getDistance(point1, point2);
        NumberFormat nf = new DecimalFormat("0.00");
        double dis = Double.parseDouble(nf.format(distance));

        for (int i = 0; i < zooms.length; i++) {
            if ((zooms[i] - dis) > 0) {
                zoom = 18 - i + 2;
                break;
            }
        }

        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(point, zoom);
        mMap.animateMapStatus(update);
    }


    /**
     * @return 两点距离(公里)
     */
    public static double getDistance(LatLng point1, LatLng point2) {
        double d = DistanceUtil.getDistance(point1, point2) / 1000;
        return Double.parseDouble(new DecimalFormat("0.0").format(d));
    }
}
