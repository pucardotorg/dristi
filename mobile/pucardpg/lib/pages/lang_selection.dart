import 'package:digit_components/models/digit_row_card/digit_row_card_model.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/blocs/app-init-bloc/app_init.dart';
import 'package:pucardpg/blocs/app-localization-bloc/app_localization.dart';
import 'package:pucardpg/widget/digit_language_selection_card.dart';
import '../blocs/localization-bloc/localization.dart';
import '../routes/routes.dart';
import '../utils/i18_key_constants.dart' as i18;

import 'package:auto_route/auto_route.dart';

@RoutePage()
class SelectLanguageScreen extends StatelessWidget {
  const SelectLanguageScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        drawer: null,
        backgroundColor: Theme.of(context).colorScheme.primary,
        body: context.read<AppInitialization>().state.maybeWhen(
            orElse: () => const Offstage(),
            initialized: (appConfig, serviceRegList) {
              final languages = appConfig.mdmsRes?.commonMasters!.stateInfo?[0].languages;
              return BlocBuilder<LocalizationBloc, LocalizationState>(
                builder: (context, locState) =>
                    Column(mainAxisAlignment: MainAxisAlignment.end, children: [
                  DigitLanguageSelectionCard(
                    // Provide language items to display in DigitRowCardItems
                    digitRowCardItems: languages!.map((e) {
                      return DigitRowCardModel(
                        label: e.label,
                        value: e.value,
                        // Check if the language is selected based on the current locale
                        isSelected:
                            e.value == context.read<LocalizationBloc>().locale,
                      );
                    }).toList(),
                    // Define callback function for language change
                    onLanguageChange: (rowCardValue) {
                      // Dispatch localization event to select the new locale
                      context.read<LocalizationBloc>().add(
                            LocalizationEvent.onSelect(
                              locale: rowCardValue.value,
                              moduleList: appConfig
                                  .mdmsRes?.commonMasters!.stateInfo?[0].localizationModules,
                            ),
                          );
                    },
                    // Define callback function for language submission
                    onLanguageSubmit: () {
                      // Navigate to the LoginRoute upon language submission
                      context.navigateTo(LoginNumberRoute());
                    },
                    // Define label for language submission button
                    languageSubmitLabel: AppLocalizations.of(context)
                        .translate(i18.common.coreCommonContinue),
                  ),
                ]),
              );
            }));
  }
}
