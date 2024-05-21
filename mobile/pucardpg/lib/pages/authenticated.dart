import 'package:auto_route/auto_route.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pucardpg/blocs/auth-bloc/authbloc.dart';
import 'package:pucardpg/pages/sideBar.dart';

import '../blocs/user-bloc/userbloc.dart';

@RoutePage()
class AuthenticatedScreenWrapper extends StatelessWidget {
  const AuthenticatedScreenWrapper({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: const AutoRouter(),
        appBar: AppBar(),
        drawer: const Drawer(
          child: SideBar(),
        )
    );
  }
}
