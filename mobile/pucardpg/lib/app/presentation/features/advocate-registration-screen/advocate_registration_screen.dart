import 'dart:io';

import 'package:country_state_city/utils/state_utils.dart';
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/digit_dropdown.dart';
import 'package:digit_components/widgets/atoms/digit_reactive_dropdown.dart';
import 'package:digit_components/widgets/atoms/digit_text_form_field.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:open_file/open_file.dart';
import 'package:pucardpg/app/data/models/state_model/state_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:pucardpg/core/constant/constants.dart';
import 'package:reactive_forms/reactive_forms.dart';
import 'package:syncfusion_flutter_pdfviewer/pdfviewer.dart';


class AdvocateRegistrationScreen extends StatefulWidget with AppMixin{

  const AdvocateRegistrationScreen({super.key, });

  @override
  AdvocateRegistrationScreenState createState() => AdvocateRegistrationScreenState();

}

class AdvocateRegistrationScreenState extends State<AdvocateRegistrationScreen> {

  bool firstChecked = false;
  String? fileName;
  FilePickerResult? result;
  PlatformFile? pickedFile;
  File? fileToDisplay;

  String stateKey = 'state';
  String registrationNumberKey = 'registrationNumber';
  String fileNameKey = 'fileName';

  @override
  void initState() {
    super.initState();
  }

  void pickFile() async {
    try {
      result = await FilePicker.platform.pickFiles(
        type: FileType.custom,
        allowedExtensions: ['pdf'],
        allowMultiple: false
      );
      if (result != null) {
        fileName = result!.files.first.name;
        pickedFile = result!.files.first;
        // List<dynamic> files = result!.files;
        //   files = result!.files.map((e) => File(e.path ?? '')).toList();
        setState(() {
          fileToDisplay = File(result!.files.single.path!);
        });
      }
    } catch(e) {
      print(e);
    }
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
                            DigitBackButton(
                              onPressed: () {
                                Navigator.of(context).pop();
                              },
                            ),
                            const DigitHelpButton()
                          ],
                        ),
                        DigitCard(
                          padding: const EdgeInsets.all(20),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text("Registration", style: widget.theme.text20W400Rob()?.apply(fontStyle: FontStyle.italic),),
                              const SizedBox(height: 20,),
                              Text("Provide details for the following", style: widget.theme.text32W700RobCon()?.apply(),),
                              const SizedBox(height: 20,),
                              DigitReactiveDropdown(
                                label: 'State of registration',
                                formControlName: stateKey,
                                isRequired: true,
                                onChanged: (val) { },
                                menuItems: state,
                                validationMessages: {
                                  'required': (_) => 'Mobile number is required',
                                },
                                valueMapper: (value) => value.toUpperCase(),
                              ),
                              const SizedBox(height: 20,),
                              DigitTextFormField(
                                label: 'BAR registration number',
                                isRequired: true,
                                formControlName: registrationNumberKey,
                                onChanged: (val) { },
                                validationMessages: {
                                  'required': (_) => 'Mobile number is required',
                                },
                              ),
                              const SizedBox(height: 20,),
                              Row(
                                crossAxisAlignment: CrossAxisAlignment.end,
                                children: [
                                  Expanded(
                                    child: DigitTextField(
                                      label: 'Upload BAR council ID',
                                      isRequired: true,
                                      readOnly: true,
                                      controller: TextEditingController(text: fileName ?? ''),
                                      onChange: (val) { },
                                    ),
                                  ),
                                  const SizedBox(width: 10,),
                                  SizedBox(
                                    height: 44,
                                    width: 120,
                                    child: DigitOutLineButton(
                                      label: 'Upload',
                                      onPressed: (){
                                        pickFile();
                                      },
                                    ),
                                  ),
                                ],
                              ),
                              if (pickedFile != null) ...[
                                const SizedBox(height: 20),
                                Container(
                                    height: 300,
                                    decoration: BoxDecoration(
                                      border: Border.all(color: Colors.grey),
                                    ),
                                    child: Center(
                                      child: SfPdfViewer.file(
                                        fileToDisplay!,
                                        onTap: (pdfDetails) {
                                          if (fileToDisplay != null) {
                                            OpenFile.open(fileToDisplay!.path);
                                          }
                                        },
                                      ),
                                    )
                                ),
                              ],
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
                      if (!form.valid && (fileName == null || fileName!.isEmpty)) {
                        DigitToast.show(context,
                          options: DigitToastOptions(
                            "All fields are required",
                            true,
                            widget.theme.theme(),
                          ),
                        );
                        return;
                      }
                      if (!form.valid) return;
                      if (fileName == null || fileName!.isEmpty) {
                        DigitToast.show(context,
                          options: DigitToastOptions(
                            "File is required",
                            true,
                            widget.theme.theme(),
                          ),
                        );
                        return;
                      }
                      Navigator.pushNamed(context, '/TermsAndConditionsScreen',);
                    },
                    child: Text('Next',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
                ),
              ],
            );
          },
        )
    );
  }

  FormGroup buildForm() => fb.group(<String, Object>{
    stateKey : FormControl<String>(
        value: '',
        validators: [
          Validators.required
        ]
    ),
    registrationNumberKey : FormControl<String>(
        value: '',
        validators: [
          Validators.required
        ]
    ),
  });

}
