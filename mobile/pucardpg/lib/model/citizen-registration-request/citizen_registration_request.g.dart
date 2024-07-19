// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'citizen_registration_request.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$CitizenRegistrationRequestImpl _$$CitizenRegistrationRequestImplFromJson(
        Map<String, dynamic> json) =>
    _$CitizenRegistrationRequestImpl(
      userInfo: UserInfo.fromJson(json['User'] as Map<String, dynamic>),
      requestInfo: json['RequestInfo'] == null
          ? const RequestInfo()
          : RequestInfo.fromJson(json['RequestInfo'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$$CitizenRegistrationRequestImplToJson(
        _$CitizenRegistrationRequestImpl instance) =>
    <String, dynamic>{
      'User': instance.userInfo,
      'RequestInfo': instance.requestInfo,
    };

_$UserInfoImpl _$$UserInfoImplFromJson(Map<String, dynamic> json) =>
    _$UserInfoImpl(
      name: json['name'] as String? ?? "dristi",
      username: json['username'] as String,
      otpReference: json['otpReference'] as String,
      tenantId: json['tenantId'] as String,
    );

Map<String, dynamic> _$$UserInfoImplToJson(_$UserInfoImpl instance) =>
    <String, dynamic>{
      'name': instance.name,
      'username': instance.username,
      'otpReference': instance.otpReference,
      'tenantId': instance.tenantId,
    };
