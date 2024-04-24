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
          appBar: AppBar(
            title: const Text(""),
            centerTitle: true,
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
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: [
                      const SizedBox(height: 20,),
                      DigitCard(
                        // padding: const EdgeInsets.all(20),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            SvgPicture.asset(
                              approvalSvg,
                              width: 340,
                              height: 200,
                            ),
                            Padding(
                              padding: const EdgeInsets.all(20),
                              child: Center(child: Text("Your application is awaiting approval ......!", style: widget.theme.text16W400Rob(),)),
                            ),
                            DigitOutlineIconButton(
                              label: 'View My Application',
                              onPressed: (){
                                Navigator.pushNamed(context, '/ViewApplicationScreen', arguments: widget.userModel);
                              },
                              icon: Icons.settings_applications,
                              iconColor: DigitTheme.instance.colorScheme.secondary,
                            ),
                            // const SizedBox(height: 20,),
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