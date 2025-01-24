package conn.in.stress.db.support.processor;

import conn.in.stress.db.support.queue.TomcatQueueDisplay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@Slf4j
public class OverHitLoggingProcessor {

    private static final MBeanServerConnection mBeanServer;
    private static final ObjectName osMBean;

    static {
        mBeanServer = ManagementFactory.getPlatformMBeanServer();
        osMBean = createOsMBean();

        if (osMBean == null) {
            throw new RuntimeException("Can't create osMBean");
        }
    }

    public static void invoke() {
        displaySystemCPU_Load();
        displayJvmCpuLoad();
    }

    private static void displaySystemCPU_Load() {
        try {
            double systemCpuLoad = (double) mBeanServer.getAttribute(osMBean, "SystemCpuLoad") * 100;

            log.info("System CPU Load: {}%", String.format("%.2f", systemCpuLoad));
        } catch (Exception e) {
            log.error("Can't get system cpu load", e);
        }
    }

    private static void displayJvmCpuLoad() {
        try {
            double processCpuLoad = (double) mBeanServer.getAttribute(osMBean, "ProcessCpuLoad") * 100;

            if (processCpuLoad < 40) {
                log.info("[FREE] JVM Process Loaded: {}%", String.format("%.2f", processCpuLoad));
            } else if (processCpuLoad > 40) {
                log.info("[WARNING] JVM Process Loaded: {}%", String.format("%.2f", processCpuLoad));
            } else if (processCpuLoad < 70) {
                log.info("[DANGER] JVM Process Loaded: {}%", String.format("%.2f", processCpuLoad));
            }

        } catch (Exception e) {
            log.error("Can't get process cpu load", e);
        }
    }

    private static ObjectName createOsMBean() {
        try {
            return ObjectName.getInstance("java.lang:type=OperatingSystem");
        } catch (MalformedObjectNameException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
