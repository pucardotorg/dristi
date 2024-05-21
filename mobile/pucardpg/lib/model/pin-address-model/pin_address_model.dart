import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:google_places_flutter/model/place_details.dart';

part 'pin_address_model.freezed.dart';
part 'pin_address_model.g.dart';

@freezed
class PlaceDetailsList with _$PlaceDetailsList {

  const factory PlaceDetailsList({
    @JsonKey(name: 'results') required List<Result> results,
  }) = _PlaceDetailsList;

  factory PlaceDetailsList.fromJson(
      Map<String, dynamic> json) =>
      _$PlaceDetailsListFromJson(json);

}