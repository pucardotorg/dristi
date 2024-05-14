// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'workflow_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$WorkflowImpl _$$WorkflowImplFromJson(Map<String, dynamic> json) =>
    _$WorkflowImpl(
      action: json['action'] as String?,
      comment: json['comments'] as String?,
      assignees: (json['assignes'] as List<dynamic>?)
          ?.map((e) => e as String)
          .toList(),
      documents: (json['documents'] as List<dynamic>?)
          ?.map((e) => Document.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$$WorkflowImplToJson(_$WorkflowImpl instance) =>
    <String, dynamic>{
      'action': instance.action,
      'comments': instance.comment,
      'assignes': instance.assignees,
      'documents': instance.documents,
    };
