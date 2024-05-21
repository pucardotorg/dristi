import 'package:auto_route/auto_route.dart';
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/blocs/auth-bloc/authbloc.dart';
import 'package:pucardpg/mixin/app_mixin.dart';
import 'package:pucardpg/model/litigant_model.dart';
import 'package:pucardpg/routes/routes.dart';
import 'package:pucardpg/widget/back_button.dart';
import 'package:pucardpg/widget/checkbox_tile.dart';
import 'package:pucardpg/widget/help_button.dart';
import 'package:pucardpg/widget/page_heading.dart';

@RoutePage()
class TermsAndConditionsScreen extends StatefulWidget with AppMixin {

  TermsAndConditionsScreen({super.key});

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
            BlocListener<AuthBloc, AuthState>(
              bloc: context.read<AuthBloc>(),
              listener: (context, state) {
                state.maybeWhen(
                  orElse: (){},
                  profileFailedState: (error) {
                    isSubmitting = false;
                    DigitToast.show(
                      context,
                      options: DigitToastOptions(
                        "Failed",
                        true,
                        widget.theme.theme(),
                      ),
                    );
                  },
                  profileSuccessState: () {
                    final userType = context.read<AuthBloc>().userModel.userType;
                    if (userType == 'LITIGANT') {
                      AutoRouter.of(context)
                          .push(LitigantSuccessRoute());
                    } else {
                      AutoRouter.of(context)
                          .push(AdvocateSuccessRoute());
                    }
                  }
                );

                // switch (state.runtimeType) {
                //
                //   case RequestFailedState:
                //     isSubmitting = false;
                //     widget.theme.showDigitDialog(
                //         true, (state as RequestFailedState).errorMsg, context);
                //     break;
                //   case LitigantSubmissionSuccessState:
                //     // Navigator.pushNamed(context, '/SuccessScreen',
                //     //     arguments: userModel);
                //     break;
                //   case AdvocateSubmissionSuccessState:
                //     // Navigator.pushNamed(context, '/AdvocateHomePage',
                //     //     arguments: userModel);
                //     break;
                //   case AdvocateClerkSubmissionSuccessState:
                //     // Navigator.pushNamed(context, '/AdvocateHomePage',
                //     //     arguments: userModel);
                //     break;
                //   default:
                //     break;
                // }
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
                      context.read<AuthBloc>().add(
                          const AuthEvent.submitProfile()
                      );
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