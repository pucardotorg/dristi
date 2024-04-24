import 'package:flutter/material.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/presentation/features/address_screen/address_screen.dart';
import 'package:pucardpg/app/presentation/features/advocate-registration-screen/advocate_registration_screen.dart';
import 'package:pucardpg/app/presentation/features/advocate_home_page/advocate_home_page.dart';
import 'package:pucardpg/app/presentation/features/application-details-screen/application_details_screen.dart';
import 'package:pucardpg/app/presentation/features/home_screen/home_screen.dart';
import 'package:pucardpg/app/presentation/features/id-otp-screen/id_otp_screen.dart';
import 'package:pucardpg/app/presentation/features/id-verification-screen/id_verification_screen.dart';
import 'package:pucardpg/app/presentation/features/mobile-number-screen/mobile_number_screen.dart';
import 'package:pucardpg/app/presentation/features/name-details-screen/name_details_screen.dart';
import 'package:pucardpg/app/presentation/features/not-registered-screen/not_registered_screen.dart';
import 'package:pucardpg/app/presentation/features/otp-screen/otp_screen.dart';
import 'package:pucardpg/app/presentation/features/success-screen/success_screen.dart';
import 'package:pucardpg/app/presentation/features/t&c-screen/terms_and_conditions_screen.dart';
import 'package:pucardpg/app/presentation/features/user-home-screen/user_home_page.dart';
import 'package:pucardpg/app/presentation/features/user-type-screen/user_type_screen.dart';



class AppRoutes {
  static Route onGenerateRoutes(RouteSettings settings) {
    switch (settings.name) {
      case '/':
        return _materialRoute(HomeScreen());

      case '/mobileNumberScreen':
        return _materialRoute(MobileNumberScreen(type: settings.arguments as String));

      case '/MobileOtpScreen':
        return _materialRoute(OtpScreen(userModel: settings.arguments as UserModel,));

      case '/YetToRegister':
        return _materialRoute(NotRegisteredScreen(userModel: settings.arguments as UserModel,));

      case '/IdVerificationScreen':
        return _materialRoute(IdVerificationScreen(userModel: settings.arguments as UserModel,));

      case '/IdOtpScreen':
        return _materialRoute(IdOtpScreen(userModel: settings.arguments as UserModel,));

      case '/NameDetailsScreen':
        return _materialRoute(NameDetailsScreen(userModel: settings.arguments as UserModel,));

      case '/AddressScreen':
        return _materialRoute(AddressScreen(userModel: settings.arguments as UserModel,));

      case '/UserTypeScreen':
        return _materialRoute(UserTypeScreen(userModel: settings.arguments as UserModel,));

      case '/TermsAndConditionsScreen':
        return _materialRoute(TermsAndConditionsScreen(userModel: settings.arguments as UserModel,));

      case '/AdvocateRegistrationScreen':
        return _materialRoute(AdvocateRegistrationScreen(userModel: settings.arguments as UserModel,));

      case '/SuccessScreen':
        return _materialRoute(SuccessScreen(userModel: settings.arguments as UserModel,));

      case '/AdvocateHomePage':
        return _materialRoute(AdvocateHomePage(userModel: settings.arguments as UserModel,));

      case '/ViewApplicationScreen':
        return _materialRoute(ApplicationDetailsScreen(userModel: settings.arguments as UserModel,));

      case '/UserHomeScreen':
        return _materialRoute(UserHomeScreen(userModel: settings.arguments as UserModel,));

      default:
        return _materialRoute(HomeScreen());
    }
  }

  static Route<dynamic> _materialRoute(Widget view) {
    return MaterialPageRoute(builder: (_) => view);
  }
}
