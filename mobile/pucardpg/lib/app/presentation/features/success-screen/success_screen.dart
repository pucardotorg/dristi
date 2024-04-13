
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';

class SuccessScreen extends StatefulWidget with AppMixin{

  const SuccessScreen({super.key, });

  @override
  SuccessScreenState createState() => SuccessScreenState();

}

class SuccessScreenState extends State<SuccessScreen> {

  bool firstChecked = false;

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
            Expanded(
              child: SingleChildScrollView(
                child: Column(
                  children: [
                    DigitCard(
                      // padding: const EdgeInsets.all(20),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Container(
                            color: Colors.green,
                            padding: const EdgeInsets.all(20),
                            child: Center(child: Text("Your registration application has been submitted successfully!", style: widget.theme.text32W700RobCon()?.apply(color: Colors.white),)),
                          ),
                          Padding(
                            padding: const EdgeInsets.all(20),
                            child: Center(child: Text("You will be given access once your application is verified and approved.", style: widget.theme.text16W400Rob(),)),
                          )
                          // const SizedBox(height: 20,),
                        ],
                      ),
                    ),
                    // Expanded(child: Container(),),
                  ],
                ),
              ),
            ),
            DigitElevatedButton(
                onPressed: () {
                  // Navigator.pushNamed(context, '/IdOtpScreen');
                },
                child: Text('Go to Home Page',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
            ),
          ],
        )
    );

  }

}
