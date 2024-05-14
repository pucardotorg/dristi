import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:pucardpg/model/document-model/document_model.dart';

part 'workflow_model.freezed.dart';
part 'workflow_model.g.dart';

@freezed
class Workflow with _$Workflow {
  const factory Workflow({
    @JsonKey(name: 'action') String? action,
    @JsonKey(name: 'comments') String? comment,
    @JsonKey(name: 'assignes') List<String>? assignees,
    @JsonKey(name: 'documents') List<Document>? documents,
  }) = _Workflow;

  factory Workflow.fromJson(Map<String, dynamic> json) =>
      _$WorkflowFromJson(json);
}