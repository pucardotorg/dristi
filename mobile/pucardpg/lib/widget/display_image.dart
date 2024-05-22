
import 'dart:typed_data';

import 'package:digit_components/theme/colors.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:pucardpg/core/save_file_mobile.dart';
import 'package:pucardpg/mixin/app_mixin.dart';

class DisplayImage extends StatelessWidget with AppMixin{

  String filename;
  Uint8List bytes;
  double? height;
  double? width;
  DisplayImage({super.key,
    required this.filename,
    required this.bytes,
    required this.height,
    required this.width
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        GestureDetector(
          child: Container(
            height: height,
            width: width,
            decoration: BoxDecoration(
                border: Border.all(color: Colors.grey),
                borderRadius:  const BorderRadius.all(Radius.circular(20))
            ),
            child: ClipRRect(
              borderRadius: BorderRadius.circular(20), // Image border
              child: SizedBox.fromSize(
                size: Size.fromRadius(16), // Image radius
                child: Image.memory(
                  bytes,
                  filterQuality: FilterQuality.high,
                  fit: BoxFit.cover,
                ),
              ),
            ),
          ),
          onTap: () {
            saveAndLaunchFile(bytes, filename);
          },
        ),
      ],
    );
  }

}