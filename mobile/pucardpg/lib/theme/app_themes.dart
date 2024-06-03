import 'dart:ui';

import 'package:digit_components/digit_components.dart';
import 'package:digit_components/theme/digit_theme.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:flutter/material.dart';

class AppTheme{

  const AppTheme._internal();

  static const AppTheme _instance = AppTheme._internal();

  static AppTheme get instance => _instance;

  Color get defaultColor => const Color(0XFF9E400A);
  Color get defaultButtonColor => const Color(0XFF007E7E);
  Color get lightGrey => const Color(0xFF505A5F);
  Color get hintGrey => const Color(0xFFB1B4B6);

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

  DigitMobileTypography get mobileTypography => DigitMobileTypography(
    normalBase: const TextStyle(
      fontFamily: 'Roboto',
    ),
    displayBase: const TextStyle(
      fontFamily: 'Roboto',
    ),
    light: colors.davyGray,
    normal: colors.woodsmokeBlack,
  );

  DigitColors get colors => const DigitColors();

  ThemeData get mobileTheme {
    const Border(top: BorderSide());

    return ThemeData(
      colorScheme: DigitTheme.instance.mobileTheme.colorScheme,
      useMaterial3: false,
      scaffoldBackgroundColor: colorScheme.background,
      textTheme: mobileTypography.textTheme,
      appBarTheme: AppBarTheme(elevation: 0, color: colorScheme.secondary),
      elevatedButtonTheme: elevatedButtonTheme,
      outlinedButtonTheme: outlinedButtonTheme,
      textButtonTheme: textButtonTheme,
      cardTheme: cardTheme,
      inputDecorationTheme: inputDecorationTheme,
      dialogTheme: dialogTheme,
    );
  }

  ColorScheme get colorScheme => ColorScheme(
    brightness: Brightness.light,
    primary: colors.regalBlue,
    onPrimary: colors.white,
    secondary: const Color(0XFF007E7E),
    onSecondary: colors.white,
    error: colors.lavaRed,
    onError: colors.white,
    background: colors.seaShellGray,
    onBackground: colors.woodsmokeBlack,
    surface: colors.alabasterWhite,
    onSurface: colors.woodsmokeBlack,
    onSurfaceVariant: colors.darkSpringGreen,
    tertiaryContainer: colors.tropicalBlue,
    inversePrimary: colors.paleLeafGreen,
    surfaceTint: colors.waterBlue,
    outline: colors.quillGray,
    shadow: colors.davyGray,
    tertiary: colors.paleRose,
    onTertiaryContainer: colors.curiousBlue,
  );

  EdgeInsets get buttonPadding => const EdgeInsets.symmetric(
    vertical: kPadding,
    horizontal: kPadding * 2,
  );

  EdgeInsets get containerMargin => const EdgeInsets.all(kPadding);

  EdgeInsets get verticalMargin => const EdgeInsets.symmetric(
    vertical: kPadding,
  );

  Duration get toastDuration => const Duration(milliseconds: 700);

  OutlinedBorder get buttonBorder => const RoundedRectangleBorder(
    borderRadius: BorderRadius.all(Radius.zero),
  );

  ElevatedButtonThemeData get elevatedButtonTheme => ElevatedButtonThemeData(
    style: ElevatedButton.styleFrom(
      shape: buttonBorder,
      padding: buttonPadding,
      backgroundColor: colorScheme.secondary,
      foregroundColor: colorScheme.onSecondary,
      disabledBackgroundColor: colorScheme.secondary.withOpacity(
        0.5,
      ),
      disabledForegroundColor: colorScheme.onSecondary,
      elevation: 0,
    ),
  );

  OutlinedButtonThemeData get outlinedButtonTheme => OutlinedButtonThemeData(
    style: OutlinedButton.styleFrom(
      foregroundColor: colorScheme.secondary,
      side: BorderSide(color: colorScheme.secondary),
      padding: buttonPadding,
    ),
  );

  TextButtonThemeData get textButtonTheme => TextButtonThemeData(
    style: TextButton.styleFrom(
      shape: buttonBorder,
      padding: buttonPadding,
      textStyle: const TextStyle(fontSize: 16),
      foregroundColor: colorScheme.secondary,
    ),
  );

  CardTheme get cardTheme => const CardTheme(
    margin: EdgeInsets.fromLTRB(kPadding, kPadding * 2, kPadding, 0),
    elevation: 1,
    shape: RoundedRectangleBorder(
      borderRadius: BorderRadius.all(
        Radius.circular(4),
      ),
    ),
  );

  InputDecorationTheme get inputDecorationTheme => InputDecorationTheme(
    enabledBorder: OutlineInputBorder(
      borderRadius: const BorderRadius.all(
        Radius.circular(
          0,
        ),
      ),
      borderSide: BorderSide(
        color: colors.davyGray,
      ),
    ),
    focusedBorder: const OutlineInputBorder(
      borderRadius: BorderRadius.all(
        Radius.circular(
          0,
        ),
      ),
      borderSide: BorderSide(
        color: Color(0XFF007E7E),
        width: 2,
      ),
    ),
    disabledBorder: OutlineInputBorder(
      borderRadius: const BorderRadius.all(
        Radius.circular(
          0,
        ),
      ),
      borderSide: BorderSide(color: colors.cloudGray, width: 1),
    ),
    contentPadding: const EdgeInsets.all(12),
    isDense: true,
    isCollapsed: true,
    floatingLabelBehavior: FloatingLabelBehavior.never,
    errorBorder: OutlineInputBorder(
      borderRadius: const BorderRadius.all(
        Radius.circular(
          0,
        ),
      ),
      borderSide: BorderSide(
        color: colors.lavaRed,
      ),
    ),
    focusedErrorBorder: OutlineInputBorder(
      borderRadius: const BorderRadius.all(
        Radius.circular(
          0,
        ),
      ),
      borderSide: BorderSide(color: colors.lavaRed, width: 2),
    ),
  );

  DialogTheme get dialogTheme => DialogTheme(
    titleTextStyle: mobileTypography.headingL,
    contentTextStyle: mobileTypography.bodyL,
    shape: const RoundedRectangleBorder(
      borderRadius: BorderRadius.all(
        Radius.circular(
          4,
        ),
      ),
    ),
    actionsPadding: const EdgeInsets.all(kPadding),
  );

  BorderSide get tableCellBorder => BorderSide(
    color: colorScheme.outline,
    width: 0.5,
  );

  BorderSide get tableCellStrongBorder => BorderSide(
    color: colorScheme.outline,
    width: 2,
  );
}

