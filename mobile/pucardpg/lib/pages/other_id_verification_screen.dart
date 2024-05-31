
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
import 'package:pucardpg/blocs/app-localization-bloc/app_localization.dart';
import 'package:pucardpg/blocs/auth-bloc/authbloc.dart';
import 'package:pucardpg/blocs/file-picker-bloc/file_picker.dart';
import 'package:pucardpg/mixin/app_mixin.dart';
import 'package:pucardpg/widget/display_image.dart';
import 'package:pucardpg/widget/display_pdf.dart';
import '../utils/i18_key_constants.dart' as i18;
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
  String typeKey = 'type';
  bool isEnable = true;
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
            context.read<AuthBloc>().userModel.idFilename = result!.files.single.name;
            context.read<AuthBloc>().userModel.idBytes = result!.files.single.bytes;
            if (pickedFile != null) {
              context.read<FileBloc>().add(FileEvent.upload(pickedFile: pickedFile!, type: 'id'));
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
                                  heading: AppLocalizations.of(context).translate(i18.idVerification.csUploadId),
                                  subHeading: AppLocalizations.of(context).translate(i18.idVerification.csUploadIdText),
                                  headingStyle: widget.theme.text24W700(),
                                  subHeadingStyle: widget.theme.text14W400Rob(),
                                ),
                                DigitReactiveDropdown(
                                  label: AppLocalizations.of(context).translate(i18.idVerification.csTypeOfId),
                                  menuItems: ['PAN', 'AADHAR', 'DRIVING LICENSE'],
                                  formControlName: typeKey,
                                  valueMapper: (value) => value.toUpperCase(),
                                  onChanged: (val) {
                                    context.read<AuthBloc>().userModel.identifierType = val;
                                    setState(() {
                                      context.read<AuthBloc>().userModel.idBytes = null;
                                      context.read<AuthBloc>().userModel.idFilename = null;
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
                                        label: AppLocalizations.of(context).translate(i18.idVerification.csUploadProof),
                                        controller: TextEditingController(text: context.read<AuthBloc>().userModel.idFilename ?? ''),
                                        onChange: (val) {
                                          // setState(() {
                                          //
                                          // });
                                        },
                                        readOnly: true,
                                        hintText: AppLocalizations.of(context).translate(i18.common.csNoFileSelected),
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
                                                initial: () {
                                                  setState(() {
                                                    isEnable = false;
                                                  });
                                                },
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
                                                  setState(() {
                                                    isEnable = true;
                                                  });
                                                  context.read<AuthBloc>().userModel.identifierId = fileStoreId;
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
                                                        AppLocalizations.of(context).translate(i18.common.csCommonChooseFile),
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
                                  Text(
                                    AppLocalizations.of(context).translate(i18.common.fileSizeExceeded),
                                    style: TextStyle(color: Colors.red),
                                  ),
                                if (extensionError) // Show text line in red if file size exceeded
                                  Text(
                                    AppLocalizations.of(context).translate(i18.common.notSupportedFileType),
                                    style: TextStyle(color: Colors.red),
                                  ),
                                if (isEnable == true && context.read<AuthBloc>().userModel.idBytes != null && context.read<AuthBloc>().userModel.idFilename != null) ...[
                                  const SizedBox(height: 20),
                                  if (context.read<AuthBloc>().userModel.idDocumentType == 'pdf')
                                    Stack(
                                      children: [
                                        DisplayPdf(
                                          filename: context.read<AuthBloc>().userModel.idFilename!,
                                          bytes: context.read<AuthBloc>().userModel.idBytes!,
                                          height: 300,
                                          width: null,
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
                                                  context.read<AuthBloc>().userModel.idBytes = null;
                                                  context.read<AuthBloc>().userModel.idFilename = null;
                                                  context.read<AuthBloc>().userModel.identifierId = null;
                                                });
                                              },
                                            ),
                                          ),
                                        ),
                                      ],
                                    ),
                                  if (context.read<AuthBloc>().userModel.idDocumentType != 'pdf')
                                    GestureDetector(
                                      child: Stack(
                                        children: [
                                          DisplayImage(
                                            filename: context.read<AuthBloc>().userModel.idFilename!,
                                            bytes: context.read<AuthBloc>().userModel.idBytes!,
                                            height: 300,
                                            width: 500,
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
                                                    context.read<AuthBloc>().userModel.idBytes = null;
                                                    context.read<AuthBloc>().userModel.idFilename = null;
                                                    context.read<AuthBloc>().userModel.identifierId = null;
                                                  });
                                                },
                                              ),
                                            ),
                                          ),
                                        ],
                                      ),
                                      onTap: () {
                                        if (context.read<AuthBloc>().userModel.idDocumentType != 'pdf') {
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
                        onPressed: isEnable
                            ? () {
                          FocusScope.of(context).unfocus();
                          form.markAllAsTouched();
                          if (!form.valid) return;
                          if(context.read<AuthBloc>().userModel.identifierType!.isNotEmpty && (context.read<AuthBloc>().userModel.idFilename == null || context.read<AuthBloc>().userModel.idFilename!.isEmpty)) {
                            DigitToast.show(context,
                              options: DigitToastOptions(
                                "Please upload ID proof",
                                true,
                                widget.theme.theme(),
                              ),
                            );
                            return;
                          }
                          if (context.read<AuthBloc>().userModel.identifierType!.isNotEmpty && context.read<AuthBloc>().userModel.idFilename!.isNotEmpty) {
                            AutoRouter.of(context)
                                .push(UserTypeRoute());
                          }
                        } : null,
                        child: Text(AppLocalizations.of(context).translate(i18.common.coreCommonContinue),
                          style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
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