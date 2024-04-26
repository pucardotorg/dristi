import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';

abstract class RegistrationLoginEvent {}

class InitialEvent extends RegistrationLoginEvent {}

class RequestOtpEvent extends RegistrationLoginEvent {
  final String mobileNumber;
  RequestOtpEvent({
    required this.mobileNumber,
  });
}

class ResendOtpEvent extends RegistrationLoginEvent {
  final String mobileNumber;
  final String type;
  ResendOtpEvent({
    required this.mobileNumber,
    required this.type,
  });
}

class SubmitRegistrationOtpEvent extends RegistrationLoginEvent {
  final String username;
  final String otp;
  UserModel userModel;
  SubmitRegistrationOtpEvent({
    required this.username,
    required this.otp,
    required this.userModel
  });
}

class SubmitLogoutUserEvent extends RegistrationLoginEvent {
  String authToken;
  SubmitLogoutUserEvent({
    required this.authToken
  });
}

class SubmitLitigantProfileEvent extends RegistrationLoginEvent {
  UserModel userModel;
  SubmitLitigantProfileEvent({
    required this.userModel
  });
}

class SendIndividualSearchEvent extends RegistrationLoginEvent {
  UserModel userModel;
  SendIndividualSearchEvent({
    required this.userModel
  });
}

class SendLoginOtpEvent extends RegistrationLoginEvent {
  final String username;
  final String password;
  UserModel userModel;
  SendLoginOtpEvent({
    required this.username,
    required this.password,
    required this.userModel
  });
}

class SubmitAdvocateIndividualEvent extends RegistrationLoginEvent {
  UserModel userModel;
  SubmitAdvocateIndividualEvent({
    required this.userModel
  });
}

class SubmitAdvocateClerkIndividualEvent extends RegistrationLoginEvent {
  UserModel userModel;
  SubmitAdvocateClerkIndividualEvent({
    required this.userModel
  });
}

class SubmitAdvocateProfileEvent extends RegistrationLoginEvent {
  UserModel userModel;
  SubmitAdvocateProfileEvent({
    required this.userModel
  });
}

class SubmitAdvocateClerkProfileEvent extends RegistrationLoginEvent {
  UserModel userModel;
  SubmitAdvocateClerkProfileEvent({
    required this.userModel
  });
}

