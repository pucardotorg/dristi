



import 'package:pucardpg/app/data/models/auth-response/auth_response.dart';
import 'package:pucardpg/app/data/models/citizen-registration-request/citizen_registration_request.dart';
import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/core/resources/data_state.dart';

abstract class RegistrationLoginRepository {

  Future<DataState<OtpResponse>> requestOtp(OtpRequest otpRequest);

  Future<DataState<AuthResponse>> getAuthResponse(String username, String password);

  Future<DataState<AuthResponse>> createCitizen(CitizenRegistrationRequest citizenRegistrationRequest);

// Future<DataState<List<BirthRegistrationApplicationModel>>> getBirthRegistrationsSearches(String search);
  //
  // Future<DataState<String>> updateBirthData(BirthRegistrationApplicationModel birthData);
  //
  // Future<DataState<String>> saveBirthData(BirthRegistrationApplicationModel birthData);

}