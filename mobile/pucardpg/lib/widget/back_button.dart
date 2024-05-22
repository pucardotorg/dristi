
import 'package:flutter/material.dart';
import 'package:pucardpg/blocs/app-localization-bloc/app_localization.dart';
import 'package:pucardpg/mixin/app_mixin.dart';
import '../utils/i18_key_constants.dart' as i18;

class DigitBackButton extends StatelessWidget with AppMixin{

  DigitBackButton({super.key});

  @override
  Widget build(BuildContext context) {
    return TextButton(
      onPressed: (){
        Navigator.of(context).pop();
      },
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          const Icon(Icons.arrow_left, color: Colors.black,),
          Text(
          AppLocalizations.of(context).translate(i18.common.csCommonBack),
            style: theme.text16W400Rob(),),
        ],
      ),
    );
  }

}