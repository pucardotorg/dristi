import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/app/bloc/otp_bloc/otp_event.dart';
import 'package:pucardpg/app/bloc/otp_bloc/otp_state.dart';
import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/app/domain/usecase/login_usecase.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:pucardpg/core/resources/data_state.dart';

class OtpBloc extends Bloc<OtpEvent, OtpState> {

  final LoginUseCase _loginUseCase;

  OtpBloc(this._loginUseCase): super(OtpInitialState()) {
    on<OtpInitialEvent>(otpInitialEvent);
    on<SendOtpEvent>(sendOtpEvent);
  }

  Future<void> otpInitialEvent(OtpInitialEvent event,
      Emitter<OtpState> emit) async {

  }

  Future<void> sendOtpEvent(SendOtpEvent event,
      Emitter<OtpState> emit) async {

      emit(OtpLoadingState());

      OtpRequest otpRequest = OtpRequest(otp: Otp(mobileNumber: event.mobileNumber, type: register));
      final dataState = await _loginUseCase.requestOtp(otpRequest);

      if(dataState is DataSuccess){
        print(otpRequest.toJson());
        print("success");
        emit(OtpSuccessState());
      }
      if(dataState is DataFailed){
        print("failed");
        print("error is ${dataState.error!.message}" ?? "");
        emit(OtpFailedState(errorMsg: dataState.error?.message ?? "",));
      }

  }




}