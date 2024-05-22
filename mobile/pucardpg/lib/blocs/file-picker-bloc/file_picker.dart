import 'dart:async';

import 'package:file_picker/file_picker.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:pucardpg/repositories/file_repository.dart';

part 'file_picker.freezed.dart';

class FileBloc extends Bloc<FileEvent, FileState> {



  final fileRepository = FileRepository();

  FileBloc() : super(const FileState.initial()) {
    on<_FileUploadEvent>(uploadFileEvent);
  }

  Future<void> uploadFileEvent(_FileUploadEvent event,
      Emitter<FileState> emit) async {

    emit(const FileState.initial());
    try {
      final response = await fileRepository.uploadFile("/filestore/v1/files", event.pickedFile!);
      if (event.type == 'id') {
        emit(FileState.uploadIdSuccess(fileStoreId: response));
      }
      if (event.type == 'bar') {
        emit(FileState.uploadBarSuccess(fileStoreId: response));
      }
    } catch (err) {
      if (event.type == 'id') {
        emit(const FileState.idFailed(errorMsg: "Failed to upload"));
      }
      if (event.type == 'bar') {
        emit(const FileState.barFailed(errorMsg: "Failed to upload"));
      }
    }
  }
}

@freezed
class FileEvent with _$FileEvent {
  const factory FileEvent.upload(
      {PlatformFile? pickedFile,
      required String type}) =_FileUploadEvent;

  const factory FileEvent.get(
      {String? fileStoreId}) =_FileGetEvent;
}

@freezed
class FileState with _$FileState {
  const factory FileState.initial() = _InitialState;
  const factory FileState.uploadIdSuccess({
    required String fileStoreId,
  }) = _UploadIdSuccessState;
  const factory FileState.uploadBarSuccess({
    required String fileStoreId,
  }) = _UploadBarSuccessState;
  const factory FileState.idFailed({
    required String errorMsg}) = _IdFailedState;
  const factory FileState.barFailed({
    required String errorMsg}) = _BartFailedState;
}