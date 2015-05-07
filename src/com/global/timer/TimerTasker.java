package com.global.timer;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTasker
{

    public static void timerSchdule(long time, TimerTask task)
    {
        new Timer().schedule(task, time);
    }

    public static void timerSchdule(long time, final Runnable run)
    {
        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                run.run();
            }
        }, time);
    }

    public static Timer timerSchduleRe(long time, final Runnable run)
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                run.run();
            }
        }, time, time);
        return timer;
    }
}
