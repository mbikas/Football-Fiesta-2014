/*
 * MatchTodayScreen.java
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
import com.io.*;
import com.api.ui.component.*;
import com.api.ui.custom.*;
import com.api.ui.dropdown.*;
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


public class MatchTodayScreen extends MyMainScreen implements FieldChangeListener, ResponseListener
{
        private ScreenManager screenManager;
        public MatchTodayScreen thisClass;
        private int screenIndex = ScreenManager.MATCH_TODAY_SCREEN;
        private boolean timerStarted = false;
        
        public MatchTodayScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            getDatesFromMatches();
            MatchScreen.setColWidth();
            initialize();
        }   
        
        private static MatchTodayScreen matchTodayScreen = null;
        synchronized public static MatchTodayScreen getInstances()
        {
            if (matchTodayScreen == null)
                matchTodayScreen = new MatchTodayScreen();
            return matchTodayScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new MatchTodayScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        
        CustomLabelField lblDays, lblHours, lblMins, lblSecs;
        long days=156, hours=03, mins=20, secs=56;
        
        
        DropdownList choiceFieldDate = null;
        String dates[];
        String currentDate = "";
        int selectedIndex = 0;
        int buttonFontHeight = Utils.fontHeightButton;
        
        public void setButtonFontHeight(int buttonFontHeight)
        {
            this.buttonFontHeight = buttonFontHeight;
        } 
        
        public void getDatesFromMatches()
        {
            getWorldCupMatchesSortedByDate();
            currentDate = getCurrentTime("dd/MM");
            
            Vector strVector = new Vector();            
            String temp = "";
            for(int i=0;i<sortingVector.size();i++)
            {
                Match1 match = (Match1)sortingVector.elementAt(i);                
                if( temp.equalsIgnoreCase(match.date) == false)
                    strVector.addElement(match.date);
                temp = match.date;
            }
            dates = new String[strVector.size()];
            for(int i=0;i<strVector.size();i++)
                dates[i] = (String)strVector.elementAt(i);
            
            getClosestDate();
            
            /*
            selectedIndex = 0;
            for(int i=0;i<strVector.size();i++)
            {
                String str = (String)strVector.elementAt(i);  
                if(str.equalsIgnoreCase(currentDate) == true)
                    selectedIndex = i;
                dates[i] = str;
            }
            */
            //getWorldCupMatchesSortedByDateTime();            
            
        }
        
        private void getClosestDate()
        {
            selectedIndex = 0;
            for(int i=0;i<dates.length;i++)
            {
                String str = dates[i];  
                if(str.equalsIgnoreCase(currentDate) == true)
                {
                    selectedIndex = i;
                    return;
                }
            }
            
            String str1[] = Utils.split(currentDate, "/");
            int day = Integer.parseInt(str1[0]);
            int month = Integer.parseInt(str1[1]);
            int date1 = month * 30 + day;
            
            int min = 12345;
            for(int i=0;i<dates.length;i++)
            {
                String str2[] = Utils.split(dates[i], "/"); 
                int date2 = Integer.parseInt(str2[0]) + Integer.parseInt(str2[1]) * 30;
                int diff = Math.abs(date2 - date1);
                if(diff <= min)
                {
                    selectedIndex = i;
                    currentDate = dates[i];
                    min = diff;
                }
            }
            return;
        }
        
        public void initialize()
        {
            this.deleteAll();              
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager("Match By Date");            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            vfmMain.add(new TranslatorField(Utils.displayWidth,10));
            
            //getTimeDifference();
            
            
            CustomLabelField lblDate = new CustomLabelField("Date: ", Utils.fontHeightText, Utils.colorTextSubHeader);
            lblDate.setFont(Utils.getBoldFont(Utils.fontHeightText));
            lblDate.setMargin(0,0,0,10);
            
            int comboWidth = Utils.getStringLength(dates[0], Utils.getBoldFont(Utils.fontHeightText)) * 2;
            choiceFieldDate = getChoiceField(dates, selectedIndex, comboWidth, Utils.getBoldFont(Utils.fontHeightText));
            
            CustomButtonField buttonOK = new CustomButtonField(Utils.buttonImageNormnal, Utils.buttonImageOver, null, "Update", false , Utils.getNormalFont(buttonFontHeight));
            buttonOK.setChangeListener(this);
            buttonOK.setButtonId(BUTTON_ID_OK);
                        
            int managerHeight = choiceFieldDate.getPreferredHeight() + 10;
            if(buttonOK.getPreferredHeight() + 10 > managerHeight) managerHeight = buttonOK.getPreferredHeight() + 10;
            if(choiceFieldDate.getPreferredWidth() > comboWidth)comboWidth = choiceFieldDate.getPreferredWidth();
            HorizontalFieldManager buttonManager = new HorizontalFieldManager(HorizontalFieldManager.USE_ALL_WIDTH); 
            VerticalFieldManager vfmCombo = Utils.getMainVerticalManager(comboWidth,managerHeight);
            
            vfmCombo.add(choiceFieldDate);
            buttonManager.add(lblDate);
            buttonManager.add(vfmCombo);
            buttonManager.add(new TranslatorField(10, managerHeight));
            buttonManager.add(buttonOK);
            //buttonManager.add(new ButtonField("TEST"));
            vfmMain.add(buttonManager);
            //vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            
            //MatchScreen.setColWidth();
            Vector dateVector = getMatchVectorByDate(currentDate);
            vfmMain.add(TeamScreen.getSubHeaderManager("Matches"));
            CustomLabelField lblTime = new CustomLabelField("Match times are shown in local time", Utils.fontHeightText-2, Utils.colorText);
            lblTime.setFont(Utils.getNormalFont( Utils.fontHeightText-2));
            lblTime.setMargin(0,0,0,10);
            vfmMain.add(lblTime);
            vfmMain.add(new TranslatorField(Utils.displayWidth, 1));
            for(int i=0;i<dateVector.size();i++)
            {
                Match1 match = (Match1)dateVector.elementAt(i);
                vfmMain.add(MatchScreen.getMatchManager(match, true, thisClass));
                vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
            }
                   
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }  
        
        private static final int BUTTON_ID_OK  = 1231;
        
        public static DropdownList getChoiceField(String[] choices, int choiceIndex, int comboWidth, Font font)
        {
            DropdownList dropdown = new DropdownList(choices, choiceIndex, Utils.dropdownImageNormal, Utils.dropdownImageOver, Utils.dropdownIcon, comboWidth, font);
            //dropdown.setMargin(0,0, 0, 10);
            return dropdown;
        }  
        
        private String convTimeZone(String time, String sourceTZ, String destTZ)
        {
            /*
            a) Create a GMT timezone calendar.

            b) Parse the input date/time string into this calendar using 'set.  So for example, say the month was "02",and your GMT Calendar was called gmt Calendar, then you would set the month using
            
            gmtCalendar.set(Calendar.MONTH, 1); // MOnth 0 = January
            
            c) Create a target time zone Calendar Object
            
            d) Take the 'Date()' from the GMT Calendar and set the target Time Zone calendar t that date.
            
            e) Now you can extract the Date/Time components using <calendar>.get(..)
            */
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Brazil/Acre"));
            calendar.set(Calendar.DAY_OF_MONTH, 14);
            calendar.set(Calendar.MONTH, 6);
            calendar.set(Calendar.YEAR, 2014);
            calendar.set(Calendar.HOUR_OF_DAY, 22);
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 00);
            Date dateBrazil = calendar.getTime();
             
            Calendar calendarTarger = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
            calendarTarger.setTime(dateBrazil);
            
            int year = calendarTarger.get(Calendar.YEAR);
            int month = calendarTarger.get(Calendar.MONTH);
            int day =  calendarTarger.get(Calendar.DAY_OF_MONTH);
            int hour = calendarTarger.get(Calendar.HOUR_OF_DAY);
            return day + "/" + month+"/"+year + " "+hour;            
            
        }
        
        SimpleSortingVector sortingVector = new SimpleSortingVector();
        public void getWorldCupMatchesSortedByDate()
        {
            sortingVector.removeAllElements();
            sortingVector.setSortComparator(new Comparator() 
            {
                public int compare(Object o1, Object o2) { 
                
                    Match1 match1 = (Match1)o1;
                    Match1 match2 = (Match1)o2;
                    
                    String date1 = match1.date;
                    String date2 = match2.date;
                    
                    String str1[] = Utils.split(date1, "/");
                    int dayCount1 = Integer.parseInt(str1[0]) + Integer.parseInt(str1[1]) * 30;
                    
                    String str2[] = Utils.split(date2, "/");
                    int dayCount2 = Integer.parseInt(str2[0]) + Integer.parseInt(str2[1]) * 30;
                    
                    //int result = match1.date.compareTo(match2.date);
                    int result = dayCount1 - dayCount2;
                    if(result < 0)          return -1;
                    else if(result > 0)     return 1;
                    else 
                    {
                        int result1 = match1.time.compareTo(match2.time);
                        if(result1 < 0)          return -1;
                        else if(result1 > 0)     return 1;
                        return match1.id - match2.id;
                    }
                }
            });
            
            for(int i=0;i<Utils.matchVector.size();i++)
            {
                Match1 m = (Match1)Utils.matchVector.elementAt(i);
                sortingVector.addElement(m);
            }  
            for(int i=0;i<Utils.remainingmatchVector.size();i++)
            {
                Match1 m = (Match1)Utils.remainingmatchVector.elementAt(i);
                sortingVector.addElement(m);
            }       
            sortingVector.reSort();             
        }
        
        
        public void getWorldCupMatchesSortedByDateTime()
        {
            sortingVector.removeAllElements();
            sortingVector.setSortComparator(new Comparator() 
            {
                public int compare(Object o1, Object o2) { 
                
                    Match1 match1 = (Match1)o1;
                    Match1 match2 = (Match1)o2;
                    
                    int result = match1.date.compareTo(match2.date);
                    if(result < 0)          return -1;
                    else if(result > 0)     return 1;
                    else 
                    {
                        int result1 = match1.time.compareTo(match2.time);
                        if(result1 < 0)          return -1;
                        else if(result1 > 0)     return 1;
                        return match1.id - match2.id;
                    }
                }
            });            
            for(int i=0;i<Utils.matchVector.size();i++)
            {
                Match1 m = (Match1)Utils.matchVector.elementAt(i);
                sortingVector.addElement(m);
            }  
            for(int i=0;i<Utils.remainingmatchVector.size();i++)
            {
                Match1 m = (Match1)Utils.remainingmatchVector.elementAt(i);
                sortingVector.addElement(m);
            }       
            sortingVector.reSort();             
        }
        
        private Vector getMatchVectorByDate(String date)
        {
            Vector dateVector = new Vector();
            for(int i=0;i<sortingVector.size();i++)
            {
                Match1 m = (Match1)sortingVector.elementAt(i);
                if( m.date.equalsIgnoreCase(date) == true)
                    dateVector.addElement(m);
            }   
            return dateVector;
            
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
        
        Timer timer;
        public void startTimer() 
        {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTick(), 0, Utils.RELOAD_TIME);
            timerStarted = true;
        }
        private class TimerTick extends TimerTask 
        {
            public void run() {    
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        callMatchScoresFromServer();                        
                    }
                });
            }
        } 
        
        
        protected void makeMenu(Menu menu, int instance)
        {
            menu.add(backMenu);
            /*
            menu.add(reloadMenu);
            if(timerStarted)
                menu.add(stopMenu);
            else
                menu.add(startMenu);
            */
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
        private MenuItem reloadMenu = new MenuItem("Reload Scores", 100, 10)
        {
            public void run()
            {
                callMatchScoresFromServer();
            }
        };  
        private MenuItem startMenu = new MenuItem("Start Autoupdate Score", 100, 10)
        {
            public void run()
            {
                startTimer();
            }
        };
        
        private MenuItem stopMenu = new MenuItem("Stop Autoupdate Score", 100, 10)
        {
            public void run()
            {
                stopTimer();
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
        
        private void stopTimer()
        {
            if(timer != null)
                timer.cancel();
            timerStarted = false;
        }
        private void backToPreviousScreen()
        {
            stopTimer();
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
            
            if(buttonId == MatchTodayScreen.BUTTON_ID_OK)
            {
                selectedIndex = choiceFieldDate.getSelectedIndex();
                currentDate = dates[selectedIndex];
                initialize();
            }
            else
            {
                Match1 match = Utils.getMatchFromID(buttonId);
                MatchScreen.callMatchDetailsScreen(match, ScreenManager.MATCH_DETAILS_SCREEN, screenIndex, thisClass);
            }
        }
        
        private int previousWidth, previousHeight;
        public void checkOrientation()
        {
            if(previousWidth != Utils.displayWidth || previousHeight != Utils.displayHeight)
                initialize();
            previousWidth = Utils.displayWidth;
            previousHeight = Utils.displayHeight;
        }
        
        private int SCORES_REQUEST_ID = 123458;
        public void callMatchScoresFromServer()
        {
            HttpRequestGet getRequest = new HttpRequestGet(this, Utils.SERVER_URL + "getRemainingMatchScores.php", null, null, SCORES_REQUEST_ID, null, false, "Please Wait...Loading PointTable"); 
        }
        
        public void responseReceived(final boolean isSuccessful, final String resultString,
                        final int responseId, Object object)
        {
            UiApplication.getUiApplication().invokeLater(new Runnable()
            {
                public void run()
                {   
                    try
                    {
                        String output = resultString.trim();
                        if(Utils.DEBUG) System.out.println(output);    
                        if (isSuccessful)
                        {
                            if (responseId == INITIAL_SCORE_REQUEST_ID)
                            {
                                Utils.SCORE_UPDATED = true;
                                callMatchScoresFromServer();
                            }
                            else if (responseId == SCORES_REQUEST_ID)
                            {
                                Parser.parseMatchScoresResposne(output);
                                initialize();
                            }
                                
                        }
                        else
                        {
                            if(Utils.DEBUG) System.out.println("ERROR: Please check your network connectivity.");
                            Dialog.alert("ERROR: Please check your network connectivity.");
                            initialize();
                        }
                    }
                    catch (Exception ex)
                    {
                        System.out.println("EXECEPTION");
                    }
                }
            }); 
        }
        
        private int INITIAL_SCORE_REQUEST_ID = 12318;
        public void callInitialScoresFromServer()
        {
            HttpRequestGet getRequest = new HttpRequestGet(this, Utils.SERVER_URL + "getInitialMatchScores.php", null, null, INITIAL_SCORE_REQUEST_ID, null, false, ""); 
        }
}


