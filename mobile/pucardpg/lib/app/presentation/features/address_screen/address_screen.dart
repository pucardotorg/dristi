import 'dart:convert';
import 'dart:io';

import 'package:country_state_city/utils/city_utils.dart';
import 'package:country_state_city/utils/state_utils.dart';
import 'package:digit_components/digit_components.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/io_client.dart';
import 'package:pucardpg/app/models/state_model.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:google_places_flutter/google_places_flutter.dart';
import 'package:google_places_flutter/model/prediction.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:reactive_forms/reactive_forms.dart';

class AddressScreen extends StatefulWidget with AppMixin {
  final String mobile;

  const AddressScreen({super.key, required this.mobile});

  @override
  AddressScreenState createState() => AddressScreenState();
}

// const kGoogleApiKey = 'AIzaSyAdAE6QFX0Oq_a6o_KQlD9GLC26PE8d1rc';
// final homeScaffoldKey = GlobalKey<ScaffoldState>();

class AddressScreenState extends State<AddressScreen> {
  String pinLocationKey = 'pinLocation';
  String pinCodeKey = 'pinCode';
  String stateKey = 'state';
  String districtKey = 'district';
  String cityKey = 'city';
  String localityKey = 'locality';
  String doorNoKey = 'doorNo';
  List<String> state = [];
  List<String> district = [];
  List<String> city = [];

  List<StatesData> statesData = [];
  String? selectedDistrict;
  String selectedCountryISOCode = 'IN';
  String selectedStateISOCode = '';

  Future<void> fetchStates(String countryISOCode) async {
    final response = await getStatesOfCountry(countryISOCode);
    setState(() {
      statesData =
          response.map((state) => StatesData.fromState(state)).toList();
      state = response.map((state) => state.name).toList();
    });
  }

  Future<void> fetchCities(String countryISOCode, String stateISOCode) async {
    final cities = await getStateCities(countryISOCode, stateISOCode);
    setState(() {
      city = cities.map((city) => city.name).toList();
    });
  }

  // static const CameraPosition initialCameraPosition =
  // CameraPosition(target: LatLng(37.42796, -122.08574), zoom: 14.0);
  //
  // Set<Marker> markersList = {};

  // late GoogleMapController googleMapController;

  // final Mode _mode = Mode.overlay;
  TextEditingController controller = TextEditingController();

  @override
  void initState() {
    fetchStates(selectedCountryISOCode);
    super.initState();
  }

  Future<void> _fetchDistrict(String pincode) async {
    final url = 'https://api.postalpincode.in/pincode/$pincode';
    final http = IOClient(HttpClient());
    final response = await http.get(Uri.parse(url));

    if (response.statusCode == 200) {
      final body = json.decode(response.body);
      final postOffice = body[0]['PostOffice'] as List<dynamic>;
      if (postOffice.isNotEmpty) {
        final firstPostOffice = postOffice[0];
        setState(() {
          selectedDistrict = firstPostOffice['District'];
        });
      }
    } else {
      // Handle error
      print('Failed to fetch district');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(),
        resizeToAvoidBottomInset: true,
        body: Column(
          children: [
            Expanded(
                child: SingleChildScrollView(
              child: ReactiveFormBuilder(
                form: () => buildForm(),
                builder: (context, form, child) {
                  return Column(
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      const SizedBox(
                        height: 10,
                      ),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          DigitBackButton(
                            onPressed: () {
                              Navigator.of(context).pop();
                            },
                          ),
                          DigitHelpButton()
                        ],
                      ),
                      DigitCard(
                        padding: const EdgeInsets.all(20.0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              "Registration",
                              style: widget.theme
                                  .text20W400Rob()
                                  ?.apply(fontStyle: FontStyle.italic),
                            ),
                            const SizedBox(
                              height: 20,
                            ),
                            Text(
                              "Enter Your Address",
                              style: widget.theme.text32W700RobCon()?.apply(),
                            ),
                            const SizedBox(
                              height: 15,
                            ),
                            Column(children: [
                              // DigitSearchDropdown(
                              //   label: 'Pin Location',
                              //   formControlName: pinLocationKey,
                              //   initialValue: '',
                              //   initialValueText: '',
                              //   suggestionsCallback:
                              //       (Iterable<String> items, String pattern) {
                              //     return items
                              //         .where((item) => item.contains(pattern));
                              //   },
                              //   menuItems:
                              //       state.map((e) => e.toString()).toList(),
                              //   valueMapper: (value) => '',
                              // ),
                              // GooglePlaceAutoCompleteTextField(
                              //   textEditingController: controller,
                              //   googleAPIKey: kGoogleApiKey,
                              //   inputDecoration: InputDecoration(),
                              //   debounceTime: 800,
                              //   countries: ["in", "fr"],
                              //   isLatLngRequired: true,
                              //   getPlaceDetailWithLatLng:
                              //       (Prediction prediction) {
                              //     print(
                              //         "placeDetails" + prediction.lng.toString());
                              //   },
                              //   itemClick: (Prediction prediction) {
                              //     controller.text = prediction.description!;
                              //     controller.selection =
                              //         TextSelection.fromPosition(TextPosition(
                              //             offset:
                              //                 prediction.description!.length));
                              //     displayPrediction(prediction);
                              //   },
                              //   itemBuilder:
                              //       (context, index, Prediction prediction) {
                              //     return Container(
                              //       padding: const EdgeInsets.all(10),
                              //       child: Row(
                              //         children: [
                              //           Icon(Icons.location_on),
                              //           SizedBox(
                              //             width: 7,
                              //           ),
                              //           Expanded(
                              //               child: Text(
                              //                   "${prediction.description ?? ""}"))
                              //         ],
                              //       ),
                              //     );
                              //   },
                              //   seperatedBuilder: Divider(),
                              //   isCrossBtnShown: true,
                              //   containerHorizontalPadding: 10,
                              // ),
                              // Container(
                              //   height: 300,
                              //   child: GoogleMap(
                              //     initialCameraPosition: initialCameraPosition,
                              //     markers: markersList,
                              //     mapType: MapType.normal,
                              //     onMapCreated: (GoogleMapController controller) {
                              //       googleMapController = controller;
                              //     },
                              //   ),
                              // ),
                              DigitTextFormField(
                                formControlName: pinCodeKey,
                                label: 'Pincode',
                                keyboardType: TextInputType.number,
                                isRequired: true,
                                onChanged: (value) {
                                  if (value.value.length == 6) {
                                    _fetchDistrict(value.value);
                                  }
                                },
                                validationMessages: {
                                  'required': (_) => 'Pincode is required',
                                },
                                inputFormatters: [
                                  FilteringTextInputFormatter.allow(
                                      RegExp("[0-9]")),
                                ],
                              ),
                              const SizedBox(
                                height: 12,
                              ),
                              DigitReactiveDropdown<String>(
                                label: 'State',
                                menuItems: state,
                                padding: const EdgeInsets.all(0),
                                isRequired: true,
                                formControlName: stateKey,
                                valueMapper: (value) => value.toUpperCase(),
                                onChanged: (value) {
                                  selectedStateISOCode = statesData
                                      .firstWhere(
                                          (state) => state.name == value)
                                      .isoCode;
                                  // Call fetchCities with selected state and country ISO codes
                                  fetchCities(selectedCountryISOCode,
                                      selectedStateISOCode);
                                },
                                validationMessages: {
                                  'required': (_) => 'State is required',
                                },
                              ),
                              const SizedBox(
                                height: 12,
                              ),
                              DigitReactiveDropdown<String>(
                                label: 'District',
                                menuItems: selectedDistrict != null
                                    ? [selectedDistrict!]
                                    : [],
                                isRequired: true,
                                formControlName: districtKey,
                                valueMapper: (value) => value.toUpperCase(),
                                onChanged: (value) {},
                                validationMessages: {
                                  'required': (_) => 'District is required',
                                },
                              ),
                              const SizedBox(
                                height: 12,
                              ),
                              DigitReactiveDropdown<String>(
                                label: 'City / Town',
                                menuItems: city,
                                isRequired: true,
                                formControlName: cityKey,
                                valueMapper: (value) => value.toUpperCase(),
                                onChanged: (value) {},
                                validationMessages: {
                                  'required': (_) => 'City / Town is required',
                                },
                              ),
                              const SizedBox(
                                height: 12,
                              ),
                              DigitTextFormField(
                                  formControlName: localityKey,
                                  label: 'Locality / Street name / Area',
                                  isRequired: true,
                                  validationMessages: {
                                    'required': (_) =>
                                        'Locality / Street name / Area is required',
                                    'maxLength': (_) =>
                                        'Max length should be 100'
                                  },
                                  inputFormatters: [
                                    FilteringTextInputFormatter.allow(
                                        RegExp("[a-zA-Z0-9 .,\\/\\-_@#\\']"))
                                  ]),
                              const SizedBox(
                                height: 12,
                              ),
                              DigitTextFormField(
                                  formControlName: doorNoKey,
                                  padding: const EdgeInsets.all(0),
                                  label: 'Door number',
                                  isRequired: true,
                                  validationMessages: {
                                    'required': (_) =>
                                        'Door number is required',
                                    'maxLength': (_) =>
                                        'Max length should be 50'
                                  },
                                  inputFormatters: [
                                    FilteringTextInputFormatter.allow(
                                        RegExp("[a-zA-Z0-9 .,\\/\\-_@#\\']"))
                                  ]),
                            ]),
                          ],
                        ),
                      ),
                    ],
                  );
                },
              ),
            )),
            DigitElevatedButton(
                onPressed: () {
                  Navigator.pushNamed(context, '/UserTypeScreen',
                      arguments: widget.mobile);
                },
                child: Text(
                  'Next',
                  style: widget.theme.text20W700()?.apply(
                        color: Colors.white,
                      ),
                )),
          ],
        ));
  }

  FormGroup buildForm() => fb.group(<String, Object>{
        pinLocationKey:
            FormControl<String>(value: '', validators: [Validators.required]),
        pinCodeKey:
            FormControl<String>(value: '', validators: [Validators.required]),
        stateKey:
            FormControl<String>(value: '', validators: [Validators.required]),
        districtKey:
            FormControl<String>(value: '', validators: [Validators.required]),
        cityKey:
            FormControl<String>(value: '', validators: [Validators.required]),
        localityKey:
            FormControl<String>(value: '', validators: [Validators.required]),
        doorNoKey: FormControl<String>(
            value: '', validators: [Validators.maxLength(8)]),
      });

  // Future<void> displayPrediction(Prediction p) async {
  //
  //   markersList.clear();
  //   markersList.add(Marker(markerId: const MarkerId("0"),position: LatLng(p.lat as double, p.lng as double),infoWindow: InfoWindow(title: detail.result.name)));
  //
  //   setState(() {});
  //
  //   googleMapController.animateCamera(CameraUpdate.newLatLngZoom(LatLng(lat, lng), 14.0));
  // }
}
