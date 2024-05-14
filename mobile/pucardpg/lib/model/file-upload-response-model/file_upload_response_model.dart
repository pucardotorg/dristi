import 'package:freezed_annotation/freezed_annotation.dart';

part 'file_upload_response_model.freezed.dart';
part 'file_upload_response_model.g.dart';

@freezed
class FileUploadResponseModel with _$FileUploadResponseModel {
  const factory FileUploadResponseModel({
    @JsonKey(name: 'files') @Default([]) List<FirestoreFileModel> files,
  }) = _FileUploadResponseModel;

  factory FileUploadResponseModel.fromJson(Map<String, dynamic> json) =>
      _$FileUploadResponseModelFromJson(json);
}

@freezed
class FirestoreFileModel with _$FirestoreFileModel {
  const factory FirestoreFileModel({
    @JsonKey(name: 'fileStoreId') String? fileStoreId,
    @JsonKey(name: 'tenantId') String? tenantId,
  }) = _FirestoreFileModel;

  factory FirestoreFileModel.fromJson(Map<String, dynamic> json) =>
      _$FirestoreFileModelFromJson(json);
}
