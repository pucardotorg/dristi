
import 'package:get/get.dart';
import 'package:pucardpg/app/bloc/otp_bloc/otp_bloc.dart';
import 'package:pucardpg/config/theme/app_themes.dart';
import 'package:pucardpg/injection_container.dart';


mixin AppMixin {

  AppTheme get theme => AppTheme.instance;

  OtpBloc otpBloc = sl<OtpBloc>();
  //
  // BirthListBloc birthListBloc = sl<BirthListBloc>();

}