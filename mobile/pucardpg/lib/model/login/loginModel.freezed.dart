// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'loginModel.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

LoginModel _$LoginModelFromJson(Map<String, dynamic> json) {
  return _LoginModel.fromJson(json);
}

/// @nodoc
mixin _$LoginModel {
  String? get username => throw _privateConstructorUsedError;
  String? get password => throw _privateConstructorUsedError;
  String? get tenantId => throw _privateConstructorUsedError;
  String? get userType => throw _privateConstructorUsedError;
  String? get scope => throw _privateConstructorUsedError;
  @JsonKey(name: "refresh_token")
  String? get refreshToken => throw _privateConstructorUsedError;
  @JsonKey(name: 'grant_type')
  String? get grantType => throw _privateConstructorUsedError;
  @JsonKey(name: '_')
  int get timeStamp => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $LoginModelCopyWith<LoginModel> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $LoginModelCopyWith<$Res> {
  factory $LoginModelCopyWith(
          LoginModel value, $Res Function(LoginModel) then) =
      _$LoginModelCopyWithImpl<$Res, LoginModel>;
  @useResult
  $Res call(
      {String? username,
      String? password,
      String? tenantId,
      String? userType,
      String? scope,
      @JsonKey(name: "refresh_token") String? refreshToken,
      @JsonKey(name: 'grant_type') String? grantType,
      @JsonKey(name: '_') int timeStamp});
}

/// @nodoc
class _$LoginModelCopyWithImpl<$Res, $Val extends LoginModel>
    implements $LoginModelCopyWith<$Res> {
  _$LoginModelCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? username = freezed,
    Object? password = freezed,
    Object? tenantId = freezed,
    Object? userType = freezed,
    Object? scope = freezed,
    Object? refreshToken = freezed,
    Object? grantType = freezed,
    Object? timeStamp = null,
  }) {
    return _then(_value.copyWith(
      username: freezed == username
          ? _value.username
          : username // ignore: cast_nullable_to_non_nullable
              as String?,
      password: freezed == password
          ? _value.password
          : password // ignore: cast_nullable_to_non_nullable
              as String?,
      tenantId: freezed == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String?,
      userType: freezed == userType
          ? _value.userType
          : userType // ignore: cast_nullable_to_non_nullable
              as String?,
      scope: freezed == scope
          ? _value.scope
          : scope // ignore: cast_nullable_to_non_nullable
              as String?,
      refreshToken: freezed == refreshToken
          ? _value.refreshToken
          : refreshToken // ignore: cast_nullable_to_non_nullable
              as String?,
      grantType: freezed == grantType
          ? _value.grantType
          : grantType // ignore: cast_nullable_to_non_nullable
              as String?,
      timeStamp: null == timeStamp
          ? _value.timeStamp
          : timeStamp // ignore: cast_nullable_to_non_nullable
              as int,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$LoginModelImplCopyWith<$Res>
    implements $LoginModelCopyWith<$Res> {
  factory _$$LoginModelImplCopyWith(
          _$LoginModelImpl value, $Res Function(_$LoginModelImpl) then) =
      __$$LoginModelImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String? username,
      String? password,
      String? tenantId,
      String? userType,
      String? scope,
      @JsonKey(name: "refresh_token") String? refreshToken,
      @JsonKey(name: 'grant_type') String? grantType,
      @JsonKey(name: '_') int timeStamp});
}

/// @nodoc
class __$$LoginModelImplCopyWithImpl<$Res>
    extends _$LoginModelCopyWithImpl<$Res, _$LoginModelImpl>
    implements _$$LoginModelImplCopyWith<$Res> {
  __$$LoginModelImplCopyWithImpl(
      _$LoginModelImpl _value, $Res Function(_$LoginModelImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? username = freezed,
    Object? password = freezed,
    Object? tenantId = freezed,
    Object? userType = freezed,
    Object? scope = freezed,
    Object? refreshToken = freezed,
    Object? grantType = freezed,
    Object? timeStamp = null,
  }) {
    return _then(_$LoginModelImpl(
      username: freezed == username
          ? _value.username
          : username // ignore: cast_nullable_to_non_nullable
              as String?,
      password: freezed == password
          ? _value.password
          : password // ignore: cast_nullable_to_non_nullable
              as String?,
      tenantId: freezed == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String?,
      userType: freezed == userType
          ? _value.userType
          : userType // ignore: cast_nullable_to_non_nullable
              as String?,
      scope: freezed == scope
          ? _value.scope
          : scope // ignore: cast_nullable_to_non_nullable
              as String?,
      refreshToken: freezed == refreshToken
          ? _value.refreshToken
          : refreshToken // ignore: cast_nullable_to_non_nullable
              as String?,
      grantType: freezed == grantType
          ? _value.grantType
          : grantType // ignore: cast_nullable_to_non_nullable
              as String?,
      timeStamp: null == timeStamp
          ? _value.timeStamp
          : timeStamp // ignore: cast_nullable_to_non_nullable
              as int,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$LoginModelImpl implements _LoginModel {
  const _$LoginModelImpl(
      {required this.username,
      this.password = "123456",
      this.tenantId = "pg",
      this.userType = "citizen",
      this.scope = "read",
      @JsonKey(name: "refresh_token") this.refreshToken,
      @JsonKey(name: 'grant_type') this.grantType,
      @JsonKey(name: '_') this.timeStamp = 1713357247536});

  factory _$LoginModelImpl.fromJson(Map<String, dynamic> json) =>
      _$$LoginModelImplFromJson(json);

  @override
  final String? username;
  @override
  @JsonKey()
  final String? password;
  @override
  @JsonKey()
  final String? tenantId;
  @override
  @JsonKey()
  final String? userType;
  @override
  @JsonKey()
  final String? scope;
  @override
  @JsonKey(name: "refresh_token")
  final String? refreshToken;
  @override
  @JsonKey(name: 'grant_type')
  final String? grantType;
  @override
  @JsonKey(name: '_')
  final int timeStamp;

  @override
  String toString() {
    return 'LoginModel(username: $username, password: $password, tenantId: $tenantId, userType: $userType, scope: $scope, refreshToken: $refreshToken, grantType: $grantType, timeStamp: $timeStamp)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$LoginModelImpl &&
            (identical(other.username, username) ||
                other.username == username) &&
            (identical(other.password, password) ||
                other.password == password) &&
            (identical(other.tenantId, tenantId) ||
                other.tenantId == tenantId) &&
            (identical(other.userType, userType) ||
                other.userType == userType) &&
            (identical(other.scope, scope) || other.scope == scope) &&
            (identical(other.refreshToken, refreshToken) ||
                other.refreshToken == refreshToken) &&
            (identical(other.grantType, grantType) ||
                other.grantType == grantType) &&
            (identical(other.timeStamp, timeStamp) ||
                other.timeStamp == timeStamp));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, username, password, tenantId,
      userType, scope, refreshToken, grantType, timeStamp);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$LoginModelImplCopyWith<_$LoginModelImpl> get copyWith =>
      __$$LoginModelImplCopyWithImpl<_$LoginModelImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$LoginModelImplToJson(
      this,
    );
  }
}

abstract class _LoginModel implements LoginModel {
  const factory _LoginModel(
      {required final String? username,
      final String? password,
      final String? tenantId,
      final String? userType,
      final String? scope,
      @JsonKey(name: "refresh_token") final String? refreshToken,
      @JsonKey(name: 'grant_type') final String? grantType,
      @JsonKey(name: '_') final int timeStamp}) = _$LoginModelImpl;

  factory _LoginModel.fromJson(Map<String, dynamic> json) =
      _$LoginModelImpl.fromJson;

  @override
  String? get username;
  @override
  String? get password;
  @override
  String? get tenantId;
  @override
  String? get userType;
  @override
  String? get scope;
  @override
  @JsonKey(name: "refresh_token")
  String? get refreshToken;
  @override
  @JsonKey(name: 'grant_type')
  String? get grantType;
  @override
  @JsonKey(name: '_')
  int get timeStamp;
  @override
  @JsonKey(ignore: true)
  _$$LoginModelImplCopyWith<_$LoginModelImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
