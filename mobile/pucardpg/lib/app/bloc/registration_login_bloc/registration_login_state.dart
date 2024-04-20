import 'package:pucardpg/app/data/models/auth-response/auth_response.dart';
import 'package:pucardpg/app/data/models/individual-search/individual_search_model.dart';

abstract class RegistrationLoginState {}

class InitialState extends RegistrationLoginState {}

class LoadingState extends RegistrationLoginState {}

class OtpGenerationSuccessState extends RegistrationLoginState {}

class IndividualSearchSuccessState extends RegistrationLoginState {
  IndividualSearchResponse individualSearchResponse;
  IndividualSearchSuccessState({required this.individualSearchResponse});
}

class OtpCorrectState extends RegistrationLoginState {
  AuthResponse authResponse;
  OtpCorrectState({required this.authResponse});
}

class RequestFailedState extends RegistrationLoginState {
  String errorMsg;
  RequestFailedState({required this.errorMsg});
}

class LitigantSubmissionSuccessState extends RegistrationLoginState {}

