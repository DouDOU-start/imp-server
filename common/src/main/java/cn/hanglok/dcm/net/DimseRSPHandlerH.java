package cn.hanglok.dcm.net;

import lombok.extern.slf4j.Slf4j;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.net.Association;
import org.dcm4che3.net.DimseRSPHandler;
import org.dcm4che3.net.Status;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Allen
 * @version 1.0
 * @className DimseRSPHandlerH
 * @description TODO
 * @date 2023/9/8
 */
@Slf4j
public class DimseRSPHandlerH extends DimseRSPHandler {
    private final DimseType dimseType;
    boolean isRunning;

    private final SCU scu;

    DimseRSPHandlerH(int msgId, DimseType dimseType, SCU scu) {
        super(msgId);

        scu.attrList = new ArrayList<>();

        this.dimseType = dimseType;
        this.scu = scu;
        this.isRunning = true;
    }

    @Override
    public void onDimseRSP(Association as, Attributes cmd, Attributes data) {
        super.onDimseRSP(as, cmd, data);
        int findStatus = cmd.getInt(Tag.Status, -1);
        if (findStatus == Status.Success) {
            this.isRunning = false;
            log.debug("On {} RSP Success[msgId={}]", dimseType, super.getMessageID());
        } else if ((findStatus & 0xB000) == 0xB000) {
            scu.attrList.add(data);
            return;
        } else {
            log.error("On {} RSP Error[msgId{}]", dimseType,  super.getMessageID());
        }

        try {
            as.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public synchronized void onClose(Association as) {
        super.onClose(as);
    }

}
