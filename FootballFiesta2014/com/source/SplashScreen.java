/*
 * SplashScreen.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.source;




import java.util.Timer;
import java.util.TimerTask;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.KeyListener;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;

import com.utils.*;
import com.utils.ScreenManager;

/**
 * Splash Screen displays for 5000ms the first scrren then the next Screen press
 * ESC key to close screen one
 * @author Bikas
 * @Date 07-01-2014
 */

public class SplashScreen extends MainScreen
{
        private Bitmap backgroundImage;
        private MainScreen next;
        private UiApplication application;
        private Timer timer = new Timer();
        private int backgroundColor = Color.BLACK;

        private boolean isInitialized = false;

        public SplashScreen(UiApplication ui, MainScreen next)
        {
                super();

                Bitmap image = Bitmap.getBitmapResource("splash.jpg");
                backgroundImage = Utils.resizeBitmap(image, Utils.displayWidth,
                                Utils.displayHeight);
                BitmapField bf = new BitmapField(backgroundImage);
                bf.setSpace(Display.getWidth() / 2 - backgroundImage.getWidth() / 2,
                                Display.getHeight() / 2 - backgroundImage.getHeight() / 2);

                this.application = ui;
                this.next = next;
                this.add(bf);
                SplashScreenListener listener = new SplashScreenListener(this);
                this.addKeyListener(listener);
                timer.schedule(new CountDown(), Utils.SPLASH_TIME);
                application.pushScreen(this);
        }

        public void dismiss()
        {
            Utils.INITIALIZATION_COMPLETE = false;
            initialize();
            while(!Utils.INITIALIZATION_COMPLETE)
            {
            }
            HomeScreen.setImages();
            ScreenManager screenManager = ScreenManager.getInstance();
            int nextIndex = ScreenManager.HOME_SCREEN;
            if (screenManager.mainScreenArray[nextIndex] == null)
                screenManager.mainScreenArray[nextIndex] = new HomeScreen();
            timer.cancel();
            application.popScreen(this);
            //application.pushScreen(next);
            application.pushScreen(screenManager.mainScreenArray[nextIndex]);
        }
        
        public void initialize()
        {            
            Utils.getXmlInformation();
            if(Utils.squadVector == null || Utils.squadVector.size() == 0)
            {
                //System.out.println("NEED XML PARSING");
                Parser parser = new Parser();
                parser.parseGroups();
                parser.parseTeams();
                parser.parseStadium();
                parser.parseCity();
                parser.parseHistory();
                parser.parseMatch();
                parser.parseRemainingMatch();
                parser.parseSquad();
                Utils.FIRST_RUN = true;                
                //Utils.saveXmlInformation();
            }
            else
                Utils.FIRST_RUN = false;
                
            if(Utils.pointTableVector != null && Utils.pointTableVector.size() > 0 )
                Utils.POINTTABLE_UPDATED = true;
                            
            Utils.INITIALIZATION_COMPLETE = true;
            
            
            
        }

        private class CountDown extends TimerTask
        {
                public void run()
                {
                    DismissThread dThread = new DismissThread();
                    application.invokeLater(dThread);
                }// end of run
        }

        private class DismissThread implements Runnable
        {
            public void run()
            {
                dismiss();
            }
        }

        protected boolean navigationClick(int status, int time)
        {
            dismiss();
            return true;
        }

        protected boolean navigationUnclick(int status, int time)
        {
            return false;
        }

        protected boolean navigationMovement(int dx, int dy, int status, int time)
        {
                return false;
        }

        public static class SplashScreenListener implements KeyListener
        {
                private SplashScreen screen;

                public boolean keyChar(char key, int status, int time)
                {
                        // intercept the ESC and MENU key - exit the splash screen
                        boolean retval = false;
                        switch (key)
                        {
                        case Characters.CONTROL_MENU:
                        case Characters.ESCAPE:
                                screen.dismiss();
                                retval = true;
                                break;
                        }
                        return retval;
                }

                public boolean keyDown(int keycode, int time)
                {
                        return false;
                }

                public boolean keyRepeat(int keycode, int time)
                {
                        return false;
                }

                public boolean keyStatus(int keycode, int time)
                {
                        return false;
                }

                public boolean keyUp(int keycode, int time)
                {
                        return false;
                }

                public SplashScreenListener(SplashScreen splash)
                {
                        screen = splash;
                }
        }

        protected void makeMenu(Menu menu, int instance)
        {
                // menu.add(mainMenu);
                // menu.add(MenuItem.separator(mainMenu.getOrdinal()+1));

                menu.add(exitMenu);
        }

        public void closeScreen()
        {
                this.close();
        }

        private MenuItem exitMenu = new MenuItem("Exit", 150, 10)
        {
                public void run()
                {
                        if (Dialog.ask(Dialog.D_YES_NO, "Exit Application?") == Dialog.YES)
                        {
                                System.exit(0);
                        }
                }
        };

        public boolean onClose()
        {
                closeScreen();
                return true;
        }
}
