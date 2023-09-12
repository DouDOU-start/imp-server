package cn.hanglok.dcm.net;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomOutputStream;
import org.dcm4che3.net.*;
import org.dcm4che3.net.pdu.PresentationContext;
import org.dcm4che3.net.service.DicomServiceException;

import java.io.File;
import java.io.IOException;

/**
 * @author Allen
 * @version 1.0
 * @className CStoreRQHandler
 * @description TODO
 * @date 2023/9/12
 */
public class CStoreRQHandler implements DimseRQHandler {

    private final String storeFolder;

    public CStoreRQHandler(String storeFolder) {
        this.storeFolder = storeFolder;
    }

    @Override
    public void onDimseRQ(Association as, PresentationContext pc, Dimse dimse, Attributes cmd, PDVInputStream data) throws IOException {
        if (dimse != Dimse.C_STORE_RQ) {
            throw new DicomServiceException(Status.UnrecognizedOperation);
        }

        Attributes rsp = Commands.mkCStoreRSP(cmd, Status.Success);

        store(pc, cmd, data);

        as.tryWriteDimseRSP(pc, rsp);
    }

    public void store(PresentationContext pc, Attributes cmd, PDVInputStream data) {

        String SOPInstanceUID = cmd.getString(Tag.AffectedSOPInstanceUID);

        File folder = new File(storeFolder);
        if (! folder.exists()) {
            folder.mkdirs();
        }

        try (DicomOutputStream dos = new DicomOutputStream(new File(storeFolder + File.separator + SOPInstanceUID + ".dcm"))) {
            Attributes fmi = new Attributes() {{
                setString(Tag.MediaStorageSOPClassUID, VR.UI, UID.CTImageStorage);
                setString(Tag.MediaStorageSOPInstanceUID, VR.UI, SOPInstanceUID);
                setString(Tag.TransferSyntaxUID, VR.UI, UID.ExplicitVRLittleEndian);
            }};

            Attributes dataset = data.readDataset(pc.getTransferSyntax());

            dos.writeDataset(fmi, dataset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onClose(Association as) {

    }
}
