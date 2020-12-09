import 'dart:async';

import 'package:flutter/services.dart';

class FlutterEmergencyMap {

  static const MethodChannel _channel = const MethodChannel('flutter_emergency_map');

  ///开始定位
  Future<String> startLocation() async {
    print('插件位置');
    final String version = await _channel.invokeMethod('startLocation');
    return version;
  }

  ///停止定位
  Future<String> stopLocation() async {
    final String version = await _channel.invokeMethod('stopLocation');
    return version;
  }

}