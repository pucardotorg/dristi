import 'package:freezed_annotation/freezed_annotation.dart';

part 'registration_model.freezed.dart';
part 'registration_model.g.dart';

@freezed
class RegistrationModel with _$RegistrationModel {

  const factory RegistrationModel({
    String? name,
    String? username,
    String? otpReference,
    String? tenantId,
  }) = _RegistrationModel;

  factory RegistrationModel.fromJson(
      Map<String, dynamic> json) =>
      _$RegistrationModelFromJson(json);

}