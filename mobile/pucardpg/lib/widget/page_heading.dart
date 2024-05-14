import 'package:digit_components/theme/colors.dart';
import 'package:flutter/material.dart';
import 'package:pucardpg/mixin/app_mixin.dart';

class PageHeading extends StatelessWidget with AppMixin {
  String heading;
  String subHeading;
  TextStyle? headingStyle;
  TextStyle? subHeadingStyle;

  PageHeading({super.key,
    required this.heading,
    required this.subHeading,
    required this.headingStyle,
    required this.subHeadingStyle
  });

  @override
  Widget build(BuildContext context) {
    return Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
      Text(
        heading,
        style: headingStyle,
      ),
      const SizedBox(
        height: 10,
      ),
      Text(
        subHeading,
        style: subHeadingStyle,
      ),
      const SizedBox(
        height: 40,
      ),
    ]);
  }
}
