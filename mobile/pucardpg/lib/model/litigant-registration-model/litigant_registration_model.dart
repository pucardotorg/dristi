
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:pucardpg/model/individual-search/individual_search_model.dart';
import 'package:pucardpg/model/request-info-model/request_info.dart';
import 'package:pucardpg/model/role-model/role.dart';


part 'litigant_registration_model.freezed.dart';
part 'litigant_registration_model.g.dart';

@freezed
class Name with _$Name {
  const factory Name({
    @JsonKey(name: 'givenName') required String givenName, // Use "{{individualGivenName}}" for replacement
    @JsonKey(name: 'familyName') required String familyName, // Use "{{individualFamilyName}}" for replacement
  }) = _Name;

  factory Name.fromJson(Map<String, dynamic> json) => _$NameFromJson(json);
}

@freezed
class Address with _$Address {
  const factory Address({
    @JsonKey(name: 'tenantId') required String tenantId,
    @JsonKey(name: 'type') @Default("PERMANENT") String type,
    @JsonKey(name: 'addressLine1') required String? addressLine1,
    @JsonKey(name: 'addressLine2') required String? addressLine2,
    @JsonKey(name: 'doorNo') required String? doorNo,
    @JsonKey(name: 'buildingName') required String? buildingName,
    @JsonKey(name: 'latitude') required double? latitude,
    @JsonKey(name: 'longitude') required double? longitude,
    @JsonKey(name: 'city') required String? city,
    @JsonKey(name: 'street') required String? street,
    @JsonKey(name: 'pincode') required String? pincode,
  }) = _Address ;

  factory Address.fromJson(Map<String, dynamic> json) => _$AddressFromJson(json);
}

@freezed
class Identifier with _$Identifier {
  const factory Identifier({
    @JsonKey(name: 'identifierType') required String identifierType,
    @JsonKey(name: 'identifierId') required String identifierId,
  }) = _Identifier ;

  factory Identifier.fromJson(Map<String, dynamic> json) =>
      _$IdentifierFromJson(json);
}

@freezed
class AdditionalFields with _$AdditionalFields {
  const factory AdditionalFields({
    @JsonKey(name: 'fields') @Default([]) List<Fields> fields, // Use "{{individualSkillExperience}}" for replacement
  }) = _AdditionalFields ;

  factory AdditionalFields.fromJson(Map<String, dynamic> json) => _$AdditionalFieldsFromJson(json);
}

@freezed
class Fields with _$Fields {
  const factory Fields({
    @JsonKey(name: 'key') required String key, // Use "{{individualSkillType}}" for replacement
    @JsonKey(name: 'value') required String value, // Use "{{individualSkillLevel}}" for replacement
  }) = _Fields;

  factory Fields.fromJson(Map<String, dynamic> json) => _$FieldsFromJson(json);
}

@freezed
class UserDetails with _$UserDetails {
  const factory UserDetails({
    @JsonKey(name: 'username') required String username,
    @JsonKey(name: 'roles') required List<Role> roles,
    @JsonKey(name: 'type') @Default("CITIZEN") String type,
  }) = _UserDetails;

  factory UserDetails.fromJson(Map<String, dynamic> json) => _$UserDetailsFromJson(json);
}


@freezed
class Individual with _$Individual {
  const factory Individual({
    @JsonKey(name: 'tenantId') required String tenantId,
    @JsonKey(name: 'individualId') String? individualId,
    @JsonKey(name: 'name') required Name name,
    @JsonKey(name: 'userDetails') required UserDetails userDetails, // Use "{{individualDateOfBirth}}" for replacement

    @JsonKey(name: 'userUuid') required String userUuid, // Use "{{individualGenderType}}" for replacement
    @JsonKey(name: 'userId') required String userId,
    @JsonKey(name: 'mobileNumber') required String mobileNumber,
    @JsonKey(name: 'address') required List<Address> address,

    @JsonKey(name: 'identifiers') required List<Identifier> identifiers,
    @JsonKey(name: 'isSystemUser') @Default(true) bool isSystemUser,
    @JsonKey(name: 'skills') @Default([]) List<Skill> skills,
    @JsonKey(name: 'additionalFields') required AdditionalFields additionalFields,

    @JsonKey(name: 'clientAuditDetails') @Default(ClientAuditDetails()) ClientAuditDetails clientAuditDetails,
    @JsonKey(name: 'auditDetails') @Default(AuditDetails()) AuditDetails? auditDetails,

}) = _Individual;

  factory Individual.fromJson(Map<String, dynamic> json) => _$IndividualFromJson(json);

}

@freezed
class ClientAuditDetails with _$ClientAuditDetails {
  const factory ClientAuditDetails({
    @JsonKey(name: 'createdBy') String? type, // Use "{{individualSkillType}}" for replacement
    @JsonKey(name: 'lastModifiedBy') String? level, // Use "{{individualSkillLevel}}" for replacement
    @JsonKey(name: 'createdTime') int? createdTime, // Use "{{individualSkillExperience}}" for replacement
    @JsonKey(name: 'lastModifiedTime') int? lastModifiedTime, // Use "{{individualSkillClientReferenceId}}" for replacement
  }) = _ClientAuditDetails ;

  factory ClientAuditDetails.fromJson(Map<String, dynamic> json) => _$ClientAuditDetailsFromJson(json);
}

@freezed
class AuditDetails with _$AuditDetails {
  const factory AuditDetails({
    @JsonKey(name: 'createdBy') String? type, // Use "{{individualSkillType}}" for replacement
    @JsonKey(name: 'lastModifiedBy') String? level, // Use "{{individualSkillLevel}}" for replacement
    @JsonKey(name: 'createdTime') int? experience, // Use "{{individualSkillExperience}}" for replacement
    @JsonKey(name: 'lastModifiedTime') int? clientReferenceId, // Use "{{individualSkillClientReferenceId}}" for replacement
  }) = _AuditDetails ;

  factory AuditDetails.fromJson(Map<String, dynamic> json) => _$AuditDetailsFromJson(json);
}

@freezed
class Skill with _$Skill {
  const factory Skill({
    @JsonKey(name: 'type') @Default("") String type, // Use "{{individualSkillType}}" for replacement
    @JsonKey(name: 'level') @Default("") String level, // Use "{{individualSkillLevel}}" for replacement
    @JsonKey(name: 'experience') @Default("") String experience, // Use "{{individualSkillExperience}}" for replacement
    @JsonKey(name: 'clientReferenceId') @Default("") String clientReferenceId, // Use "{{individualSkillClientReferenceId}}" for replacement
  }) = _Skill ;

  factory Skill.fromJson(Map<String, dynamic> json) => _$SkillFromJson(json);
}

@freezed
class LitigantNetworkModel with _$LitigantNetworkModel {
  const factory LitigantNetworkModel({
    @JsonKey(name: 'RequestInfo') required RequestInfo requestInfo,
    @JsonKey(name: 'Individual') required Individual individual, // Use "{{uuid}}" for replacement
  }) = _LitigantNetworkModel;

  factory LitigantNetworkModel.fromJson(Map<String, dynamic> json) => _$LitigantNetworkModelFromJson(json);

}

@freezed
class IndividualInfo with _$IndividualInfo {
  const factory IndividualInfo({
  @JsonKey(name: 'individualId') required String individualId,
}) = _IndividualInfo;

factory IndividualInfo.fromJson(Map<String, dynamic> json) => _$IndividualInfoFromJson(json);
}

@freezed
class LitigantResponseModel with _$LitigantResponseModel {
  const factory LitigantResponseModel({
    @JsonKey(name: 'ResponseInfo') required ResponseInfoSearch responseInfo,
    @JsonKey(name: 'Individual') required IndividualInfo individualInfo,
  }) = _LitigantResponseModel;

  factory LitigantResponseModel.fromJson(Map<String, dynamic> json) => _$LitigantResponseModelFromJson(json);
}

