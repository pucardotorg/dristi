
import 'package:freezed_annotation/freezed_annotation.dart';

part 'litigant_registration_model.freezed.dart';
part 'litigant_registration_model.g.dart';

@freezed
class RequestInfo with _$RequestInfo {
  const factory RequestInfo({
    @JsonKey(name: 'authToken') @Default("") String authToken, // Use "{{authToken}}" for replacement
  }) = _RequestInfo;

  factory RequestInfo.fromJson(Map<String, dynamic> json) =>
      _$RequestInfoFromJson(json);
}

@freezed
class Name with _$Name {
  const factory Name({
    @JsonKey(name: 'givenName') required String givenName, // Use "{{individualGivenName}}" for replacement
    @JsonKey(name: 'familyName') required String familyName, // Use "{{individualFamilyName}}" for replacement
    @JsonKey(name: 'otherNames') @Default("") String otherNames, // Use "{{individualOtherName}}" for replacement
  }) = _Name;

  factory Name.fromJson(Map<String, dynamic> json) => _$NameFromJson(json);
}

@freezed
class Address with _$Address {
  const factory Address({
    @JsonKey(name: 'clientReferenceId') @Default("") String clientReferenceId, // Use "{{uuid2}}" for replacement
    @JsonKey(name: 'tenantId') @Default("mz") String tenantId,
    @JsonKey(name: 'doorNo') required String doorNo,
    @JsonKey(name: 'latitude') required double latitude,
    @JsonKey(name: 'longitude') required double longitude,
    @JsonKey(name: 'locationAccuracy') required double locationAccuracy,
    @JsonKey(name: 'type') @Default("PERMANENT") String type,
    @JsonKey(name: 'addressLine1') @Default("") String addressLine1,
    @JsonKey(name: 'addressLine2') @Default("") String addressLine2,
    @JsonKey(name: 'landmark') @Default("") String landmark,
    @JsonKey(name: 'city') required String city,
    @JsonKey(name: 'pincode') required String pincode,
    @JsonKey(name: 'buildingName') required String buildingName,
    @JsonKey(name: 'street') required String street,
    @JsonKey(name: 'locality') @Default(Locality()) Locality locality,
  }) = _Address ;

  factory Address.fromJson(Map<String, dynamic> json) => _$AddressFromJson(json);
}

@freezed
class Locality with _$Locality {
  const factory Locality({
    @JsonKey(name: 'code') @Default("test_9b31746b933d") String code,
    @JsonKey(name: 'name') @Default("test_58630a388978") String name,
    @JsonKey(name: 'label') @Default("test_7b7f928dcab8") String label,
    @JsonKey(name: 'latitude') @Default("test_15d4a90d15a6") String latitude,
    @JsonKey(name: 'longitude') @Default("test_e8854daed039") String longitude,
    @JsonKey(name: 'children') @Default([]) List<dynamic> children, // Can be any type depending on the data
  }) = _Locality ;

  factory Locality.fromJson(Map<String, dynamic> json) => _$LocalityFromJson(json);
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
class ClientAuditDetails with _$ClientAuditDetails {
  const factory ClientAuditDetails({
    @JsonKey(name: 'createdBy') @Default("a8c7b5c5-6007-4556-945e-3969c98b5167") String type, // Use "{{individualSkillType}}" for replacement
    @JsonKey(name: 'lastModifiedBy') @Default("a8c7b5c5-6007-4556-945e-3969c98b5155") String level, // Use "{{individualSkillLevel}}" for replacement
    @JsonKey(name: 'createdTime') @Default(1692774224) int createdTime, // Use "{{individualSkillExperience}}" for replacement
    @JsonKey(name: 'lastModifiedTime') @Default(16927742467) int lastModifiedTime, // Use "{{individualSkillClientReferenceId}}" for replacement
  }) = _ClientAuditDetails ;

  factory ClientAuditDetails.fromJson(Map<String, dynamic> json) => _$ClientAuditDetailsFromJson(json);
}

@freezed
class AuditDetails with _$AuditDetails {
  const factory AuditDetails({
    @JsonKey(name: 'createdBy') @Default("a8c7b5c5-6007-4556-945e-3969c98b5155") String type, // Use "{{individualSkillType}}" for replacement
    @JsonKey(name: 'lastModifiedBy') @Default("a8c7b5c5-6007-4556-945e-3969c98b5155") String level, // Use "{{individualSkillLevel}}" for replacement
    @JsonKey(name: 'createdTime') @Default(1692856327242) int experience, // Use "{{individualSkillExperience}}" for replacement
    @JsonKey(name: 'lastModifiedTime') @Default(1692856327242) int clientReferenceId, // Use "{{individualSkillClientReferenceId}}" for replacement
  }) = _AuditDetails ;

  factory AuditDetails.fromJson(Map<String, dynamic> json) => _$AuditDetailsFromJson(json);
}

@freezed
class AdditionalFields with _$AdditionalFields {
  const factory AdditionalFields({
    @JsonKey(name: 'schema') @Default("test_f76b4cca6bff") String schema, // Use "{{individualSkillType}}" for replacement
    @JsonKey(name: 'version') @Default(23) int version, // Use "{{individualSkillLevel}}" for replacement
    @JsonKey(name: 'fields') @Default([Field()]) List<Field> fields, // Use "{{individualSkillExperience}}" for replacement
  }) = _AdditionalFields ;

  factory AdditionalFields.fromJson(Map<String, dynamic> json) => _$AdditionalFieldsFromJson(json);
}

@freezed
class Field with _$Field {
  const factory Field({
    @JsonKey(name: 'key')  @Default("test_80f7fb8d8a45") String key, // Use "{{individualSkillType}}" for replacement
    @JsonKey(name: 'value')  @Default("test_38bfd0dc223b") String value, // Use "{{individualSkillLevel}}" for replacement
  }) = _Field;

  factory Field.fromJson(Map<String, dynamic> json) => _$FieldFromJson(json);
}

@freezed
class Individual with _$Individual {
  const factory Individual({
    @JsonKey(name: 'tenantId') @Default("mz") String tenantId,
    @JsonKey(name: 'clientReferenceId') @Default("") String clientReferenceId, // Use "{{uuid}}" for replacement
    @JsonKey(name: 'name') required Name name,
    @JsonKey(name: 'dateOfBirth') @Default("25-07-1996") String dateOfBirth, // Use "{{individualDateOfBirth}}" for replacement
    @JsonKey(name: 'gender') @Default("male") String gender, // Use "{{individualGenderType}}" for replacement

    @JsonKey(name: 'bloodGroup') @Default("") bloodGroup,
    @JsonKey(name: 'mobileNumber') required String mobileNumber,
    @JsonKey(name: 'altContactNumber') required String altContactNumber,
    @JsonKey(name: 'email') @Default("souravganguly_dada@gmail.com") email,
    @JsonKey(name: 'address') required List<Address> address,

    @JsonKey(name: 'fatherName') @Default("") fatherName,
    @JsonKey(name: 'husbandName') @Default("") husbandName,
    @JsonKey(name: 'identifiers') required List<Identifier> identifiers,
    @JsonKey(name: 'skills') @Default([Skill()]) List<Skill> skills,
    @JsonKey(name: 'photo') @Default("test_35bd4e8545d8") String? photo,

    @JsonKey(name: 'additionalFields') AdditionalFields? additionalFields,

    @JsonKey(name: 'isSystemUser') @Default(true) bool isSystemUser,
    @JsonKey(name: 'rowVersion') @Default(1) int rowVersion,
    @JsonKey(name: 'nonRecoverableError') @Default(false) bool nonRecoverableError,
    @JsonKey(name: 'clientAuditDetails') @Default(ClientAuditDetails()) ClientAuditDetails clientAuditDetails,
    @JsonKey(name: 'auditDetails') @Default(AuditDetails()) AuditDetails? auditDetails,

}) = _Individual;

  factory Individual.fromJson(Map<String, dynamic> json) => _$IndividualFromJson(json);

}

@freezed
class LitigantNetworkModel with _$LitigantNetworkModel {
  const factory LitigantNetworkModel({
    @JsonKey(name: 'RequestInfo') @Default(RequestInfo()) RequestInfo requestInfo,
    @JsonKey(name: 'Individual') required Individual individual, // Use "{{uuid}}" for replacement
  }) = _LitigantNetworkModel;

  factory LitigantNetworkModel.fromJson(Map<String, dynamic> json) => _$LitigantNetworkModelFromJson(json);

}

