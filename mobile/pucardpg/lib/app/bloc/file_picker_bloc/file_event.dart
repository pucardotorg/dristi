import 'dart:io';

import 'package:dio/dio.dart';

abstract class FilePickerEvent {}

class FileEvent extends FilePickerEvent {
  final MultipartFile multipartFile;
  final File file;
  FileEvent({
    required this.multipartFile,
    required this.file
  });
}