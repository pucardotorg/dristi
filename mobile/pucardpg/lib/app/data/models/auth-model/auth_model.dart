import 'package:freezed_annotation/freezed_annotation.dart';

part 'auth_model.freezed.dart';
part 'auth_model.g.dart';

@freezed
class AuthModel with _$AuthModel {

  const factory AuthModel({
    String? username,
    String? password,
    String? tenantId,
    String? userType,
  }) = _AuthModel;

  factory AuthModel.fromJson(
      Map<String, dynamic> json) =>
      _$AuthModelFromJson(json);

}