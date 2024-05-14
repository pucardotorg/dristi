import 'package:digit_components/digit_components.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:pucardpg/app/presentation/widgets/detail_field.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';

class SuccessCard extends StatelessWidget with AppMixin {
  String heading;
  String subHeading;
  SuccessCard({super.key, required this.heading, required this.subHeading});

  @override
  Widget build(BuildContext context) {
    return DigitCard(
      padding: EdgeInsets.zero,
      child: Column(
        children: [
          Container(
              color: const Color(0xFF00703C),
              padding: const EdgeInsets.all(20),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(
                    textAlign: TextAlign.center,
                    heading,
                    style: theme.text32W700RobCon()?.apply(color: Colors.white),
                  ),
                  const SizedBox(
                    height: 20,
                  ),
                  const Icon(
                    Icons.check_circle, // Replace with your desired icon
                    color: Colors.white,
                    size: 48, // Adjust icon size as needed
                  ),
                ],
              )),
          Padding(
              padding: const EdgeInsets.all(20),
              child: Text(
                textAlign: TextAlign.center,
                subHeading,
                style: theme
                    .text16W400Rob()
                    ?.apply(color: theme.lightGrey),
              ))
        ],
      ),
    );
  }
}
