import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/blocs/auth-bloc/authbloc.dart';
import 'package:pucardpg/data/secure_storage/secureStore.dart';
import 'package:pucardpg/utils/constants.dart';

import '../model/request/requestInfo.dart';
import '../repositories/app_init_Repo.dart';

class InitClient {
  Dio init() {
    final Dio dio = Dio();
    dio.interceptors.add(AuthTokenInterceptor());
    dio.options.baseUrl = envConfig.variables.baseUrl;

    return dio;
  }
}

class AuthTokenInterceptor extends Interceptor {
  dynamic request;
  final SecureStore secureStore = SecureStore();

  @override
  Future<dynamic> onRequest(
      RequestOptions options,
      RequestInterceptorHandler handler,
      ) async {
    final authToken = await secureStore.getAccessToken();
    final userRequest = await secureStore.getUserRequest();

    if (options.data is Map) {
      options.data = {
        ...options.data,
        "RequestInfo": {
          ...RequestInfoModel(
            apiId: RequestInfoData.apiId,
            ver: RequestInfoData.ver,
            ts: DateTime.now().millisecondsSinceEpoch,
            action: options.path.split('/').last,
            did: RequestInfoData.did,
            key: RequestInfoData.key,
            authToken: authToken,
          ).toJson(),
          "userInfo": userRequest?.toJson()
        }
      };
    }
    return super.onRequest(options, handler);
  }

  @override
  void onError(DioError err, ErrorInterceptorHandler handler) async {

    if (err.type == DioErrorType.response && err.response?.statusCode == 500) {
      scaffoldMessengerKey.currentContext!
          .read<AuthBloc>()
          .add(const AuthEvent.logout());
    }
    else if(err.type == DioErrorType.response && err.response?.statusCode == 401) {
      final response = err.response;
      if (response?.data != null) {
        final Map<String, dynamic> responseData = json.decode(response?.data);

        // Check for the InvalidAccessTokenException error code
        if (responseData['Errors'] != null) {
          for (var error in responseData['Errors']) {

            try {
              final messageString = error['message']!;
              final messageStartIndex = messageString.indexOf('{');
              final messageEndIndex = messageString.lastIndexOf('}') + 1;
              final messageSubstring = messageString.substring(messageStartIndex, messageEndIndex);

              final mainResponse = json.decode(messageSubstring);
              for (var error in mainResponse['Errors']) {
                if (error['code'] == 'InvalidAccessTokenException') {
                  // Trigger the refresh token action
                  scaffoldMessengerKey.currentContext!
                      .read<AuthBloc>()
                      .add(const AuthEvent.refreshToken());
                  break;
                }
              }
            } catch (e1) {
              handler.next(err);
            }
          }
        }
      }
    } else {
      handler.next(err);
    }
  }

  @override
  Future<dynamic> onResponse(
      Response<dynamic> response,
      ResponseInterceptorHandler handler,
      ) async {
    // Notifiers.getToastMessage(scaffoldMessengerKey.currentContext!,
    //     'Created Successfully', 'SUCCESS');
    return handler.next(response);
  }

}