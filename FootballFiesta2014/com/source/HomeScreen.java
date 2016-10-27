package com.source;

import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import java.io.EOFException;
import java.io.InputStream;
import net.rim.device.api.util.StringUtilities;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Background;

import com.io.*;
import com.utils.*;
import com.api.ui.component.*;
import com.api.ui.custom.*;

import java.util.Vector;

import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;

/*
@author Bikas
@Date 14-06-2013
*/

public class HomeScreen extends MainScreen implements FieldChangeListener, ResponseListener
{
        private ScreenManager screenManager;
        public HomeScreen thisClass;
        private int screenIndex = ScreenManager.HOME_SCREEN;
        
        public HomeScreen()
        {
            //super();
            super(NO_VERTICAL_SCROLL | USE_ALL_HEIGHT);
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            
            //callInitialScoresFromServer();
            updateMatchScoresFromFile();
            initialize();
        } 
        
        public void updateMatchScoresFromFile()
        {
            screenManager.mainScreenArray[ScreenManager.MATCH_SCREEN] = new MatchScreen();
            Parser parser = new Parser();
            parser.parseMatchScoresFromFile();
            //Parser.parseMatchScoresFromFile();
            Utils.SCORE_UPDATED = true;
        }
         
        public static void setImages()
        {
            Utils.bgColor = 0x454968;
            
            if(Utils.displayHeight > 480)
                Utils.HEADER_HEIGHT = 40;
            else
                Utils.HEADER_HEIGHT = 30;
            Utils.BODY_HEIGHT = Utils.displayHeight - Utils.HEADER_HEIGHT;
            //Bitmap header_image = Bitmap.getBitmapResource("header.png");              
            //Bitmap background_image = Bitmap.getBitmapResource("bg.png");              
            
            //480x360
            int widthHeightDiff1 = Math.abs(Utils.displayWidth - 480) + Math.abs(Utils.displayHeight - 360);
            //640x420
            int widthHeightDiff2 = Math.abs(Utils.displayWidth - 640) + Math.abs(Utils.displayHeight - 420);
            
            Bitmap header_image, background_image;
            if( widthHeightDiff1 < widthHeightDiff2)//480x360
            {
                header_image = Bitmap.getBitmapResource("480_header.jpg");              
                background_image = Bitmap.getBitmapResource("480.jpg"); 
            } 
            else
            {
                header_image = Bitmap.getBitmapResource("640_header.jpg");              
                background_image = Bitmap.getBitmapResource("640.jpg");              
            }        
            
            background_image = Utils.resizeBitmap(background_image, Utils.displayWidth, Utils.BODY_HEIGHT);
            Utils.headerImage = Utils.resizeBitmap(header_image, Utils.displayWidth, Utils.HEADER_HEIGHT);
            
            Utils.BACKGROUNG_WITH_COLOR = BackgroundFactory.createSolidBackground(Utils.bgColor);
            Utils.BACKGROUNG_WITH_IMAGE = BackgroundFactory.createBitmapBackground(background_image, 0, 0,
                         Background.REPEAT_INHERIT);
            
            Utils.DROP_DOWN_HEIGHT = 35;
            Utils.dropdownImageNormal = Utils.resizeBitmap(Bitmap.getBitmapResource("dropdown-box1.png"), Utils.DROP_DOWN_WIDTH, Utils.DROP_DOWN_HEIGHT);
            Utils.dropdownImageOver = Utils.resizeBitmap(Bitmap.getBitmapResource("dropdown-box-hover1.png"), Utils.DROP_DOWN_WIDTH, Utils.DROP_DOWN_HEIGHT);
            Utils.dropdownIcon = Bitmap.getBitmapResource("down.png");
            
            return;
        }
           
        private static HomeScreen homeScreen = null;
        synchronized public static HomeScreen getInstances()
        {
            if (homeScreen == null)
                homeScreen = new HomeScreen();
            return homeScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new HomeScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        
        VerticalScrollManager vfmMain;
        
        private Vector btnVector = new Vector();
        
        public static final int TOURNAMENT_SUMMARY = 0; 
        public static final int MATCHES_TODAY = 1;        
        public static final int MATCHES = 2;
        public static final int TOP_SCORER = 3;
        public static final int TEAMS = 4;        
        public static final int SQUADS = 5;
        public static final int POINT_TABLE = 6;
        public static final int STADIUMS = 7;
        public static final int CITIES = 8;
        public static final int HISTORY = 9;
        public static final int TICKET = 10;
        public static final int TELL_A_FRIEND = 11;
        
        
        public static String[] btnNames = {"Tournament Summary", "Matches By Date", "All Matches", "Top Scorer", "Teams/Groups", "Squads", "Point Table", "Stadiums","Hosting Cities",
                                              "History", "Ticket Prices", "Tell a Friend"};
        
        
        public void initialize()
        {
            this.deleteAll();  

            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManagerHome(Utils.APPLICATION_NAME);            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            //vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            vfmMain.add(new TranslatorField(Utils.displayWidth,5));
            
            VerticalFieldManager buttonManager = new VerticalFieldManager();
            int gapBetweenButton = Utils.getVerticalMeasurement(2);
            int buttonHeight = calculateHomeScreenButtonHeight(gapBetweenButton, btnNames[0] );
            //int buttonHeight = Utils.fontHeightButton;
            int btnWidth = getCustomButton("Tournament Summary", -20, buttonHeight).getPreferredWidth();
            for(int i=0;i<btnNames.length;i++)
            {
                CustomButtonField btn = getCustomButton(btnNames[i], i, buttonHeight);                
                btn.setWidth(btnWidth);
                buttonManager.add( getHorizontalButtonManager(btn)); 
                buttonManager.add(new TranslatorField(Utils.displayWidth,gapBetweenButton)); 
            }
            
            int remainingHeight = Utils.BODY_HEIGHT - buttonManager.getPreferredHeight() - 5;
            if(remainingHeight > 0)
                vfmMain.add(new TranslatorField(Utils.displayWidth,remainingHeight/2)); 
            vfmMain.add(buttonManager);
            
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }        
        
        private int calculateHomeScreenButtonHeight(int gap, String maxButtonText)
        {
            int minFontHeight = Utils.getVerticalMeasurement(20);
            try
            {
                int fontHeight = Utils.fontHeightButton;
                int btnHeight = getCustomButton(maxButtonText, -100, fontHeight).getPreferredHeight();
                int btnWidth = getCustomButton(maxButtonText, -100, fontHeight).getPreferredWidth();
                int usedHeight = (btnNames.length * (btnHeight + gap)) + 10;
                if(usedHeight <= Utils.BODY_HEIGHT) return fontHeight;
                int p = 0;
                while(usedHeight > Utils.BODY_HEIGHT)
                {
                    p++;
                    if(usedHeight <= Utils.BODY_HEIGHT || fontHeight <= minFontHeight)break;
                    fontHeight--;
                    btnHeight = getCustomButton(maxButtonText, -100, fontHeight).getPreferredHeight();
                    usedHeight = (btnNames.length * (btnHeight + gap)) + 10;         
                    if(p>5)break;
                } 
                return fontHeight;
            }
            catch(Exception e)
            {
                return minFontHeight;
            }
        }
        
        private HorizontalFieldManager getHorizontalButtonManager(CustomButtonField btn)
        {
            HorizontalFieldManager hfm = new HorizontalFieldManager(HorizontalFieldManager.USE_ALL_WIDTH);
            hfm.add(btn);
            hfm.setMargin(0,0,0,(Utils.displayWidth-btn.getPreferredWidth())/2);
            return hfm;
        }
         
        private CustomButtonField getCustomButton(String label, int buttonId, int fontHeight)
        {
            CustomButtonField button = new CustomButtonField(Utils.buttonImageNormnal, Utils.buttonImageOver, null, label, false , Utils.getNormalFont(fontHeight));
            if(buttonId >= 0)
            {
                button.setChangeListener(this);
                button.setButtonId(buttonId);
            }
            button.setAlignment(CustomButtonField.ALIGNMENT_HORIZONTAL_CENTER);       
            return button;             
        } 
        
        private CustomButtonField getCustomButton(String label, int buttonId)
        {
            CustomButtonField button = new CustomButtonField(Utils.buttonImageNormnal, Utils.buttonImageOver, null, label, false , Utils.fontButton);
            button.setChangeListener(this);
            button.setButtonId(buttonId);
            button.setAlignment(CustomButtonField.ALIGNMENT_HORIZONTAL_CENTER);       
            return button;             
        }  
        
        
        protected void makeMenu(Menu menu, int instance)
        {
            menu.add(exitMenu);
            menu.add(statMenu);
            menu.add(contactMenu);
            menu.add(increaseFontMenu);
            menu.add(decreaseFontMenu);
        }        
      
        private MenuItem exitMenu = new MenuItem(Utils.EXIT_APPLICATION_TEXT, 100, 10)
        {
                public void run()
                {
                    promptClose();
                }
        };
        
        private MenuItem statMenu = new MenuItem("Statistics (Wiki Link)", 100, 10)
        {
                public void run()
                {
                    Utils.openBrowser("http://en.wikipedia.org/wiki/2014_FIFA_World_Cup_statistics");
                }
        };
        
        private MenuItem contactMenu = new MenuItem("Contact Us", 100, 10)
        {
                public void run()
                {
                    UiApplication.getUiApplication().invokeLater(new Runnable()
                    {
                        String mailBody = "";
                        String mailSubject = "";
                        String to = "mbikas2@uic.edu";
                        public void run()
                        {
                            Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,
                                                    new MessageArguments(MessageArguments.ARG_NEW, to,
                                                                    mailSubject, mailBody));
                        }
                    });
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
            Utils.backToPreviousScreen(screenIndex, thisClass);
        }

        public boolean onClose()
        {
                promptClose();
                return true;
        }
        public void promptClose()
        {
                if (Dialog.ask(Dialog.D_YES_NO, Utils.EXIT_APPLICATION_TEXT) == Dialog.YES)
                {
                    if(Utils.FIRST_RUN)
                        Dialog.inform("Please wait...It may take some time to save the informations for the first time.(Only Once)");
                    Utils.saveXmlInformation();
                    System.exit(0);
                }
        }
        private void closeScreen()
        {
                this.close();
        }        
        
        /**
         * FieldChangeListener implementation. indicated what button was clicked.
         * buttons are distinguished by their label
         * net.rim.device.api.ui.FieldChangeListener
         */
         
        public void fieldChanged(Field field, int context)
        {
            String text = Utils.getButtonText(field);
            int buttonId = Utils.getButtonId(field);  
            
            if (buttonId == TOURNAMENT_SUMMARY)
            {
                TournamentSummaryScreen.getInstances().pushTheScreen(thisClass, screenIndex);
            } 
            else if (buttonId == TEAMS)
            {
                //TeamScreen.getInstances().pushTheScreen(thisClass, screenIndex);
                int nextIndex = ScreenManager.TEAM_SCREEN;
                if (screenManager.mainScreenArray[nextIndex] == null)
                    screenManager.mainScreenArray[nextIndex] = new TeamScreen();
                TeamScreen screen = (TeamScreen)screenManager.mainScreenArray[nextIndex];
                //screen.initialize();
                screenManager.previousScreenArray[nextIndex].push(screenIndex);
                UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
                closeScreen();
                
            } 
            else if (buttonId == SQUADS)
            {
                int nextIndex = ScreenManager.SQUAD_SCREEN;
                if (screenManager.mainScreenArray[nextIndex] == null)
                    screenManager.mainScreenArray[nextIndex] = new SquadScreen();
                SquadScreen screen = (SquadScreen)screenManager.mainScreenArray[nextIndex];
                //screen.initialize();
                screenManager.previousScreenArray[nextIndex].push(screenIndex);
                UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
                closeScreen();
                
            } 
            else if (buttonId == HomeScreen.MATCHES)
            {
                //MatchScreen.getInstances().pushTheScreen(thisClass, screenIndex);
                int nextIndex = ScreenManager.MATCH_SCREEN;
                if (screenManager.mainScreenArray[nextIndex] == null)
                    screenManager.mainScreenArray[nextIndex] = new MatchScreen();
                //MatchScreen screen = (MatchScreen)screenManager.mainScreenArray[nextIndex];
                //screen.initialize(true);
                screenManager.previousScreenArray[nextIndex].push(screenIndex);
                
                /*
                if(Utils.SCORE_UPDATED)
                    screen.callMatchScoresFromServer();
                else
                    screen.callInitialScoresFromServer();
                */
                
                UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
                closeScreen();
            } 
            else if (buttonId == HomeScreen.POINT_TABLE)//point table
            {
                //PointTableScreen.getInstances().pushTheScreen(thisClass, screenIndex);
                int nextIndex = ScreenManager.POINT_TABLE_SCREEN;
                if (screenManager.mainScreenArray[nextIndex] == null)
                    screenManager.mainScreenArray[nextIndex] = new PointTableScreen();
                PointTableScreen screen = (PointTableScreen)screenManager.mainScreenArray[nextIndex];
                screenManager.previousScreenArray[nextIndex].push(screenIndex);
                //screen.initialize(true);
                //screen.test();
                if(Utils.POINTTABLE_UPDATED == false)
                {
                    screen.getPointTableFromFile();
                    //screen.callPointTableFromServer();
                }
                UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
                closeScreen();
            } 
            else if (buttonId ==  HomeScreen.STADIUMS)
            {
                //StadiumScreen.getInstances().pushTheScreen(thisClass, screenIndex);
                int nextIndex = ScreenManager.STADIUM_SCREEN;
                if (screenManager.mainScreenArray[nextIndex] == null)
                    screenManager.mainScreenArray[nextIndex] = new StadiumScreen();
                StadiumScreen screen = (StadiumScreen)screenManager.mainScreenArray[nextIndex];
                //screen.initialize();
                screenManager.previousScreenArray[nextIndex].push(screenIndex);
                UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
                closeScreen();
            } 
            else if (buttonId ==  HomeScreen.CITIES)
            {
                //CityScreen.getInstances().pushTheScreen(thisClass, screenIndex);
                int nextIndex = ScreenManager.CITY_SCREEN;
                if (screenManager.mainScreenArray[nextIndex] == null)
                    screenManager.mainScreenArray[nextIndex] = new CityScreen();
                CityScreen screen = (CityScreen)screenManager.mainScreenArray[nextIndex];
                //screen.initialize();
                screenManager.previousScreenArray[nextIndex].push(screenIndex);
                UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
                closeScreen();
            }
            else if (buttonId ==  HomeScreen.TICKET)
            {
                //CityScreen.getInstances().pushTheScreen(thisClass, screenIndex);
                int nextIndex = ScreenManager.TICKET_SCREEN;
                if (screenManager.mainScreenArray[nextIndex] == null)
                    screenManager.mainScreenArray[nextIndex] = new TicketScreen();
                TicketScreen screen = (TicketScreen)screenManager.mainScreenArray[nextIndex];
                //screen.initialize();
                screenManager.previousScreenArray[nextIndex].push(screenIndex);
                UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
                closeScreen();
            }
            else if (buttonId == HomeScreen.HISTORY)
            {
                //HistoryScreen.getInstances().pushTheScreen(thisClass, screenIndex);
                int nextIndex = ScreenManager.HISTORY_SCREEN;
                if (screenManager.mainScreenArray[nextIndex] == null)
                    screenManager.mainScreenArray[nextIndex] = new HistoryScreen();
                HistoryScreen screen = (HistoryScreen)screenManager.mainScreenArray[nextIndex];
                //screen.initialize();
                screenManager.previousScreenArray[nextIndex].push(screenIndex);
                UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
                closeScreen();
                
            }
            else if (buttonId == HomeScreen.MATCHES_TODAY)
            {
                int nextIndex = ScreenManager.MATCH_TODAY_SCREEN;
                if (screenManager.mainScreenArray[nextIndex] == null)
                    screenManager.mainScreenArray[nextIndex] = new MatchTodayScreen();
                MatchTodayScreen screen = (MatchTodayScreen)screenManager.mainScreenArray[nextIndex];
                screenManager.previousScreenArray[nextIndex].push(screenIndex);
                
                /*
                if(Utils.SCORE_UPDATED)
                 {
                    screen.callMatchScoresFromServer();
                    screen.initialize();
                }
                else
                    screen.callInitialScoresFromServer();
                */
                UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
                closeScreen();
            }
            
            else if (buttonId == HomeScreen.TOP_SCORER)
            {
                int nextIndex = ScreenManager.SCORER_SCREEN;
                if (screenManager.mainScreenArray[nextIndex] == null)
                    screenManager.mainScreenArray[nextIndex] = new ScorerScreen();
                ScorerScreen screen = (ScorerScreen)screenManager.mainScreenArray[nextIndex];
                screenManager.previousScreenArray[nextIndex].push(screenIndex);
                
                //screen.callGoalsFromServer();
                screen.parseHarcodedGoals();
                
                //screen.initialize(true);
                UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
                closeScreen();
            }
            
            /*
            else if (buttonId == HomeScreen.COUNT_DOWN)
            {
                int nextIndex = ScreenManager.COUNTDOWN_SCREEN;
                if (screenManager.mainScreenArray[nextIndex] == null)
                    screenManager.mainScreenArray[nextIndex] = new CountDownScreen();
                CountDownScreen screen = (CountDownScreen)screenManager.mainScreenArray[nextIndex];
                screen.StartTimer();
                screen.initialize();
                screenManager.previousScreenArray[nextIndex].push(screenIndex);
                UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
                closeScreen();
            }
            */
            else if (buttonId == HomeScreen.TELL_A_FRIEND)
            {
                UiApplication.getUiApplication().invokeLater(new Runnable()
                {
                    String mailBody = Utils.APPLICATION_NAME + " gives you the latest updated information about the biggest Football Festival in Brazil, FIFA World Cup 2014. The application contains information about groups, details information about each participating national football teams. It have the fixtures and live updates of the whole football tournament and details of all the matches. It also includes information about the stadiums, hosting cities, ticket prices and the previous events history and many more.";
                    String mailSubject = "Check out "+ Utils.APPLICATION_NAME + ", you'll love it!";
                    public void run()
                    {
                         Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,
                                                new MessageArguments(MessageArguments.ARG_NEW, "",
                                                                mailSubject, mailBody));
                    }
                });
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
        
        private boolean changing = false;
        protected void sublayout(int width, int height)
        {
            if (Utils.displayWidth != Display.getWidth() || Utils.displayHeight != Display.getHeight())
            {
                Utils.displayWidth = Display.getWidth();
                Utils.displayHeight = Display.getHeight();
                //HomeScreen.setImages();           
                changing = true;
                Utils.orientationChanged();
                changing = false;
                initialize();
            }       
            super.sublayout(width, height); 
        }
        
        private int INITIAL_SCORE_REQUEST_ID = 12318;
        public void callInitialScoresFromServer()
        {
            if(isScoreAlreadCached() == false)
            {
                HttpRequestGet getRequest = new HttpRequestGet(this, Utils.SERVER_URL + "getInitialMatchScores.php", null, null, INITIAL_SCORE_REQUEST_ID, null, false, ""); 
            }
            else
            {
                System.out.println("Score Already Updated");
            }
        }
        
        boolean isScoreAlreadCached()
        {
            if(Utils.matchVector.size() > 0)
            {
                Match1 m = (Match1)Utils.matchVector.elementAt(0);
                if(m.team1_score > 0)
                {
                    Utils.SCORE_UPDATED = true;
                    return true;
                }
            }
            return false;
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
                                Parser.parseMatchScoresResposne(output);
                                Utils.SCORE_UPDATED = true;
                            }
                        }
                        else    System.out.println("ERROR: Not Successful");
                    }
                    catch (Exception ex)
                    {
                        //Dialog.alert("EXECEPTION");
                        System.out.println("EXECEPTION");
                    }
                }
            }); 
        }
}


