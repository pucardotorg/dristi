import 'package:freezed_annotation/freezed_annotation.dart';

part 'request_info.freezed.dart';
part 'request_info.g.dart';

@freezed
class RequestInfo with _$RequestInfo {

  const factory RequestInfo({
    @JsonKey(name: 'apiId') @Default("Rainmaker") String apiId,
    @JsonKey(name: 'authToken') @Default("c835932f-2ad4-4d05-83d6-49e0b8c59f8a") String authToken,
    @JsonKey(name: 'msgId') @Default("1712987382117|en_IN") String msgId,
    @JsonKey(name: 'plainAccessRequest') @Default({}) Map<String, dynamic> plainAccessRequest
  }) = _RequestInfo;

  factory RequestInfo.fromJson(
      Map<String, dynamic> json) =>
      _$RequestInfoFromJson(json);

}
