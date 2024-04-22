
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/detail_field.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:reactive_forms/reactive_forms.dart';

class ApplicationDetailsScreen extends StatefulWidget with AppMixin{

  // UserModel userModel = UserModel();

  ApplicationDetailsScreen({super.key,
    // required this.userModel
  });

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
        body: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              const SizedBox(height: 10,),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  DigitBackButton(),
                  DigitHelpButton()
                ],
              ),
              Padding(
                  padding: const EdgeInsets.all(10),
                  child: Text("My Application", style: widget.theme.text32W700RobCon()?.apply(),)),

              DigitCard(
                padding: const EdgeInsets.all(20),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    DetailField(heading: 'Mobile number', value: '+91 8219862225'),
                    const SizedBox(height: 20,),
                    DetailField(heading: 'ID type', value: '+91 8219862225'),
                    const SizedBox(height: 20,),
                    DetailField(heading: 'Aadhar number', value: '+91 8219862225')
                  ],
                ),
              ),
              DigitCard(
                padding: const EdgeInsets.all(20),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Personal Details',
                      style: widget.theme.text24W700(),
                    ),
                    const SizedBox(height: 20,),
                    DetailField(heading: 'Name', value: '+91 8219862225'),
                    const SizedBox(height: 20,),
                    DetailField(heading: 'Location', value: '+91 8219862225'),
                    const SizedBox(height: 20,),
                    DetailField(heading: 'Address', value: '+91 8219862225')
                  ],
                ),
              ),
              DigitCard(
                padding: const EdgeInsets.all(20),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'BAR Details',
                      style: widget.theme.text24W700(),
                    ),
                    const SizedBox(height: 20,),
                    DetailField(heading: 'Bar Registration number', value: '+91 8219862225'),
                    const SizedBox(height: 20,),
                    Text(
                      'BAR Council ID',
                      style: widget.theme.text16W700Rob(),
                    ),
                  ],
                ),
              ),
              // Expanded(child: Container(),),
            ],
          ),
        ),
    );
  }
}
