// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'registration_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$RegistrationModelImpl _$$RegistrationModelImplFromJson(
        Map<String, dynamic> json) =>
    _$RegistrationModelImpl(
      name: json['name'] as String?,
      username: json['username'] as String?,
      otpReference: json['otpReference'] as String?,
      tenantId: json['tenantId'] as String?,
    );

Map<String, dynamic> _$$RegistrationModelImplToJson(
        _$RegistrationModelImpl instance) =>
    <String, dynamic>{
      'name': instance.name,
      'username': instance.username,
      'otpReference': instance.otpReference,
      'tenantId': instance.tenantId,
    };
