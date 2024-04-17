
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';

class UserTypeScreen extends StatefulWidget with AppMixin{

  final String mobile;

  UserTypeScreen({super.key, required this.mobile});

  @override
  UserTypeScreenState createState() => UserTypeScreenState();

}

class UserTypeScreenState extends State<UserTypeScreen> {

  String? selectedOption;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text(""),
          centerTitle: true,
          actions: [
            IconButton(onPressed: () {}, icon: const Icon(Icons.notifications))
          ],
          leading: IconButton(
            onPressed: () {},
            icon: const Icon(Icons.menu),
          ),
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
                          Text("Choose your user type", style: widget.theme.text32W700RobCon()?.apply(),),
                          const SizedBox(height: 20,),
                          RadioListTile(
                            title: Text('Litigant', style: widget.theme.text16W400Rob(),),
                            value: 'Litigant',
                            groupValue: selectedOption,
                            contentPadding: EdgeInsets.zero,
                            onChanged: (value) {
                              setState(() {
                                selectedOption = value;
                              });
                            },
                          ),
                          // const SizedBox(height: 20,),
                          RadioListTile(
                            title: Text('Advocate', style: widget.theme.text16W400Rob(),),
                            value: 'Advocate',
                            groupValue: selectedOption,
                            contentPadding: EdgeInsets.zero,
                            onChanged: (value) {
                              setState(() {
                                selectedOption = value;
                              });
                            },
                          ),
                          // const SizedBox(height: 20,),
                          RadioListTile(
                            title: Text('Advocate Clerk', style: widget.theme.text16W400Rob(),),
                            value: 'Advocate Clerk',
                            groupValue: selectedOption,
                            contentPadding: EdgeInsets.zero,
                            onChanged: (value) {
                              setState(() {
                                selectedOption = value;
                              });
                            },
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
                  if(selectedOption == null){
                    DigitToast.show(context,
                      options: DigitToastOptions(
                        "Please select a user type.",
                        true,
                        widget.theme.theme(),
                      ),
                    );
                    return;
                  }
                  else if(selectedOption == 'Litigant'){
                    Navigator.pushNamed(context, '/TermsAndConditionsScreen');
                  }
                  else{
                    Navigator.pushNamed(context, '/AdvocateRegistrationScreen');
                  }
                },
                child: Text('Next',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
            ),
          ],
        )
    );

  }

}
