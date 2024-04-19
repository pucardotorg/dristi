import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:pucardpg/app/data/models/role-model/role.dart';

part 'auth_response.freezed.dart';
part 'auth_response.g.dart';

@freezed
class AuthResponse with _$AuthResponse {
  const factory AuthResponse({
    required String? accessToken,
    required String? tokenType,
    required String? refreshToken,
    required int? expiresIn,
    required String? scope,
    @JsonKey(name: 'ResponseInfo') ResponseInfo? responseInfo,
    @JsonKey(name: 'UserRequest') UserRequest? userRequest,
    @JsonKey(name: 'error') String? error,
    @JsonKey(name: 'error_description') String? errorDescription,
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
    String? status,
  }) = _ResponseInfo;

  factory ResponseInfo.fromJson(Map<String, dynamic> json) => _$ResponseInfoFromJson(json);
}

@freezed
class UserRequest with _$UserRequest {
  const factory UserRequest({
    int? id,
    String? uuid,
    String? userName,
    String? name,
    String? mobileNumber,
    String? emailId,  // Make nullable
    String? locale,  // Make nullable
    String? type,
    @JsonKey(name: 'roles') List<Role>? roles,
    bool? active,
    String? tenantId,
    String? permanentCity,  // Make nullable
  }) = _UserRequest;

  factory UserRequest.fromJson(Map<String, dynamic> json) => _$UserRequestFromJson(json);
}

