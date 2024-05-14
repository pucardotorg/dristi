// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'filestore_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$FileStoreResponseImpl _$$FileStoreResponseImplFromJson(
        Map<String, dynamic> json) =>
    _$FileStoreResponseImpl(
      fileStoreIds: (json['fileStoreIds'] as List<dynamic>)
          .map((e) => Url.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$$FileStoreResponseImplToJson(
        _$FileStoreResponseImpl instance) =>
    <String, dynamic>{
      'fileStoreIds': instance.fileStoreIds,
    };

_$UrlImpl _$$UrlImplFromJson(Map<String, dynamic> json) => _$UrlImpl(
      id: json['id'] as String?,
      url: json['url'] as String?,
    );

Map<String, dynamic> _$$UrlImplToJson(_$UrlImpl instance) => <String, dynamic>{
      'id': instance.id,
      'url': instance.url,
    };

_$FileUploadResponseImpl _$$FileUploadResponseImplFromJson(
        Map<String, dynamic> json) =>
    _$FileUploadResponseImpl(
      files: (json['files'] as List<dynamic>)
          .map((e) => FileStoreId.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$$FileUploadResponseImplToJson(
        _$FileUploadResponseImpl instance) =>
    <String, dynamic>{
      'files': instance.files,
    };

_$FileStoreIdImpl _$$FileStoreIdImplFromJson(Map<String, dynamic> json) =>
    _$FileStoreIdImpl(
      fileStoreId: json['fileStoreId'] as String,
      tenantId: json['tenantId'] as String,
    );

Map<String, dynamic> _$$FileStoreIdImplToJson(_$FileStoreIdImpl instance) =>
    <String, dynamic>{
      'fileStoreId': instance.fileStoreId,
      'tenantId': instance.tenantId,
    };
