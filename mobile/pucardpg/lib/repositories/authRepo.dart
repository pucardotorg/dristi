import 'dart:convert';
import 'package:dio/dio.dart';
import 'package:pucardpg/data/remote_client.dart';
import 'package:pucardpg/model/advocate-clerk-registration-model/advocate_clerk_registration_model.dart';
import 'package:pucardpg/model/advocate-clerk-search/advocate_clerk_search_model.dart';
import 'package:pucardpg/model/advocate-registration-model/advocate_registration_model.dart';
import 'package:pucardpg/model/advocate-search/advocate_search_model.dart';
import 'package:pucardpg/model/auth-response/auth_response.dart';
import 'package:pucardpg/model/citizen-registration-request/citizen_registration_request.dart';
import 'package:pucardpg/model/individual-search/individual_search_model.dart';
import 'package:pucardpg/model/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/model/login/loginModel.dart';
import 'package:pucardpg/model/otp-models/otp_model.dart';
import 'package:pucardpg/model/role_actions/role_actions_model.dart';
import 'package:pucardpg/repositories/app_init_Repo.dart';
import 'package:pucardpg/utils/i18_key_constants.dart';

class AuthRepository {
  final Dio _client;

  AuthRepository(this._client);

  Future<AuthResponse> validateLogin(String url, LoginModel body) async {
    final formData = body.toJson();

    //make a custom Dio client which will not send the request with the interceptor
    final authClient = Dio();
    // authClient.options.baseUrl = envConfig.variables.baseUrl;
    authClient.options.baseUrl = appConstants.apiBaseURL;

    final headers = <String, String>{
      "content-type": 'application/x-www-form-urlencoded',
      "Access-Control-Allow-Origin": "*",
      "authorization": "Basic ZWdvdi11c2VyLWNsaWVudDo=",
    };

    try {
      final response = await authClient.post(url,
          data: formData, options: Options(headers: headers));
      final responseBody = AuthResponse.fromJson(response.data);

      //close this client so it doesnt interfere with other instances of DioClient
      authClient.close();

      return responseBody;
    } catch (err) {
      rethrow;
    }
  }

  Future<AuthResponse> registerUser(String url, LoginModel body) async {
    final formData = body.toJson();

    //make a custom Dio client which will not send the request with the interceptor
    final authClient = Dio();
    // authClient.options.baseUrl = envConfig.variables.baseUrl;
    authClient.options.baseUrl = appConstants.apiBaseURL;

    final headers = <String, String>{
      "content-type": 'application/json',
      "Access-Control-Allow-Origin": "*",
      "authorization": "Basic ZWdvdi11c2VyLWNsaWVudDo=",
    };

    try {
      final response = await authClient.post(url,
          data: formData, options: Options(headers: headers));
      final responseBody = AuthResponse.fromJson(response.data);

      //close this client so it doesnt interfere with other instances of DioClient
      authClient.close();

      return responseBody;
    } catch (err) {
      rethrow;
    }
  }

  Future<OtpResponse> requestOtp(String url, OtpRequest otpRequest) async {
    final formData = otpRequest.toJson();

    //make a custom Dio client which will not send the request with the interceptor
    final authClient = Dio();
    // authClient.options.baseUrl = envConfig.variables.baseUrl;
    authClient.options.baseUrl = appConstants.apiBaseURL;

    final headers = <String, String>{
      "content-type": 'application/json',
      "Access-Control-Allow-Origin": "*",
      "authorization": "Basic ZWdvdi11c2VyLWNsaWVudDo=",
    };

    try {
      final response = await authClient.post(url,
          data: formData, options: Options(headers: headers));
      final responseBody = OtpResponse.fromJson(response.data);

      //close this client so it doesnt interfere with other instances of DioClient
      authClient.close();

      return responseBody;
    } catch (err) {
      rethrow;
    }
  }

  Future<ResponseInfoSearch> logout(String url, String? accessToken) async {

    _client.options.baseUrl = appConstants.apiBaseURL;

    final headers = <String, String>{
      "content-type": 'application/json',
      "Access-Control-Allow-Origin": "*",
      "Accept": 'application/json'
    };

    try {
      final response = await _client.post(url,
          queryParameters: {
            "tenantId": appConstants.tenantId,
            "_": appConstants.timeStamp
          },
          data: {'access_token': accessToken}, options: Options(headers: headers));
      final responseBody = ResponseInfoSearch.fromJson(response.data);

      return responseBody;
    } catch (err) {
      rethrow;
    }
  }

  Future<AuthResponse> createCitizen(String url, CitizenRegistrationRequest citizenRegistrationRequest) async {
    final formData = citizenRegistrationRequest.toJson();

    //make a custom Dio client which will not send the request with the interceptor
    final authClient = Dio();
    // authClient.options.baseUrl = envConfig.variables.baseUrl;
    authClient.options.baseUrl = appConstants.apiBaseURL;

    final headers = <String, String>{
      "content-type": 'application/json',
      "Access-Control-Allow-Origin": "*",
      "authorization": "Basic ZWdvdi11c2VyLWNsaWVudDo=",
    };

    try {
      final response = await authClient.post(url,
          data: formData, options: Options(headers: headers));
      final responseBody = AuthResponse.fromJson(response.data);

      //close this client so it doesnt interfere with other instances of DioClient
      authClient.close();

      return responseBody;
    } catch (err) {
      rethrow;
    }
  }

  Future<LitigantResponseModel> registerLitigant(String url, LitigantNetworkModel litigantNetworkModel) async {
    try {
      final formData = litigantNetworkModel.toJson();

      // final authClient = Dio();
      _client.options.baseUrl = appConstants.apiBaseURL;

      final headers = <String, String>{
        "content-type": 'application/json',
        "Access-Control-Allow-Origin": "*",
        "Accept": 'application/json'
      };

      final response = await _client.post(url,
          data: formData, options: Options(headers: headers));
      final responseBody = LitigantResponseModel.fromJson(response.data);

      //close this client so it doesnt interfere with other instances of DioClient
      // authClient.close();

      return responseBody;
    } catch(e){
      rethrow;
    }
  }

  Future<AdvocateRegistrationResponse> registerAdvocate(String url, AdvocateRegistrationRequest advocateRegistrationRequest) async {
    try {
      final formData = advocateRegistrationRequest.toJson();

      // final authClient = Dio();
      _client.options.baseUrl = appConstants.apiBaseURL;

      final headers = <String, String>{
        "content-type": 'application/json',
        "Access-Control-Allow-Origin": "*",
        "Accept": 'application/json'
      };

      final response = await _client.post(url,
          data: formData, options: Options(headers: headers));
      final responseBody = AdvocateRegistrationResponse.fromJson(response.data);

      //close this client so it doesnt interfere with other instances of DioClient
      // authClient.close();

      return responseBody;
    } catch(e){
      rethrow;
    }
  }

  @override
  Future<AdvocateClerkRegistrationResponse> registerAdvocateClerk(String url, AdvocateClerkRegistrationRequest advocateClerkRegistrationRequest) async {
    try {
      final formData = advocateClerkRegistrationRequest.toJson();

      // final authClient = Dio();
      _client.options.baseUrl = appConstants.apiBaseURL;

      final headers = <String, String>{
        "content-type": 'application/json',
        "Access-Control-Allow-Origin": "*",
        "Accept": 'application/json'
      };

      final response = await _client.post(url,
          data: formData, options: Options(headers: headers));
      final responseBody = AdvocateClerkRegistrationResponse.fromJson(response.data);

      //close this client so it doesnt interfere with other instances of DioClient
      // authClient.close();

      return responseBody;
    } catch(e){
      rethrow;
    }
  }

  Future<IndividualSearchResponse> searchIndividual(String url, IndividualSearchRequest individualSearchRequest) async {
    try {
      final formData = individualSearchRequest.toJson();

      // final authClient = Dio();
      _client.options.baseUrl = appConstants.apiBaseURL;

      final headers = <String, String>{
        "content-type": 'application/json',
        "Access-Control-Allow-Origin": "*",
        "Accept": 'application/json'
      };

      final response = await _client.post(url,
          data: formData, options: Options(headers: headers));
      final responseBody = IndividualSearchResponse.fromJson(response.data);

      //close this client so it doesnt interfere with other instances of DioClient
      // authClient.close();

      return responseBody;
    } catch(e){
      rethrow;
    }
  }

  @override
  Future<AdvocateSearchResponse> searchAdvocate(String url, AdvocateSearchRequest advocateSearchRequest) async {
    try {
      final formData = advocateSearchRequest.toJson();

      // final authClient = Dio();
      _client.options.baseUrl = appConstants.apiBaseURL;

      final headers = <String, String>{
        "content-type": 'application/json',
        "Access-Control-Allow-Origin": "*",
        "Accept": 'application/json'
      };

      final response = await _client.post(url,
          data: formData, options: Options(headers: headers));
      final responseBody = AdvocateSearchResponse.fromJson(response.data);

      //close this client so it doesnt interfere with other instances of DioClient
      // authClient.close();

      return responseBody;

    } catch(e){
      rethrow;
    }
  }

  @override
  Future<AdvocateClerkSearchResponse> searchAdvocateClerk(String url, AdvocateClerkSearchRequest advocateClerkSearchRequest) async {
    try {
      final formData = advocateClerkSearchRequest.toJson();

      // final authClient = Dio();
      _client.options.baseUrl = appConstants.apiBaseURL;

      final headers = <String, String>{
        "content-type": 'application/json',
        "Access-Control-Allow-Origin": "*",
        "Accept": 'application/json'
      };

      final response = await _client.post(url,
          data: formData, options: Options(headers: headers));
      final responseBody = AdvocateClerkSearchResponse.fromJson(response.data);

      //close this client so it doesnt interfere with other instances of DioClient
      // authClient.close();

      return responseBody;

    } catch (err) {
      rethrow;
    }
  }

  Future<RoleActionsWrapperModel> searchRoleActions(
    Map<String, dynamic> body,
  ) async {
    String url = envConfig.variables.actionMapApiPath;
    final client = DioClient().dio;
    
    try {
      final Response response = await client.post(url, data: body);
      return RoleActionsWrapperModel.fromJson(json.decode(response.toString()));
    } catch (_) {
      rethrow;
    }
  }
}
