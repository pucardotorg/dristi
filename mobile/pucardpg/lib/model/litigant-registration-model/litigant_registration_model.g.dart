// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'litigant_registration_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$NameImpl _$$NameImplFromJson(Map<String, dynamic> json) => _$NameImpl(
      givenName: json['givenName'] as String,
      familyName: json['familyName'] as String,
    );

Map<String, dynamic> _$$NameImplToJson(_$NameImpl instance) =>
    <String, dynamic>{
      'givenName': instance.givenName,
      'familyName': instance.familyName,
    };

_$AddressImpl _$$AddressImplFromJson(Map<String, dynamic> json) =>
    _$AddressImpl(
      tenantId: json['tenantId'] as String? ?? "pg",
      type: json['type'] as String? ?? "PERMANENT",
      doorNo: json['doorNo'] as String?,
      latitude: (json['latitude'] as num?)?.toDouble(),
      longitude: (json['longitude'] as num?)?.toDouble(),
      city: json['city'] as String?,
      street: json['street'] as String?,
      pincode: json['pincode'] as String?,
      district: json['district'] as String?,
    );

Map<String, dynamic> _$$AddressImplToJson(_$AddressImpl instance) =>
    <String, dynamic>{
      'tenantId': instance.tenantId,
      'type': instance.type,
      'doorNo': instance.doorNo,
      'latitude': instance.latitude,
      'longitude': instance.longitude,
      'city': instance.city,
      'street': instance.street,
      'pincode': instance.pincode,
      'district': instance.district,
    };

_$IdentifierImpl _$$IdentifierImplFromJson(Map<String, dynamic> json) =>
    _$IdentifierImpl(
      identifierType: json['identifierType'] as String,
      identifierId: json['identifierId'] as String,
    );

Map<String, dynamic> _$$IdentifierImplToJson(_$IdentifierImpl instance) =>
    <String, dynamic>{
      'identifierType': instance.identifierType,
      'identifierId': instance.identifierId,
    };

_$AdditionalFieldsImpl _$$AdditionalFieldsImplFromJson(
        Map<String, dynamic> json) =>
    _$AdditionalFieldsImpl(
      fields: (json['fields'] as List<dynamic>?)
              ?.map((e) => Fields.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
    );

Map<String, dynamic> _$$AdditionalFieldsImplToJson(
        _$AdditionalFieldsImpl instance) =>
    <String, dynamic>{
      'fields': instance.fields,
    };

_$FieldsImpl _$$FieldsImplFromJson(Map<String, dynamic> json) => _$FieldsImpl(
      key: json['key'] as String,
      value: json['value'] as String,
    );

Map<String, dynamic> _$$FieldsImplToJson(_$FieldsImpl instance) =>
    <String, dynamic>{
      'key': instance.key,
      'value': instance.value,
    };

_$UserDetailsImpl _$$UserDetailsImplFromJson(Map<String, dynamic> json) =>
    _$UserDetailsImpl(
      username: json['username'] as String,
      roles: (json['roles'] as List<dynamic>)
          .map((e) => Role.fromJson(e as Map<String, dynamic>))
          .toList(),
      type: json['type'] as String? ?? "CITIZEN",
    );

Map<String, dynamic> _$$UserDetailsImplToJson(_$UserDetailsImpl instance) =>
    <String, dynamic>{
      'username': instance.username,
      'roles': instance.roles,
      'type': instance.type,
    };

_$IndividualImpl _$$IndividualImplFromJson(Map<String, dynamic> json) =>
    _$IndividualImpl(
      tenantId: json['tenantId'] as String? ?? "pg",
      individualId: json['individualId'] as String?,
      name: Name.fromJson(json['name'] as Map<String, dynamic>),
      userDetails:
          UserDetails.fromJson(json['userDetails'] as Map<String, dynamic>),
      userUuid: json['userUuid'] as String,
      userId: json['userId'] as String,
      mobileNumber: json['mobileNumber'] as String,
      address: (json['address'] as List<dynamic>)
          .map((e) => Address.fromJson(e as Map<String, dynamic>))
          .toList(),
      identifiers: (json['identifiers'] as List<dynamic>)
          .map((e) => Identifier.fromJson(e as Map<String, dynamic>))
          .toList(),
      isSystemUser: json['isSystemUser'] as bool? ?? true,
      skills: (json['skills'] as List<dynamic>?)
              ?.map((e) => Skill.fromJson(e as Map<String, dynamic>))
              .toList() ??
          const [],
      additionalFields: AdditionalFields.fromJson(
          json['additionalFields'] as Map<String, dynamic>),
      clientAuditDetails: json['clientAuditDetails'] == null
          ? const ClientAuditDetails()
          : ClientAuditDetails.fromJson(
              json['clientAuditDetails'] as Map<String, dynamic>),
      auditDetails: json['auditDetails'] == null
          ? const AuditDetails()
          : AuditDetails.fromJson(json['auditDetails'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$$IndividualImplToJson(_$IndividualImpl instance) =>
    <String, dynamic>{
      'tenantId': instance.tenantId,
      'individualId': instance.individualId,
      'name': instance.name,
      'userDetails': instance.userDetails,
      'userUuid': instance.userUuid,
      'userId': instance.userId,
      'mobileNumber': instance.mobileNumber,
      'address': instance.address,
      'identifiers': instance.identifiers,
      'isSystemUser': instance.isSystemUser,
      'skills': instance.skills,
      'additionalFields': instance.additionalFields,
      'clientAuditDetails': instance.clientAuditDetails,
      'auditDetails': instance.auditDetails,
    };

_$ClientAuditDetailsImpl _$$ClientAuditDetailsImplFromJson(
        Map<String, dynamic> json) =>
    _$ClientAuditDetailsImpl(
      type: json['createdBy'] as String?,
      level: json['lastModifiedBy'] as String?,
      createdTime: json['createdTime'] as int?,
      lastModifiedTime: json['lastModifiedTime'] as int?,
    );

Map<String, dynamic> _$$ClientAuditDetailsImplToJson(
        _$ClientAuditDetailsImpl instance) =>
    <String, dynamic>{
      'createdBy': instance.type,
      'lastModifiedBy': instance.level,
      'createdTime': instance.createdTime,
      'lastModifiedTime': instance.lastModifiedTime,
    };

_$AuditDetailsImpl _$$AuditDetailsImplFromJson(Map<String, dynamic> json) =>
    _$AuditDetailsImpl(
      type: json['createdBy'] as String?,
      level: json['lastModifiedBy'] as String?,
      experience: json['createdTime'] as int?,
      clientReferenceId: json['lastModifiedTime'] as int?,
    );

Map<String, dynamic> _$$AuditDetailsImplToJson(_$AuditDetailsImpl instance) =>
    <String, dynamic>{
      'createdBy': instance.type,
      'lastModifiedBy': instance.level,
      'createdTime': instance.experience,
      'lastModifiedTime': instance.clientReferenceId,
    };

_$SkillImpl _$$SkillImplFromJson(Map<String, dynamic> json) => _$SkillImpl(
      type: json['type'] as String? ?? "",
      level: json['level'] as String? ?? "",
      experience: json['experience'] as String? ?? "",
      clientReferenceId: json['clientReferenceId'] as String? ?? "",
    );

Map<String, dynamic> _$$SkillImplToJson(_$SkillImpl instance) =>
    <String, dynamic>{
      'type': instance.type,
      'level': instance.level,
      'experience': instance.experience,
      'clientReferenceId': instance.clientReferenceId,
    };

_$LitigantNetworkModelImpl _$$LitigantNetworkModelImplFromJson(
        Map<String, dynamic> json) =>
    _$LitigantNetworkModelImpl(
      requestInfo:
          RequestInfo.fromJson(json['RequestInfo'] as Map<String, dynamic>),
      individual:
          Individual.fromJson(json['Individual'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$$LitigantNetworkModelImplToJson(
        _$LitigantNetworkModelImpl instance) =>
    <String, dynamic>{
      'RequestInfo': instance.requestInfo,
      'Individual': instance.individual,
    };

_$IndividualInfoImpl _$$IndividualInfoImplFromJson(Map<String, dynamic> json) =>
    _$IndividualInfoImpl(
      individualId: json['individualId'] as String,
    );

Map<String, dynamic> _$$IndividualInfoImplToJson(
        _$IndividualInfoImpl instance) =>
    <String, dynamic>{
      'individualId': instance.individualId,
    };

_$LitigantResponseModelImpl _$$LitigantResponseModelImplFromJson(
        Map<String, dynamic> json) =>
    _$LitigantResponseModelImpl(
      responseInfo: ResponseInfoSearch.fromJson(
          json['ResponseInfo'] as Map<String, dynamic>),
      individualInfo:
          IndividualInfo.fromJson(json['Individual'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$$LitigantResponseModelImplToJson(
        _$LitigantResponseModelImpl instance) =>
    <String, dynamic>{
      'ResponseInfo': instance.responseInfo,
      'Individual': instance.individualInfo,
    };
