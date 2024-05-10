import 'package:file_picker/file_picker.dart';
import 'package:pucardpg/app/data/models/filestore/filestore_model.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/domain/repository/registration_login_repository.dart';
import 'package:pucardpg/core/resources/data_state.dart';

class FilePickerUseCase {
  final RegistrationLoginRepository _registrationLoginRepository;

  FilePickerUseCase(this._registrationLoginRepository);

  Future<DataState<String>> getFileStore(PlatformFile pickedFile) {
    return _registrationLoginRepository.getFileStore(pickedFile);
  }

  Future<DataState<FileStoreModel>> getFileData(UserModel userModel) async {
    var dataState = await _registrationLoginRepository.getFileData(userModel.fileStore!);

    if (dataState is DataSuccess) {
      userModel.documentType = dataState.data?.documentType;
      userModel.documentBytes = dataState.data?.bytes;
    }
    return dataState;
  }
}
