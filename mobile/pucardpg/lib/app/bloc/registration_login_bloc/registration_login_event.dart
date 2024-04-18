import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';

abstract class RegistrationLoginEvent {}

class OtpInitialEvent extends RegistrationLoginEvent {}

class SendOtpEvent extends RegistrationLoginEvent {
  final String mobileNumber;
  SendOtpEvent({
    required this.mobileNumber,
  });
}

