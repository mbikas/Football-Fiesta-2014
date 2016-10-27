/*
 * CountDownScreen.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.source;

import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import net.rim.device.api.util.StringUtilities;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Background;

import com.utils.*;
import com.api.ui.component.*;
import com.api.ui.custom.*;

import net.rim.device.api.i18n.*;
import java.util.*;

import net.rim.device.api.io.http.HttpDateParser;
import net.rim.device.api.util.SimpleSortingVector;
import net.rim.device.api.util.Comparator;
import java.util.Vector;

/*
@author Bikas
@Date 07-01-2014
*/


public class CountDownScreen extends MyMainScreen implements FieldChangeListener
{

        private ScreenManager screenManager;
        public CountDownScreen thisClass;
        private int screenIndex = ScreenManager.COUNTDOWN_SCREEN;
        
        public CountDownScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            //initialize();
        }   
        
        private static CountDownScreen countDownScreen = null;
        synchronized public static CountDownScreen getInstances()
        {
            if (countDownScreen == null)
                countDownScreen = new CountDownScreen();
            return countDownScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new CountDownScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        
        CustomLabelField lblDays, lblHours, lblMins, lblSecs;
        long days=156, hours=03, mins=20, secs=56;
        
        public void initialize()
        {
            this.deleteAll();              
                                       
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager("Countdown");            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            vfmMain.add(new TranslatorField(Utils.displayWidth,10));
            
            getTimeDifference();
            VerticalFieldManager svfmSubHeader2 = TeamScreen.getSubHeaderManager("COUNTDOWN TO BRAZIL");
            vfmMain.add(svfmSubHeader2);
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(StadiumDetailsScreen.getTextField("June 12 to July 13, 2014"));
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            vfmMain.add(new SpacerField(Utils.displayWidth, 1, Color.BLACK));
            
            if(secs < 0)
            {
                vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
                vfmMain.add(StadiumDetailsScreen.getTextField("WorldCup is Running"));
                vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            }
            else
            {
                int fontHeightTimes = Utils.fontHeightText;
                CustomGridFieldManager gridManager = new CustomGridFieldManager(4, Manager.USE_ALL_WIDTH | DrawStyle.HCENTER);
                CustomLabelField lblDays1 = new CustomLabelField("Days", fontHeightTimes, Utils.colorTeamName, LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER);
                CustomLabelField lblHours1 = new CustomLabelField("Hours", fontHeightTimes, Utils.colorTeamName, LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER);
                CustomLabelField lblMins1 = new CustomLabelField("Minutes", fontHeightTimes, Utils.colorTeamName, LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER);
                CustomLabelField lblSecs1 = new CustomLabelField("Seconds", fontHeightTimes, Utils.colorTeamName, LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER);
                gridManager.add(lblDays1);gridManager.add(lblHours1);gridManager.add(lblMins1);gridManager.add(lblSecs1);
                
                lblDays = new CustomLabelField(days+"", fontHeightTimes, Utils.colorTeamCoach, LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER);
                lblDays.setFont(Utils.getBoldFont(fontHeightTimes));
                lblHours = new CustomLabelField(hours+"", fontHeightTimes, Utils.colorTeamCoach, LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER);
                lblHours.setFont(Utils.getBoldFont(fontHeightTimes));
                lblMins = new CustomLabelField(mins+"", fontHeightTimes, Utils.colorTeamCoach, LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER);
                lblMins.setFont(Utils.getBoldFont(fontHeightTimes));
                lblSecs = new CustomLabelField(secs+"", fontHeightTimes, Utils.colorTeamCoach, LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER);
                lblSecs.setFont(Utils.getBoldFont(fontHeightTimes));
                gridManager.add(lblDays);gridManager.add(lblHours);gridManager.add(lblMins);gridManager.add(lblSecs);
                vfmMain.add(gridManager);     
            }
            
            vfmMain.add(TeamScreen.getSubHeaderManager("Current Time:"));
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(StadiumDetailsScreen.getTextField(getCurrentTime()));
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            String zone = TimeZone.getDefault().getID();
            if(zone != null && zone.equalsIgnoreCase("GMT")== false && zone.equalsIgnoreCase("")== false)
            {
                vfmMain.add(StadiumDetailsScreen.getTextField("Time Zone: "+ zone));
                vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            }
            
                   
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }  
        
        SimpleSortingVector sortingVector = new SimpleSortingVector();
        public void getWorldCupMatches(String date)
        {
            sortingVector.removeAllElements();
            int size = Utils.matchVector.size();
            sortingVector.setSortComparator(new Comparator() 
            {
                public int compare(Object o1, Object o2) { 
                
                    Match1 match1 = (Match1)o1;
                    Match1 match2 = (Match1)o2;
                    return match1.id - match2.id;
                }
            });
            
            for(int i=0;i<Utils.matchVector.size();i++)
            {
                Match1 m = (Match1)Utils.matchVector.elementAt(i);
                if( m.date.equalsIgnoreCase(date) == true || m.date.equalsIgnoreCase(date) == true )
                    sortingVector.addElement(m);
            }        
            sortingVector.reSort();             
        }
        
        
        
        public String getCurrentTime(String format)
        {
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            c.setTime(new Date(System.currentTimeMillis())); //now
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            String timeNow = simpleDateFormat.format(c.getTime());
            return timeNow;            
        }   
        
        public String getCurrentTime()
        {
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            c.setTime(new Date(System.currentTimeMillis())); //now
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            String timeNow = simpleDateFormat.format(c.getTime());
            return timeNow;            
        }
        
        String worldCupTime = "12 Jun 2014 15:00:00";        
        public void getTimeDifference()
        {            
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            String futureTime = worldCupTime;
            long timeFuture = HttpDateParser.parse(futureTime);//means after 4 hours 
            long timeNow = HttpDateParser.parse(getCurrentTime());
            
            long difference = timeFuture - timeNow;
            //System.out.println(timeFuture+" -"+timeFuture +"="+difference);
            
            /*
            //Calendar futureDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            Calendar futureDate = Calendar.getInstance();
            futureDate.set(Calendar.YEAR, 2014);
            futureDate.set(Calendar.MONTH, 1);
            futureDate.set(Calendar.DATE, 8);
            futureDate.set(Calendar.HOUR_OF_DAY, 21);
            futureDate.set(Calendar.MINUTE, 55);
            futureDate.set(Calendar.SECOND, 00);
            long diff = (futureDate.getTime().getTime() - System.currentTimeMillis()) / 1000;
            */
                       
            days = difference / (24 * 60 * 60 * 1000);
            hours = (difference / (60 * 60 * 1000)- days * 24);
            mins = ((difference / (60 * 1000))-days * 24 * 60-hours * 60);
            secs = (difference/1000-days * 24 * 60 * 60-hours * 60 * 60-mins * 60);
            //System.out.println ("" + days + "days" + hours + "hours" + mins + "minutes" + secs + "seconds");
        }
        
        public void getTimeDifference1()
        {
            //http://stackoverflow.com/questions/1806836/get-difference-in-two-different-timestamps-in-blackberry-application
            String time1 = "2014-01-10 01:38:05";
            String zone1 = "Pacific/Midway";
            String time2 = "2014-01-12 12:38:05";
            String zone2 = "America/Chicago";
            
            long timeDiff = getTimeDifference(time1, zone1, time2, zone2);
            Date date = new Date(timeDiff);
            String time = String.valueOf(date);
            //System.out.println(time);
        }
        public long getTimeDifference(String timestamp1, String timezone1, String timestamp2, String timezone2) 
        {
            long time1 = getTime(timestamp1, TimeZone.getTimeZone(timezone1));
            long time2 = getTime(timestamp2, TimeZone.getTimeZone(timezone2));
            return time2 - time1;
        }

        public long getTime(String time, TimeZone timeZone) 
        {
            Date formatter = new Date(HttpDateParser.parse(time));
            int offset = timeZone.getRawOffset();
            return formatter.getTime() + offset;
        }

        
        Timer timer;
        public void StartTimer() 
        {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTick(), 0, 1000);
        }
        private class TimerTick extends TimerTask 
        {
            public void run() {    
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        initialize();
                        
                    }
                });
            }
        }  
        
        protected void makeMenu(Menu menu, int instance)
        {
            menu.add(backMenu);
            menu.add(increaseFontMenu);
            menu.add(decreaseFontMenu);
        }        
        private MenuItem backMenu = new MenuItem(Utils.BACK_TEXT, 100, 10)
        {
            public void run()
            {
                backToPreviousScreen();
            }
        };
        private MenuItem increaseFontMenu = new MenuItem("Increase Font", 110, 10)
        {
                public void run()
                {
                    Utils.increaseFont();
                    initialize();
                }
        };
        
        private MenuItem decreaseFontMenu = new MenuItem("Decrease Font", 120, 10)
        {
                public void run()
                {
                    Utils.decreaseFont();
                    initialize();
                }
        };
        private void backToPreviousScreen()
        {
            timer.cancel();
            Utils.backToPreviousScreen(screenIndex, thisClass);
        }
        
        public boolean onClose()
        {
            backToPreviousScreen();
            return true;
        }
        private void closeScreen()
        {
            this.close();
        }
        
        public void fieldChanged(Field field, int context)
        {
            String text = Utils.getButtonText(field);
            int buttonId = Utils.getButtonId(field);
        }
        
        
        private int previousWidth, previousHeight;
        public void checkOrientation()
        {
            if(previousWidth != Utils.displayWidth || previousHeight != Utils.displayHeight)
                initialize();
            previousWidth = Utils.displayWidth;
            previousHeight = Utils.displayHeight;
        }
}


