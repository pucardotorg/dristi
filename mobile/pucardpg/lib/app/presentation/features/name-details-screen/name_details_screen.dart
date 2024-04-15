
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';

class NameDetailsScreen extends StatefulWidget with AppMixin{

  final String mobile;

  const NameDetailsScreen({super.key, required this.mobile});

  @override
  NameDetailsScreenState createState() => NameDetailsScreenState();

}

class NameDetailsScreenState extends State<NameDetailsScreen> {

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
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        DigitBackButton(
                          onPressed: (){
                            Navigator.of(context).pop();
                          },
                        ),
                        DigitHelpButton()
                      ],
                    ),
                    DigitCard(
                      padding: const EdgeInsets.all(20),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text("Registration", style: widget.theme.text20W400Rob()?.apply(fontStyle: FontStyle.italic),),
                          const SizedBox(height: 20,),
                          Text("Enter Your Name", style: widget.theme.text32W700RobCon()?.apply(),),
                          const SizedBox(height: 20,),
                          DigitTextField(
                            label: 'First name',
                            isRequired: true,
                            onChange: (val) { },
                          ),
                          const SizedBox(height: 20,),
                          DigitTextField(
                            label: 'Middle name',
                            onChange: (val) { },
                          ),
                          const SizedBox(height: 20,),
                          DigitTextField(
                            label: 'Last name',
                            isRequired: true,
                            onChange: (val) { },
                          ),
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
                  Navigator.pushNamed(context, '/AddressScreen', arguments: widget.mobile);
                },
                child: Text('Next',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
            ),
          ],
        )
    );

  }

}
