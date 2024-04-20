import 'package:pucardpg/app/data/models/auth-response/auth_response.dart';
import 'package:pucardpg/app/data/models/individual-search/individual_search_model.dart';

abstract class RegistrationLoginState {}

class OtpInitialState extends RegistrationLoginState {}

class OtpLoadingState extends RegistrationLoginState {}

class OtpSuccessState extends RegistrationLoginState {}

class IndividualSearchSuccessState extends RegistrationLoginState {
  IndividualSearchResponse individualSearchResponse;
  IndividualSearchSuccessState({required this.individualSearchResponse});
}

class OtpFailedState extends RegistrationLoginState {
  String errorMsg;
  OtpFailedState({required this.errorMsg});
}

class OtpCorrectState extends RegistrationLoginState {
  AuthResponse authResponse;
  OtpCorrectState({required this.authResponse});
}

class OtpIncorrectState extends RegistrationLoginState {
  String errorMsg;
  OtpIncorrectState({required this.errorMsg});
}

class LitigantSubmissionSuccessState extends RegistrationLoginState {}

class LitigantSubmissionFailedState extends RegistrationLoginState {}