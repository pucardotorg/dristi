// import 'package:digit_components/digit_components.dart';
// import 'package:digit_components/widgets/atoms/digit_toaster.dart';
// import 'package:digit_components/widgets/digit_card.dart';
// import 'package:flutter/material.dart';
// import 'package:flutter/services.dart';
// import 'package:geolocator/geolocator.dart';
// import 'package:permission_handler/permission_handler.dart';
// import 'package:pucardpg/app/presentation/widgets/back_button.dart';
// import 'package:pucardpg/app/presentation/widgets/help_button.dart';
// import 'package:pucardpg/config/mixin/app_mixin.dart';
// import 'package:pucardpg/core/constant/constants.dart';
// import 'package:reactive_forms/reactive_forms.dart';
//
// class HomeScreen extends StatefulWidget with AppMixin{
//
//   HomeScreen({super.key});
//
//   @override
//   HomeScreenState createState() => HomeScreenState();
//
// }
//
// class HomeScreenState extends State<HomeScreen> {
//
//   @override
//   void initState() {
//     super.initState();
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//         appBar: AppBar(
//           title: const Text(""),
//           centerTitle: true,
//           leading: IconButton(
//             onPressed: () {},
//             icon: const Icon(Icons.menu),
//           ),
//         ),
//         body: Column(
//           mainAxisAlignment: MainAxisAlignment.center,
//           children: [
//             DigitCard(
//               margin: const EdgeInsets.all(15),
//               padding: const EdgeInsets.fromLTRB(60,120,60,120),
//               child: Column(
//                 children: [
//                   DigitElevatedButton(
//                     onPressed: () {
//                       Navigator.pushNamed(context, '/mobileNumberScreen', arguments: register);
//                     },
//                     child: const Row(
//                       mainAxisAlignment: MainAxisAlignment.center,
//                       children: [
//                         Text(
//                           'Register',
//                           style: TextStyle(color: Colors.white),
//                         ),
//                         SizedBox(width: 10),
//                         Icon(
//                           Icons.app_registration,
//                           color: Colors.white,
//                         ),
//                       ],
//                     ),
//                   ),
//                   const SizedBox(height: 20),
//                   DigitElevatedButton(
//                     onPressed: () {
//                       Navigator.pushNamed(context, '/mobileNumberScreen', arguments: login);
//                     },
//                     child: const Row(
//                       mainAxisAlignment: MainAxisAlignment.center,
//                       children: [
//                         Text(
//                           'Login',
//                           style: TextStyle(color: Colors.white),
//                         ),
//                         SizedBox(width: 10),
//                         Icon(
//                           Icons.login,
//                           color: Colors.white,
//                         ),
//                       ],
//                     ),
//                   )
//                 ],
//               ),
//             ),
//           ],
//         )
//     );
//   }
// }