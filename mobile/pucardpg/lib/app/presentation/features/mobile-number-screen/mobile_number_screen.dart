

import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/material.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';

class MobileNumberScreen extends StatefulWidget with AppMixin{

  const MobileNumberScreen({super.key});

  @override
  MobileNumberScreenState createState() => MobileNumberScreenState();

}

class MobileNumberScreenState extends State<MobileNumberScreen> {

  bool rememberMe = false;
  late String mobile;
  TextEditingController searchController = TextEditingController();

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text("Birth registration list"),
        ),
        body: Column(
          children: [
            const SizedBox(height: 10,),
            const Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                DigitBackButton(),
                DigitHelpButton()
              ],
            ),
            DigitCard(
              padding: const EdgeInsets.all(20),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text("Provide Your Mobile Number", style: widget.theme.text32W700RobCon(),),
                    const SizedBox(height: 20,),
                    Text("Your mobile number will be used to login to the system going forward. We will send you a one-time password on this mobile number", style: widget.theme.text16W400Rob(),),
                    const SizedBox(height: 20,),
                    DigitTextField(
                      label: 'Enter Mobile number',
                      prefixText: "+91  ",
                      isRequired: true,
                      onChange: (val) { mobile = val; },
                    ),
                    const SizedBox(height: 20,),
                    DigitCheckboxTile( value: rememberMe, label: "Remember me", onChanged: (val){rememberMe = !rememberMe;},),
                    DigitElevatedButton(
                        onPressed: () {
                          Navigator.pushNamed(context, '/Otp', arguments: mobile);
                        },
                        child: Text('Next',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
                    ),
                  ],
                ),
            ),
          ],
        )
    );

  }

}
