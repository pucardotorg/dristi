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
  }

  Future<void> initialEvent(InitialEvent event,
      Emitter<RegistrationLoginState> emit) async {

  }

  Future<void> requestOtpEvent(RequestOtpEvent event,
      Emitter<RegistrationLoginState> emit) async {

      emit(LoadingState());

      final dataState = await _loginUseCase.requestOtp(event.mobileNumber, event.type);

      if(dataState is DataSuccess){
        emit(OtpGenerationSuccessState());
      }
      if(dataState is DataFailed){
        emit(RequestFailedState(errorMsg: dataState.error?.message ?? "",));
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

    final dataState = await _loginUseCase.registerLitigant(event.userModel);

    if(dataState is DataSuccess){
      emit(LitigantSubmissionSuccessState());
    }
    if(dataState is DataFailed){
      emit(RequestFailedState(errorMsg: "",));
    }

  }

}