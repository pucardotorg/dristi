import 'package:digit_components/digit_components.dart';
import 'package:digit_components/models/digit_row_card/digit_row_card_model.dart';
import 'package:flutter/material.dart';
import 'package:pucardpg/widget/digit_elevated_revised_button.dart';
import 'package:pucardpg/widget/digit_language_row_card.dart';

class DigitLanguageSelectionCard extends StatelessWidget {
  final List<DigitRowCardModel> digitRowCardItems;
  final ValueChanged<DigitRowCardModel>? onLanguageChange;
  final VoidCallback onLanguageSubmit;
  final String languageSubmitLabel;
  final Widget? appLogo;

  const DigitLanguageSelectionCard({
    super.key,
    required this.digitRowCardItems,
    this.onLanguageChange,
    required this.onLanguageSubmit,
    required this.languageSubmitLabel,
    this.appLogo,
  });

  @override
  Widget build(BuildContext context) {
    return DigitCard(
      padding: const EdgeInsets.symmetric(
        vertical: 16,
        horizontal: 8,
      ),
      margin: const EdgeInsets.all(8),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.end,
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          appLogo ?? const SizedBox.shrink(),
          DigitLanguageRowCard(
            spacing: 20,
            alignment: WrapAlignment.spaceBetween,
            onChanged: onLanguageChange,
            rowItems: digitRowCardItems,
            width:
            (MediaQuery.of(context).size.width / digitRowCardItems.length) -
                14 * digitRowCardItems.length,
          ),
          const SizedBox(
            height: 24,
            width: 0,
          ),
          DigitElevatedRevisedButton(
            onPressed: onLanguageSubmit,
            child: Center(
                child: Text(
                  languageSubmitLabel,
                )),
          ),
        ],
      ),
    );
  }
}