package com.example.utils;

import java.io.*;

public final class FileUtils {
    private FileUtils() {
        throw new UnsupportedOperationException();
    }

    public static String getTempDir() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String getTempFilePath(String tempDir, String fileName) throws IOException {
        File tempFile = new File(tempDir, fileName);
        tempFile.createNewFile();
        tempFile.delete();

        return tempFile.getAbsolutePath();
    }

    public static void writeFile(String filePath, byte[] rawFile) {
        InputStream input = new ByteArrayInputStream(rawFile);

        try (BufferedInputStream bufferedInput = new BufferedInputStream(input, 8192);
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream(filePath), 8192);) {

            byte[] buffer = new byte[8192];
            for (int length; ((length = bufferedInput.read(buffer)) > 0);) {
                bufferedOutput.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace(); // todo
        }
    }

    public static byte[] toByteArray(File file) throws IOException {
        byte[] bytes = new byte[(int) file.length()];

        // funny, if can use Java 7, please uses Files.readAllBytes(path)
        try(FileInputStream fis = new FileInputStream(file)){
            fis.read(bytes);
        }

        return bytes;
    }
}
