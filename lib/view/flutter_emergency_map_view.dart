import 'package:flutter/cupertino.dart';
import 'package:flutter_emergency_map/controller/flutter_emergency_map_controller.dart';

/// 应急地图Widget
class EmergencyMapWidget extends StatefulWidget {

  /// 创建事件
  final ValueChanged<EmergencyMapController> onViewCreated;

  EmergencyMapWidget({this.onViewCreated});

  @override
  _EmergencyMapWidgetState createState() => _EmergencyMapWidgetState();

}

class _EmergencyMapWidgetState extends State<EmergencyMapWidget> {

  /// 唯一标识符
  static const String type = "mapView";

  @override
  Widget build(BuildContext context) {
    return AndroidView(
      viewType: type,
      onPlatformViewCreated: onPlatformViewCreated,
      // hitTestBehavior: widget.hitTestBehavior, //渗透点击事件
      // layoutDirection: widget.layoutDirection, //嵌入视图文本方向
      // creationParams: widget.mapOptions.toMap() as dynamic, //向视图传递参数
      // creationParamsCodec: new StandardMessageCodec(), //编解码器类型
    );
  }

  /// 创建事件
  void onPlatformViewCreated(int id) {
    if (widget.onViewCreated != null) {
      widget.onViewCreated(EmergencyMapController(id));
    }
  }


  @override
  void didChangeDependencies() {
    print('didChangeDependencies');
    super.didChangeDependencies();
  }

  @override
  void dispose() {
    print('implement dispose');
    super.dispose();
  }

  @override
  void didUpdateWidget(EmergencyMapWidget emergencyMapWidget) {
    print('didUpdateWidget');
    super.didUpdateWidget(emergencyMapWidget);
  }

  @override
  void reassemble() {
    print('reassemble');
    super.reassemble();
  }

}