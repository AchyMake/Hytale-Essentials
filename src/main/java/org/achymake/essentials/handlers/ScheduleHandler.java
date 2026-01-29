package org.achymake.essentials.handlers;

import com.hypixel.hytale.server.core.HytaleServer;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduleHandler {
    public ScheduledFuture<?> schedule(Runnable command, int delay, TimeUnit timeUnit) {
        return getSchedule().schedule(command, delay, timeUnit);
    }
    public ScheduledExecutorService getSchedule() {
        return HytaleServer.SCHEDULED_EXECUTOR;
    }
}