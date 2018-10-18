package com.websocket.utils;

import java.io.*;

public class FileUtil {

    public static String readFile(String path) {
        String result = null;
        File file = new File(path);
        if (file.exists()) {
            InputStream inputStream;
            try {
                inputStream = new FileInputStream(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }
                result = byteArrayOutputStream.toString("UTF-8");
                inputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
