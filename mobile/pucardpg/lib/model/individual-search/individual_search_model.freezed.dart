// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'individual_search_model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

IndividualSearchRequest _$IndividualSearchRequestFromJson(
    Map<String, dynamic> json) {
  return _IndividualSearchRequest.fromJson(json);
}

/// @nodoc
mixin _$IndividualSearchRequest {
  @JsonKey(name: 'Individual')
  IndividualSearch get individual => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $IndividualSearchRequestCopyWith<IndividualSearchRequest> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $IndividualSearchRequestCopyWith<$Res> {
  factory $IndividualSearchRequestCopyWith(IndividualSearchRequest value,
          $Res Function(IndividualSearchRequest) then) =
      _$IndividualSearchRequestCopyWithImpl<$Res, IndividualSearchRequest>;
  @useResult
  $Res call({@JsonKey(name: 'Individual') IndividualSearch individual});

  $IndividualSearchCopyWith<$Res> get individual;
}

/// @nodoc
class _$IndividualSearchRequestCopyWithImpl<$Res,
        $Val extends IndividualSearchRequest>
    implements $IndividualSearchRequestCopyWith<$Res> {
  _$IndividualSearchRequestCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? individual = null,
  }) {
    return _then(_value.copyWith(
      individual: null == individual
          ? _value.individual
          : individual // ignore: cast_nullable_to_non_nullable
              as IndividualSearch,
    ) as $Val);
  }

  @override
  @pragma('vm:prefer-inline')
  $IndividualSearchCopyWith<$Res> get individual {
    return $IndividualSearchCopyWith<$Res>(_value.individual, (value) {
      return _then(_value.copyWith(individual: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$IndividualSearchRequestImplCopyWith<$Res>
    implements $IndividualSearchRequestCopyWith<$Res> {
  factory _$$IndividualSearchRequestImplCopyWith(
          _$IndividualSearchRequestImpl value,
          $Res Function(_$IndividualSearchRequestImpl) then) =
      __$$IndividualSearchRequestImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({@JsonKey(name: 'Individual') IndividualSearch individual});

  @override
  $IndividualSearchCopyWith<$Res> get individual;
}

/// @nodoc
class __$$IndividualSearchRequestImplCopyWithImpl<$Res>
    extends _$IndividualSearchRequestCopyWithImpl<$Res,
        _$IndividualSearchRequestImpl>
    implements _$$IndividualSearchRequestImplCopyWith<$Res> {
  __$$IndividualSearchRequestImplCopyWithImpl(
      _$IndividualSearchRequestImpl _value,
      $Res Function(_$IndividualSearchRequestImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? individual = null,
  }) {
    return _then(_$IndividualSearchRequestImpl(
      individual: null == individual
          ? _value.individual
          : individual // ignore: cast_nullable_to_non_nullable
              as IndividualSearch,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$IndividualSearchRequestImpl implements _IndividualSearchRequest {
  const _$IndividualSearchRequestImpl(
      {@JsonKey(name: 'Individual') required this.individual});

  factory _$IndividualSearchRequestImpl.fromJson(Map<String, dynamic> json) =>
      _$$IndividualSearchRequestImplFromJson(json);

  @override
  @JsonKey(name: 'Individual')
  final IndividualSearch individual;

  @override
  String toString() {
    return 'IndividualSearchRequest(individual: $individual)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$IndividualSearchRequestImpl &&
            (identical(other.individual, individual) ||
                other.individual == individual));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, individual);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$IndividualSearchRequestImplCopyWith<_$IndividualSearchRequestImpl>
      get copyWith => __$$IndividualSearchRequestImplCopyWithImpl<
          _$IndividualSearchRequestImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$IndividualSearchRequestImplToJson(
      this,
    );
  }
}

abstract class _IndividualSearchRequest implements IndividualSearchRequest {
  const factory _IndividualSearchRequest(
          {@JsonKey(name: 'Individual')
          required final IndividualSearch individual}) =
      _$IndividualSearchRequestImpl;

  factory _IndividualSearchRequest.fromJson(Map<String, dynamic> json) =
      _$IndividualSearchRequestImpl.fromJson;

  @override
  @JsonKey(name: 'Individual')
  IndividualSearch get individual;
  @override
  @JsonKey(ignore: true)
  _$$IndividualSearchRequestImplCopyWith<_$IndividualSearchRequestImpl>
      get copyWith => throw _privateConstructorUsedError;
}

RequestInfoSearch _$RequestInfoSearchFromJson(Map<String, dynamic> json) {
  return _RequestInfoSearch.fromJson(json);
}

/// @nodoc
mixin _$RequestInfoSearch {
  @JsonKey(name: 'authToken')
  String get authToken => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $RequestInfoSearchCopyWith<RequestInfoSearch> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $RequestInfoSearchCopyWith<$Res> {
  factory $RequestInfoSearchCopyWith(
          RequestInfoSearch value, $Res Function(RequestInfoSearch) then) =
      _$RequestInfoSearchCopyWithImpl<$Res, RequestInfoSearch>;
  @useResult
  $Res call({@JsonKey(name: 'authToken') String authToken});
}

/// @nodoc
class _$RequestInfoSearchCopyWithImpl<$Res, $Val extends RequestInfoSearch>
    implements $RequestInfoSearchCopyWith<$Res> {
  _$RequestInfoSearchCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? authToken = null,
  }) {
    return _then(_value.copyWith(
      authToken: null == authToken
          ? _value.authToken
          : authToken // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$RequestInfoSearchImplCopyWith<$Res>
    implements $RequestInfoSearchCopyWith<$Res> {
  factory _$$RequestInfoSearchImplCopyWith(_$RequestInfoSearchImpl value,
          $Res Function(_$RequestInfoSearchImpl) then) =
      __$$RequestInfoSearchImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({@JsonKey(name: 'authToken') String authToken});
}

/// @nodoc
class __$$RequestInfoSearchImplCopyWithImpl<$Res>
    extends _$RequestInfoSearchCopyWithImpl<$Res, _$RequestInfoSearchImpl>
    implements _$$RequestInfoSearchImplCopyWith<$Res> {
  __$$RequestInfoSearchImplCopyWithImpl(_$RequestInfoSearchImpl _value,
      $Res Function(_$RequestInfoSearchImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? authToken = null,
  }) {
    return _then(_$RequestInfoSearchImpl(
      authToken: null == authToken
          ? _value.authToken
          : authToken // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$RequestInfoSearchImpl implements _RequestInfoSearch {
  const _$RequestInfoSearchImpl(
      {@JsonKey(name: 'authToken') required this.authToken});

  factory _$RequestInfoSearchImpl.fromJson(Map<String, dynamic> json) =>
      _$$RequestInfoSearchImplFromJson(json);

  @override
  @JsonKey(name: 'authToken')
  final String authToken;

  @override
  String toString() {
    return 'RequestInfoSearch(authToken: $authToken)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$RequestInfoSearchImpl &&
            (identical(other.authToken, authToken) ||
                other.authToken == authToken));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, authToken);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$RequestInfoSearchImplCopyWith<_$RequestInfoSearchImpl> get copyWith =>
      __$$RequestInfoSearchImplCopyWithImpl<_$RequestInfoSearchImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$RequestInfoSearchImplToJson(
      this,
    );
  }
}

abstract class _RequestInfoSearch implements RequestInfoSearch {
  const factory _RequestInfoSearch(
          {@JsonKey(name: 'authToken') required final String authToken}) =
      _$RequestInfoSearchImpl;

  factory _RequestInfoSearch.fromJson(Map<String, dynamic> json) =
      _$RequestInfoSearchImpl.fromJson;

  @override
  @JsonKey(name: 'authToken')
  String get authToken;
  @override
  @JsonKey(ignore: true)
  _$$RequestInfoSearchImplCopyWith<_$RequestInfoSearchImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

IndividualSearch _$IndividualSearchFromJson(Map<String, dynamic> json) {
  return _Individual.fromJson(json);
}

/// @nodoc
mixin _$IndividualSearch {
  @JsonKey(name: 'userUuid')
  List<String> get userUuid => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $IndividualSearchCopyWith<IndividualSearch> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $IndividualSearchCopyWith<$Res> {
  factory $IndividualSearchCopyWith(
          IndividualSearch value, $Res Function(IndividualSearch) then) =
      _$IndividualSearchCopyWithImpl<$Res, IndividualSearch>;
  @useResult
  $Res call({@JsonKey(name: 'userUuid') List<String> userUuid});
}

/// @nodoc
class _$IndividualSearchCopyWithImpl<$Res, $Val extends IndividualSearch>
    implements $IndividualSearchCopyWith<$Res> {
  _$IndividualSearchCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? userUuid = null,
  }) {
    return _then(_value.copyWith(
      userUuid: null == userUuid
          ? _value.userUuid
          : userUuid // ignore: cast_nullable_to_non_nullable
              as List<String>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$IndividualImplCopyWith<$Res>
    implements $IndividualSearchCopyWith<$Res> {
  factory _$$IndividualImplCopyWith(
          _$IndividualImpl value, $Res Function(_$IndividualImpl) then) =
      __$$IndividualImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({@JsonKey(name: 'userUuid') List<String> userUuid});
}

/// @nodoc
class __$$IndividualImplCopyWithImpl<$Res>
    extends _$IndividualSearchCopyWithImpl<$Res, _$IndividualImpl>
    implements _$$IndividualImplCopyWith<$Res> {
  __$$IndividualImplCopyWithImpl(
      _$IndividualImpl _value, $Res Function(_$IndividualImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? userUuid = null,
  }) {
    return _then(_$IndividualImpl(
      userUuid: null == userUuid
          ? _value._userUuid
          : userUuid // ignore: cast_nullable_to_non_nullable
              as List<String>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$IndividualImpl implements _Individual {
  const _$IndividualImpl(
      {@JsonKey(name: 'userUuid') required final List<String> userUuid})
      : _userUuid = userUuid;

  factory _$IndividualImpl.fromJson(Map<String, dynamic> json) =>
      _$$IndividualImplFromJson(json);

  final List<String> _userUuid;
  @override
  @JsonKey(name: 'userUuid')
  List<String> get userUuid {
    if (_userUuid is EqualUnmodifiableListView) return _userUuid;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_userUuid);
  }

  @override
  String toString() {
    return 'IndividualSearch(userUuid: $userUuid)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$IndividualImpl &&
            const DeepCollectionEquality().equals(other._userUuid, _userUuid));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(_userUuid));

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

abstract class _Individual implements IndividualSearch {
  const factory _Individual(
          {@JsonKey(name: 'userUuid') required final List<String> userUuid}) =
      _$IndividualImpl;

  factory _Individual.fromJson(Map<String, dynamic> json) =
      _$IndividualImpl.fromJson;

  @override
  @JsonKey(name: 'userUuid')
  List<String> get userUuid;
  @override
  @JsonKey(ignore: true)
  _$$IndividualImplCopyWith<_$IndividualImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

ResponseInfoSearch _$ResponseInfoSearchFromJson(Map<String, dynamic> json) {
  return _ResponseInfoSearch.fromJson(json);
}

/// @nodoc
mixin _$ResponseInfoSearch {
  String? get apiId =>
      throw _privateConstructorUsedError; // Make nullable since it's empty in the example JSON
  String? get ver => throw _privateConstructorUsedError; // Make nullable
  int? get ts => throw _privateConstructorUsedError; // Make nullable
  String? get resMsgId => throw _privateConstructorUsedError; // Make nullable
  String? get msgId => throw _privateConstructorUsedError; // Make nullable
  String get status => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $ResponseInfoSearchCopyWith<ResponseInfoSearch> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $ResponseInfoSearchCopyWith<$Res> {
  factory $ResponseInfoSearchCopyWith(
          ResponseInfoSearch value, $Res Function(ResponseInfoSearch) then) =
      _$ResponseInfoSearchCopyWithImpl<$Res, ResponseInfoSearch>;
  @useResult
  $Res call(
      {String? apiId,
      String? ver,
      int? ts,
      String? resMsgId,
      String? msgId,
      String status});
}

/// @nodoc
class _$ResponseInfoSearchCopyWithImpl<$Res, $Val extends ResponseInfoSearch>
    implements $ResponseInfoSearchCopyWith<$Res> {
  _$ResponseInfoSearchCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? apiId = freezed,
    Object? ver = freezed,
    Object? ts = freezed,
    Object? resMsgId = freezed,
    Object? msgId = freezed,
    Object? status = null,
  }) {
    return _then(_value.copyWith(
      apiId: freezed == apiId
          ? _value.apiId
          : apiId // ignore: cast_nullable_to_non_nullable
              as String?,
      ver: freezed == ver
          ? _value.ver
          : ver // ignore: cast_nullable_to_non_nullable
              as String?,
      ts: freezed == ts
          ? _value.ts
          : ts // ignore: cast_nullable_to_non_nullable
              as int?,
      resMsgId: freezed == resMsgId
          ? _value.resMsgId
          : resMsgId // ignore: cast_nullable_to_non_nullable
              as String?,
      msgId: freezed == msgId
          ? _value.msgId
          : msgId // ignore: cast_nullable_to_non_nullable
              as String?,
      status: null == status
          ? _value.status
          : status // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$ResponseInfoSearchImplCopyWith<$Res>
    implements $ResponseInfoSearchCopyWith<$Res> {
  factory _$$ResponseInfoSearchImplCopyWith(_$ResponseInfoSearchImpl value,
          $Res Function(_$ResponseInfoSearchImpl) then) =
      __$$ResponseInfoSearchImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String? apiId,
      String? ver,
      int? ts,
      String? resMsgId,
      String? msgId,
      String status});
}

/// @nodoc
class __$$ResponseInfoSearchImplCopyWithImpl<$Res>
    extends _$ResponseInfoSearchCopyWithImpl<$Res, _$ResponseInfoSearchImpl>
    implements _$$ResponseInfoSearchImplCopyWith<$Res> {
  __$$ResponseInfoSearchImplCopyWithImpl(_$ResponseInfoSearchImpl _value,
      $Res Function(_$ResponseInfoSearchImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? apiId = freezed,
    Object? ver = freezed,
    Object? ts = freezed,
    Object? resMsgId = freezed,
    Object? msgId = freezed,
    Object? status = null,
  }) {
    return _then(_$ResponseInfoSearchImpl(
      apiId: freezed == apiId
          ? _value.apiId
          : apiId // ignore: cast_nullable_to_non_nullable
              as String?,
      ver: freezed == ver
          ? _value.ver
          : ver // ignore: cast_nullable_to_non_nullable
              as String?,
      ts: freezed == ts
          ? _value.ts
          : ts // ignore: cast_nullable_to_non_nullable
              as int?,
      resMsgId: freezed == resMsgId
          ? _value.resMsgId
          : resMsgId // ignore: cast_nullable_to_non_nullable
              as String?,
      msgId: freezed == msgId
          ? _value.msgId
          : msgId // ignore: cast_nullable_to_non_nullable
              as String?,
      status: null == status
          ? _value.status
          : status // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$ResponseInfoSearchImpl implements _ResponseInfoSearch {
  const _$ResponseInfoSearchImpl(
      {this.apiId,
      this.ver,
      this.ts,
      this.resMsgId,
      this.msgId,
      required this.status});

  factory _$ResponseInfoSearchImpl.fromJson(Map<String, dynamic> json) =>
      _$$ResponseInfoSearchImplFromJson(json);

  @override
  final String? apiId;
// Make nullable since it's empty in the example JSON
  @override
  final String? ver;
// Make nullable
  @override
  final int? ts;
// Make nullable
  @override
  final String? resMsgId;
// Make nullable
  @override
  final String? msgId;
// Make nullable
  @override
  final String status;

  @override
  String toString() {
    return 'ResponseInfoSearch(apiId: $apiId, ver: $ver, ts: $ts, resMsgId: $resMsgId, msgId: $msgId, status: $status)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ResponseInfoSearchImpl &&
            (identical(other.apiId, apiId) || other.apiId == apiId) &&
            (identical(other.ver, ver) || other.ver == ver) &&
            (identical(other.ts, ts) || other.ts == ts) &&
            (identical(other.resMsgId, resMsgId) ||
                other.resMsgId == resMsgId) &&
            (identical(other.msgId, msgId) || other.msgId == msgId) &&
            (identical(other.status, status) || other.status == status));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, apiId, ver, ts, resMsgId, msgId, status);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$ResponseInfoSearchImplCopyWith<_$ResponseInfoSearchImpl> get copyWith =>
      __$$ResponseInfoSearchImplCopyWithImpl<_$ResponseInfoSearchImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$ResponseInfoSearchImplToJson(
      this,
    );
  }
}

abstract class _ResponseInfoSearch implements ResponseInfoSearch {
  const factory _ResponseInfoSearch(
      {final String? apiId,
      final String? ver,
      final int? ts,
      final String? resMsgId,
      final String? msgId,
      required final String status}) = _$ResponseInfoSearchImpl;

  factory _ResponseInfoSearch.fromJson(Map<String, dynamic> json) =
      _$ResponseInfoSearchImpl.fromJson;

  @override
  String? get apiId;
  @override // Make nullable since it's empty in the example JSON
  String? get ver;
  @override // Make nullable
  int? get ts;
  @override // Make nullable
  String? get resMsgId;
  @override // Make nullable
  String? get msgId;
  @override // Make nullable
  String get status;
  @override
  @JsonKey(ignore: true)
  _$$ResponseInfoSearchImplCopyWith<_$ResponseInfoSearchImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

IndividualSearchResponse _$IndividualSearchResponseFromJson(
    Map<String, dynamic> json) {
  return _IndividualSearchResponse.fromJson(json);
}

/// @nodoc
mixin _$IndividualSearchResponse {
  @JsonKey(name: 'ResponseInfo')
  ResponseInfoSearch get responseInfo => throw _privateConstructorUsedError;
  @JsonKey(name: 'Individual')
  List<Individual> get individual => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $IndividualSearchResponseCopyWith<IndividualSearchResponse> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $IndividualSearchResponseCopyWith<$Res> {
  factory $IndividualSearchResponseCopyWith(IndividualSearchResponse value,
          $Res Function(IndividualSearchResponse) then) =
      _$IndividualSearchResponseCopyWithImpl<$Res, IndividualSearchResponse>;
  @useResult
  $Res call(
      {@JsonKey(name: 'ResponseInfo') ResponseInfoSearch responseInfo,
      @JsonKey(name: 'Individual') List<Individual> individual});

  $ResponseInfoSearchCopyWith<$Res> get responseInfo;
}

/// @nodoc
class _$IndividualSearchResponseCopyWithImpl<$Res,
        $Val extends IndividualSearchResponse>
    implements $IndividualSearchResponseCopyWith<$Res> {
  _$IndividualSearchResponseCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? responseInfo = null,
    Object? individual = null,
  }) {
    return _then(_value.copyWith(
      responseInfo: null == responseInfo
          ? _value.responseInfo
          : responseInfo // ignore: cast_nullable_to_non_nullable
              as ResponseInfoSearch,
      individual: null == individual
          ? _value.individual
          : individual // ignore: cast_nullable_to_non_nullable
              as List<Individual>,
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
abstract class _$$IndividualSearchResponseImplCopyWith<$Res>
    implements $IndividualSearchResponseCopyWith<$Res> {
  factory _$$IndividualSearchResponseImplCopyWith(
          _$IndividualSearchResponseImpl value,
          $Res Function(_$IndividualSearchResponseImpl) then) =
      __$$IndividualSearchResponseImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'ResponseInfo') ResponseInfoSearch responseInfo,
      @JsonKey(name: 'Individual') List<Individual> individual});

  @override
  $ResponseInfoSearchCopyWith<$Res> get responseInfo;
}

/// @nodoc
class __$$IndividualSearchResponseImplCopyWithImpl<$Res>
    extends _$IndividualSearchResponseCopyWithImpl<$Res,
        _$IndividualSearchResponseImpl>
    implements _$$IndividualSearchResponseImplCopyWith<$Res> {
  __$$IndividualSearchResponseImplCopyWithImpl(
      _$IndividualSearchResponseImpl _value,
      $Res Function(_$IndividualSearchResponseImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? responseInfo = null,
    Object? individual = null,
  }) {
    return _then(_$IndividualSearchResponseImpl(
      responseInfo: null == responseInfo
          ? _value.responseInfo
          : responseInfo // ignore: cast_nullable_to_non_nullable
              as ResponseInfoSearch,
      individual: null == individual
          ? _value._individual
          : individual // ignore: cast_nullable_to_non_nullable
              as List<Individual>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$IndividualSearchResponseImpl implements _IndividualSearchResponse {
  const _$IndividualSearchResponseImpl(
      {@JsonKey(name: 'ResponseInfo') required this.responseInfo,
      @JsonKey(name: 'Individual') required final List<Individual> individual})
      : _individual = individual;

  factory _$IndividualSearchResponseImpl.fromJson(Map<String, dynamic> json) =>
      _$$IndividualSearchResponseImplFromJson(json);

  @override
  @JsonKey(name: 'ResponseInfo')
  final ResponseInfoSearch responseInfo;
  final List<Individual> _individual;
  @override
  @JsonKey(name: 'Individual')
  List<Individual> get individual {
    if (_individual is EqualUnmodifiableListView) return _individual;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_individual);
  }

  @override
  String toString() {
    return 'IndividualSearchResponse(responseInfo: $responseInfo, individual: $individual)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$IndividualSearchResponseImpl &&
            (identical(other.responseInfo, responseInfo) ||
                other.responseInfo == responseInfo) &&
            const DeepCollectionEquality()
                .equals(other._individual, _individual));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, responseInfo,
      const DeepCollectionEquality().hash(_individual));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$IndividualSearchResponseImplCopyWith<_$IndividualSearchResponseImpl>
      get copyWith => __$$IndividualSearchResponseImplCopyWithImpl<
          _$IndividualSearchResponseImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$IndividualSearchResponseImplToJson(
      this,
    );
  }
}

abstract class _IndividualSearchResponse implements IndividualSearchResponse {
  const factory _IndividualSearchResponse(
          {@JsonKey(name: 'ResponseInfo')
          required final ResponseInfoSearch responseInfo,
          @JsonKey(name: 'Individual')
          required final List<Individual> individual}) =
      _$IndividualSearchResponseImpl;

  factory _IndividualSearchResponse.fromJson(Map<String, dynamic> json) =
      _$IndividualSearchResponseImpl.fromJson;

  @override
  @JsonKey(name: 'ResponseInfo')
  ResponseInfoSearch get responseInfo;
  @override
  @JsonKey(name: 'Individual')
  List<Individual> get individual;
  @override
  @JsonKey(ignore: true)
  _$$IndividualSearchResponseImplCopyWith<_$IndividualSearchResponseImpl>
      get copyWith => throw _privateConstructorUsedError;
}
