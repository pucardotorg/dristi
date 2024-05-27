// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'advocate_request_info.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

AdvocateRequestInfo _$AdvocateRequestInfoFromJson(Map<String, dynamic> json) {
  return _AdvocateRequestInfo.fromJson(json);
}

/// @nodoc
mixin _$AdvocateRequestInfo {
  @JsonKey(name: 'apiId')
  String? get apiId => throw _privateConstructorUsedError;
  @JsonKey(name: 'ver')
  String? get ver => throw _privateConstructorUsedError;
  @JsonKey(name: 'ts')
  int? get ts => throw _privateConstructorUsedError;
  @JsonKey(name: 'action')
  String? get action => throw _privateConstructorUsedError;
  @JsonKey(name: 'did')
  String? get did => throw _privateConstructorUsedError;
  @JsonKey(name: 'key')
  String? get key => throw _privateConstructorUsedError;
  @JsonKey(name: 'msgId')
  String? get msgId => throw _privateConstructorUsedError;
  @JsonKey(name: 'userInfo')
  UserRequest? get userInfo => throw _privateConstructorUsedError;
  @JsonKey(name: 'authToken')
  String? get authToken => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AdvocateRequestInfoCopyWith<AdvocateRequestInfo> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AdvocateRequestInfoCopyWith<$Res> {
  factory $AdvocateRequestInfoCopyWith(
          AdvocateRequestInfo value, $Res Function(AdvocateRequestInfo) then) =
      _$AdvocateRequestInfoCopyWithImpl<$Res, AdvocateRequestInfo>;
  @useResult
  $Res call(
      {@JsonKey(name: 'apiId') String? apiId,
      @JsonKey(name: 'ver') String? ver,
      @JsonKey(name: 'ts') int? ts,
      @JsonKey(name: 'action') String? action,
      @JsonKey(name: 'did') String? did,
      @JsonKey(name: 'key') String? key,
      @JsonKey(name: 'msgId') String? msgId,
      @JsonKey(name: 'userInfo') UserRequest? userInfo,
      @JsonKey(name: 'authToken') String? authToken});

  $UserRequestCopyWith<$Res>? get userInfo;
}

/// @nodoc
class _$AdvocateRequestInfoCopyWithImpl<$Res, $Val extends AdvocateRequestInfo>
    implements $AdvocateRequestInfoCopyWith<$Res> {
  _$AdvocateRequestInfoCopyWithImpl(this._value, this._then);

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
    Object? action = freezed,
    Object? did = freezed,
    Object? key = freezed,
    Object? msgId = freezed,
    Object? userInfo = freezed,
    Object? authToken = freezed,
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
              as int?,
      action: freezed == action
          ? _value.action
          : action // ignore: cast_nullable_to_non_nullable
              as String?,
      did: freezed == did
          ? _value.did
          : did // ignore: cast_nullable_to_non_nullable
              as String?,
      key: freezed == key
          ? _value.key
          : key // ignore: cast_nullable_to_non_nullable
              as String?,
      msgId: freezed == msgId
          ? _value.msgId
          : msgId // ignore: cast_nullable_to_non_nullable
              as String?,
      userInfo: freezed == userInfo
          ? _value.userInfo
          : userInfo // ignore: cast_nullable_to_non_nullable
              as UserRequest?,
      authToken: freezed == authToken
          ? _value.authToken
          : authToken // ignore: cast_nullable_to_non_nullable
              as String?,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $UserRequestCopyWith<$Res>? get userInfo {
    if (_value.userInfo == null) {
      return null;
    }

    return $UserRequestCopyWith<$Res>(_value.userInfo!, (value) {
      return _then(_value.copyWith(userInfo: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$AdvocateRequestInfoImplCopyWith<$Res>
    implements $AdvocateRequestInfoCopyWith<$Res> {
  factory _$$AdvocateRequestInfoImplCopyWith(_$AdvocateRequestInfoImpl value,
          $Res Function(_$AdvocateRequestInfoImpl) then) =
      __$$AdvocateRequestInfoImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'apiId') String? apiId,
      @JsonKey(name: 'ver') String? ver,
      @JsonKey(name: 'ts') int? ts,
      @JsonKey(name: 'action') String? action,
      @JsonKey(name: 'did') String? did,
      @JsonKey(name: 'key') String? key,
      @JsonKey(name: 'msgId') String? msgId,
      @JsonKey(name: 'userInfo') UserRequest? userInfo,
      @JsonKey(name: 'authToken') String? authToken});

  @override
  $UserRequestCopyWith<$Res>? get userInfo;
}

/// @nodoc
class __$$AdvocateRequestInfoImplCopyWithImpl<$Res>
    extends _$AdvocateRequestInfoCopyWithImpl<$Res, _$AdvocateRequestInfoImpl>
    implements _$$AdvocateRequestInfoImplCopyWith<$Res> {
  __$$AdvocateRequestInfoImplCopyWithImpl(_$AdvocateRequestInfoImpl _value,
      $Res Function(_$AdvocateRequestInfoImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? apiId = freezed,
    Object? ver = freezed,
    Object? ts = freezed,
    Object? action = freezed,
    Object? did = freezed,
    Object? key = freezed,
    Object? msgId = freezed,
    Object? userInfo = freezed,
    Object? authToken = freezed,
  }) {
    return _then(_$AdvocateRequestInfoImpl(
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
              as int?,
      action: freezed == action
          ? _value.action
          : action // ignore: cast_nullable_to_non_nullable
              as String?,
      did: freezed == did
          ? _value.did
          : did // ignore: cast_nullable_to_non_nullable
              as String?,
      key: freezed == key
          ? _value.key
          : key // ignore: cast_nullable_to_non_nullable
              as String?,
      msgId: freezed == msgId
          ? _value.msgId
          : msgId // ignore: cast_nullable_to_non_nullable
              as String?,
      userInfo: freezed == userInfo
          ? _value.userInfo
          : userInfo // ignore: cast_nullable_to_non_nullable
              as UserRequest?,
      authToken: freezed == authToken
          ? _value.authToken
          : authToken // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AdvocateRequestInfoImpl implements _AdvocateRequestInfo {
  const _$AdvocateRequestInfoImpl(
      {@JsonKey(name: 'apiId') this.apiId,
      @JsonKey(name: 'ver') this.ver,
      @JsonKey(name: 'ts') this.ts,
      @JsonKey(name: 'action') this.action,
      @JsonKey(name: 'did') this.did,
      @JsonKey(name: 'key') this.key,
      @JsonKey(name: 'msgId') this.msgId,
      @JsonKey(name: 'userInfo') this.userInfo,
      @JsonKey(name: 'authToken') this.authToken});

  factory _$AdvocateRequestInfoImpl.fromJson(Map<String, dynamic> json) =>
      _$$AdvocateRequestInfoImplFromJson(json);

  @override
  @JsonKey(name: 'apiId')
  final String? apiId;
  @override
  @JsonKey(name: 'ver')
  final String? ver;
  @override
  @JsonKey(name: 'ts')
  final int? ts;
  @override
  @JsonKey(name: 'action')
  final String? action;
  @override
  @JsonKey(name: 'did')
  final String? did;
  @override
  @JsonKey(name: 'key')
  final String? key;
  @override
  @JsonKey(name: 'msgId')
  final String? msgId;
  @override
  @JsonKey(name: 'userInfo')
  final UserRequest? userInfo;
  @override
  @JsonKey(name: 'authToken')
  final String? authToken;

  @override
  String toString() {
    return 'AdvocateRequestInfo(apiId: $apiId, ver: $ver, ts: $ts, action: $action, did: $did, key: $key, msgId: $msgId, userInfo: $userInfo, authToken: $authToken)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdvocateRequestInfoImpl &&
            (identical(other.apiId, apiId) || other.apiId == apiId) &&
            (identical(other.ver, ver) || other.ver == ver) &&
            (identical(other.ts, ts) || other.ts == ts) &&
            (identical(other.action, action) || other.action == action) &&
            (identical(other.did, did) || other.did == did) &&
            (identical(other.key, key) || other.key == key) &&
            (identical(other.msgId, msgId) || other.msgId == msgId) &&
            (identical(other.userInfo, userInfo) ||
                other.userInfo == userInfo) &&
            (identical(other.authToken, authToken) ||
                other.authToken == authToken));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, apiId, ver, ts, action, did, key,
      msgId, userInfo, authToken);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AdvocateRequestInfoImplCopyWith<_$AdvocateRequestInfoImpl> get copyWith =>
      __$$AdvocateRequestInfoImplCopyWithImpl<_$AdvocateRequestInfoImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AdvocateRequestInfoImplToJson(
      this,
    );
  }
}

abstract class _AdvocateRequestInfo implements AdvocateRequestInfo {
  const factory _AdvocateRequestInfo(
          {@JsonKey(name: 'apiId') final String? apiId,
          @JsonKey(name: 'ver') final String? ver,
          @JsonKey(name: 'ts') final int? ts,
          @JsonKey(name: 'action') final String? action,
          @JsonKey(name: 'did') final String? did,
          @JsonKey(name: 'key') final String? key,
          @JsonKey(name: 'msgId') final String? msgId,
          @JsonKey(name: 'userInfo') final UserRequest? userInfo,
          @JsonKey(name: 'authToken') final String? authToken}) =
      _$AdvocateRequestInfoImpl;

  factory _AdvocateRequestInfo.fromJson(Map<String, dynamic> json) =
      _$AdvocateRequestInfoImpl.fromJson;

  @override
  @JsonKey(name: 'apiId')
  String? get apiId;
  @override
  @JsonKey(name: 'ver')
  String? get ver;
  @override
  @JsonKey(name: 'ts')
  int? get ts;
  @override
  @JsonKey(name: 'action')
  String? get action;
  @override
  @JsonKey(name: 'did')
  String? get did;
  @override
  @JsonKey(name: 'key')
  String? get key;
  @override
  @JsonKey(name: 'msgId')
  String? get msgId;
  @override
  @JsonKey(name: 'userInfo')
  UserRequest? get userInfo;
  @override
  @JsonKey(name: 'authToken')
  String? get authToken;
  @override
  @JsonKey(ignore: true)
  _$$AdvocateRequestInfoImplCopyWith<_$AdvocateRequestInfoImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
