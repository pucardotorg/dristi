

import 'package:digit_components/digit_components.dart';
import 'package:digit_components/widgets/digit_card.dart';
import 'package:flutter/material.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';

class MobileNumberScreen extends StatefulWidget with AppMixin{

  const MobileNumberScreen({super.key});

  @override
  MobileNumberScreenState createState() => MobileNumberScreenState();

}

class MobileNumberScreenState extends State<MobileNumberScreen> {

  TextEditingController searchController = TextEditingController();

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text("Birth registration list"),
        ),
        body: Column(
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                TextButton(
                    onPressed: (){

                    },
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: <Widget>[
                        const Icon(Icons.arrow_left, color: Colors.black,),
                        Text('Back', style: widget.theme.text16W400Rob(),),
                      ],
                    ),
                ),
                TextButton(
                  onPressed: (){

                  },
                  child: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: <Widget>[
                      Text('Help', style: widget.theme.text16W400Rob(),),
                      const SizedBox(width: 5,),
                      Container(
                        width: 20,
                        height: 20,
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          border: Border.all(
                            color: const DigitColors().burningOrange,
                            width: 2,
                          ),
                        ),
                        child: const Icon(Icons.question_mark, size: 15,),
                      ),
                    ],
                  ),
                ),
              ],
            ),
            DigitCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text("Provide Your Mobile Number", style: widget.theme.text32W700RobCon(),),
                    const SizedBox(height: 20,),
                    Text("Your mobile number will be used to login to the system going forward. We will send you a one-time password on this mobile number", style: widget.theme.text16W400Rob(),),
                    const SizedBox(height: 20,),
                    Text("Enter Mobile number *", style: widget.theme.text16W400Rob(),),
                  ],
                ),
            ),
          ],
        )
    );

  }

}

// class BirthRegistrationListState extends State<BirthRegistrationList> {
//
//   TextEditingController searchController = TextEditingController();
//
//   @override
//   void initState() {
//     widget.birthListBloc.add(BirthListInitialEvent());
//     super.initState();
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//         appBar: AppBar(
//           title: const Text("Birth registration list"),
//         ),
//         body: Column(
//           children: [
//             DigitSearchBar(
//                 controller: searchController,
//                 hintText: "Tenant ID",
//                 textCapitalization: TextCapitalization.words,
//                 onChanged: (value) => { widget.birthListBloc.add(BirthListSearchEvent(searchString: value)) }
//             ),
//             BlocConsumer<BirthListBloc, BirthListState>(
//               bloc: widget.birthListBloc,
//               listenWhen: (previous, current) => current is BirthListActionState,
//               buildWhen: (previous, current) => current is! BirthListActionState,
//               listener: (context, state) { },
//               builder: (context, state) {
//                 switch (state.runtimeType) {
//                   case BirthListLoadingState:
//                     return const Expanded(child: Center(child: CircularProgressIndicator()));
//                   case BirthListLoadedSuccessState:
//                     return HomeScreen(birthDataList: (state as BirthListLoadedSuccessState).birthData);
//                   case BirthListErrorState:
//                     return const Expanded(child: Center(child: Text("Something went wrong")));
//                   default:
//                     return const Expanded(child: SizedBox());
//                 }
//               },
//             ),
//             DigitElevatedButton(
//                 onPressed: () {
//                   Navigator.pushNamed(context, '/BirthRegistration');
//                 },
//                 child: const Text('Birth Registration')
//             ),
//           ],
//         )
//     );
//
//   }
//
// }