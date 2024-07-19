import 'package:freezed_annotation/freezed_annotation.dart';

part 'loginModel.freezed.dart';

part 'loginModel.g.dart';

@freezed
class LoginModel with _$LoginModel {
  const factory LoginModel({
    required String? username,
    @Default("123456") String? password,
    required String? tenantId,
    @Default("citizen") String? userType,
    @Default("read") String? scope,
    @JsonKey(name: "refresh_token") String? refreshToken,
    @JsonKey(name: 'grant_type') String? grantType,
    @JsonKey(name: '_') @Default(1713357247536) int timeStamp
  }) = _LoginModel;

  factory LoginModel.fromJson(Map<String, Object?> json) =>
      _$LoginModelFromJson(json);
}
