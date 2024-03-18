package cn.hanglok.algoSched.component;


import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Allen
 * @version 1.0
 * @className HanglokAlgorithm
 * @description TODO
 * @date 2024/3/14
 */
@Component
public class HanglokAlgorithm {
    private final Map<String, AlgorithmAssembleMonitor> algorithmAssembleMonitorMap = new ConcurrentHashMap<>();

    public AlgorithmAssembleMonitor getAlgorithmAssembleMonitor(String taskId) {
        return algorithmAssembleMonitorMap.get(taskId);
    }

    public void addAlgorithmAssembleMonitor(String taskId, AlgorithmAssembleMonitor algorithmAssembleMonitor) {
        algorithmAssembleMonitorMap.put(taskId, algorithmAssembleMonitor);
    }
}
