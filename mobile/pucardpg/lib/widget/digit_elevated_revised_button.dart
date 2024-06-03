import 'package:digit_components/digit_components.dart';
import 'package:flutter/material.dart';

class DigitElevatedRevisedButton extends StatelessWidget {
  final Widget child;
  final VoidCallback? onPressed;

  const DigitElevatedRevisedButton({
    super.key,
    required this.child,
    required this.onPressed,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.only(
        top: kPadding,
        bottom: kPadding,
      ),
      constraints: const BoxConstraints(maxHeight: 40),
      child: ElevatedButton(
        style: ElevatedButton.styleFrom(
          tapTargetSize: MaterialTapTargetSize.shrinkWrap,
          textStyle: const TextStyle(
            color: Colors.black,
            fontFamily: 'Roboto',
            fontSize: 16,
            fontWeight: FontWeight.w500,
          ),
        ),
        onPressed: onPressed,
        child: Center(child: child),
      ),
    );
  }
}
