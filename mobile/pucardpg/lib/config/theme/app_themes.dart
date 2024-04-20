import 'dart:ui';

import 'package:digit_components/theme/digit_theme.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:flutter/material.dart';

class AppTheme{

  const AppTheme._internal();

  static const AppTheme _instance = AppTheme._internal();

  static AppTheme get instance => _instance;

  Color get defaultColor => DigitTheme.instance.colors.burningOrange;

  TextStyle? text32W700RobCon() => DigitTheme.instance.mobileTheme.textTheme.displayMedium;
  TextStyle? text24W700() => DigitTheme.instance.mobileTheme.textTheme.headlineLarge;

  TextStyle? text20W700() => DigitTheme.instance.mobileTheme.textTheme.headlineMedium;
  TextStyle? text16W700Rob() => DigitTheme.instance.mobileTheme.textTheme.headlineSmall;

  TextStyle? text16W400Rob() => DigitTheme.instance.mobileTheme.textTheme.bodyLarge;
  TextStyle? text14W400Rob() => DigitTheme.instance.mobileTheme.textTheme.bodyMedium;

  TextStyle? text12W400() => DigitTheme.instance.mobileTheme.textTheme.bodySmall;
  TextStyle? text24W500Rob() => DigitTheme.instance.mobileTheme.textTheme.labelLarge;

  TextStyle? text20W400Rob() => DigitTheme.instance.mobileTheme.textTheme.labelMedium;

  ThemeData theme() {
    return ThemeData(
        scaffoldBackgroundColor: Colors.white,
        fontFamily: 'Muli',
        appBarTheme: appBarTheme()
    );
  }

  AppBarTheme appBarTheme() {
    return const AppBarTheme(
      color: Colors.white,
      elevation: 0,
      centerTitle: true,
      iconTheme: IconThemeData(color: Color(0XFF8B8B8B)),
      titleTextStyle: TextStyle(color: Color(0XFF8B8B8B), fontSize: 18),
    );
  }

  showDigitDialog(bool isError, String msg, BuildContext context){
    DigitToast.show(context,
      options: DigitToastOptions(
        msg,
        isError,
        theme(),
      ),
    );
  }

}


