// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'mdmsResponse.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$MdmsResponseModelImpl _$$MdmsResponseModelImplFromJson(
        Map<String, dynamic> json) =>
    _$MdmsResponseModelImpl(
      mdmsRes: json['MdmsRes'] == null
          ? null
          : MdmsRes.fromJson(json['MdmsRes'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$$MdmsResponseModelImplToJson(
        _$MdmsResponseModelImpl instance) =>
    <String, dynamic>{
      'MdmsRes': instance.mdmsRes,
    };

_$MdmsResImpl _$$MdmsResImplFromJson(Map<String, dynamic> json) =>
    _$MdmsResImpl(
      commonMasters: json['common-masters'] == null
          ? null
          : CommonMasters.fromJson(
              json['common-masters'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$$MdmsResImplToJson(_$MdmsResImpl instance) =>
    <String, dynamic>{
      'common-masters': instance.commonMasters,
    };

_$CommonMastersImpl _$$CommonMastersImplFromJson(Map<String, dynamic> json) =>
    _$CommonMastersImpl(
      stateInfo: (json['StateInfo'] as List<dynamic>?)
          ?.map((e) => StateInfo.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$$CommonMastersImplToJson(_$CommonMastersImpl instance) =>
    <String, dynamic>{
      'StateInfo': instance.stateInfo,
    };

_$StateInfoImpl _$$StateInfoImplFromJson(Map<String, dynamic> json) =>
    _$StateInfoImpl(
      name: json['name'] as String,
      code: json['code'] as String,
      languages: (json['languages'] as List<dynamic>)
          .map((e) => Language.fromJson(e as Map<String, dynamic>))
          .toList(),
      localizationModules: (json['localizationModules'] as List<dynamic>)
          .map((e) => Modules.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$$StateInfoImplToJson(_$StateInfoImpl instance) =>
    <String, dynamic>{
      'name': instance.name,
      'code': instance.code,
      'languages': instance.languages,
      'localizationModules': instance.localizationModules,
    };

_$ModulesImpl _$$ModulesImplFromJson(Map<String, dynamic> json) =>
    _$ModulesImpl(
      label: json['label'] as String?,
      value: json['value'] as String?,
    );

Map<String, dynamic> _$$ModulesImplToJson(_$ModulesImpl instance) =>
    <String, dynamic>{
      'label': instance.label,
      'value': instance.value,
    };

_$LanguageImpl _$$LanguageImplFromJson(Map<String, dynamic> json) =>
    _$LanguageImpl(
      label: json['label'] as String,
      value: json['value'] as String,
    );

Map<String, dynamic> _$$LanguageImplToJson(_$LanguageImpl instance) =>
    <String, dynamic>{
      'label': instance.label,
      'value': instance.value,
    };
