import 'package:freezed_annotation/freezed_annotation.dart';

part 'auth_response.freezed.dart';
part 'auth_response.g.dart';

@freezed
class AuthResponse with _$AuthResponse {
  const factory AuthResponse({
    required String accessToken,
    required String tokenType,
    required String refreshToken,
    required int expiresIn,
    required String scope,
    @JsonKey(name: 'ResponseInfo') required ResponseInfo responseInfo,
    @JsonKey(name: 'UserRequest') required UserRequest userRequest,
  }) = _AuthResponse;

  factory AuthResponse.fromJson(Map<String, dynamic> json) => _$AuthResponseFromJson(json);
}

@freezed
class ResponseInfo with _$ResponseInfo {
  const factory ResponseInfo({
    String? apiId, // Make nullable since it's empty in the example JSON
    String? ver,  // Make nullable
    String? ts,  // Make nullable
    String? resMsgId,  // Make nullable
    String? msgId,  // Make nullable
    required String status,
  }) = _ResponseInfo;

  factory ResponseInfo.fromJson(Map<String, dynamic> json) => _$ResponseInfoFromJson(json);
}

@freezed
class UserRequest with _$UserRequest {
  const factory UserRequest({
    required int id,
    required String uuid,
    required String userName,
    required String name,
    required String mobileNumber,
    @JsonKey(name: 'emailId') String? emailId,  // Make nullable
    @JsonKey(name: 'locale') String? locale,  // Make nullable
    required String type,
    @JsonKey(name: 'roles') required List<Role> roles,
    required bool active,
    required String tenantId,
    @JsonKey(name: 'permanentCity') String? permanentCity,  // Make nullable
  }) = _UserRequest;

  factory UserRequest.fromJson(Map<String, dynamic> json) => _$UserRequestFromJson(json);
}

@freezed
class Role with _$Role {
  const factory Role({
    required String name,
    required String code,
    required String tenantId,
  }) = _Role;

  factory Role.fromJson(Map<String, dynamic> json) => _$RoleFromJson(json);
}
