import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';

abstract class RegistrationLoginEvent {}

class OtpInitialEvent extends RegistrationLoginEvent {}

class SendOtpEvent extends RegistrationLoginEvent {
  final String mobileNumber;
  final String type;
  SendOtpEvent({
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

