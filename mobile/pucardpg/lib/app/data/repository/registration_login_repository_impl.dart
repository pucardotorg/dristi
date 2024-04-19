

import 'dart:io';

import 'package:dio/dio.dart';

import 'package:pucardpg/app/data/data_sources/api_service.dart';
import 'package:pucardpg/app/data/models/auth-response/auth_response.dart';
import 'package:pucardpg/app/data/models/citizen-registration-request/citizen_registration_request.dart';
import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/app/domain/repository/registration_login_repository.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:pucardpg/core/resources/data_state.dart';

class RegistrationLoginRepositoryImpl implements RegistrationLoginRepository {

  final ApiService _apiService;

  RegistrationLoginRepositoryImpl(this._apiService);

  @override
  Future<DataState<OtpResponse>> requestOtp(OtpRequest otpRequest) async {
    // TODO: implement requestOtp
    try {
      final httpResponse = await _apiService.requestOtp(tenantId, timeStamp, otpRequest);

      if (httpResponse.response.statusCode == HttpStatus.ok || httpResponse.response.statusCode == HttpStatus.created) {
        return DataSuccess(httpResponse.data);
      } else {
        return DataFailed(
            DioError(
                error: httpResponse.data.error?.message,
                response: httpResponse.response,
                type: DioErrorType.response,
                requestOptions: httpResponse.response.requestOptions
            )
        );
      }
    } on DioError catch(e){
      return DataFailed(e);
    }

  }

  @override
  Future<DataState<AuthResponse>> createCitizen(CitizenRegistrationRequest citizenRegistrationRequest) async {
    // TODO: implement createCitizen
    try {
      final httpResponse = await _apiService.createCitizen(citizenRegistrationRequest);

      if (httpResponse.response.statusCode == HttpStatus.ok || httpResponse.response.statusCode == HttpStatus.created || httpResponse.response.statusCode == HttpStatus.accepted) {
        return DataSuccess(httpResponse.data);
      } else {
        return DataFailed(
            DioError(
                error: httpResponse.data.errorDescription,
                response: httpResponse.response,
                type: DioErrorType.response,
                requestOptions: httpResponse.response.requestOptions
            )
        );
      }
    } on DioError catch(e){
      return DataFailed(e);
    }

  }

  @override
  Future<DataState<AuthResponse>> getAuthResponse(String username, String password) async {
    // TODO: implement getAuthResponse
    try {
      final httpResponse = await _apiService.getAuthResponse(username);

      if (httpResponse.response.statusCode == HttpStatus.ok || httpResponse.response.statusCode == HttpStatus.created || httpResponse.response.statusCode == HttpStatus.accepted) {
        return DataSuccess(httpResponse.data);
      } else {
        return DataFailed(
            DioError(
                error: httpResponse.data.errorDescription,
                response: httpResponse.response,
                type: DioErrorType.response,
                requestOptions: httpResponse.response.requestOptions
            )
        );
      }
    } on DioError catch(e){
      return DataFailed(e);
    }

  }

}
