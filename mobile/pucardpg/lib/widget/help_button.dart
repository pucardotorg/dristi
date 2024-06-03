import 'package:pucardpg/blocs/app-localization-bloc/app_localization.dart';

import '../utils/i18_key_constants.dart' as i18;
import 'package:flutter/material.dart';
import 'package:pucardpg/mixin/app_mixin.dart';

class DigitHelpButton extends StatelessWidget with AppMixin{

  DigitHelpButton({super.key});

  @override
  Widget build(BuildContext context) {
    return TextButton(
      style: ButtonStyle(
        overlayColor: MaterialStateProperty.all(Colors.white)
      ),
      onPressed: (){

      },
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          Text(AppLocalizations.of(context).translate(
              i18.common.csCommonHelp),
            style: theme.text16W400Rob()?.apply(
            color: theme.defaultColor, decoration: TextDecoration.underline),),
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
            child: Icon(Icons.question_mark, color: theme.defaultColor, size: 15,),
          ),
        ],
      ),
    );
  }

}