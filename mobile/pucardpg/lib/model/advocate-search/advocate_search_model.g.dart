// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'advocate_search_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$AdvocateSearchRequestImpl _$$AdvocateSearchRequestImplFromJson(
        Map<String, dynamic> json) =>
    _$AdvocateSearchRequestImpl(
      criteria: (json['criteria'] as List<dynamic>?)
              ?.map((e) => SearchCriteria.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      status: (json['status'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      tenantId: json['tenantId'] as String?,
    );

Map<String, dynamic> _$$AdvocateSearchRequestImplToJson(
        _$AdvocateSearchRequestImpl instance) =>
    <String, dynamic>{
      'criteria': instance.criteria,
      'status': instance.status,
      'tenantId': instance.tenantId,
    };

_$SearchCriteriaImpl _$$SearchCriteriaImplFromJson(Map<String, dynamic> json) =>
    _$SearchCriteriaImpl(
      individualId: json['individualId'] as String?,
      uuid: json['uuid'] as String?,
      applicationNumber: json['applicationNumber'] as String?,
    );

Map<String, dynamic> _$$SearchCriteriaImplToJson(
        _$SearchCriteriaImpl instance) =>
    <String, dynamic>{
      'individualId': instance.individualId,
      'uuid': instance.uuid,
      'applicationNumber': instance.applicationNumber,
    };

_$AdvocateSearchResponseImpl _$$AdvocateSearchResponseImplFromJson(
        Map<String, dynamic> json) =>
    _$AdvocateSearchResponseImpl(
      responseInfo: json['responseInfo'] == null
          ? const ResponseInfoSearch(status: "")
          : ResponseInfoSearch.fromJson(
              json['responseInfo'] as Map<String, dynamic>),
      advocates: (json['advocates'] as List<dynamic>?)
              ?.map((e) => Advocate.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
    );

Map<String, dynamic> _$$AdvocateSearchResponseImplToJson(
        _$AdvocateSearchResponseImpl instance) =>
    <String, dynamic>{
      'responseInfo': instance.responseInfo,
      'advocates': instance.advocates,
    };
