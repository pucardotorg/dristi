// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'filestore_model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

FileStoreResponse _$FileStoreResponseFromJson(Map<String, dynamic> json) {
  return _FileStoreResponse.fromJson(json);
}

/// @nodoc
mixin _$FileStoreResponse {
  @JsonKey(name: 'fileStoreIds')
  List<Url> get fileStoreIds => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $FileStoreResponseCopyWith<FileStoreResponse> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $FileStoreResponseCopyWith<$Res> {
  factory $FileStoreResponseCopyWith(
          FileStoreResponse value, $Res Function(FileStoreResponse) then) =
      _$FileStoreResponseCopyWithImpl<$Res, FileStoreResponse>;
  @useResult
  $Res call({@JsonKey(name: 'fileStoreIds') List<Url> fileStoreIds});
}

/// @nodoc
class _$FileStoreResponseCopyWithImpl<$Res, $Val extends FileStoreResponse>
    implements $FileStoreResponseCopyWith<$Res> {
  _$FileStoreResponseCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fileStoreIds = null,
  }) {
    return _then(_value.copyWith(
      fileStoreIds: null == fileStoreIds
          ? _value.fileStoreIds
          : fileStoreIds // ignore: cast_nullable_to_non_nullable
              as List<Url>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$FileStoreResponseImplCopyWith<$Res>
    implements $FileStoreResponseCopyWith<$Res> {
  factory _$$FileStoreResponseImplCopyWith(_$FileStoreResponseImpl value,
          $Res Function(_$FileStoreResponseImpl) then) =
      __$$FileStoreResponseImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({@JsonKey(name: 'fileStoreIds') List<Url> fileStoreIds});
}

/// @nodoc
class __$$FileStoreResponseImplCopyWithImpl<$Res>
    extends _$FileStoreResponseCopyWithImpl<$Res, _$FileStoreResponseImpl>
    implements _$$FileStoreResponseImplCopyWith<$Res> {
  __$$FileStoreResponseImplCopyWithImpl(_$FileStoreResponseImpl _value,
      $Res Function(_$FileStoreResponseImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fileStoreIds = null,
  }) {
    return _then(_$FileStoreResponseImpl(
      fileStoreIds: null == fileStoreIds
          ? _value._fileStoreIds
          : fileStoreIds // ignore: cast_nullable_to_non_nullable
              as List<Url>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$FileStoreResponseImpl implements _FileStoreResponse {
  const _$FileStoreResponseImpl(
      {@JsonKey(name: 'fileStoreIds') required final List<Url> fileStoreIds})
      : _fileStoreIds = fileStoreIds;

  factory _$FileStoreResponseImpl.fromJson(Map<String, dynamic> json) =>
      _$$FileStoreResponseImplFromJson(json);

  final List<Url> _fileStoreIds;
  @override
  @JsonKey(name: 'fileStoreIds')
  List<Url> get fileStoreIds {
    if (_fileStoreIds is EqualUnmodifiableListView) return _fileStoreIds;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_fileStoreIds);
  }

  @override
  String toString() {
    return 'FileStoreResponse(fileStoreIds: $fileStoreIds)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$FileStoreResponseImpl &&
            const DeepCollectionEquality()
                .equals(other._fileStoreIds, _fileStoreIds));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(
      runtimeType, const DeepCollectionEquality().hash(_fileStoreIds));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$FileStoreResponseImplCopyWith<_$FileStoreResponseImpl> get copyWith =>
      __$$FileStoreResponseImplCopyWithImpl<_$FileStoreResponseImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$FileStoreResponseImplToJson(
      this,
    );
  }
}

abstract class _FileStoreResponse implements FileStoreResponse {
  const factory _FileStoreResponse(
      {@JsonKey(name: 'fileStoreIds')
      required final List<Url> fileStoreIds}) = _$FileStoreResponseImpl;

  factory _FileStoreResponse.fromJson(Map<String, dynamic> json) =
      _$FileStoreResponseImpl.fromJson;

  @override
  @JsonKey(name: 'fileStoreIds')
  List<Url> get fileStoreIds;
  @override
  @JsonKey(ignore: true)
  _$$FileStoreResponseImplCopyWith<_$FileStoreResponseImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

Url _$UrlFromJson(Map<String, dynamic> json) {
  return _Url.fromJson(json);
}

/// @nodoc
mixin _$Url {
  @JsonKey(name: 'id')
  String? get id => throw _privateConstructorUsedError;
  @JsonKey(name: 'url')
  String? get url => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $UrlCopyWith<Url> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $UrlCopyWith<$Res> {
  factory $UrlCopyWith(Url value, $Res Function(Url) then) =
      _$UrlCopyWithImpl<$Res, Url>;
  @useResult
  $Res call(
      {@JsonKey(name: 'id') String? id, @JsonKey(name: 'url') String? url});
}

/// @nodoc
class _$UrlCopyWithImpl<$Res, $Val extends Url> implements $UrlCopyWith<$Res> {
  _$UrlCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = freezed,
    Object? url = freezed,
  }) {
    return _then(_value.copyWith(
      id: freezed == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String?,
      url: freezed == url
          ? _value.url
          : url // ignore: cast_nullable_to_non_nullable
              as String?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$UrlImplCopyWith<$Res> implements $UrlCopyWith<$Res> {
  factory _$$UrlImplCopyWith(_$UrlImpl value, $Res Function(_$UrlImpl) then) =
      __$$UrlImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'id') String? id, @JsonKey(name: 'url') String? url});
}

/// @nodoc
class __$$UrlImplCopyWithImpl<$Res> extends _$UrlCopyWithImpl<$Res, _$UrlImpl>
    implements _$$UrlImplCopyWith<$Res> {
  __$$UrlImplCopyWithImpl(_$UrlImpl _value, $Res Function(_$UrlImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = freezed,
    Object? url = freezed,
  }) {
    return _then(_$UrlImpl(
      id: freezed == id
          ? _value.id
          : id // ignore: cast_nullable_to_non_nullable
              as String?,
      url: freezed == url
          ? _value.url
          : url // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$UrlImpl implements _Url {
  const _$UrlImpl(
      {@JsonKey(name: 'id') required this.id,
      @JsonKey(name: 'url') required this.url});

  factory _$UrlImpl.fromJson(Map<String, dynamic> json) =>
      _$$UrlImplFromJson(json);

  @override
  @JsonKey(name: 'id')
  final String? id;
  @override
  @JsonKey(name: 'url')
  final String? url;

  @override
  String toString() {
    return 'Url(id: $id, url: $url)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$UrlImpl &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.url, url) || other.url == url));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, id, url);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$UrlImplCopyWith<_$UrlImpl> get copyWith =>
      __$$UrlImplCopyWithImpl<_$UrlImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$UrlImplToJson(
      this,
    );
  }
}

abstract class _Url implements Url {
  const factory _Url(
      {@JsonKey(name: 'id') required final String? id,
      @JsonKey(name: 'url') required final String? url}) = _$UrlImpl;

  factory _Url.fromJson(Map<String, dynamic> json) = _$UrlImpl.fromJson;

  @override
  @JsonKey(name: 'id')
  String? get id;
  @override
  @JsonKey(name: 'url')
  String? get url;
  @override
  @JsonKey(ignore: true)
  _$$UrlImplCopyWith<_$UrlImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

FileUploadResponse _$FileUploadResponseFromJson(Map<String, dynamic> json) {
  return _FileUploadResponse.fromJson(json);
}

/// @nodoc
mixin _$FileUploadResponse {
  @JsonKey(name: 'files')
  List<FileStoreId> get files => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $FileUploadResponseCopyWith<FileUploadResponse> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $FileUploadResponseCopyWith<$Res> {
  factory $FileUploadResponseCopyWith(
          FileUploadResponse value, $Res Function(FileUploadResponse) then) =
      _$FileUploadResponseCopyWithImpl<$Res, FileUploadResponse>;
  @useResult
  $Res call({@JsonKey(name: 'files') List<FileStoreId> files});
}

/// @nodoc
class _$FileUploadResponseCopyWithImpl<$Res, $Val extends FileUploadResponse>
    implements $FileUploadResponseCopyWith<$Res> {
  _$FileUploadResponseCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? files = null,
  }) {
    return _then(_value.copyWith(
      files: null == files
          ? _value.files
          : files // ignore: cast_nullable_to_non_nullable
              as List<FileStoreId>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$FileUploadResponseImplCopyWith<$Res>
    implements $FileUploadResponseCopyWith<$Res> {
  factory _$$FileUploadResponseImplCopyWith(_$FileUploadResponseImpl value,
          $Res Function(_$FileUploadResponseImpl) then) =
      __$$FileUploadResponseImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({@JsonKey(name: 'files') List<FileStoreId> files});
}

/// @nodoc
class __$$FileUploadResponseImplCopyWithImpl<$Res>
    extends _$FileUploadResponseCopyWithImpl<$Res, _$FileUploadResponseImpl>
    implements _$$FileUploadResponseImplCopyWith<$Res> {
  __$$FileUploadResponseImplCopyWithImpl(_$FileUploadResponseImpl _value,
      $Res Function(_$FileUploadResponseImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? files = null,
  }) {
    return _then(_$FileUploadResponseImpl(
      files: null == files
          ? _value._files
          : files // ignore: cast_nullable_to_non_nullable
              as List<FileStoreId>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$FileUploadResponseImpl implements _FileUploadResponse {
  const _$FileUploadResponseImpl(
      {@JsonKey(name: 'files') required final List<FileStoreId> files})
      : _files = files;

  factory _$FileUploadResponseImpl.fromJson(Map<String, dynamic> json) =>
      _$$FileUploadResponseImplFromJson(json);

  final List<FileStoreId> _files;
  @override
  @JsonKey(name: 'files')
  List<FileStoreId> get files {
    if (_files is EqualUnmodifiableListView) return _files;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_files);
  }

  @override
  String toString() {
    return 'FileUploadResponse(files: $files)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$FileUploadResponseImpl &&
            const DeepCollectionEquality().equals(other._files, _files));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(_files));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$FileUploadResponseImplCopyWith<_$FileUploadResponseImpl> get copyWith =>
      __$$FileUploadResponseImplCopyWithImpl<_$FileUploadResponseImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$FileUploadResponseImplToJson(
      this,
    );
  }
}

abstract class _FileUploadResponse implements FileUploadResponse {
  const factory _FileUploadResponse(
          {@JsonKey(name: 'files') required final List<FileStoreId> files}) =
      _$FileUploadResponseImpl;

  factory _FileUploadResponse.fromJson(Map<String, dynamic> json) =
      _$FileUploadResponseImpl.fromJson;

  @override
  @JsonKey(name: 'files')
  List<FileStoreId> get files;
  @override
  @JsonKey(ignore: true)
  _$$FileUploadResponseImplCopyWith<_$FileUploadResponseImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

FileStoreId _$FileStoreIdFromJson(Map<String, dynamic> json) {
  return _FileStoreId.fromJson(json);
}

/// @nodoc
mixin _$FileStoreId {
  @JsonKey(name: 'fileStoreId')
  String get fileStoreId => throw _privateConstructorUsedError;
  @JsonKey(name: 'tenantId')
  String get tenantId => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $FileStoreIdCopyWith<FileStoreId> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $FileStoreIdCopyWith<$Res> {
  factory $FileStoreIdCopyWith(
          FileStoreId value, $Res Function(FileStoreId) then) =
      _$FileStoreIdCopyWithImpl<$Res, FileStoreId>;
  @useResult
  $Res call(
      {@JsonKey(name: 'fileStoreId') String fileStoreId,
      @JsonKey(name: 'tenantId') String tenantId});
}

/// @nodoc
class _$FileStoreIdCopyWithImpl<$Res, $Val extends FileStoreId>
    implements $FileStoreIdCopyWith<$Res> {
  _$FileStoreIdCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fileStoreId = null,
    Object? tenantId = null,
  }) {
    return _then(_value.copyWith(
      fileStoreId: null == fileStoreId
          ? _value.fileStoreId
          : fileStoreId // ignore: cast_nullable_to_non_nullable
              as String,
      tenantId: null == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$FileStoreIdImplCopyWith<$Res>
    implements $FileStoreIdCopyWith<$Res> {
  factory _$$FileStoreIdImplCopyWith(
          _$FileStoreIdImpl value, $Res Function(_$FileStoreIdImpl) then) =
      __$$FileStoreIdImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'fileStoreId') String fileStoreId,
      @JsonKey(name: 'tenantId') String tenantId});
}

/// @nodoc
class __$$FileStoreIdImplCopyWithImpl<$Res>
    extends _$FileStoreIdCopyWithImpl<$Res, _$FileStoreIdImpl>
    implements _$$FileStoreIdImplCopyWith<$Res> {
  __$$FileStoreIdImplCopyWithImpl(
      _$FileStoreIdImpl _value, $Res Function(_$FileStoreIdImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fileStoreId = null,
    Object? tenantId = null,
  }) {
    return _then(_$FileStoreIdImpl(
      fileStoreId: null == fileStoreId
          ? _value.fileStoreId
          : fileStoreId // ignore: cast_nullable_to_non_nullable
              as String,
      tenantId: null == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$FileStoreIdImpl implements _FileStoreId {
  const _$FileStoreIdImpl(
      {@JsonKey(name: 'fileStoreId') required this.fileStoreId,
      @JsonKey(name: 'tenantId') required this.tenantId});

  factory _$FileStoreIdImpl.fromJson(Map<String, dynamic> json) =>
      _$$FileStoreIdImplFromJson(json);

  @override
  @JsonKey(name: 'fileStoreId')
  final String fileStoreId;
  @override
  @JsonKey(name: 'tenantId')
  final String tenantId;

  @override
  String toString() {
    return 'FileStoreId(fileStoreId: $fileStoreId, tenantId: $tenantId)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$FileStoreIdImpl &&
            (identical(other.fileStoreId, fileStoreId) ||
                other.fileStoreId == fileStoreId) &&
            (identical(other.tenantId, tenantId) ||
                other.tenantId == tenantId));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, fileStoreId, tenantId);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$FileStoreIdImplCopyWith<_$FileStoreIdImpl> get copyWith =>
      __$$FileStoreIdImplCopyWithImpl<_$FileStoreIdImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$FileStoreIdImplToJson(
      this,
    );
  }
}

abstract class _FileStoreId implements FileStoreId {
  const factory _FileStoreId(
          {@JsonKey(name: 'fileStoreId') required final String fileStoreId,
          @JsonKey(name: 'tenantId') required final String tenantId}) =
      _$FileStoreIdImpl;

  factory _FileStoreId.fromJson(Map<String, dynamic> json) =
      _$FileStoreIdImpl.fromJson;

  @override
  @JsonKey(name: 'fileStoreId')
  String get fileStoreId;
  @override
  @JsonKey(name: 'tenantId')
  String get tenantId;
  @override
  @JsonKey(ignore: true)
  _$$FileStoreIdImplCopyWith<_$FileStoreIdImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
