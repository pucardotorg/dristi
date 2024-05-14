import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:pucardpg/model/error-model/error_model.dart';
import 'package:pucardpg/model/individual-search/individual_search_model.dart';
import 'package:pucardpg/model/request-info-model/request_info.dart';

part 'logout_model.freezed.dart';
part 'logout_model.g.dart';

@freezed
class LogoutRequest with _$LogoutRequest {
  const factory LogoutRequest({
    @JsonKey(name: 'access_token') required String? accessToken,    String? uuid,
    @JsonKey(name: 'RequestInfo') required RequestInfo requestInfo,
  }) = _LogoutRequest;

  factory LogoutRequest.fromJson(Map<String, dynamic> json) => _$LogoutRequestFromJson(json);
}

@freezed
class LogoutResponse with _$LogoutResponse {
  const factory LogoutResponse ({
    @JsonKey(name: 'responseInfo') @Default(ResponseInfoSearch(status: "")) ResponseInfoSearch responseInfo,
    @JsonKey(name: 'error') ErrorModel? error,
  }) = _LogoutResponse;

  factory LogoutResponse.fromJson(Map<String, dynamic> json) => _$LogoutResponseFromJson(json);
}