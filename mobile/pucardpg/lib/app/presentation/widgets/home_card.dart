import 'package:digit_components/digit_components.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:pucardpg/app/presentation/widgets/detail_field.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';

class HomeCard extends StatelessWidget with AppMixin {

  IconData icon;
  String heading;

  HomeCard({super.key, required this.heading, required this.icon});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 150,
      child: DigitCard(
        padding: const EdgeInsets.all(15),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              icon,
              size: 40,
              color: const Color(0xFFF47738),
            ),
            const SizedBox(height: 10,),
            Text(
              textAlign: TextAlign.center,
              heading,
              style: theme.text14W400Rob()?.apply(color: Colors.black)
            )
          ],
        ),
      ),
    );
  }
}
