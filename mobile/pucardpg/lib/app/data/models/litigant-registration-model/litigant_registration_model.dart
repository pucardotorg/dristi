
import 'package:freezed_annotation/freezed_annotation.dart';

part 'litigant_registration_model.freezed.dart';
part 'litigant_registration_model.g.dart';

@freezed
abstract class RequestInfo with _$RequestInfo {
  const factory RequestInfo({
    @JsonKey(name: 'authToken') required String authToken, // Use "{{authToken}}" for replacement
  }) = _RequestInfo;

  factory RequestInfo.fromJson(Map<String, dynamic> json) =>
      _$RequestInfoFromJson(json);
}

@freezed
abstract class Name with _$Name {
  const factory Name({
    @JsonKey(name: 'givenName') required String givenName, // Use "{{individualGivenName}}" for replacement
    @JsonKey(name: 'familyName') required String familyName, // Use "{{individualFamilyName}}" for replacement
    @JsonKey(name: 'otherNames') required String otherNames, // Use "{{individualOtherName}}" for replacement
  }) = _Name;

  factory Name.fromJson(Map<String, dynamic> json) => _$NameFromJson(json);
}

@freezed
abstract class Address with _$Address {
  const factory Address({
    @JsonKey(name: 'clientReferenceId') required String clientReferenceId, // Use "{{uuid2}}" for replacement
    @JsonKey(name: 'tenantId') required String tenantId,
    @JsonKey(name: 'doorNo') required String doorNo,
    @JsonKey(name: 'latitude') required double latitude,
    @JsonKey(name: 'longitude') required double longitude,
    @JsonKey(name: 'locationAccuracy') required double locationAccuracy,
    @JsonKey(name: 'type') required String type,
    @JsonKey(name: 'addressLine1') required String addressLine1,
    @JsonKey(name: 'addressLine2') required String addressLine2,
    @JsonKey(name: 'landmark') required String landmark,
    @JsonKey(name: 'city') required String city,
    @JsonKey(name: 'pincode') required String pincode,
    @JsonKey(name: 'buildingName') required String buildingName,
    @JsonKey(name: 'street') required String street,
    @JsonKey(name: 'locality') required Locality locality,
  }) = _Address ;

  factory Address.fromJson(Map<String, dynamic> json) => _$AddressFromJson(json);
}

@freezed
abstract class Locality with _$Locality {
  const factory Locality({
    @JsonKey(name: 'code') required String code,
    @JsonKey(name: 'name') required String name,
    @JsonKey(name: 'label') required String label,
    @JsonKey(name: 'latitude') required String latitude,
    @JsonKey(name: 'longitude') required String longitude,
    @JsonKey(name: 'children') @Default([]) required List<dynamic> children, // Can be any type depending on the data
  }) = _Locality ;

  factory Locality.fromJson(Map<String, dynamic> json) => _$LocalityFromJson(json);
}

@freezed
abstract class Identifier with _$Identifier {
  const factory Identifier({
    @JsonKey(name: 'identifierType') required String identifierType,
    @JsonKey(name: 'identifierId') required String identifierId,
  }) = _Identifier ;

  factory Identifier.fromJson(Map<String, dynamic> json) =>
      _$IdentifierFromJson(json);
}

@freezed
abstract class Skill with _$Skill {
  const factory Skill({
    @JsonKey(name: 'type') required String type, // Use "{{individualSkillType}}" for replacement
    @JsonKey(name: 'level') required String level, // Use "{{individualSkillLevel}}" for replacement
    @JsonKey(name: 'experience') required String experience, // Use "{{individualSkillExperience}}" for replacement
    @JsonKey(name: 'clientReferenceId') required String clientReferenceId, // Use "{{individualSkillClientReferenceId}}" for replacement
  }) = _Skill ;

  factory Skill.fromJson(Map<String, dynamic> json) => _$SkillFromJson(json);
}

@freezed
abstract class Individual with _$Individual {
  const factory Individual({
    @JsonKey(name: 'tenantId') required String tenantId,
    @JsonKey(name: 'clientReferenceId') required String clientReferenceId, // Use "{{uuid}}" for replacement
    Name? name,
    @JsonKey(
        name: 'dateOfBirth') required String dateOfBirth, // Use "{{individualDateOfBirth}}" for replacement
    @JsonKey(
        name: 'gender') required String gender, // Use "{{individualGenderType}}" for replacement
  }) = _Individual;

  factory Individual.fromJson(Map<String, dynamic> json) => _$IndividualFromJson(json);

}

// }