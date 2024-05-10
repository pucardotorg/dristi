import 'dart:async';

import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/digit_table_item.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';

class IdOtpScreen extends StatefulWidget with AppMixin {
  UserModel userModel = UserModel();

  IdOtpScreen({super.key, required this.userModel});

  @override
  IdOtpScreenState createState() => IdOtpScreenState();
}

class IdOtpScreenState extends State<IdOtpScreen> {
  final List<FocusNode> _focusNodes = List.generate(6, (index) => FocusNode());
  final List<TextEditingController> _otpControllers =
      List.generate(6, (index) => TextEditingController());
  late Timer _timer;
  int _start = 25;
  bool isSubmitting = false;

  @override
  void initState() {
    super.initState();
    startTimer();
  }

  void startTimer() {
    const oneSec = Duration(seconds: 1);
    _timer = Timer.periodic(
      oneSec,
      (Timer timer) {
        if (_start == 0) {
          setState(() {
            timer.cancel();
          });
        } else {
          setState(() {
            _start--;
          });
        }
      },
    );
  }

  @override
  void dispose() {
    _timer.cancel();
    for (var controller in _otpControllers) {
      controller.dispose();
    }
    super.dispose();
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
            const SizedBox(
              height: 10,
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [DigitBackButton(), DigitHelpButton()],
            ),
            DigitCard(
              padding: const EdgeInsets.all(20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    "Registration",
                    style: widget.theme
                        .text20W400Rob()
                        ?.apply(fontStyle: FontStyle.italic),
                  ),
                  const SizedBox(
                    height: 20,
                  ),
                  Text(
                    "OTP Verification",
                    style: widget.theme.text32W700RobCon(),
                  ),
                  const SizedBox(
                    height: 20,
                  ),
                  Text(
                    "Enter the OTP sent to + 91 - ${widget.userModel.mobileNumber}",
                    style: widget.theme.text16W400Rob(),
                  ),
                  const SizedBox(
                    height: 20,
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: List.generate(
                      6,
                      (index) => SizedBox(
                        width: 40,
                        child: TextField(
                          controller: _otpControllers[index],
                          keyboardType: TextInputType.number,
                          textAlign: TextAlign.center,
                          inputFormatters: [
                            FilteringTextInputFormatter.allow(RegExp(r'[0-9]')),
                          ],
                          maxLength: 2,
                          onChanged: (value) {
                            if (value.length > 1) {
                              _otpControllers[index].text =
                                  value.substring(0, 1);
                              if (index < _otpControllers.length - 1) {
                                _otpControllers[index + 1].text =
                                    value.substring(1, 2);
                              }
                            }
                            if (value.length > 1 &&
                                index < _otpControllers.length - 1) {
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
                  const SizedBox(
                    height: 20,
                  ),
                  _start == 0
                      ? Center(
                          child: GestureDetector(
                            onTap: () {
                              setState(() {
                                _start = 25;
                              });
                              startTimer();
                              for (int i = 0; i < 6; i++) {
                                _otpControllers[i].text = "";
                              }
                            },
                            child: Text(
                              'Resend OTP',
                              style: widget.theme
                                  .text16W400Rob()
                                  ?.apply(color: widget.theme.defaultColor),
                            ),
                          ),
                        )
                      : Center(child: Text('Resend OTP in $_start seconds')),
                  const SizedBox(
                    height: 10,
                  ),
                  DigitElevatedButton(
                      onPressed: isSubmitting
                          ? null
                          : () {
                              FocusScope.of(context).unfocus();
                              String otp = '';
                              _otpControllers.forEach((controller) {
                                otp += controller.text;
                              });
                              if (kDebugMode) {
                                print('Entered OTP: $otp');
                              }
                              if (otp.length != 6) {
                                DigitToast.show(
                                  context,
                                  options: DigitToastOptions(
                                    "Enter valid OTP",
                                    true,
                                    widget.theme.theme(),
                                  ),
                                );
                                return;
                              }
                              Navigator.pushNamed(context, '/NameDetailsScreen',
                                  arguments: widget.userModel);
                              isSubmitting = true;
                            },
                      child: Text(
                        'Submit',
                        style: widget.theme.text20W700()?.apply(
                              color: Colors.white,
                            ),
                      )),
                ],
              ),
            ),
          ],
        ));
  }
}
