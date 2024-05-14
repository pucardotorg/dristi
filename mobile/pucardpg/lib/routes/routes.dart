import 'package:auto_route/auto_route.dart';
import 'package:flutter/material.dart';
import 'package:pucardpg/blocs/app-localization-bloc/app_localization.dart';
import 'package:pucardpg/pages/authenticated.dart';
import 'package:pucardpg/pages/home.dart';
import 'package:pucardpg/pages/lang_selection.dart';
import 'package:pucardpg/pages/login.dart';
import 'package:pucardpg/pages/mobile_number_screen.dart';
import 'package:pucardpg/pages/profile.dart';
import 'package:pucardpg/pages/project_selection.dart';
import 'package:pucardpg/pages/unauthenticated.dart';

part 'routes.gr.dart';

@AutoRouterConfig(modules: [])
class AppRouter extends _$AppRouter {
  @override
  RouteType get defaultRouteType => const RouteType.material();

  @override
  List<AutoRoute> get routes => [
        AutoRoute(page: UnauthenticatedRouteWrapper.page, path: '/', children: [
          AutoRoute(page: LoginRoute.page, path: 'login'),
          AutoRoute(page: MobileNumberRoute.page, path: 'mobile-number'),
          AutoRoute(
              page: SelectLanguageRoute.page, initial: true, path: 'langscreen')
        ]),
        AutoRoute(page: AuthenticatedRouteWrapper.page, path: '/', children: [
          AutoRoute(page: HomeRoute.page, path: 'home', initial: true),
          AutoRoute(page: ProfileRoute.page, path: 'profile'),
          AutoRoute(
              page: ProjectSelectionRoute.page,
              path: 'projects',),
        ]),
      ];
}
