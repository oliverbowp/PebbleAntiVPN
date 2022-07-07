package pebbleantivpn.pebbleantivpn;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;
import pebbleantivpn.data.SpigotHandler;

public class PebbleAntiVPNLoggerSpigot implements Filter {

    private final SpigotHandler handler;

    public PebbleAntiVPNLoggerSpigot(PebbleAntiVPNSpigot plugin) {
        this.handler = plugin.getHandler();
        ((Logger) LogManager.getRootLogger()).addFilter(this);
    }

    @Override
    public Filter.Result getOnMismatch() {
        return Filter.Result.NEUTRAL;
    }

    @Override
    public Filter.Result getOnMatch() {
        return Filter.Result.NEUTRAL;
    }

    public Filter.Result filter(org.apache.logging.log4j.core.Logger arg0, Level arg1, Marker arg2, String message, Object... arg4) {
        return logMessage(message);
    }

    public Filter.Result filter(org.apache.logging.log4j.core.Logger arg0, Level arg1, Marker arg2, String message, Object arg4) {
        return logMessage(message);
    }

    public Filter.Result filter(org.apache.logging.log4j.core.Logger arg0, Level arg1, Marker arg2, Object message, Throwable arg4) {
        return logMessage(message.toString());
    }

    public Filter.Result filter(org.apache.logging.log4j.core.Logger arg0, Level arg1, Marker arg2, Message message, Throwable arg4) {
        return logMessage(message.getFormattedMessage());
    }

    public Filter.Result filter(org.apache.logging.log4j.core.Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5) {
        return logMessage(message);
    }

    public Filter.Result filter(org.apache.logging.log4j.core.Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5, Object arg6) {
        return logMessage(message);
    }

    public Filter.Result filter(org.apache.logging.log4j.core.Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5, Object arg6, Object arg7) {
        return logMessage(message);
    }

    public Filter.Result filter(org.apache.logging.log4j.core.Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8) {
        return logMessage(message);
    }

    public Filter.Result filter(org.apache.logging.log4j.core.Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9) {
        return logMessage(message);
    }

    public Filter.Result filter(org.apache.logging.log4j.core.Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10) {
        return logMessage(message);
    }

    public Filter.Result filter(org.apache.logging.log4j.core.Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11) {
        return logMessage(message);
    }

    public Filter.Result filter(org.apache.logging.log4j.core.Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
        return logMessage(message);
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) {
        return logMessage(message);
    }

    @Override
    public Filter.Result filter(LogEvent event) {
        return logMessage(event.getMessage().getFormattedMessage());
    }

    @Override
    public LifeCycle.State getState() {
        try {
            return LifeCycle.State.STARTED;
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public void initialize() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStarted() {
        return true;
    }

    @Override
    public boolean isStopped() {
        return false;
    }

    public Result logMessage(String message) {
        if (!(boolean) this.handler.getConfig("console-filter", false)) return Result.NEUTRAL;
        if (message.contains("lost connection")) {
            return Result.DENY;
        }
        if ((message.contains("Disconnecting"))) {
            return Result.DENY;
        }
        if (message.contains("UUID of player ") && message.contains(" is ")) {
            return Result.DENY;
        }
        if (message.contains("Could not pass event PlayerLoginEvent")) {
            return Result.DENY;
        }
        if(message.contains("An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.")) {
            return Result.DENY;
        }
        if(message.contains("Selector.select() returned prematurely 512 times in a row; rebuilding selector.")) {
            return Result.DENY;
        }
        return Result.NEUTRAL;
    }

}
