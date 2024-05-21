import 'package:flutter/material.dart';
import 'package:isar/isar.dart';
import 'app_localization.dart';
import 'package:pucardpg/model/appconfig/mdmsResponse.dart';

//the aim here is to override methods that are defined by default in the localizations delegate file
class AppLocalizationsDelegate extends LocalizationsDelegate<AppLocalizations> {
  final MdmsRes? appConfig;
  final Isar isar;

  const AppLocalizationsDelegate(this.appConfig, this.isar);

  //check from configuration if the language is supported in the app
  @override
  bool isSupported(Locale locale) {
    return (appConfig!.commonMasters?.stateInfo?[0].languages)!.map((e) {
      final results = e.value.split('_');
      if (results.isNotEmpty) return results.first;
      return null;
    }).contains(locale.languageCode);
  }

  @override
  bool shouldReload(covariant LocalizationsDelegate<AppLocalizations> old) =>
      true;

  //load localizations from storage
  @override
  Future<AppLocalizations> load(Locale locale) async {
    AppLocalizations appLocalizations = AppLocalizations(locale, isar);
    await appLocalizations.load();
    return appLocalizations;
  }
}
