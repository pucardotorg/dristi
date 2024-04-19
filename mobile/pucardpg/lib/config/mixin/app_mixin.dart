
import 'package:get/get.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_bloc.dart';
import 'package:pucardpg/app/bloc/registration_login_bloc/registration_login_bloc.dart';
import 'package:pucardpg/config/theme/app_themes.dart';
import 'package:pucardpg/injection_container.dart';


mixin AppMixin {

  AppTheme get theme => AppTheme.instance;

  RegistrationLoginBloc registrationLoginBloc = sl<RegistrationLoginBloc>();
  FileBloc fileBloc = sl<FileBloc>();

//
  // BirthListBloc birthListBloc = sl<BirthListBloc>();

}