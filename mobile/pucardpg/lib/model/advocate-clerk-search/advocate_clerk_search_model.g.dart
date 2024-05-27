// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'advocate_clerk_search_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$AdvocateClerkSearchRequestImpl _$$AdvocateClerkSearchRequestImplFromJson(
        Map<String, dynamic> json) =>
    _$AdvocateClerkSearchRequestImpl(
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

Map<String, dynamic> _$$AdvocateClerkSearchRequestImplToJson(
        _$AdvocateClerkSearchRequestImpl instance) =>
    <String, dynamic>{
      'criteria': instance.criteria,
      'status': instance.status,
      'tenantId': instance.tenantId,
    };

_$AdvocateClerkSearchResponseImpl _$$AdvocateClerkSearchResponseImplFromJson(
        Map<String, dynamic> json) =>
    _$AdvocateClerkSearchResponseImpl(
      responseInfo: json['responseInfo'] == null
          ? const ResponseInfoSearch(status: "")
          : ResponseInfoSearch.fromJson(
              json['responseInfo'] as Map<String, dynamic>),
      clerks: (json['clerks'] as List<dynamic>?)
              ?.map((e) => Clerk.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
    );

Map<String, dynamic> _$$AdvocateClerkSearchResponseImplToJson(
        _$AdvocateClerkSearchResponseImpl instance) =>
    <String, dynamic>{
      'responseInfo': instance.responseInfo,
      'clerks': instance.clerks,
    };
