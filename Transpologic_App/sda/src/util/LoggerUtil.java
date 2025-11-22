package util;

import java.util.logging.*;

public final class LoggerUtil {
    private static final Logger logger = Logger.getLogger("TranspoLogic");

    static {
        logger.setUseParentHandlers(false);
        Handler console = new ConsoleHandler();
        console.setLevel(Level.ALL);
        logger.addHandler(console);
        logger.setLevel(Level.INFO);
        // optionally add FileHandler if you want logs persisted
    }

    private LoggerUtil() {}

    public static Logger getLogger() { return logger; }
}
