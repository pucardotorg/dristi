import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/digit_outline_icon_button.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:geolocator/geolocator.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_bloc.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_event.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_state.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:reactive_forms/reactive_forms.dart';

class AdvocateHomePage extends StatefulWidget with AppMixin{

  UserModel userModel = UserModel();
  AdvocateHomePage({super.key, required this.userModel});

  @override
  AdvocateHomePageState createState() => AdvocateHomePageState();

}

class AdvocateHomePageState extends State<AdvocateHomePage> {

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
          body: Padding(
            padding: const EdgeInsets.all(20),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                SvgPicture.asset(
                  waitingSvg,
                  width: 112,
                  height: 112,
                ),
                const SizedBox(
                  height: 25,
                ),
                Text(
                  'Your registration is waiting approval',
                  style: widget.theme.text20W700()?.apply(),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(
                  height: 10,
                ),
                Padding(
                  padding: const EdgeInsets.only(left: 25, right: 25, top: 5),
                  child: Text(
                    "Your registration (ID: XXXXXXXXX) is in progress. It takes 2-3 days for verification. You'll get an SMS when it's done.",
                    style: widget.theme.text14W400Rob(),
                    textAlign: TextAlign.center,
                  ),
                ),
                const SizedBox(
                  height: 40,
                ),
                DigitElevatedButton(
                    onPressed: (){
                      Navigator.pushNamed(context, '/ViewApplicationScreen', arguments: widget.userModel);
                    },
                    child: Text(
                      'View My Application',
                      style: widget.theme.text20W700()?.apply(
                        color: Colors.white,
                      ),
                    )
                ),
              ],
            ),
          ),
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