import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:pucardpg/model/advocate-registration-model/advocate_registration_model.dart';
import 'package:pucardpg/model/advocate-request-info/advocate_request_info.dart';
import 'package:pucardpg/model/individual-search/individual_search_model.dart';

part 'advocate_search_model.freezed.dart';
part 'advocate_search_model.g.dart';

@freezed
class AdvocateSearchRequest with _$AdvocateSearchRequest {
  const factory AdvocateSearchRequest({
    @JsonKey(name: "criteria") @Default([]) List<SearchCriteria> criteria,
    @JsonKey(name: 'status') @Default([]) List<String> status,
    @JsonKey(name: 'tenantId') String? tenantId,
  }) = _AdvocateSearchRequest;

  factory AdvocateSearchRequest.fromJson(Map<String, dynamic> json) =>
      _$AdvocateSearchRequestFromJson(json);
}

@freezed
class SearchCriteria with _$SearchCriteria {
  const factory SearchCriteria({
    @JsonKey(name: "individualId") String? individualId,
    @JsonKey(name: "uuid") String? uuid,
    @JsonKey(name: "applicationNumber") String? applicationNumber,
  }) = _SearchCriteria;

  factory SearchCriteria.fromJson(Map<String, dynamic> json) =>
      _$SearchCriteriaFromJson(json);
}

@freezed
class AdvocateSearchResponse with _$AdvocateSearchResponse {
  const factory AdvocateSearchResponse ({
    @JsonKey(name: 'responseInfo') @Default(ResponseInfoSearch(status: "")) ResponseInfoSearch responseInfo,
    @JsonKey(name: 'advocates') @Default([]) List<Advocate> advocates,
  }) = _AdvocateSearchResponse;

  factory AdvocateSearchResponse.fromJson(Map<String, dynamic> json) =>
      _$AdvocateSearchResponseFromJson(json);
}