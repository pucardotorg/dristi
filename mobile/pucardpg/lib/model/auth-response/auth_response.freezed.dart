// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'auth_response.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

AuthResponse _$AuthResponseFromJson(Map<String, dynamic> json) {
  return _AuthResponse.fromJson(json);
}

/// @nodoc
mixin _$AuthResponse {
  @JsonKey(name: 'access_token')
  String? get accessToken => throw _privateConstructorUsedError;
  @JsonKey(name: 'token_type')
  String? get tokenType => throw _privateConstructorUsedError;
  @JsonKey(name: 'refresh_token')
  String? get refreshToken => throw _privateConstructorUsedError;
  @JsonKey(name: 'expires_in')
  int? get expiresIn => throw _privateConstructorUsedError;
  @JsonKey(name: 'scope')
  String? get scope => throw _privateConstructorUsedError;
  @JsonKey(name: 'ResponseInfo')
  ResponseInfo? get responseInfo => throw _privateConstructorUsedError;
  @JsonKey(name: 'UserRequest')
  UserRequest? get userRequest => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AuthResponseCopyWith<AuthResponse> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AuthResponseCopyWith<$Res> {
  factory $AuthResponseCopyWith(
          AuthResponse value, $Res Function(AuthResponse) then) =
      _$AuthResponseCopyWithImpl<$Res, AuthResponse>;
  @useResult
  $Res call(
      {@JsonKey(name: 'access_token') String? accessToken,
      @JsonKey(name: 'token_type') String? tokenType,
      @JsonKey(name: 'refresh_token') String? refreshToken,
      @JsonKey(name: 'expires_in') int? expiresIn,
      @JsonKey(name: 'scope') String? scope,
      @JsonKey(name: 'ResponseInfo') ResponseInfo? responseInfo,
      @JsonKey(name: 'UserRequest') UserRequest? userRequest});

  $ResponseInfoCopyWith<$Res>? get responseInfo;
  $UserRequestCopyWith<$Res>? get userRequest;
}

/// @nodoc
class _$AuthResponseCopyWithImpl<$Res, $Val extends AuthResponse>
    implements $AuthResponseCopyWith<$Res> {
  _$AuthResponseCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? accessToken = freezed,
    Object? tokenType = freezed,
    Object? refreshToken = freezed,
    Object? expiresIn = freezed,
    Object? scope = freezed,
    Object? responseInfo = freezed,
    Object? userRequest = freezed,
  }) {
    return _then(_value.copyWith(
      accessToken: freezed == accessToken
          ? _value.accessToken
          : accessToken // ignore: cast_nullable_to_non_nullable
              as String?,
      tokenType: freezed == tokenType
          ? _value.tokenType
          : tokenType // ignore: cast_nullable_to_non_nullable
              as String?,
      refreshToken: freezed == refreshToken
          ? _value.refreshToken
          : refreshToken // ignore: cast_nullable_to_non_nullable
              as String?,
      expiresIn: freezed == expiresIn
          ? _value.expiresIn
          : expiresIn // ignore: cast_nullable_to_non_nullable
              as int?,
      scope: freezed == scope
          ? _value.scope
          : scope // ignore: cast_nullable_to_non_nullable
              as String?,
      responseInfo: freezed == responseInfo
          ? _value.responseInfo
          : responseInfo // ignore: cast_nullable_to_non_nullable
              as ResponseInfo?,
      userRequest: freezed == userRequest
          ? _value.userRequest
          : userRequest // ignore: cast_nullable_to_non_nullable
              as UserRequest?,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $ResponseInfoCopyWith<$Res>? get responseInfo {
    if (_value.responseInfo == null) {
      return null;
    }

    return $ResponseInfoCopyWith<$Res>(_value.responseInfo!, (value) {
      return _then(_value.copyWith(responseInfo: value) as $Val);
    });
  }

  @override
  @pragma('vm:prefer-inline')
  $UserRequestCopyWith<$Res>? get userRequest {
    if (_value.userRequest == null) {
      return null;
    }

    return $UserRequestCopyWith<$Res>(_value.userRequest!, (value) {
      return _then(_value.copyWith(userRequest: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$AuthResponseImplCopyWith<$Res>
    implements $AuthResponseCopyWith<$Res> {
  factory _$$AuthResponseImplCopyWith(
          _$AuthResponseImpl value, $Res Function(_$AuthResponseImpl) then) =
      __$$AuthResponseImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'access_token') String? accessToken,
      @JsonKey(name: 'token_type') String? tokenType,
      @JsonKey(name: 'refresh_token') String? refreshToken,
      @JsonKey(name: 'expires_in') int? expiresIn,
      @JsonKey(name: 'scope') String? scope,
      @JsonKey(name: 'ResponseInfo') ResponseInfo? responseInfo,
      @JsonKey(name: 'UserRequest') UserRequest? userRequest});

  @override
  $ResponseInfoCopyWith<$Res>? get responseInfo;
  @override
  $UserRequestCopyWith<$Res>? get userRequest;
}

/// @nodoc
class __$$AuthResponseImplCopyWithImpl<$Res>
    extends _$AuthResponseCopyWithImpl<$Res, _$AuthResponseImpl>
    implements _$$AuthResponseImplCopyWith<$Res> {
  __$$AuthResponseImplCopyWithImpl(
      _$AuthResponseImpl _value, $Res Function(_$AuthResponseImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? accessToken = freezed,
    Object? tokenType = freezed,
    Object? refreshToken = freezed,
    Object? expiresIn = freezed,
    Object? scope = freezed,
    Object? responseInfo = freezed,
    Object? userRequest = freezed,
  }) {
    return _then(_$AuthResponseImpl(
      accessToken: freezed == accessToken
          ? _value.accessToken
          : accessToken // ignore: cast_nullable_to_non_nullable
              as String?,
      tokenType: freezed == tokenType
          ? _value.tokenType
          : tokenType // ignore: cast_nullable_to_non_nullable
              as String?,
      refreshToken: freezed == refreshToken
          ? _value.refreshToken
          : refreshToken // ignore: cast_nullable_to_non_nullable
              as String?,
      expiresIn: freezed == expiresIn
          ? _value.expiresIn
          : expiresIn // ignore: cast_nullable_to_non_nullable
              as int?,
      scope: freezed == scope
          ? _value.scope
          : scope // ignore: cast_nullable_to_non_nullable
              as String?,
      responseInfo: freezed == responseInfo
          ? _value.responseInfo
          : responseInfo // ignore: cast_nullable_to_non_nullable
              as ResponseInfo?,
      userRequest: freezed == userRequest
          ? _value.userRequest
          : userRequest // ignore: cast_nullable_to_non_nullable
              as UserRequest?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AuthResponseImpl implements _AuthResponse {
  const _$AuthResponseImpl(
      {@JsonKey(name: 'access_token') required this.accessToken,
      @JsonKey(name: 'token_type') required this.tokenType,
      @JsonKey(name: 'refresh_token') required this.refreshToken,
      @JsonKey(name: 'expires_in') required this.expiresIn,
      @JsonKey(name: 'scope') required this.scope,
      @JsonKey(name: 'ResponseInfo') this.responseInfo,
      @JsonKey(name: 'UserRequest') this.userRequest});

  factory _$AuthResponseImpl.fromJson(Map<String, dynamic> json) =>
      _$$AuthResponseImplFromJson(json);

  @override
  @JsonKey(name: 'access_token')
  final String? accessToken;
  @override
  @JsonKey(name: 'token_type')
  final String? tokenType;
  @override
  @JsonKey(name: 'refresh_token')
  final String? refreshToken;
  @override
  @JsonKey(name: 'expires_in')
  final int? expiresIn;
  @override
  @JsonKey(name: 'scope')
  final String? scope;
  @override
  @JsonKey(name: 'ResponseInfo')
  final ResponseInfo? responseInfo;
  @override
  @JsonKey(name: 'UserRequest')
  final UserRequest? userRequest;

  @override
  String toString() {
    return 'AuthResponse(accessToken: $accessToken, tokenType: $tokenType, refreshToken: $refreshToken, expiresIn: $expiresIn, scope: $scope, responseInfo: $responseInfo, userRequest: $userRequest)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AuthResponseImpl &&
            (identical(other.accessToken, accessToken) ||
                other.accessToken == accessToken) &&
            (identical(other.tokenType, tokenType) ||
                other.tokenType == tokenType) &&
            (identical(other.refreshToken, refreshToken) ||
                other.refreshToken == refreshToken) &&
            (identical(other.expiresIn, expiresIn) ||
                other.expiresIn == expiresIn) &&
            (identical(other.scope, scope) || other.scope == scope) &&
            (identical(other.responseInfo, responseInfo) ||
                other.responseInfo == responseInfo) &&
            (identical(other.userRequest, userRequest) ||
                other.userRequest == userRequest));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, accessToken, tokenType,
      refreshToken, expiresIn, scope, responseInfo, userRequest);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AuthResponseImplCopyWith<_$AuthResponseImpl> get copyWith =>
      __$$AuthResponseImplCopyWithImpl<_$AuthResponseImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AuthResponseImplToJson(
      this,
    );
  }
}

abstract class _AuthResponse implements AuthResponse {
  const factory _AuthResponse(
          {@JsonKey(name: 'access_token') required final String? accessToken,
          @JsonKey(name: 'token_type') required final String? tokenType,
          @JsonKey(name: 'refresh_token') required final String? refreshToken,
          @JsonKey(name: 'expires_in') required final int? expiresIn,
          @JsonKey(name: 'scope') required final String? scope,
          @JsonKey(name: 'ResponseInfo') final ResponseInfo? responseInfo,
          @JsonKey(name: 'UserRequest') final UserRequest? userRequest}) =
      _$AuthResponseImpl;

  factory _AuthResponse.fromJson(Map<String, dynamic> json) =
      _$AuthResponseImpl.fromJson;

  @override
  @JsonKey(name: 'access_token')
  String? get accessToken;
  @override
  @JsonKey(name: 'token_type')
  String? get tokenType;
  @override
  @JsonKey(name: 'refresh_token')
  String? get refreshToken;
  @override
  @JsonKey(name: 'expires_in')
  int? get expiresIn;
  @override
  @JsonKey(name: 'scope')
  String? get scope;
  @override
  @JsonKey(name: 'ResponseInfo')
  ResponseInfo? get responseInfo;
  @override
  @JsonKey(name: 'UserRequest')
  UserRequest? get userRequest;
  @override
  @JsonKey(ignore: true)
  _$$AuthResponseImplCopyWith<_$AuthResponseImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

ResponseInfo _$ResponseInfoFromJson(Map<String, dynamic> json) {
  return _ResponseInfo.fromJson(json);
}

/// @nodoc
mixin _$ResponseInfo {
  @JsonKey(name: 'api_id')
  String? get apiId =>
      throw _privateConstructorUsedError; // Make nullable since it's empty in the example JSON
  String? get ver => throw _privateConstructorUsedError; // Make nullable
  String? get ts => throw _privateConstructorUsedError; // Make nullable
  @JsonKey(name: 'res_msg_id')
  String? get resMsgId => throw _privateConstructorUsedError; // Make nullable
  @JsonKey(name: 'msg_id')
  String? get msgId => throw _privateConstructorUsedError; // Make nullable
  String? get status => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $ResponseInfoCopyWith<ResponseInfo> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $ResponseInfoCopyWith<$Res> {
  factory $ResponseInfoCopyWith(
          ResponseInfo value, $Res Function(ResponseInfo) then) =
      _$ResponseInfoCopyWithImpl<$Res, ResponseInfo>;
  @useResult
  $Res call(
      {@JsonKey(name: 'api_id') String? apiId,
      String? ver,
      String? ts,
      @JsonKey(name: 'res_msg_id') String? resMsgId,
      @JsonKey(name: 'msg_id') String? msgId,
      String? status});
}

/// @nodoc
class _$ResponseInfoCopyWithImpl<$Res, $Val extends ResponseInfo>
    implements $ResponseInfoCopyWith<$Res> {
  _$ResponseInfoCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? apiId = freezed,
    Object? ver = freezed,
    Object? ts = freezed,
    Object? resMsgId = freezed,
    Object? msgId = freezed,
    Object? status = freezed,
  }) {
    return _then(_value.copyWith(
      apiId: freezed == apiId
          ? _value.apiId
          : apiId // ignore: cast_nullable_to_non_nullable
              as String?,
      ver: freezed == ver
          ? _value.ver
          : ver // ignore: cast_nullable_to_non_nullable
              as String?,
      ts: freezed == ts
          ? _value.ts
          : ts // ignore: cast_nullable_to_non_nullable
              as String?,
      resMsgId: freezed == resMsgId
          ? _value.resMsgId
          : resMsgId // ignore: cast_nullable_to_non_nullable
              as String?,
      msgId: freezed == msgId
          ? _value.msgId
          : msgId // ignore: cast_nullable_to_non_nullable
              as String?,
      status: freezed == status
          ? _value.status
          : status // ignore: cast_nullable_to_non_nullable
              as String?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$ResponseInfoImplCopyWith<$Res>
    implements $ResponseInfoCopyWith<$Res> {
  factory _$$ResponseInfoImplCopyWith(
          _$ResponseInfoImpl value, $Res Function(_$ResponseInfoImpl) then) =
      __$$ResponseInfoImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'api_id') String? apiId,
      String? ver,
      String? ts,
      @JsonKey(name: 'res_msg_id') String? resMsgId,
      @JsonKey(name: 'msg_id') String? msgId,
      String? status});
}

/// @nodoc
class __$$ResponseInfoImplCopyWithImpl<$Res>
    extends _$ResponseInfoCopyWithImpl<$Res, _$ResponseInfoImpl>
    implements _$$ResponseInfoImplCopyWith<$Res> {
  __$$ResponseInfoImplCopyWithImpl(
      _$ResponseInfoImpl _value, $Res Function(_$ResponseInfoImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? apiId = freezed,
    Object? ver = freezed,
    Object? ts = freezed,
    Object? resMsgId = freezed,
    Object? msgId = freezed,
    Object? status = freezed,
  }) {
    return _then(_$ResponseInfoImpl(
      apiId: freezed == apiId
          ? _value.apiId
          : apiId // ignore: cast_nullable_to_non_nullable
              as String?,
      ver: freezed == ver
          ? _value.ver
          : ver // ignore: cast_nullable_to_non_nullable
              as String?,
      ts: freezed == ts
          ? _value.ts
          : ts // ignore: cast_nullable_to_non_nullable
              as String?,
      resMsgId: freezed == resMsgId
          ? _value.resMsgId
          : resMsgId // ignore: cast_nullable_to_non_nullable
              as String?,
      msgId: freezed == msgId
          ? _value.msgId
          : msgId // ignore: cast_nullable_to_non_nullable
              as String?,
      status: freezed == status
          ? _value.status
          : status // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$ResponseInfoImpl implements _ResponseInfo {
  const _$ResponseInfoImpl(
      {@JsonKey(name: 'api_id') this.apiId,
      this.ver,
      this.ts,
      @JsonKey(name: 'res_msg_id') this.resMsgId,
      @JsonKey(name: 'msg_id') this.msgId,
      this.status});

  factory _$ResponseInfoImpl.fromJson(Map<String, dynamic> json) =>
      _$$ResponseInfoImplFromJson(json);

  @override
  @JsonKey(name: 'api_id')
  final String? apiId;
// Make nullable since it's empty in the example JSON
  @override
  final String? ver;
// Make nullable
  @override
  final String? ts;
// Make nullable
  @override
  @JsonKey(name: 'res_msg_id')
  final String? resMsgId;
// Make nullable
  @override
  @JsonKey(name: 'msg_id')
  final String? msgId;
// Make nullable
  @override
  final String? status;

  @override
  String toString() {
    return 'ResponseInfo(apiId: $apiId, ver: $ver, ts: $ts, resMsgId: $resMsgId, msgId: $msgId, status: $status)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ResponseInfoImpl &&
            (identical(other.apiId, apiId) || other.apiId == apiId) &&
            (identical(other.ver, ver) || other.ver == ver) &&
            (identical(other.ts, ts) || other.ts == ts) &&
            (identical(other.resMsgId, resMsgId) ||
                other.resMsgId == resMsgId) &&
            (identical(other.msgId, msgId) || other.msgId == msgId) &&
            (identical(other.status, status) || other.status == status));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, apiId, ver, ts, resMsgId, msgId, status);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$ResponseInfoImplCopyWith<_$ResponseInfoImpl> get copyWith =>
      __$$ResponseInfoImplCopyWithImpl<_$ResponseInfoImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$ResponseInfoImplToJson(
      this,
    );
  }
}

abstract class _ResponseInfo implements ResponseInfo {
  const factory _ResponseInfo(
      {@JsonKey(name: 'api_id') final String? apiId,
      final String? ver,
      final String? ts,
      @JsonKey(name: 'res_msg_id') final String? resMsgId,
      @JsonKey(name: 'msg_id') final String? msgId,
      final String? status}) = _$ResponseInfoImpl;

  factory _ResponseInfo.fromJson(Map<String, dynamic> json) =
      _$ResponseInfoImpl.fromJson;

  @override
  @JsonKey(name: 'api_id')
  String? get apiId;
  @override // Make nullable since it's empty in the example JSON
  String? get ver;
  @override // Make nullable
  String? get ts;
  @override // Make nullable
  @JsonKey(name: 'res_msg_id')
  String? get resMsgId;
  @override // Make nullable
  @JsonKey(name: 'msg_id')
  String? get msgId;
  @override // Make nullable
  String? get status;
  @override
  @JsonKey(ignore: true)
  _$$ResponseInfoImplCopyWith<_$ResponseInfoImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

UserRequest _$UserRequestFromJson(Map<String, dynamic> json) {
  return _UserRequest.fromJson(json);
}

/// @nodoc
mixin _$UserRequest {
  int? get id => throw _privateConstructorUsedError;
  String? get uuid => throw _privateConstructorUsedError;
  String? get userName => throw _privateConstructorUsedError;
  String? get name => throw _privateConstructorUsedError;
  String? get mobileNumber => throw _privateConstructorUsedError;
  String? get emailId => throw _privateConstructorUsedError; // Make nullable
  String? get locale => throw _privateConstructorUsedError; // Make nullable
  String? get type => throw _privateConstructorUsedError;
  @JsonKey(name: 'roles')
  List<Role>? get roles => throw _privateConstructorUsedError;
  bool? get active => throw _privateConstructorUsedError;
  String? get tenantId => throw _privateConstructorUsedError;
  String? get permanentCity => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $UserRequestCopyWith<UserRequest> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $UserRequestCopyWith<$Res> {
  factory $UserRequestCopyWith(
          UserRequest value, $Res Function(UserRequest) then) =
      _$UserRequestCopyWithImpl<$Res, UserRequest>;
  @useResult
  $Res call(
      {int? id,
      String? uuid,
      String? userName,
      String? name,
      String? mobileNumber,
      String? emailId,
      String? locale,
      String? type,
      @JsonKey(name: 'roles') List<Role>? roles,
      bool? active,
      String? tenantId,
      String? permanentCity});
}

/// @nodoc
class _$UserRequestCopyWithImpl<$Res, $Val extends UserRequest>
    implements $UserRequestCopyWith<$Res> {
  _$UserRequestCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = freezed,
    Object? uuid = freezed,
    Object? userName = freezed,
    Object? name = freezed,
    Object? mobileNumber = freezed,
    Object? emailId = freezed,
    Object? locale = freezed,
    Object? type = freezed,
    Object? roles = freezed,
    Object? active = freezed,
    Object? tenantId = freezed,
    Object? permanentCity = freezed,
  }) {
    return _then(_value.copyWith(
      id: freezed == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as int?,
      uuid: freezed == uuid
          ? _value.uuid
          : uuid // ignore: cast_nullable_to_non_nullable
              as String?,
      userName: freezed == userName
          ? _value.userName
          : userName // ignore: cast_nullable_to_non_nullable
              as String?,
      name: freezed == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String?,
      mobileNumber: freezed == mobileNumber
          ? _value.mobileNumber
          : mobileNumber // ignore: cast_nullable_to_non_nullable
              as String?,
      emailId: freezed == emailId
          ? _value.emailId
          : emailId // ignore: cast_nullable_to_non_nullable
              as String?,
      locale: freezed == locale
          ? _value.locale
          : locale // ignore: cast_nullable_to_non_nullable
              as String?,
      type: freezed == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String?,
      roles: freezed == roles
          ? _value.roles
          : roles // ignore: cast_nullable_to_non_nullable
              as List<Role>?,
      active: freezed == active
          ? _value.active
          : active // ignore: cast_nullable_to_non_nullable
              as bool?,
      tenantId: freezed == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String?,
      permanentCity: freezed == permanentCity
          ? _value.permanentCity
          : permanentCity // ignore: cast_nullable_to_non_nullable
              as String?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$UserRequestImplCopyWith<$Res>
    implements $UserRequestCopyWith<$Res> {
  factory _$$UserRequestImplCopyWith(
          _$UserRequestImpl value, $Res Function(_$UserRequestImpl) then) =
      __$$UserRequestImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {int? id,
      String? uuid,
      String? userName,
      String? name,
      String? mobileNumber,
      String? emailId,
      String? locale,
      String? type,
      @JsonKey(name: 'roles') List<Role>? roles,
      bool? active,
      String? tenantId,
      String? permanentCity});
}

/// @nodoc
class __$$UserRequestImplCopyWithImpl<$Res>
    extends _$UserRequestCopyWithImpl<$Res, _$UserRequestImpl>
    implements _$$UserRequestImplCopyWith<$Res> {
  __$$UserRequestImplCopyWithImpl(
      _$UserRequestImpl _value, $Res Function(_$UserRequestImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = freezed,
    Object? uuid = freezed,
    Object? userName = freezed,
    Object? name = freezed,
    Object? mobileNumber = freezed,
    Object? emailId = freezed,
    Object? locale = freezed,
    Object? type = freezed,
    Object? roles = freezed,
    Object? active = freezed,
    Object? tenantId = freezed,
    Object? permanentCity = freezed,
  }) {
    return _then(_$UserRequestImpl(
      id: freezed == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as int?,
      uuid: freezed == uuid
          ? _value.uuid
          : uuid // ignore: cast_nullable_to_non_nullable
              as String?,
      userName: freezed == userName
          ? _value.userName
          : userName // ignore: cast_nullable_to_non_nullable
              as String?,
      name: freezed == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String?,
      mobileNumber: freezed == mobileNumber
          ? _value.mobileNumber
          : mobileNumber // ignore: cast_nullable_to_non_nullable
              as String?,
      emailId: freezed == emailId
          ? _value.emailId
          : emailId // ignore: cast_nullable_to_non_nullable
              as String?,
      locale: freezed == locale
          ? _value.locale
          : locale // ignore: cast_nullable_to_non_nullable
              as String?,
      type: freezed == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String?,
      roles: freezed == roles
          ? _value._roles
          : roles // ignore: cast_nullable_to_non_nullable
              as List<Role>?,
      active: freezed == active
          ? _value.active
          : active // ignore: cast_nullable_to_non_nullable
              as bool?,
      tenantId: freezed == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String?,
      permanentCity: freezed == permanentCity
          ? _value.permanentCity
          : permanentCity // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$UserRequestImpl implements _UserRequest {
  const _$UserRequestImpl(
      {this.id,
      this.uuid,
      this.userName,
      this.name,
      this.mobileNumber,
      this.emailId,
      this.locale,
      this.type,
      @JsonKey(name: 'roles') final List<Role>? roles,
      this.active,
      this.tenantId,
      this.permanentCity})
      : _roles = roles;

  factory _$UserRequestImpl.fromJson(Map<String, dynamic> json) =>
      _$$UserRequestImplFromJson(json);

  @override
  final int? id;
  @override
  final String? uuid;
  @override
  final String? userName;
  @override
  final String? name;
  @override
  final String? mobileNumber;
  @override
  final String? emailId;
// Make nullable
  @override
  final String? locale;
// Make nullable
  @override
  final String? type;
  final List<Role>? _roles;
  @override
  @JsonKey(name: 'roles')
  List<Role>? get roles {
    final value = _roles;
    if (value == null) return null;
    if (_roles is EqualUnmodifiableListView) return _roles;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(value);
  }

  @override
  final bool? active;
  @override
  final String? tenantId;
  @override
  final String? permanentCity;

  @override
  String toString() {
    return 'UserRequest(id: $id, uuid: $uuid, userName: $userName, name: $name, mobileNumber: $mobileNumber, emailId: $emailId, locale: $locale, type: $type, roles: $roles, active: $active, tenantId: $tenantId, permanentCity: $permanentCity)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$UserRequestImpl &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.uuid, uuid) || other.uuid == uuid) &&
            (identical(other.userName, userName) ||
                other.userName == userName) &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.mobileNumber, mobileNumber) ||
                other.mobileNumber == mobileNumber) &&
            (identical(other.emailId, emailId) || other.emailId == emailId) &&
            (identical(other.locale, locale) || other.locale == locale) &&
            (identical(other.type, type) || other.type == type) &&
            const DeepCollectionEquality().equals(other._roles, _roles) &&
            (identical(other.active, active) || other.active == active) &&
            (identical(other.tenantId, tenantId) ||
                other.tenantId == tenantId) &&
            (identical(other.permanentCity, permanentCity) ||
                other.permanentCity == permanentCity));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      id,
      uuid,
      userName,
      name,
      mobileNumber,
      emailId,
      locale,
      type,
      const DeepCollectionEquality().hash(_roles),
      active,
      tenantId,
      permanentCity);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$UserRequestImplCopyWith<_$UserRequestImpl> get copyWith =>
      __$$UserRequestImplCopyWithImpl<_$UserRequestImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$UserRequestImplToJson(
      this,
    );
  }
}

abstract class _UserRequest implements UserRequest {
  const factory _UserRequest(
      {final int? id,
      final String? uuid,
      final String? userName,
      final String? name,
      final String? mobileNumber,
      final String? emailId,
      final String? locale,
      final String? type,
      @JsonKey(name: 'roles') final List<Role>? roles,
      final bool? active,
      final String? tenantId,
      final String? permanentCity}) = _$UserRequestImpl;

  factory _UserRequest.fromJson(Map<String, dynamic> json) =
      _$UserRequestImpl.fromJson;

  @override
  int? get id;
  @override
  String? get uuid;
  @override
  String? get userName;
  @override
  String? get name;
  @override
  String? get mobileNumber;
  @override
  String? get emailId;
  @override // Make nullable
  String? get locale;
  @override // Make nullable
  String? get type;
  @override
  @JsonKey(name: 'roles')
  List<Role>? get roles;
  @override
  bool? get active;
  @override
  String? get tenantId;
  @override
  String? get permanentCity;
  @override
  @JsonKey(ignore: true)
  _$$UserRequestImplCopyWith<_$UserRequestImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
