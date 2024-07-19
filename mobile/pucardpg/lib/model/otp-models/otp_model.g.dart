// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'otp_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$OtpRequestImpl _$$OtpRequestImplFromJson(Map<String, dynamic> json) =>
    _$OtpRequestImpl(
      otp: Otp.fromJson(json['otp'] as Map<String, dynamic>),
      requestInfo: json['RequestInfo'] == null
          ? const RequestInfo()
          : RequestInfo.fromJson(json['RequestInfo'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$$OtpRequestImplToJson(_$OtpRequestImpl instance) =>
    <String, dynamic>{
      'otp': instance.otp,
      'RequestInfo': instance.requestInfo,
    };

_$OtpImpl _$$OtpImplFromJson(Map<String, dynamic> json) => _$OtpImpl(
      mobileNumber: json['mobileNumber'] as String,
      tenantId: json['tenantId'] as String,
      userType: json['userType'] as String? ?? "citizen",
      type: json['type'] as String,
    );

Map<String, dynamic> _$$OtpImplToJson(_$OtpImpl instance) => <String, dynamic>{
      'mobileNumber': instance.mobileNumber,
      'tenantId': instance.tenantId,
      'userType': instance.userType,
      'type': instance.type,
    };

_$OtpErrorImpl _$$OtpErrorImplFromJson(Map<String, dynamic> json) =>
    _$OtpErrorImpl(
      code: json['code'] as int?,
      message: json['message'] as String?,
    );

Map<String, dynamic> _$$OtpErrorImplToJson(_$OtpErrorImpl instance) =>
    <String, dynamic>{
      'code': instance.code,
      'message': instance.message,
    };

_$OtpResponseImpl _$$OtpResponseImplFromJson(Map<String, dynamic> json) =>
    _$OtpResponseImpl(
      isSuccessful: json['isSuccessful'] as bool?,
      error: json['error'] == null
          ? null
          : OtpError.fromJson(json['error'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$$OtpResponseImplToJson(_$OtpResponseImpl instance) =>
    <String, dynamic>{
      'isSuccessful': instance.isSuccessful,
      'error': instance.error,
    };
