// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'logout_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$LogoutRequestImpl _$$LogoutRequestImplFromJson(Map<String, dynamic> json) =>
    _$LogoutRequestImpl(
      accessToken: json['access_token'] as String?,
      uuid: json['uuid'] as String?,
      requestInfo:
          RequestInfo.fromJson(json['RequestInfo'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$$LogoutRequestImplToJson(_$LogoutRequestImpl instance) =>
    <String, dynamic>{
      'access_token': instance.accessToken,
      'uuid': instance.uuid,
      'RequestInfo': instance.requestInfo,
    };

_$LogoutResponseImpl _$$LogoutResponseImplFromJson(Map<String, dynamic> json) =>
    _$LogoutResponseImpl(
      responseInfo: json['responseInfo'] == null
          ? const ResponseInfoSearch(status: "")
          : ResponseInfoSearch.fromJson(
              json['responseInfo'] as Map<String, dynamic>),
      error: json['error'] == null
          ? null
          : ErrorModel.fromJson(json['error'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$$LogoutResponseImplToJson(
        _$LogoutResponseImpl instance) =>
    <String, dynamic>{
      'responseInfo': instance.responseInfo,
      'error': instance.error,
    };
