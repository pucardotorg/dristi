// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'individual_search_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$IndividualSearchRequestImpl _$$IndividualSearchRequestImplFromJson(
        Map<String, dynamic> json) =>
    _$IndividualSearchRequestImpl(
      requestInfo: RequestInfoSearch.fromJson(
          json['RequestInfo'] as Map<String, dynamic>),
      individual:
          IndividualSearch.fromJson(json['Individual'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$$IndividualSearchRequestImplToJson(
        _$IndividualSearchRequestImpl instance) =>
    <String, dynamic>{
      'RequestInfo': instance.requestInfo,
      'Individual': instance.individual,
    };

_$RequestInfoSearchImpl _$$RequestInfoSearchImplFromJson(
        Map<String, dynamic> json) =>
    _$RequestInfoSearchImpl(
      authToken: json['authToken'] as String,
    );

Map<String, dynamic> _$$RequestInfoSearchImplToJson(
        _$RequestInfoSearchImpl instance) =>
    <String, dynamic>{
      'authToken': instance.authToken,
    };

_$IndividualImpl _$$IndividualImplFromJson(Map<String, dynamic> json) =>
    _$IndividualImpl(
      userUuid:
          (json['userUuid'] as List<dynamic>).map((e) => e as String).toList(),
    );

Map<String, dynamic> _$$IndividualImplToJson(_$IndividualImpl instance) =>
    <String, dynamic>{
      'userUuid': instance.userUuid,
    };

_$ResponseInfoSearchImpl _$$ResponseInfoSearchImplFromJson(
        Map<String, dynamic> json) =>
    _$ResponseInfoSearchImpl(
      apiId: json['apiId'] as String?,
      ver: json['ver'] as String?,
      ts: json['ts'] as int?,
      resMsgId: json['resMsgId'] as String?,
      msgId: json['msgId'] as String?,
      status: json['status'] as String,
    );

Map<String, dynamic> _$$ResponseInfoSearchImplToJson(
        _$ResponseInfoSearchImpl instance) =>
    <String, dynamic>{
      'apiId': instance.apiId,
      'ver': instance.ver,
      'ts': instance.ts,
      'resMsgId': instance.resMsgId,
      'msgId': instance.msgId,
      'status': instance.status,
    };

_$IndividualSearchResponseImpl _$$IndividualSearchResponseImplFromJson(
        Map<String, dynamic> json) =>
    _$IndividualSearchResponseImpl(
      responseInfo: ResponseInfoSearch.fromJson(
          json['ResponseInfo'] as Map<String, dynamic>),
      individual: (json['Individual'] as List<dynamic>)
          .map((e) => Individual.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$$IndividualSearchResponseImplToJson(
        _$IndividualSearchResponseImpl instance) =>
    <String, dynamic>{
      'ResponseInfo': instance.responseInfo,
      'Individual': instance.individual,
    };
