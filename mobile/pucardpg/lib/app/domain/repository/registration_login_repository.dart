



import 'dart:io';

import 'package:dio/dio.dart';
import 'package:pucardpg/app/data/models/advocate-clerk-registration-model/advocate_clerk_registration_model.dart';
import 'package:pucardpg/app/data/models/advocate-clerk-search/advocate_clerk_search_model.dart';
import 'package:pucardpg/app/data/models/advocate-registration-model/advocate_registration_model.dart';
import 'package:pucardpg/app/data/models/advocate-search/advocate_search_model.dart';
import 'package:pucardpg/app/data/models/file-upload-response-model/file_upload_response_model.dart';
import 'package:pucardpg/app/data/models/individual-search/individual_search_model.dart';
import 'package:pucardpg/app/data/models/auth-response/auth_response.dart';
import 'package:pucardpg/app/data/models/citizen-registration-request/citizen_registration_request.dart';
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/app/data/models/logout-model/logout_model.dart';
import 'package:pucardpg/app/data/models/otp-models/otp_model.dart';
import 'package:pucardpg/core/resources/data_state.dart';

abstract class RegistrationLoginRepository {

  Future<DataState<OtpResponse>> requestOtp(OtpRequest otpRequest);

  Future<DataState<ResponseInfoSearch>> logoutUser(LogoutRequest logoutRequest);

  Future<DataState<IndividualSearchResponse>> searchIndividual(IndividualSearchRequest individualSearchRequest);

  Future<DataState<AdvocateRegistrationResponse>> registerAdvocate(AdvocateRegistrationRequest advocateRegistrationRequest);

  Future<DataState<AdvocateClerkRegistrationResponse>> registerAdvocateClerk(AdvocateClerkRegistrationRequest advocateClerkRegistrationRequest);

  Future<DataState<String>> getFileStore(MultipartFile multipartFile, File file);

  Future<DataState<AuthResponse>> getAuthResponse(String username, String password);

  Future<DataState<AuthResponse>> createCitizen(CitizenRegistrationRequest citizenRegistrationRequest);

  Future<DataState<LitigantResponseModel>> registerLitigant(LitigantNetworkModel litigantNetworkModel);

  Future<DataState<AdvocateSearchResponse>> searchAdvocate(AdvocateSearchRequest advocateSearchRequest);

  Future<DataState<AdvocateClerkSearchResponse>> searchAdvocateClerk(AdvocateClerkSearchRequest advocateClerkSearchRequest);

  Future<DataState<FileUploadResponseModel>> uploadFile(File file);
}