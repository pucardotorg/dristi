// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'advocate_clerk_registration_model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

Clerk _$ClerkFromJson(Map<String, dynamic> json) {
  return _Clerk.fromJson(json);
}

/// @nodoc
mixin _$Clerk {
  @JsonKey(name: "id")
  String? get id => throw _privateConstructorUsedError;
  @JsonKey(name: 'tenantId')
  String? get tenantId => throw _privateConstructorUsedError;
  @JsonKey(name: 'applicationNumber')
  String? get applicationNumber => throw _privateConstructorUsedError;
  @JsonKey(name: 'status')
  String? get status => throw _privateConstructorUsedError;
  @JsonKey(name: 'stateRegnNumber')
  String? get stateRegnNumber => throw _privateConstructorUsedError;
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
  $ClerkCopyWith<Clerk> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $ClerkCopyWith<$Res> {
  factory $ClerkCopyWith(Clerk value, $Res Function(Clerk) then) =
      _$ClerkCopyWithImpl<$Res, Clerk>;
  @useResult
  $Res call(
      {@JsonKey(name: "id") String? id,
      @JsonKey(name: 'tenantId') String? tenantId,
      @JsonKey(name: 'applicationNumber') String? applicationNumber,
      @JsonKey(name: 'status') String? status,
      @JsonKey(name: 'stateRegnNumber') String? stateRegnNumber,
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
class _$ClerkCopyWithImpl<$Res, $Val extends Clerk>
    implements $ClerkCopyWith<$Res> {
  _$ClerkCopyWithImpl(this._value, this._then);

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
    Object? stateRegnNumber = freezed,
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
      stateRegnNumber: freezed == stateRegnNumber
          ? _value.stateRegnNumber
          : stateRegnNumber // ignore: cast_nullable_to_non_nullable
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
abstract class _$$ClerkImplCopyWith<$Res> implements $ClerkCopyWith<$Res> {
  factory _$$ClerkImplCopyWith(
          _$ClerkImpl value, $Res Function(_$ClerkImpl) then) =
      __$$ClerkImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: "id") String? id,
      @JsonKey(name: 'tenantId') String? tenantId,
      @JsonKey(name: 'applicationNumber') String? applicationNumber,
      @JsonKey(name: 'status') String? status,
      @JsonKey(name: 'stateRegnNumber') String? stateRegnNumber,
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
class __$$ClerkImplCopyWithImpl<$Res>
    extends _$ClerkCopyWithImpl<$Res, _$ClerkImpl>
    implements _$$ClerkImplCopyWith<$Res> {
  __$$ClerkImplCopyWithImpl(
      _$ClerkImpl _value, $Res Function(_$ClerkImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = freezed,
    Object? tenantId = freezed,
    Object? applicationNumber = freezed,
    Object? status = freezed,
    Object? stateRegnNumber = freezed,
    Object? advocateType = freezed,
    Object? organisationID = freezed,
    Object? individualId = freezed,
    Object? isActive = freezed,
    Object? workflow = freezed,
    Object? documents = freezed,
    Object? additionalDetails = freezed,
  }) {
    return _then(_$ClerkImpl(
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
      stateRegnNumber: freezed == stateRegnNumber
          ? _value.stateRegnNumber
          : stateRegnNumber // ignore: cast_nullable_to_non_nullable
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
class _$ClerkImpl implements _Clerk {
  const _$ClerkImpl(
      {@JsonKey(name: "id") this.id,
      @JsonKey(name: 'tenantId') this.tenantId,
      @JsonKey(name: 'applicationNumber') this.applicationNumber,
      @JsonKey(name: 'status') this.status,
      @JsonKey(name: 'stateRegnNumber') this.stateRegnNumber,
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

  factory _$ClerkImpl.fromJson(Map<String, dynamic> json) =>
      _$$ClerkImplFromJson(json);

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
  @JsonKey(name: 'stateRegnNumber')
  final String? stateRegnNumber;
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
    return 'Clerk(id: $id, tenantId: $tenantId, applicationNumber: $applicationNumber, status: $status, stateRegnNumber: $stateRegnNumber, advocateType: $advocateType, organisationID: $organisationID, individualId: $individualId, isActive: $isActive, workflow: $workflow, documents: $documents, additionalDetails: $additionalDetails)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ClerkImpl &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.tenantId, tenantId) ||
                other.tenantId == tenantId) &&
            (identical(other.applicationNumber, applicationNumber) ||
                other.applicationNumber == applicationNumber) &&
            (identical(other.status, status) || other.status == status) &&
            (identical(other.stateRegnNumber, stateRegnNumber) ||
                other.stateRegnNumber == stateRegnNumber) &&
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
      stateRegnNumber,
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
  _$$ClerkImplCopyWith<_$ClerkImpl> get copyWith =>
      __$$ClerkImplCopyWithImpl<_$ClerkImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$ClerkImplToJson(
      this,
    );
  }
}

abstract class _Clerk implements Clerk {
  const factory _Clerk(
      {@JsonKey(name: "id") final String? id,
      @JsonKey(name: 'tenantId') final String? tenantId,
      @JsonKey(name: 'applicationNumber') final String? applicationNumber,
      @JsonKey(name: 'status') final String? status,
      @JsonKey(name: 'stateRegnNumber') final String? stateRegnNumber,
      @JsonKey(name: 'advocateType') final String? advocateType,
      @JsonKey(name: 'organisationID') final String? organisationID,
      @JsonKey(name: 'individualId') final String? individualId,
      @JsonKey(name: 'isActive') final bool? isActive,
      @JsonKey(name: 'workflow') final Workflow? workflow,
      @JsonKey(name: 'documents') final List<Document>? documents,
      @JsonKey(name: 'additionalDetails')
      final Map<String, dynamic>? additionalDetails}) = _$ClerkImpl;

  factory _Clerk.fromJson(Map<String, dynamic> json) = _$ClerkImpl.fromJson;

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
  @JsonKey(name: 'stateRegnNumber')
  String? get stateRegnNumber;
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
  _$$ClerkImplCopyWith<_$ClerkImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

AdvocateClerkRegistrationRequest _$AdvocateClerkRegistrationRequestFromJson(
    Map<String, dynamic> json) {
  return _AdvocateClerkRegistrationRequest.fromJson(json);
}

/// @nodoc
mixin _$AdvocateClerkRegistrationRequest {
  @JsonKey(name: 'RequestInfo')
  AdvocateRequestInfo get requestInfo => throw _privateConstructorUsedError;
  @JsonKey(name: 'clerks')
  List<Clerk> get clerks => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AdvocateClerkRegistrationRequestCopyWith<AdvocateClerkRegistrationRequest>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AdvocateClerkRegistrationRequestCopyWith<$Res> {
  factory $AdvocateClerkRegistrationRequestCopyWith(
          AdvocateClerkRegistrationRequest value,
          $Res Function(AdvocateClerkRegistrationRequest) then) =
      _$AdvocateClerkRegistrationRequestCopyWithImpl<$Res,
          AdvocateClerkRegistrationRequest>;
  @useResult
  $Res call(
      {@JsonKey(name: 'RequestInfo') AdvocateRequestInfo requestInfo,
      @JsonKey(name: 'clerks') List<Clerk> clerks});

  $AdvocateRequestInfoCopyWith<$Res> get requestInfo;
}

/// @nodoc
class _$AdvocateClerkRegistrationRequestCopyWithImpl<$Res,
        $Val extends AdvocateClerkRegistrationRequest>
    implements $AdvocateClerkRegistrationRequestCopyWith<$Res> {
  _$AdvocateClerkRegistrationRequestCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? requestInfo = null,
    Object? clerks = null,
  }) {
    return _then(_value.copyWith(
      requestInfo: null == requestInfo
          ? _value.requestInfo
          : requestInfo // ignore: cast_nullable_to_non_nullable
              as AdvocateRequestInfo,
      clerks: null == clerks
          ? _value.clerks
          : clerks // ignore: cast_nullable_to_non_nullable
              as List<Clerk>,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $AdvocateRequestInfoCopyWith<$Res> get requestInfo {
    return $AdvocateRequestInfoCopyWith<$Res>(_value.requestInfo, (value) {
      return _then(_value.copyWith(requestInfo: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$AdvocateClerkRegistrationRequestImplCopyWith<$Res>
    implements $AdvocateClerkRegistrationRequestCopyWith<$Res> {
  factory _$$AdvocateClerkRegistrationRequestImplCopyWith(
          _$AdvocateClerkRegistrationRequestImpl value,
          $Res Function(_$AdvocateClerkRegistrationRequestImpl) then) =
      __$$AdvocateClerkRegistrationRequestImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'RequestInfo') AdvocateRequestInfo requestInfo,
      @JsonKey(name: 'clerks') List<Clerk> clerks});

  @override
  $AdvocateRequestInfoCopyWith<$Res> get requestInfo;
}

/// @nodoc
class __$$AdvocateClerkRegistrationRequestImplCopyWithImpl<$Res>
    extends _$AdvocateClerkRegistrationRequestCopyWithImpl<$Res,
        _$AdvocateClerkRegistrationRequestImpl>
    implements _$$AdvocateClerkRegistrationRequestImplCopyWith<$Res> {
  __$$AdvocateClerkRegistrationRequestImplCopyWithImpl(
      _$AdvocateClerkRegistrationRequestImpl _value,
      $Res Function(_$AdvocateClerkRegistrationRequestImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? requestInfo = null,
    Object? clerks = null,
  }) {
    return _then(_$AdvocateClerkRegistrationRequestImpl(
      requestInfo: null == requestInfo
          ? _value.requestInfo
          : requestInfo // ignore: cast_nullable_to_non_nullable
              as AdvocateRequestInfo,
      clerks: null == clerks
          ? _value._clerks
          : clerks // ignore: cast_nullable_to_non_nullable
              as List<Clerk>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AdvocateClerkRegistrationRequestImpl
    implements _AdvocateClerkRegistrationRequest {
  const _$AdvocateClerkRegistrationRequestImpl(
      {@JsonKey(name: 'RequestInfo') required this.requestInfo,
      @JsonKey(name: 'clerks') required final List<Clerk> clerks})
      : _clerks = clerks;

  factory _$AdvocateClerkRegistrationRequestImpl.fromJson(
          Map<String, dynamic> json) =>
      _$$AdvocateClerkRegistrationRequestImplFromJson(json);

  @override
  @JsonKey(name: 'RequestInfo')
  final AdvocateRequestInfo requestInfo;
  final List<Clerk> _clerks;
  @override
  @JsonKey(name: 'clerks')
  List<Clerk> get clerks {
    if (_clerks is EqualUnmodifiableListView) return _clerks;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_clerks);
  }

  @override
  String toString() {
    return 'AdvocateClerkRegistrationRequest(requestInfo: $requestInfo, clerks: $clerks)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdvocateClerkRegistrationRequestImpl &&
            (identical(other.requestInfo, requestInfo) ||
                other.requestInfo == requestInfo) &&
            const DeepCollectionEquality().equals(other._clerks, _clerks));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType, requestInfo, const DeepCollectionEquality().hash(_clerks));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AdvocateClerkRegistrationRequestImplCopyWith<
          _$AdvocateClerkRegistrationRequestImpl>
      get copyWith => __$$AdvocateClerkRegistrationRequestImplCopyWithImpl<
          _$AdvocateClerkRegistrationRequestImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AdvocateClerkRegistrationRequestImplToJson(
      this,
    );
  }
}

abstract class _AdvocateClerkRegistrationRequest
    implements AdvocateClerkRegistrationRequest {
  const factory _AdvocateClerkRegistrationRequest(
          {@JsonKey(name: 'RequestInfo')
          required final AdvocateRequestInfo requestInfo,
          @JsonKey(name: 'clerks') required final List<Clerk> clerks}) =
      _$AdvocateClerkRegistrationRequestImpl;

  factory _AdvocateClerkRegistrationRequest.fromJson(
          Map<String, dynamic> json) =
      _$AdvocateClerkRegistrationRequestImpl.fromJson;

  @override
  @JsonKey(name: 'RequestInfo')
  AdvocateRequestInfo get requestInfo;
  @override
  @JsonKey(name: 'clerks')
  List<Clerk> get clerks;
  @override
  @JsonKey(ignore: true)
  _$$AdvocateClerkRegistrationRequestImplCopyWith<
          _$AdvocateClerkRegistrationRequestImpl>
      get copyWith => throw _privateConstructorUsedError;
}

AdvocateClerkRegistrationResponse _$AdvocateClerkRegistrationResponseFromJson(
    Map<String, dynamic> json) {
  return _AdvocateClerkRegistrationResponse.fromJson(json);
}

/// @nodoc
mixin _$AdvocateClerkRegistrationResponse {
  @JsonKey(name: 'responseInfo')
  ResponseInfoSearch get responseInfo => throw _privateConstructorUsedError;
  @JsonKey(name: 'clerks')
  List<Clerk> get clerks => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AdvocateClerkRegistrationResponseCopyWith<AdvocateClerkRegistrationResponse>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AdvocateClerkRegistrationResponseCopyWith<$Res> {
  factory $AdvocateClerkRegistrationResponseCopyWith(
          AdvocateClerkRegistrationResponse value,
          $Res Function(AdvocateClerkRegistrationResponse) then) =
      _$AdvocateClerkRegistrationResponseCopyWithImpl<$Res,
          AdvocateClerkRegistrationResponse>;
  @useResult
  $Res call(
      {@JsonKey(name: 'responseInfo') ResponseInfoSearch responseInfo,
      @JsonKey(name: 'clerks') List<Clerk> clerks});

  $ResponseInfoSearchCopyWith<$Res> get responseInfo;
}

/// @nodoc
class _$AdvocateClerkRegistrationResponseCopyWithImpl<$Res,
        $Val extends AdvocateClerkRegistrationResponse>
    implements $AdvocateClerkRegistrationResponseCopyWith<$Res> {
  _$AdvocateClerkRegistrationResponseCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? responseInfo = null,
    Object? clerks = null,
  }) {
    return _then(_value.copyWith(
      responseInfo: null == responseInfo
          ? _value.responseInfo
          : responseInfo // ignore: cast_nullable_to_non_nullable
              as ResponseInfoSearch,
      clerks: null == clerks
          ? _value.clerks
          : clerks // ignore: cast_nullable_to_non_nullable
              as List<Clerk>,
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
abstract class _$$AdvocateClerkRegistrationResponseImplCopyWith<$Res>
    implements $AdvocateClerkRegistrationResponseCopyWith<$Res> {
  factory _$$AdvocateClerkRegistrationResponseImplCopyWith(
          _$AdvocateClerkRegistrationResponseImpl value,
          $Res Function(_$AdvocateClerkRegistrationResponseImpl) then) =
      __$$AdvocateClerkRegistrationResponseImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'responseInfo') ResponseInfoSearch responseInfo,
      @JsonKey(name: 'clerks') List<Clerk> clerks});

  @override
  $ResponseInfoSearchCopyWith<$Res> get responseInfo;
}

/// @nodoc
class __$$AdvocateClerkRegistrationResponseImplCopyWithImpl<$Res>
    extends _$AdvocateClerkRegistrationResponseCopyWithImpl<$Res,
        _$AdvocateClerkRegistrationResponseImpl>
    implements _$$AdvocateClerkRegistrationResponseImplCopyWith<$Res> {
  __$$AdvocateClerkRegistrationResponseImplCopyWithImpl(
      _$AdvocateClerkRegistrationResponseImpl _value,
      $Res Function(_$AdvocateClerkRegistrationResponseImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? responseInfo = null,
    Object? clerks = null,
  }) {
    return _then(_$AdvocateClerkRegistrationResponseImpl(
      responseInfo: null == responseInfo
          ? _value.responseInfo
          : responseInfo // ignore: cast_nullable_to_non_nullable
              as ResponseInfoSearch,
      clerks: null == clerks
          ? _value._clerks
          : clerks // ignore: cast_nullable_to_non_nullable
              as List<Clerk>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AdvocateClerkRegistrationResponseImpl
    implements _AdvocateClerkRegistrationResponse {
  const _$AdvocateClerkRegistrationResponseImpl(
      {@JsonKey(name: 'responseInfo')
      this.responseInfo = const ResponseInfoSearch(status: ""),
      @JsonKey(name: 'clerks') required final List<Clerk> clerks})
      : _clerks = clerks;

  factory _$AdvocateClerkRegistrationResponseImpl.fromJson(
          Map<String, dynamic> json) =>
      _$$AdvocateClerkRegistrationResponseImplFromJson(json);

  @override
  @JsonKey(name: 'responseInfo')
  final ResponseInfoSearch responseInfo;
  final List<Clerk> _clerks;
  @override
  @JsonKey(name: 'clerks')
  List<Clerk> get clerks {
    if (_clerks is EqualUnmodifiableListView) return _clerks;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_clerks);
  }

  @override
  String toString() {
    return 'AdvocateClerkRegistrationResponse(responseInfo: $responseInfo, clerks: $clerks)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdvocateClerkRegistrationResponseImpl &&
            (identical(other.responseInfo, responseInfo) ||
                other.responseInfo == responseInfo) &&
            const DeepCollectionEquality().equals(other._clerks, _clerks));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType, responseInfo, const DeepCollectionEquality().hash(_clerks));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AdvocateClerkRegistrationResponseImplCopyWith<
          _$AdvocateClerkRegistrationResponseImpl>
      get copyWith => __$$AdvocateClerkRegistrationResponseImplCopyWithImpl<
          _$AdvocateClerkRegistrationResponseImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AdvocateClerkRegistrationResponseImplToJson(
      this,
    );
  }
}

abstract class _AdvocateClerkRegistrationResponse
    implements AdvocateClerkRegistrationResponse {
  const factory _AdvocateClerkRegistrationResponse(
          {@JsonKey(name: 'responseInfo') final ResponseInfoSearch responseInfo,
          @JsonKey(name: 'clerks') required final List<Clerk> clerks}) =
      _$AdvocateClerkRegistrationResponseImpl;

  factory _AdvocateClerkRegistrationResponse.fromJson(
          Map<String, dynamic> json) =
      _$AdvocateClerkRegistrationResponseImpl.fromJson;

  @override
  @JsonKey(name: 'responseInfo')
  ResponseInfoSearch get responseInfo;
  @override
  @JsonKey(name: 'clerks')
  List<Clerk> get clerks;
  @override
  @JsonKey(ignore: true)
  _$$AdvocateClerkRegistrationResponseImplCopyWith<
          _$AdvocateClerkRegistrationResponseImpl>
      get copyWith => throw _privateConstructorUsedError;
}
