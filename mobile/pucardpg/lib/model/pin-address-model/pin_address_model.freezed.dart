// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'pin_address_model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

PlaceDetailsList _$PlaceDetailsListFromJson(Map<String, dynamic> json) {
  return _PlaceDetailsList.fromJson(json);
}

/// @nodoc
mixin _$PlaceDetailsList {
  @JsonKey(name: 'results')
  List<Result> get results => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $PlaceDetailsListCopyWith<PlaceDetailsList> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $PlaceDetailsListCopyWith<$Res> {
  factory $PlaceDetailsListCopyWith(
          PlaceDetailsList value, $Res Function(PlaceDetailsList) then) =
      _$PlaceDetailsListCopyWithImpl<$Res, PlaceDetailsList>;
  @useResult
  $Res call({@JsonKey(name: 'results') List<Result> results});
}

/// @nodoc
class _$PlaceDetailsListCopyWithImpl<$Res, $Val extends PlaceDetailsList>
    implements $PlaceDetailsListCopyWith<$Res> {
  _$PlaceDetailsListCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? results = null,
  }) {
    return _then(_value.copyWith(
      results: null == results
          ? _value.results
          : results // ignore: cast_nullable_to_non_nullable
              as List<Result>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$PlaceDetailsListImplCopyWith<$Res>
    implements $PlaceDetailsListCopyWith<$Res> {
  factory _$$PlaceDetailsListImplCopyWith(_$PlaceDetailsListImpl value,
          $Res Function(_$PlaceDetailsListImpl) then) =
      __$$PlaceDetailsListImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({@JsonKey(name: 'results') List<Result> results});
}

/// @nodoc
class __$$PlaceDetailsListImplCopyWithImpl<$Res>
    extends _$PlaceDetailsListCopyWithImpl<$Res, _$PlaceDetailsListImpl>
    implements _$$PlaceDetailsListImplCopyWith<$Res> {
  __$$PlaceDetailsListImplCopyWithImpl(_$PlaceDetailsListImpl _value,
      $Res Function(_$PlaceDetailsListImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? results = null,
  }) {
    return _then(_$PlaceDetailsListImpl(
      results: null == results
          ? _value._results
          : results // ignore: cast_nullable_to_non_nullable
              as List<Result>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$PlaceDetailsListImpl implements _PlaceDetailsList {
  const _$PlaceDetailsListImpl(
      {@JsonKey(name: 'results') required final List<Result> results})
      : _results = results;

  factory _$PlaceDetailsListImpl.fromJson(Map<String, dynamic> json) =>
      _$$PlaceDetailsListImplFromJson(json);

  final List<Result> _results;
  @override
  @JsonKey(name: 'results')
  List<Result> get results {
    if (_results is EqualUnmodifiableListView) return _results;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_results);
  }

  @override
  String toString() {
    return 'PlaceDetailsList(results: $results)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$PlaceDetailsListImpl &&
            const DeepCollectionEquality().equals(other._results, _results));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(_results));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$PlaceDetailsListImplCopyWith<_$PlaceDetailsListImpl> get copyWith =>
      __$$PlaceDetailsListImplCopyWithImpl<_$PlaceDetailsListImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$PlaceDetailsListImplToJson(
      this,
    );
  }
}

abstract class _PlaceDetailsList implements PlaceDetailsList {
  const factory _PlaceDetailsList(
          {@JsonKey(name: 'results') required final List<Result> results}) =
      _$PlaceDetailsListImpl;

  factory _PlaceDetailsList.fromJson(Map<String, dynamic> json) =
      _$PlaceDetailsListImpl.fromJson;

  @override
  @JsonKey(name: 'results')
  List<Result> get results;
  @override
  @JsonKey(ignore: true)
  _$$PlaceDetailsListImplCopyWith<_$PlaceDetailsListImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
