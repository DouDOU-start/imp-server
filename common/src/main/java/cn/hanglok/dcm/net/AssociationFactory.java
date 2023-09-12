package cn.hanglok.dcm.net;

import org.dcm4che3.net.*;
import org.dcm4che3.net.pdu.AAssociateRQ;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.Executors;

/**
 * @author Allen
 * @version 1.0
 * @className AssociationFactory
 * @description TODO
 * @date 2023/9/8
 */
public class AssociationFactory {
    public static Association connect(String remoteHost, int remotePort, AAssociateRQ rq) {
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

    public static Association connect(String remoteHost, int remotePort, AAssociateRQ rq, DimseRQHandler dimseRQHandler) {
        try {
            // Create a dicom device
            Device device = new Device();
            Connection conn = new Connection();
            device.setExecutor(Executors.newFixedThreadPool(1));
            device.addConnection(conn);

            device.setScheduledExecutor(Executors.newScheduledThreadPool(10));
            device.setDimseRQHandler(dimseRQHandler);

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
}
