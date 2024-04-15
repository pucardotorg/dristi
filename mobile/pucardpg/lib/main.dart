import 'package:digit_components/theme/digit_theme.dart';
import 'package:flutter/material.dart';
import 'package:get/get_navigation/src/root/get_material_app.dart';
import 'package:pucardpg/app/presentation/features/id-verification-screen/id_verification_screen.dart';
import 'package:pucardpg/app/presentation/features/otp-screen/otp_screen.dart';

import 'app/presentation/features/mobile-number-screen/mobile_number_screen.dart';
import 'config/routes/routes.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {

  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {

    return MaterialApp(
      title: 'Flutter Demo',
      theme: DigitTheme.instance.mobileTheme,
      onGenerateRoute: AppRoutes.onGenerateRoutes,
      home: const MobileNumberScreen()
    );

  }
}

