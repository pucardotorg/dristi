import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';

abstract class OtpEvent {}

class OtpInitialEvent extends OtpEvent {}

class SendOtpEvent extends OtpEvent {
  final String mobileNumber;
  SendOtpEvent({
    required this.mobileNumber,
  });
}

