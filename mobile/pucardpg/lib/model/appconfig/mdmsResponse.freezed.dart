// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'mdmsResponse.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

MdmsResponseModel _$MdmsResponseModelFromJson(Map<String, dynamic> json) {
  return _MdmsResponseModel.fromJson(json);
}

/// @nodoc
mixin _$MdmsResponseModel {
  @JsonKey(name: 'MdmsRes')
  MdmsRes? get mdmsRes => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $MdmsResponseModelCopyWith<MdmsResponseModel> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $MdmsResponseModelCopyWith<$Res> {
  factory $MdmsResponseModelCopyWith(
          MdmsResponseModel value, $Res Function(MdmsResponseModel) then) =
      _$MdmsResponseModelCopyWithImpl<$Res, MdmsResponseModel>;
  @useResult
  $Res call({@JsonKey(name: 'MdmsRes') MdmsRes? mdmsRes});

  $MdmsResCopyWith<$Res>? get mdmsRes;
}

/// @nodoc
class _$MdmsResponseModelCopyWithImpl<$Res, $Val extends MdmsResponseModel>
    implements $MdmsResponseModelCopyWith<$Res> {
  _$MdmsResponseModelCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? mdmsRes = freezed,
  }) {
    return _then(_value.copyWith(
      mdmsRes: freezed == mdmsRes
          ? _value.mdmsRes
          : mdmsRes // ignore: cast_nullable_to_non_nullable
              as MdmsRes?,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $MdmsResCopyWith<$Res>? get mdmsRes {
    if (_value.mdmsRes == null) {
      return null;
    }

    return $MdmsResCopyWith<$Res>(_value.mdmsRes!, (value) {
      return _then(_value.copyWith(mdmsRes: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$MdmsResponseModelImplCopyWith<$Res>
    implements $MdmsResponseModelCopyWith<$Res> {
  factory _$$MdmsResponseModelImplCopyWith(_$MdmsResponseModelImpl value,
          $Res Function(_$MdmsResponseModelImpl) then) =
      __$$MdmsResponseModelImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({@JsonKey(name: 'MdmsRes') MdmsRes? mdmsRes});

  @override
  $MdmsResCopyWith<$Res>? get mdmsRes;
}

/// @nodoc
class __$$MdmsResponseModelImplCopyWithImpl<$Res>
    extends _$MdmsResponseModelCopyWithImpl<$Res, _$MdmsResponseModelImpl>
    implements _$$MdmsResponseModelImplCopyWith<$Res> {
  __$$MdmsResponseModelImplCopyWithImpl(_$MdmsResponseModelImpl _value,
      $Res Function(_$MdmsResponseModelImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? mdmsRes = freezed,
  }) {
    return _then(_$MdmsResponseModelImpl(
      mdmsRes: freezed == mdmsRes
          ? _value.mdmsRes
          : mdmsRes // ignore: cast_nullable_to_non_nullable
              as MdmsRes?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$MdmsResponseModelImpl implements _MdmsResponseModel {
  const _$MdmsResponseModelImpl(
      {@JsonKey(name: 'MdmsRes') required this.mdmsRes});

  factory _$MdmsResponseModelImpl.fromJson(Map<String, dynamic> json) =>
      _$$MdmsResponseModelImplFromJson(json);

  @override
  @JsonKey(name: 'MdmsRes')
  final MdmsRes? mdmsRes;

  @override
  String toString() {
    return 'MdmsResponseModel(mdmsRes: $mdmsRes)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$MdmsResponseModelImpl &&
            (identical(other.mdmsRes, mdmsRes) || other.mdmsRes == mdmsRes));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, mdmsRes);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$MdmsResponseModelImplCopyWith<_$MdmsResponseModelImpl> get copyWith =>
      __$$MdmsResponseModelImplCopyWithImpl<_$MdmsResponseModelImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$MdmsResponseModelImplToJson(
      this,
    );
  }
}

abstract class _MdmsResponseModel implements MdmsResponseModel {
  const factory _MdmsResponseModel(
          {@JsonKey(name: 'MdmsRes') required final MdmsRes? mdmsRes}) =
      _$MdmsResponseModelImpl;

  factory _MdmsResponseModel.fromJson(Map<String, dynamic> json) =
      _$MdmsResponseModelImpl.fromJson;

  @override
  @JsonKey(name: 'MdmsRes')
  MdmsRes? get mdmsRes;
  @override
  @JsonKey(ignore: true)
  _$$MdmsResponseModelImplCopyWith<_$MdmsResponseModelImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

MdmsRes _$MdmsResFromJson(Map<String, dynamic> json) {
  return _MdmsRes.fromJson(json);
}

/// @nodoc
mixin _$MdmsRes {
  @JsonKey(name: 'common-masters')
  CommonMasters? get commonMasters => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $MdmsResCopyWith<MdmsRes> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $MdmsResCopyWith<$Res> {
  factory $MdmsResCopyWith(MdmsRes value, $Res Function(MdmsRes) then) =
      _$MdmsResCopyWithImpl<$Res, MdmsRes>;
  @useResult
  $Res call({@JsonKey(name: 'common-masters') CommonMasters? commonMasters});

  $CommonMastersCopyWith<$Res>? get commonMasters;
}

/// @nodoc
class _$MdmsResCopyWithImpl<$Res, $Val extends MdmsRes>
    implements $MdmsResCopyWith<$Res> {
  _$MdmsResCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? commonMasters = freezed,
  }) {
    return _then(_value.copyWith(
      commonMasters: freezed == commonMasters
          ? _value.commonMasters
          : commonMasters // ignore: cast_nullable_to_non_nullable
              as CommonMasters?,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $CommonMastersCopyWith<$Res>? get commonMasters {
    if (_value.commonMasters == null) {
      return null;
    }

    return $CommonMastersCopyWith<$Res>(_value.commonMasters!, (value) {
      return _then(_value.copyWith(commonMasters: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$MdmsResImplCopyWith<$Res> implements $MdmsResCopyWith<$Res> {
  factory _$$MdmsResImplCopyWith(
          _$MdmsResImpl value, $Res Function(_$MdmsResImpl) then) =
      __$$MdmsResImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({@JsonKey(name: 'common-masters') CommonMasters? commonMasters});

  @override
  $CommonMastersCopyWith<$Res>? get commonMasters;
}

/// @nodoc
class __$$MdmsResImplCopyWithImpl<$Res>
    extends _$MdmsResCopyWithImpl<$Res, _$MdmsResImpl>
    implements _$$MdmsResImplCopyWith<$Res> {
  __$$MdmsResImplCopyWithImpl(
      _$MdmsResImpl _value, $Res Function(_$MdmsResImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? commonMasters = freezed,
  }) {
    return _then(_$MdmsResImpl(
      commonMasters: freezed == commonMasters
          ? _value.commonMasters
          : commonMasters // ignore: cast_nullable_to_non_nullable
              as CommonMasters?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$MdmsResImpl implements _MdmsRes {
  const _$MdmsResImpl(
      {@JsonKey(name: 'common-masters') required this.commonMasters});

  factory _$MdmsResImpl.fromJson(Map<String, dynamic> json) =>
      _$$MdmsResImplFromJson(json);

  @override
  @JsonKey(name: 'common-masters')
  final CommonMasters? commonMasters;

  @override
  String toString() {
    return 'MdmsRes(commonMasters: $commonMasters)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$MdmsResImpl &&
            (identical(other.commonMasters, commonMasters) ||
                other.commonMasters == commonMasters));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, commonMasters);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$MdmsResImplCopyWith<_$MdmsResImpl> get copyWith =>
      __$$MdmsResImplCopyWithImpl<_$MdmsResImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$MdmsResImplToJson(
      this,
    );
  }
}

abstract class _MdmsRes implements MdmsRes {
  const factory _MdmsRes(
      {@JsonKey(name: 'common-masters')
      required final CommonMasters? commonMasters}) = _$MdmsResImpl;

  factory _MdmsRes.fromJson(Map<String, dynamic> json) = _$MdmsResImpl.fromJson;

  @override
  @JsonKey(name: 'common-masters')
  CommonMasters? get commonMasters;
  @override
  @JsonKey(ignore: true)
  _$$MdmsResImplCopyWith<_$MdmsResImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

CommonMasters _$CommonMastersFromJson(Map<String, dynamic> json) {
  return _CommonMasters.fromJson(json);
}

/// @nodoc
mixin _$CommonMasters {
  @JsonKey(name: 'StateInfo')
  List<StateInfo>? get stateInfo => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $CommonMastersCopyWith<CommonMasters> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $CommonMastersCopyWith<$Res> {
  factory $CommonMastersCopyWith(
          CommonMasters value, $Res Function(CommonMasters) then) =
      _$CommonMastersCopyWithImpl<$Res, CommonMasters>;
  @useResult
  $Res call({@JsonKey(name: 'StateInfo') List<StateInfo>? stateInfo});
}

/// @nodoc
class _$CommonMastersCopyWithImpl<$Res, $Val extends CommonMasters>
    implements $CommonMastersCopyWith<$Res> {
  _$CommonMastersCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? stateInfo = freezed,
  }) {
    return _then(_value.copyWith(
      stateInfo: freezed == stateInfo
          ? _value.stateInfo
          : stateInfo // ignore: cast_nullable_to_non_nullable
              as List<StateInfo>?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$CommonMastersImplCopyWith<$Res>
    implements $CommonMastersCopyWith<$Res> {
  factory _$$CommonMastersImplCopyWith(
          _$CommonMastersImpl value, $Res Function(_$CommonMastersImpl) then) =
      __$$CommonMastersImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({@JsonKey(name: 'StateInfo') List<StateInfo>? stateInfo});
}

/// @nodoc
class __$$CommonMastersImplCopyWithImpl<$Res>
    extends _$CommonMastersCopyWithImpl<$Res, _$CommonMastersImpl>
    implements _$$CommonMastersImplCopyWith<$Res> {
  __$$CommonMastersImplCopyWithImpl(
      _$CommonMastersImpl _value, $Res Function(_$CommonMastersImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? stateInfo = freezed,
  }) {
    return _then(_$CommonMastersImpl(
      stateInfo: freezed == stateInfo
          ? _value._stateInfo
          : stateInfo // ignore: cast_nullable_to_non_nullable
              as List<StateInfo>?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$CommonMastersImpl implements _CommonMasters {
  const _$CommonMastersImpl(
      {@JsonKey(name: 'StateInfo') required final List<StateInfo>? stateInfo})
      : _stateInfo = stateInfo;

  factory _$CommonMastersImpl.fromJson(Map<String, dynamic> json) =>
      _$$CommonMastersImplFromJson(json);

  final List<StateInfo>? _stateInfo;
  @override
  @JsonKey(name: 'StateInfo')
  List<StateInfo>? get stateInfo {
    final value = _stateInfo;
    if (value == null) return null;
    if (_stateInfo is EqualUnmodifiableListView) return _stateInfo;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(value);
  }

  @override
  String toString() {
    return 'CommonMasters(stateInfo: $stateInfo)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$CommonMastersImpl &&
            const DeepCollectionEquality()
                .equals(other._stateInfo, _stateInfo));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(_stateInfo));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$CommonMastersImplCopyWith<_$CommonMastersImpl> get copyWith =>
      __$$CommonMastersImplCopyWithImpl<_$CommonMastersImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$CommonMastersImplToJson(
      this,
    );
  }
}

abstract class _CommonMasters implements CommonMasters {
  const factory _CommonMasters(
      {@JsonKey(name: 'StateInfo')
      required final List<StateInfo>? stateInfo}) = _$CommonMastersImpl;

  factory _CommonMasters.fromJson(Map<String, dynamic> json) =
      _$CommonMastersImpl.fromJson;

  @override
  @JsonKey(name: 'StateInfo')
  List<StateInfo>? get stateInfo;
  @override
  @JsonKey(ignore: true)
  _$$CommonMastersImplCopyWith<_$CommonMastersImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

StateInfo _$StateInfoFromJson(Map<String, dynamic> json) {
  return _StateInfo.fromJson(json);
}

/// @nodoc
mixin _$StateInfo {
  @JsonKey(name: 'name')
  String get name => throw _privateConstructorUsedError;
  @JsonKey(name: 'code')
  String get code => throw _privateConstructorUsedError;
  @JsonKey(name: 'languages')
  List<Language> get languages => throw _privateConstructorUsedError;
  @JsonKey(name: 'localizationModules')
  List<Modules> get localizationModules => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $StateInfoCopyWith<StateInfo> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $StateInfoCopyWith<$Res> {
  factory $StateInfoCopyWith(StateInfo value, $Res Function(StateInfo) then) =
      _$StateInfoCopyWithImpl<$Res, StateInfo>;
  @useResult
  $Res call(
      {@JsonKey(name: 'name') String name,
      @JsonKey(name: 'code') String code,
      @JsonKey(name: 'languages') List<Language> languages,
      @JsonKey(name: 'localizationModules') List<Modules> localizationModules});
}

/// @nodoc
class _$StateInfoCopyWithImpl<$Res, $Val extends StateInfo>
    implements $StateInfoCopyWith<$Res> {
  _$StateInfoCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? code = null,
    Object? languages = null,
    Object? localizationModules = null,
  }) {
    return _then(_value.copyWith(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      code: null == code
          ? _value.code
          : code // ignore: cast_nullable_to_non_nullable
              as String,
      languages: null == languages
          ? _value.languages
          : languages // ignore: cast_nullable_to_non_nullable
              as List<Language>,
      localizationModules: null == localizationModules
          ? _value.localizationModules
          : localizationModules // ignore: cast_nullable_to_non_nullable
              as List<Modules>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$StateInfoImplCopyWith<$Res>
    implements $StateInfoCopyWith<$Res> {
  factory _$$StateInfoImplCopyWith(
          _$StateInfoImpl value, $Res Function(_$StateInfoImpl) then) =
      __$$StateInfoImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'name') String name,
      @JsonKey(name: 'code') String code,
      @JsonKey(name: 'languages') List<Language> languages,
      @JsonKey(name: 'localizationModules') List<Modules> localizationModules});
}

/// @nodoc
class __$$StateInfoImplCopyWithImpl<$Res>
    extends _$StateInfoCopyWithImpl<$Res, _$StateInfoImpl>
    implements _$$StateInfoImplCopyWith<$Res> {
  __$$StateInfoImplCopyWithImpl(
      _$StateInfoImpl _value, $Res Function(_$StateInfoImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? code = null,
    Object? languages = null,
    Object? localizationModules = null,
  }) {
    return _then(_$StateInfoImpl(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      code: null == code
          ? _value.code
          : code // ignore: cast_nullable_to_non_nullable
              as String,
      languages: null == languages
          ? _value._languages
          : languages // ignore: cast_nullable_to_non_nullable
              as List<Language>,
      localizationModules: null == localizationModules
          ? _value._localizationModules
          : localizationModules // ignore: cast_nullable_to_non_nullable
              as List<Modules>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$StateInfoImpl implements _StateInfo {
  _$StateInfoImpl(
      {@JsonKey(name: 'name') required this.name,
      @JsonKey(name: 'code') required this.code,
      @JsonKey(name: 'languages') required final List<Language> languages,
      @JsonKey(name: 'localizationModules')
      required final List<Modules> localizationModules})
      : _languages = languages,
        _localizationModules = localizationModules;

  factory _$StateInfoImpl.fromJson(Map<String, dynamic> json) =>
      _$$StateInfoImplFromJson(json);

  @override
  @JsonKey(name: 'name')
  final String name;
  @override
  @JsonKey(name: 'code')
  final String code;
  final List<Language> _languages;
  @override
  @JsonKey(name: 'languages')
  List<Language> get languages {
    if (_languages is EqualUnmodifiableListView) return _languages;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_languages);
  }

  final List<Modules> _localizationModules;
  @override
  @JsonKey(name: 'localizationModules')
  List<Modules> get localizationModules {
    if (_localizationModules is EqualUnmodifiableListView)
      return _localizationModules;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_localizationModules);
  }

  @override
  String toString() {
    return 'StateInfo(name: $name, code: $code, languages: $languages, localizationModules: $localizationModules)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$StateInfoImpl &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.code, code) || other.code == code) &&
            const DeepCollectionEquality()
                .equals(other._languages, _languages) &&
            const DeepCollectionEquality()
                .equals(other._localizationModules, _localizationModules));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      name,
      code,
      const DeepCollectionEquality().hash(_languages),
      const DeepCollectionEquality().hash(_localizationModules));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$StateInfoImplCopyWith<_$StateInfoImpl> get copyWith =>
      __$$StateInfoImplCopyWithImpl<_$StateInfoImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$StateInfoImplToJson(
      this,
    );
  }
}

abstract class _StateInfo implements StateInfo {
  factory _StateInfo(
      {@JsonKey(name: 'name') required final String name,
      @JsonKey(name: 'code') required final String code,
      @JsonKey(name: 'languages') required final List<Language> languages,
      @JsonKey(name: 'localizationModules')
      required final List<Modules> localizationModules}) = _$StateInfoImpl;

  factory _StateInfo.fromJson(Map<String, dynamic> json) =
      _$StateInfoImpl.fromJson;

  @override
  @JsonKey(name: 'name')
  String get name;
  @override
  @JsonKey(name: 'code')
  String get code;
  @override
  @JsonKey(name: 'languages')
  List<Language> get languages;
  @override
  @JsonKey(name: 'localizationModules')
  List<Modules> get localizationModules;
  @override
  @JsonKey(ignore: true)
  _$$StateInfoImplCopyWith<_$StateInfoImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

Modules _$ModulesFromJson(Map<String, dynamic> json) {
  return _Modules.fromJson(json);
}

/// @nodoc
mixin _$Modules {
  @JsonKey(name: 'label')
  String? get label => throw _privateConstructorUsedError;
  @JsonKey(name: 'value')
  String? get value => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $ModulesCopyWith<Modules> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $ModulesCopyWith<$Res> {
  factory $ModulesCopyWith(Modules value, $Res Function(Modules) then) =
      _$ModulesCopyWithImpl<$Res, Modules>;
  @useResult
  $Res call(
      {@JsonKey(name: 'label') String? label,
      @JsonKey(name: 'value') String? value});
}

/// @nodoc
class _$ModulesCopyWithImpl<$Res, $Val extends Modules>
    implements $ModulesCopyWith<$Res> {
  _$ModulesCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? label = freezed,
    Object? value = freezed,
  }) {
    return _then(_value.copyWith(
      label: freezed == label
          ? _value.label
          : label // ignore: cast_nullable_to_non_nullable
              as String?,
      value: freezed == value
          ? _value.value
          : value // ignore: cast_nullable_to_non_nullable
              as String?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$ModulesImplCopyWith<$Res> implements $ModulesCopyWith<$Res> {
  factory _$$ModulesImplCopyWith(
          _$ModulesImpl value, $Res Function(_$ModulesImpl) then) =
      __$$ModulesImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'label') String? label,
      @JsonKey(name: 'value') String? value});
}

/// @nodoc
class __$$ModulesImplCopyWithImpl<$Res>
    extends _$ModulesCopyWithImpl<$Res, _$ModulesImpl>
    implements _$$ModulesImplCopyWith<$Res> {
  __$$ModulesImplCopyWithImpl(
      _$ModulesImpl _value, $Res Function(_$ModulesImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? label = freezed,
    Object? value = freezed,
  }) {
    return _then(_$ModulesImpl(
      label: freezed == label
          ? _value.label
          : label // ignore: cast_nullable_to_non_nullable
              as String?,
      value: freezed == value
          ? _value.value
          : value // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$ModulesImpl implements _Modules {
  const _$ModulesImpl(
      {@JsonKey(name: 'label') required this.label,
      @JsonKey(name: 'value') required this.value});

  factory _$ModulesImpl.fromJson(Map<String, dynamic> json) =>
      _$$ModulesImplFromJson(json);

  @override
  @JsonKey(name: 'label')
  final String? label;
  @override
  @JsonKey(name: 'value')
  final String? value;

  @override
  String toString() {
    return 'Modules(label: $label, value: $value)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ModulesImpl &&
            (identical(other.label, label) || other.label == label) &&
            (identical(other.value, value) || other.value == value));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, label, value);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$ModulesImplCopyWith<_$ModulesImpl> get copyWith =>
      __$$ModulesImplCopyWithImpl<_$ModulesImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$ModulesImplToJson(
      this,
    );
  }
}

abstract class _Modules implements Modules {
  const factory _Modules(
      {@JsonKey(name: 'label') required final String? label,
      @JsonKey(name: 'value') required final String? value}) = _$ModulesImpl;

  factory _Modules.fromJson(Map<String, dynamic> json) = _$ModulesImpl.fromJson;

  @override
  @JsonKey(name: 'label')
  String? get label;
  @override
  @JsonKey(name: 'value')
  String? get value;
  @override
  @JsonKey(ignore: true)
  _$$ModulesImplCopyWith<_$ModulesImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

Language _$LanguageFromJson(Map<String, dynamic> json) {
  return _Language.fromJson(json);
}

/// @nodoc
mixin _$Language {
  @JsonKey(name: 'label')
  String get label => throw _privateConstructorUsedError;
  @JsonKey(name: 'value')
  String get value => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $LanguageCopyWith<Language> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $LanguageCopyWith<$Res> {
  factory $LanguageCopyWith(Language value, $Res Function(Language) then) =
      _$LanguageCopyWithImpl<$Res, Language>;
  @useResult
  $Res call(
      {@JsonKey(name: 'label') String label,
      @JsonKey(name: 'value') String value});
}

/// @nodoc
class _$LanguageCopyWithImpl<$Res, $Val extends Language>
    implements $LanguageCopyWith<$Res> {
  _$LanguageCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? label = null,
    Object? value = null,
  }) {
    return _then(_value.copyWith(
      label: null == label
          ? _value.label
          : label // ignore: cast_nullable_to_non_nullable
              as String,
      value: null == value
          ? _value.value
          : value // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$LanguageImplCopyWith<$Res>
    implements $LanguageCopyWith<$Res> {
  factory _$$LanguageImplCopyWith(
          _$LanguageImpl value, $Res Function(_$LanguageImpl) then) =
      __$$LanguageImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'label') String label,
      @JsonKey(name: 'value') String value});
}

/// @nodoc
class __$$LanguageImplCopyWithImpl<$Res>
    extends _$LanguageCopyWithImpl<$Res, _$LanguageImpl>
    implements _$$LanguageImplCopyWith<$Res> {
  __$$LanguageImplCopyWithImpl(
      _$LanguageImpl _value, $Res Function(_$LanguageImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? label = null,
    Object? value = null,
  }) {
    return _then(_$LanguageImpl(
      label: null == label
          ? _value.label
          : label // ignore: cast_nullable_to_non_nullable
              as String,
      value: null == value
          ? _value.value
          : value // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$LanguageImpl implements _Language {
  const _$LanguageImpl(
      {@JsonKey(name: 'label') required this.label,
      @JsonKey(name: 'value') required this.value});

  factory _$LanguageImpl.fromJson(Map<String, dynamic> json) =>
      _$$LanguageImplFromJson(json);

  @override
  @JsonKey(name: 'label')
  final String label;
  @override
  @JsonKey(name: 'value')
  final String value;

  @override
  String toString() {
    return 'Language(label: $label, value: $value)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$LanguageImpl &&
            (identical(other.label, label) || other.label == label) &&
            (identical(other.value, value) || other.value == value));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, label, value);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$LanguageImplCopyWith<_$LanguageImpl> get copyWith =>
      __$$LanguageImplCopyWithImpl<_$LanguageImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$LanguageImplToJson(
      this,
    );
  }
}

abstract class _Language implements Language {
  const factory _Language(
      {@JsonKey(name: 'label') required final String label,
      @JsonKey(name: 'value') required final String value}) = _$LanguageImpl;

  factory _Language.fromJson(Map<String, dynamic> json) =
      _$LanguageImpl.fromJson;

  @override
  @JsonKey(name: 'label')
  String get label;
  @override
  @JsonKey(name: 'value')
  String get value;
  @override
  @JsonKey(ignore: true)
  _$$LanguageImplCopyWith<_$LanguageImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
