



import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/core/resources/data_state.dart';

abstract class RegistrationLoginRepository {

  Future<DataState<OtpResponse>> requestOtp(OtpRequest otpRequest);

  // Future<DataState<List<BirthRegistrationApplicationModel>>> getBirthRegistrationsSearches(String search);
  //
  // Future<DataState<String>> updateBirthData(BirthRegistrationApplicationModel birthData);
  //
  // Future<DataState<String>> saveBirthData(BirthRegistrationApplicationModel birthData);

}