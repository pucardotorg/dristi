import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:pucardpg/model/advocate-request-info/advocate_request_info.dart';
import 'package:pucardpg/model/document-model/document_model.dart';
import 'package:pucardpg/model/individual-search/individual_search_model.dart';
import 'package:pucardpg/model/workflow-model/workflow_model.dart';

part 'advocate_clerk_registration_model.freezed.dart';
part 'advocate_clerk_registration_model.g.dart';

@freezed
class Clerk with _$Clerk {

  const factory Clerk({
    @JsonKey(name: "id") String? id,
    @JsonKey(name: 'tenantId') String? tenantId,
    @JsonKey(name: 'applicationNumber') String? applicationNumber,
    @JsonKey(name: 'status') String? status,
    @JsonKey(name: 'stateRegnNumber') String? stateRegnNumber,
    @JsonKey(name: 'advocateType') String? advocateType,
    @JsonKey(name: 'organisationID') String? organisationID,
    @JsonKey(name: 'individualId') String? individualId,
    @JsonKey(name: 'isActive') bool? isActive,
    @JsonKey(name: 'workflow') Workflow? workflow,
    @JsonKey(name: 'documents') List<Document>? documents,
    @JsonKey(name: 'additionalDetails') Map<String, dynamic>? additionalDetails,
  }) = _Clerk;

  factory Clerk.fromJson(Map<String, dynamic> json) => _$ClerkFromJson(json);

}

@freezed
class AdvocateClerkRegistrationRequest with _$AdvocateClerkRegistrationRequest {
  const factory AdvocateClerkRegistrationRequest({
    @JsonKey(name: 'clerks') required List<Clerk> clerks
  }) = _AdvocateClerkRegistrationRequest;

  factory AdvocateClerkRegistrationRequest.fromJson(Map<String, dynamic> json) =>
      _$AdvocateClerkRegistrationRequestFromJson(json);
}

@freezed
class AdvocateClerkRegistrationResponse with _$AdvocateClerkRegistrationResponse {
  const factory AdvocateClerkRegistrationResponse({
    @JsonKey(name: 'responseInfo') @Default(ResponseInfoSearch(status: "")) ResponseInfoSearch responseInfo,
    @JsonKey(name: 'clerks') required List<Clerk> clerks,
  }) = _AdvocateClerkRegistrationResponse;

  factory AdvocateClerkRegistrationResponse.fromJson(Map<String, dynamic> json) =>
      _$AdvocateClerkRegistrationResponseFromJson(json);
}