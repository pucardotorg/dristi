abstract class OtpState {}

class OtpInitialState extends OtpState {}

class OtpLoadingState extends OtpState {}

class OtpSuccessState extends OtpState {}

class OtpFailedState extends OtpState {
  String errorMsg;
  OtpFailedState({required this.errorMsg});
}