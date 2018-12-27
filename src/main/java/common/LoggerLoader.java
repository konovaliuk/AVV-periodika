package common;

import org.apache.log4j.Logger;

public class LoggerLoader {

    public static Logger getLogger(Class clazz) {
        return Logger.getLogger(clazz);
    }
}
