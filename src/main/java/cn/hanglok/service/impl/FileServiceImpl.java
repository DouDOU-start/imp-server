package cn.hanglok.service.impl;

import cn.hanglok.config.FileConfig;
import cn.hanglok.dto.DicomInfoDto;
import cn.hanglok.entity.ImageLabel;
import cn.hanglok.entity.ImageSeries;
import cn.hanglok.entity.SeriesTree;
import cn.hanglok.mapper.ImageSeriesMapper;
import cn.hanglok.service.DicomService;
import cn.hanglok.service.FileService;
import cn.hanglok.service.IImageInstancesService;
import cn.hanglok.service.IImageLabelService;
import cn.hanglok.util.DicomUtils;
import cn.hanglok.util.FileUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Allen
 * @version 1.0
 * @className FileServiceImpl
 * @description TODO
 * @date 2023/6/6 13:44
 */
@Service
public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    FileConfig fileConfig;

    @Autowired
    DicomService dicomService;

    @Autowired
    IImageLabelService imageLabelService;

    @Autowired
    ImageSeriesMapper imageSeriesMapper;

    @Autowired
    IImageInstancesService imageInstancesService;


    @Override
    public DicomInfoDto uploadDicom(File file) {

        File dir;

        DicomInfoDto dicomInfo = DicomUtils.loadDicom(file);
        if (null != dicomInfo) {
            String uploadFileDir = FileConfig.dicomLocation +
                    File.separator + dicomInfo.getInstitution().getInstitutionName() +
                    File.separator + dicomInfo.getInstitutionPatient().getPatientNumber() +
                    File.separator + dicomInfo.getImageStudies().getStudyUid() +
                    File.separator + dicomInfo.getImageStudies().getImageSeries().getSeriesUid();

            if (!(dir = new File(uploadFileDir)).exists()) {
                dir.mkdirs();
            }

            File uploadFile = new File(uploadFileDir + File.separator +
                    dicomInfo.getImageStudies().getImageSeries().getImageInstance().getInstanceNumber() + ".dcm");

            DicomUtils.chnTranscoding(file, uploadFile);

            return dicomService.save(dicomInfo);
        }

        return null;

    }

    @Override
    public boolean uploadLabel(MultipartFile file, String seriesId) {

        // 不允许上传空文件
        if (file.isEmpty()) {
            return false;
        }

        ImageSeries imageSeries = imageSeriesMapper.selectOne(new QueryWrapper<>() {{
            eq("id", seriesId);
        }});

        if (null == imageSeries) {
            return false;
        }

        try {

            String fileName = file.getOriginalFilename();
            if (null != fileName) {

                // 备份覆盖标签文件移动到回收站
//                File[] files = new File(FileConfig.labelLocation + File.separator + seriesId)
//                        .listFiles((dir1, name) -> name.equals(fileName));
//                if (null != files) {
//                    for (File bakFile : files) {
//                        File bakFileDir = new File(FileConfig.labelBakLocation + File.separator + seriesId);
//                        if (!bakFileDir.exists()) {
//                            bakFileDir.mkdirs();
//                        }
//                        Files.move(
//                                bakFile.toPath(),
//                                new File(FileConfig.labelBakLocation + File.separator +
//                                        seriesId + File.separator + bakFile.getName())
//                                        .toPath(),
//                                StandardCopyOption.REPLACE_EXISTING
//                        );
//                    }
//                }

                // 备份覆盖标签文件移动到回收站
                File bakFile = new File(FileConfig.labelLocation + File.separator + seriesId);
                File delFile = new File(FileConfig.labelBakLocation + File.separator + seriesId);
                FileUtils.deleteFolder(delFile);
                if (bakFile.exists()) {
                    Files.move(
                            bakFile.toPath(),
                            delFile.toPath(),
                            StandardCopyOption.REPLACE_EXISTING
                    );
                }

                // 保存文件到本地
                File fileDir = new File(FileConfig.labelLocation + File.separator + seriesId);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                file.transferTo(new File(fileDir.getAbsolutePath() + File.separator + fileName));

                imageLabelService.saveOrUpdateLabel(new ImageLabel() {{
                    setFileName(fileName);
                    setFileLocation(File.separator + seriesId + File.separator + fileName);
                    setSeriesId(Long.valueOf(seriesId));
                }});

                return true;
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void downloadSeries(HttpServletResponse response, List<Long> seriesIds) {

        File file;
        try {
            file = new File(zipBathSeries(seriesIds));
        } catch (FileNotFoundException e) {
            logger.error("找不到系列影像数据", e);
            return;
        }

        FileUtils.resFlush(response, file, seriesIds
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining("+")) + ".zip");

    }

    @Override
    public void downloadInstanceJpg(HttpServletResponse response, String instanceId) {
        String location = imageInstancesService.getLocation(instanceId);
        File jpg = DicomUtils.toJpg(new File(location));
        FileUtils.resFlush(response, jpg, jpg.getName());
    }

//    public String zipSeries(Long seriesId) throws FileNotFoundException {
//
//        SeriesTree seriesTree = imageSeriesMapper.getTree(seriesId);
//        if (null == seriesTree) {
//            return null;
//        }
//
//        String sourcePath = FileConfig.dicomLocation + seriesTree;
//
//        if (!new File(sourcePath).exists()) {
//            logger.error("系列影像路径不存在");
//            return null;
//        }
//
//        // 创建临时目录
//        File file;
//        while (true) {
//            file = new File(FileConfig.tmpLocation + File.separator + System.currentTimeMillis());
//            if (!file.exists()) {
//                file.mkdirs();
//                break;
//            }
//        }
//
//        // 复制DICOM文件
//        FileUtils.copyFolder(sourcePath, file.getAbsolutePath() + File.separator + "dicom");
//
//        // 创建文件夹，复制标注文件
//        File labelDir = new File(file.getAbsolutePath() + File.separator + "label");
//        if (! labelDir.exists()) {
//            labelDir.mkdir();
//        }
//        File[] files = new File(FileConfig.labelLocation + File.separator + seriesId).listFiles();
//        if (null != files) {
//            for (File labelFile : files) {
//                FileUtils.copyFile(
//                        labelFile,
//                        new File(file.getAbsolutePath() + File.separator + "label" + File.separator + labelFile.getName())
//                );
//            }
//        }
//
//        // 压缩影像数据
//        String zipFilePath = FileConfig.tmpLocation + File.separator + seriesId + ".zip";
//        FileUtils.toZip(
//                file.getAbsolutePath(),
//                new FileOutputStream(zipFilePath),
//                true
//        );
//
//        return zipFilePath;
//    }

    public String zipBathSeries(List<Long> seriesIds) throws FileNotFoundException {

        Map<Long, SeriesTree> seriesTreeList = new HashMap<>();
        seriesIds.forEach(seriesId -> {
            seriesTreeList.put(seriesId, imageSeriesMapper.getTree(seriesId));
        });

        // 创建临时目录
        File file;
        while (true) {
            file = new File(FileConfig.tmpLocation + File.separator + System.currentTimeMillis());
            if (!file.exists()) {
                file.mkdirs();
                break;
            }
        }

        File finalFile = file;

        // 复制DICOM文件
        seriesTreeList.forEach((seriesId, seriesTree) -> {
            FileUtils.copyFolder(
                    FileConfig.dicomLocation + seriesTree,
                    finalFile.getAbsolutePath() + File.separator + "dicom" + File.separator + seriesId
            );
        });


        seriesIds.forEach(seriesId -> {
            // 创建文件夹，复制标注文件
            File labelDir = new File(finalFile.getAbsolutePath() + File.separator + "label" + File.separator + seriesId);
            if (! labelDir.exists()) {
                FileUtils.mkdir(labelDir.getAbsolutePath());
            }
            File[] files = new File(FileConfig.labelLocation + File.separator + seriesId).listFiles();
            if (null != files) {
                for (File labelFile : files) {
                    FileUtils.copyFile(
                            labelFile,
                            new File(finalFile.getAbsolutePath() + File.separator + "label" +
                                    File.separator + seriesId + File.separator + labelFile.getName())
                    );
                }
            }
        });

        // 压缩影像数据
        String zipFilePath = FileConfig.tmpLocation + File.separator + seriesIds
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining("+")) + ".zip";

        FileUtils.toZip(
                file.getAbsolutePath(),
                new FileOutputStream(zipFilePath),
                true
        );

        return zipFilePath;

    }

    @Override
    public void downloadSeriesLabel(HttpServletResponse response, String seriesId, String fileName) {
        File file = new File(FileConfig.labelLocation + File.separator + seriesId + File.separator + fileName);
        FileUtils.resFlush(response, file, fileName);
    }

    @Override
    public Boolean delSeriesLabel(String seriesId, String fileName) {
        File file = new File(FileConfig.labelLocation + File.separator + seriesId + File.separator + fileName);
        file.delete();

        imageLabelService.remove(new QueryWrapper<>() {{
            eq("series_id", seriesId);
            eq("file_name", fileName);
        }});

        return true;
    }
}
