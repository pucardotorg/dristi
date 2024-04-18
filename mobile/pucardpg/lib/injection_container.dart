
import 'package:get_it/get_it.dart';
import 'package:pucardpg/app/bloc/file_picker_bloc/file_bloc.dart';
import 'package:pucardpg/app/data/repository/registration_login_repository_impl.dart';
import 'package:pucardpg/app/domain/usecase/file_picker_usecase.dart';

import 'app/bloc/registration_login_bloc/registration_login_bloc.dart';
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

  sl.registerSingleton<RegistrationLoginBloc>(
      RegistrationLoginBloc(sl())
  );

  sl.registerSingleton<FilePickerUseCase>(
      FilePickerUseCase(sl())
  );

  sl.registerSingleton<FileBloc>(
      FileBloc(sl())
  );

}