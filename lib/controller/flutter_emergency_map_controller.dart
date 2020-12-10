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

  ///显示当前定位
  Future<dynamic> setLocationShow() async {
    return _channel.invokeMethod('setLocationShow');
  }

  ///地图上绘制点
  Future<bool> addPointList() async {
    return _channel.invokeMethod('addPointList');
  }


}