package ypjs.project.controller;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.UUID;

@RestController
public class SummernoteController {

    private static final String fileRoot = "C:\\summernoteImg\\"; // 이미지 저장 경로 설정

    @PostMapping("/SummerNoteImageFile")
    public JsonObject summerNoteImageFile(@RequestParam("file") MultipartFile file) {
        JsonObject jsonObject = new JsonObject();

        if (file.isEmpty()) {
            jsonObject.addProperty("responseCode", "error");
            return jsonObject;
        }

        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String saveFileName = UUID.randomUUID() + extension;
        File targetFile = new File(fileRoot + saveFileName);

        try {
            InputStream fileStream = file.getInputStream();
            OutputStream outputStream = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            fileStream.close();

            jsonObject.addProperty("url", "/summernoteImg/" + saveFileName);
            jsonObject.addProperty("responseCode", "success");
        } catch (IOException e) {
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        return jsonObject;
    }
}
