import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:json_annotation/json_annotation.dart';
import 'package:pucardpg/model/litigant-registration-model/litigant_registration_model.dart';

part 'individual_search_model.freezed.dart';
part 'individual_search_model.g.dart';

@freezed
class IndividualSearchRequest with _$IndividualSearchRequest {
  const factory IndividualSearchRequest({
    @JsonKey(name: 'RequestInfo') required RequestInfoSearch requestInfo,
    @JsonKey(name: 'Individual') required IndividualSearch individual,
  }) = _IndividualSearchRequest;

  factory IndividualSearchRequest.fromJson(
      Map<String, dynamic> json) =>
      _$IndividualSearchRequestFromJson(json);
}

@freezed
class RequestInfoSearch with _$RequestInfoSearch {
  const factory RequestInfoSearch({
    @JsonKey(name: 'authToken') required String authToken,
  }) = _RequestInfoSearch;

  factory RequestInfoSearch.fromJson(
      Map<String, dynamic> json) =>
      _$RequestInfoSearchFromJson(json);
}

@freezed
class IndividualSearch with _$IndividualSearch {
  const factory IndividualSearch({
    @JsonKey(name: 'userUuid') required List<String> userUuid,
  }) = _Individual;

  factory IndividualSearch.fromJson(
      Map<String, dynamic> json) =>
      _$IndividualSearchFromJson(json);
}

@freezed
class ResponseInfoSearch with _$ResponseInfoSearch {
  const factory ResponseInfoSearch({
    String? apiId, // Make nullable since it's empty in the example JSON
    String? ver,  // Make nullable
    int? ts,  // Make nullable
    String? resMsgId,  // Make nullable
    String? msgId,  // Make nullable
    required String status,
  }) = _ResponseInfoSearch;

  factory ResponseInfoSearch.fromJson(
      Map<String, dynamic> json) =>
      _$ResponseInfoSearchFromJson(json);
}

@freezed
class IndividualSearchResponse with _$IndividualSearchResponse {
  const factory IndividualSearchResponse({
    @JsonKey(name: 'ResponseInfo') required ResponseInfoSearch responseInfo,
    @JsonKey(name: 'Individual') required List<Individual> individual,
}) = _IndividualSearchResponse;

  factory IndividualSearchResponse.fromJson(
      Map<String, dynamic> json) =>
      _$IndividualSearchResponseFromJson(json);
}
