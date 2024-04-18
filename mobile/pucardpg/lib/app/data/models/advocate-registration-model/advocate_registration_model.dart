import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:json_annotation/json_annotation.dart';
import 'package:pucardpg/app/data/models/auth-response/auth_response.dart';

part 'advocate_registration_model.freezed.dart';
part 'advocate_registration_model.g.dart';

@freezed
class RequestInfo with _$RequestInfo {
  const factory RequestInfo({
    @JsonKey(name: 'apiId') String? apiId,
    @JsonKey(name: 'ver') String? ver,
    @JsonKey(name: 'ts') int? ts,
    @JsonKey(name: 'resMsgId') String? resMsgId,
    @JsonKey(name: 'msgId') String? msgId,
    @JsonKey(name: 'requesterId') String? requesterId,
    @JsonKey(name: 'authToken') String? authToken,
  }) = _RequestInfo;

  factory RequestInfo.fromJson(Map<String, dynamic> json) =>
      _$RequestInfoFromJson(json);
}

@freezed
class Advocate with _$Advocate {
  const factory Advocate({
    @JsonKey(name: 'tenantId') String? tenantId,
    @JsonKey(name: 'applicationNumber') String? applicationNumber,
    @JsonKey(name: 'barRegistrationNumber') String? barRegistrationNumber,
    @JsonKey(name: 'advocateType') String? advocateType,
    @JsonKey(name: 'organisationID') String? organisationID,
    @JsonKey(name: 'individualId') String? individualId,
    @JsonKey(name: 'isActive') bool? isActive,
    @JsonKey(name: 'workflow') Workflow? workflow,
    @JsonKey(name: 'documents') List<Document>? documents,
    @JsonKey(name: 'additionalDetails') Map<String, dynamic>? additionalDetails,
  }) = _Advocate;

  factory Advocate.fromJson(Map<String, dynamic> json) =>
      _$AdvocateFromJson(json);
}

@freezed
class Workflow with _$Workflow {
  const factory Workflow({
    @JsonKey(name: 'action') String? action,
    @JsonKey(name: 'comment') String? comment,
    @JsonKey(name: 'assignees') List<String>? assignees,
  }) = _Workflow;

  factory Workflow.fromJson(Map<String, dynamic> json) =>
      _$WorkflowFromJson(json);
}

@freezed
class Document with _$Document {
  const factory Document({
    @JsonKey(name: 'documentType') String? documentType,
    @JsonKey(name: 'fileStore') String? fileStore,
    @JsonKey(name: 'documentUid') String? documentUid,
    @JsonKey(name: 'additionalDetails') Map<String, dynamic>? additionalDetails,
  }) = _Document;

  factory Document.fromJson(Map<String, dynamic> json) =>
      _$DocumentFromJson(json);
}

@freezed
class AdvocateRegistrationRequest with _$AdvocateRegistrationRequest {
  const factory AdvocateRegistrationRequest({
    @JsonKey(name: 'requestInfo') @Default(RequestInfo()) RequestInfo requestInfo,
    @JsonKey(name: 'advocates') required List<Advocate> advocates
  }) = _AdvocateRegistrationRequest;

  factory AdvocateRegistrationRequest.fromJson(Map<String, dynamic> json) =>
      _$AdvocateRegistrationRequestFromJson(json);
}

@freezed
class Pagination with _$Pagination {
  const factory Pagination({
    @JsonKey(name: 'limit') int? limit,
    @JsonKey(name: 'offSet') int? offSet,
    @JsonKey(name: 'totalCount') int? totalCount,
    @JsonKey(name: 'sortBy') String? sortBy,
    @JsonKey(name: 'order') Map<String, dynamic>? order,
  }) = _Pagination;

  factory Pagination.fromJson(Map<String, dynamic> json) =>
      _$PaginationFromJson(json);
}

@freezed
class AdvocateRegistrationResponse with _$AdvocateRegistrationResponse {
  const factory AdvocateRegistrationResponse({
    @JsonKey(name: 'responseInfo') @Default(ResponseInfo(status: "")) ResponseInfo responseInfo,
    @JsonKey(name: 'advocates') required List<Advocate> advocates,
    @JsonKey(name: 'pagination') @Default(Pagination()) Pagination pagination
  }) = _AdvocateRegistrationResponse;

  factory AdvocateRegistrationResponse.fromJson(Map<String, dynamic> json) =>
      _$AdvocateRegistrationResponseFromJson(json);
}