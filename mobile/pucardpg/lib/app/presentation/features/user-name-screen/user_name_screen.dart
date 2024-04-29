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
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:reactive_forms/reactive_forms.dart';

class UserNameScreen extends StatefulWidget with AppMixin {
  UserModel userModel = UserModel();

  UserNameScreen({super.key, required this.userModel});

  @override
  UserNameScreenState createState() => UserNameScreenState();
}

class UserNameScreenState extends State<UserNameScreen> {
  String userNameKey = 'userName';
  bool isSubmitting = false;

  @override
  void initState() {
    super.initState();
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
                    "Provide Your Username",
                    style: widget.theme.text32W700RobCon(),
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
                              label: 'Enter Username',
                              formControlName: userNameKey,
                              isRequired: true,
                              onChanged: (val) {
                                widget.userModel.enteredUserName =
                                    val.value.toString();
                              },
                              inputFormatters: [
                                FilteringTextInputFormatter.allow(
                                    RegExp(r'[a-zA-Z]')),
                              ],
                              validationMessages: {
                                'required': (_) => 'Username is required',
                                'minLength': (_) =>
                                    'Username should be of minimum 2 length',
                                'maxLength': (_) =>
                                    'Username should be of maximum 128 length',
                              },
                            ),
                            const SizedBox(
                              height: 10,
                            ),
                            DigitElevatedButton(
                                onPressed: isSubmitting
                                    ? null
                                    : () {
                                        FocusScope.of(context).unfocus();
                                        form.markAllAsTouched();
                                        if (!form.valid) return;
                                        Navigator.pushNamed(
                                            context, '/MobileOtpScreen',
                                            arguments: widget.userModel);
                                        // widget.registrationLoginBloc.add(RequestOtpEvent(mobileNumber: userModel.mobileNumber!));
                                        isSubmitting = true;
                                      },
                                child: const Text('Submit'))
                          ],
                        );
                      }),
                ],
              ),
            ),
          ],
        ));
  }

  FormGroup buildForm() => fb.group(<String, Object>{
        userNameKey: FormControl<String>(
            value: widget.userModel.enteredUserName ?? "",
            validators: [
              Validators.required,
              Validators.minLength(2),
              Validators.maxLength(128),
            ]),
      });
}
