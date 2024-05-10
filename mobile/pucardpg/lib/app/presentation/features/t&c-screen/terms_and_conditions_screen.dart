import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_bloc.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_event.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_state.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/checkbox_tile.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/app/presentation/widgets/page_heading.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';

class TermsAndConditionsScreen extends StatefulWidget with AppMixin {
  UserModel userModel = UserModel();

  TermsAndConditionsScreen({super.key, required this.userModel});

  @override
  TermsAndConditionsScreenState createState() =>
      TermsAndConditionsScreenState();
}

class TermsAndConditionsScreenState extends State<TermsAndConditionsScreen> {
  bool firstChecked = false;
  bool secondChecked = false;
  bool thirdChecked = false;
  bool fourthChecked = false;

  bool isSubmitting = false;

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
                            heading: "Terms and Conditions",
                            subHeading: "Before diving in, we'll need to verify your identity for account setup",
                            headingStyle: widget.theme.text24W700(),
                            subHeadingStyle: widget.theme.text14W400Rob(),
                          ),
                          CheckboxTile(
                            value: firstChecked,
                            label: "By using this app, you agree to abide by our community guidelines, fostering a respectful and inclusive environment for all users",
                            onChanged: (val) {
                              setState(() {
                                firstChecked = !firstChecked;
                              });
                            },
                          ),
                          const SizedBox(height: 15,),
                          const Divider(height: 0, thickness: 1,),
                          const SizedBox(height: 25,),
                          CheckboxTile(
                            value: secondChecked,
                            label: "Your privacy is paramount. Rest assured, your data is securely handled and never shared with third parties without your consent",
                            onChanged: (val) {
                              setState(() {
                                secondChecked = !secondChecked;
                              });
                            },
                          ),
                          const SizedBox(height: 15,),
                          const Divider(height: 0, thickness: 1,),
                          const SizedBox(height: 25,),
                          CheckboxTile(
                            value: thirdChecked,
                            label: "Please refrain from engaging in any unlawful activities while using our app, ensuring a safe and compliant platform for everyone",
                            onChanged: (val) {
                              setState(() {
                                thirdChecked = !thirdChecked;
                              });
                            },
                          ),
                          const SizedBox(height: 15,),
                          const Divider(height: 0, thickness: 1,),
                          const SizedBox(height: 25,),
                          CheckboxTile(
                            value: fourthChecked,
                            label: "We reserve the right to modify our services and terms at any time, keeping you informed of any updates through our communication channels",
                            onChanged: (val) {
                              setState(() {
                                fourthChecked = !fourthChecked;
                              });
                            },
                          ),
                          const SizedBox(height: 15,),
                          const Divider(height: 0, thickness: 1,),
                          // const SizedBox(height: 20,),
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
                  case RequestFailedState:
                    isSubmitting = false;
                    widget.theme.showDigitDialog(
                        true, (state as RequestFailedState).errorMsg, context);
                    break;
                  case LitigantSubmissionSuccessState:
                    Navigator.pushNamed(context, '/SuccessScreen',
                        arguments: widget.userModel);
                    break;
                  case AdvocateSubmissionSuccessState:
                    Navigator.pushNamed(context, '/AdvocateHomePage',
                        arguments: widget.userModel);
                    break;
                  case AdvocateClerkSubmissionSuccessState:
                    Navigator.pushNamed(context, '/AdvocateHomePage',
                        arguments: widget.userModel);
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
                            isSubmitting = true;
                            if (firstChecked == false || secondChecked == false || thirdChecked == false || fourthChecked == false) {
                              isSubmitting = false;
                              widget.theme.showDigitDialog(true,
                                  "Select all Terms and Conditions", context);
                              return;
                            }
                            widget.registrationLoginBloc.add(SubmitIndividualProfileEvent(
                              userModel: widget.userModel
                            ));
                            // if (widget.userModel.userType == 'ADVOCATE') {
                            //   widget.registrationLoginBloc.add(
                            //       SubmitAdvocateProfileEvent(
                            //           userModel: widget.userModel));
                            // }
                            // if (widget.userModel.userType == 'ADVOCATE_CLERK') {
                            //   widget.registrationLoginBloc.add(
                            //       SubmitAdvocateClerkProfileEvent(
                            //           userModel: widget.userModel));
                            // }
                            // if (widget.userModel.userType == 'LITIGANT') {
                            //   widget.registrationLoginBloc.add(
                            //       SubmitLitigantProfileEvent(
                            //           userModel: widget.userModel));
                            // }
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
