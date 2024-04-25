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

  ThemeData get mobileTheme {
    const Border(top: BorderSide());

    return ThemeData(
      colorScheme: DigitTheme.instance.mobileTheme.colorScheme,
      useMaterial3: false,
      scaffoldBackgroundColor: DigitTheme.instance.mobileTheme.colorScheme.background,
      textTheme: DigitTheme.instance.mobileTypography.textTheme,
      appBarTheme: const AppBarTheme(elevation: 0),
      elevatedButtonTheme: elevatedButtonTheme,
      outlinedButtonTheme: DigitTheme.instance.outlinedButtonTheme,
      textButtonTheme: DigitTheme.instance.textButtonTheme,
      cardTheme: DigitTheme.instance.cardTheme,
      inputDecorationTheme: DigitTheme.instance.inputDecorationTheme,
      dialogTheme: DigitTheme.instance.dialogTheme,
    );
  }

  ElevatedButtonThemeData get elevatedButtonTheme => ElevatedButtonThemeData(
    style: ElevatedButton.styleFrom(
      shape: DigitTheme.instance.buttonBorder,
      padding: DigitTheme.instance.buttonPadding,
      backgroundColor: const Color(0xFF007E7E),
      foregroundColor: DigitTheme.instance.mobileTheme.colorScheme.onSecondary,
      disabledBackgroundColor: DigitTheme.instance.mobileTheme.colorScheme.secondary.withOpacity(
        0.5,
      ),
      disabledForegroundColor: DigitTheme.instance.mobileTheme.colorScheme.onSecondary,
      elevation: 0,
    ),
  );

}


