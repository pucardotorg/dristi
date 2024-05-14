import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:pucardpg/model/request-info-model/request_info.dart';


part 'otp_model.freezed.dart';
part 'otp_model.g.dart';

@freezed
class OtpRequest with _$OtpRequest {

  const factory OtpRequest({
    @JsonKey(name: 'otp') required Otp otp,
    @JsonKey(name: 'RequestInfo') @Default(RequestInfo()) RequestInfo requestInfo
  }) = _OtpRequest;

  factory OtpRequest.fromJson(
      Map<String, dynamic> json) =>
      _$OtpRequestFromJson(json);

}

@freezed
class Otp with _$Otp {

  const factory Otp({
    @JsonKey(name: 'mobileNumber') required String mobileNumber,
    @JsonKey(name: 'tenantId') @Default("pg") String tenantId,
    @JsonKey(name: 'userType') @Default("citizen") String userType,
    @JsonKey(name: 'type') required String type
  }) = _Otp;

  factory Otp.fromJson(
      Map<String, dynamic> json) =>
      _$OtpFromJson(json);

}

// @freezed
// class RequestInfo with _$RequestInfo {
//
//   const factory RequestInfo({
//     @JsonKey(name: 'apiId') @Default("Rainmaker") String apiId,
//     @JsonKey(name: 'authToken') @Default("c835932f-2ad4-4d05-83d6-49e0b8c59f8a") String authToken,
//     @JsonKey(name: 'msgId') @Default("1712987382117|en_IN") String msgId,
//     @JsonKey(name: 'plainAccessRequest') @Default({}) Map<String, dynamic> plainAccessRequest
//   }) = _RequestInfo;
//
//   factory RequestInfo.fromJson(
//       Map<String, dynamic> json) =>
//       _$RequestInfoFromJson(json);
//
// }

@freezed
class OtpError with _$OtpError{

  const factory OtpError({
    @JsonKey(name: 'code') int? code,
    @JsonKey(name: 'message') String? message,
  }) = _OtpError;

  factory OtpError.fromJson(
      Map<String, dynamic> json) =>
      _$OtpErrorFromJson(json);

}


@freezed
class OtpResponse with _$OtpResponse {

  const factory OtpResponse({
    @JsonKey(name: 'isSuccessful') bool? isSuccessful,
    @JsonKey(name: 'error') OtpError? error,
  }) = _OtpResponse;

  factory OtpResponse.fromJson(
      Map<String, dynamic> json) =>
      _$OtpResponseFromJson(json);

}




