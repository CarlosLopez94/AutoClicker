package com.company;

import sun.awt.Mutex;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.concurrent.Semaphore;

public class AutoClicker extends Thread {
    private final Robot robot;

    private final Semaphore semaphore;
    private final Mutex configMutex;
    private Integer currentSleepTimeMillis;
    private boolean enabled;
    private boolean writeLogsEnable;
    private Point mouseClickPosition;

    public AutoClicker() throws AWTException {
        this(5, true, true, new Point(200, 200));
    }

    public AutoClicker(Integer initCurrentSleepTimeMillis, boolean enabled, boolean writeLogsEnable, Point mouseClickPosition) throws AWTException {
        this.robot = new Robot();
        semaphore = new Semaphore(0);
        this.configMutex = new Mutex();
        this.currentSleepTimeMillis = initCurrentSleepTimeMillis;
        this.enabled = enabled;
        this.writeLogsEnable = writeLogsEnable;
        this.mouseClickPosition = mouseClickPosition;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public Integer getCurrentSleepTimeMillis() {
        configMutex.lock();
        Integer currentSleepTimeMillisAux = currentSleepTimeMillis;
        configMutex.unlock();
        return currentSleepTimeMillisAux;
    }

    public void setCurrentSleepTimeMillis(Integer currentSleepTimeMillis) {
        configMutex.lock();
        this.currentSleepTimeMillis = currentSleepTimeMillis;
        writeLog("SleepTimeMillis: " + currentSleepTimeMillis);
        configMutex.unlock();
    }

    public boolean isEnabled() {
        configMutex.lock();
        boolean enabledAux = enabled;
        configMutex.unlock();
        return enabledAux;
    }

    public void setEnabled(boolean enabled) {
        configMutex.lock();
        this.enabled = enabled;
        writeLog("Enabled: " + enabled);
        configMutex.unlock();
    }

    public boolean isWriteLogsEnable() {
        configMutex.lock();
        boolean writeLogsAux = writeLogsEnable;
        configMutex.unlock();
        return writeLogsAux;
    }

    public void setWriteLogsEnable(boolean writeLogsEnable) {
        configMutex.lock();
        this.writeLogsEnable = writeLogsEnable;
        configMutex.unlock();
    }

    public Point getMouseClickPosition() {
        configMutex.lock();
        Point mouseClickPositionAux = (Point) mouseClickPosition.clone();
        configMutex.unlock();
        return mouseClickPositionAux;
    }

    public void setMouseClickPosition(Point mouseClickPosition) {
        configMutex.lock();
        this.mouseClickPosition = mouseClickPosition;
        writeLog("newPosition: " + mouseClickPosition);
        configMutex.unlock();
    }

    private void writeLog(String log) {
        if (writeLogsEnable) {
            System.out.println(log);
        }
    }

    private void autoclick() {
        // Move mouse
        moveMouseToPosition();

        // Click
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        writeLog("Pressed");
    }

    public void moveMouseToPosition() {
        robot.mouseMove(mouseClickPosition.x, mouseClickPosition.y);
        writeLog(String.format("Mouse moved to (%d,%d)", mouseClickPosition.x, mouseClickPosition.y));
    }

    @Override
    public void run() {
        System.out.println("AutoClicker Thread started");
        while (true) {
            try {
                // If its not enabled wait for notification
                if (!isEnabled()) {
                    writeLog("AutoClicker is disabled");
                    semaphore.acquire();
                    writeLog("AutoClicker is enable");
                }

                autoclick();

                // Wait for next click
                Thread.sleep(getCurrentSleepTimeMillis() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
