// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'auth_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$AuthResponseImpl _$$AuthResponseImplFromJson(Map<String, dynamic> json) =>
    _$AuthResponseImpl(
      accessToken: json['access_token'] as String?,
      tokenType: json['token_type'] as String?,
      refreshToken: json['refresh_token'] as String?,
      expiresIn: json['expires_in'] as int?,
      scope: json['scope'] as String?,
      responseInfo: json['ResponseInfo'] == null
          ? null
          : ResponseInfo.fromJson(json['ResponseInfo'] as Map<String, dynamic>),
      userRequest: json['UserRequest'] == null
          ? null
          : UserRequest.fromJson(json['UserRequest'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$$AuthResponseImplToJson(_$AuthResponseImpl instance) =>
    <String, dynamic>{
      'access_token': instance.accessToken,
      'token_type': instance.tokenType,
      'refresh_token': instance.refreshToken,
      'expires_in': instance.expiresIn,
      'scope': instance.scope,
      'ResponseInfo': instance.responseInfo,
      'UserRequest': instance.userRequest,
    };

_$ResponseInfoImpl _$$ResponseInfoImplFromJson(Map<String, dynamic> json) =>
    _$ResponseInfoImpl(
      apiId: json['api_id'] as String?,
      ver: json['ver'] as String?,
      ts: json['ts'] as String?,
      resMsgId: json['res_msg_id'] as String?,
      msgId: json['msg_id'] as String?,
      status: json['status'] as String?,
    );

Map<String, dynamic> _$$ResponseInfoImplToJson(_$ResponseInfoImpl instance) =>
    <String, dynamic>{
      'api_id': instance.apiId,
      'ver': instance.ver,
      'ts': instance.ts,
      'res_msg_id': instance.resMsgId,
      'msg_id': instance.msgId,
      'status': instance.status,
    };

_$UserRequestImpl _$$UserRequestImplFromJson(Map<String, dynamic> json) =>
    _$UserRequestImpl(
      id: json['id'] as int?,
      uuid: json['uuid'] as String?,
      userName: json['userName'] as String?,
      name: json['name'] as String?,
      mobileNumber: json['mobileNumber'] as String?,
      emailId: json['emailId'] as String?,
      locale: json['locale'] as String?,
      type: json['type'] as String?,
      roles: (json['roles'] as List<dynamic>?)
          ?.map((e) => Role.fromJson(e as Map<String, dynamic>))
          .toList(),
      active: json['active'] as bool?,
      tenantId: json['tenantId'] as String?,
      permanentCity: json['permanentCity'] as String?,
    );

Map<String, dynamic> _$$UserRequestImplToJson(_$UserRequestImpl instance) =>
    <String, dynamic>{
      'id': instance.id,
      'uuid': instance.uuid,
      'userName': instance.userName,
      'name': instance.name,
      'mobileNumber': instance.mobileNumber,
      'emailId': instance.emailId,
      'locale': instance.locale,
      'type': instance.type,
      'roles': instance.roles,
      'active': instance.active,
      'tenantId': instance.tenantId,
      'permanentCity': instance.permanentCity,
    };
