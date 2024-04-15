// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'state_model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

/// @nodoc
mixin _$StatesData {
  dynamic get name => throw _privateConstructorUsedError;
  dynamic get countryCode => throw _privateConstructorUsedError;
  dynamic get isoCode => throw _privateConstructorUsedError;
  dynamic get latitude => throw _privateConstructorUsedError;
  dynamic get longitude => throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
  $StatesDataCopyWith<StatesData> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $StatesDataCopyWith<$Res> {
  factory $StatesDataCopyWith(
          StatesData value, $Res Function(StatesData) then) =
      _$StatesDataCopyWithImpl<$Res, StatesData>;
  @useResult
  $Res call(
      {dynamic name,
      dynamic countryCode,
      dynamic isoCode,
      dynamic latitude,
      dynamic longitude});
}

/// @nodoc
class _$StatesDataCopyWithImpl<$Res, $Val extends StatesData>
    implements $StatesDataCopyWith<$Res> {
  _$StatesDataCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = freezed,
    Object? countryCode = freezed,
    Object? isoCode = freezed,
    Object? latitude = freezed,
    Object? longitude = freezed,
  }) {
    return _then(_value.copyWith(
      name: freezed == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as dynamic,
      countryCode: freezed == countryCode
          ? _value.countryCode
          : countryCode // ignore: cast_nullable_to_non_nullable
              as dynamic,
      isoCode: freezed == isoCode
          ? _value.isoCode
          : isoCode // ignore: cast_nullable_to_non_nullable
              as dynamic,
      latitude: freezed == latitude
          ? _value.latitude
          : latitude // ignore: cast_nullable_to_non_nullable
              as dynamic,
      longitude: freezed == longitude
          ? _value.longitude
          : longitude // ignore: cast_nullable_to_non_nullable
              as dynamic,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$StatesDataImplCopyWith<$Res>
    implements $StatesDataCopyWith<$Res> {
  factory _$$StatesDataImplCopyWith(
          _$StatesDataImpl value, $Res Function(_$StatesDataImpl) then) =
      __$$StatesDataImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {dynamic name,
      dynamic countryCode,
      dynamic isoCode,
      dynamic latitude,
      dynamic longitude});
}

/// @nodoc
class __$$StatesDataImplCopyWithImpl<$Res>
    extends _$StatesDataCopyWithImpl<$Res, _$StatesDataImpl>
    implements _$$StatesDataImplCopyWith<$Res> {
  __$$StatesDataImplCopyWithImpl(
      _$StatesDataImpl _value, $Res Function(_$StatesDataImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = freezed,
    Object? countryCode = freezed,
    Object? isoCode = freezed,
    Object? latitude = freezed,
    Object? longitude = freezed,
  }) {
    return _then(_$StatesDataImpl(
      name: freezed == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as dynamic,
      countryCode: freezed == countryCode
          ? _value.countryCode
          : countryCode // ignore: cast_nullable_to_non_nullable
              as dynamic,
      isoCode: freezed == isoCode
          ? _value.isoCode
          : isoCode // ignore: cast_nullable_to_non_nullable
              as dynamic,
      latitude: freezed == latitude
          ? _value.latitude
          : latitude // ignore: cast_nullable_to_non_nullable
              as dynamic,
      longitude: freezed == longitude
          ? _value.longitude
          : longitude // ignore: cast_nullable_to_non_nullable
              as dynamic,
    ));
  }
}

/// @nodoc

class _$StatesDataImpl implements _StatesData {
  const _$StatesDataImpl(
      {this.name,
      this.countryCode,
      this.isoCode,
      this.latitude,
      this.longitude});

  @override
  final dynamic name;
  @override
  final dynamic countryCode;
  @override
  final dynamic isoCode;
  @override
  final dynamic latitude;
  @override
  final dynamic longitude;

  @override
  String toString() {
    return 'StatesData(name: $name, countryCode: $countryCode, isoCode: $isoCode, latitude: $latitude, longitude: $longitude)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$StatesDataImpl &&
            const DeepCollectionEquality().equals(other.name, name) &&
            const DeepCollectionEquality()
                .equals(other.countryCode, countryCode) &&
            const DeepCollectionEquality().equals(other.isoCode, isoCode) &&
            const DeepCollectionEquality().equals(other.latitude, latitude) &&
            const DeepCollectionEquality().equals(other.longitude, longitude));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      const DeepCollectionEquality().hash(name),
      const DeepCollectionEquality().hash(countryCode),
      const DeepCollectionEquality().hash(isoCode),
      const DeepCollectionEquality().hash(latitude),
      const DeepCollectionEquality().hash(longitude));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$StatesDataImplCopyWith<_$StatesDataImpl> get copyWith =>
      __$$StatesDataImplCopyWithImpl<_$StatesDataImpl>(this, _$identity);
}

abstract class _StatesData implements StatesData {
  const factory _StatesData(
      {final dynamic name,
      final dynamic countryCode,
      final dynamic isoCode,
      final dynamic latitude,
      final dynamic longitude}) = _$StatesDataImpl;

  @override
  dynamic get name;
  @override
  dynamic get countryCode;
  @override
  dynamic get isoCode;
  @override
  dynamic get latitude;
  @override
  dynamic get longitude;
  @override
  @JsonKey(ignore: true)
  _$$StatesDataImplCopyWith<_$StatesDataImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
