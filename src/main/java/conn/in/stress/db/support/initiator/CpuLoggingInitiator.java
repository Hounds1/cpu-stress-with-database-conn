package conn.in.stress.db.support.initiator;

import conn.in.stress.db.support.processor.OverHitLoggingProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CpuLoggingInitiator {

    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledFuture;

    public CpuLoggingInitiator() {
        scheduler = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            log.info("cpu logging initiator already started");
            return;
        }

        log.info("cpu logging started by initiator");

        scheduledFuture = scheduler.scheduleAtFixedRate(OverHitLoggingProcessor::invoke, 1, 5, TimeUnit.SECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();

        try {
            if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }

            log.info("Logging processor shutting down. Waiting for cpu logging to finish.");
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        while (true) {
            if (!scheduler.isTerminated()) {
                log.info("Waiting for tasks to terminate.");
                try {
                    Thread.sleep(100); // 과도한 CPU 사용 방지
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 쓰레드 상태 복구
                    break;
                }
            } else {
                log.info("Tasks terminated.");
                break;
            }
        }
    }
}
