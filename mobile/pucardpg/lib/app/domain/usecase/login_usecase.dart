

import 'package:pucardpg/app/data/models/advocate-registration-model/advocate_registration_model.dart';
import 'package:pucardpg/app/data/models/individual-search/individual_search_model.dart';
import 'package:pucardpg/app/data/models/auth-response/auth_response.dart';
import 'package:pucardpg/app/data/models/citizen-registration-request/citizen_registration_request.dart';
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/app/data/models/request-info-model/request_info.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/domain/repository/registration_login_repository.dart';
import 'package:pucardpg/app/domain/repository/registration_login_repository.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:pucardpg/core/resources/data_state.dart';

class LoginUseCase {

  final RegistrationLoginRepository _registrationLoginRepository;

  LoginUseCase(this._registrationLoginRepository);

  Future<DataState<OtpResponse>> requestOtp(String mobileNumber, String type) {
    OtpRequest otpRequest = OtpRequest(
        otp: Otp(
            mobileNumber: mobileNumber,
            type: type
        )
    );
    return _registrationLoginRepository.requestOtp(otpRequest);
  }

  Future<DataState<IndividualSearchResponse>> searchIndividual(IndividualSearchRequest individualSearchRequest) {
    return _registrationLoginRepository.searchIndividual(individualSearchRequest);
  }

  Future<DataState<AdvocateRegistrationResponse>> registerAdvocate(AdvocateRegistrationRequest advocateRegistrationRequest) {
    return _registrationLoginRepository.registerAdvocate(advocateRegistrationRequest);
  }

  Future<DataState<AuthResponse>> createCitizen(String username, String otpReference, UserModel userModel) async {
    CitizenRegistrationRequest citizenRegistrationRequest = CitizenRegistrationRequest(
        userInfo: UserInfo(
            username: username,
            otpReference: otpReference
        ),
    );
    var dataState = await _registrationLoginRepository.createCitizen(citizenRegistrationRequest);
    if(dataState is DataSuccess){
      userModel.id = dataState.data?.userRequest?.id;
      userModel.uuid = dataState.data?.userRequest?.uuid;
      userModel.authToken = dataState.data?.accessToken;
      userModel.username = dataState.data?.userRequest?.userName;
      userModel.mobileNumber = dataState.data?.userRequest?.mobileNumber;
    }
    return dataState;
  }

  void registerLitigant(UserModel userModel) {

    Individual individual = Individual(
      name: Name(
          givenName: userModel.firstName!,
          familyName: userModel.lastName!
      ),
      userDetails: UserDetails(
          username: userModel.username!,
          roles: [ userRegisterRole, citizenRole],
      ),
      userUuid: userModel.uuid!,
      userId: userModel.id!,
      mobileNumber: userModel.mobileNumber!,
      address: [Address(
          doorNo: userModel.addressModel.doorNo!,
          latitude: userModel.addressModel.latitude!,
          longitude: userModel.addressModel.longitude!,
          city: userModel.addressModel.city!,
          district: userModel.addressModel.district!,
          pincode: userModel.addressModel.pincode!
      )],
      identifiers: userModel.identifierId == null ? [] :
      [Identifier(
          identifierType: "ADHAAR",
          identifierId: userModel.identifierId!
      )],
      additionalFields: const AdditionalFields(
          fields: [litigant],
      ),
    );

    RequestInfo requestInfo = RequestInfo(
      authToken: userModel.authToken!
    );

    LitigantNetworkModel litigantNetworkModel = LitigantNetworkModel(
      requestInfo: requestInfo,
      individual: individual,
    );

    // return _registrationLoginRepository.createCitizen(citizenRegistrationRequest);

  }

}