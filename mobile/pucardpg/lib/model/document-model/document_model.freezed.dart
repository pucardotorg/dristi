// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'document_model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

Document _$DocumentFromJson(Map<String, dynamic> json) {
  return _Document.fromJson(json);
}

/// @nodoc
mixin _$Document {
  @JsonKey(name: 'documentType')
  String? get documentType => throw _privateConstructorUsedError;
  @JsonKey(name: 'fileStore')
  String? get fileStore => throw _privateConstructorUsedError;
  @JsonKey(name: 'documentUid')
  String? get documentUid => throw _privateConstructorUsedError;
  @JsonKey(name: 'additionalDetails')
  Object? get additionalDetails => throw _privateConstructorUsedError;

  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;
  @JsonKey(ignore: true)
  $DocumentCopyWith<Document> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $DocumentCopyWith<$Res> {
  factory $DocumentCopyWith(Document value, $Res Function(Document) then) =
      _$DocumentCopyWithImpl<$Res, Document>;
  @useResult
  $Res call(
      {@JsonKey(name: 'documentType') String? documentType,
      @JsonKey(name: 'fileStore') String? fileStore,
      @JsonKey(name: 'documentUid') String? documentUid,
      @JsonKey(name: 'additionalDetails') Object? additionalDetails});
}

/// @nodoc
class _$DocumentCopyWithImpl<$Res, $Val extends Document>
    implements $DocumentCopyWith<$Res> {
  _$DocumentCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? documentType = freezed,
    Object? fileStore = freezed,
    Object? documentUid = freezed,
    Object? additionalDetails = freezed,
  }) {
    return _then(_value.copyWith(
      documentType: freezed == documentType
          ? _value.documentType
          : documentType // ignore: cast_nullable_to_non_nullable
              as String?,
      fileStore: freezed == fileStore
          ? _value.fileStore
          : fileStore // ignore: cast_nullable_to_non_nullable
              as String?,
      documentUid: freezed == documentUid
          ? _value.documentUid
          : documentUid // ignore: cast_nullable_to_non_nullable
              as String?,
      additionalDetails: freezed == additionalDetails
          ? _value.additionalDetails
          : additionalDetails,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$DocumentImplCopyWith<$Res>
    implements $DocumentCopyWith<$Res> {
  factory _$$DocumentImplCopyWith(
          _$DocumentImpl value, $Res Function(_$DocumentImpl) then) =
      __$$DocumentImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {@JsonKey(name: 'documentType') String? documentType,
      @JsonKey(name: 'fileStore') String? fileStore,
      @JsonKey(name: 'documentUid') String? documentUid,
      @JsonKey(name: 'additionalDetails') Object? additionalDetails});
}

/// @nodoc
class __$$DocumentImplCopyWithImpl<$Res>
    extends _$DocumentCopyWithImpl<$Res, _$DocumentImpl>
    implements _$$DocumentImplCopyWith<$Res> {
  __$$DocumentImplCopyWithImpl(
      _$DocumentImpl _value, $Res Function(_$DocumentImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? documentType = freezed,
    Object? fileStore = freezed,
    Object? documentUid = freezed,
    Object? additionalDetails = freezed,
  }) {
    return _then(_$DocumentImpl(
      documentType: freezed == documentType
          ? _value.documentType
          : documentType // ignore: cast_nullable_to_non_nullable
              as String?,
      fileStore: freezed == fileStore
          ? _value.fileStore
          : fileStore // ignore: cast_nullable_to_non_nullable
              as String?,
      documentUid: freezed == documentUid
          ? _value.documentUid
          : documentUid // ignore: cast_nullable_to_non_nullable
              as String?,
      additionalDetails: freezed == additionalDetails
          ? _value.additionalDetails
          : additionalDetails,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$DocumentImpl implements _Document {
  const _$DocumentImpl(
      {@JsonKey(name: 'documentType') this.documentType,
      @JsonKey(name: 'fileStore') this.fileStore,
      @JsonKey(name: 'documentUid') this.documentUid,
      @JsonKey(name: 'additionalDetails') this.additionalDetails});

  factory _$DocumentImpl.fromJson(Map<String, dynamic> json) =>
      _$$DocumentImplFromJson(json);

  @override
  @JsonKey(name: 'documentType')
  final String? documentType;
  @override
  @JsonKey(name: 'fileStore')
  final String? fileStore;
  @override
  @JsonKey(name: 'documentUid')
  final String? documentUid;
  @override
  @JsonKey(name: 'additionalDetails')
  final Object? additionalDetails;

  @override
  String toString() {
    return 'Document(documentType: $documentType, fileStore: $fileStore, documentUid: $documentUid, additionalDetails: $additionalDetails)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$DocumentImpl &&
            (identical(other.documentType, documentType) ||
                other.documentType == documentType) &&
            (identical(other.fileStore, fileStore) ||
                other.fileStore == fileStore) &&
            (identical(other.documentUid, documentUid) ||
                other.documentUid == documentUid) &&
            const DeepCollectionEquality()
                .equals(other.additionalDetails, additionalDetails));
  }

  @JsonKey(ignore: true)
  @override
  int get hashCode => Object.hash(runtimeType, documentType, fileStore,
      documentUid, const DeepCollectionEquality().hash(additionalDetails));

  @JsonKey(ignore: true)
  @override
  @pragma('vm:prefer-inline')
  _$$DocumentImplCopyWith<_$DocumentImpl> get copyWith =>
      __$$DocumentImplCopyWithImpl<_$DocumentImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$DocumentImplToJson(
      this,
    );
  }
}

abstract class _Document implements Document {
  const factory _Document(
      {@JsonKey(name: 'documentType') final String? documentType,
      @JsonKey(name: 'fileStore') final String? fileStore,
      @JsonKey(name: 'documentUid') final String? documentUid,
      @JsonKey(name: 'additionalDetails')
      final Object? additionalDetails}) = _$DocumentImpl;

  factory _Document.fromJson(Map<String, dynamic> json) =
      _$DocumentImpl.fromJson;

  @override
  @JsonKey(name: 'documentType')
  String? get documentType;
  @override
  @JsonKey(name: 'fileStore')
  String? get fileStore;
  @override
  @JsonKey(name: 'documentUid')
  String? get documentUid;
  @override
  @JsonKey(name: 'additionalDetails')
  Object? get additionalDetails;
  @override
  @JsonKey(ignore: true)
  _$$DocumentImplCopyWith<_$DocumentImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
