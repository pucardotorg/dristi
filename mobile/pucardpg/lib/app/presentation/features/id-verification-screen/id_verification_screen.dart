
import 'dart:io';

import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_bloc.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_event.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_state.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:reactive_forms/reactive_forms.dart';

class IdVerificationScreen extends StatefulWidget with AppMixin{

  UserModel userModel = UserModel();

  IdVerificationScreen({super.key, required this.userModel});

  @override
  IdVerificationScreenState createState() => IdVerificationScreenState();

}

class IdVerificationScreenState extends State<IdVerificationScreen> {

  bool fileSizeExceeded = false;
  bool extensionError = false;
  late String mobile;
  TextEditingController aadharController = TextEditingController();
  TextEditingController typeOfIdController = TextEditingController();

  String aadharKey = 'aadhar';
  String typeKey = 'type';

  String? fileName;
  FilePickerResult? result;
  PlatformFile? pickedFile;
  File? fileToDisplay;

  @override
  void initState() {
    super.initState();
  }

  bool _validateAadharNumber(String value) {
    final RegExp mobileRegex = RegExp(r'^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$', caseSensitive: false);
    return mobileRegex.hasMatch(value);
  }

  void pickFile(FormGroup form) async {
    try {
      result = await FilePicker.platform.pickFiles(
          type: FileType.custom,
          allowedExtensions: ['pdf', 'jpg', 'png'],
          allowMultiple: false,
          withData: true,
      );
      if (result != null) {
        final file = File(result!.files.single.path!);
        final fileSize = await file.length(); // Get file size in bytes
        const maxFileSize = 5 * 1024 * 1024; // 5MB in bytes

        if (!['pdf', 'jpg', 'png'].contains(result!.files.single.extension)) {
          setState(() {
            extensionError = true;
          });
        } else {
          if (fileSize <= maxFileSize) {
            widget.userModel.documentType = result!.files.single.extension;
            pickedFile = result!.files.single;
            if (pickedFile != null) {
              widget.fileBloc.add(FileEvent(pickedFile: pickedFile!));
            }
            setState(() {
              fileName = result!.files.single.name;
              form.control(aadharKey).value = '';
              fileToDisplay = file;
              extensionError = false;
              fileSizeExceeded = false;
            });
          } else {
            setState(() {
              extensionError = false;
              fileSizeExceeded = true;
            });
          }
        }
      }
    } catch(e) {
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: _onBackPressed,
      child: Scaffold(
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
                          DigitCard(
                            padding: const EdgeInsets.all(20),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text("Registration", style: widget.theme.text20W400Rob()?.apply(fontStyle: FontStyle.italic),),
                                const SizedBox(height: 20,),
                                Text("ID Verification", style: widget.theme.text32W700RobCon()?.apply(),),
                                const SizedBox(height: 20,),
                                Text("Please provide details for registration", style: widget.theme.text16W400Rob(),),
                                const SizedBox(height: 20,),
                                DigitTextFormField(
                                  label: 'Enter aadhar number',
                                  formControlName: aadharKey,
                                  keyboardType: TextInputType.number,
                                  maxLength: 12,
                                  onChanged: (val) {
                                    widget.userModel.identifierId = val.value.toString();
                                    setState(() {
                                      fileName = '';
                                      form.control(typeKey).value = '';
                                      widget.userModel.identifierType = '';
                                    });
                                  },
                                  inputFormatters: [
                                    FilteringTextInputFormatter.allow(RegExp(r'[0-9]')),
                                  ],
                                ),
                                Center(child: Text("(or)", style: widget.theme.text20W400Rob(),)),
                                DigitReactiveDropdown(
                                  label: 'Type of ID',
                                  menuItems: ['PAN', 'AADHAR', 'DRIVING LICENSE'],
                                  formControlName: typeKey,
                                  valueMapper: (value) => value.toUpperCase(),
                                  onChanged: (val) {
                                    widget.userModel.identifierType = val;
                                    setState(() {
                                      fileName = '';
                                      form.control(aadharKey).value = '';
                                      widget.userModel.identifierId = '';
                                    });
                                  },
                                ),
                                const SizedBox(height: 20,),
                                Row(
                                  crossAxisAlignment: CrossAxisAlignment.end,
                                  children: [
                                    Expanded(
                                      child: DigitTextField(
                                        label: 'Upload ID proof',
                                        controller: TextEditingController(text: fileName ?? ''),
                                        onChange: (val) {
                                          // setState(() {
                                          //
                                          // });
                                        },
                                        readOnly: true,
                                        hintText: 'No File selected',
                                      ),
                                    ),
                                    const SizedBox(width: 10,),
                                    SizedBox(
                                      height: 44,
                                      width: 120,
                                      child: BlocListener<FileBloc, FilePickerState>(
                                        bloc: widget.fileBloc,
                                        listener: (context, state) {

                                          switch (state.runtimeType) {
                                            case FileFailedState:
                                              DigitToast.show(context,
                                                options: DigitToastOptions(
                                                  "Failed to upload",
                                                  true,
                                                  widget.theme.theme(),
                                                ),
                                              );
                                              break;
                                            case FileSuccessState:
                                              widget.userModel.identifierId = (state as FileSuccessState).fileStoreId;
                                              break;
                                          }
                                        },
                                        child: DigitOutLineButton(
                                          label: 'Upload',
                                          onPressed: (){
                                            pickFile(form);
                                          },
                                        ),
                                      )
                                    ),
                                  ],
                                ),
                                const SizedBox(height: 8,),
                                if (fileSizeExceeded) // Show text line in red if file size exceeded
                                  const Text(
                                    'File Size Limit Exceeded. Upload a file below 5MB.',
                                    style: TextStyle(color: Colors.red),
                                  ),
                                if (extensionError) // Show text line in red if file size exceeded
                                  const Text(
                                    'Please select a valid file format. Upload documents in the following formats: JPG, PNG or PDF.',
                                    style: TextStyle(color: Colors.red),
                                  ),
                              ],
                            ),
                          ),
                          const DigitInfoCard(title: "Info", description: "Using Aadhar number for Verification will provide a Verified status against your profile."),
                          // Expanded(child: Container(),),
                        ],
                      ),
                    ),
                  ),
                  DigitElevatedButton(
                      onPressed: () {
                        FocusScope.of(context).unfocus();
                        if((widget.userModel.identifierId == null || widget.userModel.identifierId!.isEmpty)
                            && (widget.userModel.identifierType == null || widget.userModel.identifierType!.isEmpty)){
                          DigitToast.show(context,
                            options: DigitToastOptions(
                              "Either adhaar number or ID proof is mandatory.",
                              true,
                              widget.theme.theme(),
                            ),
                          );
                          return;
                        }
                        if(widget.userModel.identifierType!.isNotEmpty && (fileName == null || fileName!.isEmpty)) {
                          DigitToast.show(context,
                            options: DigitToastOptions(
                              "Please upload ID proof",
                              true,
                              widget.theme.theme(),
                            ),
                          );
                          return;
                        }
                        bool isValidAadharNumber = _validateAadharNumber(form.control(aadharKey).value);
                        if ((widget.userModel.identifierType == null || widget.userModel.identifierType!.isEmpty) && (widget.userModel.identifierId!.length != 12 || !isValidAadharNumber)) {
                          DigitToast.show(context,
                            options: DigitToastOptions(
                              "Enter a valid aadhar number",
                              true,
                              widget.theme.theme(),
                            ),
                          );
                          return;
                        }
                        if (widget.userModel.identifierId!.isNotEmpty
                            && (widget.userModel.identifierType == null
                                || widget.userModel.identifierType!.isEmpty)) {
                          Navigator.pushNamed(context, '/IdOtpScreen', arguments: widget.userModel);
                        }
                        if ((widget.userModel.identifierId!.isNotEmpty) && widget.userModel.identifierType!.isNotEmpty) {
                          Navigator.pushNamed(context, '/NameDetailsScreen', arguments: widget.userModel);
                        }
                      },
                      child: Text('Next',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
                  ),
                ],
              );
            }
          )
      ),
    );

  }

  FormGroup buildForm() => fb.group(<String, Object>{
    aadharKey : FormControl<String>(
        value: widget.userModel.identifierId,
    ),
    typeKey : FormControl<String>(
        value: widget.userModel.identifierType,
    ),
  });

  Future<bool> _onBackPressed() async {
    return await DigitDialog.show(
        context,
        options: DigitDialogOptions(
            titleIcon: const Icon(
              Icons.warning,
              color: Colors.red,
            ),
            titleText: 'Warning',
            contentText:
            'Are you sure you want to exit the application?',
            primaryAction: DigitDialogActions(
                label: 'Yes',
                action: (BuildContext context) =>
                    SystemChannels.platform.invokeMethod('SystemNavigator.pop')
            ),
            secondaryAction: DigitDialogActions(
                label: 'No',
                action: (BuildContext context) =>
                    Navigator.of(context).pop(false)
            ))
    ) ?? false;
  }

}
