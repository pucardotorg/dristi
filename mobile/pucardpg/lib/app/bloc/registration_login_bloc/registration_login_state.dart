import 'package:pucardpg/app/data/models/advocate-clerk-search/advocate_clerk_search_model.dart';
import 'package:pucardpg/app/data/models/advocate-search/advocate_search_model.dart';
import 'package:pucardpg/app/data/models/auth-response/auth_response.dart';
import 'package:pucardpg/app/data/models/individual-search/individual_search_model.dart';
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/app/data/models/logout-model/logout_model.dart';

abstract class RegistrationLoginState {}

class InitialState extends RegistrationLoginState {}

class LoadingState extends RegistrationLoginState {}

class OtpGenerationSuccessState extends RegistrationLoginState {
  String type;
  OtpGenerationSuccessState({required this.type});
}

class LogoutSuccessState extends RegistrationLoginState {
  ResponseInfoSearch data;
  LogoutSuccessState({required this.data});
}

class LogoutFailedState extends RegistrationLoginState {
  String errorMsg;
  LogoutFailedState({required this.errorMsg});
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

class AdvocateSearchSuccessState extends RegistrationLoginState {
  AdvocateSearchResponse advocateSearchResponse;
  AdvocateSearchSuccessState({required this.advocateSearchResponse});
}

class AdvocateClerkSearchSuccessState extends RegistrationLoginState {
  AdvocateClerkSearchResponse advocateClerkSearchResponse;
  AdvocateClerkSearchSuccessState({required this.advocateClerkSearchResponse});
}

class AdvocateSubmissionSuccessState extends RegistrationLoginState {}

class AdvocateClerkSubmissionSuccessState extends RegistrationLoginState {}
