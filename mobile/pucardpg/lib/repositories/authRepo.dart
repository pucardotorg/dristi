import 'dart:convert';
import 'package:dio/dio.dart';
import 'package:pucardpg/data/remote_client.dart';
import 'package:pucardpg/model/auth-response/auth_response.dart';
import 'package:pucardpg/model/citizen-registration-request/citizen_registration_request.dart';
import 'package:pucardpg/model/login/loginModel.dart';
import 'package:pucardpg/model/otp-models/otp_model.dart';
import 'package:pucardpg/model/response/responsemodel.dart';
import 'package:pucardpg/model/role_actions/role_actions_model.dart';
import 'package:pucardpg/repositories/app_init_Repo.dart';

class AuthRepository {
  AuthRepository();
  Future<AuthResponse> validateLogin(String url, LoginModel body) async {
    final formData = body.toJson();

    //make a custom Dio client which will not send the request with the interceptor
    final authClient = Dio();
    // authClient.options.baseUrl = envConfig.variables.baseUrl;
    authClient.options.baseUrl = "https://dristi-qa.pucar.org";

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
    authClient.options.baseUrl = "https://dristi-qa.pucar.org";

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

  Future<OtpResponse> requestOtp(String url, OtpRequest otpRequest) async {
    final formData = otpRequest.toJson();

    //make a custom Dio client which will not send the request with the interceptor
    final authClient = Dio();
    // authClient.options.baseUrl = envConfig.variables.baseUrl;
    authClient.options.baseUrl = "https://dristi-qa.pucar.org";

    final headers = <String, String>{
      "content-type": 'application/x-www-form-urlencoded',
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

  Future<AuthResponse> createCitizen(String url, CitizenRegistrationRequest citizenRegistrationRequest) async {
    final formData = citizenRegistrationRequest.toJson();

    //make a custom Dio client which will not send the request with the interceptor
    final authClient = Dio();
    // authClient.options.baseUrl = envConfig.variables.baseUrl;
    authClient.options.baseUrl = "https://dristi-qa.pucar.org";

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
