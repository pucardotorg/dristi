import 'package:auto_route/auto_route.dart';
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:digit_components/widgets/widgets.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/blocs/app-localization-bloc/app_localization.dart';
import 'package:pucardpg/blocs/auth-bloc/authbloc.dart';
import 'package:pucardpg/mixin/app_mixin.dart';
import '../utils/i18_key_constants.dart' as i18;
import 'package:pucardpg/routes/routes.dart';
import 'package:pucardpg/widget/help_button.dart';
import 'package:pucardpg/widget/page_heading.dart';

import 'package:reactive_forms/reactive_forms.dart';

@RoutePage()
class NameDetailsScreen extends StatefulWidget with AppMixin {

  NameDetailsScreen({super.key});

  @override
  NameDetailsScreenState createState() => NameDetailsScreenState();
}

class NameDetailsScreenState extends State<NameDetailsScreen> {
  late String mobile;
  String firstNameKey = 'firstName';
  String middleNameKey = 'middleName';
  String lastNameKey = 'lastName';
  TextEditingController searchController = TextEditingController();
  bool isSubmitting = false;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: _onBackPressed,
      child: Scaffold(
          backgroundColor: Colors.white,
          body: ReactiveFormBuilder(
              form: buildForm,
              builder: (context, form, child) {
                return Column(
                  children: [
                    const SizedBox(
                      height: 50,
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: [
                        DigitHelpButton()
                      ],
                    ),
                    Expanded(
                      child: SingleChildScrollView(
                        child: Column(
                          children: [
                            Padding(
                              padding: const EdgeInsets.all(25),
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  PageHeading(
                                    heading: AppLocalizations.of(context).translate(i18.userDetails.csEnterName),
                                    subHeading: AppLocalizations.of(context).translate(i18.userDetails.csEnterNameSubText),
                                    headingStyle: widget.theme.text24W700(),
                                    subHeadingStyle: widget.theme.text14W400Rob(),
                                  ),
                                  DigitTextFormField(
                                    label: AppLocalizations.of(context).translate(i18.userDetails.coreCommonFirstName),
                                    formControlName: firstNameKey,
                                    onChanged: (val) {
                                      context.read<AuthBloc>().userModel.firstName =
                                          val.value.toString();
                                    },
                                    keyboardType: TextInputType.text,
                                    inputFormatters: [
                                      FilteringTextInputFormatter.allow(
                                          RegExp(r'^[a-zA-Z][a-zA-Z ]*')),
                                    ],
                                    validationMessages: {
                                      'required': (_) =>
                                      'First name is required',
                                      'minLength': (_) =>
                                      'First name should be minimum of 2 characters',
                                      'maxLength': (_) =>
                                      'First name should be maximum of 2 characters',
                                    },
                                  ),
                                  const SizedBox(
                                    height: 10,
                                  ),
                                  DigitTextFormField(
                                    label: AppLocalizations.of(context).translate(i18.userDetails.coreCommonMiddleName),
                                    onChanged: (val) {
                                      context.read<AuthBloc>().userModel.middleName =
                                          val.value.toString();
                                    },
                                    formControlName: middleNameKey,
                                    keyboardType: TextInputType.text,
                                    inputFormatters: [
                                      FilteringTextInputFormatter.allow(
                                          RegExp(r'^[a-zA-Z][a-zA-Z ]*')),
                                    ],
                                  ),
                                  const SizedBox(
                                    height: 10,
                                  ),
                                  DigitTextFormField(
                                    label: AppLocalizations.of(context).translate(i18.userDetails.coreCommonLastName),
                                    formControlName: lastNameKey,
                                    onChanged: (val) {
                                      context.read<AuthBloc>().userModel.lastName =
                                          val.value.toString();
                                    },
                                    keyboardType: TextInputType.text,
                                    inputFormatters: [
                                      FilteringTextInputFormatter.allow(
                                          RegExp(r'^[a-zA-Z][a-zA-Z ]*')),
                                    ],
                                    validationMessages: {
                                      'required': (_) =>
                                      'Last name is required',
                                      'minLength': (_) =>
                                      'Last name should be minimum of 2 characters',
                                      'maxLength': (_) =>
                                      'Last name should be maximum of 2 characters',
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
                    const Divider(height: 0, thickness: 2,),
                    DigitCard(
                      padding: const EdgeInsets.fromLTRB(10, 0, 10, 15),
                      child: DigitElevatedButton(
                          onPressed: isSubmitting
                              ? null
                              : () {
                            FocusScope.of(context).unfocus();
                            form.markAllAsTouched();
                            if (!form.valid) return;
                            AutoRouter.of(context)
                                .push(AddressRoute());
                            isSubmitting = false;
                          },
                          child: Text(
                            AppLocalizations.of(context).translate(i18.common.coreCommonContinue),
                            style: widget.theme.text20W700()?.apply(
                              color: Colors.white,
                            ),
                          )),
                    ),
                    // ),
                  ],
                );
              })),
    );
  }

  FormGroup buildForm() => fb.group(<String, Object>{
    firstNameKey: FormControl<String>(
        value: context.read<AuthBloc>().userModel.firstName,
        validators: [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(128)
        ]),
    middleNameKey: FormControl<String>(
        value: context.read<AuthBloc>().userModel.middleName, validators: []),
    lastNameKey: FormControl<String>(
        value: context.read<AuthBloc>().userModel.lastName,
        validators: [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(128)
        ]),
  });

  Future<bool> _onBackPressed() async {
    return await DigitDialog.show(context,
        options: DigitDialogOptions(
            titleIcon: const Icon(
              Icons.warning,
              color: Colors.red,
            ),
            titleText: 'Warning',
            contentText: 'Are you sure you want to exit the application?',
            primaryAction: DigitDialogActions(
                label: 'Yes',
                action: (BuildContext context) => SystemChannels.platform
                    .invokeMethod('SystemNavigator.pop')),
            secondaryAction: DigitDialogActions(
                label: 'No',
                action: (BuildContext context) =>
                    Navigator.of(context).pop(false)))) ??
        false;
  }
}