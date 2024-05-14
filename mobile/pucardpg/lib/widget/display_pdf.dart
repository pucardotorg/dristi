
import 'dart:typed_data';

import 'package:digit_components/theme/colors.dart';
import 'package:flutter/material.dart';
import 'package:pucardpg/core/save_file_mobile.dart';
import 'package:pucardpg/mixin/app_mixin.dart';
import 'package:syncfusion_flutter_pdfviewer/pdfviewer.dart';

class DisplayPdf extends StatelessWidget with AppMixin{

  String filename;
  Uint8List bytes;
  DisplayPdf({super.key,
    required this.filename,
    required this.bytes
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          height: 200,
          width: 200,
          alignment: Alignment.center,
          decoration: BoxDecoration(
              border: Border.all(color: Colors.grey),
              borderRadius:  const BorderRadius.all(Radius.circular(15))
          ),
          child: ClipRRect(
            borderRadius: BorderRadius.circular(20), // Image border
            child: SizedBox(
                child: SfPdfViewer.memory(
                  bytes,
                  onTap: (pdfDetails) {
                    saveAndLaunchFile(bytes, filename);
                  },
                )
            ),
          ),
        ),
        Padding(
          padding: const EdgeInsets.all(10),
          child: Text(
            filename,
          ),
        )
      ],
    );
  }

}