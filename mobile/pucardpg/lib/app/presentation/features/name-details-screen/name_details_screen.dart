
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:reactive_forms/reactive_forms.dart';

class NameDetailsScreen extends StatefulWidget with AppMixin{

  UserModel userModel = UserModel();

  NameDetailsScreen({super.key, required this.userModel});

  @override
  NameDetailsScreenState createState() => NameDetailsScreenState();

}

class NameDetailsScreenState extends State<NameDetailsScreen> {

  late String mobile;
  String firstNameKey = 'firstName';
  String middleNameKey = 'middleName';
  String lastNameKey = 'lastName';
  TextEditingController searchController = TextEditingController();

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
        body: ReactiveFormBuilder(
            form: buildForm,
            builder: (context, form, child) {
              return Column(
                children: [
                  Expanded(
                    child: SingleChildScrollView(
                      child: Column(
                        children: [
                          const SizedBox(height: 10,),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              DigitBackButton(),
                              DigitHelpButton()
                            ],
                          ),
                          DigitCard(
                            padding: const EdgeInsets.all(20),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text("Registration", style: widget.theme.text20W400Rob()?.apply(fontStyle: FontStyle.italic),),
                                const SizedBox(height: 20,),
                                Text("Enter Your Name", style: widget.theme.text32W700RobCon()?.apply(),),
                                const SizedBox(height: 20,),
                                DigitTextFormField(
                                  label: 'First name',
                                  formControlName: firstNameKey,
                                  isRequired: true,
                                  onChanged: (val) { widget.userModel.firstName = val.value.toString(); },
                                  keyboardType: TextInputType.text,
                                  inputFormatters: [
                                    FilteringTextInputFormatter.allow(RegExp(r'[a-zA-Z]')),
                                  ],
                                  validationMessages: {
                                    'required': (_) => 'First name is required',
                                    'minLength': (_) =>
                                    'First name should be minimum of 2 characters',
                                    'maxLength': (_) =>
                                    'First name should be maximum of 2 characters',
                                  },
                                ),
                                const SizedBox(height: 20,),
                                DigitTextFormField(
                                  label: 'Middle name',
                                  onChanged: (val) { widget.userModel.middleName = val.value.toString(); },
                                  formControlName: middleNameKey,
                                  isRequired: false,
                                  keyboardType: TextInputType.text,
                                  inputFormatters: [
                                    FilteringTextInputFormatter.allow(RegExp(r'[a-zA-Z]')),
                                  ],
                                ),
                                const SizedBox(height: 20,),
                                DigitTextFormField(
                                  label: 'Last name',
                                  formControlName: lastNameKey,
                                  onChanged: (val) { widget.userModel.lastName = val.value.toString(); },
                                  isRequired: true,
                                  keyboardType: TextInputType.text,
                                  inputFormatters: [
                                    FilteringTextInputFormatter.allow(RegExp(r'[a-zA-Z]')),
                                  ],
                                  validationMessages: {
                                    'required': (_) => 'Last name is required',
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
                  DigitElevatedButton(
                      onPressed: () {
                        FocusScope.of(context).unfocus();
                        form.markAllAsTouched();
                        if (!form.valid) return;
                        Navigator.pushNamed(context, '/AddressScreen', arguments: widget.userModel);
                      },
                      child: Text('Next',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
                  ),
                ],
              );
            }
        )
    );

  }


  FormGroup buildForm() => fb.group(<String, Object>{
    firstNameKey : FormControl<String>(
        value: widget.userModel.firstName,
        validators: [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(128)
        ]
    ),
    middleNameKey : FormControl<String>(
        value: widget.userModel.middleName,
        validators: []
    ),
    lastNameKey : FormControl<String>(
        value: widget.userModel.lastName,
        validators: [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(128)
        ]
    ),
  });

}
