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

  /// 展示地图
  Future<int> showMap({
    @required bool frontCamera,
  }) async {
    return _channel.invokeMethod('showMap', {
      "frontCamera": frontCamera,
    });
  }

  ///设置地图中心
  Future<bool> setMapCenter({
    @required FlutterLocationEntity flutterLocationEntity,
  }) async {
    // return _channel.invokeMethod('setMapCenter', {
    //   "setMapCenter": flutterLocationEntity,
    // });
    return _channel.invokeMethod('setMapCenter');
  }


}