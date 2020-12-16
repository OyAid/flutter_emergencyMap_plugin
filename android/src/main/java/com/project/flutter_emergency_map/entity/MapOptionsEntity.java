package com.project.flutter_emergency_map.entity;

public class MapOptionsEntity {
    private int zoomLevel; //比例尺 1-10

    private LocationEntity center; //地图中心位置
    
    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public LocationEntity getCenter() {
        return center;
    }

    public void setCenter(LocationEntity center) {
        this.center = center;
    }
}
