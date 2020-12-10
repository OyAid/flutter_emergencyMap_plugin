package com.project.flutter_emergency_map.view;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.egis.basemap.MapParams;
import com.egis.basemap.MapView;
import com.egis.geom.Point;
import com.egis.map.Map;
import com.egis.map.scalecontrol.ScaleControlOption;
import com.google.gson.Gson;
import com.project.flutter_emergency_map.utils.EmergencyUtils;

import io.flutter.Log;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

import static com.project.flutter_emergency_map.utils.MapUtils.addCvaLayer;
import static com.project.flutter_emergency_map.utils.MapUtils.addTileLayer;

//public class EmergencyPlatformView extends PlatformViewFactory {
//
//    /**
//     * 全局标识
//     */
//    public static final String SIGN = "mapView";
//
//    /**
//     * 消息器
//     */
//    private BinaryMessenger messenger;
//
//    private MapView mapView;
//
//    public EmergencyPlatformView(MessageCodec<Object> createArgsCodec, MapView mapView) {
//        super(createArgsCodec);
//        this.mapView = mapView;
//    }
//
//    public EmergencyPlatformView(BinaryMessenger messenger, MapView mapView) {
//        super(StandardMessageCodec.INSTANCE);
//        this.mapView = mapView;
//    }
//
//    @Override
//    public PlatformView create(Context context, int viewId, Object args) {
//        return new PlatformView() {
//            @Override
//            public View getView() {
//                return mapView;
//            }
//
//            @Override
//            public void dispose() {
//
//            }
//        };
//    }
//
//}

public class EmergencyPlatformView extends PlatformViewFactory implements PlatformView, MethodChannel.MethodCallHandler {

    /**
     * 全局标识
     */
    public static final String SIGN = "mapView";

    /**
     * 地图map
     */
    private MapView mapView;
    private Map mMap;

    /**
     * 地图的参数配置
     */
    private MapParams params;

    /**
     * 消息器
     */
    private BinaryMessenger messenger;

    private int zoomLevel = 3; //widget中初始化的比例尺
    
    private Point point;  //widget中初始化的地图中心位置

    /**
     * 初始化工厂信息，此处的域是 PlatformViewFactory
     */
    public EmergencyPlatformView(Context context, BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
        Log.e("初始化工厂信息", "初始化工厂信息");
        mapView = new MapView(context);
        params = new MapParams();
        params.setDefaultLevel(zoomLevel);  //
        ScaleControlOption option = new ScaleControlOption();
        option.setRight("60px");
//        params.setScaleControlOption(option);
//        params.setShowScaleControl(true);  //是否显示比例尺
        mapView.setDefaultParams(params);
        mapView.setLocationShow(true);
        mapView.setCompleteCallback(new MapView.IInitCompleteCallback() {
            @Override
            public void complete() {
                mMap = mapView.getMap();
                Log.e("拿到地图", "拿到地图信息");
                addTileLayer(mMap);
                addCvaLayer(mMap);
            }
        });
    }


    @Override
    public View getView() {
        return mapView;
    }

    @Override
    public void dispose() {

    }

    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        EmergencyPlatformView view = new EmergencyPlatformView(context, messenger);
        new MethodChannel(messenger, SIGN + "_" + viewId).setMethodCallHandler(view);
        this.initValue(args);
        return view;
    }

    /**
     * 获取widget中初始化的值
     */
    public void initValue(Object args) {
        if (args != null) {
            Log.e("create", String.valueOf(args));
            Gson gson = new Gson();
            String json = gson.toJson(args);
            java.util.Map mapType = JSON.parseObject(json, java.util.Map.class);
            zoomLevel = (int) mapType.get("zoomLevel");
//            point = new Point(12, 15);

            Log.e("map", String.valueOf(mapType.get("center")));
            Log.e("map", String.valueOf(mapType.get("zoomLevel")));
        }
    }


    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "setMapCenter":
                this.setMapCenter(call, result);
                break;            
            case "setLocationShow":
                this.setLocationShow(call, result);
                break;
            case "addPointList":
                this.addPointList(call, result);
                break;
            default:
                result.notImplemented();
        }
    }
    
    /**
     * 设置地图中心
     */
    private void setMapCenter(@NonNull MethodCall call, @NonNull final MethodChannel.Result result) {
        double lat = EmergencyUtils.getParam(call, result, "lat");  //y
        double lng = EmergencyUtils.getParam(call, result, "lng");  //x
        Log.e("设置地图中心参数-y", String.valueOf(lat));
        Log.e("设置地图中心参数-x", String.valueOf(lng));
        mMap.setCenter(new Point(lng, lat));
        params.setDefaultLevel(3);  //
        mapView.setDefaultParams(params);
        result.success(true);
    }

    /**
     * 显示当前定位
     */
    private void setLocationShow(@NonNull MethodCall call, @NonNull final MethodChannel.Result result) {
        Log.e("显示当前位置", "显示当前位置");
        mapView.setLocationShow(true);
        result.success(mMap.getZoomLevel());
    }

    /**
     * 地图上绘制点
     */
    private void addPointList(@NonNull MethodCall call, @NonNull final MethodChannel.Result result) {
        Log.e("地图上绘制点", "地图上绘制点");

        result.success(true);
    }
}
