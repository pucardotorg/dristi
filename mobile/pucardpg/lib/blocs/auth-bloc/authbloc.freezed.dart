// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'authbloc.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

/// @nodoc
mixin _$AuthEvent {
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() login,
    required TResult Function() logout,
    required TResult Function() attemptLoad,
    required TResult Function(String mobileNumber, String type) requestOtp,
    required TResult Function(String mobileNumber, String type) resendOtp,
    required TResult Function(String username, String otp, UserModel userModel)
        submitRegistrationOtp,
    required TResult Function(
            String username, String password, UserModel userModel)
        submitLoginOtpEvent,
    required TResult Function(String authToken) submitLogoutUser,
    required TResult Function() refreshToken,
    required TResult Function() submitProfile,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? login,
    TResult? Function()? logout,
    TResult? Function()? attemptLoad,
    TResult? Function(String mobileNumber, String type)? requestOtp,
    TResult? Function(String mobileNumber, String type)? resendOtp,
    TResult? Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult? Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult? Function(String authToken)? submitLogoutUser,
    TResult? Function()? refreshToken,
    TResult? Function()? submitProfile,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? login,
    TResult Function()? logout,
    TResult Function()? attemptLoad,
    TResult Function(String mobileNumber, String type)? requestOtp,
    TResult Function(String mobileNumber, String type)? resendOtp,
    TResult Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult Function(String authToken)? submitLogoutUser,
    TResult Function()? refreshToken,
    TResult Function()? submitProfile,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_AuthLoginEvent value) login,
    required TResult Function(_AuthLogoutEvent value) logout,
    required TResult Function(_AuthLoadEvent value) attemptLoad,
    required TResult Function(_RequestOtpEvent value) requestOtp,
    required TResult Function(_ResendOtpEvent value) resendOtp,
    required TResult Function(_SubmitRegistrationOtpEvent value)
        submitRegistrationOtp,
    required TResult Function(_SubmitLoginOtpEvent value) submitLoginOtpEvent,
    required TResult Function(_SubmitLogoutUserEvent value) submitLogoutUser,
    required TResult Function(_AuthRefreshTokenEvent value) refreshToken,
    required TResult Function(_SubmitProfileEvent value) submitProfile,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_AuthLoginEvent value)? login,
    TResult? Function(_AuthLogoutEvent value)? logout,
    TResult? Function(_AuthLoadEvent value)? attemptLoad,
    TResult? Function(_RequestOtpEvent value)? requestOtp,
    TResult? Function(_ResendOtpEvent value)? resendOtp,
    TResult? Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult? Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult? Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult? Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult? Function(_SubmitProfileEvent value)? submitProfile,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_AuthLoginEvent value)? login,
    TResult Function(_AuthLogoutEvent value)? logout,
    TResult Function(_AuthLoadEvent value)? attemptLoad,
    TResult Function(_RequestOtpEvent value)? requestOtp,
    TResult Function(_ResendOtpEvent value)? resendOtp,
    TResult Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult Function(_SubmitProfileEvent value)? submitProfile,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AuthEventCopyWith<$Res> {
  factory $AuthEventCopyWith(AuthEvent value, $Res Function(AuthEvent) then) =
      _$AuthEventCopyWithImpl<$Res, AuthEvent>;
}

/// @nodoc
class _$AuthEventCopyWithImpl<$Res, $Val extends AuthEvent>
    implements $AuthEventCopyWith<$Res> {
  _$AuthEventCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;
}

/// @nodoc
abstract class _$$AuthLoginEventImplCopyWith<$Res> {
  factory _$$AuthLoginEventImplCopyWith(_$AuthLoginEventImpl value,
          $Res Function(_$AuthLoginEventImpl) then) =
      __$$AuthLoginEventImplCopyWithImpl<$Res>;
}

/// @nodoc
class __$$AuthLoginEventImplCopyWithImpl<$Res>
    extends _$AuthEventCopyWithImpl<$Res, _$AuthLoginEventImpl>
    implements _$$AuthLoginEventImplCopyWith<$Res> {
  __$$AuthLoginEventImplCopyWithImpl(
      _$AuthLoginEventImpl _value, $Res Function(_$AuthLoginEventImpl) _then)
      : super(_value, _then);
}

/// @nodoc

class _$AuthLoginEventImpl implements _AuthLoginEvent {
  const _$AuthLoginEventImpl();

  @override
  String toString() {
    return 'AuthEvent.login()';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is _$AuthLoginEventImpl);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() login,
    required TResult Function() logout,
    required TResult Function() attemptLoad,
    required TResult Function(String mobileNumber, String type) requestOtp,
    required TResult Function(String mobileNumber, String type) resendOtp,
    required TResult Function(String username, String otp, UserModel userModel)
        submitRegistrationOtp,
    required TResult Function(
            String username, String password, UserModel userModel)
        submitLoginOtpEvent,
    required TResult Function(String authToken) submitLogoutUser,
    required TResult Function() refreshToken,
    required TResult Function() submitProfile,
  }) {
    return login();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? login,
    TResult? Function()? logout,
    TResult? Function()? attemptLoad,
    TResult? Function(String mobileNumber, String type)? requestOtp,
    TResult? Function(String mobileNumber, String type)? resendOtp,
    TResult? Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult? Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult? Function(String authToken)? submitLogoutUser,
    TResult? Function()? refreshToken,
    TResult? Function()? submitProfile,
  }) {
    return login?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? login,
    TResult Function()? logout,
    TResult Function()? attemptLoad,
    TResult Function(String mobileNumber, String type)? requestOtp,
    TResult Function(String mobileNumber, String type)? resendOtp,
    TResult Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult Function(String authToken)? submitLogoutUser,
    TResult Function()? refreshToken,
    TResult Function()? submitProfile,
    required TResult orElse(),
  }) {
    if (login != null) {
      return login();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_AuthLoginEvent value) login,
    required TResult Function(_AuthLogoutEvent value) logout,
    required TResult Function(_AuthLoadEvent value) attemptLoad,
    required TResult Function(_RequestOtpEvent value) requestOtp,
    required TResult Function(_ResendOtpEvent value) resendOtp,
    required TResult Function(_SubmitRegistrationOtpEvent value)
        submitRegistrationOtp,
    required TResult Function(_SubmitLoginOtpEvent value) submitLoginOtpEvent,
    required TResult Function(_SubmitLogoutUserEvent value) submitLogoutUser,
    required TResult Function(_AuthRefreshTokenEvent value) refreshToken,
    required TResult Function(_SubmitProfileEvent value) submitProfile,
  }) {
    return login(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_AuthLoginEvent value)? login,
    TResult? Function(_AuthLogoutEvent value)? logout,
    TResult? Function(_AuthLoadEvent value)? attemptLoad,
    TResult? Function(_RequestOtpEvent value)? requestOtp,
    TResult? Function(_ResendOtpEvent value)? resendOtp,
    TResult? Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult? Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult? Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult? Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult? Function(_SubmitProfileEvent value)? submitProfile,
  }) {
    return login?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_AuthLoginEvent value)? login,
    TResult Function(_AuthLogoutEvent value)? logout,
    TResult Function(_AuthLoadEvent value)? attemptLoad,
    TResult Function(_RequestOtpEvent value)? requestOtp,
    TResult Function(_ResendOtpEvent value)? resendOtp,
    TResult Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult Function(_SubmitProfileEvent value)? submitProfile,
    required TResult orElse(),
  }) {
    if (login != null) {
      return login(this);
    }
    return orElse();
  }
}

abstract class _AuthLoginEvent implements AuthEvent {
  const factory _AuthLoginEvent() = _$AuthLoginEventImpl;
}

/// @nodoc
abstract class _$$AuthLogoutEventImplCopyWith<$Res> {
  factory _$$AuthLogoutEventImplCopyWith(_$AuthLogoutEventImpl value,
          $Res Function(_$AuthLogoutEventImpl) then) =
      __$$AuthLogoutEventImplCopyWithImpl<$Res>;
}

/// @nodoc
class __$$AuthLogoutEventImplCopyWithImpl<$Res>
    extends _$AuthEventCopyWithImpl<$Res, _$AuthLogoutEventImpl>
    implements _$$AuthLogoutEventImplCopyWith<$Res> {
  __$$AuthLogoutEventImplCopyWithImpl(
      _$AuthLogoutEventImpl _value, $Res Function(_$AuthLogoutEventImpl) _then)
      : super(_value, _then);
}

/// @nodoc

class _$AuthLogoutEventImpl implements _AuthLogoutEvent {
  const _$AuthLogoutEventImpl();

  @override
  String toString() {
    return 'AuthEvent.logout()';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is _$AuthLogoutEventImpl);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() login,
    required TResult Function() logout,
    required TResult Function() attemptLoad,
    required TResult Function(String mobileNumber, String type) requestOtp,
    required TResult Function(String mobileNumber, String type) resendOtp,
    required TResult Function(String username, String otp, UserModel userModel)
        submitRegistrationOtp,
    required TResult Function(
            String username, String password, UserModel userModel)
        submitLoginOtpEvent,
    required TResult Function(String authToken) submitLogoutUser,
    required TResult Function() refreshToken,
    required TResult Function() submitProfile,
  }) {
    return logout();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? login,
    TResult? Function()? logout,
    TResult? Function()? attemptLoad,
    TResult? Function(String mobileNumber, String type)? requestOtp,
    TResult? Function(String mobileNumber, String type)? resendOtp,
    TResult? Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult? Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult? Function(String authToken)? submitLogoutUser,
    TResult? Function()? refreshToken,
    TResult? Function()? submitProfile,
  }) {
    return logout?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? login,
    TResult Function()? logout,
    TResult Function()? attemptLoad,
    TResult Function(String mobileNumber, String type)? requestOtp,
    TResult Function(String mobileNumber, String type)? resendOtp,
    TResult Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult Function(String authToken)? submitLogoutUser,
    TResult Function()? refreshToken,
    TResult Function()? submitProfile,
    required TResult orElse(),
  }) {
    if (logout != null) {
      return logout();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_AuthLoginEvent value) login,
    required TResult Function(_AuthLogoutEvent value) logout,
    required TResult Function(_AuthLoadEvent value) attemptLoad,
    required TResult Function(_RequestOtpEvent value) requestOtp,
    required TResult Function(_ResendOtpEvent value) resendOtp,
    required TResult Function(_SubmitRegistrationOtpEvent value)
        submitRegistrationOtp,
    required TResult Function(_SubmitLoginOtpEvent value) submitLoginOtpEvent,
    required TResult Function(_SubmitLogoutUserEvent value) submitLogoutUser,
    required TResult Function(_AuthRefreshTokenEvent value) refreshToken,
    required TResult Function(_SubmitProfileEvent value) submitProfile,
  }) {
    return logout(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_AuthLoginEvent value)? login,
    TResult? Function(_AuthLogoutEvent value)? logout,
    TResult? Function(_AuthLoadEvent value)? attemptLoad,
    TResult? Function(_RequestOtpEvent value)? requestOtp,
    TResult? Function(_ResendOtpEvent value)? resendOtp,
    TResult? Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult? Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult? Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult? Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult? Function(_SubmitProfileEvent value)? submitProfile,
  }) {
    return logout?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_AuthLoginEvent value)? login,
    TResult Function(_AuthLogoutEvent value)? logout,
    TResult Function(_AuthLoadEvent value)? attemptLoad,
    TResult Function(_RequestOtpEvent value)? requestOtp,
    TResult Function(_ResendOtpEvent value)? resendOtp,
    TResult Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult Function(_SubmitProfileEvent value)? submitProfile,
    required TResult orElse(),
  }) {
    if (logout != null) {
      return logout(this);
    }
    return orElse();
  }
}

abstract class _AuthLogoutEvent implements AuthEvent {
  const factory _AuthLogoutEvent() = _$AuthLogoutEventImpl;
}

/// @nodoc
abstract class _$$AuthLoadEventImplCopyWith<$Res> {
  factory _$$AuthLoadEventImplCopyWith(
          _$AuthLoadEventImpl value, $Res Function(_$AuthLoadEventImpl) then) =
      __$$AuthLoadEventImplCopyWithImpl<$Res>;
}

/// @nodoc
class __$$AuthLoadEventImplCopyWithImpl<$Res>
    extends _$AuthEventCopyWithImpl<$Res, _$AuthLoadEventImpl>
    implements _$$AuthLoadEventImplCopyWith<$Res> {
  __$$AuthLoadEventImplCopyWithImpl(
      _$AuthLoadEventImpl _value, $Res Function(_$AuthLoadEventImpl) _then)
      : super(_value, _then);
}

/// @nodoc

class _$AuthLoadEventImpl implements _AuthLoadEvent {
  const _$AuthLoadEventImpl();

  @override
  String toString() {
    return 'AuthEvent.attemptLoad()';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is _$AuthLoadEventImpl);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() login,
    required TResult Function() logout,
    required TResult Function() attemptLoad,
    required TResult Function(String mobileNumber, String type) requestOtp,
    required TResult Function(String mobileNumber, String type) resendOtp,
    required TResult Function(String username, String otp, UserModel userModel)
        submitRegistrationOtp,
    required TResult Function(
            String username, String password, UserModel userModel)
        submitLoginOtpEvent,
    required TResult Function(String authToken) submitLogoutUser,
    required TResult Function() refreshToken,
    required TResult Function() submitProfile,
  }) {
    return attemptLoad();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? login,
    TResult? Function()? logout,
    TResult? Function()? attemptLoad,
    TResult? Function(String mobileNumber, String type)? requestOtp,
    TResult? Function(String mobileNumber, String type)? resendOtp,
    TResult? Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult? Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult? Function(String authToken)? submitLogoutUser,
    TResult? Function()? refreshToken,
    TResult? Function()? submitProfile,
  }) {
    return attemptLoad?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? login,
    TResult Function()? logout,
    TResult Function()? attemptLoad,
    TResult Function(String mobileNumber, String type)? requestOtp,
    TResult Function(String mobileNumber, String type)? resendOtp,
    TResult Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult Function(String authToken)? submitLogoutUser,
    TResult Function()? refreshToken,
    TResult Function()? submitProfile,
    required TResult orElse(),
  }) {
    if (attemptLoad != null) {
      return attemptLoad();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_AuthLoginEvent value) login,
    required TResult Function(_AuthLogoutEvent value) logout,
    required TResult Function(_AuthLoadEvent value) attemptLoad,
    required TResult Function(_RequestOtpEvent value) requestOtp,
    required TResult Function(_ResendOtpEvent value) resendOtp,
    required TResult Function(_SubmitRegistrationOtpEvent value)
        submitRegistrationOtp,
    required TResult Function(_SubmitLoginOtpEvent value) submitLoginOtpEvent,
    required TResult Function(_SubmitLogoutUserEvent value) submitLogoutUser,
    required TResult Function(_AuthRefreshTokenEvent value) refreshToken,
    required TResult Function(_SubmitProfileEvent value) submitProfile,
  }) {
    return attemptLoad(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_AuthLoginEvent value)? login,
    TResult? Function(_AuthLogoutEvent value)? logout,
    TResult? Function(_AuthLoadEvent value)? attemptLoad,
    TResult? Function(_RequestOtpEvent value)? requestOtp,
    TResult? Function(_ResendOtpEvent value)? resendOtp,
    TResult? Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult? Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult? Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult? Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult? Function(_SubmitProfileEvent value)? submitProfile,
  }) {
    return attemptLoad?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_AuthLoginEvent value)? login,
    TResult Function(_AuthLogoutEvent value)? logout,
    TResult Function(_AuthLoadEvent value)? attemptLoad,
    TResult Function(_RequestOtpEvent value)? requestOtp,
    TResult Function(_ResendOtpEvent value)? resendOtp,
    TResult Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult Function(_SubmitProfileEvent value)? submitProfile,
    required TResult orElse(),
  }) {
    if (attemptLoad != null) {
      return attemptLoad(this);
    }
    return orElse();
  }
}

abstract class _AuthLoadEvent implements AuthEvent {
  const factory _AuthLoadEvent() = _$AuthLoadEventImpl;
}

/// @nodoc
abstract class _$$RequestOtpEventImplCopyWith<$Res> {
  factory _$$RequestOtpEventImplCopyWith(_$RequestOtpEventImpl value,
          $Res Function(_$RequestOtpEventImpl) then) =
      __$$RequestOtpEventImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String mobileNumber, String type});
}

/// @nodoc
class __$$RequestOtpEventImplCopyWithImpl<$Res>
    extends _$AuthEventCopyWithImpl<$Res, _$RequestOtpEventImpl>
    implements _$$RequestOtpEventImplCopyWith<$Res> {
  __$$RequestOtpEventImplCopyWithImpl(
      _$RequestOtpEventImpl _value, $Res Function(_$RequestOtpEventImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? mobileNumber = null,
    Object? type = null,
  }) {
    return _then(_$RequestOtpEventImpl(
      null == mobileNumber
          ? _value.mobileNumber
          : mobileNumber // ignore: cast_nullable_to_non_nullable
              as String,
      null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$RequestOtpEventImpl implements _RequestOtpEvent {
  const _$RequestOtpEventImpl(this.mobileNumber, this.type);

  @override
  final String mobileNumber;
  @override
  final String type;

  @override
  String toString() {
    return 'AuthEvent.requestOtp(mobileNumber: $mobileNumber, type: $type)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$RequestOtpEventImpl &&
            (identical(other.mobileNumber, mobileNumber) ||
                other.mobileNumber == mobileNumber) &&
            (identical(other.type, type) || other.type == type));
  }

  @override
  int get hashCode => Object.hash(runtimeType, mobileNumber, type);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$RequestOtpEventImplCopyWith<_$RequestOtpEventImpl> get copyWith =>
      __$$RequestOtpEventImplCopyWithImpl<_$RequestOtpEventImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() login,
    required TResult Function() logout,
    required TResult Function() attemptLoad,
    required TResult Function(String mobileNumber, String type) requestOtp,
    required TResult Function(String mobileNumber, String type) resendOtp,
    required TResult Function(String username, String otp, UserModel userModel)
        submitRegistrationOtp,
    required TResult Function(
            String username, String password, UserModel userModel)
        submitLoginOtpEvent,
    required TResult Function(String authToken) submitLogoutUser,
    required TResult Function() refreshToken,
    required TResult Function() submitProfile,
  }) {
    return requestOtp(mobileNumber, type);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? login,
    TResult? Function()? logout,
    TResult? Function()? attemptLoad,
    TResult? Function(String mobileNumber, String type)? requestOtp,
    TResult? Function(String mobileNumber, String type)? resendOtp,
    TResult? Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult? Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult? Function(String authToken)? submitLogoutUser,
    TResult? Function()? refreshToken,
    TResult? Function()? submitProfile,
  }) {
    return requestOtp?.call(mobileNumber, type);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? login,
    TResult Function()? logout,
    TResult Function()? attemptLoad,
    TResult Function(String mobileNumber, String type)? requestOtp,
    TResult Function(String mobileNumber, String type)? resendOtp,
    TResult Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult Function(String authToken)? submitLogoutUser,
    TResult Function()? refreshToken,
    TResult Function()? submitProfile,
    required TResult orElse(),
  }) {
    if (requestOtp != null) {
      return requestOtp(mobileNumber, type);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_AuthLoginEvent value) login,
    required TResult Function(_AuthLogoutEvent value) logout,
    required TResult Function(_AuthLoadEvent value) attemptLoad,
    required TResult Function(_RequestOtpEvent value) requestOtp,
    required TResult Function(_ResendOtpEvent value) resendOtp,
    required TResult Function(_SubmitRegistrationOtpEvent value)
        submitRegistrationOtp,
    required TResult Function(_SubmitLoginOtpEvent value) submitLoginOtpEvent,
    required TResult Function(_SubmitLogoutUserEvent value) submitLogoutUser,
    required TResult Function(_AuthRefreshTokenEvent value) refreshToken,
    required TResult Function(_SubmitProfileEvent value) submitProfile,
  }) {
    return requestOtp(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_AuthLoginEvent value)? login,
    TResult? Function(_AuthLogoutEvent value)? logout,
    TResult? Function(_AuthLoadEvent value)? attemptLoad,
    TResult? Function(_RequestOtpEvent value)? requestOtp,
    TResult? Function(_ResendOtpEvent value)? resendOtp,
    TResult? Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult? Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult? Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult? Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult? Function(_SubmitProfileEvent value)? submitProfile,
  }) {
    return requestOtp?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_AuthLoginEvent value)? login,
    TResult Function(_AuthLogoutEvent value)? logout,
    TResult Function(_AuthLoadEvent value)? attemptLoad,
    TResult Function(_RequestOtpEvent value)? requestOtp,
    TResult Function(_ResendOtpEvent value)? resendOtp,
    TResult Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult Function(_SubmitProfileEvent value)? submitProfile,
    required TResult orElse(),
  }) {
    if (requestOtp != null) {
      return requestOtp(this);
    }
    return orElse();
  }
}

abstract class _RequestOtpEvent implements AuthEvent {
  const factory _RequestOtpEvent(final String mobileNumber, final String type) =
      _$RequestOtpEventImpl;

  String get mobileNumber;
  String get type;
  @JsonKey(ignore: true)
  _$$RequestOtpEventImplCopyWith<_$RequestOtpEventImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$ResendOtpEventImplCopyWith<$Res> {
  factory _$$ResendOtpEventImplCopyWith(_$ResendOtpEventImpl value,
          $Res Function(_$ResendOtpEventImpl) then) =
      __$$ResendOtpEventImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String mobileNumber, String type});
}

/// @nodoc
class __$$ResendOtpEventImplCopyWithImpl<$Res>
    extends _$AuthEventCopyWithImpl<$Res, _$ResendOtpEventImpl>
    implements _$$ResendOtpEventImplCopyWith<$Res> {
  __$$ResendOtpEventImplCopyWithImpl(
      _$ResendOtpEventImpl _value, $Res Function(_$ResendOtpEventImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? mobileNumber = null,
    Object? type = null,
  }) {
    return _then(_$ResendOtpEventImpl(
      null == mobileNumber
          ? _value.mobileNumber
          : mobileNumber // ignore: cast_nullable_to_non_nullable
              as String,
      null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$ResendOtpEventImpl implements _ResendOtpEvent {
  const _$ResendOtpEventImpl(this.mobileNumber, this.type);

  @override
  final String mobileNumber;
  @override
  final String type;

  @override
  String toString() {
    return 'AuthEvent.resendOtp(mobileNumber: $mobileNumber, type: $type)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ResendOtpEventImpl &&
            (identical(other.mobileNumber, mobileNumber) ||
                other.mobileNumber == mobileNumber) &&
            (identical(other.type, type) || other.type == type));
  }

  @override
  int get hashCode => Object.hash(runtimeType, mobileNumber, type);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$ResendOtpEventImplCopyWith<_$ResendOtpEventImpl> get copyWith =>
      __$$ResendOtpEventImplCopyWithImpl<_$ResendOtpEventImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() login,
    required TResult Function() logout,
    required TResult Function() attemptLoad,
    required TResult Function(String mobileNumber, String type) requestOtp,
    required TResult Function(String mobileNumber, String type) resendOtp,
    required TResult Function(String username, String otp, UserModel userModel)
        submitRegistrationOtp,
    required TResult Function(
            String username, String password, UserModel userModel)
        submitLoginOtpEvent,
    required TResult Function(String authToken) submitLogoutUser,
    required TResult Function() refreshToken,
    required TResult Function() submitProfile,
  }) {
    return resendOtp(mobileNumber, type);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? login,
    TResult? Function()? logout,
    TResult? Function()? attemptLoad,
    TResult? Function(String mobileNumber, String type)? requestOtp,
    TResult? Function(String mobileNumber, String type)? resendOtp,
    TResult? Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult? Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult? Function(String authToken)? submitLogoutUser,
    TResult? Function()? refreshToken,
    TResult? Function()? submitProfile,
  }) {
    return resendOtp?.call(mobileNumber, type);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? login,
    TResult Function()? logout,
    TResult Function()? attemptLoad,
    TResult Function(String mobileNumber, String type)? requestOtp,
    TResult Function(String mobileNumber, String type)? resendOtp,
    TResult Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult Function(String authToken)? submitLogoutUser,
    TResult Function()? refreshToken,
    TResult Function()? submitProfile,
    required TResult orElse(),
  }) {
    if (resendOtp != null) {
      return resendOtp(mobileNumber, type);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_AuthLoginEvent value) login,
    required TResult Function(_AuthLogoutEvent value) logout,
    required TResult Function(_AuthLoadEvent value) attemptLoad,
    required TResult Function(_RequestOtpEvent value) requestOtp,
    required TResult Function(_ResendOtpEvent value) resendOtp,
    required TResult Function(_SubmitRegistrationOtpEvent value)
        submitRegistrationOtp,
    required TResult Function(_SubmitLoginOtpEvent value) submitLoginOtpEvent,
    required TResult Function(_SubmitLogoutUserEvent value) submitLogoutUser,
    required TResult Function(_AuthRefreshTokenEvent value) refreshToken,
    required TResult Function(_SubmitProfileEvent value) submitProfile,
  }) {
    return resendOtp(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_AuthLoginEvent value)? login,
    TResult? Function(_AuthLogoutEvent value)? logout,
    TResult? Function(_AuthLoadEvent value)? attemptLoad,
    TResult? Function(_RequestOtpEvent value)? requestOtp,
    TResult? Function(_ResendOtpEvent value)? resendOtp,
    TResult? Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult? Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult? Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult? Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult? Function(_SubmitProfileEvent value)? submitProfile,
  }) {
    return resendOtp?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_AuthLoginEvent value)? login,
    TResult Function(_AuthLogoutEvent value)? logout,
    TResult Function(_AuthLoadEvent value)? attemptLoad,
    TResult Function(_RequestOtpEvent value)? requestOtp,
    TResult Function(_ResendOtpEvent value)? resendOtp,
    TResult Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult Function(_SubmitProfileEvent value)? submitProfile,
    required TResult orElse(),
  }) {
    if (resendOtp != null) {
      return resendOtp(this);
    }
    return orElse();
  }
}

abstract class _ResendOtpEvent implements AuthEvent {
  const factory _ResendOtpEvent(final String mobileNumber, final String type) =
      _$ResendOtpEventImpl;

  String get mobileNumber;
  String get type;
  @JsonKey(ignore: true)
  _$$ResendOtpEventImplCopyWith<_$ResendOtpEventImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$SubmitRegistrationOtpEventImplCopyWith<$Res> {
  factory _$$SubmitRegistrationOtpEventImplCopyWith(
          _$SubmitRegistrationOtpEventImpl value,
          $Res Function(_$SubmitRegistrationOtpEventImpl) then) =
      __$$SubmitRegistrationOtpEventImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String username, String otp, UserModel userModel});
}

/// @nodoc
class __$$SubmitRegistrationOtpEventImplCopyWithImpl<$Res>
    extends _$AuthEventCopyWithImpl<$Res, _$SubmitRegistrationOtpEventImpl>
    implements _$$SubmitRegistrationOtpEventImplCopyWith<$Res> {
  __$$SubmitRegistrationOtpEventImplCopyWithImpl(
      _$SubmitRegistrationOtpEventImpl _value,
      $Res Function(_$SubmitRegistrationOtpEventImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? username = null,
    Object? otp = null,
    Object? userModel = null,
  }) {
    return _then(_$SubmitRegistrationOtpEventImpl(
      null == username
          ? _value.username
          : username // ignore: cast_nullable_to_non_nullable
              as String,
      null == otp
          ? _value.otp
          : otp // ignore: cast_nullable_to_non_nullable
              as String,
      null == userModel
          ? _value.userModel
          : userModel // ignore: cast_nullable_to_non_nullable
              as UserModel,
    ));
  }
}

/// @nodoc

class _$SubmitRegistrationOtpEventImpl implements _SubmitRegistrationOtpEvent {
  const _$SubmitRegistrationOtpEventImpl(
      this.username, this.otp, this.userModel);

  @override
  final String username;
  @override
  final String otp;
  @override
  final UserModel userModel;

  @override
  String toString() {
    return 'AuthEvent.submitRegistrationOtp(username: $username, otp: $otp, userModel: $userModel)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$SubmitRegistrationOtpEventImpl &&
            (identical(other.username, username) ||
                other.username == username) &&
            (identical(other.otp, otp) || other.otp == otp) &&
            (identical(other.userModel, userModel) ||
                other.userModel == userModel));
  }

  @override
  int get hashCode => Object.hash(runtimeType, username, otp, userModel);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$SubmitRegistrationOtpEventImplCopyWith<_$SubmitRegistrationOtpEventImpl>
      get copyWith => __$$SubmitRegistrationOtpEventImplCopyWithImpl<
          _$SubmitRegistrationOtpEventImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() login,
    required TResult Function() logout,
    required TResult Function() attemptLoad,
    required TResult Function(String mobileNumber, String type) requestOtp,
    required TResult Function(String mobileNumber, String type) resendOtp,
    required TResult Function(String username, String otp, UserModel userModel)
        submitRegistrationOtp,
    required TResult Function(
            String username, String password, UserModel userModel)
        submitLoginOtpEvent,
    required TResult Function(String authToken) submitLogoutUser,
    required TResult Function() refreshToken,
    required TResult Function() submitProfile,
  }) {
    return submitRegistrationOtp(username, otp, userModel);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? login,
    TResult? Function()? logout,
    TResult? Function()? attemptLoad,
    TResult? Function(String mobileNumber, String type)? requestOtp,
    TResult? Function(String mobileNumber, String type)? resendOtp,
    TResult? Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult? Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult? Function(String authToken)? submitLogoutUser,
    TResult? Function()? refreshToken,
    TResult? Function()? submitProfile,
  }) {
    return submitRegistrationOtp?.call(username, otp, userModel);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? login,
    TResult Function()? logout,
    TResult Function()? attemptLoad,
    TResult Function(String mobileNumber, String type)? requestOtp,
    TResult Function(String mobileNumber, String type)? resendOtp,
    TResult Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult Function(String authToken)? submitLogoutUser,
    TResult Function()? refreshToken,
    TResult Function()? submitProfile,
    required TResult orElse(),
  }) {
    if (submitRegistrationOtp != null) {
      return submitRegistrationOtp(username, otp, userModel);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_AuthLoginEvent value) login,
    required TResult Function(_AuthLogoutEvent value) logout,
    required TResult Function(_AuthLoadEvent value) attemptLoad,
    required TResult Function(_RequestOtpEvent value) requestOtp,
    required TResult Function(_ResendOtpEvent value) resendOtp,
    required TResult Function(_SubmitRegistrationOtpEvent value)
        submitRegistrationOtp,
    required TResult Function(_SubmitLoginOtpEvent value) submitLoginOtpEvent,
    required TResult Function(_SubmitLogoutUserEvent value) submitLogoutUser,
    required TResult Function(_AuthRefreshTokenEvent value) refreshToken,
    required TResult Function(_SubmitProfileEvent value) submitProfile,
  }) {
    return submitRegistrationOtp(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_AuthLoginEvent value)? login,
    TResult? Function(_AuthLogoutEvent value)? logout,
    TResult? Function(_AuthLoadEvent value)? attemptLoad,
    TResult? Function(_RequestOtpEvent value)? requestOtp,
    TResult? Function(_ResendOtpEvent value)? resendOtp,
    TResult? Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult? Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult? Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult? Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult? Function(_SubmitProfileEvent value)? submitProfile,
  }) {
    return submitRegistrationOtp?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_AuthLoginEvent value)? login,
    TResult Function(_AuthLogoutEvent value)? logout,
    TResult Function(_AuthLoadEvent value)? attemptLoad,
    TResult Function(_RequestOtpEvent value)? requestOtp,
    TResult Function(_ResendOtpEvent value)? resendOtp,
    TResult Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult Function(_SubmitProfileEvent value)? submitProfile,
    required TResult orElse(),
  }) {
    if (submitRegistrationOtp != null) {
      return submitRegistrationOtp(this);
    }
    return orElse();
  }
}

abstract class _SubmitRegistrationOtpEvent implements AuthEvent {
  const factory _SubmitRegistrationOtpEvent(
          final String username, final String otp, final UserModel userModel) =
      _$SubmitRegistrationOtpEventImpl;

  String get username;
  String get otp;
  UserModel get userModel;
  @JsonKey(ignore: true)
  _$$SubmitRegistrationOtpEventImplCopyWith<_$SubmitRegistrationOtpEventImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$SubmitLoginOtpEventImplCopyWith<$Res> {
  factory _$$SubmitLoginOtpEventImplCopyWith(_$SubmitLoginOtpEventImpl value,
          $Res Function(_$SubmitLoginOtpEventImpl) then) =
      __$$SubmitLoginOtpEventImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String username, String password, UserModel userModel});
}

/// @nodoc
class __$$SubmitLoginOtpEventImplCopyWithImpl<$Res>
    extends _$AuthEventCopyWithImpl<$Res, _$SubmitLoginOtpEventImpl>
    implements _$$SubmitLoginOtpEventImplCopyWith<$Res> {
  __$$SubmitLoginOtpEventImplCopyWithImpl(_$SubmitLoginOtpEventImpl _value,
      $Res Function(_$SubmitLoginOtpEventImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? username = null,
    Object? password = null,
    Object? userModel = null,
  }) {
    return _then(_$SubmitLoginOtpEventImpl(
      null == username
          ? _value.username
          : username // ignore: cast_nullable_to_non_nullable
              as String,
      null == password
          ? _value.password
          : password // ignore: cast_nullable_to_non_nullable
              as String,
      null == userModel
          ? _value.userModel
          : userModel // ignore: cast_nullable_to_non_nullable
              as UserModel,
    ));
  }
}

/// @nodoc

class _$SubmitLoginOtpEventImpl implements _SubmitLoginOtpEvent {
  const _$SubmitLoginOtpEventImpl(this.username, this.password, this.userModel);

  @override
  final String username;
  @override
  final String password;
  @override
  final UserModel userModel;

  @override
  String toString() {
    return 'AuthEvent.submitLoginOtpEvent(username: $username, password: $password, userModel: $userModel)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$SubmitLoginOtpEventImpl &&
            (identical(other.username, username) ||
                other.username == username) &&
            (identical(other.password, password) ||
                other.password == password) &&
            (identical(other.userModel, userModel) ||
                other.userModel == userModel));
  }

  @override
  int get hashCode => Object.hash(runtimeType, username, password, userModel);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$SubmitLoginOtpEventImplCopyWith<_$SubmitLoginOtpEventImpl> get copyWith =>
      __$$SubmitLoginOtpEventImplCopyWithImpl<_$SubmitLoginOtpEventImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() login,
    required TResult Function() logout,
    required TResult Function() attemptLoad,
    required TResult Function(String mobileNumber, String type) requestOtp,
    required TResult Function(String mobileNumber, String type) resendOtp,
    required TResult Function(String username, String otp, UserModel userModel)
        submitRegistrationOtp,
    required TResult Function(
            String username, String password, UserModel userModel)
        submitLoginOtpEvent,
    required TResult Function(String authToken) submitLogoutUser,
    required TResult Function() refreshToken,
    required TResult Function() submitProfile,
  }) {
    return submitLoginOtpEvent(username, password, userModel);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? login,
    TResult? Function()? logout,
    TResult? Function()? attemptLoad,
    TResult? Function(String mobileNumber, String type)? requestOtp,
    TResult? Function(String mobileNumber, String type)? resendOtp,
    TResult? Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult? Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult? Function(String authToken)? submitLogoutUser,
    TResult? Function()? refreshToken,
    TResult? Function()? submitProfile,
  }) {
    return submitLoginOtpEvent?.call(username, password, userModel);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? login,
    TResult Function()? logout,
    TResult Function()? attemptLoad,
    TResult Function(String mobileNumber, String type)? requestOtp,
    TResult Function(String mobileNumber, String type)? resendOtp,
    TResult Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult Function(String authToken)? submitLogoutUser,
    TResult Function()? refreshToken,
    TResult Function()? submitProfile,
    required TResult orElse(),
  }) {
    if (submitLoginOtpEvent != null) {
      return submitLoginOtpEvent(username, password, userModel);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_AuthLoginEvent value) login,
    required TResult Function(_AuthLogoutEvent value) logout,
    required TResult Function(_AuthLoadEvent value) attemptLoad,
    required TResult Function(_RequestOtpEvent value) requestOtp,
    required TResult Function(_ResendOtpEvent value) resendOtp,
    required TResult Function(_SubmitRegistrationOtpEvent value)
        submitRegistrationOtp,
    required TResult Function(_SubmitLoginOtpEvent value) submitLoginOtpEvent,
    required TResult Function(_SubmitLogoutUserEvent value) submitLogoutUser,
    required TResult Function(_AuthRefreshTokenEvent value) refreshToken,
    required TResult Function(_SubmitProfileEvent value) submitProfile,
  }) {
    return submitLoginOtpEvent(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_AuthLoginEvent value)? login,
    TResult? Function(_AuthLogoutEvent value)? logout,
    TResult? Function(_AuthLoadEvent value)? attemptLoad,
    TResult? Function(_RequestOtpEvent value)? requestOtp,
    TResult? Function(_ResendOtpEvent value)? resendOtp,
    TResult? Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult? Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult? Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult? Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult? Function(_SubmitProfileEvent value)? submitProfile,
  }) {
    return submitLoginOtpEvent?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_AuthLoginEvent value)? login,
    TResult Function(_AuthLogoutEvent value)? logout,
    TResult Function(_AuthLoadEvent value)? attemptLoad,
    TResult Function(_RequestOtpEvent value)? requestOtp,
    TResult Function(_ResendOtpEvent value)? resendOtp,
    TResult Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult Function(_SubmitProfileEvent value)? submitProfile,
    required TResult orElse(),
  }) {
    if (submitLoginOtpEvent != null) {
      return submitLoginOtpEvent(this);
    }
    return orElse();
  }
}

abstract class _SubmitLoginOtpEvent implements AuthEvent {
  const factory _SubmitLoginOtpEvent(
      final String username,
      final String password,
      final UserModel userModel) = _$SubmitLoginOtpEventImpl;

  String get username;
  String get password;
  UserModel get userModel;
  @JsonKey(ignore: true)
  _$$SubmitLoginOtpEventImplCopyWith<_$SubmitLoginOtpEventImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$SubmitLogoutUserEventImplCopyWith<$Res> {
  factory _$$SubmitLogoutUserEventImplCopyWith(
          _$SubmitLogoutUserEventImpl value,
          $Res Function(_$SubmitLogoutUserEventImpl) then) =
      __$$SubmitLogoutUserEventImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String authToken});
}

/// @nodoc
class __$$SubmitLogoutUserEventImplCopyWithImpl<$Res>
    extends _$AuthEventCopyWithImpl<$Res, _$SubmitLogoutUserEventImpl>
    implements _$$SubmitLogoutUserEventImplCopyWith<$Res> {
  __$$SubmitLogoutUserEventImplCopyWithImpl(_$SubmitLogoutUserEventImpl _value,
      $Res Function(_$SubmitLogoutUserEventImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? authToken = null,
  }) {
    return _then(_$SubmitLogoutUserEventImpl(
      null == authToken
          ? _value.authToken
          : authToken // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$SubmitLogoutUserEventImpl implements _SubmitLogoutUserEvent {
  const _$SubmitLogoutUserEventImpl(this.authToken);

  @override
  final String authToken;

  @override
  String toString() {
    return 'AuthEvent.submitLogoutUser(authToken: $authToken)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$SubmitLogoutUserEventImpl &&
            (identical(other.authToken, authToken) ||
                other.authToken == authToken));
  }

  @override
  int get hashCode => Object.hash(runtimeType, authToken);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$SubmitLogoutUserEventImplCopyWith<_$SubmitLogoutUserEventImpl>
      get copyWith => __$$SubmitLogoutUserEventImplCopyWithImpl<
          _$SubmitLogoutUserEventImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() login,
    required TResult Function() logout,
    required TResult Function() attemptLoad,
    required TResult Function(String mobileNumber, String type) requestOtp,
    required TResult Function(String mobileNumber, String type) resendOtp,
    required TResult Function(String username, String otp, UserModel userModel)
        submitRegistrationOtp,
    required TResult Function(
            String username, String password, UserModel userModel)
        submitLoginOtpEvent,
    required TResult Function(String authToken) submitLogoutUser,
    required TResult Function() refreshToken,
    required TResult Function() submitProfile,
  }) {
    return submitLogoutUser(authToken);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? login,
    TResult? Function()? logout,
    TResult? Function()? attemptLoad,
    TResult? Function(String mobileNumber, String type)? requestOtp,
    TResult? Function(String mobileNumber, String type)? resendOtp,
    TResult? Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult? Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult? Function(String authToken)? submitLogoutUser,
    TResult? Function()? refreshToken,
    TResult? Function()? submitProfile,
  }) {
    return submitLogoutUser?.call(authToken);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? login,
    TResult Function()? logout,
    TResult Function()? attemptLoad,
    TResult Function(String mobileNumber, String type)? requestOtp,
    TResult Function(String mobileNumber, String type)? resendOtp,
    TResult Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult Function(String authToken)? submitLogoutUser,
    TResult Function()? refreshToken,
    TResult Function()? submitProfile,
    required TResult orElse(),
  }) {
    if (submitLogoutUser != null) {
      return submitLogoutUser(authToken);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_AuthLoginEvent value) login,
    required TResult Function(_AuthLogoutEvent value) logout,
    required TResult Function(_AuthLoadEvent value) attemptLoad,
    required TResult Function(_RequestOtpEvent value) requestOtp,
    required TResult Function(_ResendOtpEvent value) resendOtp,
    required TResult Function(_SubmitRegistrationOtpEvent value)
        submitRegistrationOtp,
    required TResult Function(_SubmitLoginOtpEvent value) submitLoginOtpEvent,
    required TResult Function(_SubmitLogoutUserEvent value) submitLogoutUser,
    required TResult Function(_AuthRefreshTokenEvent value) refreshToken,
    required TResult Function(_SubmitProfileEvent value) submitProfile,
  }) {
    return submitLogoutUser(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_AuthLoginEvent value)? login,
    TResult? Function(_AuthLogoutEvent value)? logout,
    TResult? Function(_AuthLoadEvent value)? attemptLoad,
    TResult? Function(_RequestOtpEvent value)? requestOtp,
    TResult? Function(_ResendOtpEvent value)? resendOtp,
    TResult? Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult? Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult? Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult? Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult? Function(_SubmitProfileEvent value)? submitProfile,
  }) {
    return submitLogoutUser?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_AuthLoginEvent value)? login,
    TResult Function(_AuthLogoutEvent value)? logout,
    TResult Function(_AuthLoadEvent value)? attemptLoad,
    TResult Function(_RequestOtpEvent value)? requestOtp,
    TResult Function(_ResendOtpEvent value)? resendOtp,
    TResult Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult Function(_SubmitProfileEvent value)? submitProfile,
    required TResult orElse(),
  }) {
    if (submitLogoutUser != null) {
      return submitLogoutUser(this);
    }
    return orElse();
  }
}

abstract class _SubmitLogoutUserEvent implements AuthEvent {
  const factory _SubmitLogoutUserEvent(final String authToken) =
      _$SubmitLogoutUserEventImpl;

  String get authToken;
  @JsonKey(ignore: true)
  _$$SubmitLogoutUserEventImplCopyWith<_$SubmitLogoutUserEventImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$AuthRefreshTokenEventImplCopyWith<$Res> {
  factory _$$AuthRefreshTokenEventImplCopyWith(
          _$AuthRefreshTokenEventImpl value,
          $Res Function(_$AuthRefreshTokenEventImpl) then) =
      __$$AuthRefreshTokenEventImplCopyWithImpl<$Res>;
}

/// @nodoc
class __$$AuthRefreshTokenEventImplCopyWithImpl<$Res>
    extends _$AuthEventCopyWithImpl<$Res, _$AuthRefreshTokenEventImpl>
    implements _$$AuthRefreshTokenEventImplCopyWith<$Res> {
  __$$AuthRefreshTokenEventImplCopyWithImpl(_$AuthRefreshTokenEventImpl _value,
      $Res Function(_$AuthRefreshTokenEventImpl) _then)
      : super(_value, _then);
}

/// @nodoc

class _$AuthRefreshTokenEventImpl implements _AuthRefreshTokenEvent {
  const _$AuthRefreshTokenEventImpl();

  @override
  String toString() {
    return 'AuthEvent.refreshToken()';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AuthRefreshTokenEventImpl);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() login,
    required TResult Function() logout,
    required TResult Function() attemptLoad,
    required TResult Function(String mobileNumber, String type) requestOtp,
    required TResult Function(String mobileNumber, String type) resendOtp,
    required TResult Function(String username, String otp, UserModel userModel)
        submitRegistrationOtp,
    required TResult Function(
            String username, String password, UserModel userModel)
        submitLoginOtpEvent,
    required TResult Function(String authToken) submitLogoutUser,
    required TResult Function() refreshToken,
    required TResult Function() submitProfile,
  }) {
    return refreshToken();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? login,
    TResult? Function()? logout,
    TResult? Function()? attemptLoad,
    TResult? Function(String mobileNumber, String type)? requestOtp,
    TResult? Function(String mobileNumber, String type)? resendOtp,
    TResult? Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult? Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult? Function(String authToken)? submitLogoutUser,
    TResult? Function()? refreshToken,
    TResult? Function()? submitProfile,
  }) {
    return refreshToken?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? login,
    TResult Function()? logout,
    TResult Function()? attemptLoad,
    TResult Function(String mobileNumber, String type)? requestOtp,
    TResult Function(String mobileNumber, String type)? resendOtp,
    TResult Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult Function(String authToken)? submitLogoutUser,
    TResult Function()? refreshToken,
    TResult Function()? submitProfile,
    required TResult orElse(),
  }) {
    if (refreshToken != null) {
      return refreshToken();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_AuthLoginEvent value) login,
    required TResult Function(_AuthLogoutEvent value) logout,
    required TResult Function(_AuthLoadEvent value) attemptLoad,
    required TResult Function(_RequestOtpEvent value) requestOtp,
    required TResult Function(_ResendOtpEvent value) resendOtp,
    required TResult Function(_SubmitRegistrationOtpEvent value)
        submitRegistrationOtp,
    required TResult Function(_SubmitLoginOtpEvent value) submitLoginOtpEvent,
    required TResult Function(_SubmitLogoutUserEvent value) submitLogoutUser,
    required TResult Function(_AuthRefreshTokenEvent value) refreshToken,
    required TResult Function(_SubmitProfileEvent value) submitProfile,
  }) {
    return refreshToken(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_AuthLoginEvent value)? login,
    TResult? Function(_AuthLogoutEvent value)? logout,
    TResult? Function(_AuthLoadEvent value)? attemptLoad,
    TResult? Function(_RequestOtpEvent value)? requestOtp,
    TResult? Function(_ResendOtpEvent value)? resendOtp,
    TResult? Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult? Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult? Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult? Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult? Function(_SubmitProfileEvent value)? submitProfile,
  }) {
    return refreshToken?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_AuthLoginEvent value)? login,
    TResult Function(_AuthLogoutEvent value)? logout,
    TResult Function(_AuthLoadEvent value)? attemptLoad,
    TResult Function(_RequestOtpEvent value)? requestOtp,
    TResult Function(_ResendOtpEvent value)? resendOtp,
    TResult Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult Function(_SubmitProfileEvent value)? submitProfile,
    required TResult orElse(),
  }) {
    if (refreshToken != null) {
      return refreshToken(this);
    }
    return orElse();
  }
}

abstract class _AuthRefreshTokenEvent implements AuthEvent {
  const factory _AuthRefreshTokenEvent() = _$AuthRefreshTokenEventImpl;
}

/// @nodoc
abstract class _$$SubmitProfileEventImplCopyWith<$Res> {
  factory _$$SubmitProfileEventImplCopyWith(_$SubmitProfileEventImpl value,
          $Res Function(_$SubmitProfileEventImpl) then) =
      __$$SubmitProfileEventImplCopyWithImpl<$Res>;
}

/// @nodoc
class __$$SubmitProfileEventImplCopyWithImpl<$Res>
    extends _$AuthEventCopyWithImpl<$Res, _$SubmitProfileEventImpl>
    implements _$$SubmitProfileEventImplCopyWith<$Res> {
  __$$SubmitProfileEventImplCopyWithImpl(_$SubmitProfileEventImpl _value,
      $Res Function(_$SubmitProfileEventImpl) _then)
      : super(_value, _then);
}

/// @nodoc

class _$SubmitProfileEventImpl implements _SubmitProfileEvent {
  const _$SubmitProfileEventImpl();

  @override
  String toString() {
    return 'AuthEvent.submitProfile()';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is _$SubmitProfileEventImpl);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() login,
    required TResult Function() logout,
    required TResult Function() attemptLoad,
    required TResult Function(String mobileNumber, String type) requestOtp,
    required TResult Function(String mobileNumber, String type) resendOtp,
    required TResult Function(String username, String otp, UserModel userModel)
        submitRegistrationOtp,
    required TResult Function(
            String username, String password, UserModel userModel)
        submitLoginOtpEvent,
    required TResult Function(String authToken) submitLogoutUser,
    required TResult Function() refreshToken,
    required TResult Function() submitProfile,
  }) {
    return submitProfile();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? login,
    TResult? Function()? logout,
    TResult? Function()? attemptLoad,
    TResult? Function(String mobileNumber, String type)? requestOtp,
    TResult? Function(String mobileNumber, String type)? resendOtp,
    TResult? Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult? Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult? Function(String authToken)? submitLogoutUser,
    TResult? Function()? refreshToken,
    TResult? Function()? submitProfile,
  }) {
    return submitProfile?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? login,
    TResult Function()? logout,
    TResult Function()? attemptLoad,
    TResult Function(String mobileNumber, String type)? requestOtp,
    TResult Function(String mobileNumber, String type)? resendOtp,
    TResult Function(String username, String otp, UserModel userModel)?
        submitRegistrationOtp,
    TResult Function(String username, String password, UserModel userModel)?
        submitLoginOtpEvent,
    TResult Function(String authToken)? submitLogoutUser,
    TResult Function()? refreshToken,
    TResult Function()? submitProfile,
    required TResult orElse(),
  }) {
    if (submitProfile != null) {
      return submitProfile();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_AuthLoginEvent value) login,
    required TResult Function(_AuthLogoutEvent value) logout,
    required TResult Function(_AuthLoadEvent value) attemptLoad,
    required TResult Function(_RequestOtpEvent value) requestOtp,
    required TResult Function(_ResendOtpEvent value) resendOtp,
    required TResult Function(_SubmitRegistrationOtpEvent value)
        submitRegistrationOtp,
    required TResult Function(_SubmitLoginOtpEvent value) submitLoginOtpEvent,
    required TResult Function(_SubmitLogoutUserEvent value) submitLogoutUser,
    required TResult Function(_AuthRefreshTokenEvent value) refreshToken,
    required TResult Function(_SubmitProfileEvent value) submitProfile,
  }) {
    return submitProfile(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_AuthLoginEvent value)? login,
    TResult? Function(_AuthLogoutEvent value)? logout,
    TResult? Function(_AuthLoadEvent value)? attemptLoad,
    TResult? Function(_RequestOtpEvent value)? requestOtp,
    TResult? Function(_ResendOtpEvent value)? resendOtp,
    TResult? Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult? Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult? Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult? Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult? Function(_SubmitProfileEvent value)? submitProfile,
  }) {
    return submitProfile?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_AuthLoginEvent value)? login,
    TResult Function(_AuthLogoutEvent value)? logout,
    TResult Function(_AuthLoadEvent value)? attemptLoad,
    TResult Function(_RequestOtpEvent value)? requestOtp,
    TResult Function(_ResendOtpEvent value)? resendOtp,
    TResult Function(_SubmitRegistrationOtpEvent value)? submitRegistrationOtp,
    TResult Function(_SubmitLoginOtpEvent value)? submitLoginOtpEvent,
    TResult Function(_SubmitLogoutUserEvent value)? submitLogoutUser,
    TResult Function(_AuthRefreshTokenEvent value)? refreshToken,
    TResult Function(_SubmitProfileEvent value)? submitProfile,
    required TResult orElse(),
  }) {
    if (submitProfile != null) {
      return submitProfile(this);
    }
    return orElse();
  }
}

abstract class _SubmitProfileEvent implements AuthEvent {
  const factory _SubmitProfileEvent() = _$SubmitProfileEventImpl;
}

/// @nodoc
mixin _$AuthState {
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AuthStateCopyWith<$Res> {
  factory $AuthStateCopyWith(AuthState value, $Res Function(AuthState) then) =
      _$AuthStateCopyWithImpl<$Res, AuthState>;
}

/// @nodoc
class _$AuthStateCopyWithImpl<$Res, $Val extends AuthState>
    implements $AuthStateCopyWith<$Res> {
  _$AuthStateCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;
}

/// @nodoc
abstract class _$$ErrorStateImplCopyWith<$Res> {
  factory _$$ErrorStateImplCopyWith(
          _$ErrorStateImpl value, $Res Function(_$ErrorStateImpl) then) =
      __$$ErrorStateImplCopyWithImpl<$Res>;
}

/// @nodoc
class __$$ErrorStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$ErrorStateImpl>
    implements _$$ErrorStateImplCopyWith<$Res> {
  __$$ErrorStateImplCopyWithImpl(
      _$ErrorStateImpl _value, $Res Function(_$ErrorStateImpl) _then)
      : super(_value, _then);
}

/// @nodoc

class _$ErrorStateImpl implements _ErrorState {
  const _$ErrorStateImpl();

  @override
  String toString() {
    return 'AuthState.error()';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is _$ErrorStateImpl);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return error();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return error?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (error != null) {
      return error();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return error(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return error?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (error != null) {
      return error(this);
    }
    return orElse();
  }
}

abstract class _ErrorState implements AuthState {
  const factory _ErrorState() = _$ErrorStateImpl;
}

/// @nodoc
abstract class _$$InitialStateImplCopyWith<$Res> {
  factory _$$InitialStateImplCopyWith(
          _$InitialStateImpl value, $Res Function(_$InitialStateImpl) then) =
      __$$InitialStateImplCopyWithImpl<$Res>;
}

/// @nodoc
class __$$InitialStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$InitialStateImpl>
    implements _$$InitialStateImplCopyWith<$Res> {
  __$$InitialStateImplCopyWithImpl(
      _$InitialStateImpl _value, $Res Function(_$InitialStateImpl) _then)
      : super(_value, _then);
}

/// @nodoc

class _$InitialStateImpl implements _InitialState {
  const _$InitialStateImpl();

  @override
  String toString() {
    return 'AuthState.initial()';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is _$InitialStateImpl);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return initial();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return initial?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (initial != null) {
      return initial();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return initial(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return initial?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (initial != null) {
      return initial(this);
    }
    return orElse();
  }
}

abstract class _InitialState implements AuthState {
  const factory _InitialState() = _$InitialStateImpl;
}

/// @nodoc
abstract class _$$UnauthenticatedStateImplCopyWith<$Res> {
  factory _$$UnauthenticatedStateImplCopyWith(_$UnauthenticatedStateImpl value,
          $Res Function(_$UnauthenticatedStateImpl) then) =
      __$$UnauthenticatedStateImplCopyWithImpl<$Res>;
}

/// @nodoc
class __$$UnauthenticatedStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$UnauthenticatedStateImpl>
    implements _$$UnauthenticatedStateImplCopyWith<$Res> {
  __$$UnauthenticatedStateImplCopyWithImpl(_$UnauthenticatedStateImpl _value,
      $Res Function(_$UnauthenticatedStateImpl) _then)
      : super(_value, _then);
}

/// @nodoc

class _$UnauthenticatedStateImpl implements _UnauthenticatedState {
  const _$UnauthenticatedStateImpl();

  @override
  String toString() {
    return 'AuthState.unauthenticated()';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$UnauthenticatedStateImpl);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return unauthenticated();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return unauthenticated?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (unauthenticated != null) {
      return unauthenticated();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return unauthenticated(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return unauthenticated?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (unauthenticated != null) {
      return unauthenticated(this);
    }
    return orElse();
  }
}

abstract class _UnauthenticatedState implements AuthState {
  const factory _UnauthenticatedState() = _$UnauthenticatedStateImpl;
}

/// @nodoc
abstract class _$$AuthenticatedStateImplCopyWith<$Res> {
  factory _$$AuthenticatedStateImplCopyWith(_$AuthenticatedStateImpl value,
          $Res Function(_$AuthenticatedStateImpl) then) =
      __$$AuthenticatedStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call(
      {String accesstoken, String? refreshtoken, UserRequest? userRequest});

  $UserRequestCopyWith<$Res>? get userRequest;
}

/// @nodoc
class __$$AuthenticatedStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$AuthenticatedStateImpl>
    implements _$$AuthenticatedStateImplCopyWith<$Res> {
  __$$AuthenticatedStateImplCopyWithImpl(_$AuthenticatedStateImpl _value,
      $Res Function(_$AuthenticatedStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? accesstoken = null,
    Object? refreshtoken = freezed,
    Object? userRequest = freezed,
  }) {
    return _then(_$AuthenticatedStateImpl(
      accesstoken: null == accesstoken
          ? _value.accesstoken
          : accesstoken // ignore: cast_nullable_to_non_nullable
              as String,
      refreshtoken: freezed == refreshtoken
          ? _value.refreshtoken
          : refreshtoken // ignore: cast_nullable_to_non_nullable
              as String?,
      userRequest: freezed == userRequest
          ? _value.userRequest
          : userRequest // ignore: cast_nullable_to_non_nullable
              as UserRequest?,
    ));
  }

  @override
  @pragma('vm:prefer-inline')
  $UserRequestCopyWith<$Res>? get userRequest {
    if (_value.userRequest == null) {
      return null;
    }

    return $UserRequestCopyWith<$Res>(_value.userRequest!, (value) {
      return _then(_value.copyWith(userRequest: value));
    });
  }
}

/// @nodoc

class _$AuthenticatedStateImpl implements _AuthenticatedState {
  const _$AuthenticatedStateImpl(
      {required this.accesstoken,
      required this.refreshtoken,
      required this.userRequest});

  @override
  final String accesstoken;
  @override
  final String? refreshtoken;
  @override
  final UserRequest? userRequest;

  @override
  String toString() {
    return 'AuthState.authenticated(accesstoken: $accesstoken, refreshtoken: $refreshtoken, userRequest: $userRequest)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AuthenticatedStateImpl &&
            (identical(other.accesstoken, accesstoken) ||
                other.accesstoken == accesstoken) &&
            (identical(other.refreshtoken, refreshtoken) ||
                other.refreshtoken == refreshtoken) &&
            (identical(other.userRequest, userRequest) ||
                other.userRequest == userRequest));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, accesstoken, refreshtoken, userRequest);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AuthenticatedStateImplCopyWith<_$AuthenticatedStateImpl> get copyWith =>
      __$$AuthenticatedStateImplCopyWithImpl<_$AuthenticatedStateImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return authenticated(accesstoken, refreshtoken, userRequest);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return authenticated?.call(accesstoken, refreshtoken, userRequest);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (authenticated != null) {
      return authenticated(accesstoken, refreshtoken, userRequest);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return authenticated(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return authenticated?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (authenticated != null) {
      return authenticated(this);
    }
    return orElse();
  }
}

abstract class _AuthenticatedState implements AuthState {
  const factory _AuthenticatedState(
      {required final String accesstoken,
      required final String? refreshtoken,
      required final UserRequest? userRequest}) = _$AuthenticatedStateImpl;

  String get accesstoken;
  String? get refreshtoken;
  UserRequest? get userRequest;
  @JsonKey(ignore: true)
  _$$AuthenticatedStateImplCopyWith<_$AuthenticatedStateImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$OtpGenerationSuccessStateImplCopyWith<$Res> {
  factory _$$OtpGenerationSuccessStateImplCopyWith(
          _$OtpGenerationSuccessStateImpl value,
          $Res Function(_$OtpGenerationSuccessStateImpl) then) =
      __$$OtpGenerationSuccessStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String type});
}

/// @nodoc
class __$$OtpGenerationSuccessStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$OtpGenerationSuccessStateImpl>
    implements _$$OtpGenerationSuccessStateImplCopyWith<$Res> {
  __$$OtpGenerationSuccessStateImplCopyWithImpl(
      _$OtpGenerationSuccessStateImpl _value,
      $Res Function(_$OtpGenerationSuccessStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = null,
  }) {
    return _then(_$OtpGenerationSuccessStateImpl(
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$OtpGenerationSuccessStateImpl implements _OtpGenerationSuccessState {
  const _$OtpGenerationSuccessStateImpl({required this.type});

  @override
  final String type;

  @override
  String toString() {
    return 'AuthState.otpGenerationSucceed(type: $type)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$OtpGenerationSuccessStateImpl &&
            (identical(other.type, type) || other.type == type));
  }

  @override
  int get hashCode => Object.hash(runtimeType, type);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$OtpGenerationSuccessStateImplCopyWith<_$OtpGenerationSuccessStateImpl>
      get copyWith => __$$OtpGenerationSuccessStateImplCopyWithImpl<
          _$OtpGenerationSuccessStateImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return otpGenerationSucceed(type);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return otpGenerationSucceed?.call(type);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (otpGenerationSucceed != null) {
      return otpGenerationSucceed(type);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return otpGenerationSucceed(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return otpGenerationSucceed?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (otpGenerationSucceed != null) {
      return otpGenerationSucceed(this);
    }
    return orElse();
  }
}

abstract class _OtpGenerationSuccessState implements AuthState {
  const factory _OtpGenerationSuccessState({required final String type}) =
      _$OtpGenerationSuccessStateImpl;

  String get type;
  @JsonKey(ignore: true)
  _$$OtpGenerationSuccessStateImplCopyWith<_$OtpGenerationSuccessStateImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$ResendOtpGenerationSuccessStateImplCopyWith<$Res> {
  factory _$$ResendOtpGenerationSuccessStateImplCopyWith(
          _$ResendOtpGenerationSuccessStateImpl value,
          $Res Function(_$ResendOtpGenerationSuccessStateImpl) then) =
      __$$ResendOtpGenerationSuccessStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String type});
}

/// @nodoc
class __$$ResendOtpGenerationSuccessStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$ResendOtpGenerationSuccessStateImpl>
    implements _$$ResendOtpGenerationSuccessStateImplCopyWith<$Res> {
  __$$ResendOtpGenerationSuccessStateImplCopyWithImpl(
      _$ResendOtpGenerationSuccessStateImpl _value,
      $Res Function(_$ResendOtpGenerationSuccessStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = null,
  }) {
    return _then(_$ResendOtpGenerationSuccessStateImpl(
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$ResendOtpGenerationSuccessStateImpl
    implements _ResendOtpGenerationSuccessState {
  const _$ResendOtpGenerationSuccessStateImpl({required this.type});

  @override
  final String type;

  @override
  String toString() {
    return 'AuthState.resendOtpGenerationSucceed(type: $type)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ResendOtpGenerationSuccessStateImpl &&
            (identical(other.type, type) || other.type == type));
  }

  @override
  int get hashCode => Object.hash(runtimeType, type);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$ResendOtpGenerationSuccessStateImplCopyWith<
          _$ResendOtpGenerationSuccessStateImpl>
      get copyWith => __$$ResendOtpGenerationSuccessStateImplCopyWithImpl<
          _$ResendOtpGenerationSuccessStateImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return resendOtpGenerationSucceed(type);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return resendOtpGenerationSucceed?.call(type);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (resendOtpGenerationSucceed != null) {
      return resendOtpGenerationSucceed(type);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return resendOtpGenerationSucceed(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return resendOtpGenerationSucceed?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (resendOtpGenerationSucceed != null) {
      return resendOtpGenerationSucceed(this);
    }
    return orElse();
  }
}

abstract class _ResendOtpGenerationSuccessState implements AuthState {
  const factory _ResendOtpGenerationSuccessState({required final String type}) =
      _$ResendOtpGenerationSuccessStateImpl;

  String get type;
  @JsonKey(ignore: true)
  _$$ResendOtpGenerationSuccessStateImplCopyWith<
          _$ResendOtpGenerationSuccessStateImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$RequestFailedStateImplCopyWith<$Res> {
  factory _$$RequestFailedStateImplCopyWith(_$RequestFailedStateImpl value,
          $Res Function(_$RequestFailedStateImpl) then) =
      __$$RequestFailedStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String errorMsg});
}

/// @nodoc
class __$$RequestFailedStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$RequestFailedStateImpl>
    implements _$$RequestFailedStateImplCopyWith<$Res> {
  __$$RequestFailedStateImplCopyWithImpl(_$RequestFailedStateImpl _value,
      $Res Function(_$RequestFailedStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? errorMsg = null,
  }) {
    return _then(_$RequestFailedStateImpl(
      errorMsg: null == errorMsg
          ? _value.errorMsg
          : errorMsg // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$RequestFailedStateImpl implements _RequestFailedState {
  const _$RequestFailedStateImpl({required this.errorMsg});

  @override
  final String errorMsg;

  @override
  String toString() {
    return 'AuthState.requestFailed(errorMsg: $errorMsg)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$RequestFailedStateImpl &&
            (identical(other.errorMsg, errorMsg) ||
                other.errorMsg == errorMsg));
  }

  @override
  int get hashCode => Object.hash(runtimeType, errorMsg);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$RequestFailedStateImplCopyWith<_$RequestFailedStateImpl> get copyWith =>
      __$$RequestFailedStateImplCopyWithImpl<_$RequestFailedStateImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return requestFailed(errorMsg);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return requestFailed?.call(errorMsg);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (requestFailed != null) {
      return requestFailed(errorMsg);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return requestFailed(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return requestFailed?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (requestFailed != null) {
      return requestFailed(this);
    }
    return orElse();
  }
}

abstract class _RequestFailedState implements AuthState {
  const factory _RequestFailedState({required final String errorMsg}) =
      _$RequestFailedStateImpl;

  String get errorMsg;
  @JsonKey(ignore: true)
  _$$RequestFailedStateImplCopyWith<_$RequestFailedStateImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$OtpCorrectStateImplCopyWith<$Res> {
  factory _$$OtpCorrectStateImplCopyWith(_$OtpCorrectStateImpl value,
          $Res Function(_$OtpCorrectStateImpl) then) =
      __$$OtpCorrectStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({AuthResponse authResponse});

  $AuthResponseCopyWith<$Res> get authResponse;
}

/// @nodoc
class __$$OtpCorrectStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$OtpCorrectStateImpl>
    implements _$$OtpCorrectStateImplCopyWith<$Res> {
  __$$OtpCorrectStateImplCopyWithImpl(
      _$OtpCorrectStateImpl _value, $Res Function(_$OtpCorrectStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? authResponse = null,
  }) {
    return _then(_$OtpCorrectStateImpl(
      authResponse: null == authResponse
          ? _value.authResponse
          : authResponse // ignore: cast_nullable_to_non_nullable
              as AuthResponse,
    ));
  }

  @override
  @pragma('vm:prefer-inline')
  $AuthResponseCopyWith<$Res> get authResponse {
    return $AuthResponseCopyWith<$Res>(_value.authResponse, (value) {
      return _then(_value.copyWith(authResponse: value));
    });
  }
}

/// @nodoc

class _$OtpCorrectStateImpl implements _OtpCorrectState {
  const _$OtpCorrectStateImpl({required this.authResponse});

  @override
  final AuthResponse authResponse;

  @override
  String toString() {
    return 'AuthState.otpCorrect(authResponse: $authResponse)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$OtpCorrectStateImpl &&
            (identical(other.authResponse, authResponse) ||
                other.authResponse == authResponse));
  }

  @override
  int get hashCode => Object.hash(runtimeType, authResponse);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$OtpCorrectStateImplCopyWith<_$OtpCorrectStateImpl> get copyWith =>
      __$$OtpCorrectStateImplCopyWithImpl<_$OtpCorrectStateImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return otpCorrect(authResponse);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return otpCorrect?.call(authResponse);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (otpCorrect != null) {
      return otpCorrect(authResponse);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return otpCorrect(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return otpCorrect?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (otpCorrect != null) {
      return otpCorrect(this);
    }
    return orElse();
  }
}

abstract class _OtpCorrectState implements AuthState {
  const factory _OtpCorrectState({required final AuthResponse authResponse}) =
      _$OtpCorrectStateImpl;

  AuthResponse get authResponse;
  @JsonKey(ignore: true)
  _$$OtpCorrectStateImplCopyWith<_$OtpCorrectStateImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$RequestOtpFailedStateImplCopyWith<$Res> {
  factory _$$RequestOtpFailedStateImplCopyWith(
          _$RequestOtpFailedStateImpl value,
          $Res Function(_$RequestOtpFailedStateImpl) then) =
      __$$RequestOtpFailedStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String errorMsg});
}

/// @nodoc
class __$$RequestOtpFailedStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$RequestOtpFailedStateImpl>
    implements _$$RequestOtpFailedStateImplCopyWith<$Res> {
  __$$RequestOtpFailedStateImplCopyWithImpl(_$RequestOtpFailedStateImpl _value,
      $Res Function(_$RequestOtpFailedStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? errorMsg = null,
  }) {
    return _then(_$RequestOtpFailedStateImpl(
      errorMsg: null == errorMsg
          ? _value.errorMsg
          : errorMsg // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$RequestOtpFailedStateImpl implements _RequestOtpFailedState {
  const _$RequestOtpFailedStateImpl({required this.errorMsg});

  @override
  final String errorMsg;

  @override
  String toString() {
    return 'AuthState.requestOtpFailed(errorMsg: $errorMsg)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$RequestOtpFailedStateImpl &&
            (identical(other.errorMsg, errorMsg) ||
                other.errorMsg == errorMsg));
  }

  @override
  int get hashCode => Object.hash(runtimeType, errorMsg);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$RequestOtpFailedStateImplCopyWith<_$RequestOtpFailedStateImpl>
      get copyWith => __$$RequestOtpFailedStateImplCopyWithImpl<
          _$RequestOtpFailedStateImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return requestOtpFailed(errorMsg);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return requestOtpFailed?.call(errorMsg);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (requestOtpFailed != null) {
      return requestOtpFailed(errorMsg);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return requestOtpFailed(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return requestOtpFailed?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (requestOtpFailed != null) {
      return requestOtpFailed(this);
    }
    return orElse();
  }
}

abstract class _RequestOtpFailedState implements AuthState {
  const factory _RequestOtpFailedState({required final String errorMsg}) =
      _$RequestOtpFailedStateImpl;

  String get errorMsg;
  @JsonKey(ignore: true)
  _$$RequestOtpFailedStateImplCopyWith<_$RequestOtpFailedStateImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$RegistrationRequestOtpFailedStateImplCopyWith<$Res> {
  factory _$$RegistrationRequestOtpFailedStateImplCopyWith(
          _$RegistrationRequestOtpFailedStateImpl value,
          $Res Function(_$RegistrationRequestOtpFailedStateImpl) then) =
      __$$RegistrationRequestOtpFailedStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String errorMsg});
}

/// @nodoc
class __$$RegistrationRequestOtpFailedStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res,
        _$RegistrationRequestOtpFailedStateImpl>
    implements _$$RegistrationRequestOtpFailedStateImplCopyWith<$Res> {
  __$$RegistrationRequestOtpFailedStateImplCopyWithImpl(
      _$RegistrationRequestOtpFailedStateImpl _value,
      $Res Function(_$RegistrationRequestOtpFailedStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? errorMsg = null,
  }) {
    return _then(_$RegistrationRequestOtpFailedStateImpl(
      errorMsg: null == errorMsg
          ? _value.errorMsg
          : errorMsg // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$RegistrationRequestOtpFailedStateImpl
    implements _RegistrationRequestOtpFailedState {
  const _$RegistrationRequestOtpFailedStateImpl({required this.errorMsg});

  @override
  final String errorMsg;

  @override
  String toString() {
    return 'AuthState.registrationRequestOtpFailed(errorMsg: $errorMsg)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$RegistrationRequestOtpFailedStateImpl &&
            (identical(other.errorMsg, errorMsg) ||
                other.errorMsg == errorMsg));
  }

  @override
  int get hashCode => Object.hash(runtimeType, errorMsg);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$RegistrationRequestOtpFailedStateImplCopyWith<
          _$RegistrationRequestOtpFailedStateImpl>
      get copyWith => __$$RegistrationRequestOtpFailedStateImplCopyWithImpl<
          _$RegistrationRequestOtpFailedStateImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return registrationRequestOtpFailed(errorMsg);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return registrationRequestOtpFailed?.call(errorMsg);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (registrationRequestOtpFailed != null) {
      return registrationRequestOtpFailed(errorMsg);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return registrationRequestOtpFailed(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return registrationRequestOtpFailed?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (registrationRequestOtpFailed != null) {
      return registrationRequestOtpFailed(this);
    }
    return orElse();
  }
}

abstract class _RegistrationRequestOtpFailedState implements AuthState {
  const factory _RegistrationRequestOtpFailedState(
          {required final String errorMsg}) =
      _$RegistrationRequestOtpFailedStateImpl;

  String get errorMsg;
  @JsonKey(ignore: true)
  _$$RegistrationRequestOtpFailedStateImplCopyWith<
          _$RegistrationRequestOtpFailedStateImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$LogoutFailedStateImplCopyWith<$Res> {
  factory _$$LogoutFailedStateImplCopyWith(_$LogoutFailedStateImpl value,
          $Res Function(_$LogoutFailedStateImpl) then) =
      __$$LogoutFailedStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String errorMsg});
}

/// @nodoc
class __$$LogoutFailedStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$LogoutFailedStateImpl>
    implements _$$LogoutFailedStateImplCopyWith<$Res> {
  __$$LogoutFailedStateImplCopyWithImpl(_$LogoutFailedStateImpl _value,
      $Res Function(_$LogoutFailedStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? errorMsg = null,
  }) {
    return _then(_$LogoutFailedStateImpl(
      errorMsg: null == errorMsg
          ? _value.errorMsg
          : errorMsg // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$LogoutFailedStateImpl implements _LogoutFailedState {
  const _$LogoutFailedStateImpl({required this.errorMsg});

  @override
  final String errorMsg;

  @override
  String toString() {
    return 'AuthState.logoutFailedState(errorMsg: $errorMsg)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$LogoutFailedStateImpl &&
            (identical(other.errorMsg, errorMsg) ||
                other.errorMsg == errorMsg));
  }

  @override
  int get hashCode => Object.hash(runtimeType, errorMsg);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$LogoutFailedStateImplCopyWith<_$LogoutFailedStateImpl> get copyWith =>
      __$$LogoutFailedStateImplCopyWithImpl<_$LogoutFailedStateImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return logoutFailedState(errorMsg);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return logoutFailedState?.call(errorMsg);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (logoutFailedState != null) {
      return logoutFailedState(errorMsg);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return logoutFailedState(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return logoutFailedState?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (logoutFailedState != null) {
      return logoutFailedState(this);
    }
    return orElse();
  }
}

abstract class _LogoutFailedState implements AuthState {
  const factory _LogoutFailedState({required final String errorMsg}) =
      _$LogoutFailedStateImpl;

  String get errorMsg;
  @JsonKey(ignore: true)
  _$$LogoutFailedStateImplCopyWith<_$LogoutFailedStateImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$ProfileSuccessStateImplCopyWith<$Res> {
  factory _$$ProfileSuccessStateImplCopyWith(_$ProfileSuccessStateImpl value,
          $Res Function(_$ProfileSuccessStateImpl) then) =
      __$$ProfileSuccessStateImplCopyWithImpl<$Res>;
}

/// @nodoc
class __$$ProfileSuccessStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$ProfileSuccessStateImpl>
    implements _$$ProfileSuccessStateImplCopyWith<$Res> {
  __$$ProfileSuccessStateImplCopyWithImpl(_$ProfileSuccessStateImpl _value,
      $Res Function(_$ProfileSuccessStateImpl) _then)
      : super(_value, _then);
}

/// @nodoc

class _$ProfileSuccessStateImpl implements _ProfileSuccessState {
  const _$ProfileSuccessStateImpl();

  @override
  String toString() {
    return 'AuthState.profileSuccessState()';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ProfileSuccessStateImpl);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return profileSuccessState();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return profileSuccessState?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (profileSuccessState != null) {
      return profileSuccessState();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return profileSuccessState(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return profileSuccessState?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (profileSuccessState != null) {
      return profileSuccessState(this);
    }
    return orElse();
  }
}

abstract class _ProfileSuccessState implements AuthState {
  const factory _ProfileSuccessState() = _$ProfileSuccessStateImpl;
}

/// @nodoc
abstract class _$$ProfileFailedStateImplCopyWith<$Res> {
  factory _$$ProfileFailedStateImplCopyWith(_$ProfileFailedStateImpl value,
          $Res Function(_$ProfileFailedStateImpl) then) =
      __$$ProfileFailedStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String errorMsg});
}

/// @nodoc
class __$$ProfileFailedStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$ProfileFailedStateImpl>
    implements _$$ProfileFailedStateImplCopyWith<$Res> {
  __$$ProfileFailedStateImplCopyWithImpl(_$ProfileFailedStateImpl _value,
      $Res Function(_$ProfileFailedStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? errorMsg = null,
  }) {
    return _then(_$ProfileFailedStateImpl(
      errorMsg: null == errorMsg
          ? _value.errorMsg
          : errorMsg // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$ProfileFailedStateImpl implements _ProfileFailedState {
  const _$ProfileFailedStateImpl({required this.errorMsg});

  @override
  final String errorMsg;

  @override
  String toString() {
    return 'AuthState.profileFailedState(errorMsg: $errorMsg)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ProfileFailedStateImpl &&
            (identical(other.errorMsg, errorMsg) ||
                other.errorMsg == errorMsg));
  }

  @override
  int get hashCode => Object.hash(runtimeType, errorMsg);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$ProfileFailedStateImplCopyWith<_$ProfileFailedStateImpl> get copyWith =>
      __$$ProfileFailedStateImplCopyWithImpl<_$ProfileFailedStateImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return profileFailedState(errorMsg);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return profileFailedState?.call(errorMsg);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (profileFailedState != null) {
      return profileFailedState(errorMsg);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return profileFailedState(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return profileFailedState?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (profileFailedState != null) {
      return profileFailedState(this);
    }
    return orElse();
  }
}

abstract class _ProfileFailedState implements AuthState {
  const factory _ProfileFailedState({required final String errorMsg}) =
      _$ProfileFailedStateImpl;

  String get errorMsg;
  @JsonKey(ignore: true)
  _$$ProfileFailedStateImplCopyWith<_$ProfileFailedStateImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$IndividualSearchSuccessStateImplCopyWith<$Res> {
  factory _$$IndividualSearchSuccessStateImplCopyWith(
          _$IndividualSearchSuccessStateImpl value,
          $Res Function(_$IndividualSearchSuccessStateImpl) then) =
      __$$IndividualSearchSuccessStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({IndividualSearchResponse individualSearchResponse});

  $IndividualSearchResponseCopyWith<$Res> get individualSearchResponse;
}

/// @nodoc
class __$$IndividualSearchSuccessStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$IndividualSearchSuccessStateImpl>
    implements _$$IndividualSearchSuccessStateImplCopyWith<$Res> {
  __$$IndividualSearchSuccessStateImplCopyWithImpl(
      _$IndividualSearchSuccessStateImpl _value,
      $Res Function(_$IndividualSearchSuccessStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? individualSearchResponse = null,
  }) {
    return _then(_$IndividualSearchSuccessStateImpl(
      individualSearchResponse: null == individualSearchResponse
          ? _value.individualSearchResponse
          : individualSearchResponse // ignore: cast_nullable_to_non_nullable
              as IndividualSearchResponse,
    ));
  }

  @override
  @pragma('vm:prefer-inline')
  $IndividualSearchResponseCopyWith<$Res> get individualSearchResponse {
    return $IndividualSearchResponseCopyWith<$Res>(
        _value.individualSearchResponse, (value) {
      return _then(_value.copyWith(individualSearchResponse: value));
    });
  }
}

/// @nodoc

class _$IndividualSearchSuccessStateImpl
    implements _IndividualSearchSuccessState {
  const _$IndividualSearchSuccessStateImpl(
      {required this.individualSearchResponse});

  @override
  final IndividualSearchResponse individualSearchResponse;

  @override
  String toString() {
    return 'AuthState.individualSearchSuccessState(individualSearchResponse: $individualSearchResponse)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$IndividualSearchSuccessStateImpl &&
            (identical(
                    other.individualSearchResponse, individualSearchResponse) ||
                other.individualSearchResponse == individualSearchResponse));
  }

  @override
  int get hashCode => Object.hash(runtimeType, individualSearchResponse);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$IndividualSearchSuccessStateImplCopyWith<
          _$IndividualSearchSuccessStateImpl>
      get copyWith => __$$IndividualSearchSuccessStateImplCopyWithImpl<
          _$IndividualSearchSuccessStateImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return individualSearchSuccessState(individualSearchResponse);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return individualSearchSuccessState?.call(individualSearchResponse);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (individualSearchSuccessState != null) {
      return individualSearchSuccessState(individualSearchResponse);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return individualSearchSuccessState(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return individualSearchSuccessState?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (individualSearchSuccessState != null) {
      return individualSearchSuccessState(this);
    }
    return orElse();
  }
}

abstract class _IndividualSearchSuccessState implements AuthState {
  const factory _IndividualSearchSuccessState(
          {required final IndividualSearchResponse individualSearchResponse}) =
      _$IndividualSearchSuccessStateImpl;

  IndividualSearchResponse get individualSearchResponse;
  @JsonKey(ignore: true)
  _$$IndividualSearchSuccessStateImplCopyWith<
          _$IndividualSearchSuccessStateImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$AdvocateSearchSuccessStateImplCopyWith<$Res> {
  factory _$$AdvocateSearchSuccessStateImplCopyWith(
          _$AdvocateSearchSuccessStateImpl value,
          $Res Function(_$AdvocateSearchSuccessStateImpl) then) =
      __$$AdvocateSearchSuccessStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({AdvocateSearchResponse advocateSearchResponse});

  $AdvocateSearchResponseCopyWith<$Res> get advocateSearchResponse;
}

/// @nodoc
class __$$AdvocateSearchSuccessStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$AdvocateSearchSuccessStateImpl>
    implements _$$AdvocateSearchSuccessStateImplCopyWith<$Res> {
  __$$AdvocateSearchSuccessStateImplCopyWithImpl(
      _$AdvocateSearchSuccessStateImpl _value,
      $Res Function(_$AdvocateSearchSuccessStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? advocateSearchResponse = null,
  }) {
    return _then(_$AdvocateSearchSuccessStateImpl(
      advocateSearchResponse: null == advocateSearchResponse
          ? _value.advocateSearchResponse
          : advocateSearchResponse // ignore: cast_nullable_to_non_nullable
              as AdvocateSearchResponse,
    ));
  }

  @override
  @pragma('vm:prefer-inline')
  $AdvocateSearchResponseCopyWith<$Res> get advocateSearchResponse {
    return $AdvocateSearchResponseCopyWith<$Res>(_value.advocateSearchResponse,
        (value) {
      return _then(_value.copyWith(advocateSearchResponse: value));
    });
  }
}

/// @nodoc

class _$AdvocateSearchSuccessStateImpl implements _AdvocateSearchSuccessState {
  const _$AdvocateSearchSuccessStateImpl(
      {required this.advocateSearchResponse});

  @override
  final AdvocateSearchResponse advocateSearchResponse;

  @override
  String toString() {
    return 'AuthState.advocateSearchSuccessState(advocateSearchResponse: $advocateSearchResponse)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdvocateSearchSuccessStateImpl &&
            (identical(other.advocateSearchResponse, advocateSearchResponse) ||
                other.advocateSearchResponse == advocateSearchResponse));
  }

  @override
  int get hashCode => Object.hash(runtimeType, advocateSearchResponse);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AdvocateSearchSuccessStateImplCopyWith<_$AdvocateSearchSuccessStateImpl>
      get copyWith => __$$AdvocateSearchSuccessStateImplCopyWithImpl<
          _$AdvocateSearchSuccessStateImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return advocateSearchSuccessState(advocateSearchResponse);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return advocateSearchSuccessState?.call(advocateSearchResponse);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (advocateSearchSuccessState != null) {
      return advocateSearchSuccessState(advocateSearchResponse);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return advocateSearchSuccessState(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return advocateSearchSuccessState?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (advocateSearchSuccessState != null) {
      return advocateSearchSuccessState(this);
    }
    return orElse();
  }
}

abstract class _AdvocateSearchSuccessState implements AuthState {
  const factory _AdvocateSearchSuccessState(
          {required final AdvocateSearchResponse advocateSearchResponse}) =
      _$AdvocateSearchSuccessStateImpl;

  AdvocateSearchResponse get advocateSearchResponse;
  @JsonKey(ignore: true)
  _$$AdvocateSearchSuccessStateImplCopyWith<_$AdvocateSearchSuccessStateImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$AdvocateClerkSearchSuccessStateImplCopyWith<$Res> {
  factory _$$AdvocateClerkSearchSuccessStateImplCopyWith(
          _$AdvocateClerkSearchSuccessStateImpl value,
          $Res Function(_$AdvocateClerkSearchSuccessStateImpl) then) =
      __$$AdvocateClerkSearchSuccessStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({AdvocateClerkSearchResponse advocateClerkSearchResponse});

  $AdvocateClerkSearchResponseCopyWith<$Res> get advocateClerkSearchResponse;
}

/// @nodoc
class __$$AdvocateClerkSearchSuccessStateImplCopyWithImpl<$Res>
    extends _$AuthStateCopyWithImpl<$Res, _$AdvocateClerkSearchSuccessStateImpl>
    implements _$$AdvocateClerkSearchSuccessStateImplCopyWith<$Res> {
  __$$AdvocateClerkSearchSuccessStateImplCopyWithImpl(
      _$AdvocateClerkSearchSuccessStateImpl _value,
      $Res Function(_$AdvocateClerkSearchSuccessStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? advocateClerkSearchResponse = null,
  }) {
    return _then(_$AdvocateClerkSearchSuccessStateImpl(
      advocateClerkSearchResponse: null == advocateClerkSearchResponse
          ? _value.advocateClerkSearchResponse
          : advocateClerkSearchResponse // ignore: cast_nullable_to_non_nullable
              as AdvocateClerkSearchResponse,
    ));
  }

  @override
  @pragma('vm:prefer-inline')
  $AdvocateClerkSearchResponseCopyWith<$Res> get advocateClerkSearchResponse {
    return $AdvocateClerkSearchResponseCopyWith<$Res>(
        _value.advocateClerkSearchResponse, (value) {
      return _then(_value.copyWith(advocateClerkSearchResponse: value));
    });
  }
}

/// @nodoc

class _$AdvocateClerkSearchSuccessStateImpl
    implements _AdvocateClerkSearchSuccessState {
  const _$AdvocateClerkSearchSuccessStateImpl(
      {required this.advocateClerkSearchResponse});

  @override
  final AdvocateClerkSearchResponse advocateClerkSearchResponse;

  @override
  String toString() {
    return 'AuthState.advocateClerkSearchSuccessState(advocateClerkSearchResponse: $advocateClerkSearchResponse)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdvocateClerkSearchSuccessStateImpl &&
            (identical(other.advocateClerkSearchResponse,
                    advocateClerkSearchResponse) ||
                other.advocateClerkSearchResponse ==
                    advocateClerkSearchResponse));
  }

  @override
  int get hashCode => Object.hash(runtimeType, advocateClerkSearchResponse);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AdvocateClerkSearchSuccessStateImplCopyWith<
          _$AdvocateClerkSearchSuccessStateImpl>
      get copyWith => __$$AdvocateClerkSearchSuccessStateImplCopyWithImpl<
          _$AdvocateClerkSearchSuccessStateImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() error,
    required TResult Function() initial,
    required TResult Function() unauthenticated,
    required TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)
        authenticated,
    required TResult Function(String type) otpGenerationSucceed,
    required TResult Function(String type) resendOtpGenerationSucceed,
    required TResult Function(String errorMsg) requestFailed,
    required TResult Function(AuthResponse authResponse) otpCorrect,
    required TResult Function(String errorMsg) requestOtpFailed,
    required TResult Function(String errorMsg) registrationRequestOtpFailed,
    required TResult Function(String errorMsg) logoutFailedState,
    required TResult Function() profileSuccessState,
    required TResult Function(String errorMsg) profileFailedState,
    required TResult Function(IndividualSearchResponse individualSearchResponse)
        individualSearchSuccessState,
    required TResult Function(AdvocateSearchResponse advocateSearchResponse)
        advocateSearchSuccessState,
    required TResult Function(
            AdvocateClerkSearchResponse advocateClerkSearchResponse)
        advocateClerkSearchSuccessState,
  }) {
    return advocateClerkSearchSuccessState(advocateClerkSearchResponse);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? error,
    TResult? Function()? initial,
    TResult? Function()? unauthenticated,
    TResult? Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult? Function(String type)? otpGenerationSucceed,
    TResult? Function(String type)? resendOtpGenerationSucceed,
    TResult? Function(String errorMsg)? requestFailed,
    TResult? Function(AuthResponse authResponse)? otpCorrect,
    TResult? Function(String errorMsg)? requestOtpFailed,
    TResult? Function(String errorMsg)? registrationRequestOtpFailed,
    TResult? Function(String errorMsg)? logoutFailedState,
    TResult? Function()? profileSuccessState,
    TResult? Function(String errorMsg)? profileFailedState,
    TResult? Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult? Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult? Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
  }) {
    return advocateClerkSearchSuccessState?.call(advocateClerkSearchResponse);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? error,
    TResult Function()? initial,
    TResult Function()? unauthenticated,
    TResult Function(
            String accesstoken, String? refreshtoken, UserRequest? userRequest)?
        authenticated,
    TResult Function(String type)? otpGenerationSucceed,
    TResult Function(String type)? resendOtpGenerationSucceed,
    TResult Function(String errorMsg)? requestFailed,
    TResult Function(AuthResponse authResponse)? otpCorrect,
    TResult Function(String errorMsg)? requestOtpFailed,
    TResult Function(String errorMsg)? registrationRequestOtpFailed,
    TResult Function(String errorMsg)? logoutFailedState,
    TResult Function()? profileSuccessState,
    TResult Function(String errorMsg)? profileFailedState,
    TResult Function(IndividualSearchResponse individualSearchResponse)?
        individualSearchSuccessState,
    TResult Function(AdvocateSearchResponse advocateSearchResponse)?
        advocateSearchSuccessState,
    TResult Function(AdvocateClerkSearchResponse advocateClerkSearchResponse)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (advocateClerkSearchSuccessState != null) {
      return advocateClerkSearchSuccessState(advocateClerkSearchResponse);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_ErrorState value) error,
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UnauthenticatedState value) unauthenticated,
    required TResult Function(_AuthenticatedState value) authenticated,
    required TResult Function(_OtpGenerationSuccessState value)
        otpGenerationSucceed,
    required TResult Function(_ResendOtpGenerationSuccessState value)
        resendOtpGenerationSucceed,
    required TResult Function(_RequestFailedState value) requestFailed,
    required TResult Function(_OtpCorrectState value) otpCorrect,
    required TResult Function(_RequestOtpFailedState value) requestOtpFailed,
    required TResult Function(_RegistrationRequestOtpFailedState value)
        registrationRequestOtpFailed,
    required TResult Function(_LogoutFailedState value) logoutFailedState,
    required TResult Function(_ProfileSuccessState value) profileSuccessState,
    required TResult Function(_ProfileFailedState value) profileFailedState,
    required TResult Function(_IndividualSearchSuccessState value)
        individualSearchSuccessState,
    required TResult Function(_AdvocateSearchSuccessState value)
        advocateSearchSuccessState,
    required TResult Function(_AdvocateClerkSearchSuccessState value)
        advocateClerkSearchSuccessState,
  }) {
    return advocateClerkSearchSuccessState(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_ErrorState value)? error,
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UnauthenticatedState value)? unauthenticated,
    TResult? Function(_AuthenticatedState value)? authenticated,
    TResult? Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult? Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult? Function(_RequestFailedState value)? requestFailed,
    TResult? Function(_OtpCorrectState value)? otpCorrect,
    TResult? Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult? Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult? Function(_LogoutFailedState value)? logoutFailedState,
    TResult? Function(_ProfileSuccessState value)? profileSuccessState,
    TResult? Function(_ProfileFailedState value)? profileFailedState,
    TResult? Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult? Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult? Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
  }) {
    return advocateClerkSearchSuccessState?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_ErrorState value)? error,
    TResult Function(_InitialState value)? initial,
    TResult Function(_UnauthenticatedState value)? unauthenticated,
    TResult Function(_AuthenticatedState value)? authenticated,
    TResult Function(_OtpGenerationSuccessState value)? otpGenerationSucceed,
    TResult Function(_ResendOtpGenerationSuccessState value)?
        resendOtpGenerationSucceed,
    TResult Function(_RequestFailedState value)? requestFailed,
    TResult Function(_OtpCorrectState value)? otpCorrect,
    TResult Function(_RequestOtpFailedState value)? requestOtpFailed,
    TResult Function(_RegistrationRequestOtpFailedState value)?
        registrationRequestOtpFailed,
    TResult Function(_LogoutFailedState value)? logoutFailedState,
    TResult Function(_ProfileSuccessState value)? profileSuccessState,
    TResult Function(_ProfileFailedState value)? profileFailedState,
    TResult Function(_IndividualSearchSuccessState value)?
        individualSearchSuccessState,
    TResult Function(_AdvocateSearchSuccessState value)?
        advocateSearchSuccessState,
    TResult Function(_AdvocateClerkSearchSuccessState value)?
        advocateClerkSearchSuccessState,
    required TResult orElse(),
  }) {
    if (advocateClerkSearchSuccessState != null) {
      return advocateClerkSearchSuccessState(this);
    }
    return orElse();
  }
}

abstract class _AdvocateClerkSearchSuccessState implements AuthState {
  const factory _AdvocateClerkSearchSuccessState(
      {required final AdvocateClerkSearchResponse
          advocateClerkSearchResponse}) = _$AdvocateClerkSearchSuccessStateImpl;

  AdvocateClerkSearchResponse get advocateClerkSearchResponse;
  @JsonKey(ignore: true)
  _$$AdvocateClerkSearchSuccessStateImplCopyWith<
          _$AdvocateClerkSearchSuccessStateImpl>
      get copyWith => throw _privateConstructorUsedError;
}
