

import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:geolocator/geolocator.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:reactive_forms/reactive_forms.dart';

class MobileNumberScreen extends StatefulWidget with AppMixin{

  const MobileNumberScreen({super.key});

  @override
  MobileNumberScreenState createState() => MobileNumberScreenState();

}

class MobileNumberScreenState extends State<MobileNumberScreen> {

  bool rememberMe = false;
  late String mobile;
  String mobileNumberKey = 'mobileNumber';

  TextEditingController searchController = TextEditingController();

  @override
  void initState() {
    fetchStates('IN');
    super.initState();
  }

  bool _validateMobile(String value) {
    final RegExp mobileRegex = RegExp(r'^[6789][0-9]{9}$', caseSensitive: false);
    return mobileRegex.hasMatch(value);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
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
            const SizedBox(height: 10,),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                DigitBackButton(
                  onPressed: (){
                    Navigator.of(context).pop();
                  },
                ),
                DigitHelpButton()
              ],
            ),
            DigitCard(
              padding: const EdgeInsets.all(20),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text("Provide Your Mobile Number", style: widget.theme.text32W700RobCon(),),
                    const SizedBox(height: 20,),
                    Text("Your mobile number will be used to login to the system going forward. We will send you a one-time password on this mobile number", style: widget.theme.text16W400Rob(),),
                    const SizedBox(height: 20,),
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
                                onChanged: (val) { mobile = val.value.toString(); },
                                keyboardType: TextInputType.number,
                                validationMessages: {
                                  'required': (_) => 'Mobile number is required',
                                  'number': (_) => 'Mobile number should contain digits 0-9',
                                  'minLength': (_) =>
                                  'Mobile number should have 10 digits',
                                  'maxLength': (_) =>
                                  'Mobile number should have 10 digits',
                                },
                              ),
                              const SizedBox(height: 10,),
                              DigitCheckboxTile(
                                value: rememberMe,
                                label: "Remember me",
                                onChanged: (val){
                                  setState(() {
                                    rememberMe = !rememberMe;
                                  });
                                },
                              ),
                              DigitElevatedButton(
                                  onPressed: () {
                                    FocusScope.of(context).unfocus();
                                    form.markAllAsTouched();
                                    if (!form.valid) return;
                                    bool isValidNumber = _validateMobile(form.control(mobileNumberKey).value);
                                    if (!isValidNumber) {
                                      DigitToast.show(context,
                                        options: DigitToastOptions(
                                          "Mobile Number is not valid",
                                          true,
                                          widget.theme.theme(),
                                        ),
                                      );
                                      return;
                                    }
                                    Navigator.pushNamed(context, '/MobileOtpScreen', arguments: mobile);
                                  },
                                  child: const Text('Submit')
                              )
                            ],
                          );
                        }
                    ),
                  ],
                ),
            ),
          ],
        )
    );
  }

  FormGroup buildForm() => fb.group(<String, Object>{
    mobileNumberKey : FormControl<String>(
        value: '',
        validators: [
          Validators.required,
          Validators.number,
          Validators.minLength(10),
          Validators.maxLength(10)
        ]
    ),
  });

}
