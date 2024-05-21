import 'package:auto_route/auto_route.dart';
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/blocs/auth-bloc/authbloc.dart';
import 'package:pucardpg/mixin/app_mixin.dart';
import 'package:pucardpg/model/litigant_model.dart';
import 'package:pucardpg/widget/back_button.dart';
import 'package:pucardpg/widget/detail_field.dart';
import 'package:pucardpg/widget/display_image.dart';
import 'package:pucardpg/widget/display_pdf.dart';
import 'package:pucardpg/widget/help_button.dart';
import 'package:url_launcher/url_launcher_string.dart';

@RoutePage()
class ApplicationDetailsScreen extends StatefulWidget with AppMixin{

  ApplicationDetailsScreen({super.key});

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
                                DetailField(heading: 'Mobile number', value: '+91 ${context.read<AuthBloc>().userModel.mobileNumber ?? ""}'),
                                const SizedBox(height: 20,),
                                DetailField(heading: 'ID type', value: context.read<AuthBloc>().userModel.identifierType ?? ""),
                                const SizedBox(height: 20,),
                                if (context.read<AuthBloc>().userModel.identifierType == 'AADHAR')
                                  DetailField(heading: '${context.read<AuthBloc>().userModel.identifierType} number', value: context.read<AuthBloc>().userModel.identifierId ?? ""),
                                if (context.read<AuthBloc>().userModel.identifierType != 'AADHAR') ...[
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
                                  if (context.read<AuthBloc>().userModel.idDocumentType == 'pdf')
                                    DisplayPdf(filename: context.read<AuthBloc>().userModel.idFilename!, bytes: context.read<AuthBloc>().userModel.idBytes!),
                                  if (context.read<AuthBloc>().userModel.idDocumentType != 'pdf')
                                    DisplayImage(filename: context.read<AuthBloc>().userModel.idFilename!, bytes: context.read<AuthBloc>().userModel.idBytes!),
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
                                DetailField(heading: 'Name', value: (context.read<AuthBloc>().userModel.firstName ?? "") + (context.read<AuthBloc>().userModel.lastName ?? "")),
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
                                            _openMap(context.read<AuthBloc>().userModel.addressModel.latitude!.toString(), context.read<AuthBloc>().userModel.addressModel.longitude!.toString());
                                          },
                                          child: Row(
                                            mainAxisAlignment: MainAxisAlignment.end,
                                            children: [
                                              Text('View On Map',
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
                                            context.read<AuthBloc>().userModel.addressModel.doorNo ?? "",
                                            style: widget.theme.text16W400Rob(),
                                            textAlign: TextAlign.end,
                                          ),
                                          Text(
                                            "${context.read<AuthBloc>().userModel.addressModel.street ?? ""} ${context.read<AuthBloc>().userModel.addressModel.city ?? ""}",
                                            style: widget.theme.text16W400Rob(),
                                            textAlign: TextAlign.end,
                                          ),
                                          Text(
                                            "${context.read<AuthBloc>().userModel.addressModel.district ?? ""} ${context.read<AuthBloc>().userModel.addressModel.state ?? ""} ${context.read<AuthBloc>().userModel.addressModel.pincode ?? ""}",
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
                          if (context.read<AuthBloc>().userModel.userType == 'ADVOCATE') ...[
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
                                      value: (context.read<AuthBloc>().userModel.barRegistrationNumber ?? "")),
                                  const SizedBox(height: 20,),
                                  Text(
                                    'BAR Council ID',
                                    style: widget.theme.text16W700Rob(),
                                  ),
                                  const SizedBox(height: 20,),
                                  if (context.read<AuthBloc>().userModel.documentType == 'pdf')
                                    DisplayPdf(filename: context.read<AuthBloc>().userModel.documentFilename!, bytes: context.read<AuthBloc>().userModel.documentBytes!),


                                  if (context.read<AuthBloc>().userModel.documentType != 'pdf')
                                    DisplayImage(filename: context.read<AuthBloc>().userModel.documentFilename!, bytes: context.read<AuthBloc>().userModel.documentBytes!),
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