import 'package:flutter/material.dart';
import 'package:pucardpg/app/presentation/features/address_screen/address_screen.dart';
import 'package:pucardpg/app/presentation/features/advocate-registration-screen/advocate_registration_screen.dart';
import 'package:pucardpg/app/presentation/features/advocate_home_page/advocate_home_page.dart';
import 'package:pucardpg/app/presentation/features/home_screen/home_screen.dart';
import 'package:pucardpg/app/presentation/features/id-otp-screen/id_otp_screen.dart';
import 'package:pucardpg/app/presentation/features/id-verification-screen/id_verification_screen.dart';
import 'package:pucardpg/app/presentation/features/mobile-number-screen/mobile_number_screen.dart';
import 'package:pucardpg/app/presentation/features/name-details-screen/name_details_screen.dart';
import 'package:pucardpg/app/presentation/features/otp-screen/otp_screen.dart';
import 'package:pucardpg/app/presentation/features/success-screen/success_screen.dart';
import 'package:pucardpg/app/presentation/features/t&c-screen/terms_and_conditions_screen.dart';
import 'package:pucardpg/app/presentation/features/user-type-screen/user_type_screen.dart';



class AppRoutes {
  static Route onGenerateRoutes(RouteSettings settings) {
    switch (settings.name) {
      case '/':
        return _materialRoute(const HomeScreen());

      case '/mobileNumberScreen':
        return _materialRoute(const MobileNumberScreen());

      case '/MobileOtpScreen':
        return _materialRoute(OtpScreen(mobile: settings.arguments as String,));

      case '/IdVerificationScreen':
        return _materialRoute(IdVerificationScreen(mobile: settings.arguments as String,));

      case '/IdOtpScreen':
        return _materialRoute(IdOtpScreen(mobile: settings.arguments as String,));

      case '/NameDetailsScreen':
        return _materialRoute(NameDetailsScreen(mobile: settings.arguments as String,));

      case '/AddressScreen':
        return _materialRoute(AddressScreen(mobile: settings.arguments as String,));

      case '/UserTypeScreen':
        return _materialRoute(UserTypeScreen(mobile: settings.arguments as String,));

      case '/TermsAndConditionsScreen':
        return _materialRoute(const TermsAndConditionsScreen());

      case '/AdvocateRegistrationScreen':
        return _materialRoute(const AdvocateRegistrationScreen());

      case '/SuccessScreen':
        return _materialRoute(const SuccessScreen());

      case '/AdvocateHomePage':
        return _materialRoute(const AdvocateHomePage());

      default:
        return _materialRoute(const HomeScreen());
    }
  }

  static Route<dynamic> _materialRoute(Widget view) {
    return MaterialPageRoute(builder: (_) => view);
  }
}
