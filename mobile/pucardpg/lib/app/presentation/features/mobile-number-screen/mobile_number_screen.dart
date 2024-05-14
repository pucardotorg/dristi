import 'dart:async';

import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:flutter_svg/svg.dart';
import 'package:geolocator/geolocator.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_bloc.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_event.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_state.dart';
import 'package:pucardpg/app/data/data_sources/shared-preferences/app_shared_preference.dart';
import 'package:pucardpg/app/data/models/advocate-clerk-registration-model/advocate_clerk_registration_model.dart';
import 'package:pucardpg/app/data/models/advocate-registration-model/advocate_registration_model.dart';
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:reactive_forms/reactive_forms.dart';

class MobileNumberScreen extends StatefulWidget with AppMixin {
  MobileNumberScreen({super.key});

  @override
  MobileNumberScreenState createState() => MobileNumberScreenState();
}

class MobileNumberScreenState extends State<MobileNumberScreen> {
  bool isSubmitting = false;
  bool rememberMe = false;
  UserModel userModel = UserModel();
  String mobileNumberKey = 'mobileNumber';

  TextEditingController searchController = TextEditingController();

  int _counter = 0;
  late StreamController<int> _events;

  @override
  void initState() {
    super.initState();
    _events = StreamController<int>.broadcast();
    _events.add(25);
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


  bool _validateMobile(String value) {
    final RegExp mobileRegex =
        RegExp(r'^[6789][0-9]{9}$', caseSensitive: false);
    return mobileRegex.hasMatch(value);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
          backgroundColor: Colors.white,
          body: ReactiveFormBuilder(
            form: buildForm,
            builder: (context, form, child) {
              return Column(
                children: [
                  const SizedBox(
                    height: 50,
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      DigitBackButton(),
                      DigitHelpButton()
                    ],
                  ),
                  Expanded(
                      child: Padding(
                        padding: const EdgeInsets.all(20),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                          const SizedBox(
                            height: 10,
                          ),
                          Text(
                            "Please enter your mobile number",
                            style: widget.theme.text24W700(),
                          ),
                          const SizedBox(
                            height: 20,
                          ),
                          Text(
                            "You will use this as your log in. We will send you an OTP to verify",
                            style: widget.theme.text14W400Rob(),
                          ),
                          const SizedBox(
                            height: 20,
                          ),
                          DigitTextFormField(
                            label: 'Mobile No',
                            prefixIcon: Container(
                              margin: const EdgeInsets.only(left: 1, right: 8),
                              padding: EdgeInsets.zero,
                              decoration: const BoxDecoration(
                                  color: Color(0xFFFAFAFA),
                                  border: Border(right: BorderSide(width: 1), )
                              ),
                              child: Padding(
                                padding: const EdgeInsets.only(
                                    top: 11,
                                    left: 10,
                                    bottom: 11,
                                    right: 0),
                                child: Text(
                                  "+91  ",
                                  style: TextStyle(
                                    fontSize: 16,
                                    fontWeight: FontWeight.w400,
                                    color: DigitTheme.instance.colorScheme.onBackground,
                                  ),
                                ),
                              ),
                            ),
                            formControlName: mobileNumberKey,
                            isRequired: true,
                            maxLength: 10,
                            onChanged: (val) {
                              userModel.mobileNumber = val.value.toString();
                            },
                            keyboardType: TextInputType.number,
                            validationMessages: {
                              'required': (_) => 'Mobile number is required',
                              'number': (_) =>
                              'Mobile number should contain digits 0-9',
                              'minLength': (_) =>
                              'Mobile number should have 10 digits',
                              'maxLength': (_) =>
                              'Mobile number should have 10 digits',
                              'pattern': (_) => 'Invalid Mobile Number'
                            },
                            inputFormatters: [
                              FilteringTextInputFormatter.allow(
                                  RegExp(r'[0-9]')),
                            ],
                          ),
                          const SizedBox(
                            height: 10,
                          ),
                        ],
                        ),
                      )
                  ),
                  const Divider(height: 0, thickness: 2,),
                  BlocListener<RegistrationLoginBloc,
                      RegistrationLoginState>(
                    bloc: widget.registrationLoginBloc,
                    listener: (context, state) {
                      switch (state.runtimeType) {
                        case RequestOtpFailedState:
                          isSubmitting = false;
                          widget.theme.showDigitDialog(
                              true,
                              (state as RequestOtpFailedState)
                                  .errorMsg,
                              context);
                          break;
                        case OtpGenerationSuccessState:
                          isSubmitting = false;
                          userModel.type =
                              (state as OtpGenerationSuccessState)
                                  .type;
                          if (userModel.type == register) {
                            _startTimer();
                            showOtpDialog();
                          }
                          break;
                        default:
                          break;
                      }
                    },
                    child: DigitCard(
                      padding: const EdgeInsets.fromLTRB(10, 0, 10, 15),
                      child: DigitElevatedButton(
                          onPressed: isSubmitting
                              ? null
                              : () {
                            FocusScope.of(context).unfocus();
                            form.markAllAsTouched();
                            if (!form.valid) return;
                            bool isValidNumber = _validateMobile(
                                form
                                    .control(mobileNumberKey)
                                    .value);
                            if (!isValidNumber) {
                              widget.theme.showDigitDialog(
                                  true,
                                  "Mobile Number is not valid",
                                  context);
                      
                              return;
                            }
                            isSubmitting = true;
                            widget.registrationLoginBloc.add(
                                RequestOtpEvent(mobileNumber: userModel.mobileNumber!, type: 'register'));
                          },
                          child: const Text('Get OTP')),
                    ),
                  )
                ],
              );
          }
      ),
    );
  }

  showOtpDialog() {
    showDialog(
        context: context,
        barrierDismissible: false,
        builder: (context) {
          final List<FocusNode> _focusNodes = List.generate(6, (index) => FocusNode());
          final List<TextEditingController> _otpControllers =
          List.generate(6, (index) => TextEditingController());
          bool isSubmit = false;
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
                            Navigator.of(context).pop();
                          },
                        ),
                      ),
                    ],
                  ),
                  Row(
                    children: [
                      Text(
                        "Verify your Aadhaar",
                        style: widget.theme.text24W700(),
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
                            "Enter the OTP sent to +91******${userModel
                                .mobileNumber!.substring(6, 10)}",
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
                                            _focusNodes[index + 1]);
                                      } else if (value.isEmpty && index > 0) {
                                        FocusScope.of(context)
                                            .requestFocus(
                                            _focusNodes[index - 1]);
                                      }
                                    },
                                    focusNode: _focusNodes[index],
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
                              widget.registrationLoginBloc.add(ResendOtpEvent(
                                  mobileNumber: userModel.mobileNumber!,
                                  type: register));
                            },
                            child: Padding(
                              padding: const EdgeInsets.only(top: 20, bottom: 10),
                              child: Row(
                                children: [
                                  Text(
                                    'Resend OTP',
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
                              'Resend a new OTP in 0:${(snapshot.data == 0) || (snapshot.data == null) ? 25
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
                              child: BlocListener<RegistrationLoginBloc, RegistrationLoginState>(
                                bloc: widget.registrationLoginBloc,
                                listener: (context, state) {
                                  switch (state.runtimeType) {
                                    case RequestFailedState:
                                      if (isSubmit) {
                                        isSubmit = false;
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
                                      DigitToast.show(
                                        context,
                                        options: DigitToastOptions(
                                          "OTP sent successfully",
                                          false,
                                          DigitTheme.instance.mobileTheme,
                                        ),
                                      );
                                      break;
                                    case OtpCorrectState:
                                      DigitToast.show(
                                        context,
                                        options: DigitToastOptions(
                                          "OTP Verified",
                                          false,
                                          DigitTheme.instance.mobileTheme,
                                        ),
                                      );
                                      Future.delayed(const Duration(seconds: 1), () {
                                        isSubmit = false;
                                        Navigator.pushNamed(context, '/NameDetailsScreen', arguments: userModel);
                                      });
                                      break;
                                    default:
                                      break;
                                  }
                                },
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
                                        DigitToast.show(
                                          context,
                                          options: DigitToastOptions(
                                            "Invalid OTP",
                                            true,
                                            DigitTheme.instance.mobileTheme,
                                          ),
                                        );
                                        return;
                                      }
                                      if (userModel.type == register && otp.length == 6) {
                                        widget.registrationLoginBloc.add(
                                            SubmitRegistrationOtpEvent(
                                                username:
                                                userModel.mobileNumber!,
                                                otp: otp,
                                                userModel: userModel));
                                      }
                                      isSubmit = true;
                                    },
                                    child: Text(
                                      'Verify',
                                      style: widget.theme.text20W700()?.apply(
                                        color: Colors.white,
                                      ),
                                    )),
                              )
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

  FormGroup buildForm() => fb.group(<String, Object>{
        mobileNumberKey: FormControl<String>(
            value: AppSharedPreference.getPhoneNumber(),
            validators: [
              Validators.required,
              Validators.number,
              Validators.minLength(10),
              Validators.maxLength(10),
              Validators.pattern(r'^[6789][0-9]{9}$')
            ]),
      });
}
