import 'package:pucardpg/app/data/models/advocate-clerk-registration-model/advocate_clerk_registration_model.dart';
import 'package:pucardpg/app/data/models/advocate-clerk-search/advocate_clerk_search_model.dart';
import 'package:pucardpg/app/data/models/advocate-registration-model/advocate_registration_model.dart';
import 'package:pucardpg/app/data/models/advocate-request-info/advocate_request_info.dart';
import 'package:pucardpg/app/data/models/advocate-search/advocate_search_model.dart';
import 'package:pucardpg/app/data/models/advocate-user-info/advocate_user_info.dart';
import 'package:pucardpg/app/data/models/document-model/document_model.dart';
import 'package:pucardpg/app/data/models/individual-search/individual_search_model.dart';
import 'package:pucardpg/app/data/models/auth-response/auth_response.dart';
import 'package:pucardpg/app/data/models/citizen-registration-request/citizen_registration_request.dart';
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/app/data/models/logout-model/logout_model.dart';
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

  Future<DataState<ResponseInfoSearch>> logoutUser(String authToken) {
    LogoutRequest logoutRequest = LogoutRequest(accessToken: authToken, requestInfo: RequestInfo(authToken: authToken));
    return _registrationLoginRepository.logoutUser(logoutRequest);
  }

  Future<DataState<IndividualSearchResponse>> searchIndividual(UserModel userModel) async {
    IndividualSearchRequest individualSearchRequest = IndividualSearchRequest(
        requestInfo: RequestInfoSearch(authToken: userModel.authToken!),
        individual: IndividualSearch(userUuid: [userModel.uuid!]));

    var dataState = await _registrationLoginRepository.searchIndividual(individualSearchRequest);

    if (dataState is DataSuccess) {
      if (dataState.data!.individual.isNotEmpty) {
        Individual individual = dataState.data!.individual[0];
        var userTypeField = individual.additionalFields.fields.firstWhere(
              (field) => field.key == "userType",
          orElse: () => const Fields(key: "", value: ""),
        );
        userModel.individualId = individual.individualId;
        userModel.identifierType = individual.identifiers[0].identifierType;
        userModel.identifierId = individual.identifiers[0].identifierId;
        userModel.firstName = individual.name.givenName;
        userModel.lastName = individual.name.familyName;
        userModel.userType = userTypeField.value;
        var address = individual.address[0];
        userModel.addressModel.doorNo = address.doorNo;
        userModel.addressModel.city = address.city;
        userModel.addressModel.pincode = address.pincode;
        userModel.addressModel.street = address.street;
        userModel.addressModel.district = address.district;
        userModel.addressModel.latitude = address.latitude;
        userModel.addressModel.longitude = address.longitude;
      }
    }
    return dataState;
  }

  Future<DataState<AdvocateSearchResponse>> searchAdvocate(UserModel userModel) {
    AdvocateSearchRequest advocateSearchRequest = AdvocateSearchRequest(
      criteria: [SearchCriteria(individualId: userModel.individualId)],
        tenantId: tenantId,
        requestInfo: AdvocateRequestInfo(
            authToken: userModel.authToken,
            userInfo: AdvocateUserInfo(
              type: type,
              tenantId: tenantId,
              uuid: userModel.uuid,
              roles: [userRegisterRole, citizenRole]
            )
        ));
    return _registrationLoginRepository.searchAdvocate(advocateSearchRequest);
  }

  Future<DataState<AdvocateClerkSearchResponse>> searchAdvocateClerk(UserModel userModel) {
    AdvocateClerkSearchRequest advocateClerkSearchRequest = AdvocateClerkSearchRequest(
        criteria: [SearchCriteria(individualId: userModel.individualId)],
        tenantId: tenantId,
        requestInfo: AdvocateRequestInfo(
            authToken: userModel.authToken,
            userInfo: AdvocateUserInfo(
                type: type,
                tenantId: tenantId,
                uuid: userModel.uuid,
                roles: [userRegisterRole, citizenRole]
            )
        ));
    return _registrationLoginRepository.searchAdvocateClerk(advocateClerkSearchRequest);
  }

  Future<DataState<AdvocateRegistrationResponse>> registerAdvocate(UserModel userModel) {
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
          individualId: userModel.individualId,
          workflow: Workflow(
            action: "REGISTER",
            documents: [
              Document(fileStore: userModel.fireStore)
            ]
          ),
          documents: [
            Document(fileStore: userModel.fireStore)
          ],
          additionalDetails: {"username" : userModel.firstName! + userModel.lastName!}
        )
      ]
    );
    return _registrationLoginRepository.registerAdvocate(advocateRegistrationRequest);
  }

  Future<DataState<AdvocateClerkRegistrationResponse>> registerAdvocateClerk(UserModel userModel) {
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
              individualId: userModel.individualId,
              workflow: Workflow(
                  action: "REGISTER",
                  documents: [
                    Document(fileStore: userModel.fireStore)
                  ]
              ),
              documents: [
                Document(fileStore: userModel.fireStore)
              ],
              additionalDetails: {"username" : userModel.firstName! + userModel.lastName!}
          )
        ]
    );
    return _registrationLoginRepository.registerAdvocateClerk(advocateClerkRegistrationRequest);
  }

  Future<DataState<AuthResponse>> createCitizen(String name, String username, String otpReference, UserModel userModel) async {
    CitizenRegistrationRequest citizenRegistrationRequest = CitizenRegistrationRequest(
        userInfo: UserInfo(
            name: name,
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

  Future<DataState<LitigantResponseModel>> registerLitigant(UserModel userModel, Fields fields) {

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
      userId: userModel.id!.toString(),
      mobileNumber: userModel.mobileNumber!,
      address: [Address(
          doorNo: userModel.addressModel.doorNo!,
          latitude: userModel.addressModel.latitude!,
          longitude: userModel.addressModel.longitude!,
          city: userModel.addressModel.city!,
          district: userModel.addressModel.district!,
          street: userModel.addressModel.street!,
          pincode: userModel.addressModel.pincode!
      )],
      identifiers: userModel.identifierId == null ? [] :
      [Identifier(
          identifierType: "ADHAAR",
          identifierId: userModel.identifierId!
      )],
      additionalFields: AdditionalFields(
          fields: [fields],
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