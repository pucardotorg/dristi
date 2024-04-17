

import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/app/domain/repository/registration_login_repository.dart';
import 'package:pucardpg/app/domain/repository/registration_login_repository.dart';
import 'package:pucardpg/core/resources/data_state.dart';

class LoginUseCase {

  final RegistrationLoginRepository _registrationLoginRepository;

  LoginUseCase(this._registrationLoginRepository);

  Future<DataState<OtpResponse>> requestOtp(OtpRequest otpRequest) {
    return _registrationLoginRepository.requestOtp(otpRequest);
  }

}