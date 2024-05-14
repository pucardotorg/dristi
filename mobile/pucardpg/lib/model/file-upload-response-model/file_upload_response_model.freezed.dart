// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'file_upload_response_model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

FileUploadResponseModel _$FileUploadResponseModelFromJson(
    Map<String, dynamic> json) {
  return _FileUploadResponseModel.fromJson(json);
}

/// @nodoc
mixin _$FileUploadResponseModel {
  @JsonKey(name: 'files')
  List<FirestoreFileModel> get files => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $FileUploadResponseModelCopyWith<FileUploadResponseModel> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $FileUploadResponseModelCopyWith<$Res> {
  factory $FileUploadResponseModelCopyWith(FileUploadResponseModel value,
          $Res Function(FileUploadResponseModel) then) =
      _$FileUploadResponseModelCopyWithImpl<$Res, FileUploadResponseModel>;
  @useResult
  $Res call({@JsonKey(name: 'files') List<FirestoreFileModel> files});
}

/// @nodoc
class _$FileUploadResponseModelCopyWithImpl<$Res,
        $Val extends FileUploadResponseModel>
    implements $FileUploadResponseModelCopyWith<$Res> {
  _$FileUploadResponseModelCopyWithImpl(this._value, this._then);

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
              as List<FirestoreFileModel>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$FileUploadResponseModelImplCopyWith<$Res>
    implements $FileUploadResponseModelCopyWith<$Res> {
  factory _$$FileUploadResponseModelImplCopyWith(
          _$FileUploadResponseModelImpl value,
          $Res Function(_$FileUploadResponseModelImpl) then) =
      __$$FileUploadResponseModelImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({@JsonKey(name: 'files') List<FirestoreFileModel> files});
}

/// @nodoc
class __$$FileUploadResponseModelImplCopyWithImpl<$Res>
    extends _$FileUploadResponseModelCopyWithImpl<$Res,
        _$FileUploadResponseModelImpl>
    implements _$$FileUploadResponseModelImplCopyWith<$Res> {
  __$$FileUploadResponseModelImplCopyWithImpl(
      _$FileUploadResponseModelImpl _value,
      $Res Function(_$FileUploadResponseModelImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? files = null,
  }) {
    return _then(_$FileUploadResponseModelImpl(
      files: null == files
          ? _value._files
          : files // ignore: cast_nullable_to_non_nullable
              as List<FirestoreFileModel>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$FileUploadResponseModelImpl implements _FileUploadResponseModel {
  const _$FileUploadResponseModelImpl(
      {@JsonKey(name: 'files') final List<FirestoreFileModel> files = const []})
      : _files = files;

  factory _$FileUploadResponseModelImpl.fromJson(Map<String, dynamic> json) =>
      _$$FileUploadResponseModelImplFromJson(json);

  final List<FirestoreFileModel> _files;
  @override
  @JsonKey(name: 'files')
  List<FirestoreFileModel> get files {
    if (_files is EqualUnmodifiableListView) return _files;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_files);
  }

  @override
  String toString() {
    return 'FileUploadResponseModel(files: $files)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$FileUploadResponseModelImpl &&
            const DeepCollectionEquality().equals(other._files, _files));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(_files));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$FileUploadResponseModelImplCopyWith<_$FileUploadResponseModelImpl>
      get copyWith => __$$FileUploadResponseModelImplCopyWithImpl<
          _$FileUploadResponseModelImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$FileUploadResponseModelImplToJson(
      this,
    );
  }
}

abstract class _FileUploadResponseModel implements FileUploadResponseModel {
  const factory _FileUploadResponseModel(
          {@JsonKey(name: 'files') final List<FirestoreFileModel> files}) =
      _$FileUploadResponseModelImpl;

  factory _FileUploadResponseModel.fromJson(Map<String, dynamic> json) =
      _$FileUploadResponseModelImpl.fromJson;

  @override
  @JsonKey(name: 'files')
  List<FirestoreFileModel> get files;
  @override
  @JsonKey(ignore: true)
  _$$FileUploadResponseModelImplCopyWith<_$FileUploadResponseModelImpl>
      get copyWith => throw _privateConstructorUsedError;
}

FirestoreFileModel _$FirestoreFileModelFromJson(Map<String, dynamic> json) {
  return _FirestoreFileModel.fromJson(json);
}

/// @nodoc
mixin _$FirestoreFileModel {
  @JsonKey(name: 'fileStoreId')
  String? get fileStoreId => throw _privateConstructorUsedError;
  @JsonKey(name: 'tenantId')
  String? get tenantId => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $FirestoreFileModelCopyWith<FirestoreFileModel> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $FirestoreFileModelCopyWith<$Res> {
  factory $FirestoreFileModelCopyWith(
          FirestoreFileModel value, $Res Function(FirestoreFileModel) then) =
      _$FirestoreFileModelCopyWithImpl<$Res, FirestoreFileModel>;
  @useResult
  $Res call(
      {@JsonKey(name: 'fileStoreId') String? fileStoreId,
      @JsonKey(name: 'tenantId') String? tenantId});
}

/// @nodoc
class _$FirestoreFileModelCopyWithImpl<$Res, $Val extends FirestoreFileModel>
    implements $FirestoreFileModelCopyWith<$Res> {
  _$FirestoreFileModelCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fileStoreId = freezed,
    Object? tenantId = freezed,
  }) {
    return _then(_value.copyWith(
      fileStoreId: freezed == fileStoreId
          ? _value.fileStoreId
          : fileStoreId // ignore: cast_nullable_to_non_nullable
              as String?,
      tenantId: freezed == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String?,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$FirestoreFileModelImplCopyWith<$Res>
    implements $FirestoreFileModelCopyWith<$Res> {
  factory _$$FirestoreFileModelImplCopyWith(_$FirestoreFileModelImpl value,
          $Res Function(_$FirestoreFileModelImpl) then) =
      __$$FirestoreFileModelImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'fileStoreId') String? fileStoreId,
      @JsonKey(name: 'tenantId') String? tenantId});
}

/// @nodoc
class __$$FirestoreFileModelImplCopyWithImpl<$Res>
    extends _$FirestoreFileModelCopyWithImpl<$Res, _$FirestoreFileModelImpl>
    implements _$$FirestoreFileModelImplCopyWith<$Res> {
  __$$FirestoreFileModelImplCopyWithImpl(_$FirestoreFileModelImpl _value,
      $Res Function(_$FirestoreFileModelImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fileStoreId = freezed,
    Object? tenantId = freezed,
  }) {
    return _then(_$FirestoreFileModelImpl(
      fileStoreId: freezed == fileStoreId
          ? _value.fileStoreId
          : fileStoreId // ignore: cast_nullable_to_non_nullable
              as String?,
      tenantId: freezed == tenantId
          ? _value.tenantId
          : tenantId // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$FirestoreFileModelImpl implements _FirestoreFileModel {
  const _$FirestoreFileModelImpl(
      {@JsonKey(name: 'fileStoreId') this.fileStoreId,
      @JsonKey(name: 'tenantId') this.tenantId});

  factory _$FirestoreFileModelImpl.fromJson(Map<String, dynamic> json) =>
      _$$FirestoreFileModelImplFromJson(json);

  @override
  @JsonKey(name: 'fileStoreId')
  final String? fileStoreId;
  @override
  @JsonKey(name: 'tenantId')
  final String? tenantId;

  @override
  String toString() {
    return 'FirestoreFileModel(fileStoreId: $fileStoreId, tenantId: $tenantId)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$FirestoreFileModelImpl &&
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
  _$$FirestoreFileModelImplCopyWith<_$FirestoreFileModelImpl> get copyWith =>
      __$$FirestoreFileModelImplCopyWithImpl<_$FirestoreFileModelImpl>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$FirestoreFileModelImplToJson(
      this,
    );
  }
}

abstract class _FirestoreFileModel implements FirestoreFileModel {
  const factory _FirestoreFileModel(
          {@JsonKey(name: 'fileStoreId') final String? fileStoreId,
          @JsonKey(name: 'tenantId') final String? tenantId}) =
      _$FirestoreFileModelImpl;

  factory _FirestoreFileModel.fromJson(Map<String, dynamic> json) =
      _$FirestoreFileModelImpl.fromJson;

  @override
  @JsonKey(name: 'fileStoreId')
  String? get fileStoreId;
  @override
  @JsonKey(name: 'tenantId')
  String? get tenantId;
  @override
  @JsonKey(ignore: true)
  _$$FirestoreFileModelImplCopyWith<_$FirestoreFileModelImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
