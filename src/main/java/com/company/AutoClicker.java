package com.company;

import sun.awt.Mutex;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.concurrent.Semaphore;

public class AutoClicker extends Thread{
    private Robot robot;

    private final Semaphore semaphore;
    private Mutex configMutex;
    private Integer currentSleepTimeMillis;
    private boolean enabled;
    private boolean writeLogs;
    private Point mouseClickPosition;

    public AutoClicker() throws AWTException {
        this(5000, false, true, new Point(200,200));
    }

    public AutoClicker(Integer initCurrentSleepTimeMillis, boolean enabled, boolean writeLogs, Point mouseClickPosition) throws AWTException {
        this.robot = new Robot();
        semaphore = new Semaphore(1);
        this.configMutex = new Mutex();
        this.currentSleepTimeMillis = initCurrentSleepTimeMillis;
        this.enabled = enabled;
        this.writeLogs = writeLogs;
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
        configMutex.unlock();
    }

    public boolean isWriteLogs() {
        configMutex.lock();
        boolean writeLogsAux= writeLogs;
        configMutex.unlock();
        return writeLogsAux;
    }

    public void setWriteLogs(boolean writeLogs) {
        configMutex.lock();
        this.writeLogs = writeLogs;
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
        configMutex.unlock();
    }

    private void writeLog(String log){
        System.out.println(log);
    }

    private void autoclick(){
        // Move mouse
        moveMouseToPosition();

        //
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        System.out.println("Pressed");
    }

    public void moveMouseToPosition(){
        robot.mouseMove(mouseClickPosition.x, mouseClickPosition.y);
        System.out.printf("Mouse moved to (%d,%d)%n",mouseClickPosition.x, mouseClickPosition.y);
    }

    @Override
    public void run() {
        System.out.println("AutoClicker Thread started");
        while(true){
            try {
                // If its not enabled wait for notification
                if(!isEnabled()){
                    System.out.println("AutoClicker is disabled");
                    semaphore.acquire();
                    System.out.println("AutoClicker is enable");
                }

                // Wait for next click
                Thread.sleep(getCurrentSleepTimeMillis());

                autoclick();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
