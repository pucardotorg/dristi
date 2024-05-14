// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'advocate_search_model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

AdvocateSearchRequest _$AdvocateSearchRequestFromJson(
    Map<String, dynamic> json) {
  return _AdvocateSearchRequest.fromJson(json);
}

/// @nodoc
mixin _$AdvocateSearchRequest {
  @JsonKey(name: "criteria")
  List<SearchCriteria> get criteria => throw _privateConstructorUsedError;
  @JsonKey(name: 'status')
  List<String> get status => throw _privateConstructorUsedError;
  @JsonKey(name: 'tenantId')
  String? get tenantId => throw _privateConstructorUsedError;
  @JsonKey(name: 'RequestInfo')
  AdvocateRequestInfo get requestInfo => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AdvocateSearchRequestCopyWith<AdvocateSearchRequest> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AdvocateSearchRequestCopyWith<$Res> {
  factory $AdvocateSearchRequestCopyWith(AdvocateSearchRequest value,
          $Res Function(AdvocateSearchRequest) then) =
      _$AdvocateSearchRequestCopyWithImpl<$Res, AdvocateSearchRequest>;
  @useResult
  $Res call(
      {@JsonKey(name: "criteria") List<SearchCriteria> criteria,
      @JsonKey(name: 'status') List<String> status,
      @JsonKey(name: 'tenantId') String? tenantId,
      @JsonKey(name: 'RequestInfo') AdvocateRequestInfo requestInfo});

  $AdvocateRequestInfoCopyWith<$Res> get requestInfo;
}

/// @nodoc
class _$AdvocateSearchRequestCopyWithImpl<$Res,
        $Val extends AdvocateSearchRequest>
    implements $AdvocateSearchRequestCopyWith<$Res> {
  _$AdvocateSearchRequestCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? criteria = null,
    Object? status = null,
    Object? tenantId = freezed,
    Object? requestInfo = null,
  }) {
    return _then(_value.copyWith(
      criteria: null == criteria
          ? _value.criteria
          : criteria // ignore: cast_nullable_to_non_nullable
              as List<SearchCriteria>,
      status: null == status
          ? _value.status
          : status // ignore: cast_nullable_to_non_nullable
              as List<String>,
      tenantId: freezed == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String?,
      requestInfo: null == requestInfo
          ? _value.requestInfo
          : requestInfo // ignore: cast_nullable_to_non_nullable
              as AdvocateRequestInfo,
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
abstract class _$$AdvocateSearchRequestImplCopyWith<$Res>
    implements $AdvocateSearchRequestCopyWith<$Res> {
  factory _$$AdvocateSearchRequestImplCopyWith(
          _$AdvocateSearchRequestImpl value,
          $Res Function(_$AdvocateSearchRequestImpl) then) =
      __$$AdvocateSearchRequestImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: "criteria") List<SearchCriteria> criteria,
      @JsonKey(name: 'status') List<String> status,
      @JsonKey(name: 'tenantId') String? tenantId,
      @JsonKey(name: 'RequestInfo') AdvocateRequestInfo requestInfo});

  @override
  $AdvocateRequestInfoCopyWith<$Res> get requestInfo;
}

/// @nodoc
class __$$AdvocateSearchRequestImplCopyWithImpl<$Res>
    extends _$AdvocateSearchRequestCopyWithImpl<$Res,
        _$AdvocateSearchRequestImpl>
    implements _$$AdvocateSearchRequestImplCopyWith<$Res> {
  __$$AdvocateSearchRequestImplCopyWithImpl(_$AdvocateSearchRequestImpl _value,
      $Res Function(_$AdvocateSearchRequestImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? criteria = null,
    Object? status = null,
    Object? tenantId = freezed,
    Object? requestInfo = null,
  }) {
    return _then(_$AdvocateSearchRequestImpl(
      criteria: null == criteria
          ? _value._criteria
          : criteria // ignore: cast_nullable_to_non_nullable
              as List<SearchCriteria>,
      status: null == status
          ? _value._status
          : status // ignore: cast_nullable_to_non_nullable
              as List<String>,
      tenantId: freezed == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String?,
      requestInfo: null == requestInfo
          ? _value.requestInfo
          : requestInfo // ignore: cast_nullable_to_non_nullable
              as AdvocateRequestInfo,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AdvocateSearchRequestImpl implements _AdvocateSearchRequest {
  const _$AdvocateSearchRequestImpl(
      {@JsonKey(name: "criteria")
      final List<SearchCriteria> criteria = const [],
      @JsonKey(name: 'status') final List<String> status = const [],
      @JsonKey(name: 'tenantId') this.tenantId,
      @JsonKey(name: 'RequestInfo') required this.requestInfo})
      : _criteria = criteria,
        _status = status;

  factory _$AdvocateSearchRequestImpl.fromJson(Map<String, dynamic> json) =>
      _$$AdvocateSearchRequestImplFromJson(json);

  final List<SearchCriteria> _criteria;
  @override
  @JsonKey(name: "criteria")
  List<SearchCriteria> get criteria {
    if (_criteria is EqualUnmodifiableListView) return _criteria;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_criteria);
  }

  final List<String> _status;
  @override
  @JsonKey(name: 'status')
  List<String> get status {
    if (_status is EqualUnmodifiableListView) return _status;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_status);
  }

  @override
  @JsonKey(name: 'tenantId')
  final String? tenantId;
  @override
  @JsonKey(name: 'RequestInfo')
  final AdvocateRequestInfo requestInfo;

  @override
  String toString() {
    return 'AdvocateSearchRequest(criteria: $criteria, status: $status, tenantId: $tenantId, requestInfo: $requestInfo)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdvocateSearchRequestImpl &&
            const DeepCollectionEquality().equals(other._criteria, _criteria) &&
            const DeepCollectionEquality().equals(other._status, _status) &&
            (identical(other.tenantId, tenantId) ||
                other.tenantId == tenantId) &&
            (identical(other.requestInfo, requestInfo) ||
                other.requestInfo == requestInfo));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      const DeepCollectionEquality().hash(_criteria),
      const DeepCollectionEquality().hash(_status),
      tenantId,
      requestInfo);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AdvocateSearchRequestImplCopyWith<_$AdvocateSearchRequestImpl>
      get copyWith => __$$AdvocateSearchRequestImplCopyWithImpl<
          _$AdvocateSearchRequestImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AdvocateSearchRequestImplToJson(
      this,
    );
  }
}

abstract class _AdvocateSearchRequest implements AdvocateSearchRequest {
  const factory _AdvocateSearchRequest(
          {@JsonKey(name: "criteria") final List<SearchCriteria> criteria,
          @JsonKey(name: 'status') final List<String> status,
          @JsonKey(name: 'tenantId') final String? tenantId,
          @JsonKey(name: 'RequestInfo')
          required final AdvocateRequestInfo requestInfo}) =
      _$AdvocateSearchRequestImpl;

  factory _AdvocateSearchRequest.fromJson(Map<String, dynamic> json) =
      _$AdvocateSearchRequestImpl.fromJson;

  @override
  @JsonKey(name: "criteria")
  List<SearchCriteria> get criteria;
  @override
  @JsonKey(name: 'status')
  List<String> get status;
  @override
  @JsonKey(name: 'tenantId')
  String? get tenantId;
  @override
  @JsonKey(name: 'RequestInfo')
  AdvocateRequestInfo get requestInfo;
  @override
  @JsonKey(ignore: true)
  _$$AdvocateSearchRequestImplCopyWith<_$AdvocateSearchRequestImpl>
      get copyWith => throw _privateConstructorUsedError;
}

SearchCriteria _$SearchCriteriaFromJson(Map<String, dynamic> json) {
  return _SearchCriteria.fromJson(json);
}

/// @nodoc
mixin _$SearchCriteria {
  @JsonKey(name: "individualId")
  String? get individualId => throw _privateConstructorUsedError;
  @JsonKey(name: "uuid")
  String? get uuid => throw _privateConstructorUsedError;
  @JsonKey(name: "applicationNumber")
  String? get applicationNumber => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $SearchCriteriaCopyWith<SearchCriteria> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $SearchCriteriaCopyWith<$Res> {
  factory $SearchCriteriaCopyWith(
          SearchCriteria value, $Res Function(SearchCriteria) then) =
      _$SearchCriteriaCopyWithImpl<$Res, SearchCriteria>;
  @useResult
  $Res call(
      {@JsonKey(name: "individualId") String? individualId,
      @JsonKey(name: "uuid") String? uuid,
      @JsonKey(name: "applicationNumber") String? applicationNumber});
}

/// @nodoc
class _$SearchCriteriaCopyWithImpl<$Res, $Val extends SearchCriteria>
    implements $SearchCriteriaCopyWith<$Res> {
  _$SearchCriteriaCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? individualId = freezed,
    Object? uuid = freezed,
    Object? applicationNumber = freezed,
  }) {
    return _then(_value.copyWith(
      individualId: freezed == individualId
          ? _value.individualId
          : individualId // ignore: cast_nullable_to_non_nullable
              as String?,
      uuid: freezed == uuid
          ? _value.uuid
          : uuid // ignore: cast_nullable_to_non_nullable
              as String?,
      applicationNumber: freezed == applicationNumber
          ? _value.applicationNumber
          : applicationNumber // ignore: cast_nullable_to_non_nullable
              as String?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$SearchCriteriaImplCopyWith<$Res>
    implements $SearchCriteriaCopyWith<$Res> {
  factory _$$SearchCriteriaImplCopyWith(_$SearchCriteriaImpl value,
          $Res Function(_$SearchCriteriaImpl) then) =
      __$$SearchCriteriaImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: "individualId") String? individualId,
      @JsonKey(name: "uuid") String? uuid,
      @JsonKey(name: "applicationNumber") String? applicationNumber});
}

/// @nodoc
class __$$SearchCriteriaImplCopyWithImpl<$Res>
    extends _$SearchCriteriaCopyWithImpl<$Res, _$SearchCriteriaImpl>
    implements _$$SearchCriteriaImplCopyWith<$Res> {
  __$$SearchCriteriaImplCopyWithImpl(
      _$SearchCriteriaImpl _value, $Res Function(_$SearchCriteriaImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? individualId = freezed,
    Object? uuid = freezed,
    Object? applicationNumber = freezed,
  }) {
    return _then(_$SearchCriteriaImpl(
      individualId: freezed == individualId
          ? _value.individualId
          : individualId // ignore: cast_nullable_to_non_nullable
              as String?,
      uuid: freezed == uuid
          ? _value.uuid
          : uuid // ignore: cast_nullable_to_non_nullable
              as String?,
      applicationNumber: freezed == applicationNumber
          ? _value.applicationNumber
          : applicationNumber // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$SearchCriteriaImpl implements _SearchCriteria {
  const _$SearchCriteriaImpl(
      {@JsonKey(name: "individualId") this.individualId,
      @JsonKey(name: "uuid") this.uuid,
      @JsonKey(name: "applicationNumber") this.applicationNumber});

  factory _$SearchCriteriaImpl.fromJson(Map<String, dynamic> json) =>
      _$$SearchCriteriaImplFromJson(json);

  @override
  @JsonKey(name: "individualId")
  final String? individualId;
  @override
  @JsonKey(name: "uuid")
  final String? uuid;
  @override
  @JsonKey(name: "applicationNumber")
  final String? applicationNumber;

  @override
  String toString() {
    return 'SearchCriteria(individualId: $individualId, uuid: $uuid, applicationNumber: $applicationNumber)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$SearchCriteriaImpl &&
            (identical(other.individualId, individualId) ||
                other.individualId == individualId) &&
            (identical(other.uuid, uuid) || other.uuid == uuid) &&
            (identical(other.applicationNumber, applicationNumber) ||
                other.applicationNumber == applicationNumber));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, individualId, uuid, applicationNumber);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$SearchCriteriaImplCopyWith<_$SearchCriteriaImpl> get copyWith =>
      __$$SearchCriteriaImplCopyWithImpl<_$SearchCriteriaImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$SearchCriteriaImplToJson(
      this,
    );
  }
}

abstract class _SearchCriteria implements SearchCriteria {
  const factory _SearchCriteria(
      {@JsonKey(name: "individualId") final String? individualId,
      @JsonKey(name: "uuid") final String? uuid,
      @JsonKey(name: "applicationNumber")
      final String? applicationNumber}) = _$SearchCriteriaImpl;

  factory _SearchCriteria.fromJson(Map<String, dynamic> json) =
      _$SearchCriteriaImpl.fromJson;

  @override
  @JsonKey(name: "individualId")
  String? get individualId;
  @override
  @JsonKey(name: "uuid")
  String? get uuid;
  @override
  @JsonKey(name: "applicationNumber")
  String? get applicationNumber;
  @override
  @JsonKey(ignore: true)
  _$$SearchCriteriaImplCopyWith<_$SearchCriteriaImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

AdvocateSearchResponse _$AdvocateSearchResponseFromJson(
    Map<String, dynamic> json) {
  return _AdvocateSearchResponse.fromJson(json);
}

/// @nodoc
mixin _$AdvocateSearchResponse {
  @JsonKey(name: 'responseInfo')
  ResponseInfoSearch get responseInfo => throw _privateConstructorUsedError;
  @JsonKey(name: 'advocates')
  List<Advocate> get advocates => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AdvocateSearchResponseCopyWith<AdvocateSearchResponse> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AdvocateSearchResponseCopyWith<$Res> {
  factory $AdvocateSearchResponseCopyWith(AdvocateSearchResponse value,
          $Res Function(AdvocateSearchResponse) then) =
      _$AdvocateSearchResponseCopyWithImpl<$Res, AdvocateSearchResponse>;
  @useResult
  $Res call(
      {@JsonKey(name: 'responseInfo') ResponseInfoSearch responseInfo,
      @JsonKey(name: 'advocates') List<Advocate> advocates});

  $ResponseInfoSearchCopyWith<$Res> get responseInfo;
}

/// @nodoc
class _$AdvocateSearchResponseCopyWithImpl<$Res,
        $Val extends AdvocateSearchResponse>
    implements $AdvocateSearchResponseCopyWith<$Res> {
  _$AdvocateSearchResponseCopyWithImpl(this._value, this._then);

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
              as List<Advocate>,
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
abstract class _$$AdvocateSearchResponseImplCopyWith<$Res>
    implements $AdvocateSearchResponseCopyWith<$Res> {
  factory _$$AdvocateSearchResponseImplCopyWith(
          _$AdvocateSearchResponseImpl value,
          $Res Function(_$AdvocateSearchResponseImpl) then) =
      __$$AdvocateSearchResponseImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'responseInfo') ResponseInfoSearch responseInfo,
      @JsonKey(name: 'advocates') List<Advocate> advocates});

  @override
  $ResponseInfoSearchCopyWith<$Res> get responseInfo;
}

/// @nodoc
class __$$AdvocateSearchResponseImplCopyWithImpl<$Res>
    extends _$AdvocateSearchResponseCopyWithImpl<$Res,
        _$AdvocateSearchResponseImpl>
    implements _$$AdvocateSearchResponseImplCopyWith<$Res> {
  __$$AdvocateSearchResponseImplCopyWithImpl(
      _$AdvocateSearchResponseImpl _value,
      $Res Function(_$AdvocateSearchResponseImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? responseInfo = null,
    Object? advocates = null,
  }) {
    return _then(_$AdvocateSearchResponseImpl(
      responseInfo: null == responseInfo
          ? _value.responseInfo
          : responseInfo // ignore: cast_nullable_to_non_nullable
              as ResponseInfoSearch,
      advocates: null == advocates
          ? _value._advocates
          : advocates // ignore: cast_nullable_to_non_nullable
              as List<Advocate>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AdvocateSearchResponseImpl implements _AdvocateSearchResponse {
  const _$AdvocateSearchResponseImpl(
      {@JsonKey(name: 'responseInfo')
      this.responseInfo = const ResponseInfoSearch(status: ""),
      @JsonKey(name: 'advocates') final List<Advocate> advocates = const []})
      : _advocates = advocates;

  factory _$AdvocateSearchResponseImpl.fromJson(Map<String, dynamic> json) =>
      _$$AdvocateSearchResponseImplFromJson(json);

  @override
  @JsonKey(name: 'responseInfo')
  final ResponseInfoSearch responseInfo;
  final List<Advocate> _advocates;
  @override
  @JsonKey(name: 'advocates')
  List<Advocate> get advocates {
    if (_advocates is EqualUnmodifiableListView) return _advocates;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_advocates);
  }

  @override
  String toString() {
    return 'AdvocateSearchResponse(responseInfo: $responseInfo, advocates: $advocates)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdvocateSearchResponseImpl &&
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
  _$$AdvocateSearchResponseImplCopyWith<_$AdvocateSearchResponseImpl>
      get copyWith => __$$AdvocateSearchResponseImplCopyWithImpl<
          _$AdvocateSearchResponseImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AdvocateSearchResponseImplToJson(
      this,
    );
  }
}

abstract class _AdvocateSearchResponse implements AdvocateSearchResponse {
  const factory _AdvocateSearchResponse(
          {@JsonKey(name: 'responseInfo') final ResponseInfoSearch responseInfo,
          @JsonKey(name: 'advocates') final List<Advocate> advocates}) =
      _$AdvocateSearchResponseImpl;

  factory _AdvocateSearchResponse.fromJson(Map<String, dynamic> json) =
      _$AdvocateSearchResponseImpl.fromJson;

  @override
  @JsonKey(name: 'responseInfo')
  ResponseInfoSearch get responseInfo;
  @override
  @JsonKey(name: 'advocates')
  List<Advocate> get advocates;
  @override
  @JsonKey(ignore: true)
  _$$AdvocateSearchResponseImplCopyWith<_$AdvocateSearchResponseImpl>
      get copyWith => throw _privateConstructorUsedError;
}
