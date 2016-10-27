/*
 * MatchDetailsScreen.java
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

import net.rim.device.api.util.SimpleSortingVector;
import net.rim.device.api.util.Comparator;
import java.util.Vector;
import net.rim.device.api.util.SimpleSortingVector;
import net.rim.device.api.util.Comparator;


/*
@author Bikas
@Date 06-25-2014
*/

public class MatchDetailsScreen extends MyMainScreen implements FieldChangeListener, ResponseListener
{

        private ScreenManager screenManager;
        public MatchDetailsScreen thisClass;
        private int screenIndex = ScreenManager.MATCH_DETAILS_SCREEN;
        
        public MatchDetailsScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            //initialize();
        }   
        
        private static MatchDetailsScreen matchDetailScreen = null;
        synchronized public static MatchDetailsScreen getInstances()
        {
            if (matchDetailScreen == null)
                matchDetailScreen = new MatchDetailsScreen();
            return matchDetailScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new MatchDetailsScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        
        private Match1 match;
        private Team1 team1, team2;
        public void setMatch(Match1 match)
        {
            this.match = match;
            this.matchDetailsVector.removeAllElements();
            referee = "";
            isDataFound = false;
            team1 = Utils.getTeamNameFromId(match.team1Id);
            team2 = Utils.getTeamNameFromId(match.team2Id);
        }
        
        //Team1 team;
        
        public void initialize(boolean showLoadingMessage)
        {
            this.deleteAll();              
            
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager("Match Details");            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            
            if(showLoadingMessage)
            {
                HorizontalFieldManager hfm11 = new HorizontalFieldManager();
                CustomLabelField lblLoading = new CustomLabelField("Please Wait...Loading Match Details", Utils.fontHeightText, Color.RED, DrawStyle.HCENTER);
                lblLoading.setFont(Utils.getNormalFont(Utils.fontHeightText));
                hfm11.add(lblLoading);
                hfm11.setMargin(0,0,0,10);
                vfmMain.add(hfm11);
            }
                    
            MatchScreen.setColWidth();
            CustomLabelField lblTime = new CustomLabelField("Match times are shown in local time", Utils.fontHeightText-2, Utils.colorText);
            lblTime.setFont(Utils.getNormalFont( Utils.fontHeightText-2));
            lblTime.setMargin(0,0,0,10);
            vfmMain.add(lblTime);
            vfmMain.add(new TranslatorField(Utils.displayWidth, 1));
            vfmMain.add(MatchScreen.getMatchManager(match, true, false, thisClass));
            vfmMain.add(new SpacerField(Utils.displayWidth-10, 2, Utils.colorSpacer));
            
            if (isDataFound)
            {
                vfmMain.add(TeamScreen.getSubHeaderManager("Referee"));
                vfmMain.add(StadiumDetailsScreen.getTextField(referee));
                vfmMain.add(TeamScreen.getSubHeaderManager("Match Details"));
                vfmMain.add(getMatchDetailsManager());
                
                if(statVector.size() > 0)
                {
                    vfmMain.add(TeamScreen.getSubHeaderManager("Man of the Match"));
                    vfmMain.add(getManOftheMatchManager());
                    
                    vfmMain.add(TeamScreen.getSubHeaderManager("Statistics"));
                    vfmMain.add(getStatisticsManager());
                }
            }
                            
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }       
        
        private boolean isDataFound = false;
                
        public int fontHeightMatchText = Utils.fontHeightText;
        public int colWidths[];
        public void setColWidth()
        {
            fontHeightMatchText = Utils.fontHeightText;
            int timeWidth, scoreWidth, playerWidth;
            int usedWidth = 10;
            while (true)
            {
                timeWidth = Utils.getStringLength("X999X",  Utils.getNormalFont(fontHeightMatchText));
                scoreWidth = Utils.getStringLength("X[9-9]",  Utils.getNormalFont(fontHeightMatchText));
                playerWidth = Utils.getStringLength(playerNameMax,  Utils.getNormalFont(fontHeightMatchText));
                usedWidth = 10;
                usedWidth += (timeWidth + scoreWidth + playerWidth * 2) + 40;
                if(usedWidth <= Utils.displayWidth)break;
                fontHeightMatchText--;
            }
            timeWidth = Utils.getStringLength("X999X",  Utils.getNormalFont(fontHeightMatchText));
            scoreWidth = Utils.getStringLength("X[9-9]",  Utils.getNormalFont(fontHeightMatchText));
            usedWidth = 10 + timeWidth + scoreWidth;
            playerWidth = (Utils.displayWidth - usedWidth)/2;
            
            colWidths = new int [4];
            colWidths[0] = timeWidth;
            colWidths[1] = playerWidth;
            colWidths[2] = scoreWidth;
            colWidths[3] = playerWidth;
        }
        
        private VerticalFieldManager getMatchDetailsManager()
        {
            VerticalFieldManager vfm = new VerticalFieldManager();
            
            int goalTeam1 = 0;
            int goalTeam2 = 0;
            
            boolean isPenaltyPresent = false;
              
            int size = matchDetailsVector.size();
            int penaltyIndexStart = 0;
            if (size == 0)
            {
                //vfmMain.add(StadiumDetailsScreen.getTextField("The game has not been played yet."));
            }
            for (int i=0;i<size;i++)
            {
                MatchDetails m = (MatchDetails) matchDetailsVector.elementAt(i);
                if(m.minute >= 120 && m.isGoal == true)
                {
                    isPenaltyPresent = true;
                    penaltyIndexStart = i;
                    break;
                }
                
                TableButtonField btnTable = new TableButtonField("dummy", Utils.getNormalFont(fontHeightMatchText), true);
                btnTable.setWidths(colWidths[0], colWidths[1], colWidths[2], colWidths[3]);
                btnTable.setBackgroundColorOnFocus(TableButtonField.DEFAULT_BACKGROUND_COLOR_NORMAL);
                
                if (size == 1)
                    btnTable.setButtonPosition(TableButtonField.ONLY_ONE);
                else
                {
                    if (i == 0)
                        btnTable.setButtonPosition(TableButtonField.POSITION_FIRST);
                    else if (i == size - 1)
                        btnTable.setButtonPosition(TableButtonField.POSITION_LAST);
                }
                
                String leftPlayerName = "", rightPlayerName = "";                
                if(m.teamside == MatchDetails.TEAM_LEFT)
                {
                    btnTable.setLeftImage(m.image);
                    leftPlayerName = m.playerName;
                    rightPlayerName = "";
                    if(m.isGoal)goalTeam1++;
                }
                else if(m.teamside == MatchDetails.TEAM_RIGHT)
                {
                    btnTable.setRightImage(m.image);
                    leftPlayerName = "";
                    rightPlayerName = m.playerName;
                    if(m.isGoal)goalTeam2++;
                }
                String goalText = goalTeam1 + "-"+ goalTeam2;
                btnTable.setLabels(m.minute+"", leftPlayerName, goalText, rightPlayerName);
                
                vfm.add(btnTable);
            }//end of details loop
            
            if(isPenaltyPresent)
            {
                vfm.add(TeamScreen.getSubHeaderManager("Penalty Shoot-Out"));
                vfm.add(getPenaltyManager(penaltyIndexStart));
            }
            
            return vfm;
        } 
        
        
        private VerticalFieldManager getPenaltyManager(int penaltyIndexStart)
        {
            VerticalFieldManager vfm = new VerticalFieldManager();
            
            int goalTeam1 = 0;
            int goalTeam2 = 0;
            
            int size = matchDetailsVector.size();
            
            int leftSideGoalsCount = 0;
            int leftSideStartIndex = penaltyIndexStart;
            int rightSideStartIndex = 0;
            for(int i=penaltyIndexStart; i< size;i++)
            {
                 MatchDetails m = (MatchDetails) matchDetailsVector.elementAt(i);
                 if(m.teamside == MatchDetails.TEAM_RIGHT)
                 {
                     rightSideStartIndex = i;
                     break;
                 }
                 leftSideGoalsCount++;
            }
            int rightSideGoalsCount = size - rightSideStartIndex;
            int loopCounter = leftSideGoalsCount;
            if(rightSideGoalsCount > loopCounter) loopCounter = rightSideGoalsCount;
            
            int leftSideEndIndex = rightSideStartIndex;
            int rightSideEndIndex = size;
            
            while(loopCounter > 0)
            {
                TableButtonField btnTable = new TableButtonField("dummy", Utils.getNormalFont(fontHeightMatchText), true);
                btnTable.setWidths(colWidths[0], colWidths[1], colWidths[2], colWidths[3]);
                btnTable.setBackgroundColorOnFocus(TableButtonField.DEFAULT_BACKGROUND_COLOR_NORMAL);
                
                String leftPlayerName = "", rightPlayerName = "";
                int minute = 120;
                if(leftSideStartIndex < leftSideEndIndex)
                {
                    MatchDetails m1 = (MatchDetails) matchDetailsVector.elementAt(leftSideStartIndex);
                    btnTable.setLeftImage(m1.image);
                    leftPlayerName = m1.playerName;
                    if(m1.isGoal)goalTeam1++;
                    minute = m1.minute;
                    leftSideStartIndex++;
                }
                if(rightSideStartIndex < rightSideEndIndex)
                {   
                    MatchDetails m2 = (MatchDetails) matchDetailsVector.elementAt(rightSideStartIndex);
                    btnTable.setRightImage(m2.image);
                    rightPlayerName = m2.playerName;
                    if(m2.isGoal)goalTeam2++;
                    minute = m2.minute;
                    rightSideStartIndex++;
                }
                
                String goalText = goalTeam1 + "-"+ goalTeam2;
                btnTable.setLabels(minute+"", leftPlayerName, goalText, rightPlayerName);
                vfm.add(btnTable);
                loopCounter--;
            }
            return vfm;
        }
        
        private VerticalFieldManager getManOftheMatchManager()
        {
            VerticalFieldManager vfm = new VerticalFieldManager();
            
            int size = 1;
            
            String str = (String) statVector.elementAt(statVector.size()-1);
            String name = Utils.split(str, ":")[1];
                
            TableButtonField btnTable = new TableButtonField("dummy", Utils.getNormalFont(fontHeightMatchText), true);
            btnTable.setWidths(Utils.displayWidth - 10,0,0,0);
            btnTable.setBackgroundColorOnFocus(TableButtonField.DEFAULT_BACKGROUND_COLOR_NORMAL);
            btnTable.setAlignment(TableButtonField.ALIGNMENT_LEFT);
            btnTable.setButtonPosition(TableButtonField.ONLY_ONE);
            btnTable.setLabels(name,"","","");                
            vfm.add(btnTable);
            return vfm;
        }
        
        private VerticalFieldManager getStatisticsManager()
        {
            VerticalFieldManager vfm = new VerticalFieldManager();
            
            int size = statVector.size()-1;
            if (size == 0)
            {
                //vfmMain.add(StadiumDetailsScreen.getTextField("The game has not been played yet."));
            }
            for (int i=0;i<size-1;i++)
            {
                String str = (String) statVector.elementAt(i);
                
                TableButtonField btnTable = new TableButtonField("dummy", Utils.getNormalFont(fontHeightMatchText), true);
                btnTable.setWidths(Utils.displayWidth - 10,0,0,0);
                btnTable.setBackgroundColorOnFocus(TableButtonField.DEFAULT_BACKGROUND_COLOR_NORMAL);
                btnTable.setAlignment(TableButtonField.ALIGNMENT_LEFT);
                
                if (size == 1)
                    btnTable.setButtonPosition(TableButtonField.ONLY_ONE);
                else
                {
                    if (i == 0)
                        btnTable.setButtonPosition(TableButtonField.POSITION_FIRST);
                    else if (i == size - 1)
                        btnTable.setButtonPosition(TableButtonField.POSITION_LAST);
                }
                btnTable.setLabels(str,"","","");                
                vfm.add(btnTable);
            }//end of details loop
            
            return vfm;
        }
        
        
        Timer timer = null;
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
                        callMatchDetailsFromServer();                        
                    }
                });
            }
        }
        private boolean timerStarted = false;
        protected void makeMenu(Menu menu, int instance)
        {
            menu.add(backMenu);
            menu.add(reloadMenu);
            /*
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
                callMatchDetailsFromServer();
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
        }
        
        
        private int previousWidth, previousHeight;
        public void checkOrientation()
        {
            if(previousWidth != Utils.displayWidth || previousHeight != Utils.displayHeight)
                initialize();
            previousWidth = Utils.displayWidth;
            previousHeight = Utils.displayHeight;
        }
        
        private static int MATCH_DETAILS_REQUEST_ID = 123458;
        public void callMatchDetailsFromServer()
        {
            String[] keys = {"match_id"};
            String[] values = {match.id+""};
            HttpRequestGet getRequest = new HttpRequestGet(this, Utils.SERVER_URL + "getMatchDetailsById.php", keys, values, MATCH_DETAILS_REQUEST_ID, null, false, "Please Wait...Loading PointTable"); 
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
                            if (responseId == MATCH_DETAILS_REQUEST_ID)
                                parseMatchDetailsResposne(output);
                        }
                        else    System.out.println("ERROR: Not Successful");
                    }
                    catch (Exception ex)
                    {
                        System.out.println("EXECEPTION");
                    }
                }
            }); 
        }
        
        private String referee = "";
        private String statistics = "";
        private Vector statVector = new Vector();
        SimpleSortingVector matchDetailsVector = new SimpleSortingVector();
        private void parseMatchDetailsResposne(String output)
        {
            
            playerWidthMax = 0;
            
            //~BRA~CRO~3~1~27;Neymar;y@ 29;Neymar;g@ 71;Neymar;pen@ 88;Luiz Gustavo;y@ 90;Oscar;g@~11;Marcelo;wg@ 66;Vedran Corluka;y@ 69;Dejan Lovren;y@~~
            String values[] = Utils.split(output, "~");
            int startIndex = 0;
            int team1_score = 0;
            try
            {
                team1_score = Integer.parseInt(values[startIndex++]);
            }
            catch(Exception e)
            {
                team1_score = Integer.parseInt(values[startIndex++]);
            }
            //if(values[0][0] >='0' && values[0][0] <='9')
            //    startIndex = 0;
            //int team1_score = Integer.parseInt(values[startIndex++]);
            int team2_score = Integer.parseInt(values[startIndex++]);
            String team1_values = values[startIndex++];
            String team2_values = values[startIndex++];
            String extra = values[startIndex++]; 
            referee = values[startIndex++].trim(); 
            statistics = values[startIndex++].trim(); 
            match.team1_score = team1_score;
            match.team2_score = team2_score;
            match.extra = extra;  
            
            
            if(team1_values.equalsIgnoreCase("") == true && team2_values.equalsIgnoreCase("") == true)     
            {
                initialize(false);
                return;
            }    
            String team1_des[] = Utils.split(team1_values, "@");
            String team2_des[] = Utils.split(team2_values, "@");            
            matchDetailsVector.removeAllElements();            
            matchDetailsVector.setSortComparator(new Comparator() 
            {
                public int compare(Object o1, Object o2) { 
                
                    MatchDetails m1 = (MatchDetails)o1;
                    MatchDetails m2 = (MatchDetails)o2;
                    return m1.minute - m2.minute;
                }
            });
            addMatchDetailsToVector(team1_des, MatchDetails.TEAM_LEFT);
            addMatchDetailsToVector(team2_des, MatchDetails.TEAM_RIGHT);
            matchDetailsVector.reSort();
            
            statVector.removeAllElements();
            if(statistics != null && statistics.equalsIgnoreCase("") == false)
            {
                String stats[] = Utils.split(statistics, "@");
                for(int i=0;i<stats.length;i++)
                {
                    String strs[] = Utils.split(stats[i], ":");
                    if(stats.length <2) continue;
                    String val = strs[0].trim() + " : " + strs[1].trim();
                    statVector.addElement(val);
                }
            }
            
            setColWidth();
            isDataFound = true;
            initialize(false);
        }
        
        private void addMatchDetailsToVector(String[] team_description, int teamside)
        {
            
            for(int i=0;i<team_description.length;i++)
            {
                String mat[] = Utils.split(team_description[i], ";");
                if(mat.length <= 1) continue;
                MatchDetails m = new MatchDetails();
                m.minute = Integer.parseInt(mat[0].trim());
                m.playerName = mat[1].trim();
                m.goalText = mat[2].trim();
                m.isGoal = false;
                if(m.goalText.equalsIgnoreCase("g") == true)
                {
                    m.image = Utils.GOAL_IMAGE;
                    m.isGoal = true;
                }
                else if(m.goalText.equalsIgnoreCase("og") == true)
                {
                    m.playerName += "(og)";
                    m.image = Utils.GOAL_IMAGE;
                    m.isGoal = true;
                }
                else if(m.goalText.equalsIgnoreCase("pg") == true)
                {
                    if(m.minute < 120)
                        m.playerName += "(pen)";
                    m.image = Utils.GOAL_IMAGE;
                    m.isGoal = true;
                }
                else if(m.goalText.equalsIgnoreCase("pm") == true)
                {
                    if(m.minute < 120)
                        m.playerName += "(pen)";
                    m.image = Utils.PENALTY_MISS;
                }
                else if(m.goalText.equalsIgnoreCase("y") == true)
                    m.image = Utils.YELLOW_CARD_IMAGE;
                else if(m.goalText.equalsIgnoreCase("r") == true)
                    m.image = Utils.RED_CARD_IMAGE;
                else
                {
                    m.image = Utils.GOAL_IMAGE;
                    m.isGoal = true;
                }
                m.teamside = teamside;                  
                matchDetailsVector.addElement(m);
                int width = Utils.getStringLength(m.playerName,  Utils.getNormalFont(fontHeightMatchText));
                if(width > playerWidthMax)
                {
                    playerWidthMax = width;
                    playerNameMax = m.playerName;
                }
            }       
        }
        
        private String playerNameMax = "";
        private int playerWidthMax = 0;
}
