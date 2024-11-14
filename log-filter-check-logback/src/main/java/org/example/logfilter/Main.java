package org.example.logfilter;

import com.azure.core.util.logging.ClientLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;

public class Main {
    private static final ClientLogger LOGGER = new ClientLogger(Main.class);

    private static final Logger logger
            = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Example log from {}", Main.class.getSimpleName());
        LOGGER.atInfo().log("In main()");
        final FrameWork f = new FrameWork();
        f.callme(new Callable<String>() {
            @Override
            public String call() {
                throw new RuntimeException("...err...");
                // If you run this app, you will NOT see reactor-core 'onErrorDropped' since logback.xml
                // has a filter to filter out 'onErrorDropped'. But if we comment out that filter in
                // logback.xml, then following log will appear -
                //
                //  hh:mm:ss [main] ERROR r.c.p.Operators - Operator called default onErrorDropped
                //  reactor.core.Exceptions$ErrorCallbackNotImplemented: java.lang.RuntimeException: ...err...
            }
        });
    }

    private static final class FrameWork {
        public void callme(Callable<String> me) {
            Mono.fromCallable(me).subscribe();
        }
    }
}
