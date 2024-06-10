import 'dart:core';
import 'dart:typed_data';

class UserModel{
   UserModel({this.mobileNumber, this.type});

  int? id;
  String? uuid;
  String? authToken;
  String? username;

  String? enteredUserName;
  String? firstName;
  String? middleName;
  String? lastName;

  String? idVerificationType;

  String? mobileNumber;
  String? identifierType = "";
  String? identifierId = "";
  String? idFilename;
  String? idFileStore = "";
  Uint8List? idBytes;
  String? idDocumentType = "";

  String? type;
  String? userType;

  String? stateRegnNumber;
  String? barRegistrationNumber;
  String? fileStore;
  String? documentFilename;
  String? documentType;
  Uint8List? documentBytes;

  String? individualId;

  AddressModel addressModel = AddressModel();

}

class AddressModel{

  String? doorNo;
  double? latitude;
  double? longitude;
  double? locationAccuracy ;
  String? city;
  String? pincode;
  String? street;
  String? district;
  String? state;
  String? buildingName;

  AddressModel({
    this.doorNo,
    this.latitude,
    this.longitude,
    this.locationAccuracy,
    this.city,
    this.pincode,
    this.street,
    this.district,
    this.state,
    this.buildingName
  });

}

class FileStoreModel {
  String? documentType;
  Uint8List? bytes;

  FileStoreModel({
    required this.documentType,
    required this.bytes
  });
}
