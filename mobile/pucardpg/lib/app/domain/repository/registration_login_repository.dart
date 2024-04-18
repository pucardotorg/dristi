



import 'package:pucardpg/app/data/models/individual-search/individual_search_model.dart';
import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/core/resources/data_state.dart';

abstract class RegistrationLoginRepository {

  Future<DataState<OtpResponse>> requestOtp(OtpRequest otpRequest);

  Future<DataState<IndividualSearchResponse>> searchIndividual(IndividualSearchRequest individualSearchRequest);

// Future<DataState<List<BirthRegistrationApplicationModel>>> getBirthRegistrationsSearches(String search);
  //
  // Future<DataState<String>> updateBirthData(BirthRegistrationApplicationModel birthData);
  //
  // Future<DataState<String>> saveBirthData(BirthRegistrationApplicationModel birthData);

}