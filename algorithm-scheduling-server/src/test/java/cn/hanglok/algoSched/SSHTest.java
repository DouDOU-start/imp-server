package cn.hanglok.algoSched;

import com.jcraft.jsch.*;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Allen
 * @version 1.0
 * @className cn.hanglok.algoSched.SSHTest
 * @description TODO
 * @date 2023/12/18
 */
public class SSHTest {
    public static void main(String[] args) {

        String host = "192.168.5.164";
        String user = "hanglok";
        String password = "hanglok";
        int port = 22;

        try {
            JSch jsch = new JSch();

            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand("nvidia-smi");

            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();

            StringBuilder outputBuffer = new StringBuilder();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    outputBuffer.append(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    System.out.println("Exit status: " + channel.getExitStatus());
                    break;
                }
            }

//            System.out.println("nvidia-smi output:\n" + outputBuffer);

            String nvidiaSmiOutput = outputBuffer.toString();

            // 解析显卡内存信息
            Pattern memoryPattern = Pattern.compile("(\\d+)MiB / (\\d+)MiB");
            Matcher memoryMatcher = memoryPattern.matcher(nvidiaSmiOutput);

            int gpuCount = 0;
            while (memoryMatcher.find()) {
                gpuCount++;
                String usedMemory = memoryMatcher.group(1);
                String totalMemory = memoryMatcher.group(2);
                System.out.println("GPU " + gpuCount + ": Used Memory: " + usedMemory + " MiB, Total Memory: " + totalMemory + " MiB");
            }

            // 输出显卡数量
            System.out.println("Total GPUs: " + gpuCount);

            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
