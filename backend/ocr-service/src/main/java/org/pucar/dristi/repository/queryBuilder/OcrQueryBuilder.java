package org.pucar.dristi.repository.queryBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OcrQueryBuilder {
    @Getter
    @Setter
    private String ocrSearchByFilingNumberQuery = "SELECT * FROM dristi_ocr WHERE filingnumber = ?";
}
