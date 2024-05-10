import 'package:pucardpg/app/domain/entities/litigant_model.dart';

abstract class FilePickerState {}


class FileInitialState extends FilePickerState {}

class FileLoadingState extends FilePickerState {}

class FileSuccessState extends FilePickerState {
  String fileStoreId;
  FileSuccessState({required this.fileStoreId});
}

class DocumentSuccessState extends FilePickerState {
  String fileStoreId;
  DocumentSuccessState({required this.fileStoreId});
}

class FileFailedState extends FilePickerState {
  String errorMsg;
  FileFailedState({required this.errorMsg});
}

class GetFileSuccessState extends FilePickerState {}