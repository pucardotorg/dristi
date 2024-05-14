import 'package:freezed_annotation/freezed_annotation.dart';

part 'document_model.freezed.dart';
part 'document_model.g.dart';

@freezed
class Document with _$Document {
  const factory Document({
    @JsonKey(name: 'documentType') String? documentType,
    @JsonKey(name: 'fileStore') String? fileStore,
    @JsonKey(name: 'documentUid') String? documentUid,
    @JsonKey(name: 'additionalDetails') Object? additionalDetails,
  }) = _Document;

  factory Document.fromJson(Map<String, dynamic> json) =>
      _$DocumentFromJson(json);
}