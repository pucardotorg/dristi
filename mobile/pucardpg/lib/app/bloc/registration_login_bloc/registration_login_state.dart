abstract class RegistrationLoginState {}

class OtpInitialState extends RegistrationLoginState {}

class OtpLoadingState extends RegistrationLoginState {}

class OtpSuccessState extends RegistrationLoginState {}

class OtpFailedState extends RegistrationLoginState {
  String errorMsg;
  OtpFailedState({required this.errorMsg});
}