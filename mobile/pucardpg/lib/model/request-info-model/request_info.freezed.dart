// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'request_info.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

RequestInfo _$RequestInfoFromJson(Map<String, dynamic> json) {
  return _RequestInfo.fromJson(json);
}

/// @nodoc
mixin _$RequestInfo {
  @JsonKey(name: 'apiId')
  String get apiId => throw _privateConstructorUsedError;
  @JsonKey(name: 'authToken')
  String get authToken => throw _privateConstructorUsedError;
  @JsonKey(name: 'msgId')
  String get msgId => throw _privateConstructorUsedError;
  @JsonKey(name: 'plainAccessRequest')
  Map<String, dynamic> get plainAccessRequest =>
      throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $RequestInfoCopyWith<RequestInfo> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $RequestInfoCopyWith<$Res> {
  factory $RequestInfoCopyWith(
          RequestInfo value, $Res Function(RequestInfo) then) =
      _$RequestInfoCopyWithImpl<$Res, RequestInfo>;
  @useResult
  $Res call(
      {@JsonKey(name: 'apiId') String apiId,
      @JsonKey(name: 'authToken') String authToken,
      @JsonKey(name: 'msgId') String msgId,
      @JsonKey(name: 'plainAccessRequest')
      Map<String, dynamic> plainAccessRequest});
}

/// @nodoc
class _$RequestInfoCopyWithImpl<$Res, $Val extends RequestInfo>
    implements $RequestInfoCopyWith<$Res> {
  _$RequestInfoCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? apiId = null,
    Object? authToken = null,
    Object? msgId = null,
    Object? plainAccessRequest = null,
  }) {
    return _then(_value.copyWith(
      apiId: null == apiId
          ? _value.apiId
          : apiId // ignore: cast_nullable_to_non_nullable
              as String,
      authToken: null == authToken
          ? _value.authToken
          : authToken // ignore: cast_nullable_to_non_nullable
              as String,
      msgId: null == msgId
          ? _value.msgId
          : msgId // ignore: cast_nullable_to_non_nullable
              as String,
      plainAccessRequest: null == plainAccessRequest
          ? _value.plainAccessRequest
          : plainAccessRequest // ignore: cast_nullable_to_non_nullable
              as Map<String, dynamic>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$RequestInfoImplCopyWith<$Res>
    implements $RequestInfoCopyWith<$Res> {
  factory _$$RequestInfoImplCopyWith(
          _$RequestInfoImpl value, $Res Function(_$RequestInfoImpl) then) =
      __$$RequestInfoImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'apiId') String apiId,
      @JsonKey(name: 'authToken') String authToken,
      @JsonKey(name: 'msgId') String msgId,
      @JsonKey(name: 'plainAccessRequest')
      Map<String, dynamic> plainAccessRequest});
}

/// @nodoc
class __$$RequestInfoImplCopyWithImpl<$Res>
    extends _$RequestInfoCopyWithImpl<$Res, _$RequestInfoImpl>
    implements _$$RequestInfoImplCopyWith<$Res> {
  __$$RequestInfoImplCopyWithImpl(
      _$RequestInfoImpl _value, $Res Function(_$RequestInfoImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? apiId = null,
    Object? authToken = null,
    Object? msgId = null,
    Object? plainAccessRequest = null,
  }) {
    return _then(_$RequestInfoImpl(
      apiId: null == apiId
          ? _value.apiId
          : apiId // ignore: cast_nullable_to_non_nullable
              as String,
      authToken: null == authToken
          ? _value.authToken
          : authToken // ignore: cast_nullable_to_non_nullable
              as String,
      msgId: null == msgId
          ? _value.msgId
          : msgId // ignore: cast_nullable_to_non_nullable
              as String,
      plainAccessRequest: null == plainAccessRequest
          ? _value._plainAccessRequest
          : plainAccessRequest // ignore: cast_nullable_to_non_nullable
              as Map<String, dynamic>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$RequestInfoImpl implements _RequestInfo {
  const _$RequestInfoImpl(
      {@JsonKey(name: 'apiId') this.apiId = "Rainmaker",
      @JsonKey(name: 'authToken')
      this.authToken = "c835932f-2ad4-4d05-83d6-49e0b8c59f8a",
      @JsonKey(name: 'msgId') this.msgId = "1712987382117|en_IN",
      @JsonKey(name: 'plainAccessRequest')
      final Map<String, dynamic> plainAccessRequest = const {}})
      : _plainAccessRequest = plainAccessRequest;

  factory _$RequestInfoImpl.fromJson(Map<String, dynamic> json) =>
      _$$RequestInfoImplFromJson(json);

  @override
  @JsonKey(name: 'apiId')
  final String apiId;
  @override
  @JsonKey(name: 'authToken')
  final String authToken;
  @override
  @JsonKey(name: 'msgId')
  final String msgId;
  final Map<String, dynamic> _plainAccessRequest;
  @override
  @JsonKey(name: 'plainAccessRequest')
  Map<String, dynamic> get plainAccessRequest {
    if (_plainAccessRequest is EqualUnmodifiableMapView)
      return _plainAccessRequest;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableMapView(_plainAccessRequest);
  }

  @override
  String toString() {
    return 'RequestInfo(apiId: $apiId, authToken: $authToken, msgId: $msgId, plainAccessRequest: $plainAccessRequest)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$RequestInfoImpl &&
            (identical(other.apiId, apiId) || other.apiId == apiId) &&
            (identical(other.authToken, authToken) ||
                other.authToken == authToken) &&
            (identical(other.msgId, msgId) || other.msgId == msgId) &&
            const DeepCollectionEquality()
                .equals(other._plainAccessRequest, _plainAccessRequest));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, apiId, authToken, msgId,
      const DeepCollectionEquality().hash(_plainAccessRequest));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$RequestInfoImplCopyWith<_$RequestInfoImpl> get copyWith =>
      __$$RequestInfoImplCopyWithImpl<_$RequestInfoImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$RequestInfoImplToJson(
      this,
    );
  }
}

abstract class _RequestInfo implements RequestInfo {
  const factory _RequestInfo(
      {@JsonKey(name: 'apiId') final String apiId,
      @JsonKey(name: 'authToken') final String authToken,
      @JsonKey(name: 'msgId') final String msgId,
      @JsonKey(name: 'plainAccessRequest')
      final Map<String, dynamic> plainAccessRequest}) = _$RequestInfoImpl;

  factory _RequestInfo.fromJson(Map<String, dynamic> json) =
      _$RequestInfoImpl.fromJson;

  @override
  @JsonKey(name: 'apiId')
  String get apiId;
  @override
  @JsonKey(name: 'authToken')
  String get authToken;
  @override
  @JsonKey(name: 'msgId')
  String get msgId;
  @override
  @JsonKey(name: 'plainAccessRequest')
  Map<String, dynamic> get plainAccessRequest;
  @override
  @JsonKey(ignore: true)
  _$$RequestInfoImplCopyWith<_$RequestInfoImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
