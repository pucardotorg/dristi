import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_event.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_state.dart';
import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/app/domain/usecase/login_usecase.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:pucardpg/core/resources/data_state.dart';

class RegistrationLoginBloc extends Bloc<RegistrationLoginEvent, RegistrationLoginState> {

  final LoginUseCase _loginUseCase;

  RegistrationLoginBloc(this._loginUseCase): super(InitialState()) {
    on<InitialEvent>(initialEvent);
    on<RequestOtpEvent>(requestOtpEvent);
    on<SubmitRegistrationOtpEvent>(sendRegistrationOtpEvent);
    on<SendLoginOtpEvent>(sendLoginOtpEvent);
    on<SubmitLitigantProfileEvent>(submitLitigantProfile);
    on<SubmitAdvocateProfileEvent>(submitAdvocateProfile);
    on<SubmitAdvocateClerkProfileEvent>(submitAdvocateClerkProfile);
  }

  Future<void> initialEvent(InitialEvent event,
      Emitter<RegistrationLoginState> emit) async {

  }

  Future<void> requestOtpEvent(RequestOtpEvent event,
      Emitter<RegistrationLoginState> emit) async {

      emit(LoadingState());

      final dataStateRegister = await _loginUseCase.requestOtp(event.mobileNumber, register);

      if(dataStateRegister is DataSuccess){
        emit(OtpGenerationSuccessState(type: register));
      }
      if(dataStateRegister is DataFailed){
        final dataStateLogin = await _loginUseCase.requestOtp(event.mobileNumber, login);
        if(dataStateLogin is DataSuccess){
          emit(OtpGenerationSuccessState(type: login));
        }
        if(dataStateLogin is DataFailed){
          emit(RequestFailedState(errorMsg: dataStateLogin.error?.message ?? "",));
        }
      }

  }

  Future<void> sendRegistrationOtpEvent(SubmitRegistrationOtpEvent event,
      Emitter<RegistrationLoginState> emit) async {

    emit(LoadingState());

    final dataState = await _loginUseCase.createCitizen(event.username, event.otp, event.userModel);

    if(dataState is DataSuccess){
      emit(OtpCorrectState(authResponse: dataState.data!));
    }
    if(dataState is DataFailed){
      emit(RequestFailedState(errorMsg: dataState.error?.message ?? "",));
    }

  }

  Future<void> sendLoginOtpEvent(SendLoginOtpEvent event,
      Emitter<RegistrationLoginState> emit) async {

    emit(LoadingState());

    final dataState = await _loginUseCase.getAuthResponse(event.username, event.password, event.userModel);

    if(dataState is DataSuccess){
      final dataStateSearch = await _loginUseCase.searchIndividual(dataState.data!.accessToken!, dataState.data!.userRequest!.uuid!);
      if (dataStateSearch is DataSuccess) {
        emit(IndividualSearchSuccessState(individualSearchResponse: dataStateSearch.data!));
      }
      if(dataStateSearch is DataFailed){
        emit(RequestFailedState(errorMsg: dataState.error?.message ?? "",));
      }
    }
    if(dataState is DataFailed){
      emit(RequestFailedState(errorMsg: dataState.error?.message ?? "",));
    }

  }

  Future<void> submitLitigantProfile(SubmitLitigantProfileEvent event,
      Emitter<RegistrationLoginState> emit) async {

    emit(LoadingState());

    final dataState = await _loginUseCase.registerLitigant(event.userModel, litigant);

    if(dataState is DataSuccess){
      emit(LitigantSubmissionSuccessState(litigantResponseModel: dataState.data!));
    }
    if(dataState is DataFailed){
      emit(RequestFailedState(errorMsg: "",));
    }

  }

  Future<void> submitAdvocateProfile(SubmitAdvocateProfileEvent event,
      Emitter<RegistrationLoginState> emit) async {

    emit(LoadingState());

    final dataState = await _loginUseCase.registerLitigant(event.userModel, advocate);

    if(dataState is DataSuccess){
      final dataState1 = await _loginUseCase.registerAdvocate(dataState.data!.individualInfo.individualId, event.userModel);

      if(dataState1 is DataSuccess){
        emit(AdvocateSubmissionSuccessState());
      }
      if(dataState1 is DataFailed){
        emit(RequestFailedState(errorMsg: "",));
      }
    }
    if(dataState is DataFailed){
      emit(RequestFailedState(errorMsg: "",));
    }

  }

  Future<void> submitAdvocateClerkProfile(SubmitAdvocateClerkProfileEvent event,
      Emitter<RegistrationLoginState> emit) async {

    emit(LoadingState());

    final dataState = await _loginUseCase.registerLitigant(event.userModel, clerk);

    if(dataState is DataSuccess){
      final dataState1 = await _loginUseCase.registerAdvocateClerk(dataState.data!.individualInfo.individualId, event.userModel);

      if(dataState1 is DataSuccess){
        emit(AdvocateClerkSubmissionSuccessState());
      }
      if(dataState1 is DataFailed){
        emit(RequestFailedState(errorMsg: "",));
      }    }
    if(dataState is DataFailed){
      emit(RequestFailedState(errorMsg: "",));
    }

  }

}