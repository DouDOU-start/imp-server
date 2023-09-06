package cn.hanglok.dcm.net;

import cn.hanglok.dcm.core.AttributesH;
import lombok.extern.slf4j.Slf4j;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.net.*;
import org.dcm4che3.net.pdu.AAssociateRQ;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Allen
 * @version 1.0
 * @className SCU
 * @description SCU net
 * @date 2023/9/5
 */
@Slf4j
public class SCU {

    private final String SCUAet = "SCU";
    private final String remoteAet;
    private final String remoteHost;
    private final int remotePort;
    private Map<Integer, List<Attributes>> rspMap = new HashMap<>();

    public SCU(String remoteAet, String remoteHost, int remotePort) {
        this.remoteAet = remoteAet;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    void getOrCreateAttributes(int msgId, Attributes attributes) {
        List<Attributes> attributesList = rspMap.get(msgId);
        if (attributesList == null) {
            attributesList = new ArrayList<>();
        }
        attributesList.add(attributes);
        rspMap.put(msgId, attributesList);
    }

    public Association connect(AAssociateRQ rq) {
        try {
            // Create a dicom device
            Device device = new Device();
            Connection conn = new Connection();
            device.setExecutor(Executors.newFixedThreadPool(1));
            device.addConnection(conn);

            // Create a applicationEntity (AE) for your Dicom server
            ApplicationEntity ae = new ApplicationEntity();
            ae.addConnection(conn);
            device.addApplicationEntity(ae);

            // Connect to the DICOM server
            Connection remoteConn = new Connection(null, remoteHost, remotePort);

            device.bindConnections();

            return ae.connect(remoteConn, rq);

        } catch (IncompatibleConnectionException | GeneralSecurityException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void cstore(File dcmFile) {
        try {
            Attributes attr = AttributesH.parseAttributes(dcmFile);

            if (null == attr) {
                return;
            }

            // Release this code for now, it will influence the Chinese characterSet.
//            String characterSet = attr.getString(Tag.SpecificCharacterSet);
//            if (StringUtils.isBlank(characterSet) || !"GB18030".equals(characterSet)) { // 设置编码，防止乱码
//                attr.setString(Tag.SpecificCharacterSet, VR.PN, "GB18030");
//                attr.setString(Tag.SpecificCharacterSet, VR.CS, "GB18030");
//            }

            AAssociateRQ rq = new AAssociateRQ() {{
                setCalledAET(remoteAet); // Remote dicom Server applicationEntity Title (Aet)
                setCallingAET(SCUAet); // SCU Aet
                addPresentationContext(SCUPresentationContext.C_STORE.getPc());
            }};

            Association association = connect(rq);

            association.cstore(
                    SCUPresentationContext.C_STORE.getAs(),
                    parseSOPInstanceUID(dcmFile),
                    0,
                    DataWriterAdapter.forAttributes(attr),
                    SCUPresentationContext.C_STORE.getTss()[0]
            );

            association.release();

        }  catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String parseSOPInstanceUID(File file) {
        Attributes attr = AttributesH.parseAttributes(file);
        if (null != attr) {
            return attr.getString(Tag.SOPInstanceUID, "");
        }
        return null;
    }

    public List<Attributes> cfind(int msgId, QueryRetrieveLevel qrl) {

        try {
            AAssociateRQ rq = new AAssociateRQ() {{
                setCalledAET(remoteAet); // Remote dicom Server applicationEntity Title (Aet)
                setCallingAET(SCUAet); // SCU Aet
                addPresentationContext(SCUPresentationContext.C_FIND.getPc());
            }};

            Association association = connect(rq);

            Attributes attr = new Attributes();

            attr.setString(Tag.QueryRetrieveLevel, VR.CS, qrl.getValue());
            attr.setString(Tag.PatientName, VR.PN, "");

            attr.setNull(Tag.SeriesInstanceUID, VR.UI);
            attr.setNull(Tag.InstanceCreatorUID, VR.UI);

            CountDownLatch latch = new CountDownLatch(1);

            association.cfind(
                    SCUPresentationContext.C_FIND.getAs(),
                    2,
                    attr,
                    SCUPresentationContext.C_FIND.getTss()[0],
                    new CFindRSPHandler(this, msgId, latch)
            );

            boolean timeout = ! latch.await(3000, TimeUnit.SECONDS);

            association.release();

            if (timeout) {
                rspMap.remove(msgId);
                throw new RuntimeException(String.format("cfind timeout, msgId: %s", msgId));
            }

            return rspMap.get(msgId);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public List<String> getSeriesInstanceUIDs(int msgId) {

        List<Attributes> attributes;

        if (null != (attributes = rspMap.get(msgId))) {
            return attributes.stream()
                    .map(attr->attr.getString(Tag.SeriesInstanceUID, null))
                    .filter(Objects::nonNull).toList();
        }

        log.error("msgId: {} no result", msgId);

        return null;

    }

    private static final class CFindRSPHandler extends DimseRSPHandler {

        private final SCU scu;

        private final int msgId;
        private final CountDownLatch latch;

        public CFindRSPHandler(SCU scu, int msgId, CountDownLatch latch) {
            super(msgId);
            this.scu = scu;
            this.msgId = msgId;
            this.latch = latch;
        }

        @Override
        public void onDimseRSP(Association as, Attributes cmd, Attributes data) {
            super.onDimseRSP(as, cmd, data);
            int findStatus = cmd.getInt(Tag.Status, -1);
            if (findStatus == Status.Success) {
                log.debug("On find RSP Success[msgId={}]", msgId);
                latch.countDown();
            } else if ((findStatus & 0xB000) == 0xB000) {
                scu.getOrCreateAttributes(msgId, data);
            } else {
                log.error("On find RSP Error[msgId{}", msgId);
            }
        }

        @Override
        public void onClose(Association as) {
            super.onClose(as);
        }
    }

}
