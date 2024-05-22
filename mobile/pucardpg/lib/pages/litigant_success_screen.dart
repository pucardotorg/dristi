
import 'package:auto_route/auto_route.dart';
import 'package:digit_components/digit_components.dart';
import 'package:pucardpg/blocs/app-localization-bloc/app_localization.dart';
import '../utils/i18_key_constants.dart' as i18;
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:pucardpg/mixin/app_mixin.dart';
import 'package:pucardpg/utils/constants.dart';

@RoutePage()
class LitigantSuccessScreen extends StatefulWidget with AppMixin{

  LitigantSuccessScreen({super.key});

  @override
  LitigantSuccessScreenState createState() => LitigantSuccessScreenState();

}

class LitigantSuccessScreenState extends State<LitigantSuccessScreen> {

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
                AppLocalizations.of(context)
                    .translate(i18.success.csRegisterSuccess),
                style: widget.theme.text20W700()?.apply(),
                textAlign: TextAlign.center,
              ),
              const SizedBox(
                height: 10,
              ),
              Padding(
                padding: const EdgeInsets.only(left: 25, right: 25, top: 5),
                child: Text(
                  AppLocalizations.of(context)
                      .translate(i18.success.csRegisterSuccessSubText),
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
                  child: Text(AppLocalizations.of(context)
                      .translate(i18.success.fileACase),
                    style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
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
                            AppLocalizations.of(context)
                                .translate(i18.success.joinACase),
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