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
import 'package:pucardpg/app/presentation/widgets/page_heading.dart';
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
        backgroundColor: Colors.white,
        body: Column(
          children: [
            Expanded(
              child: SingleChildScrollView(
                child: Column(
                  children: [
                    const SizedBox(
                      height: 50,
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [DigitBackButton(), DigitHelpButton()],
                    ),
                    Padding(
                      padding: const EdgeInsets.all(20),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          PageHeading(
                            heading: "Tell us a bit about yourself",
                            subHeading: "This will help us streamline a few things and personalise your experience",
                            headingStyle: widget.theme.text24W700(),
                            subHeadingStyle: widget.theme.text14W400Rob(),
                          ),
                          RadioListTile(
                            title: Padding(
                              padding: const EdgeInsets.only(bottom: 10),
                              child: Text(
                                'I’m a litigant',
                                style: widget.theme.text20W700(),
                              ),
                            ),
                            subtitle: Text(
                              'I have to file a complaint, join a case, or have a complaint against me',
                              style: widget.theme.text12W400(),
                            ),
                            value: 'Litigant',
                            groupValue: selectedOption,
                            contentPadding: EdgeInsets.zero,
                            onChanged: (value) {
                              setState(() {
                                selectedOption = value;
                              });
                            },
                            fillColor: MaterialStatePropertyAll(widget.theme.defaultColor),
                          ),
                          const SizedBox(height: 15,),
                          const Divider(height: 0, thickness: 1,),
                          const SizedBox(height: 25,),
                          RadioListTile(
                            title: Padding(
                              padding: const EdgeInsets.only(bottom: 10),
                              child: Text(
                                'I’m an advocate',
                                style: widget.theme.text20W700(),
                              ),
                            ),
                            subtitle: Text(
                              'I’m professionally qualified to plead the cause of another in a court of law',
                              style: widget.theme.text12W400(),
                            ),
                            value: 'Advocate',
                            groupValue: selectedOption,
                            contentPadding: EdgeInsets.zero,
                            onChanged: (value) {
                              setState(() {
                                selectedOption = value;
                              });
                            },
                            fillColor: MaterialStatePropertyAll(widget.theme.defaultColor),
                          ),
                          const SizedBox(height: 15,),
                          const Divider(height: 0, thickness: 1,),
                          const SizedBox(height: 25,),
                          RadioListTile(
                            title: Padding(
                              padding: const EdgeInsets.only(bottom: 10),
                              child: Text(
                                'I’m an advocate’s clerk',
                                style: widget.theme.text20W700(),
                              ),
                            ),
                            subtitle: Text(
                              'I organise the daily workload and administration for a group of advocates ',
                              style: widget.theme.text12W400(),
                            ),
                            value: 'Advocate_Clerk',
                            groupValue: selectedOption,
                            contentPadding: EdgeInsets.zero,
                            onChanged: (value) {
                              setState(() {
                                selectedOption = value;
                              });
                            },
                            fillColor: MaterialStatePropertyAll(widget.theme.defaultColor),
                          ),
                          const SizedBox(height: 15,),
                          const Divider(height: 0, thickness: 1,),
                        ],
                      ),
                    ),
                    // Expanded(child: Container(),),
                  ],
                ),
              ),
            ),
            const Divider(height: 0, thickness: 2,),
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
                      Navigator.pushNamedAndRemoveUntil(context,
                        '/', arguments: widget.userModel,// Replace with your actual route name
                            (Route route) => false, // This predicate will always return false, clearing the stack
                      );
                    });
                    break;
                }
              },
              child: DigitCard(
                padding: const EdgeInsets.fromLTRB(10, 0, 10, 15),
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
                            } else if (widget.userModel.userType == 'ADVOCATE') {
                              Navigator.pushNamed(
                                  context, '/AdvocateRegistrationScreen',
                                  arguments: widget.userModel);
                              // widget.registrationLoginBloc.add(
                              //     SubmitAdvocateIndividualEvent(
                              //         userModel: widget.userModel));
                            } else {
                              Navigator.pushNamed(
                                  context, '/TermsAndConditionsScreen',
                                  arguments: widget.userModel);
                              // widget.registrationLoginBloc.add(
                              //     SubmitAdvocateClerkIndividualEvent(
                              //         userModel: widget.userModel));
                            }
                          },
                    child: Text(
                      'Continue',
                      style: widget.theme.text20W700()?.apply(
                            color: Colors.white,
                          ),
                    )),
              ),
            )
          ],
        ));
  }
}
