import 'dart:io';

import 'package:dio/dio.dart';
import 'package:pucardpg/app/data/models/file-upload-response-model/file_upload_response_model.dart';
import 'package:pucardpg/app/domain/repository/registration_login_repository.dart';
import 'package:pucardpg/core/resources/data_state.dart';

class FilePickerUseCase {
  final RegistrationLoginRepository _registrationLoginRepository;

  FilePickerUseCase(this._registrationLoginRepository);

  Future<DataState<String>> getFileStore(MultipartFile multipartFile, File file) {
    return _registrationLoginRepository.getFileStore(multipartFile, file);
  }

  Future<DataState<FileUploadResponseModel>> uploadFile(File file) {
    return _registrationLoginRepository.uploadFile(file);
  }
}
