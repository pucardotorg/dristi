// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'loginModel.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$LoginModelImpl _$$LoginModelImplFromJson(Map<String, dynamic> json) =>
    _$LoginModelImpl(
      username: json['username'] as String?,
      password: json['password'] as String? ?? "123456",
      tenantId: json['tenantId'] as String? ?? "pg",
      userType: json['userType'] as String? ?? "citizen",
      scope: json['scope'] as String? ?? "read",
      grantType: json['grant_type'] as String? ?? "password",
      timeStamp: json['_'] as int? ?? 1713357247536,
    );

Map<String, dynamic> _$$LoginModelImplToJson(_$LoginModelImpl instance) =>
    <String, dynamic>{
      'username': instance.username,
      'password': instance.password,
      'tenantId': instance.tenantId,
      'userType': instance.userType,
      'scope': instance.scope,
      'grant_type': instance.grantType,
      '_': instance.timeStamp,
    };
