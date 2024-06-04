import 'dart:async';
import 'dart:convert';
import 'dart:ffi';

import 'package:dio/src/dio_mixin.dart';
import 'package:dio/src/options.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:pucardpg/data/api_interceptors.dart';
import 'package:pucardpg/data/secure_storage/secureStore.dart';
import 'package:pucardpg/model/advocate-clerk-registration-model/advocate_clerk_registration_model.dart';
import 'package:pucardpg/model/advocate-clerk-search/advocate_clerk_search_model.dart';
import 'package:pucardpg/model/advocate-registration-model/advocate_registration_model.dart';
import 'package:pucardpg/model/advocate-request-info/advocate_request_info.dart';
import 'package:pucardpg/model/advocate-search/advocate_search_model.dart';
import 'package:pucardpg/model/advocate-user-info/advocate_user_info.dart';
import 'package:pucardpg/model/auth-response/auth_response.dart';
import 'package:pucardpg/model/citizen-registration-request/citizen_registration_request.dart';
import 'package:pucardpg/model/document-model/document_model.dart';
import 'package:pucardpg/model/individual-search/individual_search_model.dart';
import 'package:pucardpg/model/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/model/litigant_model.dart';
import 'package:pucardpg/model/login/loginModel.dart';
import 'package:pucardpg/model/otp-models/otp_model.dart';
import 'package:pucardpg/model/request-info-model/request_info.dart';
import 'package:pucardpg/model/workflow-model/workflow_model.dart';
import 'package:pucardpg/repositories/authRepo.dart';
import 'package:pucardpg/repositories/file_repository.dart';
import 'package:pucardpg/utils/i18_key_constants.dart';

part 'authbloc.freezed.dart';

class AuthBloc extends Bloc<AuthEvent, AuthState> {

  late String accesstoken;
  late UserRequest _userRequest;
  late String _refreshtoken;
  late AuthResponse _authResponse;
  final authRepository = AuthRepository(InitClient().init());
  final fileRepository = FileRepository();

  UserModel userModel = UserModel();

  AuthBloc() : super(const AuthState.unauthenticated()) {
    on<_SubmitLoginOtpEvent>(_onLogin);
    on<_AuthLoginEvent>(_onAuthLogin);
    on<_AuthLogoutEvent>(_onLogout);
    on<_AuthLoadEvent>(_onLoad);
    on<_AuthRefreshTokenEvent>(_onRefreshToken);
    on<_SubmitRegistrationOtpEvent>(_onRegistration);
    on<_RequestOtpEvent>(_requestOtpEvent);
    on<_ResendOtpEvent>(resendOtpEvent);
    on<_SubmitProfileEvent>(submitIndividualProfile);
  }

  FutureOr<void> _onAuthLogin(
      _AuthLoginEvent event, Emitter<AuthState> emit) async {
    emit(const AuthState.initial());
    final secureStore = SecureStore();
    secureStore.setAccessToken(accesstoken);
    secureStore.setRefreshToken(_refreshtoken);
    secureStore.setAccessInfo(_authResponse);

    emit(AuthState.authenticated(
        accesstoken: accesstoken,
        refreshtoken: _refreshtoken,
        userRequest: _userRequest));
  }

  FutureOr<void> _onLogin(_SubmitLoginOtpEvent event,
      Emitter<AuthState> emit) async {
    final secureStore = SecureStore();

    AuthResponse response;
    try {
      emit(const AuthState.initial());
      response = await authRepository.validateLogin(
          "/user/oauth/token",
          LoginModel(
              username: event.username,
              password: event.password,
              grantType: 'password'
          )
      );

      _authResponse = response;

      accesstoken = response.accessToken!;
      _refreshtoken = response.refreshToken!;
      _userRequest = response.userRequest!;
      secureStore.setUserRequest(response.userRequest!);
      secureStore.setAccessToken(accesstoken);
      secureStore.setRefreshToken(_refreshtoken);

      IndividualSearchRequest individualSearchRequest = IndividualSearchRequest(
          individual: IndividualSearch(userUuid: [response.userRequest?.uuid ?? ""]));

      final responseSearchIndividual = await authRepository.searchIndividual('/individual/v1/_search?limit=${appConstants.limit}&offset=${appConstants.offset}&tenantId=${appConstants.tenantId}', individualSearchRequest);
      userModel = UserModel(mobileNumber: userModel.mobileNumber, type: userModel.type);
      userModel.authToken = response.accessToken;
      userModel.id = response.userRequest?.id;
      userModel.uuid = response.userRequest?.uuid;
      userModel.username = response.userRequest?.userName;
      if (responseSearchIndividual.individual.isEmpty) {
        emit(AuthState.individualSearchSuccessState(individualSearchResponse: responseSearchIndividual));
      } else {
        Individual individual = responseSearchIndividual.individual[0];
        final userTypeField = individual.additionalFields.fields.firstWhere(
              (field) => field.key == "userType",
          orElse: () => const Fields(key: "", value: ""),
        );
        final idVerificationTypeField = individual.additionalFields.fields.firstWhere(
              (field) => field.key == "idVerificationType",
          orElse: () => const Fields(key: "", value: ""),
        );
        final identifierType = individual.identifiers[0].identifierType;
        final detailField = individual.additionalFields.fields.firstWhere(
              (field) => field.key == "identifierIdDetails",
          orElse: () => const Fields(key: "", value: ""),
        ).value;
        userModel.idVerificationType = idVerificationTypeField.value;
        if (detailField != "") {
          final identifierIdDetails = jsonDecode(detailField);
          if (userModel.idVerificationType != 'AADHAR') {
            userModel.idFilename = identifierIdDetails['filename'];
            userModel.idFileStore = identifierIdDetails['fileStoreId'];
            var fileResponse = await fileRepository.getFileData('/filestore/v1/files/id', appConstants.tenantId, identifierIdDetails['fileStoreId']);
            userModel.idBytes = fileResponse.bytes;
            userModel.idDocumentType = fileResponse.documentType;
          }
        }
        userModel.individualId = individual.individualId;
        userModel.identifierType = identifierType;
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

        if (userModel.userType == 'LITIGANT') {
          emit(AuthState.individualSearchSuccessState(individualSearchResponse: responseSearchIndividual));
        }
        if (userModel.userType == 'ADVOCATE') {
          AdvocateSearchRequest advocateSearchRequest = AdvocateSearchRequest(
              criteria: [SearchCriteria(individualId: userModel.individualId)],
              tenantId: appConstants.tenantId,
          );
          final responseSearchAdvocate = await authRepository.searchAdvocate('/advocate/advocate/v1/_search', advocateSearchRequest);
          if (responseSearchAdvocate.advocates[0].responseList.isNotEmpty) {
            ResponseList advocate = responseSearchAdvocate.advocates[0].responseList[0];
            userModel.documentFilename = advocate.additionalDetails?['filename'];
            userModel.barRegistrationNumber = advocate.barRegistrationNumber;
            userModel.fileStore = advocate.documents?[0].fileStore;
            var fileResponse = await fileRepository.getFileData('/filestore/v1/files/id', appConstants.tenantId, advocate.documents![0].fileStore!);
            userModel.documentBytes = fileResponse.bytes;
            userModel.documentType = fileResponse.documentType;
            // userModel.documentType = advocate.documents?[0].documentType;
            userModel.documentFilename = advocate.additionalDetails?['filename'];
          }
          emit(AuthState.advocateSearchSuccessState(advocateSearchResponse: responseSearchAdvocate));
        }
        if (userModel.userType == 'ADVOCATE_CLERK') {
          AdvocateClerkSearchRequest advocateClerkSearchRequest = AdvocateClerkSearchRequest(
              criteria: [SearchCriteria(individualId: userModel.individualId)],
              tenantId: appConstants.tenantId,
          );
          final responseSearchAdvocateClerk = await authRepository.searchAdvocateClerk('/advocate/clerk/v1/_search', advocateClerkSearchRequest);
          emit(AuthState.advocateClerkSearchSuccessState(advocateClerkSearchResponse: responseSearchAdvocateClerk));
        }
      }
    } catch (err) {
      emit(const AuthState.requestFailed(errorMsg: "Login Failed",));
      rethrow;
    }
  }

  FutureOr<void> _onRegistration(
      _SubmitRegistrationOtpEvent event, Emitter<AuthState> emit) async {
    AuthResponse response;
    try {
      emit(const AuthState.initial());
      final secureStore = SecureStore();

      CitizenRegistrationRequest citizenRegistrationRequest = CitizenRegistrationRequest(
        userInfo: UserInfo(
            username: event.username,
            otpReference: event.otp
        ),
      );

      response = await authRepository.createCitizen(
          "/user/citizen/_create",
          citizenRegistrationRequest
      );

      accesstoken = response.accessToken!;
      _refreshtoken = response.refreshToken!;
      _userRequest = response.userRequest!;
      userModel = UserModel(mobileNumber: userModel.mobileNumber, type: userModel.type);
      userModel.authToken = response.accessToken;
      userModel.id = response.userRequest?.id;
      userModel.uuid = response.userRequest?.uuid;
      userModel.username = response.userRequest?.userName;
      secureStore.setUserRequest(response.userRequest!);
      secureStore.setAccessToken(accesstoken);
      secureStore.setRefreshToken(_refreshtoken);
      emit(AuthState.otpCorrect(
          authResponse: response));

    } catch (err) {
      emit(const AuthState.requestFailed(errorMsg: "Registration Failed",));
      rethrow;
    }
  }

  FutureOr<void> _onLogout(_AuthLogoutEvent event, Emitter<AuthState> emit) async {
    emit(const AuthState.initial());
    try {
      final secureStore = SecureStore();

      final response = await authRepository.logout("/user/_logout", await secureStore.getAccessToken());
      if (response.status == "Logout successfully") {
        secureStore.deleteAccessToken();
        secureStore.deleteAccessInfo();
        secureStore.deleteSelectedIndividual();
        userModel = UserModel();

        emit(const AuthState.unauthenticated());
      }
    } catch (e1) {
      emit(const AuthState.initial());
    }
  }

  Future<FutureOr<void>> _onLoad(
      _AuthLoadEvent event, Emitter<AuthState> emit) async {

    emit(const AuthState.initial());
    final secureStore = SecureStore();

    //first attempt to get the accessToken from local secure storage, if successful, the user need not go through the login page again
    AuthResponse? accessInfo;
    accessInfo = await secureStore.getAccessInfo();

    if (accessInfo != null) {
      accesstoken = accessInfo.accessToken!;
      _refreshtoken = accessInfo.refreshToken!;
      _userRequest = accessInfo.userRequest!;

      emit(AuthState.authenticated(
          accesstoken: accesstoken,
          refreshtoken: _refreshtoken,
          userRequest: _userRequest));
    } else {
      //stay in the unauthenicated state
      emit(const AuthState.unauthenticated());
    }
  }

  FutureOr<void> _requestOtpEvent(_RequestOtpEvent event,
      Emitter<AuthState> emit) async {

    emit(const AuthState.initial());

    OtpRequest otpRequest = OtpRequest(
        otp: Otp(
            mobileNumber: event.mobileNumber,
            type: event.type
        )
    );

    try{
      final registerResponse = await authRepository.requestOtp("/user-otp/v1/_send?tenantId=pg&_=1712987382117", otpRequest);
      emit(AuthState.otpGenerationSucceed(type: event.type));
    }
    catch(e1){
      if (event.type == 'login') {
        emit(const AuthState.requestOtpFailed(errorMsg: 'Request Otp Failed'));
      }
      if (event.type == 'register') {
        emit(const AuthState.registrationRequestOtpFailed(errorMsg: 'Request Otp Failed'));
      }
    }

  }

  FutureOr<void> resendOtpEvent(_ResendOtpEvent event,
      Emitter<AuthState> emit) async {

    emit(const AuthState.initial());

    OtpRequest otpRequest = OtpRequest(
        otp: Otp(
            mobileNumber: event.mobileNumber,
            type: event.type
        )
    );

    try{
      final registerResponse = await authRepository.requestOtp("/user-otp/v1/_send?tenantId=pg&_=1712987382117", otpRequest);
      emit(const AuthState.resendOtpGenerationSucceed(type: "register"));
    }
    catch(e1) {
      if (event.type == 'login') {
        emit(const AuthState.requestOtpFailed(errorMsg: 'Request Otp Failed'));
      }
      if (event.type == 'register') {
        emit(const AuthState.registrationRequestOtpFailed(errorMsg: 'Request Otp Failed'));
      }    }
  }

  FutureOr<void> _onRefreshToken(_AuthRefreshTokenEvent event, Emitter<AuthState> emit) async {
    emit(const AuthState.initial());

    AuthResponse response;
    final secureStore = SecureStore();
    response = await authRepository.validateLogin(
        "/user/oauth/token",
        LoginModel(
          username: _userRequest.userName,
          refreshToken: await secureStore.getRefreshToken(),
          grantType: 'refresh_token'
        )
    );

    accesstoken = response.accessToken!;
    _refreshtoken = response.refreshToken!;
    _userRequest = response.userRequest!;
    userModel.authToken = response.accessToken!;
    _authResponse = response;

    //store accessToken in secure storage
    await secureStore.setUserRequest(response.userRequest!);
    await secureStore.setAccessToken(accesstoken);
    await secureStore.setRefreshToken(_refreshtoken);

    //store other access Information in secure storage
    secureStore.setAccessInfo(response);

    //change to authenticated state now that we have access
    emit(AuthState.error(event.requestOptions, event.handler));
  }

  FutureOr<void> submitIndividualProfile(_SubmitProfileEvent event,
      Emitter<AuthState> emit) async {

    try{
      emit(const AuthState.initial());

      String? identifierType;
      if (userModel.identifierType == null || userModel.identifierType!.isEmpty) {
        identifierType = 'AADHAR';
      } else {
        identifierType = userModel.identifierType;
      }
      String? identifierId;
      if (userModel.identifierId == null) {
        identifierId = '448022452235';
      } else {
        identifierId = userModel.identifierId;
      }
      Individual individual = Individual(
        name: Name(
            givenName: userModel.firstName!,
            familyName: userModel.lastName!
        ),
        userDetails: UserDetails(
          username: userModel.username!,
          roles: [appConstants.getCitizenRole,
            appConstants.getCaseCreatorRole,
            appConstants.getCaseEditorRole,
            appConstants.getCaseViewerRole],
        ),
        userUuid: userModel.uuid!,
        userId: userModel.id!.toString(),
        mobileNumber: userModel.mobileNumber!,
        address: [Address(
            doorNo: userModel.addressModel.doorNo ?? "",
            latitude: userModel.addressModel.latitude ?? 0.0,
            longitude: userModel.addressModel.longitude ?? 0.0,
            city: userModel.addressModel.city ?? "",
            district: userModel.addressModel.district ?? "",
            street: userModel.addressModel.street ?? "",
            pincode: userModel.addressModel.pincode ?? ""
        )],
        identifiers: userModel.identifierId == null ? [] :
        [Identifier(
          identifierType: identifierType ?? 'AADHAR',
          identifierId: identifierId ?? '448022345455',
        )],
        additionalFields: AdditionalFields(
          fields: [Fields(
            key: 'userType',
            value: userModel.userType!,
          ),
            Fields(
              key: 'idVerificationType',
              value: userModel.idVerificationType!,
            ),
            Fields(
                key: 'identifierIdDetails',
                value: userModel.idVerificationType != 'AADHAR' ? jsonEncode({'fileStoreId' : userModel.identifierId, 'filename': userModel.idFilename})
                    : jsonEncode({})
            )
          ],
        ),
      );

      RequestInfo requestInfo = RequestInfo(
          authToken: userModel.authToken!
      );

      LitigantNetworkModel litigantNetworkModel = LitigantNetworkModel(
        requestInfo: requestInfo,
        individual: individual,
      );
      final registerResponse = await authRepository.registerLitigant("/individual/v1/_create", litigantNetworkModel);
      userModel.individualId = registerResponse.individualInfo.individualId;
      if (userModel.userType == 'ADVOCATE') {
        AdvocateRegistrationRequest advocateRegistrationRequest = AdvocateRegistrationRequest(
            advocate:
              ResponseList(
                  tenantId: appConstants.tenantId,
                  barRegistrationNumber: userModel.barRegistrationNumber,
                  individualId: userModel.individualId,
                  workflow: Workflow(
                      action: "REGISTER",
                      documents: [
                        Document(fileStore: userModel.fileStore)
                      ]
                  ),
                  documents: [
                    Document(
                        fileStore: userModel.fileStore,
                        documentType: userModel.documentType
                    )
                  ],
                  additionalDetails: {
                    "username" : userModel.firstName! + userModel.lastName!,
                    "filename" : userModel.documentFilename
                  }
              )
        );
        await authRepository.registerAdvocate('/advocate/advocate/v1/_create', advocateRegistrationRequest);
      } else if (userModel.userType == 'ADVOCATE_CLERK') {
        AdvocateClerkRegistrationRequest advocateClerkRegistrationRequest = AdvocateClerkRegistrationRequest(
            clerk:
              ResponseListClerk(
                  tenantId: appConstants.tenantId,
                  stateRegnNumber: userModel.stateRegnNumber,
                  individualId: userModel.individualId,
                  workflow: const Workflow(
                      action: "REGISTER",
                      documents: [
                      ]
                  ),
                  documents: [
                  ],
                  additionalDetails: {"username" : userModel.firstName! + userModel.lastName!}
              )
        );
        await authRepository.registerAdvocateClerk('/advocate/clerk/v1/_create', advocateClerkRegistrationRequest);
      } else if (userModel.userType == 'LITIGANT') {
        final secureStore = SecureStore();
        secureStore.setAccessToken(accesstoken);
        secureStore.setRefreshToken(_refreshtoken);
        secureStore.setAccessInfo(_authResponse);
        emit(const AuthState.profileSuccessState());
      }
    }
    catch(e1) {
      emit(const AuthState.profileFailedState(errorMsg: 'Registering Failed'));
    }
  }
}

@freezed
class AuthEvent with _$AuthEvent {
  const factory AuthEvent.login() = _AuthLoginEvent;
  const factory AuthEvent.logout() = _AuthLogoutEvent;
  const factory AuthEvent.attemptLoad() = _AuthLoadEvent;
  const factory AuthEvent.requestOtp(String mobileNumber, String type) = _RequestOtpEvent;
  const factory AuthEvent.resendOtp(
      final String mobileNumber, final String type) = _ResendOtpEvent;

  const factory AuthEvent.submitRegistrationOtp(
      final String username, final String otp, UserModel userModel) = _SubmitRegistrationOtpEvent;
  const factory AuthEvent.submitLoginOtpEvent(
      final String username, final String password, UserModel userModel) = _SubmitLoginOtpEvent;
  const factory AuthEvent.submitLogoutUser(
      final String authToken) = _SubmitLogoutUserEvent;
  const factory AuthEvent.refreshToken(
      RequestOptions requestOptions,
      ErrorInterceptorHandler handler) = _AuthRefreshTokenEvent;
  const factory AuthEvent.submitProfile() = _SubmitProfileEvent;
}

@freezed
class AuthState with _$AuthState {

  const factory AuthState.error(
      RequestOptions requestOptions,
      ErrorInterceptorHandler handler) = _ErrorState;
  const factory AuthState.initial() = _InitialState;
  const factory AuthState.unauthenticated() = _UnauthenticatedState;
  const factory AuthState.authenticated({
    required String accesstoken,
    required String? refreshtoken,
    required UserRequest? userRequest,
  }) = _AuthenticatedState;
  const factory AuthState.otpGenerationSucceed({
    required String type,
  }) = _OtpGenerationSuccessState;
  const factory AuthState.resendOtpGenerationSucceed({
    required String type,
  }) = _ResendOtpGenerationSuccessState;
  const factory AuthState.requestFailed({
    required String errorMsg,
  }) = _RequestFailedState;
  const factory AuthState.otpCorrect({
    required AuthResponse authResponse,
  }) = _OtpCorrectState;
  const factory AuthState.requestOtpFailed({
    required String errorMsg
  }) = _RequestOtpFailedState;
  const factory AuthState.registrationRequestOtpFailed({
    required String errorMsg,
  }) = _RegistrationRequestOtpFailedState;
  const factory AuthState.logoutFailedState({
    required String errorMsg
  }) = _LogoutFailedState;

  const factory AuthState.profileSuccessState() = _ProfileSuccessState;
  const factory AuthState.profileFailedState({
    required String errorMsg
  }) = _ProfileFailedState;

  const factory AuthState.individualSearchSuccessState({
    required IndividualSearchResponse individualSearchResponse
  }) = _IndividualSearchSuccessState;

  const factory AuthState.advocateSearchSuccessState({
    required AdvocateSearchResponse advocateSearchResponse
  }) = _AdvocateSearchSuccessState;

  const factory AuthState.advocateClerkSearchSuccessState({
    required AdvocateClerkSearchResponse advocateClerkSearchResponse
  }) = _AdvocateClerkSearchSuccessState;
}
