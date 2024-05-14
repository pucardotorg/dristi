// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'error_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$ErrorModelImpl _$$ErrorModelImplFromJson(Map<String, dynamic> json) =>
    _$ErrorModelImpl(
      accessToken: json['code'] as String?,
      message: json['message'] as String?,
      description: json['description'] as String?,
      fields: json['fields'] as String?,
    );

Map<String, dynamic> _$$ErrorModelImplToJson(_$ErrorModelImpl instance) =>
    <String, dynamic>{
      'code': instance.accessToken,
      'message': instance.message,
      'description': instance.description,
      'fields': instance.fields,
    };
