import 'dart:async';

import 'package:digit_components/digit_components.dart';
import 'package:digit_components/theme/digit_theme.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_bloc.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_event.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_state.dart';
import 'package:pucardpg/app/data/models/advocate-clerk-registration-model/advocate_clerk_registration_model.dart';
import 'package:pucardpg/app/data/models/advocate-registration-model/advocate_registration_model.dart';
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:pucardpg/core/constant/constants.dart';

import '../../../domain/entities/litigant_model.dart';

class OtpScreen extends StatefulWidget with AppMixin {
  UserModel userModel = UserModel();

  OtpScreen({super.key, required this.userModel});

  @override
  OtpScreenState createState() => OtpScreenState();
}

class OtpScreenState extends State<OtpScreen> {
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
          title: Row(
            children: [
              Image.asset(
                digitSvg,
                fit: BoxFit.contain,
              ),
              const VerticalDivider(
                color: Colors.white,
              ),
              const Padding(
                padding: EdgeInsets.symmetric(horizontal: 8.0),
                child: Text(
                  "State",
                  style: TextStyle(
                    color: Colors.white,
                  ),
                ),
              ),
            ],
          ),
          centerTitle: false,
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
                            if (value.isNotEmpty &&
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
                              widget.registrationLoginBloc.add(ResendOtpEvent(
                                  mobileNumber: widget.userModel.mobileNumber!,
                                  type: widget.userModel.type!));
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
                  BlocListener<RegistrationLoginBloc, RegistrationLoginState>(
                    bloc: widget.registrationLoginBloc,
                    listener: (context, state) {
                      switch (state.runtimeType) {
                        case RequestFailedState:
                          if (isSubmitting) {
                            isSubmitting = false;
                            DigitToast.show(
                              context,
                              options: DigitToastOptions(
                                "Failed",
                                true,
                                widget.theme.theme(),
                              ),
                            );
                          }
                          return;
                        case ResendOtpGenerationSuccessState:
                          if (isSubmitting) {
                            isSubmitting = false;
                            DigitToast.show(
                              context,
                              options: DigitToastOptions(
                                "OTP generated successfully",
                                false,
                                DigitTheme.instance.mobileTheme,
                              ),
                            );
                          }
                          break;
                        case OtpCorrectState:
                          if (isSubmitting) {
                            isSubmitting = false;
                            _makeOTPSuccessToast();
                            Navigator.pushNamed(
                                context, '/IdVerificationScreen',
                                arguments: widget.userModel);
                          }
                          break;
                        case IndividualSearchSuccessState:
                          if (isSubmitting) {
                            isSubmitting = false;
                            List<Individual> listIndividuals =
                                (state as IndividualSearchSuccessState)
                                    .individualSearchResponse
                                    .individual;
                            if (listIndividuals.isEmpty) {
                              _makeOTPSuccessToast();
                              Navigator.pushNamed(context, '/YetToRegister',
                                  arguments: widget.userModel);
                            } else {
                              if (widget.userModel.userType == 'LITIGANT') {
                                _makeOTPSuccessToast();
                                Navigator.pushNamed(context, '/UserHomeScreen',
                                    arguments: widget.userModel);
                              }
                            }
                          }
                          break;
                        case AdvocateSearchSuccessState:
                          if (isSubmitting) {
                            isSubmitting = false;
                            List<Advocate> advocates =
                                (state as AdvocateSearchSuccessState)
                                    .advocateSearchResponse
                                    .advocates;
                            if (advocates.isEmpty) {
                              _makeOTPSuccessToast();
                              Navigator.pushNamed(
                                  context, '/AdvocateRegistrationScreen',
                                  arguments: widget.userModel);
                            } else {
                              _makeOTPSuccessToast();
                              Navigator.pushNamed(context, '/AdvocateHomePage',
                                  arguments: widget.userModel);
                            }
                          }
                          break;
                        case AdvocateClerkSearchSuccessState:
                          if (isSubmitting) {
                            isSubmitting = false;
                            List<Clerk> clerks =
                                (state as AdvocateClerkSearchSuccessState)
                                    .advocateClerkSearchResponse
                                    .clerks;
                            if (clerks.isEmpty) {
                              _makeOTPSuccessToast();
                              Navigator.pushNamed(
                                  context, '/AdvocateRegistrationScreen',
                                  arguments: widget.userModel);
                            } else {
                              _makeOTPSuccessToast();
                              Navigator.pushNamed(context, '/AdvocateHomePage',
                                  arguments: widget.userModel);
                            }
                          }
                          break;
                        default:
                          break;
                      }
                    },
                    child: DigitElevatedButton(
                        onPressed: isSubmitting
                            ? null
                            : () {
                                FocusScope.of(context).unfocus();
                                String otp = '';
                                _otpControllers.forEach((controller) {
                                  otp += controller.text;
                                });
                                if (widget.userModel.type == register) {
                                  widget.registrationLoginBloc.add(
                                      SubmitRegistrationOtpEvent(
                                          username:
                                              widget.userModel.mobileNumber!,
                                          otp: otp,
                                          userModel: widget.userModel));
                                }
                                if (widget.userModel.type == login) {
                                  widget.registrationLoginBloc.add(
                                      SendLoginOtpEvent(
                                          username:
                                              widget.userModel.mobileNumber!,
                                          password: otp,
                                          userModel: widget.userModel));
                                }
                                isSubmitting = true;
                              },
                        child: Text(
                          'Submit',
                          style: widget.theme.text20W700()?.apply(
                                color: Colors.white,
                              ),
                        )),
                  )
                ],
              ),
            ),
          ],
        ));
  }

  _makeOTPSuccessToast() {
    DigitToast.show(
      context,
      options: DigitToastOptions(
        "OTP Verified",
        false,
        DigitTheme.instance.mobileTheme,
      ),
    );
  }
}
