import 'package:pucardpg/app/data/models/advocate-clerk-registration-model/advocate_clerk_registration_model.dart';
import 'package:pucardpg/app/data/models/advocate-registration-model/advocate_registration_model.dart';
import 'package:pucardpg/app/data/models/advocate-request-info/advocate_request_info.dart';
import 'package:pucardpg/app/data/models/advocate-user-info/advocate_user_info.dart';
import 'package:pucardpg/app/data/models/document-model/document_model.dart';
import 'package:pucardpg/app/data/models/individual-search/individual_search_model.dart';
import 'package:pucardpg/app/data/models/auth-response/auth_response.dart';
import 'package:pucardpg/app/data/models/citizen-registration-request/citizen_registration_request.dart';
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/app/data/models/request-info-model/request_info.dart';
import 'package:pucardpg/app/data/models/role-model/role.dart';
import 'package:pucardpg/app/data/models/workflow-model/workflow_model.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/domain/repository/registration_login_repository.dart';
import 'package:pucardpg/app/domain/repository/registration_login_repository.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:pucardpg/core/resources/data_state.dart';
import 'package:pucardpg/app/data/models/request-info-model/request_info.dart' as r;

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

  Future<DataState<IndividualSearchResponse>> searchIndividual(String authToken, String uuid) {
    IndividualSearchRequest individualSearchRequest = IndividualSearchRequest(
        requestInfo: RequestInfoSearch(authToken: authToken),
        individual: IndividualSearch(userUuid: [uuid]));
    return _registrationLoginRepository.searchIndividual(individualSearchRequest);
  }

  Future<DataState<AdvocateRegistrationResponse>> registerAdvocate(String individualId, UserModel userModel) {
    AdvocateRegistrationRequest advocateRegistrationRequest = AdvocateRegistrationRequest(
    requestInfo: AdvocateRequestInfo(
        userInfo: AdvocateUserInfo(
          type: type,
          tenantId: tenantId,
          roles: [
            Role(
              code: "USER_REGISTER",
              name: "USER_REGISTER",
              tenantId: tenantId
            )
          ],
          uuid: userModel.uuid
        ),
        authToken: userModel.authToken
      ),
      advocates: [
        Advocate(
          tenantId: tenantId,
          barRegistrationNumber: userModel.barRegistrationNumber,
          individualId: individualId,
          workflow: Workflow(
            action: "REGISTER",
            documents: [
              Document(fileStore: userModel.fireStore)
            ]
          ),
          documents: [
            Document(fileStore: userModel.fireStore)
          ],
          additionalDetails: {'stateOfRegistration' : userModel.stateOfRegistration, "username" : userModel.firstName! + userModel.lastName!}
        )
      ]
    );
    return _registrationLoginRepository.registerAdvocate(advocateRegistrationRequest);
  }

  Future<DataState<AdvocateClerkRegistrationResponse>> registerAdvocateClerk(String individualId, UserModel userModel) {
    AdvocateClerkRegistrationRequest advocateClerkRegistrationRequest = AdvocateClerkRegistrationRequest(
        requestInfo: AdvocateRequestInfo(
            userInfo: AdvocateUserInfo(
                type: type,
                tenantId: tenantId,
                roles: [
                  Role(
                      code: "USER_REGISTER",
                      name: "USER_REGISTER",
                      tenantId: tenantId
                  )
                ],
                uuid: userModel.uuid
            ),
            authToken: userModel.authToken
        ),
        clerks: [
          Clerk(
              tenantId: tenantId,
              stateRegnNumber: userModel.stateRegnNumber,
              individualId: individualId,
              workflow: Workflow(
                  action: "REGISTER",
                  documents: [
                    Document(fileStore: userModel.fireStore)
                  ]
              ),
              documents: [
                Document(fileStore: userModel.fireStore)
              ],
              additionalDetails: {'stateOfRegistration' : userModel.stateOfRegistration, "username" : userModel.firstName! + userModel.lastName!}
          )
        ]
    );
    return _registrationLoginRepository.registerAdvocateClerk(advocateClerkRegistrationRequest);
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

  Future<DataState<AuthResponse>> getAuthResponse(String username, String password, UserModel userModel) async {

    var dataState = await _registrationLoginRepository.getAuthResponse(username, password);
    if(dataState is DataSuccess){
      userModel.id = dataState.data?.userRequest?.id;
      userModel.uuid = dataState.data?.userRequest?.uuid;
      userModel.authToken = dataState.data?.accessToken;
      userModel.username = dataState.data?.userRequest?.userName;
      userModel.mobileNumber = dataState.data?.userRequest?.mobileNumber;
    }
    return dataState;
  }

  Future<DataState<LitigantResponseModel>> registerLitigant(UserModel userModel) {

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
          latitude: userModel.addressModel.latitude ?? 0.0,
          longitude: userModel.addressModel.longitude ?? 0.0,
          city: userModel.addressModel.city ?? "",
          district: userModel.addressModel.district ?? "",
          pincode: userModel.addressModel.pincode ?? ""
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

    r.RequestInfo requestInfo = r.RequestInfo(
      authToken: userModel.authToken!
    );

    LitigantNetworkModel litigantNetworkModel = LitigantNetworkModel(
      requestInfo: requestInfo,
      individual: individual,
    );

    print(litigantNetworkModel.toJson());

    return _registrationLoginRepository.registerLitigant(litigantNetworkModel);

  }

}