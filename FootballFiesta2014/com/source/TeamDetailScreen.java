/*
 * TeamDetailScreen.java
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

import com.io.*;
import com.utils.*;
import com.api.ui.component.*;
import com.api.ui.custom.*;
import net.rim.device.api.util.SimpleSortingVector;
import net.rim.device.api.util.Comparator;
import java.util.Vector;
/*
@author Bikas
@Date 05-01-2014
*/


public class TeamDetailScreen extends MyMainScreen implements FieldChangeListener, ResponseListener
{

        private ScreenManager screenManager;
        public TeamDetailScreen thisClass;
        private int screenIndex = ScreenManager.TEAM_DETAILS_SCREEN;
        
        public TeamDetailScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            MatchScreen.setColWidth();
            //initialize();
        }   
        
        private static TeamDetailScreen teamDetailScreen = null;
        synchronized public static TeamDetailScreen getInstances()
        {
            if (teamDetailScreen == null)
                teamDetailScreen = new TeamDetailScreen();
            return teamDetailScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new TeamDetailScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        
        private Team1 team;
        public void setTeam(Team1 team)
        {
            this.team = team;
        }
        
        public void initialize()
        {
            this.deleteAll();              
                                       
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager(team.name);            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            
            vfmMain.add(new TranslatorField(Utils.displayWidth,5)); 
            vfmMain.add(getTeamTopManager(team, Utils.displayWidth));
            
            VerticalFieldManager vfmSubHeader1 = TeamScreen.getSubHeaderManager("Achievements");
            vfmMain.add(new TranslatorField(Utils.displayWidth,5)); 
            vfmMain.add(vfmSubHeader1);
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(StadiumDetailsScreen.getTextTitleField("Appearances:"));
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            vfmMain.add(StadiumDetailsScreen.getTextField(team.appearances+""));
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(StadiumDetailsScreen.getTextTitleField("Best Results:"));
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            vfmMain.add(StadiumDetailsScreen.getTextField(team.bestResults));
            
            VerticalFieldManager vfmSubHeader2 = TeamScreen.getSubHeaderManager("Key Players");
            vfmMain.add(new TranslatorField(Utils.displayWidth,5)); 
            vfmMain.add(vfmSubHeader2);
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));            
            for(int i=0;i<team.keyPlayers.size();i++)
            {         
                vfmMain.add(StadiumDetailsScreen.getTextField((String)team.keyPlayers.elementAt(i))); 
            }
            //MatchScreen.setColWidth();
            getWorldCupMatches(team);
            vfmMain.add(TeamScreen.getSubHeaderManager("Matches Played"));
            CustomLabelField lblTime = new CustomLabelField("Match times are shown in local time", Utils.fontHeightText-2, Utils.colorText);
            lblTime.setFont(Utils.getNormalFont( Utils.fontHeightText-2));
            lblTime.setMargin(0,0,0,10);
            vfmMain.add(lblTime);
            vfmMain.add(new TranslatorField(Utils.displayWidth, 1));
            for(int i=0;i<sortingVector.size();i++)
            {
                Match1 match = (Match1)sortingVector.elementAt(i);
                vfmMain.add(MatchScreen.getMatchManager(match, true, thisClass));
                vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
            }
            
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }       
        
      
        private VerticalFieldManager getTeamTopManager(Team1 team, int managerWidth)
        {
            VerticalFieldManager vfm = new VerticalFieldManager();
            
            BitmapField imageField = new BitmapField(Bitmap.getBitmapResource(team.flagImageName), DrawStyle.HCENTER | BitmapField.USE_ALL_WIDTH);
            vfm.add(imageField);
            
            int countryFontSize = Utils.fontHeightText;
            int strLength = Utils.getStringLength(team.name, Utils.getBoldFont(countryFontSize));
            while(strLength >= managerWidth)
            {
                countryFontSize--;
                strLength = Utils.getStringLength(team.name, Utils.getBoldFont(countryFontSize));
            }
            CustomRichTextField lblCountry = StadiumDetailsScreen.getRichTextField(team.name, countryFontSize, Utils.colorTeamName);
            lblCountry.setFont(Utils.getBoldFont(countryFontSize));
            
            CustomRichTextField lblCountryNickName = StadiumDetailsScreen.getRichTextField("(" + team.nickName +")", countryFontSize, Utils.colorTeamName);
            lblCountryNickName.setFont(Utils.getBoldFont(countryFontSize));
            
            CustomRichTextField lblRegion = StadiumDetailsScreen.getRichTextField(team.region, Utils.fontHeightText, Utils.colorTeamCoach);
            lblRegion.setFont(Utils.getNormalFont(Utils.fontHeightText));
            
            CustomRichTextField lblRanking = StadiumDetailsScreen.getRichTextField("Ranking: " + team.rank, Utils.fontHeightText, Utils.colorTeamCoach);
            lblRanking.setFont(Utils.getNormalFont(Utils.fontHeightText));
            
            CustomRichTextField lblCoach = StadiumDetailsScreen.getRichTextField("Coach: " + team.coach, Utils.fontHeightText, Utils.colorTeamCoach);
            lblRanking.setFont(Utils.getNormalFont(Utils.fontHeightText));
            
            CustomRichTextField lblCaptain = StadiumDetailsScreen.getRichTextField("Captain: " + team.captain, Utils.fontHeightText, Utils.colorTeamCoach);
            lblCaptain.setFont(Utils.getNormalFont(Utils.fontHeightText));
            
            vfm.add(lblCountry);
            vfm.add(new TranslatorField(managerWidth,1));
            vfm.add(lblCountryNickName);
            vfm.add(new TranslatorField(managerWidth,1));
            vfm.add(lblRegion);
            vfm.add(new TranslatorField(managerWidth,1));
            vfm.add(lblRanking);
            vfm.add(new TranslatorField(managerWidth,1));
            vfm.add(lblCoach);
            vfm.add(new TranslatorField(managerWidth,1));
            vfm.add(lblCaptain);
            
            return vfm;            
        }
        
        protected void makeMenu(Menu menu, int instance)
        {
            //menu.add(reloadMenu);
            menu.add(backMenu);
            menu.add(increaseFontMenu);
            menu.add(decreaseFontMenu);
        }    
        
        private MenuItem reloadMenu = new MenuItem("Reload Scores", 100, 10)
        {
            public void run()
            {
                callMatchScoresFromServer();
            }
        };
            
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
            
            Match1 match = Utils.getMatchFromID(buttonId);
            MatchScreen.callMatchDetailsScreen(match, ScreenManager.MATCH_DETAILS_SCREEN, screenIndex, thisClass);
        }
        private int previousWidth, previousHeight;
        public void checkOrientation()
        {
            if(previousWidth != Utils.displayWidth || previousHeight != Utils.displayHeight)
                initialize();
            previousWidth = Utils.displayWidth;
            previousHeight = Utils.displayHeight;
        }
        
        SimpleSortingVector sortingVector = new SimpleSortingVector();
        public void getWorldCupMatches(Team1 team)
        {
            sortingVector.removeAllElements();
            sortingVector.setSortComparator(new Comparator() 
            {
                public int compare(Object o1, Object o2) { 
                
                    Match1 match1 = (Match1)o1;
                    Match1 match2 = (Match1)o2;
                    return match1.id - match2.id;
                    /*
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
                    */
                }
            });
            
            for(int i=0;i<Utils.matchVector.size();i++)
            {
                Match1 m = (Match1)Utils.matchVector.elementAt(i);
                if( m.team1Id.equalsIgnoreCase(team.id) == true || m.team2Id.equalsIgnoreCase(team.id) == true )
                    sortingVector.addElement(m);
            }  
            for(int i=0;i<Utils.remainingmatchVector.size();i++)
            {
                Match1 m = (Match1)Utils.remainingmatchVector.elementAt(i);
                if( m.team1Id.equalsIgnoreCase(team.id) == true || m.team2Id.equalsIgnoreCase(team.id) == true )
                    sortingVector.addElement(m);
            }       
            sortingVector.reSort();             
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
                            if (responseId == SCORES_REQUEST_ID)
                            {
                                Parser.parseMatchScoresResposne(output);
                                initialize();
                            }
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
}


