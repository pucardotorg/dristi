import 'package:auto_route/auto_route.dart';
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/theme/digit_theme.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/blocs/auth-bloc/authbloc.dart';
import 'package:pucardpg/mixin/app_mixin.dart';
import 'package:pucardpg/model/litigant_model.dart';
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
                    'Continue',
                    style: widget.theme.text20W700()?.apply(
                      color: Colors.white,
                    ),
                  )),
            ),
          ],
        ));
  }
}