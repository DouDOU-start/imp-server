package cn.hanglok.common.dcm.net;

/**
 * @author Allen
 * @version 1.0
 * @className SCUConstructor
 * @description TODO
 * @date 2023/9/8
 */
public class SCUConstructor {
    public static SCU createSCU(String remoteAet, String remoteHost, int remotePort) {
        return new SCU(remoteAet, remoteHost, remotePort);
    }

    public static SCU createSCU(String sucAet, String remoteAet, String remoteHost, int remotePort) {
        return new SCU(sucAet, remoteAet, remoteHost, remotePort);
    }

}
