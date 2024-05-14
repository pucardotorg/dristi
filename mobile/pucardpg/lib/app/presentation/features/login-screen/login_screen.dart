import 'dart:async';

import 'package:digit_components/digit_components.dart';
import 'package:digit_components/theme/digit_theme.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
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
import 'package:pucardpg/app/presentation/widgets/page_heading.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:reactive_forms/reactive_forms.dart';

class LoginScreen extends StatefulWidget with AppMixin {
  LoginScreen({super.key});

  @override
  LoginScreenState createState() => LoginScreenState();
}

class LoginScreenState extends State<LoginScreen> {
  bool isSubmitting = false;
  UserModel userModel = UserModel();
  String mobileNumberKey = 'mobileNumber';

  TextEditingController searchController = TextEditingController();

  int _counter = 0;
  late StreamController<int> _events;

  @override
  void initState() {
    super.initState();
    _getStoragePermission();
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

  Future<bool> _handleLocationPermission() async {
    bool serviceEnabled;
    LocationPermission permission;

    serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text(
              'Location services are disabled. Please enable the services')));
      return false;
    }
    permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Location permissions are denied')));
        return false;
      }
    }
    if (permission == LocationPermission.deniedForever) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text(
              'Location permissions are permanently denied, we cannot request permissions.')));
      return false;
    }
    return true;
  }

  Future _getStoragePermission() async {
    var status = await Permission.storage.status;
    if (!status.isGranted) {
      await Permission.storage.request();
    }
    _handleLocationPermission();
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
                  child: Padding(
                    padding: const EdgeInsets.all(20),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        const SizedBox(
                          height: 90,
                        ),
                        Image.asset(
                          govtIndia,
                          height: 100,
                        ),
                        const SizedBox(
                          height: 40,
                        ),
                        Text(
                          "Sign In to your account",
                          style: widget.theme.text32W700RobCon(),
                        ),
                        const SizedBox(
                          height: 10,
                        ),
                        Text(
                          "Welcome back! Please enter your credentials",
                          style: widget.theme.text14W400Rob(),
                        ),
                        const SizedBox(
                          height: 30,
                        ),
                        ReactiveFormBuilder(
                            form: buildForm,
                            builder: (context, form, child) {
                              return Column(
                                mainAxisAlignment: MainAxisAlignment.start,
                                children: <Widget>[
                                  DigitTextFormField(
                                    label: 'Phone No',
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
                                  BlocListener<RegistrationLoginBloc,
                                      RegistrationLoginState>(
                                    bloc: widget.registrationLoginBloc,
                                    listener: (context, state) {
                                      switch (state.runtimeType) {
                                        case RequestOtpFailedState:
                                          if (isSubmitting) {
                                            isSubmitting = false;
                                            widget.theme.showDigitDialog(
                                                true,
                                                (state as RequestOtpFailedState)
                                                    .errorMsg,
                                                context);
                                          }
                                          break;
                                        case OtpGenerationSuccessState:
                                          if (isSubmitting) {
                                            isSubmitting = false;
                                            userModel.type =
                                                (state as OtpGenerationSuccessState)
                                                    .type;
                                            if (userModel.type == login) {
                                              _startTimer();
                                              showOtpDialog();
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
                                              RequestOtpEvent(
                                                  mobileNumber:
                                                  userModel.mobileNumber!, type: 'login'));
                                        },
                                        child: const Text('Sign In')),
                                  )
                                ],
                              );
                            }),
                        const SizedBox(
                          height: 20,
                        ),
                        RichText(
                            text: TextSpan(
                              style: widget.theme.text16W400Rob(),
                              children: <TextSpan>[
                                const TextSpan(
                                    text:
                                    "Don't have an account?"),
                                TextSpan(
                                  text: '  Register here ',
                                  style:
                                  TextStyle(fontWeight: FontWeight.bold, color: widget.theme.defaultColor),
                                  recognizer: TapGestureRecognizer()
                                    ..onTap = () {
                                      Navigator.pushNamed(
                                          context, '/MobileNumberScreen');
                                      // showOtpDialog();
                                    },
                                ),
                              ],
                            )
                        )
                      ],
                    ),
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.all(30),
                child: RichText(
                    text: TextSpan(
                      style: widget.theme.text14W400Rob(),
                      children: const <TextSpan>[
                        TextSpan(
                            text:
                            "Powered by"),
                        TextSpan(
                          text: ' DRISTI',
                          style:
                          TextStyle(fontWeight: FontWeight.bold),
                        ),
                      ],
                    )
                ),
              )
            ],
          )
      ),
    );
  }

  FormGroup buildForm() => fb.group(<String, Object>{
    mobileNumberKey: FormControl<String>(
        value: userModel.mobileNumber,
        validators: [
          Validators.required,
          Validators.number,
          Validators.minLength(10),
          Validators.maxLength(10),
          Validators.pattern(r'^[6789][0-9]{9}$')
        ]),
  });

  // showOtpDialog() {
  //   DigitDialog.show(context,
  //       options: DigitDialogOptions(
  //           titleIcon: const Icon(
  //             Icons.warning,
  //             color: Colors.red,
  //           ),
  //           titleText: 'Verify Mobile Number',
  //           contentText: 'Enter the OTP sent to +91 ${userModel.mobileNumber}',
  //           primaryAction: DigitDialogActions(
  //             label: 'Confirm',
  //             action: (BuildContext context) {
  //               //your_primary_action();
  //             },
  //           ),
  //           secondaryAction: DigitDialogActions(
  //               label: 'Cancel',
  //               action: (BuildContext context) => {
  //
  //               }
  //             //your_secondary_action(),
  //           ),
  //       )
  //   );
  // }

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
                                      case IndividualSearchSuccessState:
                                        List<Individual> listIndividuals =
                                            (state as IndividualSearchSuccessState)
                                                .individualSearchResponse
                                                .individual;
                                        if (listIndividuals.isEmpty) {
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
                                            Navigator.pushNamed(context, '/YetToRegister', arguments: userModel);
                                          });
                                        } else {
                                          if (userModel.userType == 'LITIGANT') {
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
                                              Navigator.pushNamed(context, '/UserHomeScreen', arguments: userModel);
                                            });
                                          }
                                        }
                                        break;
                                      case AdvocateSearchSuccessState:
                                        List<Advocate> advocates =
                                            (state as AdvocateSearchSuccessState)
                                                .advocateSearchResponse
                                                .advocates;
                                        // if (advocates.isEmpty) {
                                        //   DigitToast.show(
                                        //     context,
                                        //     options: DigitToastOptions(
                                        //       "OTP Verified",
                                        //       false,
                                        //       DigitTheme.instance.mobileTheme,
                                        //     ),
                                        //   );
                                        //   Future.delayed(const Duration(seconds: 1), () {
                                        //     isSubmit = false;
                                        //     Navigator.pushNamed(context, '/AdvocateRegistrationScreen', arguments: userModel);
                                        //   });
                                        // }
                                        if (advocates.isNotEmpty) {
                                          DigitToast.show(
                                            context,
                                            options: DigitToastOptions(
                                              "OTP Verified",
                                              false,
                                              DigitTheme.instance.mobileTheme,
                                            ),
                                          );
                                          if (advocates[0].status != "INACTIVE" && advocates[0].isActive == false) {
                                            Future.delayed(const Duration(seconds: 1), () {
                                              isSubmit = false;
                                              Navigator.pushNamed(context, '/AdvocateHomePage', arguments: userModel);
                                            });
                                          }
                                          if (advocates[0].status == "INACTIVE" && advocates[0].isActive == false) {
                                            Future.delayed(const Duration(seconds: 1), () {
                                              isSubmit = false;
                                              Navigator.pushNamed(context, '/NameDetailsScreen', arguments: userModel);
                                            });
                                          }
                                          if (advocates[0].status == "ACTIVE" && advocates[0].isActive == true) {
                                            Future.delayed(const Duration(seconds: 1), () {
                                              isSubmit = false;
                                              Navigator.pushNamed(context, '/UserHomeScreen', arguments: userModel);
                                            });
                                          }
                                        }
                                        break;
                                      case AdvocateClerkSearchSuccessState:
                                        List<Clerk> clerks =
                                            (state as AdvocateClerkSearchSuccessState)
                                                .advocateClerkSearchResponse
                                                .clerks;
                                        if (clerks.isEmpty) {
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
                                            Navigator.pushNamed(context, '/AdvocateRegistrationScreen', arguments: userModel);
                                          });
                                        } else {
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
                                            Navigator.pushNamed(context, '/AdvocateHomePage', arguments: userModel);
                                          });
                                        }
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
                                        if (userModel.type == login && otp.length == 6) {
                                          widget.registrationLoginBloc.add(
                                              SendLoginOtpEvent(
                                                  username:
                                                  userModel.mobileNumber!,
                                                  password: otp,
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

  // void startTimer() {
  //   const oneSec = Duration(seconds: 1);
  //   _timer = Timer.periodic(
  //     oneSec,
  //         (Timer timer) {
  //       if (_start == 0) {
  //         setState(() {
  //           timer.cancel();
  //         });
  //       } else {
  //         setState(() {
  //           _start--;
  //         });
  //       }
  //     },
  //   );
  // }

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