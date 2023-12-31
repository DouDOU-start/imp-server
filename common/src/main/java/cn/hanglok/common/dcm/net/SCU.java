package cn.hanglok.common.dcm.net;

import cn.hanglok.common.dcm.core.AttributesH;
import lombok.extern.slf4j.Slf4j;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.data.VR;
import org.dcm4che3.net.*;
import org.dcm4che3.net.pdu.AAssociateRQ;
import org.dcm4che3.net.pdu.RoleSelection;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Allen
 * @version 1.0
 * @className SCU
 * @description SCU net
 * @date 2023/9/5
 */
@Slf4j
public class SCU {

    private final String SCUAet;
    private final String remoteAet;
    private final String remoteHost;
    private final int remotePort;

    protected List<Attributes> attrList;
    private final AtomicInteger messageID = new AtomicInteger();

    SCU(String remoteAet, String remoteHost, int remotePort) {
        this.SCUAet = "SCU";
        this.remoteAet = remoteAet;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    SCU(String aet, String remoteAet, String remoteHost, int remotePort) {
        this.SCUAet = aet;
        this.remoteAet = remoteAet;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public int nextMessageID() {
        return messageID.incrementAndGet() & 0xFFFF;
    }

    public void cstore(File dcmFile) {
        DimseRSPHandlerH rsp = new DimseRSPHandlerH(this.nextMessageID(), DimseType.C_STORE, this);
        cstore(dcmFile, rsp);
    }

    /**
     * Store DICOM file to server
     * @param dcmFile dicomFile
     * @param rspHandler DimseRSPHandler
     */
    public void cstore(File dcmFile, DimseRSPHandler rspHandler) {
        try {
            AAssociateRQ rq = new AAssociateRQ() {{
                setCalledAET(remoteAet); // Remote dicom Server applicationEntity Title (Aet)
                setCallingAET(SCUAet); // SCU Aet
                addPresentationContext(SCUPresentationContext.C_STORE.getPc()); //SCU supported presentation context.
            }};

            Association association = AssociationFactory.connect(remoteHost, remotePort, rq);

            association.cstore(
                    SCUPresentationContext.C_STORE.getAs(),
                    parseSOPInstanceUID(dcmFile),
                    0,
                    DataWriterAdapter.forAttributes(AttributesH.parseAttributes(dcmFile)),
                    UID.ImplicitVRLittleEndian,
                    rspHandler
            );

        }  catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String parseSOPInstanceUID(File file) {
        Attributes attr = AttributesH.parseAttributes(file);
        if (null != attr) {
            return attr.getString(Tag.SOPInstanceUID, "");
        }
        return null;
    }

    public void cfind(Attributes attr, DimseRSPHandlerH rspHandler) {

        try {
            AAssociateRQ rq = new AAssociateRQ() {{
                setCalledAET(remoteAet); // Remote dicom Server applicationEntity Title (Aet)
                setCallingAET(SCUAet); // SCU Aet
                addPresentationContext(SCUPresentationContext.C_FIND.getPc());
            }};

            Association association = AssociationFactory.connect(remoteHost, remotePort, rq);

            association.cfind(
                    SCUPresentationContext.C_FIND.getAs(),
                    2,
                    attr,
                    UID.ExplicitVRLittleEndian,
                    rspHandler
            );

            while (rspHandler.isRunning) {
                Thread.sleep(3);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public List<String> getSeriesInstanceUID(DimseRSPHandlerH rspHandler) {
        Attributes attributes = new Attributes() {{
            setString(Tag.QueryRetrieveLevel, VR.CS, String.valueOf(QueryRetrieveLevel.Patient));
            setNull(Tag.SeriesInstanceUID, VR.UI);
        }};

        cfind(attributes, rspHandler);

        return attrList.stream()
                .map(attr->attr.getString(Tag.SeriesInstanceUID, null))
                .filter(Objects::nonNull).toList();

    }

    public void cget(Attributes attr, String storeFolder) {
        try {
            AAssociateRQ rq = new AAssociateRQ() {{
                setCalledAET(remoteAet); // Remote dicom Server applicationEntity Title (Aet)
                setCallingAET(SCUAet); // SCU Aet
                addPresentationContext(SCUPresentationContext.C_GET.getPc());
                addPresentationContext(SCUPresentationContext.C_STORE.getPc());
                addRoleSelection(new RoleSelection(UID.CTImageStorage, false, true));
            }};

            Association association = AssociationFactory.connect(remoteHost, remotePort, rq, new CStoreRQHandler(storeFolder));

            association.cget(
                    UID.PatientRootQueryRetrieveInformationModelGet,
                    0,
                    attr,
                    UID.ExplicitVRLittleEndian,
                    new DimseRSPHandlerH(this.nextMessageID(), DimseType.C_GET, this)
            );

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void download(QueryRetrieveLevel qrl, int tag,  String value, String storeFolder) {
        Attributes attr = new Attributes() {{
            setString(Tag.QueryRetrieveLevel, VR.CS, qrl.toString());
            setString(tag, VR.UI, value);
        }};
        cget(attr, storeFolder);
    }

    public boolean cecho() {
        try {
            AAssociateRQ rq = new AAssociateRQ() {{
                setCalledAET(remoteAet); // Remote dicom Server applicationEntity Title (Aet)
                setCallingAET(SCUAet); // SCU Aet
                addPresentationContext(SCUPresentationContext.C_ECHO.getPc());
            }};

            Association association = AssociationFactory.connect(remoteHost, remotePort, rq);
            DimseRSP cecho = association.cecho();
            while (cecho.next()) {
                if (cecho.getCommand().getInt(Tag.Status, -1) == Status.Success) {
                    return true;
                }
            }
            return false;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
