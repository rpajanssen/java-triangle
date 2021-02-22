package com.example.utils;

import javax.servlet.http.Part;
import java.io.*;

import static com.example.utils.FileUtils.toByteArray;

public final class HttpPartReader {
    public static String extractParamValue(Part part, String encoding) throws IOException {
        String fileName = part.getSubmittedFileName();
        if (isBinary(part)) {
            return new String(toByteArray(readBinaryFilePart(part, fileName)));
        } else {
            return readStringPart(part, encoding);
        }
    }

    private static boolean isBinary(Part part) {
        return part.getSubmittedFileName() != null;
    }

    private static File readBinaryFilePart(Part part, String fileName) throws IOException {
        File tempFile = getTemporaryFile(fileName);

        try (BufferedInputStream input = new BufferedInputStream(part.getInputStream(), 8192);
             BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(tempFile), 8192);) {

            byte[] buffer = new byte[8192];
            for (int length = 0; ((length = input.read(buffer)) > 0); ) {
                output.write(buffer, 0, length);
            }
        }

        return tempFile;
    }

    private static String readStringPart(Part part, String encoding) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), encoding));

        StringBuilder value = new StringBuilder();
        char[] buffer = new char[8192];
        for (int length; (length = reader.read(buffer)) > 0;) {
            value.append(buffer, 0, length);
        }

        return value.toString();
    }

    private static File getTemporaryFile(String fileName) throws IOException {
        File tempFile = new File(getTempLocation(), fileName);
        tempFile.createNewFile();
        tempFile.deleteOnExit();
        return tempFile;
    }

    private static String getTempLocation() {
        return System.getProperty("java.io.tmpdir");
    }
}
