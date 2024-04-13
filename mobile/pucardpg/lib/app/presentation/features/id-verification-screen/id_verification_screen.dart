
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';

class IdVerificationScreen extends StatefulWidget with AppMixin{

  final String mobile;

  const IdVerificationScreen({super.key, required this.mobile});

  @override
  IdVerificationScreenState createState() => IdVerificationScreenState();

}

class IdVerificationScreenState extends State<IdVerificationScreen> {

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
            Expanded(
              child: SingleChildScrollView(
                child: Column(
                  children: [
                    const SizedBox(height: 10,),
                    DigitCard(
                      padding: const EdgeInsets.all(20),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text("Registration", style: widget.theme.text20W400Rob()?.apply(fontStyle: FontStyle.italic),),
                          const SizedBox(height: 20,),
                          Text("ID Verification", style: widget.theme.text32W700RobCon()?.apply(),),
                          const SizedBox(height: 20,),
                          Text("Please provide details for registration", style: widget.theme.text16W400Rob(),),
                          const SizedBox(height: 20,),
                          DigitTextField(
                            label: 'Enter aadhar number',
                            onChange: (val) { mobile = val; },
                          ),
                          const SizedBox(height: 20,),
                          Center(child: Text("(or)", style: widget.theme.text20W400Rob(),)),
                          const SizedBox(height: 20,),
                          DigitTextField(
                            label: 'Enter Type of ID',
                            onChange: (val) { mobile = val; },
                          ),
                          const SizedBox(height: 20,),
                          Row(
                            crossAxisAlignment: CrossAxisAlignment.end,
                            children: [
                              Expanded(
                                child: DigitTextField(
                                  label: 'Upload ID proof',
                                  readOnly: true,
                                  onChange: (val) { mobile = val; },
                                ),
                              ),
                              const SizedBox(width: 10,),
                              SizedBox(
                                height: 44,
                                width: 120,
                                child: DigitOutLineButton(
                                  label: 'Upload',
                                  onPressed: (){},
                                ),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                    const DigitInfoCard(title: "Info", description: "Using Aadhar number for Verification will provide a Verified status against your profile."),
                    // Expanded(child: Container(),),
                  ],
                ),
              ),
            ),
            DigitElevatedButton(
                onPressed: () {
                  Navigator.pushNamed(context, '/IdOtpScreen', arguments: widget.mobile);
                },
                child: Text('Next',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
            ),
          ],
        )
    );

  }

}
