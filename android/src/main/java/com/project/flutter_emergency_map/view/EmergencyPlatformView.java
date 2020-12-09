package com.project.flutter_emergency_map.view;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.egis.basemap.MapParams;
import com.egis.basemap.MapView;
import com.egis.geom.Point;
import com.egis.map.Map;
import com.egis.map.scalecontrol.ScaleControlOption;
import com.project.flutter_emergency_map.utils.EmergencyUtils;

import java.util.HashMap;

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

    /**
     * 初始化工厂信息，此处的域是 PlatformViewFactory
     */
    public EmergencyPlatformView(Context context, BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
        Log.e("初始化工厂信息", "初始化工厂信息");
        mapView = new MapView(context);
        params = new MapParams();
        ScaleControlOption option = new ScaleControlOption();
        option.setRight("60px");
//        option.setBackground("rgba(255,26,40,0)");
//        option.setBackground("rgba(255,255,255,0.5)");
//        params.setScaleControlOption(option);
//        params.setShowScaleControl(true);  //是否显示比例尺
        params.setMaxZoom(100);
//        params.setDefaultCenter(new Point(103.494973447, 35.2141988589));
        mapView.setDefaultParams(params);
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
        return view;
    }


    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "showMap":
                this.startMapView(call, result);
                break;
            case "setMapCenter":
                this.setMapCenter(call, result);
                break;
            default:
                result.notImplemented();
        }
    }

    /**
     * 开启地图
     */
    private void startMapView(@NonNull MethodCall call, @NonNull final MethodChannel.Result result) {
//        mapView.setCompleteCallback(new MapView.IInitCompleteCallback() {
//            @Override
//            public void complete() {
//                mMap = mapView.getMap();
//                result.success('d');
//            }
//        });
    }

    /**
     * 设置地图中心
     */
    private void setMapCenter(@NonNull MethodCall call, @NonNull final MethodChannel.Result result) {
//        HashMap<String, Double> map = new HashMap<>();
        //给map中添加元素
//        map.put("星期一", "Monday");
//        map = EmergencyUtils.getParam(call, result, "setMapCenter");
//        Point point = new Point(map["lat"], map["lng"]);
//        double latitude = map["lat"];  //y
//        double longitude = map["lng"];  //x

//        params.setDefaultCenter(point);
        Log.e("设置地图中心", "设置地图中心");
        params.setDefaultCenter(new Point(103.494973447, 35.2141988589));
        result.success(true);
    }

}
