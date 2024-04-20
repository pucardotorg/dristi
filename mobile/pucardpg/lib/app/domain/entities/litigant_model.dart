import 'dart:core';

class UserModel{

  int? id;
  String? uuid;
  String? authToken;
  String? username;

  String? firstName;
  String? middleName;
  String? lastName;

  String? mobileNumber;
  String? identifierType;
  String? identifierId = "";

  String? type;
  String? userType;

  AddressModel addressModel = AddressModel();

  UserModel({
    this.firstName,
    this.middleName,
    this.lastName,
    this.mobileNumber,
    this.identifierType,
    this.identifierId,
    this.type,
    this.userType
  });

}

class AddressModel{

  String? doorNo = "";
  double? latitude = 0.0;
  double? longitude = 0.0;
  double? locationAccuracy = 0.0;
  String? city = "";
  String? pincode = "";
  String? street = "";
  String? district = "";
  String? state = "";

  AddressModel({
    this.doorNo,
    this.latitude,
    this.longitude,
    this.locationAccuracy,
    this.city,
    this.pincode,
    this.street,
    this.district,
    this.state
  });

}
