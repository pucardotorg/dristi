// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'file_picker.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

/// @nodoc
mixin _$FileEvent {
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(PlatformFile? pickedFile, String type) upload,
    required TResult Function(String? fileStoreId) get,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(PlatformFile? pickedFile, String type)? upload,
    TResult? Function(String? fileStoreId)? get,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(PlatformFile? pickedFile, String type)? upload,
    TResult Function(String? fileStoreId)? get,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_FileUploadEvent value) upload,
    required TResult Function(_FileGetEvent value) get,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_FileUploadEvent value)? upload,
    TResult? Function(_FileGetEvent value)? get,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_FileUploadEvent value)? upload,
    TResult Function(_FileGetEvent value)? get,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $FileEventCopyWith<$Res> {
  factory $FileEventCopyWith(FileEvent value, $Res Function(FileEvent) then) =
      _$FileEventCopyWithImpl<$Res, FileEvent>;
}

/// @nodoc
class _$FileEventCopyWithImpl<$Res, $Val extends FileEvent>
    implements $FileEventCopyWith<$Res> {
  _$FileEventCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;
}

/// @nodoc
abstract class _$$FileUploadEventImplCopyWith<$Res> {
  factory _$$FileUploadEventImplCopyWith(_$FileUploadEventImpl value,
          $Res Function(_$FileUploadEventImpl) then) =
      __$$FileUploadEventImplCopyWithImpl<$Res>;
  @useResult
  $Res call({PlatformFile? pickedFile, String type});
}

/// @nodoc
class __$$FileUploadEventImplCopyWithImpl<$Res>
    extends _$FileEventCopyWithImpl<$Res, _$FileUploadEventImpl>
    implements _$$FileUploadEventImplCopyWith<$Res> {
  __$$FileUploadEventImplCopyWithImpl(
      _$FileUploadEventImpl _value, $Res Function(_$FileUploadEventImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? pickedFile = freezed,
    Object? type = null,
  }) {
    return _then(_$FileUploadEventImpl(
      pickedFile: freezed == pickedFile
          ? _value.pickedFile
          : pickedFile // ignore: cast_nullable_to_non_nullable
              as PlatformFile?,
      type: null == type
          ? _value.type
          : type // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$FileUploadEventImpl implements _FileUploadEvent {
  const _$FileUploadEventImpl({this.pickedFile, required this.type});

  @override
  final PlatformFile? pickedFile;
  @override
  final String type;

  @override
  String toString() {
    return 'FileEvent.upload(pickedFile: $pickedFile, type: $type)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$FileUploadEventImpl &&
            (identical(other.pickedFile, pickedFile) ||
                other.pickedFile == pickedFile) &&
            (identical(other.type, type) || other.type == type));
  }

  @override
  int get hashCode => Object.hash(runtimeType, pickedFile, type);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$FileUploadEventImplCopyWith<_$FileUploadEventImpl> get copyWith =>
      __$$FileUploadEventImplCopyWithImpl<_$FileUploadEventImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(PlatformFile? pickedFile, String type) upload,
    required TResult Function(String? fileStoreId) get,
  }) {
    return upload(pickedFile, type);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(PlatformFile? pickedFile, String type)? upload,
    TResult? Function(String? fileStoreId)? get,
  }) {
    return upload?.call(pickedFile, type);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(PlatformFile? pickedFile, String type)? upload,
    TResult Function(String? fileStoreId)? get,
    required TResult orElse(),
  }) {
    if (upload != null) {
      return upload(pickedFile, type);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_FileUploadEvent value) upload,
    required TResult Function(_FileGetEvent value) get,
  }) {
    return upload(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_FileUploadEvent value)? upload,
    TResult? Function(_FileGetEvent value)? get,
  }) {
    return upload?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_FileUploadEvent value)? upload,
    TResult Function(_FileGetEvent value)? get,
    required TResult orElse(),
  }) {
    if (upload != null) {
      return upload(this);
    }
    return orElse();
  }
}

abstract class _FileUploadEvent implements FileEvent {
  const factory _FileUploadEvent(
      {final PlatformFile? pickedFile,
      required final String type}) = _$FileUploadEventImpl;

  PlatformFile? get pickedFile;
  String get type;
  @JsonKey(ignore: true)
  _$$FileUploadEventImplCopyWith<_$FileUploadEventImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$FileGetEventImplCopyWith<$Res> {
  factory _$$FileGetEventImplCopyWith(
          _$FileGetEventImpl value, $Res Function(_$FileGetEventImpl) then) =
      __$$FileGetEventImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String? fileStoreId});
}

/// @nodoc
class __$$FileGetEventImplCopyWithImpl<$Res>
    extends _$FileEventCopyWithImpl<$Res, _$FileGetEventImpl>
    implements _$$FileGetEventImplCopyWith<$Res> {
  __$$FileGetEventImplCopyWithImpl(
      _$FileGetEventImpl _value, $Res Function(_$FileGetEventImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fileStoreId = freezed,
  }) {
    return _then(_$FileGetEventImpl(
      fileStoreId: freezed == fileStoreId
          ? _value.fileStoreId
          : fileStoreId // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc

class _$FileGetEventImpl implements _FileGetEvent {
  const _$FileGetEventImpl({this.fileStoreId});

  @override
  final String? fileStoreId;

  @override
  String toString() {
    return 'FileEvent.get(fileStoreId: $fileStoreId)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$FileGetEventImpl &&
            (identical(other.fileStoreId, fileStoreId) ||
                other.fileStoreId == fileStoreId));
  }

  @override
  int get hashCode => Object.hash(runtimeType, fileStoreId);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$FileGetEventImplCopyWith<_$FileGetEventImpl> get copyWith =>
      __$$FileGetEventImplCopyWithImpl<_$FileGetEventImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function(PlatformFile? pickedFile, String type) upload,
    required TResult Function(String? fileStoreId) get,
  }) {
    return get(fileStoreId);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function(PlatformFile? pickedFile, String type)? upload,
    TResult? Function(String? fileStoreId)? get,
  }) {
    return get?.call(fileStoreId);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function(PlatformFile? pickedFile, String type)? upload,
    TResult Function(String? fileStoreId)? get,
    required TResult orElse(),
  }) {
    if (get != null) {
      return get(fileStoreId);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_FileUploadEvent value) upload,
    required TResult Function(_FileGetEvent value) get,
  }) {
    return get(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_FileUploadEvent value)? upload,
    TResult? Function(_FileGetEvent value)? get,
  }) {
    return get?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_FileUploadEvent value)? upload,
    TResult Function(_FileGetEvent value)? get,
    required TResult orElse(),
  }) {
    if (get != null) {
      return get(this);
    }
    return orElse();
  }
}

abstract class _FileGetEvent implements FileEvent {
  const factory _FileGetEvent({final String? fileStoreId}) = _$FileGetEventImpl;

  String? get fileStoreId;
  @JsonKey(ignore: true)
  _$$FileGetEventImplCopyWith<_$FileGetEventImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$FileState {
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() initial,
    required TResult Function(String fileStoreId) uploadIdSuccess,
    required TResult Function(String fileStoreId) uploadBarSuccess,
    required TResult Function(String errorMsg) idFailed,
    required TResult Function(String errorMsg) barFailed,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? initial,
    TResult? Function(String fileStoreId)? uploadIdSuccess,
    TResult? Function(String fileStoreId)? uploadBarSuccess,
    TResult? Function(String errorMsg)? idFailed,
    TResult? Function(String errorMsg)? barFailed,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? initial,
    TResult Function(String fileStoreId)? uploadIdSuccess,
    TResult Function(String fileStoreId)? uploadBarSuccess,
    TResult Function(String errorMsg)? idFailed,
    TResult Function(String errorMsg)? barFailed,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UploadIdSuccessState value) uploadIdSuccess,
    required TResult Function(_UploadBarSuccessState value) uploadBarSuccess,
    required TResult Function(_IdFailedState value) idFailed,
    required TResult Function(_BartFailedState value) barFailed,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UploadIdSuccessState value)? uploadIdSuccess,
    TResult? Function(_UploadBarSuccessState value)? uploadBarSuccess,
    TResult? Function(_IdFailedState value)? idFailed,
    TResult? Function(_BartFailedState value)? barFailed,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_InitialState value)? initial,
    TResult Function(_UploadIdSuccessState value)? uploadIdSuccess,
    TResult Function(_UploadBarSuccessState value)? uploadBarSuccess,
    TResult Function(_IdFailedState value)? idFailed,
    TResult Function(_BartFailedState value)? barFailed,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $FileStateCopyWith<$Res> {
  factory $FileStateCopyWith(FileState value, $Res Function(FileState) then) =
      _$FileStateCopyWithImpl<$Res, FileState>;
}

/// @nodoc
class _$FileStateCopyWithImpl<$Res, $Val extends FileState>
    implements $FileStateCopyWith<$Res> {
  _$FileStateCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;
}

/// @nodoc
abstract class _$$InitialStateImplCopyWith<$Res> {
  factory _$$InitialStateImplCopyWith(
          _$InitialStateImpl value, $Res Function(_$InitialStateImpl) then) =
      __$$InitialStateImplCopyWithImpl<$Res>;
}

/// @nodoc
class __$$InitialStateImplCopyWithImpl<$Res>
    extends _$FileStateCopyWithImpl<$Res, _$InitialStateImpl>
    implements _$$InitialStateImplCopyWith<$Res> {
  __$$InitialStateImplCopyWithImpl(
      _$InitialStateImpl _value, $Res Function(_$InitialStateImpl) _then)
      : super(_value, _then);
}

/// @nodoc

class _$InitialStateImpl implements _InitialState {
  const _$InitialStateImpl();

  @override
  String toString() {
    return 'FileState.initial()';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is _$InitialStateImpl);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() initial,
    required TResult Function(String fileStoreId) uploadIdSuccess,
    required TResult Function(String fileStoreId) uploadBarSuccess,
    required TResult Function(String errorMsg) idFailed,
    required TResult Function(String errorMsg) barFailed,
  }) {
    return initial();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? initial,
    TResult? Function(String fileStoreId)? uploadIdSuccess,
    TResult? Function(String fileStoreId)? uploadBarSuccess,
    TResult? Function(String errorMsg)? idFailed,
    TResult? Function(String errorMsg)? barFailed,
  }) {
    return initial?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? initial,
    TResult Function(String fileStoreId)? uploadIdSuccess,
    TResult Function(String fileStoreId)? uploadBarSuccess,
    TResult Function(String errorMsg)? idFailed,
    TResult Function(String errorMsg)? barFailed,
    required TResult orElse(),
  }) {
    if (initial != null) {
      return initial();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UploadIdSuccessState value) uploadIdSuccess,
    required TResult Function(_UploadBarSuccessState value) uploadBarSuccess,
    required TResult Function(_IdFailedState value) idFailed,
    required TResult Function(_BartFailedState value) barFailed,
  }) {
    return initial(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UploadIdSuccessState value)? uploadIdSuccess,
    TResult? Function(_UploadBarSuccessState value)? uploadBarSuccess,
    TResult? Function(_IdFailedState value)? idFailed,
    TResult? Function(_BartFailedState value)? barFailed,
  }) {
    return initial?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_InitialState value)? initial,
    TResult Function(_UploadIdSuccessState value)? uploadIdSuccess,
    TResult Function(_UploadBarSuccessState value)? uploadBarSuccess,
    TResult Function(_IdFailedState value)? idFailed,
    TResult Function(_BartFailedState value)? barFailed,
    required TResult orElse(),
  }) {
    if (initial != null) {
      return initial(this);
    }
    return orElse();
  }
}

abstract class _InitialState implements FileState {
  const factory _InitialState() = _$InitialStateImpl;
}

/// @nodoc
abstract class _$$UploadIdSuccessStateImplCopyWith<$Res> {
  factory _$$UploadIdSuccessStateImplCopyWith(_$UploadIdSuccessStateImpl value,
          $Res Function(_$UploadIdSuccessStateImpl) then) =
      __$$UploadIdSuccessStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String fileStoreId});
}

/// @nodoc
class __$$UploadIdSuccessStateImplCopyWithImpl<$Res>
    extends _$FileStateCopyWithImpl<$Res, _$UploadIdSuccessStateImpl>
    implements _$$UploadIdSuccessStateImplCopyWith<$Res> {
  __$$UploadIdSuccessStateImplCopyWithImpl(_$UploadIdSuccessStateImpl _value,
      $Res Function(_$UploadIdSuccessStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fileStoreId = null,
  }) {
    return _then(_$UploadIdSuccessStateImpl(
      fileStoreId: null == fileStoreId
          ? _value.fileStoreId
          : fileStoreId // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$UploadIdSuccessStateImpl implements _UploadIdSuccessState {
  const _$UploadIdSuccessStateImpl({required this.fileStoreId});

  @override
  final String fileStoreId;

  @override
  String toString() {
    return 'FileState.uploadIdSuccess(fileStoreId: $fileStoreId)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$UploadIdSuccessStateImpl &&
            (identical(other.fileStoreId, fileStoreId) ||
                other.fileStoreId == fileStoreId));
  }

  @override
  int get hashCode => Object.hash(runtimeType, fileStoreId);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$UploadIdSuccessStateImplCopyWith<_$UploadIdSuccessStateImpl>
      get copyWith =>
          __$$UploadIdSuccessStateImplCopyWithImpl<_$UploadIdSuccessStateImpl>(
              this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() initial,
    required TResult Function(String fileStoreId) uploadIdSuccess,
    required TResult Function(String fileStoreId) uploadBarSuccess,
    required TResult Function(String errorMsg) idFailed,
    required TResult Function(String errorMsg) barFailed,
  }) {
    return uploadIdSuccess(fileStoreId);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? initial,
    TResult? Function(String fileStoreId)? uploadIdSuccess,
    TResult? Function(String fileStoreId)? uploadBarSuccess,
    TResult? Function(String errorMsg)? idFailed,
    TResult? Function(String errorMsg)? barFailed,
  }) {
    return uploadIdSuccess?.call(fileStoreId);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? initial,
    TResult Function(String fileStoreId)? uploadIdSuccess,
    TResult Function(String fileStoreId)? uploadBarSuccess,
    TResult Function(String errorMsg)? idFailed,
    TResult Function(String errorMsg)? barFailed,
    required TResult orElse(),
  }) {
    if (uploadIdSuccess != null) {
      return uploadIdSuccess(fileStoreId);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UploadIdSuccessState value) uploadIdSuccess,
    required TResult Function(_UploadBarSuccessState value) uploadBarSuccess,
    required TResult Function(_IdFailedState value) idFailed,
    required TResult Function(_BartFailedState value) barFailed,
  }) {
    return uploadIdSuccess(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UploadIdSuccessState value)? uploadIdSuccess,
    TResult? Function(_UploadBarSuccessState value)? uploadBarSuccess,
    TResult? Function(_IdFailedState value)? idFailed,
    TResult? Function(_BartFailedState value)? barFailed,
  }) {
    return uploadIdSuccess?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_InitialState value)? initial,
    TResult Function(_UploadIdSuccessState value)? uploadIdSuccess,
    TResult Function(_UploadBarSuccessState value)? uploadBarSuccess,
    TResult Function(_IdFailedState value)? idFailed,
    TResult Function(_BartFailedState value)? barFailed,
    required TResult orElse(),
  }) {
    if (uploadIdSuccess != null) {
      return uploadIdSuccess(this);
    }
    return orElse();
  }
}

abstract class _UploadIdSuccessState implements FileState {
  const factory _UploadIdSuccessState({required final String fileStoreId}) =
      _$UploadIdSuccessStateImpl;

  String get fileStoreId;
  @JsonKey(ignore: true)
  _$$UploadIdSuccessStateImplCopyWith<_$UploadIdSuccessStateImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$UploadBarSuccessStateImplCopyWith<$Res> {
  factory _$$UploadBarSuccessStateImplCopyWith(
          _$UploadBarSuccessStateImpl value,
          $Res Function(_$UploadBarSuccessStateImpl) then) =
      __$$UploadBarSuccessStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String fileStoreId});
}

/// @nodoc
class __$$UploadBarSuccessStateImplCopyWithImpl<$Res>
    extends _$FileStateCopyWithImpl<$Res, _$UploadBarSuccessStateImpl>
    implements _$$UploadBarSuccessStateImplCopyWith<$Res> {
  __$$UploadBarSuccessStateImplCopyWithImpl(_$UploadBarSuccessStateImpl _value,
      $Res Function(_$UploadBarSuccessStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? fileStoreId = null,
  }) {
    return _then(_$UploadBarSuccessStateImpl(
      fileStoreId: null == fileStoreId
          ? _value.fileStoreId
          : fileStoreId // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$UploadBarSuccessStateImpl implements _UploadBarSuccessState {
  const _$UploadBarSuccessStateImpl({required this.fileStoreId});

  @override
  final String fileStoreId;

  @override
  String toString() {
    return 'FileState.uploadBarSuccess(fileStoreId: $fileStoreId)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$UploadBarSuccessStateImpl &&
            (identical(other.fileStoreId, fileStoreId) ||
                other.fileStoreId == fileStoreId));
  }

  @override
  int get hashCode => Object.hash(runtimeType, fileStoreId);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$UploadBarSuccessStateImplCopyWith<_$UploadBarSuccessStateImpl>
      get copyWith => __$$UploadBarSuccessStateImplCopyWithImpl<
          _$UploadBarSuccessStateImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() initial,
    required TResult Function(String fileStoreId) uploadIdSuccess,
    required TResult Function(String fileStoreId) uploadBarSuccess,
    required TResult Function(String errorMsg) idFailed,
    required TResult Function(String errorMsg) barFailed,
  }) {
    return uploadBarSuccess(fileStoreId);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? initial,
    TResult? Function(String fileStoreId)? uploadIdSuccess,
    TResult? Function(String fileStoreId)? uploadBarSuccess,
    TResult? Function(String errorMsg)? idFailed,
    TResult? Function(String errorMsg)? barFailed,
  }) {
    return uploadBarSuccess?.call(fileStoreId);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? initial,
    TResult Function(String fileStoreId)? uploadIdSuccess,
    TResult Function(String fileStoreId)? uploadBarSuccess,
    TResult Function(String errorMsg)? idFailed,
    TResult Function(String errorMsg)? barFailed,
    required TResult orElse(),
  }) {
    if (uploadBarSuccess != null) {
      return uploadBarSuccess(fileStoreId);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UploadIdSuccessState value) uploadIdSuccess,
    required TResult Function(_UploadBarSuccessState value) uploadBarSuccess,
    required TResult Function(_IdFailedState value) idFailed,
    required TResult Function(_BartFailedState value) barFailed,
  }) {
    return uploadBarSuccess(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UploadIdSuccessState value)? uploadIdSuccess,
    TResult? Function(_UploadBarSuccessState value)? uploadBarSuccess,
    TResult? Function(_IdFailedState value)? idFailed,
    TResult? Function(_BartFailedState value)? barFailed,
  }) {
    return uploadBarSuccess?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_InitialState value)? initial,
    TResult Function(_UploadIdSuccessState value)? uploadIdSuccess,
    TResult Function(_UploadBarSuccessState value)? uploadBarSuccess,
    TResult Function(_IdFailedState value)? idFailed,
    TResult Function(_BartFailedState value)? barFailed,
    required TResult orElse(),
  }) {
    if (uploadBarSuccess != null) {
      return uploadBarSuccess(this);
    }
    return orElse();
  }
}

abstract class _UploadBarSuccessState implements FileState {
  const factory _UploadBarSuccessState({required final String fileStoreId}) =
      _$UploadBarSuccessStateImpl;

  String get fileStoreId;
  @JsonKey(ignore: true)
  _$$UploadBarSuccessStateImplCopyWith<_$UploadBarSuccessStateImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$IdFailedStateImplCopyWith<$Res> {
  factory _$$IdFailedStateImplCopyWith(
          _$IdFailedStateImpl value, $Res Function(_$IdFailedStateImpl) then) =
      __$$IdFailedStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String errorMsg});
}

/// @nodoc
class __$$IdFailedStateImplCopyWithImpl<$Res>
    extends _$FileStateCopyWithImpl<$Res, _$IdFailedStateImpl>
    implements _$$IdFailedStateImplCopyWith<$Res> {
  __$$IdFailedStateImplCopyWithImpl(
      _$IdFailedStateImpl _value, $Res Function(_$IdFailedStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? errorMsg = null,
  }) {
    return _then(_$IdFailedStateImpl(
      errorMsg: null == errorMsg
          ? _value.errorMsg
          : errorMsg // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$IdFailedStateImpl implements _IdFailedState {
  const _$IdFailedStateImpl({required this.errorMsg});

  @override
  final String errorMsg;

  @override
  String toString() {
    return 'FileState.idFailed(errorMsg: $errorMsg)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$IdFailedStateImpl &&
            (identical(other.errorMsg, errorMsg) ||
                other.errorMsg == errorMsg));
  }

  @override
  int get hashCode => Object.hash(runtimeType, errorMsg);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$IdFailedStateImplCopyWith<_$IdFailedStateImpl> get copyWith =>
      __$$IdFailedStateImplCopyWithImpl<_$IdFailedStateImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() initial,
    required TResult Function(String fileStoreId) uploadIdSuccess,
    required TResult Function(String fileStoreId) uploadBarSuccess,
    required TResult Function(String errorMsg) idFailed,
    required TResult Function(String errorMsg) barFailed,
  }) {
    return idFailed(errorMsg);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? initial,
    TResult? Function(String fileStoreId)? uploadIdSuccess,
    TResult? Function(String fileStoreId)? uploadBarSuccess,
    TResult? Function(String errorMsg)? idFailed,
    TResult? Function(String errorMsg)? barFailed,
  }) {
    return idFailed?.call(errorMsg);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? initial,
    TResult Function(String fileStoreId)? uploadIdSuccess,
    TResult Function(String fileStoreId)? uploadBarSuccess,
    TResult Function(String errorMsg)? idFailed,
    TResult Function(String errorMsg)? barFailed,
    required TResult orElse(),
  }) {
    if (idFailed != null) {
      return idFailed(errorMsg);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UploadIdSuccessState value) uploadIdSuccess,
    required TResult Function(_UploadBarSuccessState value) uploadBarSuccess,
    required TResult Function(_IdFailedState value) idFailed,
    required TResult Function(_BartFailedState value) barFailed,
  }) {
    return idFailed(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UploadIdSuccessState value)? uploadIdSuccess,
    TResult? Function(_UploadBarSuccessState value)? uploadBarSuccess,
    TResult? Function(_IdFailedState value)? idFailed,
    TResult? Function(_BartFailedState value)? barFailed,
  }) {
    return idFailed?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_InitialState value)? initial,
    TResult Function(_UploadIdSuccessState value)? uploadIdSuccess,
    TResult Function(_UploadBarSuccessState value)? uploadBarSuccess,
    TResult Function(_IdFailedState value)? idFailed,
    TResult Function(_BartFailedState value)? barFailed,
    required TResult orElse(),
  }) {
    if (idFailed != null) {
      return idFailed(this);
    }
    return orElse();
  }
}

abstract class _IdFailedState implements FileState {
  const factory _IdFailedState({required final String errorMsg}) =
      _$IdFailedStateImpl;

  String get errorMsg;
  @JsonKey(ignore: true)
  _$$IdFailedStateImplCopyWith<_$IdFailedStateImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class _$$BartFailedStateImplCopyWith<$Res> {
  factory _$$BartFailedStateImplCopyWith(_$BartFailedStateImpl value,
          $Res Function(_$BartFailedStateImpl) then) =
      __$$BartFailedStateImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String errorMsg});
}

/// @nodoc
class __$$BartFailedStateImplCopyWithImpl<$Res>
    extends _$FileStateCopyWithImpl<$Res, _$BartFailedStateImpl>
    implements _$$BartFailedStateImplCopyWith<$Res> {
  __$$BartFailedStateImplCopyWithImpl(
      _$BartFailedStateImpl _value, $Res Function(_$BartFailedStateImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? errorMsg = null,
  }) {
    return _then(_$BartFailedStateImpl(
      errorMsg: null == errorMsg
          ? _value.errorMsg
          : errorMsg // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$BartFailedStateImpl implements _BartFailedState {
  const _$BartFailedStateImpl({required this.errorMsg});

  @override
  final String errorMsg;

  @override
  String toString() {
    return 'FileState.barFailed(errorMsg: $errorMsg)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$BartFailedStateImpl &&
            (identical(other.errorMsg, errorMsg) ||
                other.errorMsg == errorMsg));
  }

  @override
  int get hashCode => Object.hash(runtimeType, errorMsg);

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$BartFailedStateImplCopyWith<_$BartFailedStateImpl> get copyWith =>
      __$$BartFailedStateImplCopyWithImpl<_$BartFailedStateImpl>(
          this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() initial,
    required TResult Function(String fileStoreId) uploadIdSuccess,
    required TResult Function(String fileStoreId) uploadBarSuccess,
    required TResult Function(String errorMsg) idFailed,
    required TResult Function(String errorMsg) barFailed,
  }) {
    return barFailed(errorMsg);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? initial,
    TResult? Function(String fileStoreId)? uploadIdSuccess,
    TResult? Function(String fileStoreId)? uploadBarSuccess,
    TResult? Function(String errorMsg)? idFailed,
    TResult? Function(String errorMsg)? barFailed,
  }) {
    return barFailed?.call(errorMsg);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? initial,
    TResult Function(String fileStoreId)? uploadIdSuccess,
    TResult Function(String fileStoreId)? uploadBarSuccess,
    TResult Function(String errorMsg)? idFailed,
    TResult Function(String errorMsg)? barFailed,
    required TResult orElse(),
  }) {
    if (barFailed != null) {
      return barFailed(errorMsg);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_InitialState value) initial,
    required TResult Function(_UploadIdSuccessState value) uploadIdSuccess,
    required TResult Function(_UploadBarSuccessState value) uploadBarSuccess,
    required TResult Function(_IdFailedState value) idFailed,
    required TResult Function(_BartFailedState value) barFailed,
  }) {
    return barFailed(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_InitialState value)? initial,
    TResult? Function(_UploadIdSuccessState value)? uploadIdSuccess,
    TResult? Function(_UploadBarSuccessState value)? uploadBarSuccess,
    TResult? Function(_IdFailedState value)? idFailed,
    TResult? Function(_BartFailedState value)? barFailed,
  }) {
    return barFailed?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_InitialState value)? initial,
    TResult Function(_UploadIdSuccessState value)? uploadIdSuccess,
    TResult Function(_UploadBarSuccessState value)? uploadBarSuccess,
    TResult Function(_IdFailedState value)? idFailed,
    TResult Function(_BartFailedState value)? barFailed,
    required TResult orElse(),
  }) {
    if (barFailed != null) {
      return barFailed(this);
    }
    return orElse();
  }
}

abstract class _BartFailedState implements FileState {
  const factory _BartFailedState({required final String errorMsg}) =
      _$BartFailedStateImpl;

  String get errorMsg;
  @JsonKey(ignore: true)
  _$$BartFailedStateImplCopyWith<_$BartFailedStateImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
