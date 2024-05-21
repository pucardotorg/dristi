import 'dart:convert';
import 'package:file_picker/file_picker.dart';
import 'package:http/http.dart';
import 'package:http_parser/http_parser.dart';
import 'package:mime/src/mime_type.dart';
import 'package:pucardpg/model/file_store_model.dart';
import 'package:pucardpg/utils/i18_key_constants.dart';

class FileRepository {
  FileRepository();

  String baseUrl = appConstants.apiBaseURL;
  Future<String> uploadFile(String url, PlatformFile pickedFile) async {
    try {
      var request = MultipartRequest("POST", Uri.parse(baseUrl + url));

      var fileName = '${pickedFile.name}.${pickedFile.extension?.toLowerCase()}';
      MultipartFile multipartFile = MultipartFile.fromBytes(
          'file', pickedFile.bytes!,
          contentType: getMediaType(fileName),
          filename: fileName);
      request.files.add(multipartFile);

      request.fields['tenantId'] = 'pg';
      request.fields['module'] = 'module';
      var httpResponse = await request.send();
      Map? respStr = json.decode(await httpResponse.stream.bytesToString());
      List<dynamic> files = respStr?['files'];
      return files[0]['fileStoreId'];
    } catch (err) {
      rethrow;
    }
  }

  MediaType getMediaType(String? path) {
    if (path == null) return MediaType('', '');
    String? mimeStr = lookupMimeType(path);
    var fileType = mimeStr?.split('/');
    if (fileType != null && fileType.isNotEmpty) {
      return MediaType(fileType.first, fileType.last);
    } else {
      return MediaType('', '');
    }
  }

  Future<FileStoreModel> getFileData(String url, String tenantId, String fileStoreId) async {
    var res = await get(Uri.parse(baseUrl + '$url?tenantId=$tenantId&fileStoreId=$fileStoreId'));
    final bytes = res.bodyBytes;
    final documentType = res.headers['content-type']!.split('/').last;
    FileStoreModel fileStoreModel = FileStoreModel(documentType: documentType, bytes: bytes);
    return fileStoreModel;
  }

  static String getExtension(String url) {
    final fileName = url.substring(0, url.indexOf('?')).split('/').last;
    return fileName.split('.').last;
  }
}
