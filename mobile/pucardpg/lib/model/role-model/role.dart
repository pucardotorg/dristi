import 'package:freezed_annotation/freezed_annotation.dart';

part 'role.freezed.dart';
part 'role.g.dart';

@freezed
class Role with _$Role {
  const factory Role({
    required String? name,
    required String? code,
    @Default(null) String? description,
    required String tenantId,
  }) = _Role;

  factory Role.fromJson(Map<String, dynamic> json) => _$RoleFromJson(json);
}