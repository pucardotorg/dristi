import 'package:flutter/cupertino.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';

class DetailField extends StatelessWidget with AppMixin {

  String heading;
  String value;

  DetailField({super.key,
    required this.heading,
    required this.value
  });

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          flex: 1,
          child: Text(
            heading,
            style: theme.text16W700Rob(),
          ),
        ),
        Expanded(
          flex: 1,
          child: Text(
            value,
            style: theme.text16W400Rob(),
            textAlign: TextAlign.end,
          ),
        ),
      ],
    );
  }
}