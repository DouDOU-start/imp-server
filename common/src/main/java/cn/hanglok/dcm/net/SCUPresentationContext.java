package cn.hanglok.dcm.net;

import lombok.Getter;
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

    C_STORE("1.2.840.10008.5.1.4.1.1.2", new String[]{"1.2.840.10008.1.2.1", "1.2.840.10008.1.2.2"});

    private final PresentationContext pc;
    private final String as;
    private final String[] tss;

    SCUPresentationContext(String as, String[] tss) {
        this.as = as;
        this.tss = tss;
        this.pc = new PresentationContext(0, as, tss);
    }
}
