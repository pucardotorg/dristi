import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/checkbox_icon.dart';
import 'package:flutter/material.dart';
import 'package:pucardpg/widget/checkbox_tile_icon.dart';

class CheckboxTile extends StatelessWidget {
  final bool value;
  final String label;
  final ValueChanged<bool>? onChanged;
  final EdgeInsets? margin;
  final EdgeInsets? padding;

  const CheckboxTile({
    this.value = false,
    required this.label,
    this.onChanged,
    this.padding,
    this.margin,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Padding(
      padding: padding ?? const EdgeInsets.all(0),
      child: InkWell(
        onTap: () => onChanged?.call(!value),
        child: Padding(
          padding: const EdgeInsets.only(left: 0, bottom: kPadding * 2, top: 1),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              value
                  ? const CheckboxTileIcon(
                value: true,
              )
                  : const CheckboxTileIcon(),
              const SizedBox(width: kPadding * 2),
              Expanded(
                child: Text(
                  label,
                  style: Theme.of(context).textTheme.bodyLarge,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
