import 'package:country_state_city/models/state.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'state_model.freezed.dart';

@freezed
class StatesData with _$StatesData {
  const factory StatesData({
    dynamic name,
    dynamic countryCode,
    dynamic isoCode,
    dynamic latitude,
    dynamic longitude
  }) = _StatesData;

  factory StatesData.fromState(State state) {
    return StatesData(
      name: state.name,
      countryCode: state.countryCode,
      isoCode: state.isoCode,
      latitude: state.latitude,
      longitude: state.longitude,
    );
  }
}