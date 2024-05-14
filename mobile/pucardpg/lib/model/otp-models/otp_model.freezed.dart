// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'otp_model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

OtpRequest _$OtpRequestFromJson(Map<String, dynamic> json) {
  return _OtpRequest.fromJson(json);
}

/// @nodoc
mixin _$OtpRequest {
  @JsonKey(name: 'otp')
  Otp get otp => throw _privateConstructorUsedError;
  @JsonKey(name: 'RequestInfo')
  RequestInfo get requestInfo => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $OtpRequestCopyWith<OtpRequest> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $OtpRequestCopyWith<$Res> {
  factory $OtpRequestCopyWith(
          OtpRequest value, $Res Function(OtpRequest) then) =
      _$OtpRequestCopyWithImpl<$Res, OtpRequest>;
  @useResult
  $Res call(
      {@JsonKey(name: 'otp') Otp otp,
      @JsonKey(name: 'RequestInfo') RequestInfo requestInfo});

  $OtpCopyWith<$Res> get otp;
  $RequestInfoCopyWith<$Res> get requestInfo;
}

/// @nodoc
class _$OtpRequestCopyWithImpl<$Res, $Val extends OtpRequest>
    implements $OtpRequestCopyWith<$Res> {
  _$OtpRequestCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? otp = null,
    Object? requestInfo = null,
  }) {
    return _then(_value.copyWith(
      otp: null == otp
          ? _value.otp
          : otp // ignore: cast_nullable_to_non_nullable
              as Otp,
      requestInfo: null == requestInfo
          ? _value.requestInfo
          : requestInfo // ignore: cast_nullable_to_non_nullable
              as RequestInfo,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $OtpCopyWith<$Res> get otp {
    return $OtpCopyWith<$Res>(_value.otp, (value) {
      return _then(_value.copyWith(otp: value) as $Val);
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
abstract class _$$OtpRequestImplCopyWith<$Res>
    implements $OtpRequestCopyWith<$Res> {
  factory _$$OtpRequestImplCopyWith(
          _$OtpRequestImpl value, $Res Function(_$OtpRequestImpl) then) =
      __$$OtpRequestImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'otp') Otp otp,
      @JsonKey(name: 'RequestInfo') RequestInfo requestInfo});

  @override
  $OtpCopyWith<$Res> get otp;
  @override
  $RequestInfoCopyWith<$Res> get requestInfo;
}

/// @nodoc
class __$$OtpRequestImplCopyWithImpl<$Res>
    extends _$OtpRequestCopyWithImpl<$Res, _$OtpRequestImpl>
    implements _$$OtpRequestImplCopyWith<$Res> {
  __$$OtpRequestImplCopyWithImpl(
      _$OtpRequestImpl _value, $Res Function(_$OtpRequestImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? otp = null,
    Object? requestInfo = null,
  }) {
    return _then(_$OtpRequestImpl(
      otp: null == otp
          ? _value.otp
          : otp // ignore: cast_nullable_to_non_nullable
              as Otp,
      requestInfo: null == requestInfo
          ? _value.requestInfo
          : requestInfo // ignore: cast_nullable_to_non_nullable
              as RequestInfo,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$OtpRequestImpl implements _OtpRequest {
  const _$OtpRequestImpl(
      {@JsonKey(name: 'otp') required this.otp,
      @JsonKey(name: 'RequestInfo') this.requestInfo = const RequestInfo()});

  factory _$OtpRequestImpl.fromJson(Map<String, dynamic> json) =>
      _$$OtpRequestImplFromJson(json);

  @override
  @JsonKey(name: 'otp')
  final Otp otp;
  @override
  @JsonKey(name: 'RequestInfo')
  final RequestInfo requestInfo;

  @override
  String toString() {
    return 'OtpRequest(otp: $otp, requestInfo: $requestInfo)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$OtpRequestImpl &&
            (identical(other.otp, otp) || other.otp == otp) &&
            (identical(other.requestInfo, requestInfo) ||
                other.requestInfo == requestInfo));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, otp, requestInfo);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$OtpRequestImplCopyWith<_$OtpRequestImpl> get copyWith =>
      __$$OtpRequestImplCopyWithImpl<_$OtpRequestImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$OtpRequestImplToJson(
      this,
    );
  }
}

abstract class _OtpRequest implements OtpRequest {
  const factory _OtpRequest(
          {@JsonKey(name: 'otp') required final Otp otp,
          @JsonKey(name: 'RequestInfo') final RequestInfo requestInfo}) =
      _$OtpRequestImpl;

  factory _OtpRequest.fromJson(Map<String, dynamic> json) =
      _$OtpRequestImpl.fromJson;

  @override
  @JsonKey(name: 'otp')
  Otp get otp;
  @override
  @JsonKey(name: 'RequestInfo')
  RequestInfo get requestInfo;
  @override
  @JsonKey(ignore: true)
  _$$OtpRequestImplCopyWith<_$OtpRequestImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

Otp _$OtpFromJson(Map<String, dynamic> json) {
  return _Otp.fromJson(json);
}

/// @nodoc
mixin _$Otp {
  @JsonKey(name: 'mobileNumber')
  String get mobileNumber => throw _privateConstructorUsedError;
  @JsonKey(name: 'tenantId')
  String get tenantId => throw _privateConstructorUsedError;
  @JsonKey(name: 'userType')
  String get userType => throw _privateConstructorUsedError;
  @JsonKey(name: 'type')
  String get type => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $OtpCopyWith<Otp> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $OtpCopyWith<$Res> {
  factory $OtpCopyWith(Otp value, $Res Function(Otp) then) =
      _$OtpCopyWithImpl<$Res, Otp>;
  @useResult
  $Res call(
      {@JsonKey(name: 'mobileNumber') String mobileNumber,
      @JsonKey(name: 'tenantId') String tenantId,
      @JsonKey(name: 'userType') String userType,
      @JsonKey(name: 'type') String type});
}

/// @nodoc
class _$OtpCopyWithImpl<$Res, $Val extends Otp> implements $OtpCopyWith<$Res> {
  _$OtpCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? mobileNumber = null,
    Object? tenantId = null,
    Object? userType = null,
    Object? type = null,
  }) {
    return _then(_value.copyWith(
      mobileNumber: null == mobileNumber
          ? _value.mobileNumber
          : mobileNumber // ignore: cast_nullable_to_non_nullable
              as String,
      tenantId: null == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String,
      userType: null == userType
          ? _value.userType
          : userType // ignore: cast_nullable_to_non_nullable
              as String,
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$OtpImplCopyWith<$Res> implements $OtpCopyWith<$Res> {
  factory _$$OtpImplCopyWith(_$OtpImpl value, $Res Function(_$OtpImpl) then) =
      __$$OtpImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'mobileNumber') String mobileNumber,
      @JsonKey(name: 'tenantId') String tenantId,
      @JsonKey(name: 'userType') String userType,
      @JsonKey(name: 'type') String type});
}

/// @nodoc
class __$$OtpImplCopyWithImpl<$Res> extends _$OtpCopyWithImpl<$Res, _$OtpImpl>
    implements _$$OtpImplCopyWith<$Res> {
  __$$OtpImplCopyWithImpl(_$OtpImpl _value, $Res Function(_$OtpImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? mobileNumber = null,
    Object? tenantId = null,
    Object? userType = null,
    Object? type = null,
  }) {
    return _then(_$OtpImpl(
      mobileNumber: null == mobileNumber
          ? _value.mobileNumber
          : mobileNumber // ignore: cast_nullable_to_non_nullable
              as String,
      tenantId: null == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String,
      userType: null == userType
          ? _value.userType
          : userType // ignore: cast_nullable_to_non_nullable
              as String,
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$OtpImpl implements _Otp {
  const _$OtpImpl(
      {@JsonKey(name: 'mobileNumber') required this.mobileNumber,
      @JsonKey(name: 'tenantId') this.tenantId = "pg",
      @JsonKey(name: 'userType') this.userType = "citizen",
      @JsonKey(name: 'type') required this.type});

  factory _$OtpImpl.fromJson(Map<String, dynamic> json) =>
      _$$OtpImplFromJson(json);

  @override
  @JsonKey(name: 'mobileNumber')
  final String mobileNumber;
  @override
  @JsonKey(name: 'tenantId')
  final String tenantId;
  @override
  @JsonKey(name: 'userType')
  final String userType;
  @override
  @JsonKey(name: 'type')
  final String type;

  @override
  String toString() {
    return 'Otp(mobileNumber: $mobileNumber, tenantId: $tenantId, userType: $userType, type: $type)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$OtpImpl &&
            (identical(other.mobileNumber, mobileNumber) ||
                other.mobileNumber == mobileNumber) &&
            (identical(other.tenantId, tenantId) ||
                other.tenantId == tenantId) &&
            (identical(other.userType, userType) ||
                other.userType == userType) &&
            (identical(other.type, type) || other.type == type));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, mobileNumber, tenantId, userType, type);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$OtpImplCopyWith<_$OtpImpl> get copyWith =>
      __$$OtpImplCopyWithImpl<_$OtpImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$OtpImplToJson(
      this,
    );
  }
}

abstract class _Otp implements Otp {
  const factory _Otp(
      {@JsonKey(name: 'mobileNumber') required final String mobileNumber,
      @JsonKey(name: 'tenantId') final String tenantId,
      @JsonKey(name: 'userType') final String userType,
      @JsonKey(name: 'type') required final String type}) = _$OtpImpl;

  factory _Otp.fromJson(Map<String, dynamic> json) = _$OtpImpl.fromJson;

  @override
  @JsonKey(name: 'mobileNumber')
  String get mobileNumber;
  @override
  @JsonKey(name: 'tenantId')
  String get tenantId;
  @override
  @JsonKey(name: 'userType')
  String get userType;
  @override
  @JsonKey(name: 'type')
  String get type;
  @override
  @JsonKey(ignore: true)
  _$$OtpImplCopyWith<_$OtpImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

OtpError _$OtpErrorFromJson(Map<String, dynamic> json) {
  return _OtpError.fromJson(json);
}

/// @nodoc
mixin _$OtpError {
  @JsonKey(name: 'code')
  int? get code => throw _privateConstructorUsedError;
  @JsonKey(name: 'message')
  String? get message => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $OtpErrorCopyWith<OtpError> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $OtpErrorCopyWith<$Res> {
  factory $OtpErrorCopyWith(OtpError value, $Res Function(OtpError) then) =
      _$OtpErrorCopyWithImpl<$Res, OtpError>;
  @useResult
  $Res call(
      {@JsonKey(name: 'code') int? code,
      @JsonKey(name: 'message') String? message});
}

/// @nodoc
class _$OtpErrorCopyWithImpl<$Res, $Val extends OtpError>
    implements $OtpErrorCopyWith<$Res> {
  _$OtpErrorCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? code = freezed,
    Object? message = freezed,
  }) {
    return _then(_value.copyWith(
      code: freezed == code
          ? _value.code
          : code // ignore: cast_nullable_to_non_nullable
              as int?,
      message: freezed == message
          ? _value.message
          : message // ignore: cast_nullable_to_non_nullable
              as String?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$OtpErrorImplCopyWith<$Res>
    implements $OtpErrorCopyWith<$Res> {
  factory _$$OtpErrorImplCopyWith(
          _$OtpErrorImpl value, $Res Function(_$OtpErrorImpl) then) =
      __$$OtpErrorImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'code') int? code,
      @JsonKey(name: 'message') String? message});
}

/// @nodoc
class __$$OtpErrorImplCopyWithImpl<$Res>
    extends _$OtpErrorCopyWithImpl<$Res, _$OtpErrorImpl>
    implements _$$OtpErrorImplCopyWith<$Res> {
  __$$OtpErrorImplCopyWithImpl(
      _$OtpErrorImpl _value, $Res Function(_$OtpErrorImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? code = freezed,
    Object? message = freezed,
  }) {
    return _then(_$OtpErrorImpl(
      code: freezed == code
          ? _value.code
          : code // ignore: cast_nullable_to_non_nullable
              as int?,
      message: freezed == message
          ? _value.message
          : message // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$OtpErrorImpl implements _OtpError {
  const _$OtpErrorImpl(
      {@JsonKey(name: 'code') this.code,
      @JsonKey(name: 'message') this.message});

  factory _$OtpErrorImpl.fromJson(Map<String, dynamic> json) =>
      _$$OtpErrorImplFromJson(json);

  @override
  @JsonKey(name: 'code')
  final int? code;
  @override
  @JsonKey(name: 'message')
  final String? message;

  @override
  String toString() {
    return 'OtpError(code: $code, message: $message)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$OtpErrorImpl &&
            (identical(other.code, code) || other.code == code) &&
            (identical(other.message, message) || other.message == message));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, code, message);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$OtpErrorImplCopyWith<_$OtpErrorImpl> get copyWith =>
      __$$OtpErrorImplCopyWithImpl<_$OtpErrorImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$OtpErrorImplToJson(
      this,
    );
  }
}

abstract class _OtpError implements OtpError {
  const factory _OtpError(
      {@JsonKey(name: 'code') final int? code,
      @JsonKey(name: 'message') final String? message}) = _$OtpErrorImpl;

  factory _OtpError.fromJson(Map<String, dynamic> json) =
      _$OtpErrorImpl.fromJson;

  @override
  @JsonKey(name: 'code')
  int? get code;
  @override
  @JsonKey(name: 'message')
  String? get message;
  @override
  @JsonKey(ignore: true)
  _$$OtpErrorImplCopyWith<_$OtpErrorImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

OtpResponse _$OtpResponseFromJson(Map<String, dynamic> json) {
  return _OtpResponse.fromJson(json);
}

/// @nodoc
mixin _$OtpResponse {
  @JsonKey(name: 'isSuccessful')
  bool? get isSuccessful => throw _privateConstructorUsedError;
  @JsonKey(name: 'error')
  OtpError? get error => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $OtpResponseCopyWith<OtpResponse> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $OtpResponseCopyWith<$Res> {
  factory $OtpResponseCopyWith(
          OtpResponse value, $Res Function(OtpResponse) then) =
      _$OtpResponseCopyWithImpl<$Res, OtpResponse>;
  @useResult
  $Res call(
      {@JsonKey(name: 'isSuccessful') bool? isSuccessful,
      @JsonKey(name: 'error') OtpError? error});

  $OtpErrorCopyWith<$Res>? get error;
}

/// @nodoc
class _$OtpResponseCopyWithImpl<$Res, $Val extends OtpResponse>
    implements $OtpResponseCopyWith<$Res> {
  _$OtpResponseCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? isSuccessful = freezed,
    Object? error = freezed,
  }) {
    return _then(_value.copyWith(
      isSuccessful: freezed == isSuccessful
          ? _value.isSuccessful
          : isSuccessful // ignore: cast_nullable_to_non_nullable
              as bool?,
      error: freezed == error
          ? _value.error
          : error // ignore: cast_nullable_to_non_nullable
              as OtpError?,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $OtpErrorCopyWith<$Res>? get error {
    if (_value.error == null) {
      return null;
    }

    return $OtpErrorCopyWith<$Res>(_value.error!, (value) {
      return _then(_value.copyWith(error: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$OtpResponseImplCopyWith<$Res>
    implements $OtpResponseCopyWith<$Res> {
  factory _$$OtpResponseImplCopyWith(
          _$OtpResponseImpl value, $Res Function(_$OtpResponseImpl) then) =
      __$$OtpResponseImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'isSuccessful') bool? isSuccessful,
      @JsonKey(name: 'error') OtpError? error});

  @override
  $OtpErrorCopyWith<$Res>? get error;
}

/// @nodoc
class __$$OtpResponseImplCopyWithImpl<$Res>
    extends _$OtpResponseCopyWithImpl<$Res, _$OtpResponseImpl>
    implements _$$OtpResponseImplCopyWith<$Res> {
  __$$OtpResponseImplCopyWithImpl(
      _$OtpResponseImpl _value, $Res Function(_$OtpResponseImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? isSuccessful = freezed,
    Object? error = freezed,
  }) {
    return _then(_$OtpResponseImpl(
      isSuccessful: freezed == isSuccessful
          ? _value.isSuccessful
          : isSuccessful // ignore: cast_nullable_to_non_nullable
              as bool?,
      error: freezed == error
          ? _value.error
          : error // ignore: cast_nullable_to_non_nullable
              as OtpError?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$OtpResponseImpl implements _OtpResponse {
  const _$OtpResponseImpl(
      {@JsonKey(name: 'isSuccessful') this.isSuccessful,
      @JsonKey(name: 'error') this.error});

  factory _$OtpResponseImpl.fromJson(Map<String, dynamic> json) =>
      _$$OtpResponseImplFromJson(json);

  @override
  @JsonKey(name: 'isSuccessful')
  final bool? isSuccessful;
  @override
  @JsonKey(name: 'error')
  final OtpError? error;

  @override
  String toString() {
    return 'OtpResponse(isSuccessful: $isSuccessful, error: $error)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$OtpResponseImpl &&
            (identical(other.isSuccessful, isSuccessful) ||
                other.isSuccessful == isSuccessful) &&
            (identical(other.error, error) || other.error == error));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, isSuccessful, error);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$OtpResponseImplCopyWith<_$OtpResponseImpl> get copyWith =>
      __$$OtpResponseImplCopyWithImpl<_$OtpResponseImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$OtpResponseImplToJson(
      this,
    );
  }
}

abstract class _OtpResponse implements OtpResponse {
  const factory _OtpResponse(
      {@JsonKey(name: 'isSuccessful') final bool? isSuccessful,
      @JsonKey(name: 'error') final OtpError? error}) = _$OtpResponseImpl;

  factory _OtpResponse.fromJson(Map<String, dynamic> json) =
      _$OtpResponseImpl.fromJson;

  @override
  @JsonKey(name: 'isSuccessful')
  bool? get isSuccessful;
  @override
  @JsonKey(name: 'error')
  OtpError? get error;
  @override
  @JsonKey(ignore: true)
  _$$OtpResponseImplCopyWith<_$OtpResponseImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
