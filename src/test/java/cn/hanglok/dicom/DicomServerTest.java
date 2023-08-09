package cn.hanglok.dicom;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.net.*;
import org.dcm4che3.net.pdu.PresentationContext;
import org.dcm4che3.net.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.Executors;

/**
 * @author Allen
 * @version 1.0
 * @className DicomServerTest
 * @description TODO
 * @date 2023/8/9
 */
@SpringBootTest
public class DicomServerTest {

    @Test
    public void start() throws GeneralSecurityException, IOException {
        new DicomServer(1122, "aeTitle").start();
    }

    static class DicomServer {
        private final int port;
        private final String aeTitle;

        public DicomServer(int port, String aeTitle) {
            this.port = port;
            this.aeTitle = aeTitle;
        }

        public void start() throws IOException, GeneralSecurityException {

            Device device = new Device("device_name");

            device.setExecutor(Executors.newSingleThreadExecutor());

            Connection conn = new Connection();
            conn.setPort(port);

            device.addConnection(conn);

            ApplicationEntity ae = new ApplicationEntity();
            ae.setAETitle(aeTitle);

            device.addApplicationEntity(ae);

            device.setDimseRQHandler(new BasicCStoreSCP() {
                @Override
                protected void store(Association as, PresentationContext pc, Attributes rq, PDVInputStream data, Attributes rsp) {
                    System.out.print("收到数据");
                }
            });

            ae.addConnection(conn);

            device.bindConnections();

//            System.in.read();
        }
    }
}
