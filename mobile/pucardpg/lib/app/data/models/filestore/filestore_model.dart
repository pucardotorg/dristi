import 'package:freezed_annotation/freezed_annotation.dart';

part 'filestore_model.freezed.dart';
part 'filestore_model.g.dart';

@freezed
class FileStoreResponse with _$FileStoreResponse {
  const factory FileStoreResponse({
    @JsonKey(name: 'fileStoreIds') required List<Url> fileStoreIds,
  }) = _FileStoreResponse;

  factory FileStoreResponse.fromJson(Map<String, dynamic> json) => _$FileStoreResponseFromJson(json);
}

@freezed
class Url with _$Url {
  const factory Url({
    @JsonKey(name: 'id') required String? id,
    @JsonKey(name: 'url') required String? url,
  }) = _Url;

  factory Url.fromJson(Map<String, dynamic> json) => _$UrlFromJson(json);
}

@freezed
class FileUploadResponse with _$FileUploadResponse {
  const factory FileUploadResponse({
    @JsonKey(name: 'files') required List<FileStoreId> files,
  }) = _FileUploadResponse;

  factory FileUploadResponse.fromJson(Map<String, dynamic> json) => _$FileUploadResponseFromJson(json);
}

@freezed
class FileStoreId with _$FileStoreId {
  const factory FileStoreId({
    @JsonKey(name: 'fileStoreId') required String fileStoreId,
    @JsonKey(name: 'tenantId') required String tenantId,
  }) = _FileStoreId;

  factory FileStoreId.fromJson(Map<String, dynamic> json) => _$FileStoreIdFromJson(json);
}