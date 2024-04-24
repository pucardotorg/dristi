import 'package:pucardpg/app/data/models/auth-response/auth_response.dart';
import 'package:pucardpg/app/data/models/individual-search/individual_search_model.dart';
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart';

abstract class RegistrationLoginState {}

class InitialState extends RegistrationLoginState {}

class LoadingState extends RegistrationLoginState {}

class OtpGenerationSuccessState extends RegistrationLoginState {
  String type;
  OtpGenerationSuccessState({required this.type});
}

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

class RequestOtpFailedState extends RegistrationLoginState {
  String errorMsg;
  RequestOtpFailedState({required this.errorMsg});
}

class LitigantSubmissionSuccessState extends RegistrationLoginState {
  LitigantResponseModel litigantResponseModel;
  LitigantSubmissionSuccessState({required this.litigantResponseModel});
}

class AdvocateSubmissionSuccessState extends RegistrationLoginState {}

class AdvocateClerkSubmissionSuccessState extends RegistrationLoginState {}
