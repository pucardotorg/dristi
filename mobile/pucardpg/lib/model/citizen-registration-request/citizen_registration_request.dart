import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:pucardpg/model/request-info-model/request_info.dart';

part 'citizen_registration_request.freezed.dart';
part 'citizen_registration_request.g.dart';

@freezed
class CitizenRegistrationRequest with _$CitizenRegistrationRequest {
  const factory CitizenRegistrationRequest({
    @JsonKey(name: 'User') required UserInfo userInfo,
    @JsonKey(name: 'RequestInfo') @Default(RequestInfo()) RequestInfo requestInfo,

  }) = _CitizenRegistrationRequest;

  factory CitizenRegistrationRequest.fromJson(Map<String, dynamic> json) => _$CitizenRegistrationRequestFromJson(json);
}

@freezed
class UserInfo with _$UserInfo {
  const factory UserInfo({
    @JsonKey(name: 'name') @Default("dristi") String name,
    @JsonKey(name: 'username') required String username,
    @JsonKey(name: 'otpReference') required String otpReference,
    @JsonKey(name: 'tenantId') required String tenantId,
  }) = _UserInfo;

  factory UserInfo.fromJson(Map<String, dynamic> json) => _$UserInfoFromJson(json);
}