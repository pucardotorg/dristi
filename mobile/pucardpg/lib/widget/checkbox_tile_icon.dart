import 'package:digit_components/theme/colors.dart';
import 'package:flutter/material.dart';
import 'package:pucardpg/theme/app_themes.dart';

/*A single checkbox component */
class CheckboxTileIcon extends StatelessWidget {
  final bool value;

  // Constructor for the DigitCheckbox widget with required parameters
  const CheckboxTileIcon({
    super.key,
    this.value = false,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Container(
      width: 24.0,
      height: 24.0,
      decoration: BoxDecoration(
        border: Border.all(
          color: value
              ? AppTheme.instance.defaultColor
              : const DigitColors().davyGray,
          width: 2.0,
        ),
        borderRadius: BorderRadius.zero,
      ),
      child: value
          ? Center(
              child: Icon(
                Icons.check,
                size: 16.0,
                color: AppTheme.instance.defaultColor,
              ),
            )
          : null,
    );
  }
}
