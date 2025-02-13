package org.drishti.esign.config;

import com.itextpdf.text.pdf.PdfSignatureAppearance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PdfSignatureAppearanceCache {
    private static final Map<String, PdfSignatureAppearance> cache = new ConcurrentHashMap<>();

    public static void put(String key, PdfSignatureAppearance value) {
        cache.put(key, value);
    }

    public static PdfSignatureAppearance get(String key) {
        return cache.get(key);
    }

    public static void remove(String key) {
        cache.remove(key);
    }

    public static void clear() {
        cache.clear();
    }
}