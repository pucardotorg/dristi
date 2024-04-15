import 'package:dio/dio.dart';
import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:retrofit/retrofit.dart';

part 'api_service.g.dart';

@RestApi(baseUrl: apiBaseURL)
abstract class ApiService {

  factory ApiService(Dio dio) = _ApiService;

  @POST('/user-otp/v1/_send')
  Future<HttpResponse<String>> requestOtp(OtpRequest otpRequest);

}