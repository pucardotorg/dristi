import 'dart:ffi';
import 'dart:io';

import 'package:dio/dio.dart';
import 'package:pucardpg/app/data/models/advocate-registration-model/advocate_registration_model.dart';
import 'package:pucardpg/app/data/models/auth-model/auth_model.dart';
import 'package:pucardpg/app/data/models/auth-response/auth_response.dart';
import 'package:pucardpg/app/data/models/citizen-registration-request/citizen_registration_request.dart';
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart' as lr;
import 'package:pucardpg/app/data/models/individual-search/individual_search_model.dart';
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart' as lr;
import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/app/data/models/registration-model/registration_model.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:retrofit/http.dart';
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

  @POST("/user/oauth/token")
  @FormUrlEncoded()  // Specify form data encoding
  Future<HttpResponse<AuthResponse>> getAuthResponse(
      @Header('authorization') String authorization,
      @Field("username") String username,
    [
      @Field("password") String password = "123456",
      @Field("tenantId") String tenantId = "pg",
      @Field("userType") String userType = "citizen",
      @Field("scope") String scope = "read",
      @Field("grant_type") String grantType = "password",
      @Query('_') int _ = 1713357247536,
    ]
  );

  @POST('/user/citizen/_create')
  Future<HttpResponse<AuthResponse>> createCitizen(@Body() CitizenRegistrationRequest citizenRegistrationRequest);

  @POST('/individual/v1/_create')
  Future<HttpResponse<String>> registerLitigant(@Body() lr.LitigantNetworkModel litigantRegistrationModel);

  @POST('/individual/v1/_search')
  Future<HttpResponse<IndividualSearchResponse>> searchIndividual(
      @Query('limit') int limit,
      @Query('offset') int offset,
      @Query('tenantId') String tenantId,
      @Body() IndividualSearchRequest individualSearchRequest);

  @POST('/advocate/v1/_create')
  Future<HttpResponse<AdvocateRegistrationResponse>> registerAdvocate(
      @Body() AdvocateRegistrationRequest advocateRegistrationRequest);

}
