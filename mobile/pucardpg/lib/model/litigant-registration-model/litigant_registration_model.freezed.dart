// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'litigant_registration_model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

Name _$NameFromJson(Map<String, dynamic> json) {
  return _Name.fromJson(json);
}

/// @nodoc
mixin _$Name {
  @JsonKey(name: 'givenName')
  String get givenName =>
      throw _privateConstructorUsedError; // Use "{{individualGivenName}}" for replacement
  @JsonKey(name: 'familyName')
  String get familyName => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $NameCopyWith<Name> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $NameCopyWith<$Res> {
  factory $NameCopyWith(Name value, $Res Function(Name) then) =
      _$NameCopyWithImpl<$Res, Name>;
  @useResult
  $Res call(
      {@JsonKey(name: 'givenName') String givenName,
      @JsonKey(name: 'familyName') String familyName});
}

/// @nodoc
class _$NameCopyWithImpl<$Res, $Val extends Name>
    implements $NameCopyWith<$Res> {
  _$NameCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? givenName = null,
    Object? familyName = null,
  }) {
    return _then(_value.copyWith(
      givenName: null == givenName
          ? _value.givenName
          : givenName // ignore: cast_nullable_to_non_nullable
              as String,
      familyName: null == familyName
          ? _value.familyName
          : familyName // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$NameImplCopyWith<$Res> implements $NameCopyWith<$Res> {
  factory _$$NameImplCopyWith(
          _$NameImpl value, $Res Function(_$NameImpl) then) =
      __$$NameImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'givenName') String givenName,
      @JsonKey(name: 'familyName') String familyName});
}

/// @nodoc
class __$$NameImplCopyWithImpl<$Res>
    extends _$NameCopyWithImpl<$Res, _$NameImpl>
    implements _$$NameImplCopyWith<$Res> {
  __$$NameImplCopyWithImpl(_$NameImpl _value, $Res Function(_$NameImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? givenName = null,
    Object? familyName = null,
  }) {
    return _then(_$NameImpl(
      givenName: null == givenName
          ? _value.givenName
          : givenName // ignore: cast_nullable_to_non_nullable
              as String,
      familyName: null == familyName
          ? _value.familyName
          : familyName // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$NameImpl implements _Name {
  const _$NameImpl(
      {@JsonKey(name: 'givenName') required this.givenName,
      @JsonKey(name: 'familyName') required this.familyName});

  factory _$NameImpl.fromJson(Map<String, dynamic> json) =>
      _$$NameImplFromJson(json);

  @override
  @JsonKey(name: 'givenName')
  final String givenName;
// Use "{{individualGivenName}}" for replacement
  @override
  @JsonKey(name: 'familyName')
  final String familyName;

  @override
  String toString() {
    return 'Name(givenName: $givenName, familyName: $familyName)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$NameImpl &&
            (identical(other.givenName, givenName) ||
                other.givenName == givenName) &&
            (identical(other.familyName, familyName) ||
                other.familyName == familyName));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, givenName, familyName);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$NameImplCopyWith<_$NameImpl> get copyWith =>
      __$$NameImplCopyWithImpl<_$NameImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$NameImplToJson(
      this,
    );
  }
}

abstract class _Name implements Name {
  const factory _Name(
          {@JsonKey(name: 'givenName') required final String givenName,
          @JsonKey(name: 'familyName') required final String familyName}) =
      _$NameImpl;

  factory _Name.fromJson(Map<String, dynamic> json) = _$NameImpl.fromJson;

  @override
  @JsonKey(name: 'givenName')
  String get givenName;
  @override // Use "{{individualGivenName}}" for replacement
  @JsonKey(name: 'familyName')
  String get familyName;
  @override
  @JsonKey(ignore: true)
  _$$NameImplCopyWith<_$NameImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

Address _$AddressFromJson(Map<String, dynamic> json) {
  return _Address.fromJson(json);
}

/// @nodoc
mixin _$Address {
  @JsonKey(name: 'tenantId')
  String get tenantId => throw _privateConstructorUsedError;
  @JsonKey(name: 'type')
  String get type => throw _privateConstructorUsedError;
  @JsonKey(name: 'addressLine1')
  String? get addressLine1 => throw _privateConstructorUsedError;
  @JsonKey(name: 'addressLine2')
  String? get addressLine2 => throw _privateConstructorUsedError;
  @JsonKey(name: 'doorNo')
  String? get doorNo => throw _privateConstructorUsedError;
  @JsonKey(name: 'buildingName')
  String? get buildingName => throw _privateConstructorUsedError;
  @JsonKey(name: 'latitude')
  double? get latitude => throw _privateConstructorUsedError;
  @JsonKey(name: 'longitude')
  double? get longitude => throw _privateConstructorUsedError;
  @JsonKey(name: 'city')
  String? get city => throw _privateConstructorUsedError;
  @JsonKey(name: 'street')
  String? get street => throw _privateConstructorUsedError;
  @JsonKey(name: 'pincode')
  String? get pincode => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AddressCopyWith<Address> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AddressCopyWith<$Res> {
  factory $AddressCopyWith(Address value, $Res Function(Address) then) =
      _$AddressCopyWithImpl<$Res, Address>;
  @useResult
  $Res call(
      {@JsonKey(name: 'tenantId') String tenantId,
      @JsonKey(name: 'type') String type,
      @JsonKey(name: 'addressLine1') String? addressLine1,
      @JsonKey(name: 'addressLine2') String? addressLine2,
      @JsonKey(name: 'doorNo') String? doorNo,
      @JsonKey(name: 'buildingName') String? buildingName,
      @JsonKey(name: 'latitude') double? latitude,
      @JsonKey(name: 'longitude') double? longitude,
      @JsonKey(name: 'city') String? city,
      @JsonKey(name: 'street') String? street,
      @JsonKey(name: 'pincode') String? pincode});
}

/// @nodoc
class _$AddressCopyWithImpl<$Res, $Val extends Address>
    implements $AddressCopyWith<$Res> {
  _$AddressCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? tenantId = null,
    Object? type = null,
    Object? addressLine1 = freezed,
    Object? addressLine2 = freezed,
    Object? doorNo = freezed,
    Object? buildingName = freezed,
    Object? latitude = freezed,
    Object? longitude = freezed,
    Object? city = freezed,
    Object? street = freezed,
    Object? pincode = freezed,
  }) {
    return _then(_value.copyWith(
      tenantId: null == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String,
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String,
      addressLine1: freezed == addressLine1
          ? _value.addressLine1
          : addressLine1 // ignore: cast_nullable_to_non_nullable
              as String?,
      addressLine2: freezed == addressLine2
          ? _value.addressLine2
          : addressLine2 // ignore: cast_nullable_to_non_nullable
              as String?,
      doorNo: freezed == doorNo
          ? _value.doorNo
          : doorNo // ignore: cast_nullable_to_non_nullable
              as String?,
      buildingName: freezed == buildingName
          ? _value.buildingName
          : buildingName // ignore: cast_nullable_to_non_nullable
              as String?,
      latitude: freezed == latitude
          ? _value.latitude
          : latitude // ignore: cast_nullable_to_non_nullable
              as double?,
      longitude: freezed == longitude
          ? _value.longitude
          : longitude // ignore: cast_nullable_to_non_nullable
              as double?,
      city: freezed == city
          ? _value.city
          : city // ignore: cast_nullable_to_non_nullable
              as String?,
      street: freezed == street
          ? _value.street
          : street // ignore: cast_nullable_to_non_nullable
              as String?,
      pincode: freezed == pincode
          ? _value.pincode
          : pincode // ignore: cast_nullable_to_non_nullable
              as String?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$AddressImplCopyWith<$Res> implements $AddressCopyWith<$Res> {
  factory _$$AddressImplCopyWith(
          _$AddressImpl value, $Res Function(_$AddressImpl) then) =
      __$$AddressImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'tenantId') String tenantId,
      @JsonKey(name: 'type') String type,
      @JsonKey(name: 'addressLine1') String? addressLine1,
      @JsonKey(name: 'addressLine2') String? addressLine2,
      @JsonKey(name: 'doorNo') String? doorNo,
      @JsonKey(name: 'buildingName') String? buildingName,
      @JsonKey(name: 'latitude') double? latitude,
      @JsonKey(name: 'longitude') double? longitude,
      @JsonKey(name: 'city') String? city,
      @JsonKey(name: 'street') String? street,
      @JsonKey(name: 'pincode') String? pincode});
}

/// @nodoc
class __$$AddressImplCopyWithImpl<$Res>
    extends _$AddressCopyWithImpl<$Res, _$AddressImpl>
    implements _$$AddressImplCopyWith<$Res> {
  __$$AddressImplCopyWithImpl(
      _$AddressImpl _value, $Res Function(_$AddressImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? tenantId = null,
    Object? type = null,
    Object? addressLine1 = freezed,
    Object? addressLine2 = freezed,
    Object? doorNo = freezed,
    Object? buildingName = freezed,
    Object? latitude = freezed,
    Object? longitude = freezed,
    Object? city = freezed,
    Object? street = freezed,
    Object? pincode = freezed,
  }) {
    return _then(_$AddressImpl(
      tenantId: null == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String,
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String,
      addressLine1: freezed == addressLine1
          ? _value.addressLine1
          : addressLine1 // ignore: cast_nullable_to_non_nullable
              as String?,
      addressLine2: freezed == addressLine2
          ? _value.addressLine2
          : addressLine2 // ignore: cast_nullable_to_non_nullable
              as String?,
      doorNo: freezed == doorNo
          ? _value.doorNo
          : doorNo // ignore: cast_nullable_to_non_nullable
              as String?,
      buildingName: freezed == buildingName
          ? _value.buildingName
          : buildingName // ignore: cast_nullable_to_non_nullable
              as String?,
      latitude: freezed == latitude
          ? _value.latitude
          : latitude // ignore: cast_nullable_to_non_nullable
              as double?,
      longitude: freezed == longitude
          ? _value.longitude
          : longitude // ignore: cast_nullable_to_non_nullable
              as double?,
      city: freezed == city
          ? _value.city
          : city // ignore: cast_nullable_to_non_nullable
              as String?,
      street: freezed == street
          ? _value.street
          : street // ignore: cast_nullable_to_non_nullable
              as String?,
      pincode: freezed == pincode
          ? _value.pincode
          : pincode // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AddressImpl implements _Address {
  const _$AddressImpl(
      {@JsonKey(name: 'tenantId') required this.tenantId,
      @JsonKey(name: 'type') this.type = "PERMANENT",
      @JsonKey(name: 'addressLine1') required this.addressLine1,
      @JsonKey(name: 'addressLine2') required this.addressLine2,
      @JsonKey(name: 'doorNo') required this.doorNo,
      @JsonKey(name: 'buildingName') required this.buildingName,
      @JsonKey(name: 'latitude') required this.latitude,
      @JsonKey(name: 'longitude') required this.longitude,
      @JsonKey(name: 'city') required this.city,
      @JsonKey(name: 'street') required this.street,
      @JsonKey(name: 'pincode') required this.pincode});

  factory _$AddressImpl.fromJson(Map<String, dynamic> json) =>
      _$$AddressImplFromJson(json);

  @override
  @JsonKey(name: 'tenantId')
  final String tenantId;
  @override
  @JsonKey(name: 'type')
  final String type;
  @override
  @JsonKey(name: 'addressLine1')
  final String? addressLine1;
  @override
  @JsonKey(name: 'addressLine2')
  final String? addressLine2;
  @override
  @JsonKey(name: 'doorNo')
  final String? doorNo;
  @override
  @JsonKey(name: 'buildingName')
  final String? buildingName;
  @override
  @JsonKey(name: 'latitude')
  final double? latitude;
  @override
  @JsonKey(name: 'longitude')
  final double? longitude;
  @override
  @JsonKey(name: 'city')
  final String? city;
  @override
  @JsonKey(name: 'street')
  final String? street;
  @override
  @JsonKey(name: 'pincode')
  final String? pincode;

  @override
  String toString() {
    return 'Address(tenantId: $tenantId, type: $type, addressLine1: $addressLine1, addressLine2: $addressLine2, doorNo: $doorNo, buildingName: $buildingName, latitude: $latitude, longitude: $longitude, city: $city, street: $street, pincode: $pincode)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AddressImpl &&
            (identical(other.tenantId, tenantId) ||
                other.tenantId == tenantId) &&
            (identical(other.type, type) || other.type == type) &&
            (identical(other.addressLine1, addressLine1) ||
                other.addressLine1 == addressLine1) &&
            (identical(other.addressLine2, addressLine2) ||
                other.addressLine2 == addressLine2) &&
            (identical(other.doorNo, doorNo) || other.doorNo == doorNo) &&
            (identical(other.buildingName, buildingName) ||
                other.buildingName == buildingName) &&
            (identical(other.latitude, latitude) ||
                other.latitude == latitude) &&
            (identical(other.longitude, longitude) ||
                other.longitude == longitude) &&
            (identical(other.city, city) || other.city == city) &&
            (identical(other.street, street) || other.street == street) &&
            (identical(other.pincode, pincode) || other.pincode == pincode));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      tenantId,
      type,
      addressLine1,
      addressLine2,
      doorNo,
      buildingName,
      latitude,
      longitude,
      city,
      street,
      pincode);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AddressImplCopyWith<_$AddressImpl> get copyWith =>
      __$$AddressImplCopyWithImpl<_$AddressImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AddressImplToJson(
      this,
    );
  }
}

abstract class _Address implements Address {
  const factory _Address(
          {@JsonKey(name: 'tenantId') required final String tenantId,
          @JsonKey(name: 'type') final String type,
          @JsonKey(name: 'addressLine1') required final String? addressLine1,
          @JsonKey(name: 'addressLine2') required final String? addressLine2,
          @JsonKey(name: 'doorNo') required final String? doorNo,
          @JsonKey(name: 'buildingName') required final String? buildingName,
          @JsonKey(name: 'latitude') required final double? latitude,
          @JsonKey(name: 'longitude') required final double? longitude,
          @JsonKey(name: 'city') required final String? city,
          @JsonKey(name: 'street') required final String? street,
          @JsonKey(name: 'pincode') required final String? pincode}) =
      _$AddressImpl;

  factory _Address.fromJson(Map<String, dynamic> json) = _$AddressImpl.fromJson;

  @override
  @JsonKey(name: 'tenantId')
  String get tenantId;
  @override
  @JsonKey(name: 'type')
  String get type;
  @override
  @JsonKey(name: 'addressLine1')
  String? get addressLine1;
  @override
  @JsonKey(name: 'addressLine2')
  String? get addressLine2;
  @override
  @JsonKey(name: 'doorNo')
  String? get doorNo;
  @override
  @JsonKey(name: 'buildingName')
  String? get buildingName;
  @override
  @JsonKey(name: 'latitude')
  double? get latitude;
  @override
  @JsonKey(name: 'longitude')
  double? get longitude;
  @override
  @JsonKey(name: 'city')
  String? get city;
  @override
  @JsonKey(name: 'street')
  String? get street;
  @override
  @JsonKey(name: 'pincode')
  String? get pincode;
  @override
  @JsonKey(ignore: true)
  _$$AddressImplCopyWith<_$AddressImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

Identifier _$IdentifierFromJson(Map<String, dynamic> json) {
  return _Identifier.fromJson(json);
}

/// @nodoc
mixin _$Identifier {
  @JsonKey(name: 'identifierType')
  String get identifierType => throw _privateConstructorUsedError;
  @JsonKey(name: 'identifierId')
  String get identifierId => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $IdentifierCopyWith<Identifier> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $IdentifierCopyWith<$Res> {
  factory $IdentifierCopyWith(
          Identifier value, $Res Function(Identifier) then) =
      _$IdentifierCopyWithImpl<$Res, Identifier>;
  @useResult
  $Res call(
      {@JsonKey(name: 'identifierType') String identifierType,
      @JsonKey(name: 'identifierId') String identifierId});
}

/// @nodoc
class _$IdentifierCopyWithImpl<$Res, $Val extends Identifier>
    implements $IdentifierCopyWith<$Res> {
  _$IdentifierCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? identifierType = null,
    Object? identifierId = null,
  }) {
    return _then(_value.copyWith(
      identifierType: null == identifierType
          ? _value.identifierType
          : identifierType // ignore: cast_nullable_to_non_nullable
              as String,
      identifierId: null == identifierId
          ? _value.identifierId
          : identifierId // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$IdentifierImplCopyWith<$Res>
    implements $IdentifierCopyWith<$Res> {
  factory _$$IdentifierImplCopyWith(
          _$IdentifierImpl value, $Res Function(_$IdentifierImpl) then) =
      __$$IdentifierImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'identifierType') String identifierType,
      @JsonKey(name: 'identifierId') String identifierId});
}

/// @nodoc
class __$$IdentifierImplCopyWithImpl<$Res>
    extends _$IdentifierCopyWithImpl<$Res, _$IdentifierImpl>
    implements _$$IdentifierImplCopyWith<$Res> {
  __$$IdentifierImplCopyWithImpl(
      _$IdentifierImpl _value, $Res Function(_$IdentifierImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? identifierType = null,
    Object? identifierId = null,
  }) {
    return _then(_$IdentifierImpl(
      identifierType: null == identifierType
          ? _value.identifierType
          : identifierType // ignore: cast_nullable_to_non_nullable
              as String,
      identifierId: null == identifierId
          ? _value.identifierId
          : identifierId // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$IdentifierImpl implements _Identifier {
  const _$IdentifierImpl(
      {@JsonKey(name: 'identifierType') required this.identifierType,
      @JsonKey(name: 'identifierId') required this.identifierId});

  factory _$IdentifierImpl.fromJson(Map<String, dynamic> json) =>
      _$$IdentifierImplFromJson(json);

  @override
  @JsonKey(name: 'identifierType')
  final String identifierType;
  @override
  @JsonKey(name: 'identifierId')
  final String identifierId;

  @override
  String toString() {
    return 'Identifier(identifierType: $identifierType, identifierId: $identifierId)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$IdentifierImpl &&
            (identical(other.identifierType, identifierType) ||
                other.identifierType == identifierType) &&
            (identical(other.identifierId, identifierId) ||
                other.identifierId == identifierId));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, identifierType, identifierId);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$IdentifierImplCopyWith<_$IdentifierImpl> get copyWith =>
      __$$IdentifierImplCopyWithImpl<_$IdentifierImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$IdentifierImplToJson(
      this,
    );
  }
}

abstract class _Identifier implements Identifier {
  const factory _Identifier(
      {@JsonKey(name: 'identifierType') required final String identifierType,
      @JsonKey(name: 'identifierId')
      required final String identifierId}) = _$IdentifierImpl;

  factory _Identifier.fromJson(Map<String, dynamic> json) =
      _$IdentifierImpl.fromJson;

  @override
  @JsonKey(name: 'identifierType')
  String get identifierType;
  @override
  @JsonKey(name: 'identifierId')
  String get identifierId;
  @override
  @JsonKey(ignore: true)
  _$$IdentifierImplCopyWith<_$IdentifierImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

AdditionalFields _$AdditionalFieldsFromJson(Map<String, dynamic> json) {
  return _AdditionalFields.fromJson(json);
}

/// @nodoc
mixin _$AdditionalFields {
  @JsonKey(name: 'fields')
  List<Fields> get fields => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AdditionalFieldsCopyWith<AdditionalFields> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AdditionalFieldsCopyWith<$Res> {
  factory $AdditionalFieldsCopyWith(
          AdditionalFields value, $Res Function(AdditionalFields) then) =
      _$AdditionalFieldsCopyWithImpl<$Res, AdditionalFields>;
  @useResult
  $Res call({@JsonKey(name: 'fields') List<Fields> fields});
}

/// @nodoc
class _$AdditionalFieldsCopyWithImpl<$Res, $Val extends AdditionalFields>
    implements $AdditionalFieldsCopyWith<$Res> {
  _$AdditionalFieldsCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fields = null,
  }) {
    return _then(_value.copyWith(
      fields: null == fields
          ? _value.fields
          : fields // ignore: cast_nullable_to_non_nullable
              as List<Fields>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$AdditionalFieldsImplCopyWith<$Res>
    implements $AdditionalFieldsCopyWith<$Res> {
  factory _$$AdditionalFieldsImplCopyWith(_$AdditionalFieldsImpl value,
          $Res Function(_$AdditionalFieldsImpl) then) =
      __$$AdditionalFieldsImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({@JsonKey(name: 'fields') List<Fields> fields});
}

/// @nodoc
class __$$AdditionalFieldsImplCopyWithImpl<$Res>
    extends _$AdditionalFieldsCopyWithImpl<$Res, _$AdditionalFieldsImpl>
    implements _$$AdditionalFieldsImplCopyWith<$Res> {
  __$$AdditionalFieldsImplCopyWithImpl(_$AdditionalFieldsImpl _value,
      $Res Function(_$AdditionalFieldsImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fields = null,
  }) {
    return _then(_$AdditionalFieldsImpl(
      fields: null == fields
          ? _value._fields
          : fields // ignore: cast_nullable_to_non_nullable
              as List<Fields>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AdditionalFieldsImpl implements _AdditionalFields {
  const _$AdditionalFieldsImpl(
      {@JsonKey(name: 'fields') final List<Fields> fields = const []})
      : _fields = fields;

  factory _$AdditionalFieldsImpl.fromJson(Map<String, dynamic> json) =>
      _$$AdditionalFieldsImplFromJson(json);

  final List<Fields> _fields;
  @override
  @JsonKey(name: 'fields')
  List<Fields> get fields {
    if (_fields is EqualUnmodifiableListView) return _fields;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_fields);
  }

  @override
  String toString() {
    return 'AdditionalFields(fields: $fields)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdditionalFieldsImpl &&
            const DeepCollectionEquality().equals(other._fields, _fields));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(_fields));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AdditionalFieldsImplCopyWith<_$AdditionalFieldsImpl> get copyWith =>
      __$$AdditionalFieldsImplCopyWithImpl<_$AdditionalFieldsImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AdditionalFieldsImplToJson(
      this,
    );
  }
}

abstract class _AdditionalFields implements AdditionalFields {
  const factory _AdditionalFields(
          {@JsonKey(name: 'fields') final List<Fields> fields}) =
      _$AdditionalFieldsImpl;

  factory _AdditionalFields.fromJson(Map<String, dynamic> json) =
      _$AdditionalFieldsImpl.fromJson;

  @override
  @JsonKey(name: 'fields')
  List<Fields> get fields;
  @override
  @JsonKey(ignore: true)
  _$$AdditionalFieldsImplCopyWith<_$AdditionalFieldsImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

Fields _$FieldsFromJson(Map<String, dynamic> json) {
  return _Fields.fromJson(json);
}

/// @nodoc
mixin _$Fields {
  @JsonKey(name: 'key')
  String get key =>
      throw _privateConstructorUsedError; // Use "{{individualSkillType}}" for replacement
  @JsonKey(name: 'value')
  String get value => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $FieldsCopyWith<Fields> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $FieldsCopyWith<$Res> {
  factory $FieldsCopyWith(Fields value, $Res Function(Fields) then) =
      _$FieldsCopyWithImpl<$Res, Fields>;
  @useResult
  $Res call(
      {@JsonKey(name: 'key') String key, @JsonKey(name: 'value') String value});
}

/// @nodoc
class _$FieldsCopyWithImpl<$Res, $Val extends Fields>
    implements $FieldsCopyWith<$Res> {
  _$FieldsCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? key = null,
    Object? value = null,
  }) {
    return _then(_value.copyWith(
      key: null == key
          ? _value.key
          : key // ignore: cast_nullable_to_non_nullable
              as String,
      value: null == value
          ? _value.value
          : value // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$FieldsImplCopyWith<$Res> implements $FieldsCopyWith<$Res> {
  factory _$$FieldsImplCopyWith(
          _$FieldsImpl value, $Res Function(_$FieldsImpl) then) =
      __$$FieldsImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'key') String key, @JsonKey(name: 'value') String value});
}

/// @nodoc
class __$$FieldsImplCopyWithImpl<$Res>
    extends _$FieldsCopyWithImpl<$Res, _$FieldsImpl>
    implements _$$FieldsImplCopyWith<$Res> {
  __$$FieldsImplCopyWithImpl(
      _$FieldsImpl _value, $Res Function(_$FieldsImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? key = null,
    Object? value = null,
  }) {
    return _then(_$FieldsImpl(
      key: null == key
          ? _value.key
          : key // ignore: cast_nullable_to_non_nullable
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
class _$FieldsImpl implements _Fields {
  const _$FieldsImpl(
      {@JsonKey(name: 'key') required this.key,
      @JsonKey(name: 'value') required this.value});

  factory _$FieldsImpl.fromJson(Map<String, dynamic> json) =>
      _$$FieldsImplFromJson(json);

  @override
  @JsonKey(name: 'key')
  final String key;
// Use "{{individualSkillType}}" for replacement
  @override
  @JsonKey(name: 'value')
  final String value;

  @override
  String toString() {
    return 'Fields(key: $key, value: $value)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$FieldsImpl &&
            (identical(other.key, key) || other.key == key) &&
            (identical(other.value, value) || other.value == value));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, key, value);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$FieldsImplCopyWith<_$FieldsImpl> get copyWith =>
      __$$FieldsImplCopyWithImpl<_$FieldsImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$FieldsImplToJson(
      this,
    );
  }
}

abstract class _Fields implements Fields {
  const factory _Fields(
      {@JsonKey(name: 'key') required final String key,
      @JsonKey(name: 'value') required final String value}) = _$FieldsImpl;

  factory _Fields.fromJson(Map<String, dynamic> json) = _$FieldsImpl.fromJson;

  @override
  @JsonKey(name: 'key')
  String get key;
  @override // Use "{{individualSkillType}}" for replacement
  @JsonKey(name: 'value')
  String get value;
  @override
  @JsonKey(ignore: true)
  _$$FieldsImplCopyWith<_$FieldsImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

UserDetails _$UserDetailsFromJson(Map<String, dynamic> json) {
  return _UserDetails.fromJson(json);
}

/// @nodoc
mixin _$UserDetails {
  @JsonKey(name: 'username')
  String get username => throw _privateConstructorUsedError;
  @JsonKey(name: 'roles')
  List<Role> get roles => throw _privateConstructorUsedError;
  @JsonKey(name: 'type')
  String get type => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $UserDetailsCopyWith<UserDetails> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $UserDetailsCopyWith<$Res> {
  factory $UserDetailsCopyWith(
          UserDetails value, $Res Function(UserDetails) then) =
      _$UserDetailsCopyWithImpl<$Res, UserDetails>;
  @useResult
  $Res call(
      {@JsonKey(name: 'username') String username,
      @JsonKey(name: 'roles') List<Role> roles,
      @JsonKey(name: 'type') String type});
}

/// @nodoc
class _$UserDetailsCopyWithImpl<$Res, $Val extends UserDetails>
    implements $UserDetailsCopyWith<$Res> {
  _$UserDetailsCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? username = null,
    Object? roles = null,
    Object? type = null,
  }) {
    return _then(_value.copyWith(
      username: null == username
          ? _value.username
          : username // ignore: cast_nullable_to_non_nullable
              as String,
      roles: null == roles
          ? _value.roles
          : roles // ignore: cast_nullable_to_non_nullable
              as List<Role>,
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$UserDetailsImplCopyWith<$Res>
    implements $UserDetailsCopyWith<$Res> {
  factory _$$UserDetailsImplCopyWith(
          _$UserDetailsImpl value, $Res Function(_$UserDetailsImpl) then) =
      __$$UserDetailsImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'username') String username,
      @JsonKey(name: 'roles') List<Role> roles,
      @JsonKey(name: 'type') String type});
}

/// @nodoc
class __$$UserDetailsImplCopyWithImpl<$Res>
    extends _$UserDetailsCopyWithImpl<$Res, _$UserDetailsImpl>
    implements _$$UserDetailsImplCopyWith<$Res> {
  __$$UserDetailsImplCopyWithImpl(
      _$UserDetailsImpl _value, $Res Function(_$UserDetailsImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? username = null,
    Object? roles = null,
    Object? type = null,
  }) {
    return _then(_$UserDetailsImpl(
      username: null == username
          ? _value.username
          : username // ignore: cast_nullable_to_non_nullable
              as String,
      roles: null == roles
          ? _value._roles
          : roles // ignore: cast_nullable_to_non_nullable
              as List<Role>,
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$UserDetailsImpl implements _UserDetails {
  const _$UserDetailsImpl(
      {@JsonKey(name: 'username') required this.username,
      @JsonKey(name: 'roles') required final List<Role> roles,
      @JsonKey(name: 'type') this.type = "CITIZEN"})
      : _roles = roles;

  factory _$UserDetailsImpl.fromJson(Map<String, dynamic> json) =>
      _$$UserDetailsImplFromJson(json);

  @override
  @JsonKey(name: 'username')
  final String username;
  final List<Role> _roles;
  @override
  @JsonKey(name: 'roles')
  List<Role> get roles {
    if (_roles is EqualUnmodifiableListView) return _roles;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_roles);
  }

  @override
  @JsonKey(name: 'type')
  final String type;

  @override
  String toString() {
    return 'UserDetails(username: $username, roles: $roles, type: $type)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$UserDetailsImpl &&
            (identical(other.username, username) ||
                other.username == username) &&
            const DeepCollectionEquality().equals(other._roles, _roles) &&
            (identical(other.type, type) || other.type == type));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType, username, const DeepCollectionEquality().hash(_roles), type);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$UserDetailsImplCopyWith<_$UserDetailsImpl> get copyWith =>
      __$$UserDetailsImplCopyWithImpl<_$UserDetailsImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$UserDetailsImplToJson(
      this,
    );
  }
}

abstract class _UserDetails implements UserDetails {
  const factory _UserDetails(
      {@JsonKey(name: 'username') required final String username,
      @JsonKey(name: 'roles') required final List<Role> roles,
      @JsonKey(name: 'type') final String type}) = _$UserDetailsImpl;

  factory _UserDetails.fromJson(Map<String, dynamic> json) =
      _$UserDetailsImpl.fromJson;

  @override
  @JsonKey(name: 'username')
  String get username;
  @override
  @JsonKey(name: 'roles')
  List<Role> get roles;
  @override
  @JsonKey(name: 'type')
  String get type;
  @override
  @JsonKey(ignore: true)
  _$$UserDetailsImplCopyWith<_$UserDetailsImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

Individual _$IndividualFromJson(Map<String, dynamic> json) {
  return _Individual.fromJson(json);
}

/// @nodoc
mixin _$Individual {
  @JsonKey(name: 'tenantId')
  String get tenantId => throw _privateConstructorUsedError;
  @JsonKey(name: 'individualId')
  String? get individualId => throw _privateConstructorUsedError;
  @JsonKey(name: 'name')
  Name get name => throw _privateConstructorUsedError;
  @JsonKey(name: 'userDetails')
  UserDetails get userDetails =>
      throw _privateConstructorUsedError; // Use "{{individualDateOfBirth}}" for replacement
  @JsonKey(name: 'userUuid')
  String get userUuid =>
      throw _privateConstructorUsedError; // Use "{{individualGenderType}}" for replacement
  @JsonKey(name: 'userId')
  String get userId => throw _privateConstructorUsedError;
  @JsonKey(name: 'mobileNumber')
  String get mobileNumber => throw _privateConstructorUsedError;
  @JsonKey(name: 'address')
  List<Address> get address => throw _privateConstructorUsedError;
  @JsonKey(name: 'identifiers')
  List<Identifier> get identifiers => throw _privateConstructorUsedError;
  @JsonKey(name: 'isSystemUser')
  bool get isSystemUser => throw _privateConstructorUsedError;
  @JsonKey(name: 'skills')
  List<Skill> get skills => throw _privateConstructorUsedError;
  @JsonKey(name: 'additionalFields')
  AdditionalFields get additionalFields => throw _privateConstructorUsedError;
  @JsonKey(name: 'clientAuditDetails')
  ClientAuditDetails get clientAuditDetails =>
      throw _privateConstructorUsedError;
  @JsonKey(name: 'auditDetails')
  AuditDetails? get auditDetails => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $IndividualCopyWith<Individual> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $IndividualCopyWith<$Res> {
  factory $IndividualCopyWith(
          Individual value, $Res Function(Individual) then) =
      _$IndividualCopyWithImpl<$Res, Individual>;
  @useResult
  $Res call(
      {@JsonKey(name: 'tenantId') String tenantId,
      @JsonKey(name: 'individualId') String? individualId,
      @JsonKey(name: 'name') Name name,
      @JsonKey(name: 'userDetails') UserDetails userDetails,
      @JsonKey(name: 'userUuid') String userUuid,
      @JsonKey(name: 'userId') String userId,
      @JsonKey(name: 'mobileNumber') String mobileNumber,
      @JsonKey(name: 'address') List<Address> address,
      @JsonKey(name: 'identifiers') List<Identifier> identifiers,
      @JsonKey(name: 'isSystemUser') bool isSystemUser,
      @JsonKey(name: 'skills') List<Skill> skills,
      @JsonKey(name: 'additionalFields') AdditionalFields additionalFields,
      @JsonKey(name: 'clientAuditDetails')
      ClientAuditDetails clientAuditDetails,
      @JsonKey(name: 'auditDetails') AuditDetails? auditDetails});

  $NameCopyWith<$Res> get name;
  $UserDetailsCopyWith<$Res> get userDetails;
  $AdditionalFieldsCopyWith<$Res> get additionalFields;
  $ClientAuditDetailsCopyWith<$Res> get clientAuditDetails;
  $AuditDetailsCopyWith<$Res>? get auditDetails;
}

/// @nodoc
class _$IndividualCopyWithImpl<$Res, $Val extends Individual>
    implements $IndividualCopyWith<$Res> {
  _$IndividualCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? tenantId = null,
    Object? individualId = freezed,
    Object? name = null,
    Object? userDetails = null,
    Object? userUuid = null,
    Object? userId = null,
    Object? mobileNumber = null,
    Object? address = null,
    Object? identifiers = null,
    Object? isSystemUser = null,
    Object? skills = null,
    Object? additionalFields = null,
    Object? clientAuditDetails = null,
    Object? auditDetails = freezed,
  }) {
    return _then(_value.copyWith(
      tenantId: null == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String,
      individualId: freezed == individualId
          ? _value.individualId
          : individualId // ignore: cast_nullable_to_non_nullable
              as String?,
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as Name,
      userDetails: null == userDetails
          ? _value.userDetails
          : userDetails // ignore: cast_nullable_to_non_nullable
              as UserDetails,
      userUuid: null == userUuid
          ? _value.userUuid
          : userUuid // ignore: cast_nullable_to_non_nullable
              as String,
      userId: null == userId
          ? _value.userId
          : userId // ignore: cast_nullable_to_non_nullable
              as String,
      mobileNumber: null == mobileNumber
          ? _value.mobileNumber
          : mobileNumber // ignore: cast_nullable_to_non_nullable
              as String,
      address: null == address
          ? _value.address
          : address // ignore: cast_nullable_to_non_nullable
              as List<Address>,
      identifiers: null == identifiers
          ? _value.identifiers
          : identifiers // ignore: cast_nullable_to_non_nullable
              as List<Identifier>,
      isSystemUser: null == isSystemUser
          ? _value.isSystemUser
          : isSystemUser // ignore: cast_nullable_to_non_nullable
              as bool,
      skills: null == skills
          ? _value.skills
          : skills // ignore: cast_nullable_to_non_nullable
              as List<Skill>,
      additionalFields: null == additionalFields
          ? _value.additionalFields
          : additionalFields // ignore: cast_nullable_to_non_nullable
              as AdditionalFields,
      clientAuditDetails: null == clientAuditDetails
          ? _value.clientAuditDetails
          : clientAuditDetails // ignore: cast_nullable_to_non_nullable
              as ClientAuditDetails,
      auditDetails: freezed == auditDetails
          ? _value.auditDetails
          : auditDetails // ignore: cast_nullable_to_non_nullable
              as AuditDetails?,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $NameCopyWith<$Res> get name {
    return $NameCopyWith<$Res>(_value.name, (value) {
      return _then(_value.copyWith(name: value) as $Val);
    });
  }

  @override
  @pragma('vm:prefer-inline')
  $UserDetailsCopyWith<$Res> get userDetails {
    return $UserDetailsCopyWith<$Res>(_value.userDetails, (value) {
      return _then(_value.copyWith(userDetails: value) as $Val);
    });
  }

  @override
  @pragma('vm:prefer-inline')
  $AdditionalFieldsCopyWith<$Res> get additionalFields {
    return $AdditionalFieldsCopyWith<$Res>(_value.additionalFields, (value) {
      return _then(_value.copyWith(additionalFields: value) as $Val);
    });
  }

  @override
  @pragma('vm:prefer-inline')
  $ClientAuditDetailsCopyWith<$Res> get clientAuditDetails {
    return $ClientAuditDetailsCopyWith<$Res>(_value.clientAuditDetails,
        (value) {
      return _then(_value.copyWith(clientAuditDetails: value) as $Val);
    });
  }

  @override
  @pragma('vm:prefer-inline')
  $AuditDetailsCopyWith<$Res>? get auditDetails {
    if (_value.auditDetails == null) {
      return null;
    }

    return $AuditDetailsCopyWith<$Res>(_value.auditDetails!, (value) {
      return _then(_value.copyWith(auditDetails: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$IndividualImplCopyWith<$Res>
    implements $IndividualCopyWith<$Res> {
  factory _$$IndividualImplCopyWith(
          _$IndividualImpl value, $Res Function(_$IndividualImpl) then) =
      __$$IndividualImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'tenantId') String tenantId,
      @JsonKey(name: 'individualId') String? individualId,
      @JsonKey(name: 'name') Name name,
      @JsonKey(name: 'userDetails') UserDetails userDetails,
      @JsonKey(name: 'userUuid') String userUuid,
      @JsonKey(name: 'userId') String userId,
      @JsonKey(name: 'mobileNumber') String mobileNumber,
      @JsonKey(name: 'address') List<Address> address,
      @JsonKey(name: 'identifiers') List<Identifier> identifiers,
      @JsonKey(name: 'isSystemUser') bool isSystemUser,
      @JsonKey(name: 'skills') List<Skill> skills,
      @JsonKey(name: 'additionalFields') AdditionalFields additionalFields,
      @JsonKey(name: 'clientAuditDetails')
      ClientAuditDetails clientAuditDetails,
      @JsonKey(name: 'auditDetails') AuditDetails? auditDetails});

  @override
  $NameCopyWith<$Res> get name;
  @override
  $UserDetailsCopyWith<$Res> get userDetails;
  @override
  $AdditionalFieldsCopyWith<$Res> get additionalFields;
  @override
  $ClientAuditDetailsCopyWith<$Res> get clientAuditDetails;
  @override
  $AuditDetailsCopyWith<$Res>? get auditDetails;
}

/// @nodoc
class __$$IndividualImplCopyWithImpl<$Res>
    extends _$IndividualCopyWithImpl<$Res, _$IndividualImpl>
    implements _$$IndividualImplCopyWith<$Res> {
  __$$IndividualImplCopyWithImpl(
      _$IndividualImpl _value, $Res Function(_$IndividualImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? tenantId = null,
    Object? individualId = freezed,
    Object? name = null,
    Object? userDetails = null,
    Object? userUuid = null,
    Object? userId = null,
    Object? mobileNumber = null,
    Object? address = null,
    Object? identifiers = null,
    Object? isSystemUser = null,
    Object? skills = null,
    Object? additionalFields = null,
    Object? clientAuditDetails = null,
    Object? auditDetails = freezed,
  }) {
    return _then(_$IndividualImpl(
      tenantId: null == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String,
      individualId: freezed == individualId
          ? _value.individualId
          : individualId // ignore: cast_nullable_to_non_nullable
              as String?,
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as Name,
      userDetails: null == userDetails
          ? _value.userDetails
          : userDetails // ignore: cast_nullable_to_non_nullable
              as UserDetails,
      userUuid: null == userUuid
          ? _value.userUuid
          : userUuid // ignore: cast_nullable_to_non_nullable
              as String,
      userId: null == userId
          ? _value.userId
          : userId // ignore: cast_nullable_to_non_nullable
              as String,
      mobileNumber: null == mobileNumber
          ? _value.mobileNumber
          : mobileNumber // ignore: cast_nullable_to_non_nullable
              as String,
      address: null == address
          ? _value._address
          : address // ignore: cast_nullable_to_non_nullable
              as List<Address>,
      identifiers: null == identifiers
          ? _value._identifiers
          : identifiers // ignore: cast_nullable_to_non_nullable
              as List<Identifier>,
      isSystemUser: null == isSystemUser
          ? _value.isSystemUser
          : isSystemUser // ignore: cast_nullable_to_non_nullable
              as bool,
      skills: null == skills
          ? _value._skills
          : skills // ignore: cast_nullable_to_non_nullable
              as List<Skill>,
      additionalFields: null == additionalFields
          ? _value.additionalFields
          : additionalFields // ignore: cast_nullable_to_non_nullable
              as AdditionalFields,
      clientAuditDetails: null == clientAuditDetails
          ? _value.clientAuditDetails
          : clientAuditDetails // ignore: cast_nullable_to_non_nullable
              as ClientAuditDetails,
      auditDetails: freezed == auditDetails
          ? _value.auditDetails
          : auditDetails // ignore: cast_nullable_to_non_nullable
              as AuditDetails?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$IndividualImpl implements _Individual {
  const _$IndividualImpl(
      {@JsonKey(name: 'tenantId') required this.tenantId,
      @JsonKey(name: 'individualId') this.individualId,
      @JsonKey(name: 'name') required this.name,
      @JsonKey(name: 'userDetails') required this.userDetails,
      @JsonKey(name: 'userUuid') required this.userUuid,
      @JsonKey(name: 'userId') required this.userId,
      @JsonKey(name: 'mobileNumber') required this.mobileNumber,
      @JsonKey(name: 'address') required final List<Address> address,
      @JsonKey(name: 'identifiers') required final List<Identifier> identifiers,
      @JsonKey(name: 'isSystemUser') this.isSystemUser = true,
      @JsonKey(name: 'skills') final List<Skill> skills = const [],
      @JsonKey(name: 'additionalFields') required this.additionalFields,
      @JsonKey(name: 'clientAuditDetails')
      this.clientAuditDetails = const ClientAuditDetails(),
      @JsonKey(name: 'auditDetails') this.auditDetails = const AuditDetails()})
      : _address = address,
        _identifiers = identifiers,
        _skills = skills;

  factory _$IndividualImpl.fromJson(Map<String, dynamic> json) =>
      _$$IndividualImplFromJson(json);

  @override
  @JsonKey(name: 'tenantId')
  final String tenantId;
  @override
  @JsonKey(name: 'individualId')
  final String? individualId;
  @override
  @JsonKey(name: 'name')
  final Name name;
  @override
  @JsonKey(name: 'userDetails')
  final UserDetails userDetails;
// Use "{{individualDateOfBirth}}" for replacement
  @override
  @JsonKey(name: 'userUuid')
  final String userUuid;
// Use "{{individualGenderType}}" for replacement
  @override
  @JsonKey(name: 'userId')
  final String userId;
  @override
  @JsonKey(name: 'mobileNumber')
  final String mobileNumber;
  final List<Address> _address;
  @override
  @JsonKey(name: 'address')
  List<Address> get address {
    if (_address is EqualUnmodifiableListView) return _address;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_address);
  }

  final List<Identifier> _identifiers;
  @override
  @JsonKey(name: 'identifiers')
  List<Identifier> get identifiers {
    if (_identifiers is EqualUnmodifiableListView) return _identifiers;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_identifiers);
  }

  @override
  @JsonKey(name: 'isSystemUser')
  final bool isSystemUser;
  final List<Skill> _skills;
  @override
  @JsonKey(name: 'skills')
  List<Skill> get skills {
    if (_skills is EqualUnmodifiableListView) return _skills;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_skills);
  }

  @override
  @JsonKey(name: 'additionalFields')
  final AdditionalFields additionalFields;
  @override
  @JsonKey(name: 'clientAuditDetails')
  final ClientAuditDetails clientAuditDetails;
  @override
  @JsonKey(name: 'auditDetails')
  final AuditDetails? auditDetails;

  @override
  String toString() {
    return 'Individual(tenantId: $tenantId, individualId: $individualId, name: $name, userDetails: $userDetails, userUuid: $userUuid, userId: $userId, mobileNumber: $mobileNumber, address: $address, identifiers: $identifiers, isSystemUser: $isSystemUser, skills: $skills, additionalFields: $additionalFields, clientAuditDetails: $clientAuditDetails, auditDetails: $auditDetails)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$IndividualImpl &&
            (identical(other.tenantId, tenantId) ||
                other.tenantId == tenantId) &&
            (identical(other.individualId, individualId) ||
                other.individualId == individualId) &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.userDetails, userDetails) ||
                other.userDetails == userDetails) &&
            (identical(other.userUuid, userUuid) ||
                other.userUuid == userUuid) &&
            (identical(other.userId, userId) || other.userId == userId) &&
            (identical(other.mobileNumber, mobileNumber) ||
                other.mobileNumber == mobileNumber) &&
            const DeepCollectionEquality().equals(other._address, _address) &&
            const DeepCollectionEquality()
                .equals(other._identifiers, _identifiers) &&
            (identical(other.isSystemUser, isSystemUser) ||
                other.isSystemUser == isSystemUser) &&
            const DeepCollectionEquality().equals(other._skills, _skills) &&
            (identical(other.additionalFields, additionalFields) ||
                other.additionalFields == additionalFields) &&
            (identical(other.clientAuditDetails, clientAuditDetails) ||
                other.clientAuditDetails == clientAuditDetails) &&
            (identical(other.auditDetails, auditDetails) ||
                other.auditDetails == auditDetails));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      tenantId,
      individualId,
      name,
      userDetails,
      userUuid,
      userId,
      mobileNumber,
      const DeepCollectionEquality().hash(_address),
      const DeepCollectionEquality().hash(_identifiers),
      isSystemUser,
      const DeepCollectionEquality().hash(_skills),
      additionalFields,
      clientAuditDetails,
      auditDetails);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$IndividualImplCopyWith<_$IndividualImpl> get copyWith =>
      __$$IndividualImplCopyWithImpl<_$IndividualImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$IndividualImplToJson(
      this,
    );
  }
}

abstract class _Individual implements Individual {
  const factory _Individual(
      {@JsonKey(name: 'tenantId') required final String tenantId,
      @JsonKey(name: 'individualId') final String? individualId,
      @JsonKey(name: 'name') required final Name name,
      @JsonKey(name: 'userDetails') required final UserDetails userDetails,
      @JsonKey(name: 'userUuid') required final String userUuid,
      @JsonKey(name: 'userId') required final String userId,
      @JsonKey(name: 'mobileNumber') required final String mobileNumber,
      @JsonKey(name: 'address') required final List<Address> address,
      @JsonKey(name: 'identifiers') required final List<Identifier> identifiers,
      @JsonKey(name: 'isSystemUser') final bool isSystemUser,
      @JsonKey(name: 'skills') final List<Skill> skills,
      @JsonKey(name: 'additionalFields')
      required final AdditionalFields additionalFields,
      @JsonKey(name: 'clientAuditDetails')
      final ClientAuditDetails clientAuditDetails,
      @JsonKey(name: 'auditDetails')
      final AuditDetails? auditDetails}) = _$IndividualImpl;

  factory _Individual.fromJson(Map<String, dynamic> json) =
      _$IndividualImpl.fromJson;

  @override
  @JsonKey(name: 'tenantId')
  String get tenantId;
  @override
  @JsonKey(name: 'individualId')
  String? get individualId;
  @override
  @JsonKey(name: 'name')
  Name get name;
  @override
  @JsonKey(name: 'userDetails')
  UserDetails get userDetails;
  @override // Use "{{individualDateOfBirth}}" for replacement
  @JsonKey(name: 'userUuid')
  String get userUuid;
  @override // Use "{{individualGenderType}}" for replacement
  @JsonKey(name: 'userId')
  String get userId;
  @override
  @JsonKey(name: 'mobileNumber')
  String get mobileNumber;
  @override
  @JsonKey(name: 'address')
  List<Address> get address;
  @override
  @JsonKey(name: 'identifiers')
  List<Identifier> get identifiers;
  @override
  @JsonKey(name: 'isSystemUser')
  bool get isSystemUser;
  @override
  @JsonKey(name: 'skills')
  List<Skill> get skills;
  @override
  @JsonKey(name: 'additionalFields')
  AdditionalFields get additionalFields;
  @override
  @JsonKey(name: 'clientAuditDetails')
  ClientAuditDetails get clientAuditDetails;
  @override
  @JsonKey(name: 'auditDetails')
  AuditDetails? get auditDetails;
  @override
  @JsonKey(ignore: true)
  _$$IndividualImplCopyWith<_$IndividualImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

ClientAuditDetails _$ClientAuditDetailsFromJson(Map<String, dynamic> json) {
  return _ClientAuditDetails.fromJson(json);
}

/// @nodoc
mixin _$ClientAuditDetails {
  @JsonKey(name: 'createdBy')
  String? get type =>
      throw _privateConstructorUsedError; // Use "{{individualSkillType}}" for replacement
  @JsonKey(name: 'lastModifiedBy')
  String? get level =>
      throw _privateConstructorUsedError; // Use "{{individualSkillLevel}}" for replacement
  @JsonKey(name: 'createdTime')
  int? get createdTime =>
      throw _privateConstructorUsedError; // Use "{{individualSkillExperience}}" for replacement
  @JsonKey(name: 'lastModifiedTime')
  int? get lastModifiedTime => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $ClientAuditDetailsCopyWith<ClientAuditDetails> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $ClientAuditDetailsCopyWith<$Res> {
  factory $ClientAuditDetailsCopyWith(
          ClientAuditDetails value, $Res Function(ClientAuditDetails) then) =
      _$ClientAuditDetailsCopyWithImpl<$Res, ClientAuditDetails>;
  @useResult
  $Res call(
      {@JsonKey(name: 'createdBy') String? type,
      @JsonKey(name: 'lastModifiedBy') String? level,
      @JsonKey(name: 'createdTime') int? createdTime,
      @JsonKey(name: 'lastModifiedTime') int? lastModifiedTime});
}

/// @nodoc
class _$ClientAuditDetailsCopyWithImpl<$Res, $Val extends ClientAuditDetails>
    implements $ClientAuditDetailsCopyWith<$Res> {
  _$ClientAuditDetailsCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = freezed,
    Object? level = freezed,
    Object? createdTime = freezed,
    Object? lastModifiedTime = freezed,
  }) {
    return _then(_value.copyWith(
      type: freezed == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String?,
      level: freezed == level
          ? _value.level
          : level // ignore: cast_nullable_to_non_nullable
              as String?,
      createdTime: freezed == createdTime
          ? _value.createdTime
          : createdTime // ignore: cast_nullable_to_non_nullable
              as int?,
      lastModifiedTime: freezed == lastModifiedTime
          ? _value.lastModifiedTime
          : lastModifiedTime // ignore: cast_nullable_to_non_nullable
              as int?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$ClientAuditDetailsImplCopyWith<$Res>
    implements $ClientAuditDetailsCopyWith<$Res> {
  factory _$$ClientAuditDetailsImplCopyWith(_$ClientAuditDetailsImpl value,
          $Res Function(_$ClientAuditDetailsImpl) then) =
      __$$ClientAuditDetailsImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'createdBy') String? type,
      @JsonKey(name: 'lastModifiedBy') String? level,
      @JsonKey(name: 'createdTime') int? createdTime,
      @JsonKey(name: 'lastModifiedTime') int? lastModifiedTime});
}

/// @nodoc
class __$$ClientAuditDetailsImplCopyWithImpl<$Res>
    extends _$ClientAuditDetailsCopyWithImpl<$Res, _$ClientAuditDetailsImpl>
    implements _$$ClientAuditDetailsImplCopyWith<$Res> {
  __$$ClientAuditDetailsImplCopyWithImpl(_$ClientAuditDetailsImpl _value,
      $Res Function(_$ClientAuditDetailsImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = freezed,
    Object? level = freezed,
    Object? createdTime = freezed,
    Object? lastModifiedTime = freezed,
  }) {
    return _then(_$ClientAuditDetailsImpl(
      type: freezed == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String?,
      level: freezed == level
          ? _value.level
          : level // ignore: cast_nullable_to_non_nullable
              as String?,
      createdTime: freezed == createdTime
          ? _value.createdTime
          : createdTime // ignore: cast_nullable_to_non_nullable
              as int?,
      lastModifiedTime: freezed == lastModifiedTime
          ? _value.lastModifiedTime
          : lastModifiedTime // ignore: cast_nullable_to_non_nullable
              as int?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$ClientAuditDetailsImpl implements _ClientAuditDetails {
  const _$ClientAuditDetailsImpl(
      {@JsonKey(name: 'createdBy') this.type,
      @JsonKey(name: 'lastModifiedBy') this.level,
      @JsonKey(name: 'createdTime') this.createdTime,
      @JsonKey(name: 'lastModifiedTime') this.lastModifiedTime});

  factory _$ClientAuditDetailsImpl.fromJson(Map<String, dynamic> json) =>
      _$$ClientAuditDetailsImplFromJson(json);

  @override
  @JsonKey(name: 'createdBy')
  final String? type;
// Use "{{individualSkillType}}" for replacement
  @override
  @JsonKey(name: 'lastModifiedBy')
  final String? level;
// Use "{{individualSkillLevel}}" for replacement
  @override
  @JsonKey(name: 'createdTime')
  final int? createdTime;
// Use "{{individualSkillExperience}}" for replacement
  @override
  @JsonKey(name: 'lastModifiedTime')
  final int? lastModifiedTime;

  @override
  String toString() {
    return 'ClientAuditDetails(type: $type, level: $level, createdTime: $createdTime, lastModifiedTime: $lastModifiedTime)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ClientAuditDetailsImpl &&
            (identical(other.type, type) || other.type == type) &&
            (identical(other.level, level) || other.level == level) &&
            (identical(other.createdTime, createdTime) ||
                other.createdTime == createdTime) &&
            (identical(other.lastModifiedTime, lastModifiedTime) ||
                other.lastModifiedTime == lastModifiedTime));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, type, level, createdTime, lastModifiedTime);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$ClientAuditDetailsImplCopyWith<_$ClientAuditDetailsImpl> get copyWith =>
      __$$ClientAuditDetailsImplCopyWithImpl<_$ClientAuditDetailsImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$ClientAuditDetailsImplToJson(
      this,
    );
  }
}

abstract class _ClientAuditDetails implements ClientAuditDetails {
  const factory _ClientAuditDetails(
          {@JsonKey(name: 'createdBy') final String? type,
          @JsonKey(name: 'lastModifiedBy') final String? level,
          @JsonKey(name: 'createdTime') final int? createdTime,
          @JsonKey(name: 'lastModifiedTime') final int? lastModifiedTime}) =
      _$ClientAuditDetailsImpl;

  factory _ClientAuditDetails.fromJson(Map<String, dynamic> json) =
      _$ClientAuditDetailsImpl.fromJson;

  @override
  @JsonKey(name: 'createdBy')
  String? get type;
  @override // Use "{{individualSkillType}}" for replacement
  @JsonKey(name: 'lastModifiedBy')
  String? get level;
  @override // Use "{{individualSkillLevel}}" for replacement
  @JsonKey(name: 'createdTime')
  int? get createdTime;
  @override // Use "{{individualSkillExperience}}" for replacement
  @JsonKey(name: 'lastModifiedTime')
  int? get lastModifiedTime;
  @override
  @JsonKey(ignore: true)
  _$$ClientAuditDetailsImplCopyWith<_$ClientAuditDetailsImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

AuditDetails _$AuditDetailsFromJson(Map<String, dynamic> json) {
  return _AuditDetails.fromJson(json);
}

/// @nodoc
mixin _$AuditDetails {
  @JsonKey(name: 'createdBy')
  String? get type =>
      throw _privateConstructorUsedError; // Use "{{individualSkillType}}" for replacement
  @JsonKey(name: 'lastModifiedBy')
  String? get level =>
      throw _privateConstructorUsedError; // Use "{{individualSkillLevel}}" for replacement
  @JsonKey(name: 'createdTime')
  int? get experience =>
      throw _privateConstructorUsedError; // Use "{{individualSkillExperience}}" for replacement
  @JsonKey(name: 'lastModifiedTime')
  int? get clientReferenceId => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AuditDetailsCopyWith<AuditDetails> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AuditDetailsCopyWith<$Res> {
  factory $AuditDetailsCopyWith(
          AuditDetails value, $Res Function(AuditDetails) then) =
      _$AuditDetailsCopyWithImpl<$Res, AuditDetails>;
  @useResult
  $Res call(
      {@JsonKey(name: 'createdBy') String? type,
      @JsonKey(name: 'lastModifiedBy') String? level,
      @JsonKey(name: 'createdTime') int? experience,
      @JsonKey(name: 'lastModifiedTime') int? clientReferenceId});
}

/// @nodoc
class _$AuditDetailsCopyWithImpl<$Res, $Val extends AuditDetails>
    implements $AuditDetailsCopyWith<$Res> {
  _$AuditDetailsCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = freezed,
    Object? level = freezed,
    Object? experience = freezed,
    Object? clientReferenceId = freezed,
  }) {
    return _then(_value.copyWith(
      type: freezed == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String?,
      level: freezed == level
          ? _value.level
          : level // ignore: cast_nullable_to_non_nullable
              as String?,
      experience: freezed == experience
          ? _value.experience
          : experience // ignore: cast_nullable_to_non_nullable
              as int?,
      clientReferenceId: freezed == clientReferenceId
          ? _value.clientReferenceId
          : clientReferenceId // ignore: cast_nullable_to_non_nullable
              as int?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$AuditDetailsImplCopyWith<$Res>
    implements $AuditDetailsCopyWith<$Res> {
  factory _$$AuditDetailsImplCopyWith(
          _$AuditDetailsImpl value, $Res Function(_$AuditDetailsImpl) then) =
      __$$AuditDetailsImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'createdBy') String? type,
      @JsonKey(name: 'lastModifiedBy') String? level,
      @JsonKey(name: 'createdTime') int? experience,
      @JsonKey(name: 'lastModifiedTime') int? clientReferenceId});
}

/// @nodoc
class __$$AuditDetailsImplCopyWithImpl<$Res>
    extends _$AuditDetailsCopyWithImpl<$Res, _$AuditDetailsImpl>
    implements _$$AuditDetailsImplCopyWith<$Res> {
  __$$AuditDetailsImplCopyWithImpl(
      _$AuditDetailsImpl _value, $Res Function(_$AuditDetailsImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = freezed,
    Object? level = freezed,
    Object? experience = freezed,
    Object? clientReferenceId = freezed,
  }) {
    return _then(_$AuditDetailsImpl(
      type: freezed == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String?,
      level: freezed == level
          ? _value.level
          : level // ignore: cast_nullable_to_non_nullable
              as String?,
      experience: freezed == experience
          ? _value.experience
          : experience // ignore: cast_nullable_to_non_nullable
              as int?,
      clientReferenceId: freezed == clientReferenceId
          ? _value.clientReferenceId
          : clientReferenceId // ignore: cast_nullable_to_non_nullable
              as int?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AuditDetailsImpl implements _AuditDetails {
  const _$AuditDetailsImpl(
      {@JsonKey(name: 'createdBy') this.type,
      @JsonKey(name: 'lastModifiedBy') this.level,
      @JsonKey(name: 'createdTime') this.experience,
      @JsonKey(name: 'lastModifiedTime') this.clientReferenceId});

  factory _$AuditDetailsImpl.fromJson(Map<String, dynamic> json) =>
      _$$AuditDetailsImplFromJson(json);

  @override
  @JsonKey(name: 'createdBy')
  final String? type;
// Use "{{individualSkillType}}" for replacement
  @override
  @JsonKey(name: 'lastModifiedBy')
  final String? level;
// Use "{{individualSkillLevel}}" for replacement
  @override
  @JsonKey(name: 'createdTime')
  final int? experience;
// Use "{{individualSkillExperience}}" for replacement
  @override
  @JsonKey(name: 'lastModifiedTime')
  final int? clientReferenceId;

  @override
  String toString() {
    return 'AuditDetails(type: $type, level: $level, experience: $experience, clientReferenceId: $clientReferenceId)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AuditDetailsImpl &&
            (identical(other.type, type) || other.type == type) &&
            (identical(other.level, level) || other.level == level) &&
            (identical(other.experience, experience) ||
                other.experience == experience) &&
            (identical(other.clientReferenceId, clientReferenceId) ||
                other.clientReferenceId == clientReferenceId));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, type, level, experience, clientReferenceId);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AuditDetailsImplCopyWith<_$AuditDetailsImpl> get copyWith =>
      __$$AuditDetailsImplCopyWithImpl<_$AuditDetailsImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AuditDetailsImplToJson(
      this,
    );
  }
}

abstract class _AuditDetails implements AuditDetails {
  const factory _AuditDetails(
          {@JsonKey(name: 'createdBy') final String? type,
          @JsonKey(name: 'lastModifiedBy') final String? level,
          @JsonKey(name: 'createdTime') final int? experience,
          @JsonKey(name: 'lastModifiedTime') final int? clientReferenceId}) =
      _$AuditDetailsImpl;

  factory _AuditDetails.fromJson(Map<String, dynamic> json) =
      _$AuditDetailsImpl.fromJson;

  @override
  @JsonKey(name: 'createdBy')
  String? get type;
  @override // Use "{{individualSkillType}}" for replacement
  @JsonKey(name: 'lastModifiedBy')
  String? get level;
  @override // Use "{{individualSkillLevel}}" for replacement
  @JsonKey(name: 'createdTime')
  int? get experience;
  @override // Use "{{individualSkillExperience}}" for replacement
  @JsonKey(name: 'lastModifiedTime')
  int? get clientReferenceId;
  @override
  @JsonKey(ignore: true)
  _$$AuditDetailsImplCopyWith<_$AuditDetailsImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

Skill _$SkillFromJson(Map<String, dynamic> json) {
  return _Skill.fromJson(json);
}

/// @nodoc
mixin _$Skill {
  @JsonKey(name: 'type')
  String get type =>
      throw _privateConstructorUsedError; // Use "{{individualSkillType}}" for replacement
  @JsonKey(name: 'level')
  String get level =>
      throw _privateConstructorUsedError; // Use "{{individualSkillLevel}}" for replacement
  @JsonKey(name: 'experience')
  String get experience =>
      throw _privateConstructorUsedError; // Use "{{individualSkillExperience}}" for replacement
  @JsonKey(name: 'clientReferenceId')
  String get clientReferenceId => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $SkillCopyWith<Skill> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $SkillCopyWith<$Res> {
  factory $SkillCopyWith(Skill value, $Res Function(Skill) then) =
      _$SkillCopyWithImpl<$Res, Skill>;
  @useResult
  $Res call(
      {@JsonKey(name: 'type') String type,
      @JsonKey(name: 'level') String level,
      @JsonKey(name: 'experience') String experience,
      @JsonKey(name: 'clientReferenceId') String clientReferenceId});
}

/// @nodoc
class _$SkillCopyWithImpl<$Res, $Val extends Skill>
    implements $SkillCopyWith<$Res> {
  _$SkillCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = null,
    Object? level = null,
    Object? experience = null,
    Object? clientReferenceId = null,
  }) {
    return _then(_value.copyWith(
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String,
      level: null == level
          ? _value.level
          : level // ignore: cast_nullable_to_non_nullable
              as String,
      experience: null == experience
          ? _value.experience
          : experience // ignore: cast_nullable_to_non_nullable
              as String,
      clientReferenceId: null == clientReferenceId
          ? _value.clientReferenceId
          : clientReferenceId // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$SkillImplCopyWith<$Res> implements $SkillCopyWith<$Res> {
  factory _$$SkillImplCopyWith(
          _$SkillImpl value, $Res Function(_$SkillImpl) then) =
      __$$SkillImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'type') String type,
      @JsonKey(name: 'level') String level,
      @JsonKey(name: 'experience') String experience,
      @JsonKey(name: 'clientReferenceId') String clientReferenceId});
}

/// @nodoc
class __$$SkillImplCopyWithImpl<$Res>
    extends _$SkillCopyWithImpl<$Res, _$SkillImpl>
    implements _$$SkillImplCopyWith<$Res> {
  __$$SkillImplCopyWithImpl(
      _$SkillImpl _value, $Res Function(_$SkillImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? type = null,
    Object? level = null,
    Object? experience = null,
    Object? clientReferenceId = null,
  }) {
    return _then(_$SkillImpl(
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String,
      level: null == level
          ? _value.level
          : level // ignore: cast_nullable_to_non_nullable
              as String,
      experience: null == experience
          ? _value.experience
          : experience // ignore: cast_nullable_to_non_nullable
              as String,
      clientReferenceId: null == clientReferenceId
          ? _value.clientReferenceId
          : clientReferenceId // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$SkillImpl implements _Skill {
  const _$SkillImpl(
      {@JsonKey(name: 'type') this.type = "",
      @JsonKey(name: 'level') this.level = "",
      @JsonKey(name: 'experience') this.experience = "",
      @JsonKey(name: 'clientReferenceId') this.clientReferenceId = ""});

  factory _$SkillImpl.fromJson(Map<String, dynamic> json) =>
      _$$SkillImplFromJson(json);

  @override
  @JsonKey(name: 'type')
  final String type;
// Use "{{individualSkillType}}" for replacement
  @override
  @JsonKey(name: 'level')
  final String level;
// Use "{{individualSkillLevel}}" for replacement
  @override
  @JsonKey(name: 'experience')
  final String experience;
// Use "{{individualSkillExperience}}" for replacement
  @override
  @JsonKey(name: 'clientReferenceId')
  final String clientReferenceId;

  @override
  String toString() {
    return 'Skill(type: $type, level: $level, experience: $experience, clientReferenceId: $clientReferenceId)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$SkillImpl &&
            (identical(other.type, type) || other.type == type) &&
            (identical(other.level, level) || other.level == level) &&
            (identical(other.experience, experience) ||
                other.experience == experience) &&
            (identical(other.clientReferenceId, clientReferenceId) ||
                other.clientReferenceId == clientReferenceId));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, type, level, experience, clientReferenceId);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$SkillImplCopyWith<_$SkillImpl> get copyWith =>
      __$$SkillImplCopyWithImpl<_$SkillImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$SkillImplToJson(
      this,
    );
  }
}

abstract class _Skill implements Skill {
  const factory _Skill(
          {@JsonKey(name: 'type') final String type,
          @JsonKey(name: 'level') final String level,
          @JsonKey(name: 'experience') final String experience,
          @JsonKey(name: 'clientReferenceId') final String clientReferenceId}) =
      _$SkillImpl;

  factory _Skill.fromJson(Map<String, dynamic> json) = _$SkillImpl.fromJson;

  @override
  @JsonKey(name: 'type')
  String get type;
  @override // Use "{{individualSkillType}}" for replacement
  @JsonKey(name: 'level')
  String get level;
  @override // Use "{{individualSkillLevel}}" for replacement
  @JsonKey(name: 'experience')
  String get experience;
  @override // Use "{{individualSkillExperience}}" for replacement
  @JsonKey(name: 'clientReferenceId')
  String get clientReferenceId;
  @override
  @JsonKey(ignore: true)
  _$$SkillImplCopyWith<_$SkillImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

LitigantNetworkModel _$LitigantNetworkModelFromJson(Map<String, dynamic> json) {
  return _LitigantNetworkModel.fromJson(json);
}

/// @nodoc
mixin _$LitigantNetworkModel {
  @JsonKey(name: 'RequestInfo')
  RequestInfo get requestInfo => throw _privateConstructorUsedError;
  @JsonKey(name: 'Individual')
  Individual get individual => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $LitigantNetworkModelCopyWith<LitigantNetworkModel> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $LitigantNetworkModelCopyWith<$Res> {
  factory $LitigantNetworkModelCopyWith(LitigantNetworkModel value,
          $Res Function(LitigantNetworkModel) then) =
      _$LitigantNetworkModelCopyWithImpl<$Res, LitigantNetworkModel>;
  @useResult
  $Res call(
      {@JsonKey(name: 'RequestInfo') RequestInfo requestInfo,
      @JsonKey(name: 'Individual') Individual individual});

  $RequestInfoCopyWith<$Res> get requestInfo;
  $IndividualCopyWith<$Res> get individual;
}

/// @nodoc
class _$LitigantNetworkModelCopyWithImpl<$Res,
        $Val extends LitigantNetworkModel>
    implements $LitigantNetworkModelCopyWith<$Res> {
  _$LitigantNetworkModelCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? requestInfo = null,
    Object? individual = null,
  }) {
    return _then(_value.copyWith(
      requestInfo: null == requestInfo
          ? _value.requestInfo
          : requestInfo // ignore: cast_nullable_to_non_nullable
              as RequestInfo,
      individual: null == individual
          ? _value.individual
          : individual // ignore: cast_nullable_to_non_nullable
              as Individual,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $RequestInfoCopyWith<$Res> get requestInfo {
    return $RequestInfoCopyWith<$Res>(_value.requestInfo, (value) {
      return _then(_value.copyWith(requestInfo: value) as $Val);
    });
  }

  @override
  @pragma('vm:prefer-inline')
  $IndividualCopyWith<$Res> get individual {
    return $IndividualCopyWith<$Res>(_value.individual, (value) {
      return _then(_value.copyWith(individual: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$LitigantNetworkModelImplCopyWith<$Res>
    implements $LitigantNetworkModelCopyWith<$Res> {
  factory _$$LitigantNetworkModelImplCopyWith(_$LitigantNetworkModelImpl value,
          $Res Function(_$LitigantNetworkModelImpl) then) =
      __$$LitigantNetworkModelImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'RequestInfo') RequestInfo requestInfo,
      @JsonKey(name: 'Individual') Individual individual});

  @override
  $RequestInfoCopyWith<$Res> get requestInfo;
  @override
  $IndividualCopyWith<$Res> get individual;
}

/// @nodoc
class __$$LitigantNetworkModelImplCopyWithImpl<$Res>
    extends _$LitigantNetworkModelCopyWithImpl<$Res, _$LitigantNetworkModelImpl>
    implements _$$LitigantNetworkModelImplCopyWith<$Res> {
  __$$LitigantNetworkModelImplCopyWithImpl(_$LitigantNetworkModelImpl _value,
      $Res Function(_$LitigantNetworkModelImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? requestInfo = null,
    Object? individual = null,
  }) {
    return _then(_$LitigantNetworkModelImpl(
      requestInfo: null == requestInfo
          ? _value.requestInfo
          : requestInfo // ignore: cast_nullable_to_non_nullable
              as RequestInfo,
      individual: null == individual
          ? _value.individual
          : individual // ignore: cast_nullable_to_non_nullable
              as Individual,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$LitigantNetworkModelImpl implements _LitigantNetworkModel {
  const _$LitigantNetworkModelImpl(
      {@JsonKey(name: 'RequestInfo') required this.requestInfo,
      @JsonKey(name: 'Individual') required this.individual});

  factory _$LitigantNetworkModelImpl.fromJson(Map<String, dynamic> json) =>
      _$$LitigantNetworkModelImplFromJson(json);

  @override
  @JsonKey(name: 'RequestInfo')
  final RequestInfo requestInfo;
  @override
  @JsonKey(name: 'Individual')
  final Individual individual;

  @override
  String toString() {
    return 'LitigantNetworkModel(requestInfo: $requestInfo, individual: $individual)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$LitigantNetworkModelImpl &&
            (identical(other.requestInfo, requestInfo) ||
                other.requestInfo == requestInfo) &&
            (identical(other.individual, individual) ||
                other.individual == individual));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, requestInfo, individual);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$LitigantNetworkModelImplCopyWith<_$LitigantNetworkModelImpl>
      get copyWith =>
          __$$LitigantNetworkModelImplCopyWithImpl<_$LitigantNetworkModelImpl>(
              this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$LitigantNetworkModelImplToJson(
      this,
    );
  }
}

abstract class _LitigantNetworkModel implements LitigantNetworkModel {
  const factory _LitigantNetworkModel(
          {@JsonKey(name: 'RequestInfo') required final RequestInfo requestInfo,
          @JsonKey(name: 'Individual') required final Individual individual}) =
      _$LitigantNetworkModelImpl;

  factory _LitigantNetworkModel.fromJson(Map<String, dynamic> json) =
      _$LitigantNetworkModelImpl.fromJson;

  @override
  @JsonKey(name: 'RequestInfo')
  RequestInfo get requestInfo;
  @override
  @JsonKey(name: 'Individual')
  Individual get individual;
  @override
  @JsonKey(ignore: true)
  _$$LitigantNetworkModelImplCopyWith<_$LitigantNetworkModelImpl>
      get copyWith => throw _privateConstructorUsedError;
}

IndividualInfo _$IndividualInfoFromJson(Map<String, dynamic> json) {
  return _IndividualInfo.fromJson(json);
}

/// @nodoc
mixin _$IndividualInfo {
  @JsonKey(name: 'individualId')
  String get individualId => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $IndividualInfoCopyWith<IndividualInfo> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $IndividualInfoCopyWith<$Res> {
  factory $IndividualInfoCopyWith(
          IndividualInfo value, $Res Function(IndividualInfo) then) =
      _$IndividualInfoCopyWithImpl<$Res, IndividualInfo>;
  @useResult
  $Res call({@JsonKey(name: 'individualId') String individualId});
}

/// @nodoc
class _$IndividualInfoCopyWithImpl<$Res, $Val extends IndividualInfo>
    implements $IndividualInfoCopyWith<$Res> {
  _$IndividualInfoCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? individualId = null,
  }) {
    return _then(_value.copyWith(
      individualId: null == individualId
          ? _value.individualId
          : individualId // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$IndividualInfoImplCopyWith<$Res>
    implements $IndividualInfoCopyWith<$Res> {
  factory _$$IndividualInfoImplCopyWith(_$IndividualInfoImpl value,
          $Res Function(_$IndividualInfoImpl) then) =
      __$$IndividualInfoImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({@JsonKey(name: 'individualId') String individualId});
}

/// @nodoc
class __$$IndividualInfoImplCopyWithImpl<$Res>
    extends _$IndividualInfoCopyWithImpl<$Res, _$IndividualInfoImpl>
    implements _$$IndividualInfoImplCopyWith<$Res> {
  __$$IndividualInfoImplCopyWithImpl(
      _$IndividualInfoImpl _value, $Res Function(_$IndividualInfoImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? individualId = null,
  }) {
    return _then(_$IndividualInfoImpl(
      individualId: null == individualId
          ? _value.individualId
          : individualId // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$IndividualInfoImpl implements _IndividualInfo {
  const _$IndividualInfoImpl(
      {@JsonKey(name: 'individualId') required this.individualId});

  factory _$IndividualInfoImpl.fromJson(Map<String, dynamic> json) =>
      _$$IndividualInfoImplFromJson(json);

  @override
  @JsonKey(name: 'individualId')
  final String individualId;

  @override
  String toString() {
    return 'IndividualInfo(individualId: $individualId)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$IndividualInfoImpl &&
            (identical(other.individualId, individualId) ||
                other.individualId == individualId));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, individualId);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$IndividualInfoImplCopyWith<_$IndividualInfoImpl> get copyWith =>
      __$$IndividualInfoImplCopyWithImpl<_$IndividualInfoImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$IndividualInfoImplToJson(
      this,
    );
  }
}

abstract class _IndividualInfo implements IndividualInfo {
  const factory _IndividualInfo(
          {@JsonKey(name: 'individualId') required final String individualId}) =
      _$IndividualInfoImpl;

  factory _IndividualInfo.fromJson(Map<String, dynamic> json) =
      _$IndividualInfoImpl.fromJson;

  @override
  @JsonKey(name: 'individualId')
  String get individualId;
  @override
  @JsonKey(ignore: true)
  _$$IndividualInfoImplCopyWith<_$IndividualInfoImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

LitigantResponseModel _$LitigantResponseModelFromJson(
    Map<String, dynamic> json) {
  return _LitigantResponseModel.fromJson(json);
}

/// @nodoc
mixin _$LitigantResponseModel {
  @JsonKey(name: 'ResponseInfo')
  ResponseInfoSearch get responseInfo => throw _privateConstructorUsedError;
  @JsonKey(name: 'Individual')
  IndividualInfo get individualInfo => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $LitigantResponseModelCopyWith<LitigantResponseModel> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $LitigantResponseModelCopyWith<$Res> {
  factory $LitigantResponseModelCopyWith(LitigantResponseModel value,
          $Res Function(LitigantResponseModel) then) =
      _$LitigantResponseModelCopyWithImpl<$Res, LitigantResponseModel>;
  @useResult
  $Res call(
      {@JsonKey(name: 'ResponseInfo') ResponseInfoSearch responseInfo,
      @JsonKey(name: 'Individual') IndividualInfo individualInfo});

  $ResponseInfoSearchCopyWith<$Res> get responseInfo;
  $IndividualInfoCopyWith<$Res> get individualInfo;
}

/// @nodoc
class _$LitigantResponseModelCopyWithImpl<$Res,
        $Val extends LitigantResponseModel>
    implements $LitigantResponseModelCopyWith<$Res> {
  _$LitigantResponseModelCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? responseInfo = null,
    Object? individualInfo = null,
  }) {
    return _then(_value.copyWith(
      responseInfo: null == responseInfo
          ? _value.responseInfo
          : responseInfo // ignore: cast_nullable_to_non_nullable
              as ResponseInfoSearch,
      individualInfo: null == individualInfo
          ? _value.individualInfo
          : individualInfo // ignore: cast_nullable_to_non_nullable
              as IndividualInfo,
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
  $IndividualInfoCopyWith<$Res> get individualInfo {
    return $IndividualInfoCopyWith<$Res>(_value.individualInfo, (value) {
      return _then(_value.copyWith(individualInfo: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$LitigantResponseModelImplCopyWith<$Res>
    implements $LitigantResponseModelCopyWith<$Res> {
  factory _$$LitigantResponseModelImplCopyWith(
          _$LitigantResponseModelImpl value,
          $Res Function(_$LitigantResponseModelImpl) then) =
      __$$LitigantResponseModelImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'ResponseInfo') ResponseInfoSearch responseInfo,
      @JsonKey(name: 'Individual') IndividualInfo individualInfo});

  @override
  $ResponseInfoSearchCopyWith<$Res> get responseInfo;
  @override
  $IndividualInfoCopyWith<$Res> get individualInfo;
}

/// @nodoc
class __$$LitigantResponseModelImplCopyWithImpl<$Res>
    extends _$LitigantResponseModelCopyWithImpl<$Res,
        _$LitigantResponseModelImpl>
    implements _$$LitigantResponseModelImplCopyWith<$Res> {
  __$$LitigantResponseModelImplCopyWithImpl(_$LitigantResponseModelImpl _value,
      $Res Function(_$LitigantResponseModelImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? responseInfo = null,
    Object? individualInfo = null,
  }) {
    return _then(_$LitigantResponseModelImpl(
      responseInfo: null == responseInfo
          ? _value.responseInfo
          : responseInfo // ignore: cast_nullable_to_non_nullable
              as ResponseInfoSearch,
      individualInfo: null == individualInfo
          ? _value.individualInfo
          : individualInfo // ignore: cast_nullable_to_non_nullable
              as IndividualInfo,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$LitigantResponseModelImpl implements _LitigantResponseModel {
  const _$LitigantResponseModelImpl(
      {@JsonKey(name: 'ResponseInfo') required this.responseInfo,
      @JsonKey(name: 'Individual') required this.individualInfo});

  factory _$LitigantResponseModelImpl.fromJson(Map<String, dynamic> json) =>
      _$$LitigantResponseModelImplFromJson(json);

  @override
  @JsonKey(name: 'ResponseInfo')
  final ResponseInfoSearch responseInfo;
  @override
  @JsonKey(name: 'Individual')
  final IndividualInfo individualInfo;

  @override
  String toString() {
    return 'LitigantResponseModel(responseInfo: $responseInfo, individualInfo: $individualInfo)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$LitigantResponseModelImpl &&
            (identical(other.responseInfo, responseInfo) ||
                other.responseInfo == responseInfo) &&
            (identical(other.individualInfo, individualInfo) ||
                other.individualInfo == individualInfo));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, responseInfo, individualInfo);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$LitigantResponseModelImplCopyWith<_$LitigantResponseModelImpl>
      get copyWith => __$$LitigantResponseModelImplCopyWithImpl<
          _$LitigantResponseModelImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$LitigantResponseModelImplToJson(
      this,
    );
  }
}

abstract class _LitigantResponseModel implements LitigantResponseModel {
  const factory _LitigantResponseModel(
          {@JsonKey(name: 'ResponseInfo')
          required final ResponseInfoSearch responseInfo,
          @JsonKey(name: 'Individual')
          required final IndividualInfo individualInfo}) =
      _$LitigantResponseModelImpl;

  factory _LitigantResponseModel.fromJson(Map<String, dynamic> json) =
      _$LitigantResponseModelImpl.fromJson;

  @override
  @JsonKey(name: 'ResponseInfo')
  ResponseInfoSearch get responseInfo;
  @override
  @JsonKey(name: 'Individual')
  IndividualInfo get individualInfo;
  @override
  @JsonKey(ignore: true)
  _$$LitigantResponseModelImplCopyWith<_$LitigantResponseModelImpl>
      get copyWith => throw _privateConstructorUsedError;
}
