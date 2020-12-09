package com.project.flutter_emergency_map.utils;

import com.egis.basemap.MapView;
import com.egis.draw.DrawTool;
import com.egis.gdm.CommandManager;
import com.egis.geom.Envelope;
import com.egis.layer.CanvasLayer;
import com.egis.layer.ElementLayer;
import com.egis.layer.FeatureLayer;
import com.egis.layer.TileLayer;
import com.egis.layer.source.WMTSSource;
import com.egis.map.Map;

public class MapUtils {

    public static final String BASE_URL = "http://t0.tianditu.gov.cn/";
    public static final String TRAFFIC_URL = "http://10.18.17.84:9090/service/api/egis/base/v1";
    public static final String LAYER_ID_TILE = "tileLayer";
    public static final String LAYER_ID_IMG = "imageLayer";
    public static final String LAYER_ID_CVA = "cvaLayer";
    public static final String LAYER_ID_PLOT = "plotLayer";
    public static final String LAYER_ID_FEATURE = "featureLayer";
    public static final String LAYER_ID_ELEMENT = "elementLayer";
    public static final String LAYER_ID_TRANSITION_ELEMENT = "transitionElementLayer";
    public static final String LAYER_ID_VECTOR = "vectorTileLayer";
    public static final String LAYER_ID_EWS = "ewsLayer";
    public static final String LAYER_ID_TRAFFIC = "trafficLayer";

    /**
     * 添加矢量瓦片图层
     * @param map
     */
    public static void addTileLayer(Map map) {
        TileLayer layer = new TileLayer(LAYER_ID_TILE, LAYER_ID_TILE);
        WMTSSource source = new WMTSSource();
        source.setLayers("vec");
        source.setMatrix(21);
        source.setMatrixSet("c");
        source.setMatrixPrefix("");
        source.setProjection("EPSG:4490");
        source.setFormat("tiles");
        source.setUrl(BASE_URL + "vec_c/wmts?tk=4f62e1d82bd46e2ff470b291c2260156");
        source.setExtent(new Envelope(-180,-90,180,90));
        layer.setSource(source);
        map.addLayer(layer);
    }

    /**
     * 添加影像图层
     * @param map
     */
    public static void addImageLayer(Map map) {
        TileLayer imageLayer = new TileLayer(LAYER_ID_IMG, LAYER_ID_IMG);
        WMTSSource imageSource = new WMTSSource();
        imageSource.setLayers("img");
        imageSource.setMatrix(21);
        imageSource.setMatrixSet("c");
        imageSource.setMatrixPrefix("");
        imageSource.setProjection("EPSG:4490");
        imageSource.setFormat("tiles");
        imageSource.setUrl(BASE_URL + "img_c/wmts?tk=4f62e1d82bd46e2ff470b291c2260156");
        imageSource.setExtent(new Envelope(-180,-90,180,90));
        imageLayer.setSource(imageSource);
        map.addLayer(imageLayer);
    }

    /**
     * 添加注记图层，注记图层一般加在最顶层
     */
    public static void addCvaLayer(Map map){
        TileLayer layerCva = new TileLayer(LAYER_ID_CVA, LAYER_ID_CVA);
        WMTSSource sourceCva = new WMTSSource();
        sourceCva.setLayers("cva");
        sourceCva.setMatrix(21);
        sourceCva.setMatrixSet("c");
        sourceCva.setMatrixPrefix("");
        sourceCva.setProjection("EPSG:4490");
        sourceCva.setTileSize(256);
        sourceCva.setFormat("tiles");
        sourceCva.setUrl(BASE_URL + "cva_c/wmts?tk=4f62e1d82bd46e2ff470b291c2260156");
        sourceCva.setExtent(new Envelope(-180,-90,180,90));
        layerCva.setSource(sourceCva);
        map.addLayer(layerCva);
    }


    public static void createPlotLayer(Map map) {
        CanvasLayer plotLayer = new CanvasLayer(LAYER_ID_PLOT, LAYER_ID_PLOT);
        map.addLayer(plotLayer);
    }

    public static void createElementLayer(Map map) {
        ElementLayer layer = new ElementLayer(LAYER_ID_ELEMENT, LAYER_ID_ELEMENT);
        map.addLayer(layer);
    }

    public static void createTransitionElementLayer(Map map) {
        ElementLayer layer = new ElementLayer(LAYER_ID_TRANSITION_ELEMENT, LAYER_ID_TRANSITION_ELEMENT);
        map.addLayer(layer);
    }

    //构建分页查询结果要素图层
    public static void createFeatureLayer(Map map) {
        FeatureLayer layer = new FeatureLayer(LAYER_ID_FEATURE, LAYER_ID_FEATURE);
        layer.setVisible(true);
        layer.setOpacity(1);
        map.addLayer(layer);
    }

    //构建实时路况图层
    public static void createTrafficLayer(Map map) {
//        TrafficLayer layer = new TrafficLayer(LAYER_ID_TRAFFIC,LAYER_ID_TRAFFIC,TRAFFIC_URL, EWSConfig.AUTH_CLIENT_ID,EWSConfig.CLIENT_SECRET,1);
//        map.addLayer(layer);
    }

    //执行命令
    public static void executeCommand(MapView mapView, String tag) {
        ElementLayer layer = (ElementLayer) mapView.getMap().findLayer(LAYER_ID_ELEMENT);
        CommandManager manager = mapView.getCommandManager();
        DrawTool drawTool = (DrawTool)manager.getCommand(tag);
        drawTool.onClick(null);
    }

    public static void addEWSLayer(Map map){
        TileLayer layerEWS = new TileLayer(LAYER_ID_EWS, LAYER_ID_EWS);
        WMTSSource source = new WMTSSource();
        source.setExtent(new Envelope(-180, -90, 180, 90));
        source.setLayers("wmts_world_vct_4326");
        source.setMatrix(17);
        source.setMatrixSet("wmts_hubei_4326");
        source.setMatrixPrefix("wmts_hubei_4326:");
        source.setProjection("EPSG:4326");
        source.setTileSize(256);
        source.setUrl("http://172.17.38.105:8081/wmts");
        layerEWS.setSource(source);
        map.addLayer(layerEWS);
    }
}
