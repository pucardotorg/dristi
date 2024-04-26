import 'dart:io';

import 'package:dio/dio.dart';
import 'package:file_picker/file_picker.dart';

abstract class FilePickerEvent {}

class FileEvent extends FilePickerEvent {
  final PlatformFile pickedFile;
  FileEvent({
    required this.pickedFile
  });
}