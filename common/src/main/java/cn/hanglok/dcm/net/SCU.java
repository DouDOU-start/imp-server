package cn.hanglok.dcm.net;

import cn.hanglok.dcm.core.AttributesH;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.net.*;
import org.dcm4che3.net.pdu.AAssociateRQ;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.Executors;

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

    public SCU(String remoteAet, String remoteHost, int remotePort) {
        this.remoteAet = remoteAet;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
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

            String characterSet = attr.getString(Tag.SpecificCharacterSet);
            if (StringUtils.isBlank(characterSet) || !"GB18030".equals(characterSet)) { // 设置编码，防止乱码
                attr.setString(Tag.SpecificCharacterSet, VR.PN, "GB18030");
                attr.setString(Tag.SpecificCharacterSet, VR.CS, "GB18030");
            }

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

}
