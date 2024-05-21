import 'package:digit_components/models/digit_row_card/digit_row_card_model.dart';
import 'package:digit_components/theme/digit_theme.dart';
import 'package:flutter/material.dart';
import 'package:pucardpg/theme/app_themes.dart';

class DigitLanguageRowCard extends StatelessWidget {
  final List<DigitRowCardModel> rowItems;
  final ValueChanged<DigitRowCardModel>? onChanged;
  final double width;
  final double spacing;
  final WrapAlignment alignment;

  const DigitLanguageRowCard(
      {super.key,
        this.onChanged,
        required this.rowItems,
        required this.width,
        this.spacing = kPadding,
        this.alignment = WrapAlignment.start});

  @override
  Widget build(BuildContext context) {
    final theme = AppTheme.instance;

    return Wrap(
      alignment: alignment,
      spacing: spacing,
      children: rowItems
          .map(
            (e) => Padding(
          padding: const EdgeInsets.only(top: kPadding),
          child: IntrinsicWidth(
            stepHeight: 36,
            stepWidth: width,
            child: GestureDetector(
              onTap: () => onChanged?.call(e),
              child: Container(
                decoration: BoxDecoration(
                  border: Border.all(
                    color: e.isSelected
                        ? theme.colorScheme.secondary
                        : theme.colorScheme.onBackground,
                  ),
                  color: e.isSelected
                      ? theme.colorScheme.secondary
                      : theme.colorScheme.onSecondary,
                ),
                child: Center(
                  child: Text(
                    e.label,
                    style: TextStyle(
                      fontWeight: FontWeight.w400,
                      color: e.isSelected
                          ? theme.colorScheme.onSecondary
                          : theme.colorScheme.onBackground,
                    ),
                    overflow: TextOverflow.ellipsis,
                    softWrap: false,
                  ),
                ),
              ),
            ),
          ),
        ),
      )
          .toList(),
    );
  }
}