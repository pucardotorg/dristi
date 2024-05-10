import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_event.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_state.dart';
import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/app/domain/usecase/file_picker_usecase.dart';
import 'package:pucardpg/app/domain/usecase/login_usecase.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:pucardpg/core/resources/data_state.dart';

class FileBloc extends Bloc<FilePickerEvent, FilePickerState> {

  final FilePickerUseCase _filePickerUseCase;

  FileBloc(this._filePickerUseCase): super(FileInitialState()) {
    on<FileEvent>(uploadFileEvent);
    on<GetFileEvent>(getFileData);
  }

  Future<void> uploadFileEvent(FileEvent event,
      Emitter<FilePickerState> emit) async {

      emit(FileLoadingState());

      final dataState = await _filePickerUseCase.getFileStore(event.pickedFile);

      if(dataState is DataSuccess){
        if (event.type == 'idProof') {
          emit(FileSuccessState(fileStoreId: dataState.data!));
        }
        if (event.type == 'advocate') {
          emit(DocumentSuccessState(fileStoreId: dataState.data!));
        }
      }
      if(dataState is DataFailed){
        emit(FileFailedState(errorMsg: dataState.error?.message ?? "",));
      }
  }

  Future<void> getFileData(GetFileEvent event,
      Emitter<FilePickerState> emit) async {

      emit(FileLoadingState());

      final dataState = await _filePickerUseCase.getFileData(event.userModel);

      if(dataState is DataSuccess){
        emit(GetFileSuccessState());
      }
      if(dataState is DataFailed){
        emit(FileFailedState(errorMsg: dataState.error?.message ?? "",));
      }

  }
}