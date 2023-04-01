package ru.eqour.timetable.watch.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogHelper {

    private static final Logger LOG = LogManager.getLogger();

    public static void logStackTrace(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        LOG.log(Level.ERROR, errors);
    }
}
