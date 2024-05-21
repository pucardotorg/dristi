// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'pin_address_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$PlaceDetailsListImpl _$$PlaceDetailsListImplFromJson(
        Map<String, dynamic> json) =>
    _$PlaceDetailsListImpl(
      results: (json['results'] as List<dynamic>)
          .map((e) => Result.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$$PlaceDetailsListImplToJson(
        _$PlaceDetailsListImpl instance) =>
    <String, dynamic>{
      'results': instance.results,
    };
