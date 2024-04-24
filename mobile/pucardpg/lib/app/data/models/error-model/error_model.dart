import 'package:freezed_annotation/freezed_annotation.dart';

part 'error_model.freezed.dart';
part 'error_model.g.dart';

@freezed
class ErrorModel with _$ErrorModel {
  const factory ErrorModel({
    @JsonKey(name: 'code') required String? accessToken,
    @JsonKey(name: 'message') required String? message,
    @JsonKey(name: 'description') required String? description,
    @JsonKey(name: 'fields') required String? fields,
  }) = _ErrorModel;

  factory ErrorModel.fromJson(Map<String, dynamic> json) => _$ErrorModelFromJson(json);
}