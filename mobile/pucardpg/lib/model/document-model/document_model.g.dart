// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'document_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$DocumentImpl _$$DocumentImplFromJson(Map<String, dynamic> json) =>
    _$DocumentImpl(
      documentType: json['documentType'] as String?,
      fileStore: json['fileStore'] as String?,
      documentUid: json['documentUid'] as String?,
      additionalDetails: json['additionalDetails'],
    );

Map<String, dynamic> _$$DocumentImplToJson(_$DocumentImpl instance) =>
    <String, dynamic>{
      'documentType': instance.documentType,
      'fileStore': instance.fileStore,
      'documentUid': instance.documentUid,
      'additionalDetails': instance.additionalDetails,
    };
