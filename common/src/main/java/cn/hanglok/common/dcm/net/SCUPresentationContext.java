package cn.hanglok.common.dcm.net;

import lombok.Getter;
import org.dcm4che3.data.UID;
import org.dcm4che3.net.pdu.PresentationContext;

/**
 * @author Allen
 * @version 1.0
 * @enumName SCUPresentationContext
 * @description TODO
 * @date 2023/9/5
 */
@Getter
public enum SCUPresentationContext {

    C_GET(0x01, UID.PatientRootQueryRetrieveInformationModelGet),
    C_FIND(0x02, UID.PatientRootQueryRetrieveInformationModelFind),
    C_STORE(0x03, UID.CTImageStorage);

    private final PresentationContext pc;
    private final String as;

    SCUPresentationContext(int pcid, String as) {
        this.as = as;
        this.pc = new PresentationContext(pcid, as,
                UID.ImplicitVRLittleEndian,
                UID.ExplicitVRLittleEndian,
                UID.DeflatedExplicitVRLittleEndian,
                UID.ExplicitVRBigEndian
        );
    }
}
