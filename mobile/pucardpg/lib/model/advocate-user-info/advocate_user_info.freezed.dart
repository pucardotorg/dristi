// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'advocate_user_info.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

AdvocateUserInfo _$AdvocateUserInfoFromJson(Map<String, dynamic> json) {
  return _AdvocateUserInfo.fromJson(json);
}

/// @nodoc
mixin _$AdvocateUserInfo {
  @JsonKey(name: 'type')
  String? get type => throw _privateConstructorUsedError;
  @JsonKey(name: 'tenantId')
  String? get tenantId => throw _privateConstructorUsedError;
  @JsonKey(name: 'roles')
  List<Role>? get roles => throw _privateConstructorUsedError;
  @JsonKey(name: 'uuid')
  String? get uuid => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AdvocateUserInfoCopyWith<AdvocateUserInfo> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AdvocateUserInfoCopyWith<$Res> {
  factory $AdvocateUserInfoCopyWith(
          AdvocateUserInfo value, $Res Function(AdvocateUserInfo) then) =
      _$AdvocateUserInfoCopyWithImpl<$Res, AdvocateUserInfo>;
  @useResult
  $Res call(
      {@JsonKey(name: 'type') String? type,
      @JsonKey(name: 'tenantId') String? tenantId,
      @JsonKey(name: 'roles') List<Role>? roles,
      @JsonKey(name: 'uuid') String? uuid});
}

/// @nodoc
class _$AdvocateUserInfoCopyWithImpl<$Res, $Val extends AdvocateUserInfo>
    implements $AdvocateUserInfoCopyWith<$Res> {
  _$AdvocateUserInfoCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = freezed,
    Object? tenantId = freezed,
    Object? roles = freezed,
    Object? uuid = freezed,
  }) {
    return _then(_value.copyWith(
      type: freezed == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String?,
      tenantId: freezed == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String?,
      roles: freezed == roles
          ? _value.roles
          : roles // ignore: cast_nullable_to_non_nullable
              as List<Role>?,
      uuid: freezed == uuid
          ? _value.uuid
          : uuid // ignore: cast_nullable_to_non_nullable
              as String?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$AdvocateUserInfoImplCopyWith<$Res>
    implements $AdvocateUserInfoCopyWith<$Res> {
  factory _$$AdvocateUserInfoImplCopyWith(_$AdvocateUserInfoImpl value,
          $Res Function(_$AdvocateUserInfoImpl) then) =
      __$$AdvocateUserInfoImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'type') String? type,
      @JsonKey(name: 'tenantId') String? tenantId,
      @JsonKey(name: 'roles') List<Role>? roles,
      @JsonKey(name: 'uuid') String? uuid});
}

/// @nodoc
class __$$AdvocateUserInfoImplCopyWithImpl<$Res>
    extends _$AdvocateUserInfoCopyWithImpl<$Res, _$AdvocateUserInfoImpl>
    implements _$$AdvocateUserInfoImplCopyWith<$Res> {
  __$$AdvocateUserInfoImplCopyWithImpl(_$AdvocateUserInfoImpl _value,
      $Res Function(_$AdvocateUserInfoImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = freezed,
    Object? tenantId = freezed,
    Object? roles = freezed,
    Object? uuid = freezed,
  }) {
    return _then(_$AdvocateUserInfoImpl(
      type: freezed == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String?,
      tenantId: freezed == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String?,
      roles: freezed == roles
          ? _value._roles
          : roles // ignore: cast_nullable_to_non_nullable
              as List<Role>?,
      uuid: freezed == uuid
          ? _value.uuid
          : uuid // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AdvocateUserInfoImpl implements _AdvocateUserInfo {
  const _$AdvocateUserInfoImpl(
      {@JsonKey(name: 'type') this.type,
      @JsonKey(name: 'tenantId') this.tenantId,
      @JsonKey(name: 'roles') final List<Role>? roles,
      @JsonKey(name: 'uuid') this.uuid})
      : _roles = roles;

  factory _$AdvocateUserInfoImpl.fromJson(Map<String, dynamic> json) =>
      _$$AdvocateUserInfoImplFromJson(json);

  @override
  @JsonKey(name: 'type')
  final String? type;
  @override
  @JsonKey(name: 'tenantId')
  final String? tenantId;
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
  @JsonKey(name: 'uuid')
  final String? uuid;

  @override
  String toString() {
    return 'AdvocateUserInfo(type: $type, tenantId: $tenantId, roles: $roles, uuid: $uuid)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdvocateUserInfoImpl &&
            (identical(other.type, type) || other.type == type) &&
            (identical(other.tenantId, tenantId) ||
                other.tenantId == tenantId) &&
            const DeepCollectionEquality().equals(other._roles, _roles) &&
            (identical(other.uuid, uuid) || other.uuid == uuid));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, type, tenantId,
      const DeepCollectionEquality().hash(_roles), uuid);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AdvocateUserInfoImplCopyWith<_$AdvocateUserInfoImpl> get copyWith =>
      __$$AdvocateUserInfoImplCopyWithImpl<_$AdvocateUserInfoImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AdvocateUserInfoImplToJson(
      this,
    );
  }
}

abstract class _AdvocateUserInfo implements AdvocateUserInfo {
  const factory _AdvocateUserInfo(
      {@JsonKey(name: 'type') final String? type,
      @JsonKey(name: 'tenantId') final String? tenantId,
      @JsonKey(name: 'roles') final List<Role>? roles,
      @JsonKey(name: 'uuid') final String? uuid}) = _$AdvocateUserInfoImpl;

  factory _AdvocateUserInfo.fromJson(Map<String, dynamic> json) =
      _$AdvocateUserInfoImpl.fromJson;

  @override
  @JsonKey(name: 'type')
  String? get type;
  @override
  @JsonKey(name: 'tenantId')
  String? get tenantId;
  @override
  @JsonKey(name: 'roles')
  List<Role>? get roles;
  @override
  @JsonKey(name: 'uuid')
  String? get uuid;
  @override
  @JsonKey(ignore: true)
  _$$AdvocateUserInfoImplCopyWith<_$AdvocateUserInfoImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
