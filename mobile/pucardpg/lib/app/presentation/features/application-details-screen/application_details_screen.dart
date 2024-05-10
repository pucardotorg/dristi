
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/detail_field.dart';
import 'package:pucardpg/app/presentation/widgets/display_image.dart';
import 'package:pucardpg/app/presentation/widgets/display_pdf.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:reactive_forms/reactive_forms.dart';
import 'package:syncfusion_flutter_pdfviewer/pdfviewer.dart';
import 'package:url_launcher/url_launcher_string.dart';

class ApplicationDetailsScreen extends StatefulWidget with AppMixin{

  UserModel userModel = UserModel();

  ApplicationDetailsScreen({super.key, required this.userModel});

  @override
  ApplicationDetailsScreenState createState() => ApplicationDetailsScreenState();

}

class ApplicationDetailsScreenState extends State<ApplicationDetailsScreen> {

  late String mobile;
  String firstNameKey = 'firstName';
  String middleNameKey = 'middleName';
  String lastNameKey = 'lastName';
  TextEditingController searchController = TextEditingController();

  @override
  void initState() {
    super.initState();
  }

  Future<void> _openMap(String lat, String lng) async {
    String googleURL = 'https://www.google.com/maps/search/?api=1&query=$lat,$lng';
    await canLaunchUrlString(googleURL)
      ? await launchUrlString(googleURL)
          : throw 'Could not launch $googleURL';
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
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    const SizedBox(height: 50,),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        DigitBackButton(),
                        DigitHelpButton()
                      ],
                    ),
                    Padding(
                        padding: const EdgeInsets.all(20),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text("My Application", style: widget.theme.text24W700()?.apply(),),
                            const SizedBox(height: 30,),
                            Container(
                              padding: const EdgeInsets.all(20),
                              decoration: BoxDecoration(
                                  border: Border.all(color: Colors.grey),
                                  borderRadius:  const BorderRadius.all(Radius.circular(15))
                              ),
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  DetailField(heading: 'Mobile number', value: '+91 ${widget.userModel.mobileNumber ?? ""}'),
                                  const SizedBox(height: 20,),
                                  DetailField(heading: 'ID type', value: widget.userModel.identifierType ?? ""),
                                  const SizedBox(height: 20,),
                                  if (widget.userModel.identifierType == 'AADHAR')
                                    DetailField(heading: '${widget.userModel.identifierType} number', value: widget.userModel.identifierId ?? ""),
                                  if (widget.userModel.identifierType != 'AADHAR') ...[
                                    Row(
                                      children: [
                                        Expanded(
                                          flex: 1,
                                          child: Text(
                                            'ID Proof',
                                            style: widget.theme.text16W700Rob(),
                                          ),
                                        ),
                                        const Expanded(
                                          flex: 1,
                                          child: Text(
                                            "",
                                          ),
                                        ),
                                      ],
                                    ),
                                    const SizedBox(height: 10,),
                                    if (widget.userModel.idDocumentType == 'pdf')
                                      DisplayPdf(filename: widget.userModel.idFilename!, bytes: widget.userModel.idBytes!),
                                    if (widget.userModel.idDocumentType != 'pdf')
                                      DisplayImage(filename: widget.userModel.idFilename!, bytes: widget.userModel.idBytes!),
                                  ],
                                ],
                              ),
                            ),
                            const SizedBox(height: 20,),
                            Container(
                              padding: const EdgeInsets.all(20),
                              decoration: BoxDecoration(
                                  border: Border.all(color: Colors.grey),
                                  borderRadius:  const BorderRadius.all(Radius.circular(15))
                              ),
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  DetailField(heading: 'Name', value: (widget.userModel.firstName ?? "") + (widget.userModel.lastName ?? "")),
                                  const SizedBox(height: 20,),
                                  Row(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Expanded(
                                        flex: 1,
                                        child: Text(
                                          'Location',
                                          style: widget.theme.text16W700Rob(),
                                        ),
                                      ),
                                      Expanded(
                                          flex: 1,
                                          child: GestureDetector(
                                            onTap: () {
                                              _openMap(widget.userModel.addressModel.latitude!.toString(), widget.userModel.addressModel.longitude!.toString());
                                            },
                                            child: Row(
                                              mainAxisAlignment: MainAxisAlignment.end,
                                              children: [
                                                Text(
                                                  'View On Map',
                                                  style: widget.theme.text16W400Rob()?.apply(color: const Color(0xFFF47738), decoration: TextDecoration.underline),
                                                ),
                                                const Icon(
                                                  Icons.location_on,
                                                  color: Color(0xFFF47738),
                                                )
                                              ],
                                            ),
                                          )
                                      ),
                                    ],
                                  ),
                                  const SizedBox(height: 20,),
                                  Row(
                                    children: [
                                      Expanded(
                                        flex: 1,
                                        child: Text(
                                          'Address',
                                          style: widget.theme.text16W700Rob(),
                                        ),
                                      ),
                                      Expanded(
                                        flex: 2,
                                        child: Column(
                                          crossAxisAlignment: CrossAxisAlignment.end,
                                          children: [
                                            Text(
                                              widget.userModel.addressModel.doorNo ?? "",
                                              style: widget.theme.text16W400Rob(),
                                              textAlign: TextAlign.end,
                                            ),
                                            Text(
                                              "${widget.userModel.addressModel.street ?? ""} ${widget.userModel.addressModel.city ?? ""}",
                                              style: widget.theme.text16W400Rob(),
                                              textAlign: TextAlign.end,
                                            ),
                                            Text(
                                              "${widget.userModel.addressModel.district ?? ""} ${widget.userModel.addressModel.state ?? ""} ${widget.userModel.addressModel.pincode ?? ""}",
                                              style: widget.theme.text16W400Rob(),
                                              textAlign: TextAlign.end,
                                            ),
                                          ],
                                        ),
                                      ),
                                    ],
                                  ),
                                  // DetailField(heading: 'Address', value: (widget.userModel.addressModel.doorNo ?? "") +
                                  //     (widget.userModel.addressModel.street ?? "") +
                                  //     (widget.userModel.addressModel.city ?? "") +
                                  //     (widget.userModel.addressModel.district ?? "") +
                                  //     (widget.userModel.addressModel.state ?? "") +
                                  //     (widget.userModel.addressModel.pincode ?? "")
                                  // )
                                ],
                              ),
                            ),
                            const SizedBox(height: 20,),
                            if (widget.userModel.userType == 'ADVOCATE') ...[
                              Container(
                                padding: const EdgeInsets.all(20),
                                decoration: BoxDecoration(
                                    border: Border.all(color: Colors.grey),
                                    borderRadius:  const BorderRadius.all(Radius.circular(15))
                                ),
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    DetailField(heading: 'Bar Registration number',
                                        value: (widget.userModel.barRegistrationNumber ?? "")),
                                    const SizedBox(height: 20,),
                                    Text(
                                      'BAR Council ID',
                                      style: widget.theme.text16W700Rob(),
                                    ),
                                    const SizedBox(height: 20,),
                                    if (widget.userModel.documentType == 'pdf')
                                      DisplayPdf(filename: widget.userModel.documentFilename!, bytes: widget.userModel.documentBytes!),
              
              
                                    if (widget.userModel.documentType != 'pdf')
                                      DisplayImage(filename: widget.userModel.documentFilename!, bytes: widget.userModel.documentBytes!),
                                  ],
                                ),
                              ),
                            ]
                          ],
                        )
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

                  },
                  child: Text('Continue',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
              ),
            ),
          ],
        ),
    );
  }
}
