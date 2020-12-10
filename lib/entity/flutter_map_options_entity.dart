import 'flutter_location_entity.dart';

class MapOptions {
  int zoomLevel; //比例尺 1-10

  FlutterLocationEntity center; //地图中心位置

  MapOptions({
    this.zoomLevel,
    this.center
  });

  MapOptions.fromJson(Map<String, dynamic> json) {
    zoomLevel = json['zoomLevel'];
    center = json['center'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['zoomLevel'] = this.zoomLevel;
    data['center'] = this.center;
    return data;
  }

  Map<String, Object> toMap() {
    return {
      'zoomLevel': this.zoomLevel,
      'center': this.center?.toMap(),
    };
  }

}