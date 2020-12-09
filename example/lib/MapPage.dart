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
    controller.showMap(frontCamera: true);
  }


  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          Expanded(
              child: EmergencyMapWidget(
                onViewCreated: createView,
              ),
          ),
          RaisedButton(
              onPressed: () {
                controller.setMapCenter(flutterLocationEntity: FlutterLocationEntity());
              },
              child: Text('设置中心'),
          )
        ],
      )
    );
  }

}

