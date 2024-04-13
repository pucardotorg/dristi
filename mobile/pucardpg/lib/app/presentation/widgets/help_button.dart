
import 'package:digit_components/theme/colors.dart';
import 'package:flutter/material.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';

class DigitHelpButton extends StatelessWidget with AppMixin{

  const DigitHelpButton({super.key});

  @override
  Widget build(BuildContext context) {
    return TextButton(
      onPressed: (){

      },
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          Text('Help', style: theme.text16W400Rob()?.apply(color: theme.defaultColor,),),
          const SizedBox(width: 5,),
          Container(
            width: 20,
            height: 20,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              border: Border.all(
                color: theme.defaultColor,
                width: 2,
              ),
            ),
            child: const Icon(Icons.question_mark, size: 15,),
          ),
        ],
      ),
    );
  }

}