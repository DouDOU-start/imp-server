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
                File[] files = new File(FileConfig.labelLocation)
                        .listFiles((dir1, name) -> name.substring(0, name.indexOf(".")).equals(seriesId));
                for (File bakFile : files) {
                    Files.move(
                            bakFile.toPath(),
                            new File(FileConfig.labelBakLocation + File.separator + bakFile.getName()).toPath(),
                            StandardCopyOption.REPLACE_EXISTING
                    );
                }

                // 保存文件到本地
                String fileLocation = FileConfig.labelLocation + File.separator + seriesId + fileName.substring(fileName.indexOf("."));
                file.transferTo(new File(fileLocation));

                imageLabelService.saveOrUpdateLabel(new ImageLabel() {{
                    setFileName(fileName);
                    setFileLocation(fileLocation);
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
    public void downloadSeries(HttpServletResponse response, String seriesId) {

        File file;
        try {
            file = new File(zipSeries(Long.valueOf(seriesId)));
        } catch (FileNotFoundException e) {
            logger.error("找不到系列影像数据", e);
            return;
        }

        FileUtils.resFlush(response, file, seriesId + ".zip");

    }

    @Override
    public void downloadInstanceJpg(HttpServletResponse response, String instanceId) {
        String location = imageInstancesService.getLocation(instanceId);
        File jpg = DicomUtils.toJpg(new File(location));
        FileUtils.resFlush(response, jpg, jpg.getName());
    }

    public String zipSeries(Long seriesId) throws FileNotFoundException {

        SeriesTree seriesTree = imageSeriesMapper.getTree(seriesId);
        if (null == seriesTree) {
            return null;
        }

        String sourcePath = FileConfig.dicomLocation + seriesTree;

        if (!new File(sourcePath).exists()) {
            logger.error("系列影像路径不存在");
            return null;
        }

        // 创建临时目录
        File file;
        while (true) {
            file = new File(FileConfig.tmpLocation + File.separator + System.currentTimeMillis());
            if (!file.exists()) {
                file.mkdirs();
                break;
            }
        }

        // 复制DICOM文件
        FileUtils.copyFolder(sourcePath, file.getAbsolutePath() + File.separator + "dicom");

        // 复制标注文件
        File[] files = new File(FileConfig.labelLocation).listFiles((dir1, name) ->
                name.substring(0, name.indexOf(".")).equals(String.valueOf(seriesId)));

        for (File labelFile : files) {
            FileUtils.copyFile(
                    labelFile,
                    new File(file.getAbsolutePath() + File.separator + labelFile.getName())
            );
        }

        // 压缩影像数据
        String zipFilePath = FileConfig.tmpLocation + File.separator + seriesId + ".zip";
        FileUtils.toZip(
                file.getAbsolutePath(),
                new FileOutputStream(zipFilePath),
                true
        );

        return zipFilePath;
    }
}
