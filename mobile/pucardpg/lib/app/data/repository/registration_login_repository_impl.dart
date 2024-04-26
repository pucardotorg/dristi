import 'dart:convert';
import 'dart:io';

import 'package:dio/dio.dart' as dio;
import 'package:file_picker/file_picker.dart';
import 'package:http/http.dart';
import 'package:http_parser/http_parser.dart';
import 'package:mime/mime.dart';

import 'package:pucardpg/app/data/data_sources/api_service.dart';
import 'package:pucardpg/app/data/models/advocate-clerk-registration-model/advocate_clerk_registration_model.dart';
import 'package:pucardpg/app/data/models/advocate-clerk-search/advocate_clerk_search_model.dart';
import 'package:pucardpg/app/data/models/advocate-registration-model/advocate_registration_model.dart';
import 'package:pucardpg/app/data/models/advocate-search/advocate_search_model.dart';
import 'package:pucardpg/app/data/models/auth-response/auth_response.dart';
import 'package:pucardpg/app/data/models/citizen-registration-request/citizen_registration_request.dart';
import 'package:pucardpg/app/data/models/individual-search/individual_search_model.dart';
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/app/data/models/logout-model/logout_model.dart';
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

      if (httpResponse.response.statusCode == HttpStatus.ok || httpResponse.response.statusCode == HttpStatus.created || httpResponse.response.statusCode == HttpStatus.accepted) {
        return DataSuccess(httpResponse.data);
      } else {
        return DataFailed(
            dio.DioError(
                error: httpResponse.data.error?.message,
                response: httpResponse.response,
                type: dio.DioErrorType.response,
                requestOptions: httpResponse.response.requestOptions
            )
        );
      }
    } on dio.DioError catch(e){
      // if(e.response?.statusCode == 400){
      //   return DataFailed(
      //       dio.DioError(
      //           error: e.response?.data.error?.message,
      //           response: e.response,
      //           type: dio.DioErrorType.response,
      //           requestOptions: e.requestOptions
      //       )
      //   );
      // }
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
            dio.DioError(
                error: "",
                response: httpResponse.response,
                type: dio.DioErrorType.response,
                requestOptions: httpResponse.response.requestOptions
            )
        );
      }
    } on dio.DioError catch(e){
      return DataFailed(e);
    }

  }

  @override
  Future<DataState<ResponseInfoSearch>> logoutUser(LogoutRequest logoutRequest) async {
    try {
      final httpResponse = await _apiService.logoutUser(tenantId, timeStamp, logoutRequest);

      if (httpResponse.response.statusCode == HttpStatus.ok || httpResponse.response.statusCode == HttpStatus.created || httpResponse.response.statusCode == HttpStatus.accepted) {
        return DataSuccess(httpResponse.data);
      } else {
        return DataFailed(
            dio.DioError(
                error: "",
                response: httpResponse.response,
                type: dio.DioErrorType.response,
                requestOptions: httpResponse.response.requestOptions
            )
        );
      }
    } on dio.DioError catch(e){
      return DataFailed(e);
    }

  }

  @override
  Future<DataState<AuthResponse>> getAuthResponse(String username, String password) async {
    // TODO: implement getAuthResponse
    try {
      final httpResponse = await _apiService.getAuthResponse('Basic ZWdvdi11c2VyLWNsaWVudDo=', username, password);

      if (httpResponse.response.statusCode == HttpStatus.ok || httpResponse.response.statusCode == HttpStatus.created || httpResponse.response.statusCode == HttpStatus.accepted) {
        return DataSuccess(httpResponse.data);
      } else {
        return DataFailed(
            dio.DioError(
                error: "",
                response: httpResponse.response,
                type: dio.DioErrorType.response,
                requestOptions: httpResponse.response.requestOptions
            )
        );
      }
    } on dio.DioError catch(e){
      return DataFailed(e);
    }

  }

  @override
  Future<DataState<AdvocateRegistrationResponse>> registerAdvocate(AdvocateRegistrationRequest advocateRegistrationRequest) async {
    try {
      final httpResponse = await _apiService.registerAdvocate('application/json', advocateRegistrationRequest);

      if (httpResponse.response.statusCode == HttpStatus.ok || httpResponse.response.statusCode == HttpStatus.created || httpResponse.response.statusCode == HttpStatus.accepted) {
        return DataSuccess(httpResponse.data);
      } else {
        return DataFailed(
            dio.DioError(
                error: "",
                response: httpResponse.response,
                type: dio.DioErrorType.response,
                requestOptions: httpResponse.response.requestOptions
            )
        );
      }
    } on dio.DioError catch(e){
      return DataFailed(e);
    }
  }

  @override
  Future<DataState<AdvocateClerkRegistrationResponse>> registerAdvocateClerk(AdvocateClerkRegistrationRequest advocateClerkRegistrationRequest) async {
    try {
      final httpResponse = await _apiService.registerAdvocateClerk('application/json', advocateClerkRegistrationRequest);

      if (httpResponse.response.statusCode == HttpStatus.ok || httpResponse.response.statusCode == HttpStatus.created || httpResponse.response.statusCode == HttpStatus.accepted) {
        return DataSuccess(httpResponse.data);
      } else {
        return DataFailed(
            dio.DioError(
                error: "",
                response: httpResponse.response,
                type: dio.DioErrorType.response,
                requestOptions: httpResponse.response.requestOptions
            )
        );
      }
    } on dio.DioError catch(e){
      return DataFailed(e);
    }
  }

  @override
  Future<DataState<String>> getFileStore(PlatformFile pickedFile) async {

    var request = MultipartRequest("POST", Uri.parse('$apiBaseURL/filestore/v1/files'));

    var fileName = '${pickedFile.name}.${pickedFile.extension?.toLowerCase()}';
    MultipartFile multipartFile = MultipartFile.fromBytes(
        'file', pickedFile.bytes!,
        contentType: getMediaType(fileName),
        filename: fileName);
    request.files.add(multipartFile);

    request.fields['tenantId'] = tenantId;
    request.fields['module'] = 'module';
    await request.send().then((response) async {
      if (response.statusCode == 201) {
        dynamic respStr = json.decode(await response.stream.bytesToString());
      }
    });

    return DataSuccess("");

  }

  MediaType getMediaType(String? path) {
    if (path == null) return MediaType('', '');
    String? mimeStr = lookupMimeType(path);
    var fileType = mimeStr?.split('/');
    if (fileType != null && fileType.isNotEmpty) {
      return MediaType(fileType.first, fileType.last);
    } else {
      return MediaType('', '');
    }
  }

  @override
  Future<DataState<IndividualSearchResponse>> searchIndividual(IndividualSearchRequest individualSearchRequest) async {
    try {
      final httpResponse = await _apiService.searchIndividual(limit, offset, tenantId, individualSearchRequest);

      if (httpResponse.response.statusCode == HttpStatus.ok || httpResponse.response.statusCode == HttpStatus.created || httpResponse.response.statusCode == HttpStatus.accepted) {
        return DataSuccess(httpResponse.data);
      } else {
        return DataFailed(
            dio.DioError(
                error: "",
                response: httpResponse.response,
                type: dio.DioErrorType.response,
                requestOptions: httpResponse.response.requestOptions
            )
        );
      }
    } on dio.DioError catch(e){
      return DataFailed(e);
    }
  }

  @override
  Future<DataState<LitigantResponseModel>> registerLitigant(LitigantNetworkModel litigantNetworkModel) async {
    try {
      final httpResponse = await _apiService.registerLitigant(litigantNetworkModel);

      if (httpResponse.response.statusCode == HttpStatus.ok || httpResponse.response.statusCode == HttpStatus.created || httpResponse.response.statusCode == HttpStatus.accepted) {
        return DataSuccess(httpResponse.data);
      } else {
        return DataFailed(
            dio.DioError(
                error: "",
                response: httpResponse.response,
                type: dio.DioErrorType.response,
                requestOptions: httpResponse.response.requestOptions
            )
        );
      }
    } on dio.DioError catch(e){
      return DataFailed(e);
    }
  }

  @override
  Future<DataState<AdvocateSearchResponse>> searchAdvocate(AdvocateSearchRequest advocateSearchRequest) async {
    try {
      final httpResponse = await _apiService.searchAdvocate('application/json', advocateSearchRequest);

      if (httpResponse.response.statusCode == HttpStatus.ok || httpResponse.response.statusCode == HttpStatus.created || httpResponse.response.statusCode == HttpStatus.accepted) {
        return DataSuccess(httpResponse.data);
      } else {
        return DataFailed(
            dio.DioError(
                error: "",
                response: httpResponse.response,
                type: dio.DioErrorType.response,
                requestOptions: httpResponse.response.requestOptions
            )
        );
      }
    } on dio.DioError catch(e){
      return DataFailed(e);
    }
  }

  @override
  Future<DataState<AdvocateClerkSearchResponse>> searchAdvocateClerk(AdvocateClerkSearchRequest advocateClerkSearchRequest) async {
    try {
      final httpResponse = await _apiService.searchAdvocateClerk('application/json', advocateClerkSearchRequest);

      if (httpResponse.response.statusCode == HttpStatus.ok || httpResponse.response.statusCode == HttpStatus.created || httpResponse.response.statusCode == HttpStatus.accepted) {
        return DataSuccess(httpResponse.data);
      } else {
        return DataFailed(
            dio.DioError(
                error: "",
                response: httpResponse.response,
                type: dio.DioErrorType.response,
                requestOptions: httpResponse.response.requestOptions
            )
        );
      }
    } on dio.DioError catch(e){
      return DataFailed(e);
    }
  }

}
