import 'package:file_picker/file_picker.dart';
import 'package:pucardpg/app/domain/repository/registration_login_repository.dart';
import 'package:pucardpg/core/resources/data_state.dart';

class FilePickerUseCase {
  final RegistrationLoginRepository _registrationLoginRepository;

  FilePickerUseCase(this._registrationLoginRepository);

  Future<DataState<String>> getFileStore(PlatformFile pickedFile) {
    return _registrationLoginRepository.getFileStore(pickedFile);
  }
}
