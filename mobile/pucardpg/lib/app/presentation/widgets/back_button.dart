
import 'package:flutter/material.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';

class DigitBackButton extends StatelessWidget with AppMixin{

  final VoidCallback? onPressed;

  const DigitBackButton({super.key, required this.onPressed});

  @override
  Widget build(BuildContext context) {
    return TextButton(
      onPressed: onPressed,
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          const Icon(Icons.arrow_left, color: Colors.black,),
          Text('Back', style: theme.text16W400Rob(),),
        ],
      ),
    );
  }

}