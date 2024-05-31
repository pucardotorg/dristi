// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'advocate_registration_model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

ResponseList _$ResponseListFromJson(Map<String, dynamic> json) {
  return _ResponseList.fromJson(json);
}

/// @nodoc
mixin _$ResponseList {
  @JsonKey(name: "id")
  String? get id => throw _privateConstructorUsedError;
  @JsonKey(name: 'tenantId')
  String? get tenantId => throw _privateConstructorUsedError;
  @JsonKey(name: 'applicationNumber')
  String? get applicationNumber => throw _privateConstructorUsedError;
  @JsonKey(name: 'status')
  String? get status => throw _privateConstructorUsedError;
  @JsonKey(name: 'barRegistrationNumber')
  String? get barRegistrationNumber => throw _privateConstructorUsedError;
  @JsonKey(name: 'advocateType')
  String? get advocateType => throw _privateConstructorUsedError;
  @JsonKey(name: 'organisationID')
  String? get organisationID => throw _privateConstructorUsedError;
  @JsonKey(name: 'individualId')
  String? get individualId => throw _privateConstructorUsedError;
  @JsonKey(name: 'isActive')
  bool? get isActive => throw _privateConstructorUsedError;
  @JsonKey(name: 'workflow')
  Workflow? get workflow => throw _privateConstructorUsedError;
  @JsonKey(name: 'documents')
  List<Document>? get documents => throw _privateConstructorUsedError;
  @JsonKey(name: 'additionalDetails')
  Map<String, dynamic>? get additionalDetails =>
      throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $ResponseListCopyWith<ResponseList> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $ResponseListCopyWith<$Res> {
  factory $ResponseListCopyWith(
          ResponseList value, $Res Function(ResponseList) then) =
      _$ResponseListCopyWithImpl<$Res, ResponseList>;
  @useResult
  $Res call(
      {@JsonKey(name: "id") String? id,
      @JsonKey(name: 'tenantId') String? tenantId,
      @JsonKey(name: 'applicationNumber') String? applicationNumber,
      @JsonKey(name: 'status') String? status,
      @JsonKey(name: 'barRegistrationNumber') String? barRegistrationNumber,
      @JsonKey(name: 'advocateType') String? advocateType,
      @JsonKey(name: 'organisationID') String? organisationID,
      @JsonKey(name: 'individualId') String? individualId,
      @JsonKey(name: 'isActive') bool? isActive,
      @JsonKey(name: 'workflow') Workflow? workflow,
      @JsonKey(name: 'documents') List<Document>? documents,
      @JsonKey(name: 'additionalDetails')
      Map<String, dynamic>? additionalDetails});

  $WorkflowCopyWith<$Res>? get workflow;
}

/// @nodoc
class _$ResponseListCopyWithImpl<$Res, $Val extends ResponseList>
    implements $ResponseListCopyWith<$Res> {
  _$ResponseListCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = freezed,
    Object? tenantId = freezed,
    Object? applicationNumber = freezed,
    Object? status = freezed,
    Object? barRegistrationNumber = freezed,
    Object? advocateType = freezed,
    Object? organisationID = freezed,
    Object? individualId = freezed,
    Object? isActive = freezed,
    Object? workflow = freezed,
    Object? documents = freezed,
    Object? additionalDetails = freezed,
  }) {
    return _then(_value.copyWith(
      id: freezed == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String?,
      tenantId: freezed == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String?,
      applicationNumber: freezed == applicationNumber
          ? _value.applicationNumber
          : applicationNumber // ignore: cast_nullable_to_non_nullable
              as String?,
      status: freezed == status
          ? _value.status
          : status // ignore: cast_nullable_to_non_nullable
              as String?,
      barRegistrationNumber: freezed == barRegistrationNumber
          ? _value.barRegistrationNumber
          : barRegistrationNumber // ignore: cast_nullable_to_non_nullable
              as String?,
      advocateType: freezed == advocateType
          ? _value.advocateType
          : advocateType // ignore: cast_nullable_to_non_nullable
              as String?,
      organisationID: freezed == organisationID
          ? _value.organisationID
          : organisationID // ignore: cast_nullable_to_non_nullable
              as String?,
      individualId: freezed == individualId
          ? _value.individualId
          : individualId // ignore: cast_nullable_to_non_nullable
              as String?,
      isActive: freezed == isActive
          ? _value.isActive
          : isActive // ignore: cast_nullable_to_non_nullable
              as bool?,
      workflow: freezed == workflow
          ? _value.workflow
          : workflow // ignore: cast_nullable_to_non_nullable
              as Workflow?,
      documents: freezed == documents
          ? _value.documents
          : documents // ignore: cast_nullable_to_non_nullable
              as List<Document>?,
      additionalDetails: freezed == additionalDetails
          ? _value.additionalDetails
          : additionalDetails // ignore: cast_nullable_to_non_nullable
              as Map<String, dynamic>?,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $WorkflowCopyWith<$Res>? get workflow {
    if (_value.workflow == null) {
      return null;
    }

    return $WorkflowCopyWith<$Res>(_value.workflow!, (value) {
      return _then(_value.copyWith(workflow: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$ResponseListImplCopyWith<$Res>
    implements $ResponseListCopyWith<$Res> {
  factory _$$ResponseListImplCopyWith(
          _$ResponseListImpl value, $Res Function(_$ResponseListImpl) then) =
      __$$ResponseListImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: "id") String? id,
      @JsonKey(name: 'tenantId') String? tenantId,
      @JsonKey(name: 'applicationNumber') String? applicationNumber,
      @JsonKey(name: 'status') String? status,
      @JsonKey(name: 'barRegistrationNumber') String? barRegistrationNumber,
      @JsonKey(name: 'advocateType') String? advocateType,
      @JsonKey(name: 'organisationID') String? organisationID,
      @JsonKey(name: 'individualId') String? individualId,
      @JsonKey(name: 'isActive') bool? isActive,
      @JsonKey(name: 'workflow') Workflow? workflow,
      @JsonKey(name: 'documents') List<Document>? documents,
      @JsonKey(name: 'additionalDetails')
      Map<String, dynamic>? additionalDetails});

  @override
  $WorkflowCopyWith<$Res>? get workflow;
}

/// @nodoc
class __$$ResponseListImplCopyWithImpl<$Res>
    extends _$ResponseListCopyWithImpl<$Res, _$ResponseListImpl>
    implements _$$ResponseListImplCopyWith<$Res> {
  __$$ResponseListImplCopyWithImpl(
      _$ResponseListImpl _value, $Res Function(_$ResponseListImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = freezed,
    Object? tenantId = freezed,
    Object? applicationNumber = freezed,
    Object? status = freezed,
    Object? barRegistrationNumber = freezed,
    Object? advocateType = freezed,
    Object? organisationID = freezed,
    Object? individualId = freezed,
    Object? isActive = freezed,
    Object? workflow = freezed,
    Object? documents = freezed,
    Object? additionalDetails = freezed,
  }) {
    return _then(_$ResponseListImpl(
      id: freezed == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String?,
      tenantId: freezed == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String?,
      applicationNumber: freezed == applicationNumber
          ? _value.applicationNumber
          : applicationNumber // ignore: cast_nullable_to_non_nullable
              as String?,
      status: freezed == status
          ? _value.status
          : status // ignore: cast_nullable_to_non_nullable
              as String?,
      barRegistrationNumber: freezed == barRegistrationNumber
          ? _value.barRegistrationNumber
          : barRegistrationNumber // ignore: cast_nullable_to_non_nullable
              as String?,
      advocateType: freezed == advocateType
          ? _value.advocateType
          : advocateType // ignore: cast_nullable_to_non_nullable
              as String?,
      organisationID: freezed == organisationID
          ? _value.organisationID
          : organisationID // ignore: cast_nullable_to_non_nullable
              as String?,
      individualId: freezed == individualId
          ? _value.individualId
          : individualId // ignore: cast_nullable_to_non_nullable
              as String?,
      isActive: freezed == isActive
          ? _value.isActive
          : isActive // ignore: cast_nullable_to_non_nullable
              as bool?,
      workflow: freezed == workflow
          ? _value.workflow
          : workflow // ignore: cast_nullable_to_non_nullable
              as Workflow?,
      documents: freezed == documents
          ? _value._documents
          : documents // ignore: cast_nullable_to_non_nullable
              as List<Document>?,
      additionalDetails: freezed == additionalDetails
          ? _value._additionalDetails
          : additionalDetails // ignore: cast_nullable_to_non_nullable
              as Map<String, dynamic>?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$ResponseListImpl implements _ResponseList {
  const _$ResponseListImpl(
      {@JsonKey(name: "id") this.id,
      @JsonKey(name: 'tenantId') this.tenantId,
      @JsonKey(name: 'applicationNumber') this.applicationNumber,
      @JsonKey(name: 'status') this.status,
      @JsonKey(name: 'barRegistrationNumber') this.barRegistrationNumber,
      @JsonKey(name: 'advocateType') this.advocateType,
      @JsonKey(name: 'organisationID') this.organisationID,
      @JsonKey(name: 'individualId') this.individualId,
      @JsonKey(name: 'isActive') this.isActive,
      @JsonKey(name: 'workflow') this.workflow,
      @JsonKey(name: 'documents') final List<Document>? documents,
      @JsonKey(name: 'additionalDetails')
      final Map<String, dynamic>? additionalDetails})
      : _documents = documents,
        _additionalDetails = additionalDetails;

  factory _$ResponseListImpl.fromJson(Map<String, dynamic> json) =>
      _$$ResponseListImplFromJson(json);

  @override
  @JsonKey(name: "id")
  final String? id;
  @override
  @JsonKey(name: 'tenantId')
  final String? tenantId;
  @override
  @JsonKey(name: 'applicationNumber')
  final String? applicationNumber;
  @override
  @JsonKey(name: 'status')
  final String? status;
  @override
  @JsonKey(name: 'barRegistrationNumber')
  final String? barRegistrationNumber;
  @override
  @JsonKey(name: 'advocateType')
  final String? advocateType;
  @override
  @JsonKey(name: 'organisationID')
  final String? organisationID;
  @override
  @JsonKey(name: 'individualId')
  final String? individualId;
  @override
  @JsonKey(name: 'isActive')
  final bool? isActive;
  @override
  @JsonKey(name: 'workflow')
  final Workflow? workflow;
  final List<Document>? _documents;
  @override
  @JsonKey(name: 'documents')
  List<Document>? get documents {
    final value = _documents;
    if (value == null) return null;
    if (_documents is EqualUnmodifiableListView) return _documents;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(value);
  }

  final Map<String, dynamic>? _additionalDetails;
  @override
  @JsonKey(name: 'additionalDetails')
  Map<String, dynamic>? get additionalDetails {
    final value = _additionalDetails;
    if (value == null) return null;
    if (_additionalDetails is EqualUnmodifiableMapView)
      return _additionalDetails;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableMapView(value);
  }

  @override
  String toString() {
    return 'ResponseList(id: $id, tenantId: $tenantId, applicationNumber: $applicationNumber, status: $status, barRegistrationNumber: $barRegistrationNumber, advocateType: $advocateType, organisationID: $organisationID, individualId: $individualId, isActive: $isActive, workflow: $workflow, documents: $documents, additionalDetails: $additionalDetails)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ResponseListImpl &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.tenantId, tenantId) ||
                other.tenantId == tenantId) &&
            (identical(other.applicationNumber, applicationNumber) ||
                other.applicationNumber == applicationNumber) &&
            (identical(other.status, status) || other.status == status) &&
            (identical(other.barRegistrationNumber, barRegistrationNumber) ||
                other.barRegistrationNumber == barRegistrationNumber) &&
            (identical(other.advocateType, advocateType) ||
                other.advocateType == advocateType) &&
            (identical(other.organisationID, organisationID) ||
                other.organisationID == organisationID) &&
            (identical(other.individualId, individualId) ||
                other.individualId == individualId) &&
            (identical(other.isActive, isActive) ||
                other.isActive == isActive) &&
            (identical(other.workflow, workflow) ||
                other.workflow == workflow) &&
            const DeepCollectionEquality()
                .equals(other._documents, _documents) &&
            const DeepCollectionEquality()
                .equals(other._additionalDetails, _additionalDetails));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      id,
      tenantId,
      applicationNumber,
      status,
      barRegistrationNumber,
      advocateType,
      organisationID,
      individualId,
      isActive,
      workflow,
      const DeepCollectionEquality().hash(_documents),
      const DeepCollectionEquality().hash(_additionalDetails));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$ResponseListImplCopyWith<_$ResponseListImpl> get copyWith =>
      __$$ResponseListImplCopyWithImpl<_$ResponseListImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$ResponseListImplToJson(
      this,
    );
  }
}

abstract class _ResponseList implements ResponseList {
  const factory _ResponseList(
      {@JsonKey(name: "id") final String? id,
      @JsonKey(name: 'tenantId') final String? tenantId,
      @JsonKey(name: 'applicationNumber') final String? applicationNumber,
      @JsonKey(name: 'status') final String? status,
      @JsonKey(name: 'barRegistrationNumber')
      final String? barRegistrationNumber,
      @JsonKey(name: 'advocateType') final String? advocateType,
      @JsonKey(name: 'organisationID') final String? organisationID,
      @JsonKey(name: 'individualId') final String? individualId,
      @JsonKey(name: 'isActive') final bool? isActive,
      @JsonKey(name: 'workflow') final Workflow? workflow,
      @JsonKey(name: 'documents') final List<Document>? documents,
      @JsonKey(name: 'additionalDetails')
      final Map<String, dynamic>? additionalDetails}) = _$ResponseListImpl;

  factory _ResponseList.fromJson(Map<String, dynamic> json) =
      _$ResponseListImpl.fromJson;

  @override
  @JsonKey(name: "id")
  String? get id;
  @override
  @JsonKey(name: 'tenantId')
  String? get tenantId;
  @override
  @JsonKey(name: 'applicationNumber')
  String? get applicationNumber;
  @override
  @JsonKey(name: 'status')
  String? get status;
  @override
  @JsonKey(name: 'barRegistrationNumber')
  String? get barRegistrationNumber;
  @override
  @JsonKey(name: 'advocateType')
  String? get advocateType;
  @override
  @JsonKey(name: 'organisationID')
  String? get organisationID;
  @override
  @JsonKey(name: 'individualId')
  String? get individualId;
  @override
  @JsonKey(name: 'isActive')
  bool? get isActive;
  @override
  @JsonKey(name: 'workflow')
  Workflow? get workflow;
  @override
  @JsonKey(name: 'documents')
  List<Document>? get documents;
  @override
  @JsonKey(name: 'additionalDetails')
  Map<String, dynamic>? get additionalDetails;
  @override
  @JsonKey(ignore: true)
  _$$ResponseListImplCopyWith<_$ResponseListImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

Advocate _$AdvocateFromJson(Map<String, dynamic> json) {
  return _Advocate.fromJson(json);
}

/// @nodoc
mixin _$Advocate {
  @JsonKey(name: "id")
  String? get id => throw _privateConstructorUsedError;
  @JsonKey(name: 'barRegistrationNumber')
  String? get barRegistrationNumber => throw _privateConstructorUsedError;
  @JsonKey(name: 'applicationNumber')
  String? get applicationNumber => throw _privateConstructorUsedError;
  @JsonKey(name: 'individualId')
  String? get individualId => throw _privateConstructorUsedError;
  @JsonKey(name: 'responseList')
  List<ResponseList> get responseList => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AdvocateCopyWith<Advocate> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AdvocateCopyWith<$Res> {
  factory $AdvocateCopyWith(Advocate value, $Res Function(Advocate) then) =
      _$AdvocateCopyWithImpl<$Res, Advocate>;
  @useResult
  $Res call(
      {@JsonKey(name: "id") String? id,
      @JsonKey(name: 'barRegistrationNumber') String? barRegistrationNumber,
      @JsonKey(name: 'applicationNumber') String? applicationNumber,
      @JsonKey(name: 'individualId') String? individualId,
      @JsonKey(name: 'responseList') List<ResponseList> responseList});
}

/// @nodoc
class _$AdvocateCopyWithImpl<$Res, $Val extends Advocate>
    implements $AdvocateCopyWith<$Res> {
  _$AdvocateCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = freezed,
    Object? barRegistrationNumber = freezed,
    Object? applicationNumber = freezed,
    Object? individualId = freezed,
    Object? responseList = null,
  }) {
    return _then(_value.copyWith(
      id: freezed == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String?,
      barRegistrationNumber: freezed == barRegistrationNumber
          ? _value.barRegistrationNumber
          : barRegistrationNumber // ignore: cast_nullable_to_non_nullable
              as String?,
      applicationNumber: freezed == applicationNumber
          ? _value.applicationNumber
          : applicationNumber // ignore: cast_nullable_to_non_nullable
              as String?,
      individualId: freezed == individualId
          ? _value.individualId
          : individualId // ignore: cast_nullable_to_non_nullable
              as String?,
      responseList: null == responseList
          ? _value.responseList
          : responseList // ignore: cast_nullable_to_non_nullable
              as List<ResponseList>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$AdvocateImplCopyWith<$Res>
    implements $AdvocateCopyWith<$Res> {
  factory _$$AdvocateImplCopyWith(
          _$AdvocateImpl value, $Res Function(_$AdvocateImpl) then) =
      __$$AdvocateImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: "id") String? id,
      @JsonKey(name: 'barRegistrationNumber') String? barRegistrationNumber,
      @JsonKey(name: 'applicationNumber') String? applicationNumber,
      @JsonKey(name: 'individualId') String? individualId,
      @JsonKey(name: 'responseList') List<ResponseList> responseList});
}

/// @nodoc
class __$$AdvocateImplCopyWithImpl<$Res>
    extends _$AdvocateCopyWithImpl<$Res, _$AdvocateImpl>
    implements _$$AdvocateImplCopyWith<$Res> {
  __$$AdvocateImplCopyWithImpl(
      _$AdvocateImpl _value, $Res Function(_$AdvocateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = freezed,
    Object? barRegistrationNumber = freezed,
    Object? applicationNumber = freezed,
    Object? individualId = freezed,
    Object? responseList = null,
  }) {
    return _then(_$AdvocateImpl(
      id: freezed == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String?,
      barRegistrationNumber: freezed == barRegistrationNumber
          ? _value.barRegistrationNumber
          : barRegistrationNumber // ignore: cast_nullable_to_non_nullable
              as String?,
      applicationNumber: freezed == applicationNumber
          ? _value.applicationNumber
          : applicationNumber // ignore: cast_nullable_to_non_nullable
              as String?,
      individualId: freezed == individualId
          ? _value.individualId
          : individualId // ignore: cast_nullable_to_non_nullable
              as String?,
      responseList: null == responseList
          ? _value._responseList
          : responseList // ignore: cast_nullable_to_non_nullable
              as List<ResponseList>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AdvocateImpl implements _Advocate {
  const _$AdvocateImpl(
      {@JsonKey(name: "id") this.id,
      @JsonKey(name: 'barRegistrationNumber') this.barRegistrationNumber,
      @JsonKey(name: 'applicationNumber') this.applicationNumber,
      @JsonKey(name: 'individualId') this.individualId,
      @JsonKey(name: 'responseList')
      required final List<ResponseList> responseList})
      : _responseList = responseList;

  factory _$AdvocateImpl.fromJson(Map<String, dynamic> json) =>
      _$$AdvocateImplFromJson(json);

  @override
  @JsonKey(name: "id")
  final String? id;
  @override
  @JsonKey(name: 'barRegistrationNumber')
  final String? barRegistrationNumber;
  @override
  @JsonKey(name: 'applicationNumber')
  final String? applicationNumber;
  @override
  @JsonKey(name: 'individualId')
  final String? individualId;
  final List<ResponseList> _responseList;
  @override
  @JsonKey(name: 'responseList')
  List<ResponseList> get responseList {
    if (_responseList is EqualUnmodifiableListView) return _responseList;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_responseList);
  }

  @override
  String toString() {
    return 'Advocate(id: $id, barRegistrationNumber: $barRegistrationNumber, applicationNumber: $applicationNumber, individualId: $individualId, responseList: $responseList)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdvocateImpl &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.barRegistrationNumber, barRegistrationNumber) ||
                other.barRegistrationNumber == barRegistrationNumber) &&
            (identical(other.applicationNumber, applicationNumber) ||
                other.applicationNumber == applicationNumber) &&
            (identical(other.individualId, individualId) ||
                other.individualId == individualId) &&
            const DeepCollectionEquality()
                .equals(other._responseList, _responseList));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      id,
      barRegistrationNumber,
      applicationNumber,
      individualId,
      const DeepCollectionEquality().hash(_responseList));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AdvocateImplCopyWith<_$AdvocateImpl> get copyWith =>
      __$$AdvocateImplCopyWithImpl<_$AdvocateImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AdvocateImplToJson(
      this,
    );
  }
}

abstract class _Advocate implements Advocate {
  const factory _Advocate(
      {@JsonKey(name: "id") final String? id,
      @JsonKey(name: 'barRegistrationNumber')
      final String? barRegistrationNumber,
      @JsonKey(name: 'applicationNumber') final String? applicationNumber,
      @JsonKey(name: 'individualId') final String? individualId,
      @JsonKey(name: 'responseList')
      required final List<ResponseList> responseList}) = _$AdvocateImpl;

  factory _Advocate.fromJson(Map<String, dynamic> json) =
      _$AdvocateImpl.fromJson;

  @override
  @JsonKey(name: "id")
  String? get id;
  @override
  @JsonKey(name: 'barRegistrationNumber')
  String? get barRegistrationNumber;
  @override
  @JsonKey(name: 'applicationNumber')
  String? get applicationNumber;
  @override
  @JsonKey(name: 'individualId')
  String? get individualId;
  @override
  @JsonKey(name: 'responseList')
  List<ResponseList> get responseList;
  @override
  @JsonKey(ignore: true)
  _$$AdvocateImplCopyWith<_$AdvocateImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

AdvocateRegistrationRequest _$AdvocateRegistrationRequestFromJson(
    Map<String, dynamic> json) {
  return _AdvocateRegistrationRequest.fromJson(json);
}

/// @nodoc
mixin _$AdvocateRegistrationRequest {
  @JsonKey(name: 'advocate')
  ResponseList get advocate => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AdvocateRegistrationRequestCopyWith<AdvocateRegistrationRequest>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AdvocateRegistrationRequestCopyWith<$Res> {
  factory $AdvocateRegistrationRequestCopyWith(
          AdvocateRegistrationRequest value,
          $Res Function(AdvocateRegistrationRequest) then) =
      _$AdvocateRegistrationRequestCopyWithImpl<$Res,
          AdvocateRegistrationRequest>;
  @useResult
  $Res call({@JsonKey(name: 'advocate') ResponseList advocate});

  $ResponseListCopyWith<$Res> get advocate;
}

/// @nodoc
class _$AdvocateRegistrationRequestCopyWithImpl<$Res,
        $Val extends AdvocateRegistrationRequest>
    implements $AdvocateRegistrationRequestCopyWith<$Res> {
  _$AdvocateRegistrationRequestCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? advocate = null,
  }) {
    return _then(_value.copyWith(
      advocate: null == advocate
          ? _value.advocate
          : advocate // ignore: cast_nullable_to_non_nullable
              as ResponseList,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $ResponseListCopyWith<$Res> get advocate {
    return $ResponseListCopyWith<$Res>(_value.advocate, (value) {
      return _then(_value.copyWith(advocate: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$AdvocateRegistrationRequestImplCopyWith<$Res>
    implements $AdvocateRegistrationRequestCopyWith<$Res> {
  factory _$$AdvocateRegistrationRequestImplCopyWith(
          _$AdvocateRegistrationRequestImpl value,
          $Res Function(_$AdvocateRegistrationRequestImpl) then) =
      __$$AdvocateRegistrationRequestImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({@JsonKey(name: 'advocate') ResponseList advocate});

  @override
  $ResponseListCopyWith<$Res> get advocate;
}

/// @nodoc
class __$$AdvocateRegistrationRequestImplCopyWithImpl<$Res>
    extends _$AdvocateRegistrationRequestCopyWithImpl<$Res,
        _$AdvocateRegistrationRequestImpl>
    implements _$$AdvocateRegistrationRequestImplCopyWith<$Res> {
  __$$AdvocateRegistrationRequestImplCopyWithImpl(
      _$AdvocateRegistrationRequestImpl _value,
      $Res Function(_$AdvocateRegistrationRequestImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? advocate = null,
  }) {
    return _then(_$AdvocateRegistrationRequestImpl(
      advocate: null == advocate
          ? _value.advocate
          : advocate // ignore: cast_nullable_to_non_nullable
              as ResponseList,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AdvocateRegistrationRequestImpl
    implements _AdvocateRegistrationRequest {
  const _$AdvocateRegistrationRequestImpl(
      {@JsonKey(name: 'advocate') required this.advocate});

  factory _$AdvocateRegistrationRequestImpl.fromJson(
          Map<String, dynamic> json) =>
      _$$AdvocateRegistrationRequestImplFromJson(json);

  @override
  @JsonKey(name: 'advocate')
  final ResponseList advocate;

  @override
  String toString() {
    return 'AdvocateRegistrationRequest(advocate: $advocate)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdvocateRegistrationRequestImpl &&
            (identical(other.advocate, advocate) ||
                other.advocate == advocate));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, advocate);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AdvocateRegistrationRequestImplCopyWith<_$AdvocateRegistrationRequestImpl>
      get copyWith => __$$AdvocateRegistrationRequestImplCopyWithImpl<
          _$AdvocateRegistrationRequestImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AdvocateRegistrationRequestImplToJson(
      this,
    );
  }
}

abstract class _AdvocateRegistrationRequest
    implements AdvocateRegistrationRequest {
  const factory _AdvocateRegistrationRequest(
          {@JsonKey(name: 'advocate') required final ResponseList advocate}) =
      _$AdvocateRegistrationRequestImpl;

  factory _AdvocateRegistrationRequest.fromJson(Map<String, dynamic> json) =
      _$AdvocateRegistrationRequestImpl.fromJson;

  @override
  @JsonKey(name: 'advocate')
  ResponseList get advocate;
  @override
  @JsonKey(ignore: true)
  _$$AdvocateRegistrationRequestImplCopyWith<_$AdvocateRegistrationRequestImpl>
      get copyWith => throw _privateConstructorUsedError;
}

AdvocateRegistrationResponse _$AdvocateRegistrationResponseFromJson(
    Map<String, dynamic> json) {
  return _AdvocateRegistrationResponse.fromJson(json);
}

/// @nodoc
mixin _$AdvocateRegistrationResponse {
  @JsonKey(name: 'responseInfo')
  ResponseInfoSearch get responseInfo => throw _privateConstructorUsedError;
  @JsonKey(name: 'advocates')
  List<ResponseList> get advocates => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AdvocateRegistrationResponseCopyWith<AdvocateRegistrationResponse>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AdvocateRegistrationResponseCopyWith<$Res> {
  factory $AdvocateRegistrationResponseCopyWith(
          AdvocateRegistrationResponse value,
          $Res Function(AdvocateRegistrationResponse) then) =
      _$AdvocateRegistrationResponseCopyWithImpl<$Res,
          AdvocateRegistrationResponse>;
  @useResult
  $Res call(
      {@JsonKey(name: 'responseInfo') ResponseInfoSearch responseInfo,
      @JsonKey(name: 'advocates') List<ResponseList> advocates});

  $ResponseInfoSearchCopyWith<$Res> get responseInfo;
}

/// @nodoc
class _$AdvocateRegistrationResponseCopyWithImpl<$Res,
        $Val extends AdvocateRegistrationResponse>
    implements $AdvocateRegistrationResponseCopyWith<$Res> {
  _$AdvocateRegistrationResponseCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? responseInfo = null,
    Object? advocates = null,
  }) {
    return _then(_value.copyWith(
      responseInfo: null == responseInfo
          ? _value.responseInfo
          : responseInfo // ignore: cast_nullable_to_non_nullable
              as ResponseInfoSearch,
      advocates: null == advocates
          ? _value.advocates
          : advocates // ignore: cast_nullable_to_non_nullable
              as List<ResponseList>,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $ResponseInfoSearchCopyWith<$Res> get responseInfo {
    return $ResponseInfoSearchCopyWith<$Res>(_value.responseInfo, (value) {
      return _then(_value.copyWith(responseInfo: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$AdvocateRegistrationResponseImplCopyWith<$Res>
    implements $AdvocateRegistrationResponseCopyWith<$Res> {
  factory _$$AdvocateRegistrationResponseImplCopyWith(
          _$AdvocateRegistrationResponseImpl value,
          $Res Function(_$AdvocateRegistrationResponseImpl) then) =
      __$$AdvocateRegistrationResponseImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'responseInfo') ResponseInfoSearch responseInfo,
      @JsonKey(name: 'advocates') List<ResponseList> advocates});

  @override
  $ResponseInfoSearchCopyWith<$Res> get responseInfo;
}

/// @nodoc
class __$$AdvocateRegistrationResponseImplCopyWithImpl<$Res>
    extends _$AdvocateRegistrationResponseCopyWithImpl<$Res,
        _$AdvocateRegistrationResponseImpl>
    implements _$$AdvocateRegistrationResponseImplCopyWith<$Res> {
  __$$AdvocateRegistrationResponseImplCopyWithImpl(
      _$AdvocateRegistrationResponseImpl _value,
      $Res Function(_$AdvocateRegistrationResponseImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? responseInfo = null,
    Object? advocates = null,
  }) {
    return _then(_$AdvocateRegistrationResponseImpl(
      responseInfo: null == responseInfo
          ? _value.responseInfo
          : responseInfo // ignore: cast_nullable_to_non_nullable
              as ResponseInfoSearch,
      advocates: null == advocates
          ? _value._advocates
          : advocates // ignore: cast_nullable_to_non_nullable
              as List<ResponseList>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AdvocateRegistrationResponseImpl
    implements _AdvocateRegistrationResponse {
  const _$AdvocateRegistrationResponseImpl(
      {@JsonKey(name: 'responseInfo')
      this.responseInfo = const ResponseInfoSearch(status: ""),
      @JsonKey(name: 'advocates') required final List<ResponseList> advocates})
      : _advocates = advocates;

  factory _$AdvocateRegistrationResponseImpl.fromJson(
          Map<String, dynamic> json) =>
      _$$AdvocateRegistrationResponseImplFromJson(json);

  @override
  @JsonKey(name: 'responseInfo')
  final ResponseInfoSearch responseInfo;
  final List<ResponseList> _advocates;
  @override
  @JsonKey(name: 'advocates')
  List<ResponseList> get advocates {
    if (_advocates is EqualUnmodifiableListView) return _advocates;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_advocates);
  }

  @override
  String toString() {
    return 'AdvocateRegistrationResponse(responseInfo: $responseInfo, advocates: $advocates)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdvocateRegistrationResponseImpl &&
            (identical(other.responseInfo, responseInfo) ||
                other.responseInfo == responseInfo) &&
            const DeepCollectionEquality()
                .equals(other._advocates, _advocates));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, responseInfo,
      const DeepCollectionEquality().hash(_advocates));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AdvocateRegistrationResponseImplCopyWith<
          _$AdvocateRegistrationResponseImpl>
      get copyWith => __$$AdvocateRegistrationResponseImplCopyWithImpl<
          _$AdvocateRegistrationResponseImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AdvocateRegistrationResponseImplToJson(
      this,
    );
  }
}

abstract class _AdvocateRegistrationResponse
    implements AdvocateRegistrationResponse {
  const factory _AdvocateRegistrationResponse(
          {@JsonKey(name: 'responseInfo') final ResponseInfoSearch responseInfo,
          @JsonKey(name: 'advocates')
          required final List<ResponseList> advocates}) =
      _$AdvocateRegistrationResponseImpl;

  factory _AdvocateRegistrationResponse.fromJson(Map<String, dynamic> json) =
      _$AdvocateRegistrationResponseImpl.fromJson;

  @override
  @JsonKey(name: 'responseInfo')
  ResponseInfoSearch get responseInfo;
  @override
  @JsonKey(name: 'advocates')
  List<ResponseList> get advocates;
  @override
  @JsonKey(ignore: true)
  _$$AdvocateRegistrationResponseImplCopyWith<
          _$AdvocateRegistrationResponseImpl>
      get copyWith => throw _privateConstructorUsedError;
}
