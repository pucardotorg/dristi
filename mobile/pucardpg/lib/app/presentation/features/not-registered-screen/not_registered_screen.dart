import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/digit_outline_icon_button.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:geolocator/geolocator.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:reactive_forms/reactive_forms.dart';

class NotRegisteredScreen extends StatefulWidget with AppMixin{

  UserModel userModel = UserModel();

  NotRegisteredScreen({super.key, required this.userModel});

  @override
  NotRegisteredScreenState createState() => NotRegisteredScreenState();

}

class NotRegisteredScreenState extends State<NotRegisteredScreen> {

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
              const SizedBox(height: 50,),
              Row(
                mainAxisAlignment: MainAxisAlignment.end,
                children: [
                  DigitHelpButton()],
              ),
              Expanded(
                child: SingleChildScrollView(
                  child: Column(
                    children: [
                      const SizedBox(height: 50,),
                      Padding(
                        padding: const EdgeInsets.all(15),
                        child: Column(
                          children: [
                            SvgPicture.asset(
                              yetToRegister,
                              width: 340,
                              height: 200,
                              colorFilter: ColorFilter.mode(widget.theme.colorScheme.secondary, BlendMode.srcIn),
                            ),
                            Padding(
                              padding: const EdgeInsets.all(20),
                              child: Center(child: Text("Start by creating your account.",
                                style: widget.theme.text16W400Rob(),)),
                            ),
                            Container(
                              constraints: const BoxConstraints(maxHeight: 50, minHeight: 40),
                              child: OutlinedButton(
                                onPressed: () {
                                  Navigator.pushNamed(context, '/NameDetailsScreen', arguments: widget.userModel);
                                },
                                style: OutlinedButton.styleFrom(
                                  shape: const RoundedRectangleBorder(
                                    borderRadius: BorderRadius.zero,
                                  ),
                                ),
                                child: Padding(
                                  padding: const EdgeInsets.all(2),
                                  child: Row(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: [
                                      Flexible(
                                          child: Icon(
                                            Icons.app_registration,
                                            color: widget.theme.colorScheme.secondary,
                                          )),
                                      const SizedBox(width: 2),
                                      Text(
                                        "Register",
                                        style: DigitTheme.instance.mobileTheme.textTheme.headlineSmall
                                            ?.apply(
                                          color: widget.theme.colorScheme.secondary,
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                      // Expanded(child: Container(),),
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