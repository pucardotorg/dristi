
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/app/presentation/widgets/success_card.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:pucardpg/core/constant/constants.dart';

class SuccessScreen extends StatefulWidget with AppMixin{

  UserModel userModel = UserModel();

  SuccessScreen({super.key, required this.userModel});

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
    return WillPopScope(
      onWillPop: _onBackPressed,
      child: Scaffold(
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
                      if (widget.userModel.userType != 'Litigant') ...[
                        SuccessCard(
                            heading: "Your registration application has been submitted successfully!",
                            subHeading: "You will be given access once your application is verified and approved."
                        )
                      ],
                      if (widget.userModel.userType == 'Litigant') ...[
                        SuccessCard(
                            heading: "Thank you for registering",
                            subHeading: "You can proceed to file a case or respond to a case."
                        )
                      ]
                      // Expanded(child: Container(),),
                    ],
                  ),
                ),
              ),
              DigitElevatedButton(
                  onPressed: () {
                    if(selectedOption != 'Litigant'){
                    Navigator.pushNamed(context, '/AdvocateHomePage');
                    } else {
      
                    }
                  },
                  child: Text('Go to Home Page',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
              ),
            ],
          )
      ),
    );

  }

  Future<bool> _onBackPressed() async {
    return await DigitDialog.show(
        context,
        options: DigitDialogOptions(
            titleIcon: const Icon(
              Icons.warning,
              color: Colors.red,
            ),
            titleText: 'Warning',
            contentText:
            'Are you sure you want to exit the application?',
            primaryAction: DigitDialogActions(
                label: 'Yes',
                action: (BuildContext context) =>
                    SystemChannels.platform.invokeMethod('SystemNavigator.pop')
            ),
            secondaryAction: DigitDialogActions(
                label: 'No',
                action: (BuildContext context) =>
                    Navigator.of(context).pop(false)
            ))
    ) ?? false;
  }

}
