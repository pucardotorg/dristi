import 'package:auto_route/auto_route.dart';
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/blocs/app-localization-bloc/app_localization.dart';
import 'package:pucardpg/blocs/auth-bloc/authbloc.dart';
import 'package:pucardpg/data/secure_storage/secureStore.dart';
import 'package:pucardpg/mixin/app_mixin.dart';
import '../utils/i18_key_constants.dart' as i18;
import 'package:dio/dio.dart';
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
                            heading: AppLocalizations.of(context)
                                .translate(i18.terms.esCommonUserTermsAndConditions),
                            subHeading: AppLocalizations.of(context)
                                .translate(i18.idVerification.csVerifyIdentitySubText),
                            headingStyle: widget.theme.text24W700(),
                            subHeadingStyle: widget.theme.text14W400Rob()?.apply(color: widget.theme.lightGrey),
                          ),
                          CheckboxTile(
                            value: firstChecked,
                            label: AppLocalizations.of(context)
                                .translate(i18.terms.firstTermsAndConditions),
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
                            label: AppLocalizations.of(context)
                                .translate(i18.terms.secondTermsAndConditions),
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
                            label: AppLocalizations.of(context)
                                .translate(i18.terms.thirdTermsAndConditions),
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
                            label: AppLocalizations.of(context)
                                .translate(i18.terms.fourthTermsAndConditions),
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
                  error: (requestOptions, handler) async {
                    isSubmitting = false;
                    // DigitToast.show(
                    //   context,
                    //   options: DigitToastOptions(
                    //     "Try Again",
                    //     true,
                    //     widget.theme.theme(),
                    //   ),
                    // );
                    final newAuthToken = await SecureStore().getAccessToken();
                    requestOptions.data["RequestInfo"]["authToken"] = newAuthToken;

                    final retryResponse = await Dio().fetch(requestOptions);
                    return handler.resolve(retryResponse);
                  },
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
                      AppLocalizations.of(context)
                          .translate(i18.common.coreCommonContinue),
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