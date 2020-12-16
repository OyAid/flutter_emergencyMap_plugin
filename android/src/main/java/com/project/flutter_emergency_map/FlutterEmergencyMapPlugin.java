package com.project.flutter_emergency_map;

import android.content.Context;

import androidx.annotation.NonNull;

import com.egis.basemap.MapView;
import com.egis.map.Map;
import com.gsafety.mapsdk.core.location.GMapLocationClient;
import com.gsafety.mapsdk.core.location.listener.GMapLocationListener;
import com.gsafety.mapsdk.core.location.model.GMapLocation;
import com.gsafety.mapsdk.core.location.model.GmapLocationClientOption;
import com.project.flutter_emergency_map.utils.EWSConfig;
import com.project.flutter_emergency_map.utils.EmergencyUtils;
import com.project.flutter_emergency_map.view.EmergencyPlatformView;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformViewRegistry;

import static com.project.flutter_emergency_map.utils.MapUtils.addCvaLayer;
import static com.project.flutter_emergency_map.utils.MapUtils.addTileLayer;


/** FlutterEmergencyMapPlugin */
public class FlutterEmergencyMapPlugin implements FlutterPlugin, MethodCallHandler {

  private MethodChannel channel;

  ///定位
  GMapLocationClient gMapLocationClient;

  /**
   * 地图map
   */
  private MapView mapView;
  private Map mMap;


  /**
   * 初始化
   */
  public FlutterEmergencyMapPlugin(BinaryMessenger messenger, Context context, MethodChannel channel, PlatformViewRegistry registry) {
//    //在此初始化加快地图打开速度
//    mapView = new MapView(context);
//    mapView.setCompleteCallback(new MapView.IInitCompleteCallback() {
//      @Override
//      public void complete() {
//        mMap = mapView.getMap();
//      }
//    });

    //2，初始化定位
    gMapLocationClient = new GMapLocationClient(context, EWSConfig.SERVICE_URL,EWSConfig.AUTH_TYPE,EWSConfig.AUTH_CLIENT_ID,EWSConfig.CLIENT_SECRET,EWSConfig.TOKEN_URL);
    //3，设置监听
    gMapLocationClient.setgMapLocationListener( new GMapLocationListener() {

      @Override
      public void onLocationChanged(GMapLocation gMapLocation, int i, String s) {
        Log.e("定位监听", String.valueOf(gMapLocation));
        if(i == 1000) {
          String text = "城市："+gMapLocation.getCity()+"\n国家："+gMapLocation.getCountry()+"\n省份："+gMapLocation.getProvince()+"\n路段："+gMapLocation.getRoad()+"\n区域："+gMapLocation.getDistrict();
          Log.e("拿到定位", text);
        }
      }

    });
    //4，配置参数
    GmapLocationClientOption clientOption = new GmapLocationClientOption();
    //设置坐标系类型 默认CGCS2000
    clientOption.setCoorType("CGCS2000");
    //是否仅一次定位 默认true
    clientOption.setOnceLocation(true);
    //是否需要地址信息 默认false
    clientOption.setNeedAddress(true);
    //定位间隔时间，oncelocation = false 才有效
    clientOption.setInterval(5000);
    gMapLocationClient.setGmapLocationClientOption(clientOption);

    //注册地图view
    registry.registerViewFactory(EmergencyPlatformView.SIGN, new EmergencyPlatformView(context, messenger));

  }

  public FlutterEmergencyMapPlugin() {

  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_emergency_map");
    channel.setMethodCallHandler(new FlutterEmergencyMapPlugin(flutterPluginBinding.getBinaryMessenger(), flutterPluginBinding.getApplicationContext(), channel, flutterPluginBinding.getPlatformViewRegistry()));
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(this);
  }

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_emergency_map");
    channel.setMethodCallHandler(new FlutterEmergencyMapPlugin(registrar.messenger(), registrar.context(), channel, registrar.platformViewRegistry()));
  }


  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    switch (call.method) {
      case "startLocation":
        this.startLocation(call, result);
        break;
      case "stopLocation":
        this.stopLocation(call, result);
        break;
      default:
        result.notImplemented();
    }
  }


  /**
   * 开始定位
   */
  private void startLocation(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
    Log.e("测试", "进入原生方法");
    //5，开启定位
    gMapLocationClient.startLocation();
//    gMapLocationClient.setgMapLocationListener( new GMapLocationListener() {
//
//      @Override
//      public void onLocationChanged(GMapLocation gMapLocation, int i, String s) {
//        Log.e("定位监听", String.valueOf(gMapLocation));
//      }
//
//    });
  }

  /**
   * 停止定位
   */
  private void stopLocation(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
    Log.e("停止定位", "停止定位");
    gMapLocationClient.stopLocation();
  }
  
}
