import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:flutter_emergency_map/entity/flutter_location_entity.dart';

/// 地图通信中心
class EmergencyMapController {

  EmergencyMapController(int id) : _channel = new MethodChannel('mapView_$id');

  // static const MethodChannel _channel = const MethodChannel('mapView_$id');

  /// 通信
  MethodChannel _channel;

  ///监听
  StreamSubscription<dynamic> _eventSubscription;


  // EmergencyMapController() {
  //   //初始化
  //   initEvent();
  // }
  //
  // initEvent() {
  //   _eventSubscription = EventChannel
  // }


  ///设置地图中心
  Future<void> setMapCenter(FlutterLocationEntity flutterLocationEntity) async {
    return await _channel.invokeMethod(
        'setMapCenter', flutterLocationEntity.toJson()
    );
  }

  ///设置地图中心
  Future<bool> showLocation() async {
    return await _channel.invokeMethod('showLocation');
  }

  ///地图上绘制点
  Future<bool> addMarkers() async {
    return _channel.invokeMethod('addMarkers');
  }

  ///地图上绘制点
  Future<bool> setMapChart() async {
    return _channel.invokeMethod('setMapChart');
  }

  /// 图层切换
  Future<bool> mapSwitch({
    @required bool is2DMap, // true：2D地图； false：影像地图。
  }) async {
    return _channel.invokeMethod('mapSwitch', {
      "is2DMap": is2DMap ?? true,
    });
  }

  /// 缩放地图到指定的缩放级别
  Future<bool> zoomToLevel({
    @required int level, // level: 等级 1-10
  }) async {
    return _channel.invokeMethod('zoomToLevel', {
      "level": level,
    });
  }

}