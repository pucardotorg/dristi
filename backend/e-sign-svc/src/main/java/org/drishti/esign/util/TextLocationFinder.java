package org.drishti.esign.util;


import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextLocationFinder implements RenderListener {

    private String keyword;

    public TextLocationFinder(String keyword) {
        this.keyword = keyword;
    }

    @Setter
    @Getter
    private float keywordX, keywordY;

    @Getter
    private Boolean keywordFound = false;


    @Override
    public void renderText(TextRenderInfo renderInfo) {
        String text = renderInfo.getText();
        if (text != null && text.contains(keyword)) {
            // Coordinates are in user space units
            keywordX = renderInfo.getBaseline().getStartPoint().get(0);
            keywordY = renderInfo.getBaseline().getStartPoint().get(1);
            keywordFound = true;
            log.debug("Keyword '{}' found at coordinates ({}, {})", keyword, keywordX, keywordY);
        }
    }


    @Override
    public void renderImage(ImageRenderInfo renderInfo) {
        // Not used in current implementation
    }

    @Override
    public void beginTextBlock() {
        // Not used in current implementation
    }

    @Override
    public void endTextBlock() {
        // Not used in current implementation
    }
}
