

import 'package:shared_preferences/shared_preferences.dart';

class AppSharedPreference {

  static late SharedPreferences pref;

  static initializeSharedPref() async {
    pref = await SharedPreferences.getInstance();
  }

  static setPhoneNumber(String phoneNumber) {
    pref.setString('phoneNumber', phoneNumber);
  }

  static String getPhoneNumber() {
    return pref.getString('phoneNumber') ?? '';
  }

}