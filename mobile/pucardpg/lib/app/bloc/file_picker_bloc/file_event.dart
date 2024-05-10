import 'dart:io';

import 'package:dio/dio.dart';
import 'package:file_picker/file_picker.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';

abstract class FilePickerEvent {}

class FileEvent extends FilePickerEvent {
  final PlatformFile pickedFile;
  final String type;
  FileEvent({
    required this.pickedFile,
    required this.type
  });
}

class GetFileEvent extends FilePickerEvent {
  final UserModel userModel;
  GetFileEvent({
    required this.userModel
  });
}