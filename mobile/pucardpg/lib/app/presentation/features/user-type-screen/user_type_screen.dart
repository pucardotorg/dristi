import 'package:digit_components/digit_components.dart';
import 'package:digit_components/theme/digit_theme.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_bloc.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_event.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_state.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:pucardpg/core/constant/constants.dart';

class UserTypeScreen extends StatefulWidget with AppMixin {
  UserModel userModel = UserModel();

  UserTypeScreen({super.key, required this.userModel});

  @override
  UserTypeScreenState createState() => UserTypeScreenState();
}

class UserTypeScreenState extends State<UserTypeScreen> {
  bool isSubmitting = false;
  String? selectedOption;

  @override
  void initState() {
    super.initState();
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
            Expanded(
              child: SingleChildScrollView(
                child: Column(
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
                            "Choose your user type",
                            style: widget.theme.text32W700RobCon()?.apply(),
                          ),
                          const SizedBox(
                            height: 20,
                          ),
                          RadioListTile(
                            title: Text(
                              'Litigant',
                              style: widget.theme.text16W400Rob(),
                            ),
                            value: 'Litigant',
                            groupValue: selectedOption,
                            contentPadding: EdgeInsets.zero,
                            onChanged: (value) {
                              setState(() {
                                selectedOption = value;
                              });
                            },
                          ),
                          // const SizedBox(height: 20,),
                          RadioListTile(
                            title: Text(
                              'Advocate',
                              style: widget.theme.text16W400Rob(),
                            ),
                            value: 'Advocate',
                            groupValue: selectedOption,
                            contentPadding: EdgeInsets.zero,
                            onChanged: (value) {
                              setState(() {
                                selectedOption = value;
                              });
                            },
                          ),
                          // const SizedBox(height: 20,),
                          RadioListTile(
                            title: Text(
                              'Advocate Clerk',
                              style: widget.theme.text16W400Rob(),
                            ),
                            value: 'Advocate_Clerk',
                            groupValue: selectedOption,
                            contentPadding: EdgeInsets.zero,
                            onChanged: (value) {
                              setState(() {
                                selectedOption = value;
                              });
                            },
                          ),
                        ],
                      ),
                    ),
                    // Expanded(child: Container(),),
                  ],
                ),
              ),
            ),
            BlocListener<RegistrationLoginBloc, RegistrationLoginState>(
              bloc: widget.registrationLoginBloc,
              listener: (context, state) {
                switch (state.runtimeType) {
                  case LogoutFailedState:
                    isSubmitting = false;
                    widget.theme.showDigitDialog(
                        true, (state as LogoutFailedState).errorMsg, context);
                    break;
                  case LogoutSuccessState:
                    DigitToast.show(
                      context,
                      options: DigitToastOptions(
                        "We are redirecting you to login page ...",
                        false,
                        DigitTheme.instance.mobileTheme,
                      ),
                    );
                    Future.delayed(const Duration(seconds: 2), () {
                      isSubmitting = false;
                      Navigator.pushNamed(context, '/', arguments: widget.userModel);
                    });
                    break;
                }
              },
              child: DigitElevatedButton(
                  onPressed: isSubmitting
                      ? null
                      : () {
                          widget.userModel.userType =
                              selectedOption?.toUpperCase();
                          if (selectedOption == null) {
                            DigitToast.show(
                              context,
                              options: DigitToastOptions(
                                "Please select a user type.",
                                true,
                                widget.theme.theme(),
                              ),
                            );
                            return;
                          } else if (widget.userModel.userType == 'LITIGANT') {
                            Navigator.pushNamed(
                                context, '/TermsAndConditionsScreen',
                                arguments: widget.userModel);
                          } else if (widget.userModel.userType == 'ADVOCATE') {
                            isSubmitting = true;
                            widget.registrationLoginBloc.add(
                                SubmitAdvocateIndividualEvent(
                                    userModel: widget.userModel));
                          } else if (widget.userModel.userType ==
                              'ADVOCATE_CLERK') {
                            isSubmitting = true;
                            widget.registrationLoginBloc.add(
                                SubmitAdvocateClerkIndividualEvent(
                                    userModel: widget.userModel));
                          }
                        },
                  child: Text(
                    'Next',
                    style: widget.theme.text20W700()?.apply(
                          color: Colors.white,
                        ),
                  )),
            )
          ],
        ));
  }
}
