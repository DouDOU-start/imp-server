package cn.hanglok.common;

import cn.hanglok.common.dcm.net.*;
import cn.hanglok.common.dcm.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.dcm4che3.data.Tag;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

/**
 * @author Allen
 * @version 1.0
 * @className cn.hanglok.common.SCUTest
 * @description TODO
 * @date 2023/9/8
 */
@Slf4j
public class SCUTest {

    private static final String remoteAet = "ORTHANC";
    private static final String remoteHost = "127.0.0.1";
    private static final int remotePort = 4242;

    @Test
    @Disabled
    public void getSeriesInstanceUID() {
        SCU scu = SCUConstructor.createSCU(remoteAet, remoteHost, remotePort);
        List<String> seriesInstanceUID = scu.getSeriesInstanceUID(new CFindRSPHandler(scu.nextMessageID(), DimseType.C_FIND, scu));

        log.info("result: {}", seriesInstanceUID);
    }


    @Test
    @Disabled
    public void cstore() {

        SCU scu = SCUConstructor.createSCU(remoteAet, remoteHost, remotePort);

        String dicomFolderPath = "/Users/allen/imp-fileDir/dicom/阳江市人民医院/327059/1.2.840.113704.1.111.5528.1399618045.49/1.2.840.113704.1.111.9164.1399618350.6";

        FileUtils.scanFolder(new File(dicomFolderPath), file -> {
            scu.cstore(file);
            return null;
        });

    }

    @Test
    @Disabled
    public void cecho() {
        SCU scu = SCUConstructor.createSCU(remoteAet, remoteHost, remotePort);
        boolean status = scu.cecho();
        log.info("echo rsp: {}",  status ? "success": "false");
    }

    class download {
        public static void main(String[] args) {

            SCU scu = SCUConstructor.createSCU(remoteAet, remoteHost, remotePort);

            scu.download(QueryRetrieveLevel.Series, Tag.SeriesInstanceUID, "1.2.840.113704.1.111.5152.1384997683.18", "/Users/allen/imp-fileDir/cget");
        }
    }


}
