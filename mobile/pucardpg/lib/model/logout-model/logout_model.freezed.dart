// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'logout_model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

LogoutRequest _$LogoutRequestFromJson(Map<String, dynamic> json) {
  return _LogoutRequest.fromJson(json);
}

/// @nodoc
mixin _$LogoutRequest {
  @JsonKey(name: 'access_token')
  String? get accessToken => throw _privateConstructorUsedError;
  String? get uuid => throw _privateConstructorUsedError;
  @JsonKey(name: 'RequestInfo')
  RequestInfo get requestInfo => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $LogoutRequestCopyWith<LogoutRequest> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $LogoutRequestCopyWith<$Res> {
  factory $LogoutRequestCopyWith(
          LogoutRequest value, $Res Function(LogoutRequest) then) =
      _$LogoutRequestCopyWithImpl<$Res, LogoutRequest>;
  @useResult
  $Res call(
      {@JsonKey(name: 'access_token') String? accessToken,
      String? uuid,
      @JsonKey(name: 'RequestInfo') RequestInfo requestInfo});

  $RequestInfoCopyWith<$Res> get requestInfo;
}

/// @nodoc
class _$LogoutRequestCopyWithImpl<$Res, $Val extends LogoutRequest>
    implements $LogoutRequestCopyWith<$Res> {
  _$LogoutRequestCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? accessToken = freezed,
    Object? uuid = freezed,
    Object? requestInfo = null,
  }) {
    return _then(_value.copyWith(
      accessToken: freezed == accessToken
          ? _value.accessToken
          : accessToken // ignore: cast_nullable_to_non_nullable
              as String?,
      uuid: freezed == uuid
          ? _value.uuid
          : uuid // ignore: cast_nullable_to_non_nullable
              as String?,
      requestInfo: null == requestInfo
          ? _value.requestInfo
          : requestInfo // ignore: cast_nullable_to_non_nullable
              as RequestInfo,
    ) as $Val);
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
abstract class _$$LogoutRequestImplCopyWith<$Res>
    implements $LogoutRequestCopyWith<$Res> {
  factory _$$LogoutRequestImplCopyWith(
          _$LogoutRequestImpl value, $Res Function(_$LogoutRequestImpl) then) =
      __$$LogoutRequestImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'access_token') String? accessToken,
      String? uuid,
      @JsonKey(name: 'RequestInfo') RequestInfo requestInfo});

  @override
  $RequestInfoCopyWith<$Res> get requestInfo;
}

/// @nodoc
class __$$LogoutRequestImplCopyWithImpl<$Res>
    extends _$LogoutRequestCopyWithImpl<$Res, _$LogoutRequestImpl>
    implements _$$LogoutRequestImplCopyWith<$Res> {
  __$$LogoutRequestImplCopyWithImpl(
      _$LogoutRequestImpl _value, $Res Function(_$LogoutRequestImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? accessToken = freezed,
    Object? uuid = freezed,
    Object? requestInfo = null,
  }) {
    return _then(_$LogoutRequestImpl(
      accessToken: freezed == accessToken
          ? _value.accessToken
          : accessToken // ignore: cast_nullable_to_non_nullable
              as String?,
      uuid: freezed == uuid
          ? _value.uuid
          : uuid // ignore: cast_nullable_to_non_nullable
              as String?,
      requestInfo: null == requestInfo
          ? _value.requestInfo
          : requestInfo // ignore: cast_nullable_to_non_nullable
              as RequestInfo,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$LogoutRequestImpl implements _LogoutRequest {
  const _$LogoutRequestImpl(
      {@JsonKey(name: 'access_token') required this.accessToken,
      this.uuid,
      @JsonKey(name: 'RequestInfo') required this.requestInfo});

  factory _$LogoutRequestImpl.fromJson(Map<String, dynamic> json) =>
      _$$LogoutRequestImplFromJson(json);

  @override
  @JsonKey(name: 'access_token')
  final String? accessToken;
  @override
  final String? uuid;
  @override
  @JsonKey(name: 'RequestInfo')
  final RequestInfo requestInfo;

  @override
  String toString() {
    return 'LogoutRequest(accessToken: $accessToken, uuid: $uuid, requestInfo: $requestInfo)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$LogoutRequestImpl &&
            (identical(other.accessToken, accessToken) ||
                other.accessToken == accessToken) &&
            (identical(other.uuid, uuid) || other.uuid == uuid) &&
            (identical(other.requestInfo, requestInfo) ||
                other.requestInfo == requestInfo));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, accessToken, uuid, requestInfo);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$LogoutRequestImplCopyWith<_$LogoutRequestImpl> get copyWith =>
      __$$LogoutRequestImplCopyWithImpl<_$LogoutRequestImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$LogoutRequestImplToJson(
      this,
    );
  }
}

abstract class _LogoutRequest implements LogoutRequest {
  const factory _LogoutRequest(
      {@JsonKey(name: 'access_token') required final String? accessToken,
      final String? uuid,
      @JsonKey(name: 'RequestInfo')
      required final RequestInfo requestInfo}) = _$LogoutRequestImpl;

  factory _LogoutRequest.fromJson(Map<String, dynamic> json) =
      _$LogoutRequestImpl.fromJson;

  @override
  @JsonKey(name: 'access_token')
  String? get accessToken;
  @override
  String? get uuid;
  @override
  @JsonKey(name: 'RequestInfo')
  RequestInfo get requestInfo;
  @override
  @JsonKey(ignore: true)
  _$$LogoutRequestImplCopyWith<_$LogoutRequestImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

LogoutResponse _$LogoutResponseFromJson(Map<String, dynamic> json) {
  return _LogoutResponse.fromJson(json);
}

/// @nodoc
mixin _$LogoutResponse {
  @JsonKey(name: 'responseInfo')
  ResponseInfoSearch get responseInfo => throw _privateConstructorUsedError;
  @JsonKey(name: 'error')
  ErrorModel? get error => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $LogoutResponseCopyWith<LogoutResponse> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $LogoutResponseCopyWith<$Res> {
  factory $LogoutResponseCopyWith(
          LogoutResponse value, $Res Function(LogoutResponse) then) =
      _$LogoutResponseCopyWithImpl<$Res, LogoutResponse>;
  @useResult
  $Res call(
      {@JsonKey(name: 'responseInfo') ResponseInfoSearch responseInfo,
      @JsonKey(name: 'error') ErrorModel? error});

  $ResponseInfoSearchCopyWith<$Res> get responseInfo;
  $ErrorModelCopyWith<$Res>? get error;
}

/// @nodoc
class _$LogoutResponseCopyWithImpl<$Res, $Val extends LogoutResponse>
    implements $LogoutResponseCopyWith<$Res> {
  _$LogoutResponseCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? responseInfo = null,
    Object? error = freezed,
  }) {
    return _then(_value.copyWith(
      responseInfo: null == responseInfo
          ? _value.responseInfo
          : responseInfo // ignore: cast_nullable_to_non_nullable
              as ResponseInfoSearch,
      error: freezed == error
          ? _value.error
          : error // ignore: cast_nullable_to_non_nullable
              as ErrorModel?,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $ResponseInfoSearchCopyWith<$Res> get responseInfo {
    return $ResponseInfoSearchCopyWith<$Res>(_value.responseInfo, (value) {
      return _then(_value.copyWith(responseInfo: value) as $Val);
    });
  }

  @override
  @pragma('vm:prefer-inline')
  $ErrorModelCopyWith<$Res>? get error {
    if (_value.error == null) {
      return null;
    }

    return $ErrorModelCopyWith<$Res>(_value.error!, (value) {
      return _then(_value.copyWith(error: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$LogoutResponseImplCopyWith<$Res>
    implements $LogoutResponseCopyWith<$Res> {
  factory _$$LogoutResponseImplCopyWith(_$LogoutResponseImpl value,
          $Res Function(_$LogoutResponseImpl) then) =
      __$$LogoutResponseImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'responseInfo') ResponseInfoSearch responseInfo,
      @JsonKey(name: 'error') ErrorModel? error});

  @override
  $ResponseInfoSearchCopyWith<$Res> get responseInfo;
  @override
  $ErrorModelCopyWith<$Res>? get error;
}

/// @nodoc
class __$$LogoutResponseImplCopyWithImpl<$Res>
    extends _$LogoutResponseCopyWithImpl<$Res, _$LogoutResponseImpl>
    implements _$$LogoutResponseImplCopyWith<$Res> {
  __$$LogoutResponseImplCopyWithImpl(
      _$LogoutResponseImpl _value, $Res Function(_$LogoutResponseImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? responseInfo = null,
    Object? error = freezed,
  }) {
    return _then(_$LogoutResponseImpl(
      responseInfo: null == responseInfo
          ? _value.responseInfo
          : responseInfo // ignore: cast_nullable_to_non_nullable
              as ResponseInfoSearch,
      error: freezed == error
          ? _value.error
          : error // ignore: cast_nullable_to_non_nullable
              as ErrorModel?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$LogoutResponseImpl implements _LogoutResponse {
  const _$LogoutResponseImpl(
      {@JsonKey(name: 'responseInfo')
      this.responseInfo = const ResponseInfoSearch(status: ""),
      @JsonKey(name: 'error') this.error});

  factory _$LogoutResponseImpl.fromJson(Map<String, dynamic> json) =>
      _$$LogoutResponseImplFromJson(json);

  @override
  @JsonKey(name: 'responseInfo')
  final ResponseInfoSearch responseInfo;
  @override
  @JsonKey(name: 'error')
  final ErrorModel? error;

  @override
  String toString() {
    return 'LogoutResponse(responseInfo: $responseInfo, error: $error)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$LogoutResponseImpl &&
            (identical(other.responseInfo, responseInfo) ||
                other.responseInfo == responseInfo) &&
            (identical(other.error, error) || other.error == error));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, responseInfo, error);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$LogoutResponseImplCopyWith<_$LogoutResponseImpl> get copyWith =>
      __$$LogoutResponseImplCopyWithImpl<_$LogoutResponseImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$LogoutResponseImplToJson(
      this,
    );
  }
}

abstract class _LogoutResponse implements LogoutResponse {
  const factory _LogoutResponse(
      {@JsonKey(name: 'responseInfo') final ResponseInfoSearch responseInfo,
      @JsonKey(name: 'error') final ErrorModel? error}) = _$LogoutResponseImpl;

  factory _LogoutResponse.fromJson(Map<String, dynamic> json) =
      _$LogoutResponseImpl.fromJson;

  @override
  @JsonKey(name: 'responseInfo')
  ResponseInfoSearch get responseInfo;
  @override
  @JsonKey(name: 'error')
  ErrorModel? get error;
  @override
  @JsonKey(ignore: true)
  _$$LogoutResponseImplCopyWith<_$LogoutResponseImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
