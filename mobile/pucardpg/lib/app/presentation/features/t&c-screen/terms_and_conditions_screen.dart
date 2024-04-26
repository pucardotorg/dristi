
import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/atoms/digit_toaster.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_bloc.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_event.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_state.dart';
import 'package:pucardpg/app/domain/entities/litigant_model.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';

class TermsAndConditionsScreen extends StatefulWidget with AppMixin{

  UserModel userModel = UserModel();

  TermsAndConditionsScreen({super.key, required this.userModel});

  @override
  TermsAndConditionsScreenState createState() => TermsAndConditionsScreenState();

}

class TermsAndConditionsScreenState extends State<TermsAndConditionsScreen> {

  bool firstChecked = false;

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
        body: Column(
          children: [
            Expanded(
              child: SingleChildScrollView(
                child: Column(
                  children: [
                    const SizedBox(height: 10,),
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
                          Text("Terms and Conditions", style: widget.theme.text32W700RobCon()?.apply(),),
                          const SizedBox(height: 20,),
                          DigitCheckboxTile(
                            value: firstChecked,
                            label: "I agree to Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                            onChanged: (val){
                              setState(() {
                                firstChecked = !firstChecked;
                              });
                            },
                          ),
                          // const SizedBox(height: 20,),
                        ],
                      ),
                    ),
                    // Expanded(child: Container(),),
                  ],
                ),
              ),
            ),
            BlocListener<RegistrationLoginBloc, RegistrationLoginState>(
              bloc: widget.registrationLoginBloc,
              listener: (context, state) {

                switch (state.runtimeType) {
                  case RequestFailedState:
                    widget.theme.showDigitDialog(true, (state as RequestFailedState).errorMsg, context);
                    break;
                  case LitigantSubmissionSuccessState:
                    Navigator.pushNamed(context, '/SuccessScreen', arguments: widget.userModel);
                    break;
                  case AdvocateSubmissionSuccessState:
                    Navigator.pushNamed(context, '/SuccessScreen', arguments: widget.userModel);
                    break;
                  case AdvocateClerkSubmissionSuccessState:
                    Navigator.pushNamed(context, '/SuccessScreen', arguments: widget.userModel);
                    break;
                  default:
                    break;
                }
              },
              child: DigitElevatedButton(
                  onPressed: () {
                    if (firstChecked == false) {
                      widget.theme.showDigitDialog(true, "Select all Terms and Conditions", context);
                      return;
                    }
                    if (widget.userModel.userType == 'ADVOCATE') {
                      widget.registrationLoginBloc.add(SubmitAdvocateProfileEvent(userModel: widget.userModel));
                    }
                    if (widget.userModel.userType == 'ADVOCATE_CLERK') {
                      widget.registrationLoginBloc.add(SubmitAdvocateClerkProfileEvent(userModel: widget.userModel));
                    }
                    if (widget.userModel.userType == 'LITIGANT') {
                      widget.registrationLoginBloc.add(SubmitLitigantProfileEvent(userModel: widget.userModel));
                    }
                  },
                  child: Text('Submit',  style: widget.theme.text20W700()?.apply(color: Colors.white, ),)
              ),
            )
          ],
        )
    );

  }

}
