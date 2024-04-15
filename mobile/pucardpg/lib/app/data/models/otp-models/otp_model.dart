import 'package:freezed_annotation/freezed_annotation.dart';

part 'otp_model.freezed.dart';
part 'otp_model.g.dart';

@freezed
class OtpRequest with _$OtpRequest {

  const factory OtpRequest({
    Otp? otp,
    RequestInfo? requestInfo
  }) = _OtpRequest;

  factory OtpRequest.fromJson(
      Map<String, dynamic> json) =>
      _$OtpRequestFromJson(json);

}

@freezed
class Otp with _$Otp {

  const factory Otp({
    String? mobileNumber,
    String? tenantId,
    String? userType,
    String? type
  }) = _Otp;

  factory Otp.fromJson(
      Map<String, dynamic> json) =>
      _$OtpFromJson(json);

}

@freezed
class RequestInfo with _$RequestInfo {

  const factory RequestInfo({
    String? apiId,
    String? authToken,
    String? msgId,
    Map<String, dynamic>? plainAccessRequest
  }) = _RequestInfo;

  factory RequestInfo.fromJson(
      Map<String, dynamic> json) =>
      _$RequestInfoFromJson(json);

}
