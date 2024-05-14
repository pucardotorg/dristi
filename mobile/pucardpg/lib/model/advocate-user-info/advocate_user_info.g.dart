// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'advocate_user_info.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$AdvocateUserInfoImpl _$$AdvocateUserInfoImplFromJson(
        Map<String, dynamic> json) =>
    _$AdvocateUserInfoImpl(
      type: json['type'] as String?,
      tenantId: json['tenantId'] as String?,
      roles: (json['roles'] as List<dynamic>?)
          ?.map((e) => Role.fromJson(e as Map<String, dynamic>))
          .toList(),
      uuid: json['uuid'] as String?,
    );

Map<String, dynamic> _$$AdvocateUserInfoImplToJson(
        _$AdvocateUserInfoImpl instance) =>
    <String, dynamic>{
      'type': instance.type,
      'tenantId': instance.tenantId,
      'roles': instance.roles,
      'uuid': instance.uuid,
    };
