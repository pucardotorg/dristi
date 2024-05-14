// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'advocate_request_info.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$AdvocateRequestInfoImpl _$$AdvocateRequestInfoImplFromJson(
        Map<String, dynamic> json) =>
    _$AdvocateRequestInfoImpl(
      apiId: json['apiId'] as String?,
      ver: json['ver'] as String?,
      ts: json['ts'] as int?,
      action: json['action'] as String?,
      did: json['did'] as String?,
      key: json['key'] as String?,
      msgId: json['msgId'] as String?,
      userInfo: json['userInfo'] == null
          ? null
          : AdvocateUserInfo.fromJson(json['userInfo'] as Map<String, dynamic>),
      authToken: json['authToken'] as String?,
    );

Map<String, dynamic> _$$AdvocateRequestInfoImplToJson(
        _$AdvocateRequestInfoImpl instance) =>
    <String, dynamic>{
      'apiId': instance.apiId,
      'ver': instance.ver,
      'ts': instance.ts,
      'action': instance.action,
      'did': instance.did,
      'key': instance.key,
      'msgId': instance.msgId,
      'userInfo': instance.userInfo,
      'authToken': instance.authToken,
    };
