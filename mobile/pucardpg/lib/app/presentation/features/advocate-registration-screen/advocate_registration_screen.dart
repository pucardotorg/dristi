import 'dart:developer';
import 'dart:io';

import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:open_file/open_file.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_bloc.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_event.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_state.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:dio/dio.dart';
import 'package:syncfusion_flutter_pdfviewer/pdfviewer.dart';


class AdvocateRegistrationScreen extends StatefulWidget with AppMixin{

  UserModel userModel = UserModel();

  AdvocateRegistrationScreen({super.key, required this.userModel});

  @override
  AdvocateRegistrationScreenState createState() => AdvocateRegistrationScreenState();

}

class AdvocateRegistrationScreenState extends State<AdvocateRegistrationScreen> {

  bool fileSizeExceeded = false;
  bool firstChecked = false;
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
        allowMultiple: false
      );
      if (result != null) {
        final file = File(result!.files.single.path!);
        final fileSize = await file.length(); // Get file size in bytes
        const maxFileSize = 5 * 1024 * 1024; // 5MB in bytes
        if (fileSize <= maxFileSize) {
          fileName = result!.files.single.name;
          pickedFile = result!.files.single;
          setState(() {
            fileToDisplay = file;
            fileSizeExceeded = false;
          });
        } else {
          setState(() {
            fileSizeExceeded = true;
          });
        }
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
        body: Column(
          children: [
            Expanded(
              child: SingleChildScrollView(
                child: Column(
                  children: [
                    const SizedBox(
                      height: 10,
                    ),
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
                          Text("Provide details for the following", style: widget.theme.text32W700RobCon()?.apply(),),
                          const SizedBox(height: 20,),
                          if (widget.userModel.userType == 'Advocate') ...[
                            DigitTextField(
                              label: 'BAR registration number',
                              isRequired: true,
                              onChange: (val) {
                                widget.userModel.barRegistrationNumber = val;
                              },
                            ),
                          ],
                          if (widget.userModel.userType == 'Advocate Clerk') ...[
                            DigitTextField(
                              label: 'State registration number',
                              isRequired: true,
                              onChange: (val) {
                                widget.userModel.stateRegnNumber = val;
                              },
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
                    // getMultipartFile();
                    Navigator.pushNamed(context, '/TermsAndConditionsScreen', arguments: widget.userModel);
                  },
                  child: Text('Next',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
              ),
            )
          ],
        )
    );

  }

  Future getMultipartFile() async {
    MultipartFile multiPartFile = await MultipartFile.fromFile(fileToDisplay!.path, filename: fileName);
    inspect(multiPartFile);
    widget.fileBloc.add(FileEvent(multipartFile: multiPartFile, file: fileToDisplay!));
  }

}
