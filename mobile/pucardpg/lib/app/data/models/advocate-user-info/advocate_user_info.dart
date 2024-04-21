import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:pucardpg/app/data/models/role-model/role.dart';

part 'advocate_user_info.freezed.dart';
part 'advocate_user_info.g.dart';

@freezed
class AdvocateUserInfo with _$AdvocateUserInfo {
  const factory AdvocateUserInfo({
    @JsonKey(name: 'type') String? type,
    @JsonKey(name: 'tenantId') String? tenantId,
    @JsonKey(name: 'roles') List<Role>? roles,
    @JsonKey(name: 'uuid') String? uuid,
  }) = _AdvocateUserInfo;

  factory AdvocateUserInfo.fromJson(Map<String, dynamic> json) =>
      _$AdvocateUserInfoFromJson(json);
}