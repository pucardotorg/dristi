import 'dart:async';

import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:pucardpg/data/secure_storage/secureStore.dart';
import 'package:pucardpg/model/auth-response/auth_response.dart';
import 'package:pucardpg/model/citizen-registration-request/citizen_registration_request.dart';
import 'package:pucardpg/model/individual/individual.dart';
import 'package:pucardpg/model/dataModel.dart';
import 'package:pucardpg/model/litigant_model.dart';
import 'package:pucardpg/model/login/loginModel.dart';
import 'package:pucardpg/model/otp-models/otp_model.dart';
// import 'package:pucardpg/model/response/responsemodel.dart';
import 'package:pucardpg/repositories/app_init_Repo.dart';
import 'package:pucardpg/repositories/authRepo.dart';

part 'authbloc.freezed.dart';

class AuthBloc extends Bloc<AuthEvent, AuthState> {

  late String _accesstoken;
  late UserRequest _userRequest;
  late String _refreshtoken;
  final authRepository = AuthRepository();

  AuthBloc() : super(const AuthState.unauthenticated()) {
    on<_AuthLoginEvent>(_onLogin);
    on<_AuthLogoutEvent>(_onLogout);
    on<_AuthLoadEvent>(_onLoad);
  }

  FutureOr<void> _onLogin(
      _AuthLoginEvent event, Emitter<AuthState> emit) async {
    AuthResponse response;
    final secureStore = SecureStore();
    //Send a login request and retrieve the access_token for further requests
    try {
      final loginUrl =
          event.actionMap?[DataModelType.user]?[ApiOperation.login];

      response = await authRepository.validateLogin(
          "/user/oauth/token",
          LoginModel(
            username: event.username,
          )
      );

      _accesstoken = response.accessToken!;
      _refreshtoken = response.refreshToken!;
      _userRequest = response.userRequest!;

      //store accessToken in secure storage
      secureStore.setAccessToken(_accesstoken);

      //store other access Information in secure storage
      secureStore.setAccessInfo(response);

      //change to authenticated state now that we have access
      emit(AuthState.authenticated(
          accesstoken: _accesstoken,
          refreshtoken: _refreshtoken,
          userRequest: _userRequest));

      final actionsWrapper = await authRepository.searchRoleActions({
        "roleCodes": response.userRequest?.roles?.map((e) => e.code).toList(),
        "tenantId": envConfig.variables.tenantId,
        "actionMaster": "actions-test",
        "enabled": true,
      });

      //role actions must also be stored in secureStore so that we don't have to make calls for it repeatedly
      await secureStore.setRoleActions(actionsWrapper);

      // final individualRemoteRepository = IndividualSearchRemoteRepository();

      // final loggedInIndividual =
      //     await individualRemoteRepository.searchIndividual(
      //         IndividualSearchModel(
      //           userUuid: [response.userRequest!.uuid],
      //         ),
      //         event.actionMap);

      // secureStore.setSelectedIndividual(loggedInIndividual.first.id);
    } catch (err) {
      rethrow;
    }
  }

  FutureOr<void> _onRegistration(
      _SubmitRegistrationOtpEvent event, Emitter<AuthState> emit) async {
    AuthResponse response;
    final secureStore = SecureStore();
    //Send a login request and retrieve the access_token for further requests
    try {
      // final loginUrl =
      // event.actionMap?[DataModelType.user]?[ApiOperation.login];

      CitizenRegistrationRequest citizenRegistrationRequest = CitizenRegistrationRequest(
        userInfo: UserInfo(
            name: event.userModel.enteredUserName ?? "",
            username: event.username,
            otpReference: event.otp
        ),
      );

      response = await authRepository.createCitizen(
          "/user/citizen/_create",
          citizenRegistrationRequest
      );
      //change to authenticated state now that we have access
      emit(AuthState.otpCorrect(
          authResponse: response));

      // final individualRemoteRepository = IndividualSearchRemoteRepository();

      // final loggedInIndividual =
      //     await individualRemoteRepository.searchIndividual(
      //         IndividualSearchModel(
      //           userUuid: [response.userRequest!.uuid],
      //         ),
      //         event.actionMap);

      // secureStore.setSelectedIndividual(loggedInIndividual.first.id);
    } catch (err) {
      emit(const AuthState.requestOtpFailed(errorMsg: "failed",));
      rethrow;
    }
  }

  FutureOr<void> _onLogout(_AuthLogoutEvent event, Emitter<AuthState> emit) {
    //when we logout, we need the access token to be deleted and invalidated. All the AccessInfo stored locally is now redundant. Delete it.
    final secureStore = SecureStore();
    secureStore.deleteAccessToken();
    secureStore.deleteAccessInfo();
    secureStore.deleteSelectedIndividual();

    emit(const AuthState.unauthenticated());
  }

  Future<FutureOr<void>> _onLoad(
      _AuthLoadEvent event, Emitter<AuthState> emit) async {
    final secureStore = SecureStore();

    //first attempt to get the accessToken from local secure storage, if successful, the user need not go through the login page again
    AuthResponse? accessInfo;
    accessInfo = await secureStore.getAccessInfo();

    if (accessInfo != null) {
      _accesstoken = accessInfo.accessToken!;
      _refreshtoken = accessInfo.refreshToken!;
      _userRequest = accessInfo.userRequest!;

      emit(AuthState.authenticated(
          accesstoken: _accesstoken,
          refreshtoken: _refreshtoken,
          userRequest: _userRequest));
    } else {
      //stay in the unauthenicated state
      emit(const AuthState.unauthenticated());
    }
  }

  Future<void> _requestOtpEvent(_RequestOtpEvent event,
      Emitter<AuthState> emit) async {

    OtpRequest otpRequestRegister = OtpRequest(
        otp: Otp(
            mobileNumber: event.mobileNumber,
            type: "register"
        )
    );

    OtpRequest otpRequestLogin = OtpRequest(
        otp: Otp(
            mobileNumber: event.mobileNumber,
            type: "login"
        )
    );

    try{
      final registerResponse = await authRepository.requestOtp("/user-otp/v1/_send?tenantId=pg&_=1712987382117", otpRequestRegister);
      emit(const AuthState.otpSucceed(type: "register"));
    }
    catch(e1){
      try{
        final loginResponse = await authRepository.requestOtp("/user-otp/v1/_send?tenantId=pg&_=1712987382117", otpRequestLogin);
        emit(const AuthState.otpSucceed(type: "login"));
      }
      catch(e2){
        emit(const AuthState.requestFailed(errorMsg: 'failed'));
      }
    }

  }

}

@freezed
class AuthEvent with _$AuthEvent {
  const factory AuthEvent.login(
          {String? username,
          String? password,
          Map<DataModelType, Map<ApiOperation, String>>? actionMap}) =
      _AuthLoginEvent;
  const factory AuthEvent.logout() = _AuthLogoutEvent;
  const factory AuthEvent.attemptLoad() = _AuthLoadEvent;
  const factory AuthEvent.requestOtp(String mobileNumber) = _RequestOtpEvent;
  const factory AuthEvent.resendOtp(
      final String mobileNumber, final String type) = _ResendOtpEvent;
  const factory AuthEvent.submitRegistrationOtp(
      final String username, final String otp, UserModel userModel) = _SubmitRegistrationOtpEvent;
  const factory AuthEvent.sendLoginOtpEvent(
      final String username, final String password, UserModel userModel) = _SendLoginOtpEvent;
  const factory AuthEvent.submitLogoutUser(
      final String authToken) = _SubmitLogoutUserEvent;

}

@freezed
class AuthState with _$AuthState {

  const factory AuthState.error() = _ErrorState;
  const factory AuthState.unauthenticated() = _UnauthenticatedState;
  const factory AuthState.authenticated({
    required String accesstoken,
    required String? refreshtoken,
    required UserRequest? userRequest,
  }) = _AuthenticatedState;
  const factory AuthState.otpSucceed({
    required String type,
  }) = _OtpGenerationSuccessState;
  const factory AuthState.resendOtpSucceed({
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
  const factory AuthState.logoutFailedState({
    required String errorMsg
  }) = _LogoutFailedState;

}
