package com.project.flutter_emergency_map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.egis.basemap.MapParams;
import com.egis.basemap.MapView;
import com.egis.display.element.Element;
import com.egis.display.element.ElementBase;
import com.egis.draw.DrawTool;
import com.egis.entity.core.Field;
import com.egis.entity.core.Fields;
import com.egis.entity.def.GFeature;
import com.egis.entity.property.EntityPropertyTypes;
import com.egis.gdm.CommandManager;
import com.egis.gdm.CommandNotify;
import com.egis.gdm.Container;
import com.egis.geom.Point;
import com.egis.interact.NoopTool;
import com.egis.layer.CanvasLayer;
import com.egis.layer.ElementLayer;
import com.egis.layer.FeatureLayer;
import com.egis.layer.Graphic;
import com.egis.map.IClickEventCallbacks;
import com.egis.map.Map;
import com.egis.map.scalecontrol.ScaleControlOption;
import com.egis.plot.comm.PlotTool;
import com.egis.plot.plotalgorithm.PlotAlgorithm;
import com.egis.plot.plotalgorithm.PointAlgorithm;
import com.egis.render.ChartRenderer;
import com.egis.render.SimpleRenderer;
import com.egis.stat.BarChart;
import com.egis.symbol.Color;
import com.egis.symbol.PictureMarkerSymbol;
import com.egis.symbol.SimpleMarkerSymbol;
import com.egis.symbol.Symbol;
import com.google.gson.Gson;
import com.gsafety.mapsdk.model.LatLng;
import com.project.flutter_emergency_map.R;
import com.project.flutter_emergency_map.utils.EmergencyUtils;

import java.util.ArrayList;
import java.util.List;

import io.flutter.Log;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;
import rx.functions.Function;

import static com.project.flutter_emergency_map.utils.MapUtils.BASE64_HEAFER;
import static com.project.flutter_emergency_map.utils.MapUtils.LAYER_ID_ELEMENT;
import static com.project.flutter_emergency_map.utils.MapUtils.LAYER_ID_PLOT;
import static com.project.flutter_emergency_map.utils.MapUtils.addCvaLayer;
import static com.project.flutter_emergency_map.utils.MapUtils.addFeatureLayer;
import static com.project.flutter_emergency_map.utils.MapUtils.addImageLayer;
import static com.project.flutter_emergency_map.utils.MapUtils.addTileLayer;
import static com.project.flutter_emergency_map.utils.MapUtils.createElementLayer;
import static com.project.flutter_emergency_map.utils.MapUtils.createPlotLayer;

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
    private ElementLayer mLayer; //点
    private CanvasLayer layer;  //图像

    /**
     * 显示当前定位
     */
    private ElementLayer elementLayer;
    private Element element;
    private PictureMarkerSymbol pictureMarkerSymbol;

    /**
     * 地图的参数配置
     */
    private MapParams params;

    /**
     * 消息器
     */
    private BinaryMessenger messenger;

    private int zoomLevel = 3; //widget中初始化的比例尺

    private Context context;

    /**
     * 初始化工厂信息，此处的域是 PlatformViewFactory
     */
    public EmergencyPlatformView(Context context, BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.context = context;
        this.messenger = messenger;
        Log.e("初始化工厂信息View", "初始化工厂信息View");
        mapView = new MapView(context);
        params = new MapParams();
        mLayer = new ElementLayer();
        params.setDefaultLevel(zoomLevel);  //地图显示等级
        ScaleControlOption option = new ScaleControlOption();
        option.setBackground("rgba(0, 0, 0, 0.2)");
        params.setScaleControlOption(option);
        params.setShowScaleControl(true);  //是否显示比例尺
        mapView.setDefaultParams(params);
        mapView.setLocationShow(false);    //是否显示定位按钮
        mapView.setCompleteCallback(new MapView.IInitCompleteCallback() {
            @Override
            public void complete() {
                mMap = mapView.getMap();
                Log.e("拿到地图", "拿到地图信息");

                addTileLayer(mMap);   // 添加栅格瓦片图层
                addCvaLayer(mMap);    // 添加注记图层

            }
        });
    }


    @Override
    public View getView() {
        return mapView;
    }

    @Override
    public void dispose() {
        mapView.destroy();
        mMap.destroy();
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

            Log.e("map-zoomLevel", String.valueOf(this.zoomLevel));
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
            case "zoomToLevel":
                this.zoomToLevel(call, result);
                break;
            case "addMarkers":
                this.addMarkers(call, result);
                break;            
            case "mapSwitch":
                this.mapSwitch(call, result);
                break;            
            case "setMapChart":
                this.setMapChart(call, result);
                break;
            case "showLocation":
                this.showLocation(call, result);
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
        mMap.setCenter(new Point(lng, lat));
        result.success(true);
    }

    /**
     * 缩放地图到指定的缩放级别
     */
    private void zoomToLevel(@NonNull MethodCall call, @NonNull final MethodChannel.Result result) {
        int level = EmergencyUtils.getParam(call, result, "level");
        mMap.zoomTo(level);
        result.success(true);
    }

    /**
     * 图层切换
     */
    private void mapSwitch(@NonNull MethodCall call, @NonNull final MethodChannel.Result result) {
        boolean is2DMap = EmergencyUtils.getParam(call, result, "is2DMap");  // true：2D地图； false：影像地图。
        if (is2DMap) {
            addTileLayer(mMap);
            addCvaLayer(mMap);
            mMap.findLayer("imageLayer").setVisible(false);
            mMap.findLayer("tileLayer").setVisible(true);
        } else {
            addImageLayer(mMap);
            addCvaLayer(mMap);
            mMap.findLayer("imageLayer").setVisible(true);
            mMap.findLayer("tileLayer").setVisible(false);
        }
        result.success(true);
    }

    /**
     * 地图上绘制点
     */
    private void addMarkers(@NonNull MethodCall call, @NonNull final MethodChannel.Result result) {
        Log.e("地图上绘制点", "地图上绘制点");

        createPlotLayer(mMap);
        layer = (CanvasLayer) mMap.findLayer(LAYER_ID_PLOT);
        //创建一个命令管理实例，用来管理所有命令
        CommandManager commandManager = mapView.getCommandManager();
        //创建命令通知实例，传入命令管理对象
        CommandNotify commandNotify = new CommandNotify(commandManager);
        //创建一个空的点元素对象
        Point point = new Point();
        point.setSrid(4490);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol();
        pictureMarkerSymbol.setWidth(64);
        pictureMarkerSymbol.setHeight(64);
//        String src = BASE64_HEAFER + EncodeUtils.base64Encode2String(ImageUtils.bitmap2Bytes(((BitmapDrawable) ContextCompat.getDrawable(context, )).getBitmap(), Bitmap.CompressFormat.PNG));
        String src = BASE64_HEAFER + EncodeUtils.base64Encode2String(ImageUtils.bitmap2Bytes(((BitmapDrawable) ContextCompat.getDrawable(context, Integer.parseInt("../images/location.png"))).getBitmap(), Bitmap.CompressFormat.PNG));
//        String src = BASE64_HEAFER;
        pictureMarkerSymbol.setSource(src);
        Element picElement = new Element();
        picElement.setGeometry(point);
        picElement.setSymbol(pictureMarkerSymbol);
        //创建图片标绘命令
        PlotAlgorithm pointAlgorithm = new PointAlgorithm();
        PlotTool picturetPlot = new PlotTool("picturetPlot","图片",null,picElement,new Element[]{picElement}, pointAlgorithm,layer);
        commandManager.add(picturetPlot);
        Container container = mapView.getContainer();
        commandManager.onCreate(container);
        //激活图片标绘命令
        commandNotify.activeCommand("picturetPlot",picturetPlot);

        result.success(true);
    }
    

    /**
     * 显示当前定位
     */
    private void showLocation(@NonNull MethodCall call, @NonNull final MethodChannel.Result result) {
        Log.e("显示当前定位", "显示当前定位");
        createElementLayer(mMap);
        elementLayer = (ElementLayer) mMap.findLayer(LAYER_ID_ELEMENT);
        if(layer == null)return;
        layer.clear();
        element = new Element();
        Point point = new Point(118.76741,32.041546);   //lng:  经    lat: 纬
        element.setGeometry(point);
        element.setSymbol(getSymbol());
        // 将点元素对象添加到元素图层中
        layer.add(element);
        mMap.setCenter(point);
        result.success(true);
    }


    /**
     * 创建点元素对象需要用到的符号
     *
     * @return
     */
    private PictureMarkerSymbol getSymbol() {
        pictureMarkerSymbol = new PictureMarkerSymbol();
        //设置宽高
        pictureMarkerSymbol.setWidth(50);
        pictureMarkerSymbol.setHeight(50);
        //设置偏移量
        pictureMarkerSymbol.setOffsetX(25);
        pictureMarkerSymbol.setOffsetY(25);
        //设置图标
        String src = BASE64_HEAFER + EncodeUtils.base64Encode2String(ImageUtils.bitmap2Bytes(((BitmapDrawable) ContextCompat.getDrawable(context, R.mipmap.navi_guide_turn)).getBitmap(), Bitmap.CompressFormat.PNG));
        pictureMarkerSymbol.setSource(src);
        return pictureMarkerSymbol;
    }

    /**
     * 地图上绘制图表
     */
    private void setMapChart(@NonNull MethodCall call, @NonNull final MethodChannel.Result result) {
        Log.e("地图上绘制图表", "地图上绘制图表");

        FeatureLayer featureLayer = new FeatureLayer("featureLayer", "featureLayer");
        mMap.addLayer(featureLayer);
        Point point = null;

        Fields fields = createFields();
        GFeature feature = new GFeature(fields);
        point = new Point(116.3f, 39.8f, 0);
        feature.setGeometry(point);
        feature.setValue("red", 31);
        feature.setValue("orange", 21);
        feature.setValue("yellow", 15);
        feature.setValue("blue", 10);
        featureLayer.add(feature);

        feature = new GFeature(fields);
        point = new Point(116.5f, 40f, 0);
        feature.setGeometry(point);
        feature.setValue("red", 21);
        feature.setValue("orange", 51);
        feature.setValue("yellow", 55);
        feature.setValue("blue", 60);
        featureLayer.add(feature);

        feature = new GFeature(fields);
        point = new Point(116.4f, 40.1f, 0);
        feature.setGeometry(point);
        feature.setValue("red", 11);
        feature.setValue("orange", 51);
        feature.setValue("yellow", 65);
        feature.setValue("blue", 30);
        featureLayer.add(feature);

        List<Color> colors = new ArrayList<Color>();
        colors.add(new Color((short) 100, (short) 100, (short) 0));
        colors.add(new Color((short) 200, (short) 100, (short) 0));
        colors.add(new Color((short) 200, (short) 0, (short) 100));
        colors.add(new Color((short) 0, (short) 100, (short) 100));

        BarChart barChart = new BarChart();
        barChart.setBarBorderColor(new Color((short) 200, (short) 100, (short) 0));
        barChart.setLabel("bar");
        barChart.setColors(colors);
        barChart.setFields(fields);
        barChart.setName("季度总量");

        ChartRenderer chartRenderer = new ChartRenderer(mMap, barChart);
        featureLayer.render(chartRenderer);

        result.success(true);
    }
    

    private Fields createFields() {
        Fields fields = new Fields();
        Field field = new Field();
        field.setDataType(EntityPropertyTypes.Int);
        field.setName("red");
        field.setAlias("一季度");
        fields.add(field);
        field = new Field();
        field.setDataType(EntityPropertyTypes.Int);
        field.setName("orange");
        field.setAlias("二季度");
        fields.add(field);
        field = new Field();
        field.setDataType(EntityPropertyTypes.Int);
        field.setName("yellow");
        field.setAlias("三季度");
        fields.add(field);
        field = new Field();
        field.setDataType(EntityPropertyTypes.Int);
        field.setName("blue");
        field.setAlias("四季度");
        fields.add(field);
        field = new Field();
        field.setDataType(EntityPropertyTypes.Point);
        field.setName("shape");
        field.setAlias("");
        fields.add(field);
        return fields;
    }



}
