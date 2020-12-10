import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_emergency_map/flutter_emergency_map.dart';

class MapPage extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('基本地图'),
      ),
      body: _MapWidget()
    );
  }

}


class _MapWidget extends StatefulWidget {
  @override
  __MapWidgetState createState() => __MapWidgetState();
}

class __MapWidgetState extends State<_MapWidget> {

  EmergencyMapController controller;

  void createView(EmergencyMapController emergencyMapController) {
    controller = emergencyMapController;
  }


  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          Expanded(
              child: EmergencyMapWidget(
                onViewCreated: createView,
                mapOptions: MapOptions(
                  zoomLevel: 3,
                  center: FlutterLocationEntity(lat: 35.2141988589, lng: 103.494973447)
                ),
              ),
          ),
          Row(
            children: [
              RaisedButton(
                onPressed: () {
                  controller.setMapCenter(FlutterLocationEntity(lat: 35.2141988589, lng: 103.494973447));
                },
                child: Text('设置地图中心'),
              ),
              RaisedButton(
                onPressed: () {
                  controller.setLocationShow().then((value) {
                    print('等级---$value');
                  });
                },
                child: Text('显示当前位置'),
              )
            ],
          )
        ],
      )
    );
  }

}

