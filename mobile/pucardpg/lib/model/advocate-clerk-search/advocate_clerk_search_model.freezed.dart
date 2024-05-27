// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'advocate_clerk_search_model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

AdvocateClerkSearchRequest _$AdvocateClerkSearchRequestFromJson(
    Map<String, dynamic> json) {
  return _AdvocateClerkSearchRequest.fromJson(json);
}

/// @nodoc
mixin _$AdvocateClerkSearchRequest {
  @JsonKey(name: "criteria")
  List<SearchCriteria> get criteria => throw _privateConstructorUsedError;
  @JsonKey(name: 'status')
  List<String> get status => throw _privateConstructorUsedError;
  @JsonKey(name: 'tenantId')
  String? get tenantId => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AdvocateClerkSearchRequestCopyWith<AdvocateClerkSearchRequest>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AdvocateClerkSearchRequestCopyWith<$Res> {
  factory $AdvocateClerkSearchRequestCopyWith(AdvocateClerkSearchRequest value,
          $Res Function(AdvocateClerkSearchRequest) then) =
      _$AdvocateClerkSearchRequestCopyWithImpl<$Res,
          AdvocateClerkSearchRequest>;
  @useResult
  $Res call(
      {@JsonKey(name: "criteria") List<SearchCriteria> criteria,
      @JsonKey(name: 'status') List<String> status,
      @JsonKey(name: 'tenantId') String? tenantId});
}

/// @nodoc
class _$AdvocateClerkSearchRequestCopyWithImpl<$Res,
        $Val extends AdvocateClerkSearchRequest>
    implements $AdvocateClerkSearchRequestCopyWith<$Res> {
  _$AdvocateClerkSearchRequestCopyWithImpl(this._value, this._then);

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
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$AdvocateClerkSearchRequestImplCopyWith<$Res>
    implements $AdvocateClerkSearchRequestCopyWith<$Res> {
  factory _$$AdvocateClerkSearchRequestImplCopyWith(
          _$AdvocateClerkSearchRequestImpl value,
          $Res Function(_$AdvocateClerkSearchRequestImpl) then) =
      __$$AdvocateClerkSearchRequestImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: "criteria") List<SearchCriteria> criteria,
      @JsonKey(name: 'status') List<String> status,
      @JsonKey(name: 'tenantId') String? tenantId});
}

/// @nodoc
class __$$AdvocateClerkSearchRequestImplCopyWithImpl<$Res>
    extends _$AdvocateClerkSearchRequestCopyWithImpl<$Res,
        _$AdvocateClerkSearchRequestImpl>
    implements _$$AdvocateClerkSearchRequestImplCopyWith<$Res> {
  __$$AdvocateClerkSearchRequestImplCopyWithImpl(
      _$AdvocateClerkSearchRequestImpl _value,
      $Res Function(_$AdvocateClerkSearchRequestImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? criteria = null,
    Object? status = null,
    Object? tenantId = freezed,
  }) {
    return _then(_$AdvocateClerkSearchRequestImpl(
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
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$AdvocateClerkSearchRequestImpl implements _AdvocateClerkSearchRequest {
  const _$AdvocateClerkSearchRequestImpl(
      {@JsonKey(name: "criteria")
      final List<SearchCriteria> criteria = const [],
      @JsonKey(name: 'status') final List<String> status = const [],
      @JsonKey(name: 'tenantId') this.tenantId})
      : _criteria = criteria,
        _status = status;

  factory _$AdvocateClerkSearchRequestImpl.fromJson(
          Map<String, dynamic> json) =>
      _$$AdvocateClerkSearchRequestImplFromJson(json);

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
  String toString() {
    return 'AdvocateClerkSearchRequest(criteria: $criteria, status: $status, tenantId: $tenantId)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdvocateClerkSearchRequestImpl &&
            const DeepCollectionEquality().equals(other._criteria, _criteria) &&
            const DeepCollectionEquality().equals(other._status, _status) &&
            (identical(other.tenantId, tenantId) ||
                other.tenantId == tenantId));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType,
      const DeepCollectionEquality().hash(_criteria),
      const DeepCollectionEquality().hash(_status),
      tenantId);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$AdvocateClerkSearchRequestImplCopyWith<_$AdvocateClerkSearchRequestImpl>
      get copyWith => __$$AdvocateClerkSearchRequestImplCopyWithImpl<
          _$AdvocateClerkSearchRequestImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AdvocateClerkSearchRequestImplToJson(
      this,
    );
  }
}

abstract class _AdvocateClerkSearchRequest
    implements AdvocateClerkSearchRequest {
  const factory _AdvocateClerkSearchRequest(
          {@JsonKey(name: "criteria") final List<SearchCriteria> criteria,
          @JsonKey(name: 'status') final List<String> status,
          @JsonKey(name: 'tenantId') final String? tenantId}) =
      _$AdvocateClerkSearchRequestImpl;

  factory _AdvocateClerkSearchRequest.fromJson(Map<String, dynamic> json) =
      _$AdvocateClerkSearchRequestImpl.fromJson;

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
  @JsonKey(ignore: true)
  _$$AdvocateClerkSearchRequestImplCopyWith<_$AdvocateClerkSearchRequestImpl>
      get copyWith => throw _privateConstructorUsedError;
}

AdvocateClerkSearchResponse _$AdvocateClerkSearchResponseFromJson(
    Map<String, dynamic> json) {
  return _AdvocateClerkSearchResponse.fromJson(json);
}

/// @nodoc
mixin _$AdvocateClerkSearchResponse {
  @JsonKey(name: 'responseInfo')
  ResponseInfoSearch get responseInfo => throw _privateConstructorUsedError;
  @JsonKey(name: 'clerks')
  List<Clerk> get clerks => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $AdvocateClerkSearchResponseCopyWith<AdvocateClerkSearchResponse>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AdvocateClerkSearchResponseCopyWith<$Res> {
  factory $AdvocateClerkSearchResponseCopyWith(
          AdvocateClerkSearchResponse value,
          $Res Function(AdvocateClerkSearchResponse) then) =
      _$AdvocateClerkSearchResponseCopyWithImpl<$Res,
          AdvocateClerkSearchResponse>;
  @useResult
  $Res call(
      {@JsonKey(name: 'responseInfo') ResponseInfoSearch responseInfo,
      @JsonKey(name: 'clerks') List<Clerk> clerks});

  $ResponseInfoSearchCopyWith<$Res> get responseInfo;
}

/// @nodoc
class _$AdvocateClerkSearchResponseCopyWithImpl<$Res,
        $Val extends AdvocateClerkSearchResponse>
    implements $AdvocateClerkSearchResponseCopyWith<$Res> {
  _$AdvocateClerkSearchResponseCopyWithImpl(this._value, this._then);

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
abstract class _$$AdvocateClerkSearchResponseImplCopyWith<$Res>
    implements $AdvocateClerkSearchResponseCopyWith<$Res> {
  factory _$$AdvocateClerkSearchResponseImplCopyWith(
          _$AdvocateClerkSearchResponseImpl value,
          $Res Function(_$AdvocateClerkSearchResponseImpl) then) =
      __$$AdvocateClerkSearchResponseImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'responseInfo') ResponseInfoSearch responseInfo,
      @JsonKey(name: 'clerks') List<Clerk> clerks});

  @override
  $ResponseInfoSearchCopyWith<$Res> get responseInfo;
}

/// @nodoc
class __$$AdvocateClerkSearchResponseImplCopyWithImpl<$Res>
    extends _$AdvocateClerkSearchResponseCopyWithImpl<$Res,
        _$AdvocateClerkSearchResponseImpl>
    implements _$$AdvocateClerkSearchResponseImplCopyWith<$Res> {
  __$$AdvocateClerkSearchResponseImplCopyWithImpl(
      _$AdvocateClerkSearchResponseImpl _value,
      $Res Function(_$AdvocateClerkSearchResponseImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? responseInfo = null,
    Object? clerks = null,
  }) {
    return _then(_$AdvocateClerkSearchResponseImpl(
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
class _$AdvocateClerkSearchResponseImpl
    implements _AdvocateClerkSearchResponse {
  const _$AdvocateClerkSearchResponseImpl(
      {@JsonKey(name: 'responseInfo')
      this.responseInfo = const ResponseInfoSearch(status: ""),
      @JsonKey(name: 'clerks') final List<Clerk> clerks = const []})
      : _clerks = clerks;

  factory _$AdvocateClerkSearchResponseImpl.fromJson(
          Map<String, dynamic> json) =>
      _$$AdvocateClerkSearchResponseImplFromJson(json);

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
    return 'AdvocateClerkSearchResponse(responseInfo: $responseInfo, clerks: $clerks)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AdvocateClerkSearchResponseImpl &&
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
  _$$AdvocateClerkSearchResponseImplCopyWith<_$AdvocateClerkSearchResponseImpl>
      get copyWith => __$$AdvocateClerkSearchResponseImplCopyWithImpl<
          _$AdvocateClerkSearchResponseImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$AdvocateClerkSearchResponseImplToJson(
      this,
    );
  }
}

abstract class _AdvocateClerkSearchResponse
    implements AdvocateClerkSearchResponse {
  const factory _AdvocateClerkSearchResponse(
          {@JsonKey(name: 'responseInfo') final ResponseInfoSearch responseInfo,
          @JsonKey(name: 'clerks') final List<Clerk> clerks}) =
      _$AdvocateClerkSearchResponseImpl;

  factory _AdvocateClerkSearchResponse.fromJson(Map<String, dynamic> json) =
      _$AdvocateClerkSearchResponseImpl.fromJson;

  @override
  @JsonKey(name: 'responseInfo')
  ResponseInfoSearch get responseInfo;
  @override
  @JsonKey(name: 'clerks')
  List<Clerk> get clerks;
  @override
  @JsonKey(ignore: true)
  _$$AdvocateClerkSearchResponseImplCopyWith<_$AdvocateClerkSearchResponseImpl>
      get copyWith => throw _privateConstructorUsedError;
}
