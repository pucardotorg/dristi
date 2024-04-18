import 'dart:ffi';

import 'package:dio/dio.dart';
import 'package:pucardpg/app/data/models/auth-model/auth_model.dart';
import 'package:pucardpg/app/data/models/individual-search/individual_search_model.dart';
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/app/data/models/registration-model/registration_model.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:retrofit/retrofit.dart';

part 'api_service.g.dart';

@RestApi(baseUrl: apiBaseURL)
abstract class ApiService {

  factory ApiService(Dio dio) = _ApiService;

  @POST('/user-otp/v1/_send')
  Future<HttpResponse<OtpResponse>> requestOtp(
      @Query('tenantId') String tenantId,
      @Query('_') int _,
      @Body() OtpRequest otpRequest
  );

  @POST('/user/oauth/token')
  Future<HttpResponse<String>> getAuthToken(@Body() AuthModel authModel);

  @POST('/user/citizen/_create')
  Future<HttpResponse<String>> registerUser(@Body() RegistrationModel registrationModel);

  @POST('/individual/v1/_create')
  Future<HttpResponse<String>> registerLitigant(@Body() LitigantNetworkModel litigantRegistrationModel);

  @POST('/individual/v1/_search')
  Future<HttpResponse<IndividualSearchResponse>> searchIndividual(
      @Query('limit') int limit,
      @Query('offset') int offset,
      @Query('tenantId') String tenantId,
      @Body() IndividualSearchRequest individualSearchRequest);

}
