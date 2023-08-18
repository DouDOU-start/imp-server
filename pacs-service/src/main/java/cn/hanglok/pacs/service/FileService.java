package cn.hanglok.pacs.service;


import cn.hanglok.pacs.dto.DicomInfoDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @author Allen
 * @version 1.0
 * @className FileService
 * @description TODO
 * @date 2023/6/6 13:43
 */
public interface FileService {
    DicomInfoDto uploadDicom(File file);

    boolean uploadLabel(MultipartFile file, String seriesId);

    void downloadSeries(HttpServletResponse response, List<Long> seriesIds);

    void downloadInstanceJpg(HttpServletResponse response, String instanceId);

    void downloadSeriesLabel(HttpServletResponse response, String seriesId, String fileName);

    Boolean delSeriesLabel(String seriesId, String fileName);
}
