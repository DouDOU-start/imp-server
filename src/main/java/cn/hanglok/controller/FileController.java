package cn.hanglok.controller;

import cn.hanglok.dto.DicomInfoDto;
import cn.hanglok.entity.res.Res;
import cn.hanglok.service.FileService;
import cn.hanglok.util.DicomUtils;
import cn.hanglok.util.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author Allen
 * @version 1.0
 * @className FileUploadController
 * @description 文件上传
 * @date 2023/5/22 16:01
 */
@RestController
@RequestMapping("/file")
@Tag(name = "2.0 文件模块")
public class FileController {

    @Autowired
    private FileService fileService;

    @Operation(summary = "上传DICOM文件")
    @PostMapping("/dicom")
    public Res<DicomInfoDto> uploadDicom(@RequestParam MultipartFile file) {

        File tempFile = FileUtils.convertTempFile(file);

        DicomInfoDto dicomInfoDto = null;

        if (DicomUtils.verify(tempFile)) {
            dicomInfoDto = fileService.uploadDicom(tempFile);
        }

        tempFile.delete();

        if (null == dicomInfoDto) {
            return Res.entity(400, "请上保证DICOM文件的准确性！", null);
        }

        return Res.ok(dicomInfoDto);
    }

    @Operation(summary = "上传标注文件")
    @Parameters(@Parameter(name = "seriesId", description = "系列 id", in = ParameterIn.PATH))
    @PostMapping("/label/{seriesId}")
    public Res<Boolean> uploadLabel(@RequestParam MultipartFile file, @PathVariable("seriesId") String seriesId) {
        return Res.ok(fileService.uploadLabel(file, seriesId));
    }

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file,
//                                             @RequestParam String fileName,
//                                             @RequestParam int chunkNumber,
//                                             @RequestParam int totalChunks,
//                                             @RequestParam String identifier) throws IOException {
//        // 创建临时目录
//        String tempDir = uploadDir + identifier;
//        File tempDirFile = new File(tempDir);
//        tempDirFile.mkdirs();
//
//        // 保存分片文件
//        File chunkFile = new File(tempDir, chunkNumber + ".part");
//        file.transferTo(chunkFile);
//
//        System.out.println(chunkNumber + " Chunk uploaded successfully");
//
//        // 检查是否收集到所有分片，如果是，则进行分片组装
//        if (chunkNumber == totalChunks - 1) {
//            assembleFile(tempDir, fileName);
//        }
//
//        return ResponseEntity.ok("Chunk uploaded successfully");
//    }
//
//    private void assembleFile(String tempDir, String filename) throws IOException {
//        File[] chunks = new File(tempDir).listFiles();
//        Arrays.sort(chunks, Comparator.comparing(File::getName));
//
//        File outputFile = new File(uploadDir, filename);
//
//        try (FileOutputStream outputStream = new FileOutputStream(outputFile, true)) {
//            for (File chunk : chunks) {
//                byte[] chunkData = Files.readAllBytes(chunk.toPath());
//                outputStream.write(chunkData);
//                chunk.delete();
//            }
//        }
//
//        System.out.println("文件合成成功");
//
//        // 删除临时目录
//        FileUtils.deleteDirectory(new File(tempDir));
//    }

}
