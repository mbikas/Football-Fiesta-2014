/*
 * TournamentSummaryScreen.java
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

import java.util.Vector;

/*
@author Bikas
@Date 12-07-2014
*/


public class TournamentSummaryScreen extends MyMainScreen implements FieldChangeListener
{

        private ScreenManager screenManager;
        public TournamentSummaryScreen thisClass;
        private int screenIndex = ScreenManager.TOURNAMENT_SUMMARY_SCREEN;
        
        public TournamentSummaryScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            initialize();
        }   
        
        private static TournamentSummaryScreen tournamentSummaryScreen = null;
        synchronized public static TournamentSummaryScreen getInstances()
        {
            if (tournamentSummaryScreen == null)
                tournamentSummaryScreen = new TournamentSummaryScreen();
            return tournamentSummaryScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new TournamentSummaryScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        private static final int finalMatchID = 64;
        public void initialize()
        {
            this.deleteAll();
                        
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager("Tournament Summary -- 2014");            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            
            
            VerticalFieldManager svfmSubHeader2 = TeamScreen.getSubHeaderManager("FOOTBALL FIESTA IN BRAZIL");
            vfmMain.add(svfmSubHeader2);
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(StadiumDetailsScreen.getTextField("June 12 to July 13, 2014"));
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            vfmMain.add(StadiumDetailsScreen.getTextField("32 Temas"));
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            vfmMain.add(new SpacerField(Utils.displayWidth, 1, Color.BLACK));
            
            
            String teamIds[] = {"GER", "ARG", "NED", "BRA"};
            String titless[] = {"Champion", "Runner-Up", "Third-Place", "Fourth-Place"};
           
            for(int i=0;i<teamIds.length;i++)
            {
                vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
                vfmMain.add(TeamScreen.getSubHeaderManager(titless[i]));
                vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
                Team1 team = Utils.getTeamNameFromId(teamIds[i]);
                HorizontalFieldManager hfmTeam = TeamScreen.getHorizontalManager(team.name, team, Utils.displayWidth, TeamScreen.BUTTON_ID_NAME, false, thisClass);
                vfmMain.add(hfmTeam);
            }
            
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(TeamScreen.getSubHeaderManager("Final Match"));
            CustomLabelField lblTime = new CustomLabelField("Match times are shown in local time", Utils.fontHeightText-2, Utils.colorText);
            lblTime.setFont(Utils.getNormalFont( Utils.fontHeightText-2));
            lblTime.setMargin(0,0,0,10);
            vfmMain.add(lblTime);
            vfmMain.add(new TranslatorField(Utils.displayWidth, 1));
            Match1 match = Utils.getMatchFromID(finalMatchID);
            vfmMain.add(MatchScreen.getMatchManager(match, false, thisClass));
            
            String titles[]  = {"adidas Golden Ball", "adidas Golden Boot", "adidas Golden Glove", "Hyundai Young Player Award","Fair Play Award"};
            String values[] = {"Lionel Messi (Argentina)", "James Rodriguez (Colombia)", "Manuel Neuer (Germany)", "Paul Pogba (France)",  "Colombia"};
            
            for(int i=0;i<titles.length;i++)
            {
                if(values[i].equalsIgnoreCase("null") == true)continue;
                vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
                vfmMain.add(TeamScreen.getSubHeaderManager(titles[i]));
                vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
                vfmMain.add(StadiumDetailsScreen.getTextField(values[i])); 
            } 
            
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
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
            
            Team1 team = Utils.getTeamNameFromId(text); 
            if(team == null)
            {
                Match1 match = Utils.getMatchFromID(finalMatchID);
                MatchScreen.callMatchDetailsScreen(match, ScreenManager.MATCH_DETAILS_SCREEN, screenIndex, thisClass);
            }      
            else
            {
                int nextIndex = ScreenManager.TEAM_DETAILS_SCREEN;
                if (screenManager.mainScreenArray[nextIndex] == null)
                    screenManager.mainScreenArray[nextIndex] = new TeamDetailScreen();                
                TeamDetailScreen screen = (TeamDetailScreen)screenManager.mainScreenArray[nextIndex];
                screen.setTeam(team);
                screen.initialize();
                screenManager.previousScreenArray[nextIndex].push(screenIndex);
                UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
                closeScreen();
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
}
