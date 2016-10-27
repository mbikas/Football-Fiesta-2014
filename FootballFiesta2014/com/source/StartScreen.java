package com.source;

import net.rim.device.api.ui.UiApplication;

import net.rim.device.api.util.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;

import com.utils.ScreenManager;
import com.utils.Utils;
import com.io.threadpool.ThreadPool;
import com.utils.Parser;
import com.utils.XmlInformation1;
import com.utils.Logger;
import com.utils.Squad;
import com.utils.Player;

import java.util.*;


import com.utils.*;
import com.api.ui.component.*;
import com.api.ui.custom.*;

import net.rim.device.api.i18n.*;
import java.util.*;

/*
@author Bikas
@Date 01-01-2014
*/

final public class StartScreen extends UiApplication
{
        ScreenManager screenManager;
        public StartScreen()
        {            
            //Utils.displayWidth = Display.getWidth();
            //Utils.displayHeight = Display.getHeight();
            
            
            ThreadPool.createSingleInstance(Utils.MAX_THREADS_IN_THREAD_POOL);
            screenManager = ScreenManager.getInstance();
            
            new SplashScreen(this, null);            
            //screenManager.mainScreenArray[ScreenManager.MATCH_SCREEN] = new MatchScreen();
            //initialize();
            
            //Logger logger = new Logger();
            //logger.createPointTableSql();
            //logger.createMatchTableSql();
            //callCountDownScreen();
            
            //callHomeScreen();
            //testTimeZoneConversion();            
        }
        
        public static void main(String[] args)
        {
                StartScreen application = new StartScreen();
                application.enterEventDispatcher();
        }
        
        private void callHomeScreen()
        {
           HomeScreen.setImages();
           HomeScreen homeScreen = new HomeScreen();
            if (screenManager.mainScreenArray[ScreenManager.HOME_SCREEN] == null)
                screenManager.mainScreenArray[ScreenManager.HOME_SCREEN] = homeScreen;
            UiApplication.getUiApplication().pushScreen(homeScreen); 
        }
        
        private void callCountDownScreen()
        {
            int nextIndex = ScreenManager.COUNTDOWN_SCREEN;
            if (screenManager.mainScreenArray[nextIndex] == null)
                screenManager.mainScreenArray[nextIndex] = new CountDownScreen();
            CountDownScreen screen = (CountDownScreen)screenManager.mainScreenArray[nextIndex];
            screen.StartTimer();
            screen.initialize();
            //screenManager.previousScreenArray[nextIndex].push(screenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
            //closeScreen();
        }
        
        
}


