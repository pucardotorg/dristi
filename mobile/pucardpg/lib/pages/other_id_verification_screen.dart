
import 'dart:io';
import 'dart:typed_data';

import 'package:auto_route/auto_route.dart';
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:digit_components/widgets/widgets.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:open_filex/open_filex.dart';
import 'package:pucardpg/blocs/auth-bloc/authbloc.dart';
import 'package:pucardpg/blocs/file-picker-bloc/file_picker.dart';
import 'package:pucardpg/mixin/app_mixin.dart';
import 'package:pucardpg/model/litigant_model.dart';
import 'package:pucardpg/routes/routes.dart';
import 'package:pucardpg/widget/back_button.dart';
import 'package:pucardpg/widget/help_button.dart';
import 'package:pucardpg/widget/page_heading.dart';
import 'package:reactive_forms/reactive_forms.dart';
import 'package:syncfusion_flutter_pdfviewer/pdfviewer.dart';

@RoutePage()
class OtherIdVerificationScreen extends StatefulWidget with AppMixin{

  OtherIdVerificationScreen({super.key});

  @override
  OtherIdVerificationScreenState createState() => OtherIdVerificationScreenState();

}

class OtherIdVerificationScreenState extends State<OtherIdVerificationScreen> {

  bool fileSizeExceeded = false;
  bool extensionError = false;
  TextEditingController typeOfIdController = TextEditingController();

  String typeKey = 'type';

  String? fileName;
  String? idFilename;
  Uint8List? idBytes;
  FilePickerResult? result;
  PlatformFile? pickedFile;
  File? fileToDisplay;

  @override
  void initState() {
    super.initState();
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
            context.read<AuthBloc>().userModel.idDocumentType = result!.files.single.extension;
            pickedFile = result!.files.single;
            idFilename = result!.files.single.name;
            idBytes = result!.files.single.bytes;
            if (pickedFile != null) {
              context.read<FileBloc>().add(FileEvent.upload(pickedFile: pickedFile!, type: 'id'));
            }
            setState(() {
              fileName = '1 File Uploaded';
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
    return Scaffold(
        backgroundColor: Colors.white,
        body: ReactiveFormBuilder(
            form: buildForm,
            builder: (context, form, child) {
              return Column(
                children: [
                  const SizedBox(height: 50,),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      DigitBackButton(),
                      DigitHelpButton()],
                  ),
                  Expanded(
                    child: SingleChildScrollView(
                      child: Column(
                        children: [
                          Padding(
                            padding: const EdgeInsets.all(20),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                PageHeading(
                                  heading: "Upload your ID",
                                  subHeading: "Please upload a valid government issued ID",
                                  headingStyle: widget.theme.text24W700(),
                                  subHeadingStyle: widget.theme.text14W400Rob(),
                                ),
                                DigitReactiveDropdown(
                                  label: 'Type of ID',
                                  menuItems: ['PAN', 'AADHAR', 'DRIVING LICENSE'],
                                  formControlName: typeKey,
                                  valueMapper: (value) => value.toUpperCase(),
                                  isRequired: true,
                                  onChanged: (val) {
                                    context.read<AuthBloc>().userModel.identifierType = val;
                                    setState(() {
                                      fileName = null;
                                      pickedFile = null;
                                      context.read<AuthBloc>().userModel.identifierId = null;
                                    });
                                  },
                                  validationMessages: {
                                    'required': (_) => 'ID is required',
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
                                        child: BlocListener<FileBloc, FileState>(
                                          bloc: context.read<FileBloc>(),
                                          listener: (context, state) {
                                            state.maybeWhen(
                                                orElse: (){},
                                                idFailed: (error){
                                                  DigitToast.show(context,
                                                    options: DigitToastOptions(
                                                      error,
                                                      true,
                                                      widget.theme.theme(),
                                                    ),
                                                  );
                                                },
                                                uploadIdSuccess: (fileStoreId) {
                                                  context.read<AuthBloc>().userModel.identifierId = fileStoreId;
                                                  context.read<AuthBloc>().userModel.idFilename = idFilename;
                                                  context.read<AuthBloc>().userModel.idBytes = idBytes;
                                                }
                                            );
                                          },
                                          child: SizedBox(
                                            height: 44,
                                            width: 120,
                                            child: Container(
                                              constraints: const BoxConstraints(maxHeight: 50, minHeight: 40),
                                              child: OutlinedButton(
                                                onPressed: () {
                                                  pickFile(form);
                                                },
                                                style: OutlinedButton.styleFrom(
                                                  shape: const RoundedRectangleBorder(
                                                    borderRadius: BorderRadius.zero,
                                                  ),
                                                ),
                                                child: Padding(
                                                  padding: const EdgeInsets.all(2),
                                                  child: Row(
                                                    mainAxisAlignment: MainAxisAlignment.center,
                                                    children: [
                                                      Flexible(
                                                          child: Icon(
                                                            Icons.file_upload,
                                                            color: widget.theme.colorScheme.secondary,
                                                          )),
                                                      const SizedBox(width: 2),
                                                      Text(
                                                        "Upload",
                                                        style: DigitTheme.instance.mobileTheme.textTheme.headlineSmall
                                                            ?.apply(
                                                          color: widget.theme.colorScheme.secondary,
                                                        ),
                                                      ),
                                                    ],
                                                  ),
                                                ),
                                              ),
                                            ),
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
                                if (pickedFile != null) ...[
                                  const SizedBox(height: 20),
                                  if (pickedFile!.extension == 'pdf')
                                    Stack(
                                      children: [
                                        Container(
                                          height: 300,
                                          alignment: Alignment.center,
                                          decoration: BoxDecoration(
                                              border: Border.all(color: Colors.grey),
                                              borderRadius:  const BorderRadius.all(Radius.circular(21))
                                          ),
                                          child: ClipRRect(
                                            borderRadius: BorderRadius.circular(20), // Image border
                                            child: SizedBox(
                                                child: SfPdfViewer.file(
                                                  fileToDisplay!,
                                                  onTap: (pdfDetails) {
                                                    if (fileToDisplay != null) {
                                                      OpenFilex.open(fileToDisplay!.path);
                                                    }
                                                  },
                                                )
                                            ),
                                          ),

                                        ),
                                        Positioned(
                                          top: 0,
                                          right: 0,
                                          child: Container(
                                            decoration: const BoxDecoration(
                                                color: Color(0XFF0B4B66),
                                                borderRadius:  BorderRadius.only(
                                                    topRight: Radius.circular(4),
                                                    bottomLeft: Radius.circular(4)
                                                )
                                            ),
                                            child: IconButton(
                                              icon: Icon(Icons.close),
                                              color: Colors.white,
                                              onPressed: () {
                                                setState(() {
                                                  pickedFile = null;
                                                  fileName = null;
                                                  context.read<AuthBloc>().userModel.fileStore = null;
                                                });
                                              },
                                            ),
                                          ),
                                        ),
                                      ],
                                    ),
                                  if (pickedFile!.extension != 'pdf')
                                    GestureDetector(
                                      child: Stack(
                                        children: [
                                          Container(
                                            height: 300,
                                            width: 500,
                                            decoration: BoxDecoration(
                                                border: Border.all(color: Colors.grey),
                                                borderRadius:  const BorderRadius.all(Radius.circular(21))
                                            ),
                                            child: ClipRRect(
                                              borderRadius: BorderRadius.circular(20), // Image border
                                              child: SizedBox.fromSize(
                                                size: Size.fromRadius(16), // Image radius
                                                child: Image.file(
                                                  fileToDisplay!,
                                                  filterQuality: FilterQuality.high,
                                                  fit: BoxFit.cover,
                                                ),
                                              ),
                                            ),
                                          ),
                                          Positioned(
                                            top: 0,
                                            right: 0,
                                            child: Container(
                                              decoration: const BoxDecoration(
                                                  color: Color(0XFF0B4B66),
                                                  borderRadius:  BorderRadius.only(
                                                      topRight: Radius.circular(4),
                                                      bottomLeft: Radius.circular(4)
                                                  )
                                              ),
                                              child: IconButton(
                                                icon: Icon(Icons.close),
                                                color: Colors.white,
                                                onPressed: () {
                                                  setState(() {
                                                    pickedFile = null;
                                                    fileName = null;
                                                    context.read<AuthBloc>().userModel.identifierId = null;
                                                  });
                                                },
                                              ),
                                            ),
                                          ),
                                        ],
                                      ),
                                      onTap: () {
                                        if (pickedFile!.extension != 'pdf') {
                                          OpenFilex.open(fileToDisplay!.path);
                                        }
                                      },
                                    ),
                                ],
                              ],
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  const Divider(height: 0, thickness: 2,),
                  DigitCard(
                    padding: const EdgeInsets.fromLTRB(10, 0, 10, 15),
                    child: DigitElevatedButton(
                        onPressed: () {
                          FocusScope.of(context).unfocus();
                          form.markAllAsTouched();
                          if (!form.valid) return;
                          if(context.read<AuthBloc>().userModel.identifierType!.isNotEmpty && (fileName == null || fileName!.isEmpty)) {
                            DigitToast.show(context,
                              options: DigitToastOptions(
                                "Please upload ID proof",
                                true,
                                widget.theme.theme(),
                              ),
                            );
                            return;
                          }
                          if (context.read<AuthBloc>().userModel.identifierType!.isNotEmpty && fileName!.isNotEmpty) {
                            AutoRouter.of(context)
                                .push(UserTypeRoute());
                          }
                        },
                        child: Text('Next',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
                    ),
                  ),
                ],
              );
            }
        )
    );
  }

  FormGroup buildForm() => fb.group(<String, Object>{
    typeKey : FormControl<String>(
        value: context.read<AuthBloc>().userModel.identifierType,
        validators: [Validators.required]
    ),
  });
}