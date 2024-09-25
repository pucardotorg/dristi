package org.pucar.dristi.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OcrPersist {
    private Ocr ocr;
}
