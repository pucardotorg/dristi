import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:pucardpg/model/advocate-user-info/advocate_user_info.dart';
import 'package:pucardpg/model/auth-response/auth_response.dart';

part 'advocate_request_info.freezed.dart';
part 'advocate_request_info.g.dart';

@freezed
class AdvocateRequestInfo with _$AdvocateRequestInfo {
  const factory AdvocateRequestInfo({
    @JsonKey(name: 'apiId') String? apiId,
    @JsonKey(name: 'ver') String? ver,
    @JsonKey(name: 'ts') int? ts,
    @JsonKey(name: 'action') String? action,
    @JsonKey(name: 'did') String? did,
    @JsonKey(name: 'key') String? key,
    @JsonKey(name: 'msgId') String? msgId,
    @JsonKey(name: 'userInfo') UserRequest? userInfo,
    @JsonKey(name: 'authToken') String? authToken,
  }) = _AdvocateRequestInfo;

  factory AdvocateRequestInfo.fromJson(Map<String, dynamic> json) =>
      _$AdvocateRequestInfoFromJson(json);
}