
import 'package:get_it/get_it.dart';
import 'package:pucardpg/app/bloc/otp_bloc/otp_bloc.dart';
import 'package:pucardpg/app/data/repository/registration_login_repository_impl.dart';

import 'app/data/data_sources/api_service.dart';

import 'package:dio/dio.dart';

import 'app/domain/repository/registration_login_repository.dart';
import 'app/domain/usecase/login_usecase.dart';

final sl = GetIt.instance;

Future<void> initializeDependencies() async {

  sl.registerSingleton<Dio>(Dio());

  sl.registerSingleton<ApiService>(ApiService(sl()));

  sl.registerSingleton<RegistrationLoginRepository>(
      RegistrationLoginRepositoryImpl(sl())
  );

  sl.registerSingleton<LoginUseCase>(
      LoginUseCase(sl())
  );

  sl.registerSingleton<OtpBloc>(
      OtpBloc(sl())
  );

}