import 'package:digit_components/theme/digit_theme.dart';
import 'package:flutter/material.dart';

class DigitElevatedCard extends StatelessWidget {
  final Widget child;
  final EdgeInsets? padding;
  final EdgeInsets? margin;
  final VoidCallback? onPressed;

  const DigitElevatedCard({
    required this.child,
    super.key,
    this.padding,
    this.margin,
    this.onPressed,
  });

  @override
  Widget build(BuildContext context) => Container(
    margin: margin,
    decoration: const BoxDecoration(
      boxShadow: [
        BoxShadow(
            spreadRadius: 5,
            blurRadius: 8.0,
            color: Color(0xFFf2f2f2)),
        BoxShadow(color: Colors.white),
      ],
    ),
    child: InkWell(
      borderRadius: BorderRadius.circular(4),
      onTap: onPressed,
      child: Padding(
        padding: padding ??
            const EdgeInsets.only(
              left: kPadding * 2,
              right: kPadding * 2,
              top: kPadding * 2,
              bottom: kPadding * 2,
            ),
        child: child,
      ),
    ),
  );
}