class FlutterLocationEntity {
  double lat;

  double lng;

  FlutterLocationEntity({
    this.lat,
    this.lng
  });

  FlutterLocationEntity.fromJson(Map<String, dynamic> json) {
    lat = json['lat'];
    lng = json['lng'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['lat'] = this.lat;
    data['lng'] = this.lng;
    return data;
  }

}