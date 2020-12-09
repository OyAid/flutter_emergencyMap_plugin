import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_emergency_map/flutter_emergency_map.dart';

class LocationPage extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('应急地图'),
      ),
      body: _LocationWidget(),
    );
  }

}

class _LocationWidget extends StatefulWidget {
  @override
  __LocationWidgetState createState() => __LocationWidgetState();
}

class __LocationWidgetState extends State<_LocationWidget> {

  FlutterEmergencyMap flutterEmergencyMap = FlutterEmergencyMap();

  ///开始定位
  void onStartLocation() {
    flutterEmergencyMap.startLocation().then((value) {
      print('定位数据---$value');
    });
  }

  ///停止定位
  void onStopLocation() {
    flutterEmergencyMap.stopLocation().then((value) {
      print('定位数据---$value');
    });
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            RaisedButton(
              onPressed: onStartLocation,
              child: Text('开始定位'),
            ),
            RaisedButton(
              onPressed: onStopLocation,
              child: Text('停止定位'),
            ),
          ],
        )
      ],
    );
  }

}

