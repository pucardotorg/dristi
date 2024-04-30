import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter/services.dart';
import 'package:geolocator/geolocator.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_bloc.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_event.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_state.dart';
import 'package:pucardpg/app/data/data_sources/shared-preferences/app_shared_preference.dart';
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

  @override
  void initState() {
    super.initState();
    _getStoragePermission();
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
                mainAxisAlignment: MainAxisAlignment.end,
                children: [
                  // DigitBackButton(),
                  DigitHelpButton()
                ],
              ),
              DigitCard(
                padding: const EdgeInsets.all(20),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Provide Your Mobile Number",
                      style: widget.theme.text32W700RobCon(),
                    ),
                    const SizedBox(
                      height: 20,
                    ),
                    RichText(
                      text: TextSpan(
                        style: widget.theme.text16W400Rob(),
                        children: <TextSpan>[
                          TextSpan(
                              text:
                                  'Your mobile number will be used to login to the system going forward. We will send you a '),
                          TextSpan(
                              text: 'one-time password ',
                              style:
                                  const TextStyle(fontWeight: FontWeight.bold)),
                          TextSpan(text: 'on this mobile number')
                        ],
                      ),
                    ),
                    const SizedBox(
                      height: 20,
                    ),
                    ReactiveFormBuilder(
                        form: buildForm,
                        builder: (context, form, child) {
                          return Column(
                            mainAxisAlignment: MainAxisAlignment.start,
                            children: <Widget>[
                              DigitTextFormField(
                                label: 'Enter mobile number',
                                prefixText: "+91  ",
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
                              DigitCheckboxTile(
                                value: rememberMe,
                                label: "Remember me",
                                onChanged: (val) {
                                  setState(() {
                                    rememberMe = !rememberMe;
                                  });
                                },
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
                                        Navigator.pushNamed(context, '/');
                                      }
                                      break;
                                    case OtpGenerationSuccessState:
                                      if (isSubmitting) {
                                        isSubmitting = false;
                                        userModel.type =
                                            (state as OtpGenerationSuccessState)
                                                .type;
                                        if (userModel.type == register) {
                                          Navigator.pushNamed(
                                              context, '/UsernameScreen',
                                              arguments: userModel);
                                        } else {
                                          Navigator.pushNamed(
                                              context, '/MobileOtpScreen',
                                              arguments: userModel);
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
                                            if(rememberMe){
                                              AppSharedPreference.setPhoneNumber(userModel.mobileNumber!);
                                            }
                                            widget.registrationLoginBloc.add(
                                                RequestOtpEvent(
                                                    mobileNumber:
                                                        userModel.mobileNumber!));
                                          },
                                    child: const Text('Submit')),
                              )
                            ],
                          );
                        }),
                  ],
                ),
              ),
            ],
          )),
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
