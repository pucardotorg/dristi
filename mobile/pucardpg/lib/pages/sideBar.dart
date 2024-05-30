import 'package:digit_components/digit_components.dart';
import 'package:digit_components/models/digit_row_card/digit_row_card_model.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/blocs/app-init-bloc/app_init.dart';
import 'package:pucardpg/blocs/app-localization-bloc/app_localization.dart';
import 'package:pucardpg/blocs/auth-bloc/authbloc.dart';
import 'package:pucardpg/blocs/localization-bloc/localization.dart';
import 'package:pucardpg/theme/app_themes.dart';
import 'package:pucardpg/widget/digit_language_row_card.dart';
import '../utils/i18_key_constants.dart' as i18;

class SideBar extends StatelessWidget {
  // final Map<DataModelType, Map<ApiOperation, String>> actionMap;
  const SideBar({
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final theme = AppTheme.instance;
    var t = AppLocalizations.of(context);
    var home = t.translate(i18.common.esCommonHome);
    var logout = t.translate(i18.common.coreCommonLogout);
    var language = t.translate(i18.common.csCommonLanguage);

    // return BlocBuilder<AppInitialization, InitState>(
    //   builder: (context, state) {
    //     final actionMap = state.entityActionMapping;
    return BlocBuilder<AuthBloc, AuthState>(
      builder: (context, state) {
        return Column(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Container(
                color: theme.colorScheme.secondary.withOpacity(0.12),
                child: SizedBox(
                  width: MediaQuery.of(context).size.width,
                  height: 200,
                  child: state.maybeWhen(
                      orElse: () => const Offstage(),
                      // orElse: () => const Text('Side Bar could not load'),
                      authenticated: (accessToken, refreshToken, userRequest) {
                        return Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Text(
                              userRequest!.name ?? "",
                              style: theme.mobileTypography.textTheme.headlineLarge,
                            ),
                            Text(
                              userRequest.mobileNumber ?? "",
                              style: theme.mobileTypography.textTheme.headlineLarge?.apply(
                                color: theme.lightGrey
                              ),
                            ),
                          ],
                        );
                      }),
                )),
            DigitIconTile(
              title: home,
              icon: Icons.home,
              onPressed: () {
                // AutoRouter.of(context).replace(HomeRoute());
              },
            ),
            DigitIconTile(
              title: logout,
              icon: Icons.logout,
              onPressed: () {
                context.read<AuthBloc>().add(const AuthEvent.logout());
              },
            ),
            SizedBox(
                width: MediaQuery.of(context).size.width,
                child: DigitIconTile(
                  icon: Icons.language,
                  onPressed: () {},
                  title: language,
                  content: BlocBuilder<AppInitialization, InitState>(
                      builder: (context, state) => state.maybeWhen(
                            orElse: () => const Text('Could not load'),
                            initialized: (appConfig, serviceReg) {
                              final languages =
                                  appConfig.mdmsRes?.commonMasters!.stateInfo?[0].languages;

                              return BlocBuilder<LocalizationBloc,
                                      LocalizationState>(
                                  builder: (context, locState) {
                                return Padding(
                                  padding: const EdgeInsets.only(top: 8.0),
                                  child: DigitLanguageRowCard(
                                    rowItems: languages!.map((e) {
                                      return DigitRowCardModel(
                                          label: e.label,
                                          value: e.value,
                                          isSelected: e.value ==
                                              context
                                                  .read<LocalizationBloc>()
                                                  .locale);
                                    }).toList(),
                                    width: MediaQuery.of(context).size.width,
                                    onChanged: (rowCardValue) {
                                      context.read<LocalizationBloc>().add(
                                          LocalizationEvent.onSelect(
                                              locale: rowCardValue.value,
                                              moduleList: appConfig
                                                  .mdmsRes?.commonMasters!.stateInfo?[0]
                                                  .localizationModules));
                                    },
                                  ),
                                );
                              });
                            },
                          )),
                )),
            // DigitIconTile(
            //   title: AppLocalizations.of(context)
            //       .translate(i18.common.coreCommonProfile),
            //   icon: Icons.person,
            //   onPressed: () {
            //     // AutoRouter.of(context).replace(ProfileRoute());
            //   },
            // ),
            SizedBox(
              height: MediaQuery.of(context).size.height / 3,
            ),
            const PoweredByDigit(
              version: 'v1',
            ),
            const SizedBox(height: 5,),
          ],
        );
      },
    );
    //   },
    // );
  }
}
