// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'file_upload_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$FileUploadResponseModelImpl _$$FileUploadResponseModelImplFromJson(
        Map<String, dynamic> json) =>
    _$FileUploadResponseModelImpl(
      files: (json['files'] as List<dynamic>?)
              ?.map(
                  (e) => FirestoreFileModel.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
    );

Map<String, dynamic> _$$FileUploadResponseModelImplToJson(
        _$FileUploadResponseModelImpl instance) =>
    <String, dynamic>{
      'files': instance.files,
    };

_$FirestoreFileModelImpl _$$FirestoreFileModelImplFromJson(
        Map<String, dynamic> json) =>
    _$FirestoreFileModelImpl(
      fileStoreId: json['fileStoreId'] as String?,
      tenantId: json['tenantId'] as String?,
    );

Map<String, dynamic> _$$FirestoreFileModelImplToJson(
        _$FirestoreFileModelImpl instance) =>
    <String, dynamic>{
      'fileStoreId': instance.fileStoreId,
      'tenantId': instance.tenantId,
    };
