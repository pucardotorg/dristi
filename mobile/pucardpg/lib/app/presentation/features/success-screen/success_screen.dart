
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_svg/flutter_svg.dart';
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
          backgroundColor: Colors.white,
          body: Padding(
            padding: const EdgeInsets.all(20),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                SvgPicture.asset(
                  successSvg,
                  width: 112,
                  height: 112,
                ),
                const SizedBox(
                  height: 25,
                ),
                Text(
                  "You’ve been registered successfully!",
                  style: widget.theme.text20W700()?.apply(),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(
                  height: 10,
                ),
                Padding(
                  padding: const EdgeInsets.only(left: 25, right: 25, top: 5),
                  child: Text(
                    "You can now proceed to file a case or join an on-going case",
                    style: widget.theme.text14W400Rob(),
                    textAlign: TextAlign.center,
                  ),
                ),
                const SizedBox(
                  height: 40,
                ),
                DigitElevatedButton(
                    onPressed: () {

                    },
                    child: Text('File a Case',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
                ),
                Container(
                  margin: const EdgeInsets.only(
                    top: kPadding,
                    bottom: kPadding,
                  ),
                  constraints: const BoxConstraints(maxHeight: 50, minHeight: 40),
                  child: OutlinedButton(
                    onPressed: () {

                    },
                    style: OutlinedButton.styleFrom(
                          shape: const RoundedRectangleBorder(
                            borderRadius: BorderRadius.zero,
                          ),
                        ),
                    child: Padding(
                      padding: const EdgeInsets.all(kPadding / 2),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text(
                            'Join a Case',
                            style: widget.theme.text20W700()?.apply(
                                  color: widget.theme.colorScheme.secondary)
                          ),
                        ],
                      ),
                    ),
                  ),
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
