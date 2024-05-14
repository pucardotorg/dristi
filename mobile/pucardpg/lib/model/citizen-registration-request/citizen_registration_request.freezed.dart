// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'citizen_registration_request.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

CitizenRegistrationRequest _$CitizenRegistrationRequestFromJson(
    Map<String, dynamic> json) {
  return _CitizenRegistrationRequest.fromJson(json);
}

/// @nodoc
mixin _$CitizenRegistrationRequest {
  @JsonKey(name: 'User')
  UserInfo get userInfo => throw _privateConstructorUsedError;
  @JsonKey(name: 'RequestInfo')
  RequestInfo get requestInfo => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $CitizenRegistrationRequestCopyWith<CitizenRegistrationRequest>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $CitizenRegistrationRequestCopyWith<$Res> {
  factory $CitizenRegistrationRequestCopyWith(CitizenRegistrationRequest value,
          $Res Function(CitizenRegistrationRequest) then) =
      _$CitizenRegistrationRequestCopyWithImpl<$Res,
          CitizenRegistrationRequest>;
  @useResult
  $Res call(
      {@JsonKey(name: 'User') UserInfo userInfo,
      @JsonKey(name: 'RequestInfo') RequestInfo requestInfo});

  $UserInfoCopyWith<$Res> get userInfo;
  $RequestInfoCopyWith<$Res> get requestInfo;
}

/// @nodoc
class _$CitizenRegistrationRequestCopyWithImpl<$Res,
        $Val extends CitizenRegistrationRequest>
    implements $CitizenRegistrationRequestCopyWith<$Res> {
  _$CitizenRegistrationRequestCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? userInfo = null,
    Object? requestInfo = null,
  }) {
    return _then(_value.copyWith(
      userInfo: null == userInfo
          ? _value.userInfo
          : userInfo // ignore: cast_nullable_to_non_nullable
              as UserInfo,
      requestInfo: null == requestInfo
          ? _value.requestInfo
          : requestInfo // ignore: cast_nullable_to_non_nullable
              as RequestInfo,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $UserInfoCopyWith<$Res> get userInfo {
    return $UserInfoCopyWith<$Res>(_value.userInfo, (value) {
      return _then(_value.copyWith(userInfo: value) as $Val);
    });
  }

  @override
  @pragma('vm:prefer-inline')
  $RequestInfoCopyWith<$Res> get requestInfo {
    return $RequestInfoCopyWith<$Res>(_value.requestInfo, (value) {
      return _then(_value.copyWith(requestInfo: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$CitizenRegistrationRequestImplCopyWith<$Res>
    implements $CitizenRegistrationRequestCopyWith<$Res> {
  factory _$$CitizenRegistrationRequestImplCopyWith(
          _$CitizenRegistrationRequestImpl value,
          $Res Function(_$CitizenRegistrationRequestImpl) then) =
      __$$CitizenRegistrationRequestImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'User') UserInfo userInfo,
      @JsonKey(name: 'RequestInfo') RequestInfo requestInfo});

  @override
  $UserInfoCopyWith<$Res> get userInfo;
  @override
  $RequestInfoCopyWith<$Res> get requestInfo;
}

/// @nodoc
class __$$CitizenRegistrationRequestImplCopyWithImpl<$Res>
    extends _$CitizenRegistrationRequestCopyWithImpl<$Res,
        _$CitizenRegistrationRequestImpl>
    implements _$$CitizenRegistrationRequestImplCopyWith<$Res> {
  __$$CitizenRegistrationRequestImplCopyWithImpl(
      _$CitizenRegistrationRequestImpl _value,
      $Res Function(_$CitizenRegistrationRequestImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? userInfo = null,
    Object? requestInfo = null,
  }) {
    return _then(_$CitizenRegistrationRequestImpl(
      userInfo: null == userInfo
          ? _value.userInfo
          : userInfo // ignore: cast_nullable_to_non_nullable
              as UserInfo,
      requestInfo: null == requestInfo
          ? _value.requestInfo
          : requestInfo // ignore: cast_nullable_to_non_nullable
              as RequestInfo,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$CitizenRegistrationRequestImpl implements _CitizenRegistrationRequest {
  const _$CitizenRegistrationRequestImpl(
      {@JsonKey(name: 'User') required this.userInfo,
      @JsonKey(name: 'RequestInfo') this.requestInfo = const RequestInfo()});

  factory _$CitizenRegistrationRequestImpl.fromJson(
          Map<String, dynamic> json) =>
      _$$CitizenRegistrationRequestImplFromJson(json);

  @override
  @JsonKey(name: 'User')
  final UserInfo userInfo;
  @override
  @JsonKey(name: 'RequestInfo')
  final RequestInfo requestInfo;

  @override
  String toString() {
    return 'CitizenRegistrationRequest(userInfo: $userInfo, requestInfo: $requestInfo)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$CitizenRegistrationRequestImpl &&
            (identical(other.userInfo, userInfo) ||
                other.userInfo == userInfo) &&
            (identical(other.requestInfo, requestInfo) ||
                other.requestInfo == requestInfo));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, userInfo, requestInfo);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$CitizenRegistrationRequestImplCopyWith<_$CitizenRegistrationRequestImpl>
      get copyWith => __$$CitizenRegistrationRequestImplCopyWithImpl<
          _$CitizenRegistrationRequestImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$CitizenRegistrationRequestImplToJson(
      this,
    );
  }
}

abstract class _CitizenRegistrationRequest
    implements CitizenRegistrationRequest {
  const factory _CitizenRegistrationRequest(
          {@JsonKey(name: 'User') required final UserInfo userInfo,
          @JsonKey(name: 'RequestInfo') final RequestInfo requestInfo}) =
      _$CitizenRegistrationRequestImpl;

  factory _CitizenRegistrationRequest.fromJson(Map<String, dynamic> json) =
      _$CitizenRegistrationRequestImpl.fromJson;

  @override
  @JsonKey(name: 'User')
  UserInfo get userInfo;
  @override
  @JsonKey(name: 'RequestInfo')
  RequestInfo get requestInfo;
  @override
  @JsonKey(ignore: true)
  _$$CitizenRegistrationRequestImplCopyWith<_$CitizenRegistrationRequestImpl>
      get copyWith => throw _privateConstructorUsedError;
}

UserInfo _$UserInfoFromJson(Map<String, dynamic> json) {
  return _UserInfo.fromJson(json);
}

/// @nodoc
mixin _$UserInfo {
  @JsonKey(name: 'name')
  String get name => throw _privateConstructorUsedError;
  @JsonKey(name: 'username')
  String get username => throw _privateConstructorUsedError;
  @JsonKey(name: 'otpReference')
  String get otpReference => throw _privateConstructorUsedError;
  @JsonKey(name: 'tenantId')
  String get tenantId => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $UserInfoCopyWith<UserInfo> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $UserInfoCopyWith<$Res> {
  factory $UserInfoCopyWith(UserInfo value, $Res Function(UserInfo) then) =
      _$UserInfoCopyWithImpl<$Res, UserInfo>;
  @useResult
  $Res call(
      {@JsonKey(name: 'name') String name,
      @JsonKey(name: 'username') String username,
      @JsonKey(name: 'otpReference') String otpReference,
      @JsonKey(name: 'tenantId') String tenantId});
}

/// @nodoc
class _$UserInfoCopyWithImpl<$Res, $Val extends UserInfo>
    implements $UserInfoCopyWith<$Res> {
  _$UserInfoCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? username = null,
    Object? otpReference = null,
    Object? tenantId = null,
  }) {
    return _then(_value.copyWith(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      username: null == username
          ? _value.username
          : username // ignore: cast_nullable_to_non_nullable
              as String,
      otpReference: null == otpReference
          ? _value.otpReference
          : otpReference // ignore: cast_nullable_to_non_nullable
              as String,
      tenantId: null == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$UserInfoImplCopyWith<$Res>
    implements $UserInfoCopyWith<$Res> {
  factory _$$UserInfoImplCopyWith(
          _$UserInfoImpl value, $Res Function(_$UserInfoImpl) then) =
      __$$UserInfoImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'name') String name,
      @JsonKey(name: 'username') String username,
      @JsonKey(name: 'otpReference') String otpReference,
      @JsonKey(name: 'tenantId') String tenantId});
}

/// @nodoc
class __$$UserInfoImplCopyWithImpl<$Res>
    extends _$UserInfoCopyWithImpl<$Res, _$UserInfoImpl>
    implements _$$UserInfoImplCopyWith<$Res> {
  __$$UserInfoImplCopyWithImpl(
      _$UserInfoImpl _value, $Res Function(_$UserInfoImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? username = null,
    Object? otpReference = null,
    Object? tenantId = null,
  }) {
    return _then(_$UserInfoImpl(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      username: null == username
          ? _value.username
          : username // ignore: cast_nullable_to_non_nullable
              as String,
      otpReference: null == otpReference
          ? _value.otpReference
          : otpReference // ignore: cast_nullable_to_non_nullable
              as String,
      tenantId: null == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$UserInfoImpl implements _UserInfo {
  const _$UserInfoImpl(
      {@JsonKey(name: 'name') this.name = "dristi",
      @JsonKey(name: 'username') required this.username,
      @JsonKey(name: 'otpReference') required this.otpReference,
      @JsonKey(name: 'tenantId') this.tenantId = "pg"});

  factory _$UserInfoImpl.fromJson(Map<String, dynamic> json) =>
      _$$UserInfoImplFromJson(json);

  @override
  @JsonKey(name: 'name')
  final String name;
  @override
  @JsonKey(name: 'username')
  final String username;
  @override
  @JsonKey(name: 'otpReference')
  final String otpReference;
  @override
  @JsonKey(name: 'tenantId')
  final String tenantId;

  @override
  String toString() {
    return 'UserInfo(name: $name, username: $username, otpReference: $otpReference, tenantId: $tenantId)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$UserInfoImpl &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.username, username) ||
                other.username == username) &&
            (identical(other.otpReference, otpReference) ||
                other.otpReference == otpReference) &&
            (identical(other.tenantId, tenantId) ||
                other.tenantId == tenantId));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, name, username, otpReference, tenantId);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$UserInfoImplCopyWith<_$UserInfoImpl> get copyWith =>
      __$$UserInfoImplCopyWithImpl<_$UserInfoImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$UserInfoImplToJson(
      this,
    );
  }
}

abstract class _UserInfo implements UserInfo {
  const factory _UserInfo(
      {@JsonKey(name: 'name') final String name,
      @JsonKey(name: 'username') required final String username,
      @JsonKey(name: 'otpReference') required final String otpReference,
      @JsonKey(name: 'tenantId') final String tenantId}) = _$UserInfoImpl;

  factory _UserInfo.fromJson(Map<String, dynamic> json) =
      _$UserInfoImpl.fromJson;

  @override
  @JsonKey(name: 'name')
  String get name;
  @override
  @JsonKey(name: 'username')
  String get username;
  @override
  @JsonKey(name: 'otpReference')
  String get otpReference;
  @override
  @JsonKey(name: 'tenantId')
  String get tenantId;
  @override
  @JsonKey(ignore: true)
  _$$UserInfoImplCopyWith<_$UserInfoImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
