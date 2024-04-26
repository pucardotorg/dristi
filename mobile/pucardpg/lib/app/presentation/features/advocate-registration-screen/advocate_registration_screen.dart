import 'dart:async';
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
import 'package:open_file/open_file.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_bloc.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_event.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_state.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:syncfusion_flutter_pdfviewer/pdfviewer.dart';


class AdvocateRegistrationScreen extends StatefulWidget with AppMixin{

  UserModel userModel = UserModel();

  AdvocateRegistrationScreen({super.key, required this.userModel});

  @override
  AdvocateRegistrationScreenState createState() => AdvocateRegistrationScreenState();

}

class AdvocateRegistrationScreenState extends State<AdvocateRegistrationScreen> {

  bool fileSizeExceeded = false;
  bool extensionError = false;
  String? fileName;
  FilePickerResult? result;
  PlatformFile? pickedFile;
  File? fileToDisplay;

  @override
  void initState() {
    super.initState();
  }

  void pickFile() async {
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

        if (!['pdf', 'jpg', 'png', 'jpeg'].contains(result!.files.single.extension)) {
          setState(() {
            extensionError = true;
          });
        } else {
          if (fileSize <= maxFileSize) {
            fileName = result!.files.single.name;
            pickedFile = result!.files.single;
            if (pickedFile != null) {
              widget.fileBloc.add(FileEvent(pickedFile: pickedFile!));
            }
            setState(() {
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
          body: Column(
            children: [
              Expanded(
                child: SingleChildScrollView(
                  child: Column(
                    children: [
                      const SizedBox(
                        height: 10,
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
                            if (widget.userModel.userType == 'ADVOCATE') ...[
                              DigitTextField(
                                label: 'BAR registration number',
                                isRequired: true,
                                onChange: (val) {
                                  widget.userModel.barRegistrationNumber = val;
                                },
                                inputFormatter: [
                                  FilteringTextInputFormatter.allow(
                                      RegExp("[a-zA-Z0-9\\/\\']"))
                                ],
                                textCapitalization: TextCapitalization.characters,
                              ),
                            ],
                            if (widget.userModel.userType == 'ADVOCATE_CLERK') ...[
                              DigitTextField(
                                label: 'State registration number',
                                isRequired: true,
                                onChange: (val) {
                                  widget.userModel.stateRegnNumber = val;
                                },
                                inputFormatter: [
                                  FilteringTextInputFormatter.allow(
                                      RegExp("[a-zA-Z0-9\\/\\']"))
                                ],
                                textCapitalization: TextCapitalization.characters,
                              ),
                            ],
                            const SizedBox(height: 20,),
                            Row(
                              crossAxisAlignment: CrossAxisAlignment.end,
                              children: [
                                Expanded(
                                  child: DigitTextField(
                                    label: 'Upload BAR council ID',
                                    controller: TextEditingController(text: fileName ?? ''),
                                    isRequired: true,
                                    readOnly: true,
                                    hintText: 'No File selected',
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
                            if (pickedFile != null) ...[
                              const SizedBox(height: 20),
                              if (pickedFile!.extension == 'pdf')
                                Container(
                                  height: 300,
                                  alignment: Alignment.center,
                                  decoration: BoxDecoration(
                                    border: Border.all(color: Colors.grey),
                                  ),
                                  child: SfPdfViewer.file(
                                        fileToDisplay!,
                                        onTap: (pdfDetails) {
                                          if (fileToDisplay != null) {
                                            OpenFile.open(fileToDisplay!.path);
                                          }
                                        },
                                    )
                                ),
                              if (pickedFile!.extension != 'pdf')
                                GestureDetector(
                                  child: Container(
                                    height: 300,
                                    width: 500,
                                    decoration: BoxDecoration(
                                      border: Border.all(color: Colors.grey),
                                    ),
                                    child: Image.file(fileToDisplay!,
                                      filterQuality: FilterQuality.high,
                                      fit: BoxFit.fill,
                                    ),
                                  ),
                                  onTap: () {
                                    if (pickedFile!.extension != 'pdf') {
                                      OpenFile.open(fileToDisplay!.path);
                                    }
                                  },
                                )
                            ],
                          ],
                        ),
                      ),
                      // Expanded(child: Container(),),
                    ],
                  ),
                ),
              ),
              BlocListener<FileBloc, FilePickerState>(
                bloc: widget.fileBloc,
                listener: (context, state) {

                },
                child: DigitElevatedButton(
                    onPressed: () {
                      FocusScope.of(context).unfocus();
                      if (widget.userModel.userType == 'ADVOCATE') {
                        if((widget.userModel.barRegistrationNumber == null || widget.userModel.barRegistrationNumber!.isEmpty)
                            && (fileName == null || fileName!.isEmpty)) {
                          DigitToast.show(context,
                            options: DigitToastOptions(
                              "Please fill the details",
                              true,
                              widget.theme.theme(),
                            ),
                          );
                          return;
                        }
                        if((widget.userModel.barRegistrationNumber == null || widget.userModel.barRegistrationNumber!.isEmpty)
                            && (fileName != null || fileName!.isNotEmpty)) {
                          DigitToast.show(context,
                            options: DigitToastOptions(
                              "Please fill the BAR registration number",
                              true,
                              widget.theme.theme(),
                            ),
                          );
                          return;
                        }
                        if((widget.userModel.barRegistrationNumber != null || widget.userModel.barRegistrationNumber!.isNotEmpty)
                            && (fileName == null || fileName!.isEmpty)) {
                          DigitToast.show(context,
                            options: DigitToastOptions(
                              "Please upload the BAR council ID",
                              true,
                              widget.theme.theme(),
                            ),
                          );
                          return;
                        }
                      }
                      if (widget.userModel.userType == 'ADVOCATE_CLERK') {
                        if((widget.userModel.stateRegnNumber == null || widget.userModel.stateRegnNumber!.isEmpty)
                            && (fileName == null || fileName!.isEmpty)) {
                          DigitToast.show(context,
                            options: DigitToastOptions(
                              "Please fill the details",
                              true,
                              widget.theme.theme(),
                            ),
                          );
                          return;
                        }
                        if((widget.userModel.stateRegnNumber == null || widget.userModel.stateRegnNumber!.isEmpty)
                            && (fileName != null || fileName!.isNotEmpty)) {
                          DigitToast.show(context,
                            options: DigitToastOptions(
                              "Please fill the BAR registration number",
                              true,
                              widget.theme.theme(),
                            ),
                          );
                          return;
                        }
                        if((widget.userModel.stateRegnNumber != null || widget.userModel.stateRegnNumber!.isNotEmpty)
                            && (fileName == null || fileName!.isEmpty)) {
                          DigitToast.show(context,
                            options: DigitToastOptions(
                              "Please upload the BAR council ID",
                              true,
                              widget.theme.theme(),
                            ),
                          );
                          return;
                        }
                      }

                      // getMultipartFile();
                      Navigator.pushNamed(context, '/TermsAndConditionsScreen', arguments: widget.userModel);
                    },
                    child: Text('Next',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
                ),
              )
            ],
          )
      ),
    );

  }

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
