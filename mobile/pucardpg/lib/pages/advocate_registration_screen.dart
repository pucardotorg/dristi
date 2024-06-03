import 'dart:io';
import 'dart:typed_data';
import 'dart:ui';

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
import 'package:pucardpg/widget/digit_elevated_card.dart';
import 'package:pucardpg/widget/digit_elevated_revised_button.dart';
import 'package:pucardpg/widget/display_image.dart';
import 'package:pucardpg/widget/display_pdf.dart';
import '../utils/i18_key_constants.dart' as i18;
import 'package:pucardpg/routes/routes.dart';
import 'package:pucardpg/widget/back_button.dart';
import 'package:pucardpg/widget/help_button.dart';
import 'package:pucardpg/widget/page_heading.dart';
import 'package:syncfusion_flutter_pdfviewer/pdfviewer.dart';

@RoutePage()
class AdvocateRegistrationScreen extends StatefulWidget with AppMixin{

  AdvocateRegistrationScreen({super.key});

  @override
  AdvocateRegistrationScreenState createState() => AdvocateRegistrationScreenState();

}

class AdvocateRegistrationScreenState extends State<AdvocateRegistrationScreen> {

  bool fileSizeExceeded = false;
  bool extensionError = false;
  bool isEnable = true;
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
            context.read<AuthBloc>().userModel.documentType = result!.files.single.extension;
            pickedFile = result!.files.single;
            context.read<AuthBloc>().userModel.documentFilename = result!.files.single.name;
            context.read<AuthBloc>().userModel.documentBytes = result!.files.single.bytes;
            if (pickedFile != null) {
              context.read<FileBloc>().add(FileEvent.upload(pickedFile: pickedFile!, type: 'bar'));
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
                      children: [
                        DigitBackButton(),
                        DigitHelpButton()],
                    ),
                    Padding(
                      padding: const EdgeInsets.all(20),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          PageHeading(
                            heading: AppLocalizations.of(context).translate(i18.advocateVerification.coreAdvocateVerification),
                            subHeading: AppLocalizations.of(context).translate(i18.advocateVerification.coreAdvocateVerificationText),
                            headingStyle: widget.theme.text24W700(),
                            subHeadingStyle: widget.theme.text14W400Rob(),
                          ),
                          DigitTextField(
                            label: AppLocalizations.of(context).translate(i18.advocateVerification.barRegistrationNumber),
                            controller: TextEditingController(text: context.read<AuthBloc>().userModel.barRegistrationNumber ?? ''),
                            onChange: (val) {
                              context.read<AuthBloc>().userModel.barRegistrationNumber = val;
                            },
                            inputFormatter: [
                              FilteringTextInputFormatter.allow(
                                  RegExp("[a-zA-Z0-9\\/\\']"))
                            ],
                            maxLength: 20,
                            textCapitalization: TextCapitalization.characters,
                          ),
                          const SizedBox(height: 20,),
                          Row(
                            crossAxisAlignment: CrossAxisAlignment.end,
                            children: [
                              Expanded(
                                child: DigitTextField(
                                  label: AppLocalizations.of(context).translate(i18.advocateVerification.barCouncilId),
                                  controller: TextEditingController(text: context.read<AuthBloc>().userModel.documentFilename ?? ''),
                                  readOnly: true,
                                  hintText: AppLocalizations.of(context).translate(i18.common.csNoFileSelected),
                                  onChange: (val) { },
                                ),
                              ),
                              const SizedBox(width: 10,),
                              BlocListener<FileBloc, FileState>(
                                bloc: context.read<FileBloc>(),
                                listener: (context, state) {
                                  state.maybeWhen(
                                    orElse: (){},
                                    initial: () {
                                      setState(() {
                                        isEnable = false;
                                      });
                                    },
                                    barFailed: (error){
                                      DigitToast.show(context,
                                        options: DigitToastOptions(
                                          error,
                                          true,
                                          widget.theme.theme(),
                                        ),
                                      );
                                    },
                                    uploadBarSuccess: (fileStoreId) {
                                      setState(() {
                                        isEnable = true;
                                      });
                                      context.read<AuthBloc>().userModel.fileStore = fileStoreId;
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
                                        pickFile();
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
                          if (isEnable == true && context.read<AuthBloc>().userModel.documentBytes != null && context.read<AuthBloc>().userModel.documentFilename != null) ...[
                            const SizedBox(height: 20),
                            if (context.read<AuthBloc>().userModel.documentType == 'pdf')
                              Stack(
                                children: [
                                  DisplayPdf(
                                    filename: context.read<AuthBloc>().userModel.documentFilename!,
                                    bytes: context.read<AuthBloc>().userModel.documentBytes!,
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
                                            setState(() {
                                              context.read<AuthBloc>().userModel.documentBytes = null;
                                              context.read<AuthBloc>().userModel.documentFilename = null;
                                              context.read<AuthBloc>().userModel.fileStore = null;
                                            });
                                          });
                                        },
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            if (context.read<AuthBloc>().userModel.documentType != 'pdf')
                              GestureDetector(
                                child: Stack(
                                  children: [
                                    DisplayImage(
                                        filename: context.read<AuthBloc>().userModel.documentFilename!,
                                        bytes: context.read<AuthBloc>().userModel.documentBytes!,
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
                                              context.read<AuthBloc>().userModel.documentBytes = null;
                                              context.read<AuthBloc>().userModel.documentFilename = null;
                                              context.read<AuthBloc>().userModel.fileStore = null;
                                            });
                                          },
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                                onTap: () {
                                  if (context.read<AuthBloc>().userModel.documentType != 'pdf') {
                                    OpenFilex.open(fileToDisplay!.path);
                                  }
                                },
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
            DigitElevatedCard(
              margin: EdgeInsets.zero,
              child: DigitElevatedRevisedButton(
                  onPressed: isEnable
                      ? () {
                    FocusScope.of(context).unfocus();
                    if (context.read<AuthBloc>().userModel.userType == 'ADVOCATE') {
                      if((context.read<AuthBloc>().userModel.barRegistrationNumber == null || context.read<AuthBloc>().userModel.barRegistrationNumber!.isEmpty)
                          && (context.read<AuthBloc>().userModel.documentFilename == null || context.read<AuthBloc>().userModel.documentFilename!.isEmpty)) {
                        DigitToast.show(context,
                          options: DigitToastOptions(
                            "Please fill the details",
                            true,
                            widget.theme.theme(),
                          ),
                        );
                        return;
                      }
                      if((context.read<AuthBloc>().userModel.barRegistrationNumber == null || context.read<AuthBloc>().userModel.barRegistrationNumber!.isEmpty)
                          && (context.read<AuthBloc>().userModel.documentFilename != null || context.read<AuthBloc>().userModel.documentFilename!.isNotEmpty)) {
                        DigitToast.show(context,
                          options: DigitToastOptions(
                            "Please fill the BAR registration number",
                            true,
                            widget.theme.theme(),
                          ),
                        );
                        return;
                      }
                      if((context.read<AuthBloc>().userModel.barRegistrationNumber != null || context.read<AuthBloc>().userModel.barRegistrationNumber!.isNotEmpty)
                          && (context.read<AuthBloc>().userModel.documentFilename == null || context.read<AuthBloc>().userModel.documentFilename!.isEmpty)) {
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
                    AutoRouter.of(context)
                        .push(TermsAndConditionsRoute());
                    } : null,
                  child: Text(AppLocalizations.of(context).translate(i18.common.coreCommonContinue))
              ),
            ),
          ],
        ),
    );
  }
}