import 'dart:core';

class LitigantModel{

  String? firstName;
  String? middleName;
  String? lastName;

  String? mobileNumber;
  String? identifierType;
  String? identifierId;

  AddressModel? addressModel;

  LitigantModel({
    this.firstName,
    this.middleName,
    this.lastName,
    this.mobileNumber,
    this.identifierType,
    this.identifierId,
    this.addressModel
  });

}

class AddressModel{

  String? doorNo;
  double? latitude;
  double? longitude;
  double? locationAccuracy;
  String? city;
  String? pincode;
  String? street;
  String? district;
  String? state;

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
