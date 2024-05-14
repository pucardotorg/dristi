// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'request_info.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$RequestInfoImpl _$$RequestInfoImplFromJson(Map<String, dynamic> json) =>
    _$RequestInfoImpl(
      apiId: json['apiId'] as String? ?? "Rainmaker",
      authToken: json['authToken'] as String? ??
          "c835932f-2ad4-4d05-83d6-49e0b8c59f8a",
      msgId: json['msgId'] as String? ?? "1712987382117|en_IN",
      plainAccessRequest:
          json['plainAccessRequest'] as Map<String, dynamic>? ?? const {},
    );

Map<String, dynamic> _$$RequestInfoImplToJson(_$RequestInfoImpl instance) =>
    <String, dynamic>{
      'apiId': instance.apiId,
      'authToken': instance.authToken,
      'msgId': instance.msgId,
      'plainAccessRequest': instance.plainAccessRequest,
    };
