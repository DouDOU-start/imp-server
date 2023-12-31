package cn.hanglok.pacs.controller;

import cn.hanglok.pacs.dto.DicomInfoDto;
import cn.hanglok.pacs.entity.res.Res;
import cn.hanglok.pacs.service.FileService;
import cn.hanglok.pacs.util.DicomUtils;
import cn.hanglok.pacs.util.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

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

    @Operation(summary = "下载标注文件")
    @Parameters({
            @Parameter(name = "seriesId", description = "系列 id", in = ParameterIn.PATH),
            @Parameter(name = "fileName", description = "标签文件名", in = ParameterIn.PATH)
    })
    @GetMapping("/label/{seriesId}/{fileName}")
    public void downloadSeriesLabel(HttpServletResponse response,
                                    @PathVariable("seriesId") String seriesId,
                                    @PathVariable("fileName") String fileName) {
        fileService.downloadSeriesLabel(response, seriesId, fileName);
    }

    @Operation(summary = "删除标注文件")
    @DeleteMapping("/label/{seriesId}/{fileName}")
    public Res<Boolean> delSeriesLabel(@PathVariable("seriesId") String seriesId,
                                       @PathVariable("fileName") String fileName) {
        return Res.ok(fileService.delSeriesLabel(seriesId, fileName));
    }

    @Operation(summary = "下载影像系列文件包")
    @Parameters(@Parameter(name = "seriesIds",description = "系列id，批量用英文逗号分隔", in = ParameterIn.QUERY))
    @GetMapping("/series")
    public void downloadSeries(HttpServletResponse response, @RequestParam String seriesIds) {
        fileService.downloadSeries(response, Arrays.stream(seriesIds.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "下载DICOM文件图片")
    @GetMapping("/jpg/{instanceId}")
    public void downloadInstanceJpg(HttpServletResponse response, @PathVariable("instanceId") String instanceId) {
        fileService.downloadInstanceJpg(response, instanceId);
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
