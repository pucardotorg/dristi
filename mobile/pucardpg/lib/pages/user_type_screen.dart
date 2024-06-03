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
import 'package:pucardpg/mixin/app_mixin.dart';
import '../utils/i18_key_constants.dart' as i18;
import 'package:pucardpg/routes/routes.dart';
import 'package:pucardpg/widget/back_button.dart';
import 'package:pucardpg/widget/help_button.dart';
import 'package:pucardpg/widget/page_heading.dart';

@RoutePage()
class UserTypeScreen extends StatefulWidget with AppMixin {

  UserTypeScreen({super.key});

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
                            heading: AppLocalizations.of(context).translate(i18.userSelection.selectUserTypeText),
                            subHeading: AppLocalizations.of(context).translate(i18.userSelection.selectUserTypeSubText),
                            headingStyle: widget.theme.text24W700(),
                            subHeadingStyle: widget.theme.text14W400Rob()?.apply(color: widget.theme.lightGrey),
                          ),
                          RadioListTile(
                            title: Padding(
                              padding: const EdgeInsets.only(bottom: 10),
                              child: Text(
                                AppLocalizations.of(context).translate(i18.userSelection.litigantText),
                                style: widget.theme.text20W700(),
                              ),
                            ),
                            subtitle: Text(
                              AppLocalizations.of(context).translate(i18.userSelection.litigantSubText),
                              style: widget.theme.text12W400()?.apply(color: widget.theme.lightGrey),
                            ),
                            value: 'Litigant',
                            groupValue: selectedOption,
                            contentPadding: EdgeInsets.zero,
                            onChanged: (value) {
                              setState(() {
                                selectedOption = value;
                              });
                            },
                            activeColor: widget.theme.defaultColor,
                          ),
                          const SizedBox(height: 15,),
                          const Divider(height: 0, thickness: 1,),
                          const SizedBox(height: 25,),
                          RadioListTile(
                            title: Padding(
                              padding: const EdgeInsets.only(bottom: 10),
                              child: Text(
                                AppLocalizations.of(context).translate(i18.userSelection.advocateText),
                                style: widget.theme.text20W700(),
                              ),
                            ),
                            subtitle: Text(
                              AppLocalizations.of(context).translate(i18.userSelection.advocateSubText),
                              style: widget.theme.text12W400()?.apply(color: widget.theme.lightGrey),
                            ),
                            value: 'Advocate',
                            groupValue: selectedOption,
                            contentPadding: EdgeInsets.zero,
                            onChanged: (value) {
                              setState(() {
                                selectedOption = value;
                              });
                            },
                            activeColor: widget.theme.defaultColor,
                          ),
                          const SizedBox(height: 15,),
                          const Divider(height: 0, thickness: 1,),
                          const SizedBox(height: 25,),
                          RadioListTile(
                            title: Padding(
                              padding: const EdgeInsets.only(bottom: 10),
                              child: Text(
                                AppLocalizations.of(context).translate(i18.userSelection.advocateClerkText),
                                style: widget.theme.text20W700(),
                              ),
                            ),
                            subtitle: Text(
                              AppLocalizations.of(context).translate(i18.userSelection.advocateClerkSubText),
                              style: widget.theme.text12W400()?.apply(color: widget.theme.lightGrey),
                            ),
                            value: 'Advocate_Clerk',
                            groupValue: selectedOption,
                            contentPadding: EdgeInsets.zero,
                            onChanged: (value) {
                              setState(() {
                                selectedOption = value;
                              });
                            },
                            activeColor: widget.theme.defaultColor,
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
            DigitCard(
              padding: const EdgeInsets.fromLTRB(10, 0, 10, 15),
              child: DigitElevatedButton(
                  onPressed: isSubmitting
                      ? null
                      : () {
                    context.read<AuthBloc>().userModel.userType =
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
                    } else if (context.read<AuthBloc>().userModel.userType == 'ADVOCATE') {
                      AutoRouter.of(context)
                          .push(AdvocateRegistrationRoute());
                    } else {
                      AutoRouter.of(context)
                          .push(TermsAndConditionsRoute());
                    }
                  },
                  child: Text(
                    AppLocalizations.of(context).translate(i18.common.coreCommonContinue),
                    style: widget.theme.text20W700()?.apply(
                      color: Colors.white,
                    ),
                  )),
            ),
          ],
        ));
  }
}