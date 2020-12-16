import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'LocationPage.dart';
import 'MapPage.dart';
import 'utils/FunctionItemWidget.dart';

class HomePage extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _HomeWidget(),
    );
  }

}

class _HomeWidget extends StatefulWidget {
  @override
  __HomeWidgetState createState() => __HomeWidgetState();
}

class __HomeWidgetState extends State<_HomeWidget> {

  @override
  Widget build(BuildContext context) {
    return Container(
      child: ListView(
          children: <Widget>[
              FunctionItemWidget(
                label: '基本地位',
                sublabel: '基本定位功能的实现',
                target: LocationPage(),
              ),
              FunctionItemWidget(
                label: '创建地图',
                sublabel: '基础地图、个性化地图、TextureMapView、离线地图、室内地图以及多地图创建',
                target: MapPage(),
              ),
          ]
      ),
    );
  }

}

