package conn.in.stress.db.integration.controller;

import conn.in.stress.db.support.initiator.CpuLoggingInitiator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/db/in/stress")
@Slf4j
public class CpuStressController {

    private static boolean IS_ALREADY_RUNNING = false;

    private final CpuLoggingInitiator cpuLoggingInitiator;

    @GetMapping("/cpu")
    public String start() {
        if (!IS_ALREADY_RUNNING) {
            IS_ALREADY_RUNNING = true;
            cpuLoggingInitiator.start();
        }

//        log.info("Tomcat queue size : {}", tomcatQueueDisplay.getQueueSize());

        return "0";
    }

    @GetMapping("/stop")
    public String stop() {
        if (IS_ALREADY_RUNNING) {
            IS_ALREADY_RUNNING = false;
            cpuLoggingInitiator.shutdown();
        }

        return "0";
    }
}
