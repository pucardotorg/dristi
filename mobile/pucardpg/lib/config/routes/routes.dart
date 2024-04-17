import 'package:flutter/material.dart';
import 'package:pucardpg/app/presentation/features/address_screen/address_screen.dart';
import 'package:pucardpg/app/presentation/features/advocate-registration-screen/advocate_registration_screen.dart';
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
        return _materialRoute(MobileNumberScreen());

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
        return _materialRoute(TermsAndConditionsScreen());

      case '/AdvocateRegistrationScreen':
        return _materialRoute(AdvocateRegistrationScreen());

      case '/SuccessScreen':
        return _materialRoute(SuccessScreen());

      default:
        return _materialRoute(MobileNumberScreen());
    }
  }

  static Route<dynamic> _materialRoute(Widget view) {
    return MaterialPageRoute(builder: (_) => view);
  }
}
