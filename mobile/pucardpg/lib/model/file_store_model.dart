import 'dart:typed_data';

class FileStoreModel {
  String? documentType;
  Uint8List? bytes;

  FileStoreModel({
    required this.documentType,
    required this.bytes
  });
}