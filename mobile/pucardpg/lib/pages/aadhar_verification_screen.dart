import 'dart:async';

import 'package:auto_route/auto_route.dart';
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:digit_components/widgets/widgets.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/blocs/app-localization-bloc/app_localization.dart';
import 'package:pucardpg/blocs/auth-bloc/authbloc.dart';
import 'package:pucardpg/mixin/app_mixin.dart';
import 'package:pucardpg/widget/digit_toast_revised.dart';
import '../utils/i18_key_constants.dart' as i18;
import 'package:pucardpg/routes/routes.dart';
import 'package:pucardpg/widget/back_button.dart';
import 'package:pucardpg/widget/help_button.dart';
import 'package:pucardpg/widget/page_heading.dart';

@RoutePage()
class AadharVerificationScreen extends StatefulWidget with AppMixin {

  AadharVerificationScreen({super.key});

  @override
  AadharVerificationScreenState createState() => AadharVerificationScreenState();
}

class AadharVerificationScreenState extends State<AadharVerificationScreen> {

  final List<FocusNode> _focusNodes = List.generate(3, (index) => FocusNode());
  final List<TextEditingController> _aadharControllers =
  List.generate(3, (index) => TextEditingController());

  List<FocusNode> _otpFocusNodes = List.generate(6, (index) => FocusNode());
  List<TextEditingController> _otpControllers = List.generate(6, (index) => TextEditingController());
  bool isSubmit = false;

  int _counter = 0;
  late StreamController<int> _events;

  @override
  void initState() {
    super.initState();
    _events = StreamController<int>.broadcast();
    _events.add(25);
  }

  @override
  void dispose() {
    _timer?.cancel();
    _events.close();
    super.dispose();
  }

  Timer? _timer;
  void _startTimer() {
    _counter = 25;
    if (_timer != null) {
      _timer!.cancel();
    }
    _timer = Timer.periodic(Duration(seconds: 1), (timer) {
      //setState(() {
      (_counter > 0) ? _counter-- : _timer!.cancel();
      //});
      print(_counter);
      _events.add(_counter);
    });
  }

  bool _validateAadharNumber(String value) {
    final RegExp mobileRegex = RegExp(r'^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$', caseSensitive: false);
    return mobileRegex.hasMatch(value);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Colors.white,
        body: Column(
          children: [
            const SizedBox(
              height: 50,
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                DigitBackButton(),
                DigitHelpButton()],
            ),
            Expanded(
              child: SingleChildScrollView(
                child: Column(
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(20),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          PageHeading(
                            heading: AppLocalizations.of(context).translate(i18.idVerification.csEnterAadhar),
                            subHeading: AppLocalizations.of(context).translate(i18.idVerification.csEnterAadharText),
                            headingStyle: widget.theme.text24W700(),
                            subHeadingStyle: widget.theme.text14W400Rob(),
                          ),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: List.generate(
                              3,
                                  (index) => SizedBox(
                                width: 110,
                                child: TextField(
                                  controller: _aadharControllers[index],
                                  keyboardType: TextInputType.number,
                                  textAlign: TextAlign.center,
                                  inputFormatters: [
                                    FilteringTextInputFormatter.allow(RegExp(r'[0-9]')),
                                  ],
                                  maxLength: 5,
                                  onChanged: (value) {
                                    if (value.length > 4) {
                                      _aadharControllers[index].text =
                                          value.substring(0, 4);
                                      if (index < _aadharControllers.length - 1) {
                                        _aadharControllers[index + 1].text =
                                            value.substring(4, 5);
                                      }
                                    }
                                    if (value.length > 4 &&
                                        index < _aadharControllers.length - 1) {
                                      FocusScope.of(context)
                                          .requestFocus(_focusNodes[index + 1]);
                                    } else if (value.isEmpty && index > 0) {
                                      FocusScope.of(context)
                                          .requestFocus(_focusNodes[index - 1]);
                                    }
                                  },
                                  focusNode: _focusNodes[index],
                                  decoration: InputDecoration(
                                    counterText: "",
                                    border: OutlineInputBorder(
                                      borderRadius: BorderRadius.circular(10.0),
                                    ),
                                  ),
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
            const Divider(
              height: 0,
              thickness: 2,
            ),
            DigitCard(
              padding: const EdgeInsets.fromLTRB(10, 0, 10, 15),
              child: DigitElevatedButton(
                  onPressed: () {
                    String aadharNumber = '';
                    for (var controller in _aadharControllers) {
                      aadharNumber += controller.text;
                    }
                    bool isValidAadharNumber = _validateAadharNumber(aadharNumber);
                    if ((aadharNumber.length != 12 || !isValidAadharNumber)) {
                      DigitToastRevised.show(context,
                        options: DigitToastOptions(
                          "Enter a valid aadhar number",
                          true,
                          widget.theme.theme(),
                        ),
                      );
                      return;
                    }
                    context.read<AuthBloc>().userModel.identifierId = aadharNumber;
                    context.read<AuthBloc>().userModel.identifierType = "AADHAR";
                    showOtpDialog();
                    _startTimer();
                  },
                  child: Text(
                    AppLocalizations.of(context).translate(i18.common.coreCommonGetOtp),
                    style: widget.theme.text20W700()?.apply(
                      color: Colors.white,
                    ),
                  )),
            ),
          ],
        ));
  }

  showOtpDialog() {
    var t = AppLocalizations.of(context);
    final verifyAadhar = t.translate(i18.idVerification.csVerifyAadhar);
    final csLoginOtpText = t.translate(i18.common.csLoginOtpText);
    final resendOtp = t.translate(i18.common.csResendOtp);
    final resendOtpText = t.translate(i18.common.csResendAnotherOtp);
    final verify = t.translate(i18.common.verify);

    showDialog(
        context: context,
        barrierDismissible: false,
        builder: (context) {
          return AlertDialog(
            titlePadding: EdgeInsets.only(left: 20),
            title: Column(
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [
                      Container(
                        height: 38,
                        width: 38,
                        decoration: const BoxDecoration(
                          color: Color(0XFF505A5F),
                        ),
                        child: IconButton(
                          icon: Icon(Icons.close),
                          color: Colors.white,
                          onPressed: () {
                            _otpFocusNodes = List.generate(6, (index) => FocusNode());
                            _otpControllers = List.generate(6, (index) => TextEditingController());
                            isSubmit = false;
                            _timer?.cancel();
                            _events.close();
                            _events = StreamController<int>.broadcast();
                            _events.add(25);
                            Navigator.of(context).pop();
                          },
                        ),
                      ),
                    ],
                  ),
                  Row(
                    children: [
                      Container(
                        margin: const EdgeInsets.only(right: 10, top: 10),
                        child: Text(
                          verifyAadhar,
                          style: widget.theme.text24W700(),
                        ),
                      ),
                    ],
                  ),
                ]
            ),
            actionsPadding: const EdgeInsets.only(left: 60, right: 60, bottom: 20),
            content: StreamBuilder<int>(
                stream: _events.stream,
                builder: (BuildContext context, AsyncSnapshot<int> snapshot) {
                  return SingleChildScrollView(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Row(
                          children: [
                            Text(
                              "$csLoginOtpText +91******${context.read<AuthBloc>().userModel.mobileNumber!.substring(6, 10)}",
                              style: widget.theme.text14W400Rob(),
                            ),
                          ],
                        ),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          children: List.generate(
                            6,
                                (index) =>
                                Padding(
                                  padding: const EdgeInsets.only(
                                      left: 2, right: 2, top: 20),
                                  child: SizedBox(
                                    width: 40,
                                    child: TextField(
                                      controller: _otpControllers[index],
                                      keyboardType: TextInputType.number,
                                      textAlign: TextAlign.center,
                                      inputFormatters: [
                                        FilteringTextInputFormatter.allow(
                                            RegExp(r'[0-9]')),
                                      ],
                                      maxLength: 2,
                                      onChanged: (value) {
                                        if (value.length > 1) {
                                          _otpControllers[index].text =
                                              value.substring(0, 1);
                                          if (index <
                                              _otpControllers.length - 1) {
                                            _otpControllers[index + 1].text =
                                                value.substring(1, 2);
                                          }
                                        }
                                        if (value.length > 1 &&
                                            index < _otpControllers.length - 1) {
                                          FocusScope.of(context)
                                              .requestFocus(
                                              _otpFocusNodes[index + 1]);
                                        } else if (value.isEmpty && index > 0) {
                                          FocusScope.of(context)
                                              .requestFocus(
                                              _otpFocusNodes[index - 1]);
                                        }
                                      },
                                      focusNode: _otpFocusNodes[index],
                                      decoration: InputDecoration(
                                        counterText: "",
                                        border: OutlineInputBorder(
                                          borderRadius: BorderRadius.circular(
                                              10.0),
                                        ),
                                      ),
                                    ),
                                  ),
                                ),
                          ),
                        ),
                        _counter == 0
                            ? GestureDetector(
                          onTap: () {
                            setState(() {
                              _counter = 25;
                            });
                            _startTimer();
                            // startTimer();
                            for (int i = 0; i < 6; i++) {
                              _otpControllers[i].text = "";
                            }
                            FocusScope.of(context).unfocus();
                          },
                          child: Padding(
                            padding: const EdgeInsets.only(top: 20, bottom: 10),
                            child: Row(
                              children: [
                                Text(
                                  resendOtp,
                                  style: widget.theme
                                      .text16W400Rob()
                                      ?.apply(color: widget.theme.defaultColor),
                                ),
                              ],
                            ),
                          ),
                        )
                            : Padding(
                          padding: const EdgeInsets.only(top: 20, bottom: 10),
                          child: Row(
                            children: [
                              Text(
                                '$resendOtpText 0:${(snapshot.data == 0) || (snapshot.data == null) ? 25
                                    : snapshot.data! < 10 ? "0${snapshot.data}" : snapshot.data}',
                                style: widget.theme.text14W400Rob(),),
                            ],
                          ),
                        ),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Container(
                              width: 150,
                              child: DigitElevatedButton(
                                  onPressed: isSubmit
                                      ? null
                                      : () {
                                    FocusScope.of(context).unfocus();
                                    String otp = '';
                                    for (var controller in _otpControllers) {
                                      otp += controller.text;
                                    }
                                    if (otp.length != 6) {
                                      DigitToastRevised.show(
                                        context,
                                        options: DigitToastOptions(
                                          "Invalid OTP",
                                          true,
                                          DigitTheme.instance.mobileTheme,
                                        ),
                                      );
                                      return;
                                    }
                                    Navigator.of(context).pop();
                                    AutoRouter.of(context)
                                        .push(UserTypeRoute());
                                    // isSubmit = true;
                                  },
                                  child: Text(
                                    verify,
                                    style: widget.theme.text20W700()?.apply(
                                      color: Colors.white,
                                    ),
                                  )),
                            ),
                          ],
                        )
                      ],
                    ),
                  );
                }
            ),
          );

        }
    );
  }

}