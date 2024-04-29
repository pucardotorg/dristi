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
  }

  Future<void> uploadFileEvent(FileEvent event,
      Emitter<FilePickerState> emit) async {

      emit(FileLoadingState());

      final dataState = await _filePickerUseCase.getFileStore(event.pickedFile);

      if(dataState is DataSuccess){
        emit(FileSuccessState(fileStoreId: dataState.data!));
      }
      if(dataState is DataFailed){
        emit(FileFailedState(errorMsg: dataState.error?.message ?? "",));
      }
  }




}