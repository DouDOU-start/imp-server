package cn.hanglok.dcm.net;

import lombok.extern.slf4j.Slf4j;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.net.Association;
import org.dcm4che3.net.DimseRSPHandler;
import org.dcm4che3.net.Status;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author Allen
 * @version 1.0
 * @className CFindRSPHandler
 * @description TODO
 * @date 2023/9/8
 */
@Slf4j
public class CFindRSPHandler extends DimseRSPHandlerH {
    public CFindRSPHandler(int msgId, DimseType dimseType, SCU scu) {
        super(msgId, dimseType, scu);
    }

}