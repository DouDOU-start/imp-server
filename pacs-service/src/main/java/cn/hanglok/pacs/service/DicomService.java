package cn.hanglok.pacs.service;

import cn.hanglok.pacs.dto.DicomInfoDto;

/**
 * @author Allen
 * @version 1.0
 * @className DicomService
 * @description TODO
 * @date 2023/6/7 16:08
 */
public interface DicomService {
    DicomInfoDto save(DicomInfoDto dicomInfo);
}
