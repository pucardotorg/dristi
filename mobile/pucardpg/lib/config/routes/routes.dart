import 'package:flutter/material.dart';
import 'package:pucardpg/app/presentation/features/mobile-number-screen/mobile_number_screen.dart';
import 'package:pucardpg/app/presentation/features/otp-screen/otp_screen.dart';



class AppRoutes {
  static Route onGenerateRoutes(RouteSettings settings) {
    switch (settings.name) {
      case '/':
        return _materialRoute(const MobileNumberScreen());

      case '/Otp':
        return _materialRoute(OtpScreen(mobile: settings.arguments as String,));

      default:
        return _materialRoute(const MobileNumberScreen());
    }
  }

  static Route<dynamic> _materialRoute(Widget view) {
    return MaterialPageRoute(builder: (_) => view);
  }
}
