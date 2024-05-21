import 'package:freezed_annotation/freezed_annotation.dart';

part 'mdmsResponse.freezed.dart';
part 'mdmsResponse.g.dart';

@freezed
class MdmsResponseModel with _$MdmsResponseModel {
  const factory MdmsResponseModel({
    @JsonKey(name: 'MdmsRes') required MdmsRes? mdmsRes,
  }) = _MdmsResponseModel;

  factory MdmsResponseModel.fromJson(
    Map<String, dynamic> json,
  ) =>
      _$MdmsResponseModelFromJson(json);
}

@freezed
class MdmsRes with _$MdmsRes {
  const factory MdmsRes({
    @JsonKey(name: 'common-masters') required CommonMasters? commonMasters
  }) = _MdmsRes;

  factory MdmsRes.fromJson(
    Map<String, dynamic> json,
  ) =>
      _$MdmsResFromJson(json);
}

@freezed
class CommonMasters with _$CommonMasters {
  const factory CommonMasters({
    @JsonKey(name: 'StateInfo') required List<StateInfo>? stateInfo,
  }) = _CommonMasters;

  factory CommonMasters.fromJson(
    Map<String, dynamic> json,
  ) =>
      _$CommonMastersFromJson(json);
}

@freezed
class StateInfo with _$StateInfo {
  factory StateInfo({
    @JsonKey(name: 'name') required String name,
    @JsonKey(name: 'code') required String code,
    @JsonKey(name: 'languages') required List<Language> languages,
    @JsonKey(name: 'localizationModules') required List<Modules> localizationModules
  }) = _StateInfo;

  factory StateInfo.fromJson(Map<String, dynamic> json) =>
      _$StateInfoFromJson(json);
}

@freezed
class Modules with _$Modules {
  const factory Modules({
    @JsonKey(name: 'label') required String? label,
    @JsonKey(name: 'value') required String? value}) =
  _Modules;

  factory Modules.fromJson(
      Map<String, dynamic> json,
      ) =>
      _$ModulesFromJson(json);
}

@freezed
class Language with _$Language {
  const factory Language({
    @JsonKey(name: 'label') required String label,
    @JsonKey(name: 'value') required String value}) =
      _Language;

  factory Language.fromJson(
    Map<String, dynamic> json,
  ) =>
      _$LanguageFromJson(json);
}
