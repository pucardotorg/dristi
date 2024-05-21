import 'package:auto_route/auto_route.dart';
import 'package:flutter/material.dart';
import 'package:pucardpg/blocs/app-localization-bloc/app_localization.dart';
import 'package:pucardpg/pages/aadhar_verification_screen.dart';
import 'package:pucardpg/pages/address_screen.dart';
import 'package:pucardpg/pages/advocate_registration_screen.dart';
import 'package:pucardpg/pages/advocate_success_screen.dart';
import 'package:pucardpg/pages/application_details_screen.dart';
import 'package:pucardpg/pages/authenticated.dart';
import 'package:pucardpg/pages/home.dart';
import 'package:pucardpg/pages/identity_selection_screen.dart';
import 'package:pucardpg/pages/lang_selection.dart';
import 'package:pucardpg/pages/litigant_success_screen.dart';
import 'package:pucardpg/pages/login.dart';
import 'package:pucardpg/pages/login_screen.dart';
import 'package:pucardpg/pages/mobile_number_screen.dart';
import 'package:pucardpg/pages/name_details_screen.dart';
import 'package:pucardpg/pages/other_id_verification_screen.dart';
import 'package:pucardpg/pages/profile.dart';
import 'package:pucardpg/pages/project_selection.dart';
import 'package:pucardpg/pages/terms_and_conditions_screen.dart';
import 'package:pucardpg/pages/unauthenticated.dart';
import 'package:pucardpg/pages/user_home_page.dart';
import 'package:pucardpg/pages/user_type_screen.dart';
import 'package:pucardpg/pages/yet_to_register_screen.dart';

part 'routes.gr.dart';

@AutoRouterConfig(modules: [])
class AppRouter extends _$AppRouter {
  @override
  RouteType get defaultRouteType => const RouteType.material();

  @override
  List<AutoRoute> get routes => [
        AutoRoute(page: UnauthenticatedRouteWrapper.page, path: '/', children: [
          AutoRoute(page: NotRegisteredRoute.page, path: 'not-registered'),
          AutoRoute(page: LoginNumberRoute.page, path: 'login-number'),
          AutoRoute(page: MobileNumberRoute.page, path: 'mobile-number'),
          AutoRoute(page: NameDetailsRoute.page, path: 'name-details'),
          AutoRoute(page: AddressRoute.page, path: 'address'),
          AutoRoute(page: IdentitySelectionRoute.page, path: 'identity-selection'),
          AutoRoute(page: AadharVerificationRoute.page, path: 'aadhar-verification'),
          AutoRoute(page: OtherIdVerificationRoute.page, path: 'other-id-verification'),
          AutoRoute(page: UserTypeRoute.page, path: 'user-type'),
          AutoRoute(page: AdvocateRegistrationRoute.page, path: 'advocate-registration'),
          AutoRoute(page: TermsAndConditionsRoute.page, path: 'terms-and-conditions'),
          AutoRoute(page: LitigantSuccessRoute.page, path: 'litigant-success'),
          AutoRoute(page: AdvocateSuccessRoute.page, path: 'advocate-success'),
          AutoRoute(page: ApplicationDetailsRoute.page, path: 'application-details'),
          AutoRoute(
              page: SelectLanguageRoute.page, initial: true, path: 'langscreen')
        ]),
        AutoRoute(page: AuthenticatedRouteWrapper.page, path: '/', children: [
          AutoRoute(page: UserHomeRoute.page, path: 'user-home', initial: true),
        ]),
      ];
}
