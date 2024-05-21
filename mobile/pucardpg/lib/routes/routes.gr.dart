// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// AutoRouterGenerator
// **************************************************************************

// ignore_for_file: type=lint
// coverage:ignore-file

part of 'routes.dart';

abstract class _$AppRouter extends RootStackRouter {
  // ignore: unused_element
  _$AppRouter({super.navigatorKey});

  @override
  final Map<String, PageFactory> pagesMap = {
    AadharVerificationRoute.name: (routeData) {
      final args = routeData.argsAs<AadharVerificationRouteArgs>(
          orElse: () => const AadharVerificationRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: AadharVerificationScreen(key: args.key),
      );
    },
    AddressRoute.name: (routeData) {
      final args = routeData.argsAs<AddressRouteArgs>(
          orElse: () => const AddressRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: AddressScreen(key: args.key),
      );
    },
    AdvocateRegistrationRoute.name: (routeData) {
      final args = routeData.argsAs<AdvocateRegistrationRouteArgs>(
          orElse: () => const AdvocateRegistrationRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: AdvocateRegistrationScreen(key: args.key),
      );
    },
    AdvocateSuccessRoute.name: (routeData) {
      final args = routeData.argsAs<AdvocateSuccessRouteArgs>(
          orElse: () => const AdvocateSuccessRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: AdvocateSuccessScreen(key: args.key),
      );
    },
    ApplicationDetailsRoute.name: (routeData) {
      final args = routeData.argsAs<ApplicationDetailsRouteArgs>(
          orElse: () => const ApplicationDetailsRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: ApplicationDetailsScreen(key: args.key),
      );
    },
    AuthenticatedRouteWrapper.name: (routeData) {
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: const AuthenticatedScreenWrapper(),
      );
    },
    IdentitySelectionRoute.name: (routeData) {
      final args = routeData.argsAs<IdentitySelectionRouteArgs>(
          orElse: () => const IdentitySelectionRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: IdentitySelectionScreen(key: args.key),
      );
    },
    LitigantSuccessRoute.name: (routeData) {
      final args = routeData.argsAs<LitigantSuccessRouteArgs>(
          orElse: () => const LitigantSuccessRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: LitigantSuccessScreen(key: args.key),
      );
    },
    LoginNumberRoute.name: (routeData) {
      final args = routeData.argsAs<LoginNumberRouteArgs>(
          orElse: () => const LoginNumberRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: LoginNumberScreen(key: args.key),
      );
    },
    MobileNumberRoute.name: (routeData) {
      final args = routeData.argsAs<MobileNumberRouteArgs>(
          orElse: () => const MobileNumberRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: MobileNumberScreen(key: args.key),
      );
    },
    NameDetailsRoute.name: (routeData) {
      final args = routeData.argsAs<NameDetailsRouteArgs>(
          orElse: () => const NameDetailsRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: NameDetailsScreen(key: args.key),
      );
    },
    NotRegisteredRoute.name: (routeData) {
      final args = routeData.argsAs<NotRegisteredRouteArgs>(
          orElse: () => const NotRegisteredRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: NotRegisteredScreen(key: args.key),
      );
    },
    OtherIdVerificationRoute.name: (routeData) {
      final args = routeData.argsAs<OtherIdVerificationRouteArgs>(
          orElse: () => const OtherIdVerificationRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: OtherIdVerificationScreen(key: args.key),
      );
    },
    SelectLanguageRoute.name: (routeData) {
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: const SelectLanguageScreen(),
      );
    },
    TermsAndConditionsRoute.name: (routeData) {
      final args = routeData.argsAs<TermsAndConditionsRouteArgs>(
          orElse: () => const TermsAndConditionsRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: TermsAndConditionsScreen(key: args.key),
      );
    },
    UnauthenticatedRouteWrapper.name: (routeData) {
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: const UnauthenticatedScreenWrapper(),
      );
    },
    UserHomeRoute.name: (routeData) {
      final args = routeData.argsAs<UserHomeRouteArgs>(
          orElse: () => const UserHomeRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: UserHomeScreen(key: args.key),
      );
    },
    UserTypeRoute.name: (routeData) {
      final args = routeData.argsAs<UserTypeRouteArgs>(
          orElse: () => const UserTypeRouteArgs());
      return AutoRoutePage<dynamic>(
        routeData: routeData,
        child: UserTypeScreen(key: args.key),
      );
    },
  };
}

/// generated route for
/// [AadharVerificationScreen]
class AadharVerificationRoute
    extends PageRouteInfo<AadharVerificationRouteArgs> {
  AadharVerificationRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          AadharVerificationRoute.name,
          args: AadharVerificationRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'AadharVerificationRoute';

  static const PageInfo<AadharVerificationRouteArgs> page =
      PageInfo<AadharVerificationRouteArgs>(name);
}

class AadharVerificationRouteArgs {
  const AadharVerificationRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'AadharVerificationRouteArgs{key: $key}';
  }
}

/// generated route for
/// [AddressScreen]
class AddressRoute extends PageRouteInfo<AddressRouteArgs> {
  AddressRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          AddressRoute.name,
          args: AddressRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'AddressRoute';

  static const PageInfo<AddressRouteArgs> page =
      PageInfo<AddressRouteArgs>(name);
}

class AddressRouteArgs {
  const AddressRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'AddressRouteArgs{key: $key}';
  }
}

/// generated route for
/// [AdvocateRegistrationScreen]
class AdvocateRegistrationRoute
    extends PageRouteInfo<AdvocateRegistrationRouteArgs> {
  AdvocateRegistrationRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          AdvocateRegistrationRoute.name,
          args: AdvocateRegistrationRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'AdvocateRegistrationRoute';

  static const PageInfo<AdvocateRegistrationRouteArgs> page =
      PageInfo<AdvocateRegistrationRouteArgs>(name);
}

class AdvocateRegistrationRouteArgs {
  const AdvocateRegistrationRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'AdvocateRegistrationRouteArgs{key: $key}';
  }
}

/// generated route for
/// [AdvocateSuccessScreen]
class AdvocateSuccessRoute extends PageRouteInfo<AdvocateSuccessRouteArgs> {
  AdvocateSuccessRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          AdvocateSuccessRoute.name,
          args: AdvocateSuccessRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'AdvocateSuccessRoute';

  static const PageInfo<AdvocateSuccessRouteArgs> page =
      PageInfo<AdvocateSuccessRouteArgs>(name);
}

class AdvocateSuccessRouteArgs {
  const AdvocateSuccessRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'AdvocateSuccessRouteArgs{key: $key}';
  }
}

/// generated route for
/// [ApplicationDetailsScreen]
class ApplicationDetailsRoute
    extends PageRouteInfo<ApplicationDetailsRouteArgs> {
  ApplicationDetailsRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          ApplicationDetailsRoute.name,
          args: ApplicationDetailsRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'ApplicationDetailsRoute';

  static const PageInfo<ApplicationDetailsRouteArgs> page =
      PageInfo<ApplicationDetailsRouteArgs>(name);
}

class ApplicationDetailsRouteArgs {
  const ApplicationDetailsRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'ApplicationDetailsRouteArgs{key: $key}';
  }
}

/// generated route for
/// [AuthenticatedScreenWrapper]
class AuthenticatedRouteWrapper extends PageRouteInfo<void> {
  const AuthenticatedRouteWrapper({List<PageRouteInfo>? children})
      : super(
          AuthenticatedRouteWrapper.name,
          initialChildren: children,
        );

  static const String name = 'AuthenticatedRouteWrapper';

  static const PageInfo<void> page = PageInfo<void>(name);
}

/// generated route for
/// [IdentitySelectionScreen]
class IdentitySelectionRoute extends PageRouteInfo<IdentitySelectionRouteArgs> {
  IdentitySelectionRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          IdentitySelectionRoute.name,
          args: IdentitySelectionRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'IdentitySelectionRoute';

  static const PageInfo<IdentitySelectionRouteArgs> page =
      PageInfo<IdentitySelectionRouteArgs>(name);
}

class IdentitySelectionRouteArgs {
  const IdentitySelectionRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'IdentitySelectionRouteArgs{key: $key}';
  }
}

/// generated route for
/// [LitigantSuccessScreen]
class LitigantSuccessRoute extends PageRouteInfo<LitigantSuccessRouteArgs> {
  LitigantSuccessRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          LitigantSuccessRoute.name,
          args: LitigantSuccessRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'LitigantSuccessRoute';

  static const PageInfo<LitigantSuccessRouteArgs> page =
      PageInfo<LitigantSuccessRouteArgs>(name);
}

class LitigantSuccessRouteArgs {
  const LitigantSuccessRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'LitigantSuccessRouteArgs{key: $key}';
  }
}

/// generated route for
/// [LoginNumberScreen]
class LoginNumberRoute extends PageRouteInfo<LoginNumberRouteArgs> {
  LoginNumberRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          LoginNumberRoute.name,
          args: LoginNumberRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'LoginNumberRoute';

  static const PageInfo<LoginNumberRouteArgs> page =
      PageInfo<LoginNumberRouteArgs>(name);
}

class LoginNumberRouteArgs {
  const LoginNumberRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'LoginNumberRouteArgs{key: $key}';
  }
}

/// generated route for
/// [MobileNumberScreen]
class MobileNumberRoute extends PageRouteInfo<MobileNumberRouteArgs> {
  MobileNumberRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          MobileNumberRoute.name,
          args: MobileNumberRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'MobileNumberRoute';

  static const PageInfo<MobileNumberRouteArgs> page =
      PageInfo<MobileNumberRouteArgs>(name);
}

class MobileNumberRouteArgs {
  const MobileNumberRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'MobileNumberRouteArgs{key: $key}';
  }
}

/// generated route for
/// [NameDetailsScreen]
class NameDetailsRoute extends PageRouteInfo<NameDetailsRouteArgs> {
  NameDetailsRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          NameDetailsRoute.name,
          args: NameDetailsRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'NameDetailsRoute';

  static const PageInfo<NameDetailsRouteArgs> page =
      PageInfo<NameDetailsRouteArgs>(name);
}

class NameDetailsRouteArgs {
  const NameDetailsRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'NameDetailsRouteArgs{key: $key}';
  }
}

/// generated route for
/// [NotRegisteredScreen]
class NotRegisteredRoute extends PageRouteInfo<NotRegisteredRouteArgs> {
  NotRegisteredRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          NotRegisteredRoute.name,
          args: NotRegisteredRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'NotRegisteredRoute';

  static const PageInfo<NotRegisteredRouteArgs> page =
      PageInfo<NotRegisteredRouteArgs>(name);
}

class NotRegisteredRouteArgs {
  const NotRegisteredRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'NotRegisteredRouteArgs{key: $key}';
  }
}

/// generated route for
/// [OtherIdVerificationScreen]
class OtherIdVerificationRoute
    extends PageRouteInfo<OtherIdVerificationRouteArgs> {
  OtherIdVerificationRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          OtherIdVerificationRoute.name,
          args: OtherIdVerificationRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'OtherIdVerificationRoute';

  static const PageInfo<OtherIdVerificationRouteArgs> page =
      PageInfo<OtherIdVerificationRouteArgs>(name);
}

class OtherIdVerificationRouteArgs {
  const OtherIdVerificationRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'OtherIdVerificationRouteArgs{key: $key}';
  }
}

/// generated route for
/// [SelectLanguageScreen]
class SelectLanguageRoute extends PageRouteInfo<void> {
  const SelectLanguageRoute({List<PageRouteInfo>? children})
      : super(
          SelectLanguageRoute.name,
          initialChildren: children,
        );

  static const String name = 'SelectLanguageRoute';

  static const PageInfo<void> page = PageInfo<void>(name);
}

/// generated route for
/// [TermsAndConditionsScreen]
class TermsAndConditionsRoute
    extends PageRouteInfo<TermsAndConditionsRouteArgs> {
  TermsAndConditionsRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          TermsAndConditionsRoute.name,
          args: TermsAndConditionsRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'TermsAndConditionsRoute';

  static const PageInfo<TermsAndConditionsRouteArgs> page =
      PageInfo<TermsAndConditionsRouteArgs>(name);
}

class TermsAndConditionsRouteArgs {
  const TermsAndConditionsRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'TermsAndConditionsRouteArgs{key: $key}';
  }
}

/// generated route for
/// [UnauthenticatedScreenWrapper]
class UnauthenticatedRouteWrapper extends PageRouteInfo<void> {
  const UnauthenticatedRouteWrapper({List<PageRouteInfo>? children})
      : super(
          UnauthenticatedRouteWrapper.name,
          initialChildren: children,
        );

  static const String name = 'UnauthenticatedRouteWrapper';

  static const PageInfo<void> page = PageInfo<void>(name);
}

/// generated route for
/// [UserHomeScreen]
class UserHomeRoute extends PageRouteInfo<UserHomeRouteArgs> {
  UserHomeRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          UserHomeRoute.name,
          args: UserHomeRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'UserHomeRoute';

  static const PageInfo<UserHomeRouteArgs> page =
      PageInfo<UserHomeRouteArgs>(name);
}

class UserHomeRouteArgs {
  const UserHomeRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'UserHomeRouteArgs{key: $key}';
  }
}

/// generated route for
/// [UserTypeScreen]
class UserTypeRoute extends PageRouteInfo<UserTypeRouteArgs> {
  UserTypeRoute({
    Key? key,
    List<PageRouteInfo>? children,
  }) : super(
          UserTypeRoute.name,
          args: UserTypeRouteArgs(key: key),
          initialChildren: children,
        );

  static const String name = 'UserTypeRoute';

  static const PageInfo<UserTypeRouteArgs> page =
      PageInfo<UserTypeRouteArgs>(name);
}

class UserTypeRouteArgs {
  const UserTypeRouteArgs({this.key});

  final Key? key;

  @override
  String toString() {
    return 'UserTypeRouteArgs{key: $key}';
  }
}
