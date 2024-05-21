import 'package:auto_route/auto_route.dart';
import 'package:digit_components/digit_components.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:pucardpg/mixin/app_mixin.dart';
import 'package:pucardpg/widget/home_card.dart';

@RoutePage()
class UserHomeScreen extends StatefulWidget with AppMixin{

  UserHomeScreen({super.key});

  @override
  UserHomeScreenState createState() => UserHomeScreenState();

}

class UserHomeScreenState extends State<UserHomeScreen> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: _onBackPressed,
      child: Scaffold(
          backgroundColor: Colors.white,
          body: Column(
            children: [
              Expanded(
                child: SingleChildScrollView(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: [
                      const SizedBox(height: 50,),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          Expanded(
                              child: HomeCard(heading: "Start a new case", icon: Icons.add_circle_sharp)
                          ),
                          Expanded(
                              child: HomeCard(heading: "Cases in progress", icon: Icons.description)
                          ),
                          Expanded(
                              child: HomeCard(heading: "Closed Cases", icon: Icons.fact_check)
                          ),
                        ],
                      ),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          Expanded(
                              child: HomeCard(heading: "My Hearings", icon: Icons.account_balance)
                          ),
                          Expanded(
                              child: HomeCard(heading: "Join a new case", icon: Icons.description)
                          ),
                          const Expanded(child: SizedBox(width: 0,))
                        ],
                      ),
                    ],
                  ),
                ),
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