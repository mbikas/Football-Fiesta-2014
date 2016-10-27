/*
 * ScorerScreen.java
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
import net.rim.device.api.util.SimpleSortingVector;
import net.rim.device.api.util.Comparator;
import java.util.Vector;
import com.io.*;
import net.rim.device.api.i18n.*;
import java.util.*;


/*
@author Bikas
@Date 26-06-2014
*/


public class ScorerScreen extends MyMainScreen implements FieldChangeListener, ResponseListener
{

        private ScreenManager screenManager;
        public ScorerScreen thisClass;
        private int screenIndex = ScreenManager.SCORER_SCREEN;
        
        public ScorerScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            initialize(true);
        }   
        
        private static ScorerScreen scorerScreen = null;
        synchronized public static ScorerScreen getInstances()
        {
            if (scorerScreen == null)
                scorerScreen = new ScorerScreen();
            return scorerScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new ScorerScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        
        private int BUTTON_ID_TOP =10041;
        private int BUTTON_ID_ALL =10042;
        private int type = BUTTON_ID_TOP;
        String headerText = "Top Scorer";
        
        public void initialize(boolean showLoadingMessage)
        {
            this.deleteAll();  
            
            if(type == BUTTON_ID_TOP)
                headerText = "Top Scorer";
            else if(type == BUTTON_ID_ALL)
                headerText = "All Goals";
            
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager(headerText);            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            
            if(showLoadingMessage)
            {
                HorizontalFieldManager hfm11 = new HorizontalFieldManager();
                CustomLabelField lblLoading = new CustomLabelField("Please Wait...Loading Goals", Utils.fontHeightText, Color.RED, DrawStyle.HCENTER);
                lblLoading.setFont(Utils.getNormalFont(Utils.fontHeightText));
                hfm11.add(lblLoading);
                hfm11.setMargin(0,0,0,10);
                vfmMain.add(hfm11);
            }
            
            //vfmMain.add(new TranslatorField(Utils.displayWidth, 5));
            vfmMain.add(new SpacerField(Utils.displayWidth-5, 1, Utils.colorSpacer));
            vfmMain.add(getButtonManager(this.type));
            vfmMain.add(new SpacerField(Utils.displayWidth-5, 1, Utils.colorSpacer));
            vfmMain.add(new TranslatorField(Utils.displayWidth, 5));
            
            if(scorerVector.size() > 0)
            {
                vfmMain.add(getMatchDetailsManager());
                vfmMain.add(new TranslatorField(Utils.displayWidth, 10));
                vfmMain.add(StadiumDetailsScreen.getTextField("MP = Match Played"));
                vfmMain.add(new TranslatorField(Utils.displayWidth, 2));
                vfmMain.add(StadiumDetailsScreen.getTextField("GS = Goal Scored"));
                vfmMain.add(new TranslatorField(Utils.displayWidth, 2));
                vfmMain.add(StadiumDetailsScreen.getTextField("GA = Goal Assisted"));
                vfmMain.add(new TranslatorField(Utils.displayWidth, 2));
            }
            
            
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }
        
        
        public int fontHeightMatchText = Utils.fontHeightText;
        public int colWidths[];
        public void setColWidth(Scorer scorer)
        {
            fontHeightMatchText = Utils.fontHeightText;
            int playedWidth, scoredWidth, playerWidth, assisted;
            int usedWidth = 10;
            while (true)
            {
                playerWidth = Utils.getStringLength(scorer.playerName,  Utils.getNormalFont(fontHeightMatchText));
                playedWidth = Utils.getStringLength("MPX",  Utils.getNormalFont(fontHeightMatchText));
                scoredWidth = Utils.getStringLength("GSX",  Utils.getNormalFont(fontHeightMatchText));
                assisted = Utils.getStringLength("GAXX",  Utils.getNormalFont(fontHeightMatchText));
                usedWidth = 10;
                usedWidth += (playerWidth + playedWidth + scoredWidth + assisted);
                if(usedWidth <= Utils.displayWidth)break;
                fontHeightMatchText--;
            }
            
            playedWidth = Utils.getStringLength("MPX",  Utils.getNormalFont(fontHeightMatchText));
            scoredWidth = Utils.getStringLength("GSX",  Utils.getNormalFont(fontHeightMatchText));
            assisted = Utils.getStringLength("GAX",  Utils.getNormalFont(fontHeightMatchText));
            usedWidth = 10 + playedWidth + scoredWidth + assisted;
            playerWidth = Utils.displayWidth - usedWidth;
            
            colWidths = new int [4];
            colWidths[0] = playerWidth;
            colWidths[1] = playedWidth;
            colWidths[2] = scoredWidth;
            colWidths[3] = assisted;
        }
        
        private Vector scorerVector = new Vector();
        private VerticalFieldManager getMatchDetailsManager()
        {
            VerticalFieldManager vfm = new VerticalFieldManager();
            
            TableButtonField btnTableTop = new TableButtonField("dummy", Utils.getBoldFont(fontHeightMatchText), true);
            btnTableTop.setWidths(colWidths[0], colWidths[1], colWidths[2], colWidths[3]);
            btnTableTop.setBackgroundColorOnFocus(TableButtonField.DEFAULT_BACKGROUND_COLOR_NORMAL);
            btnTableTop.setButtonPosition(TableButtonField.ONLY_ONE);
            btnTableTop.setLabels("Players", "MP", "GS", "GA");
            vfm.add(btnTableTop);
            
            int size = scorerVector.size();
            if (size == 0)
            {
                //vfmMain.add(StadiumDetailsScreen.getTextField("The game has not been played yet."));
            }
            for (int i=0;i<size;i++)
            {
                Scorer scorer = (Scorer) scorerVector.elementAt(i);
                
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
                btnTable.setLabels(scorer.playerName , scorer.matchPlayed+"", scorer.goalScored+"", scorer.assisted+"");
                vfm.add(btnTable);
            }//end of details loop
            
            return vfm;
        } 
        
        //////////////////////////
        
        public HorizontalFieldManager getButtonManager(int type)
        {   
            int fontHeight = Utils.fontHeightText;
            VerticalFieldManager vfm1, vfm2, vfm4;
            int usedWidth = 0;
            //CustomLabelField lbl;
            while (true)
            {
                //lbl = new CustomLabelField("Sort By : ", fontHeight, Utils.colorText);
                //lbl.setFont(Utils.getBoldFont(fontHeight));
                
                vfm1 = getHyperLinkButton("TOP SCORER", BUTTON_ID_TOP, fontHeight);
                vfm2 = getHyperLinkButton("ALL GOALS", BUTTON_ID_ALL, fontHeight);
                
                usedWidth = vfm1.getPreferredWidth()+vfm2.getPreferredWidth()+10;
                if(usedWidth < (Utils.displayWidth -20))break;
                fontHeight--;
            }
            
            HorizontalFieldManager hfmButton = new HorizontalFieldManager();
            //hfmButton.add(lbl);
            hfmButton.add(vfm1);hfmButton.add(vfm2);
            
            hfmButton.setMargin(0,0,0,Utils.displayWidth - usedWidth);
            return hfmButton;            
        }
        
        public VerticalFieldManager getHyperLinkButton(String text, int buttonID, int fontHeight)
        {
            VerticalFieldManager vfm = new VerticalFieldManager();
            
            if(type == buttonID)
            {
                CustomLabelField lbl = new CustomLabelField(text, fontHeight, Utils.colorTextFocus);
                lbl.setFont(Utils.getBoldFont(fontHeight));
                lbl.setMargin(0,0,0,10);
                vfm.add(lbl);
            }
            else
            {
                HyperlinkButtonField btnMatchByGroup = new HyperlinkButtonField(text, Utils.colorText, Utils.colorTextFocus, Utils.bgColor, 0, 0);
                btnMatchByGroup.setButtonId(buttonID);
                btnMatchByGroup.setFont(Utils.getUnderlinedBoldFont(fontHeight));
                btnMatchByGroup.setChangeListener(this);
                btnMatchByGroup.setMargin(0,0,0,10);
                vfm.add(btnMatchByGroup);
            }
            
            return vfm;
        }
        
        protected void makeMenu(Menu menu, int instance)
        {
            menu.add(backMenu);
            //menu.add(reloadMenu);
            menu.add(increaseFontMenu);
            menu.add(decreaseFontMenu);
        }     
        
        private void reload()
        {
            initialize(true);
            callGoalsFromServer();
        }
        
        private MenuItem backMenu = new MenuItem(Utils.BACK_TEXT, 100, 10)
        {
            public void run()
            {
                backToPreviousScreen();
            }
        };
        
        private MenuItem reloadMenu = new MenuItem("Reload Scorer", 100, 10)
        {
            public void run()
            {
                reload();
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
            
            if(buttonId == BUTTON_ID_TOP)
            {
                this.type = BUTTON_ID_TOP;
                initialize(true);
            }
            else if(buttonId == BUTTON_ID_ALL)
            {
                this.type = BUTTON_ID_ALL;
                initialize(true); 
            }
            
            //callGoalsFromServer();
            
            parseHarcodedGoals();
        }
        
        private int previousWidth, previousHeight;
        public void checkOrientation()
        {
            if(previousWidth != Utils.displayWidth || previousHeight != Utils.displayHeight)
                initialize(false);
            previousWidth = Utils.displayWidth;
            previousHeight = Utils.displayHeight;
        }
        
        
        private int TOP_REQUEST_ID = 13258;
        private int GOAL_REQUEST_ID = 13258;
        public void callGoalsFromServer()
        {
            String[] keys = {"all"};
            String[] values = {1+""};
            
            if(type == BUTTON_ID_ALL)
            {
                HttpRequestGet getRequest = new HttpRequestGet(this, Utils.SERVER_URL + "getTopScorer.php", keys, values, GOAL_REQUEST_ID, null, false, "Please Wait...Loading PointTable"); 
            } 
            else
            {
                HttpRequestGet getRequest = new HttpRequestGet(this, Utils.SERVER_URL + "getTopScorer.php", null, null, TOP_REQUEST_ID, null, false, "Please Wait...Loading PointTable"); 
            }
        }
        
        public void parseHarcodedGoals()
        {
            String topGoal = "~James RODRIGUEZ(COL)~5~6~2~@";
            String allGoal = "~James RODRIGUEZ(COL)~5~6~2~@Thomas MUELLER(GER)~7~5~3~@NEYMAR(BRA)~5~4~1~@Lionel MESSI(ARG)~7~4~1~@Robin VAN PERSIE(NED)~6~4~0~@Karim BENZEMA~5~3~2~@Andre SCHUERRLE~6~3~1~@Arjen ROBBEN~7~3~1~@Enner VALENCIA~3~3~0~@Xherdan SHAQIRI~4~3~0~@Toni KROOS~7~2~4~@Abdelmoumene DJABOU~3~2~1~@Ivan PERISIC~3~2~1~@GERVINHO~3~2~1~@Asamoah GYAN~3~2~1~@Memphis DEPAY~4~2~1~@Islam SLIMANI~4~2~1~@Alexis SANCHEZ~4~2~1~@OSCAR~7~2~1~@DAVID LUIZ~7~2~1~@Tim CAHILL~2~2~0~@Luis SUAREZ~2~2~0~@Mario MANDZUKIC~2~2~0~@Wilfried BONY~3~2~0~@Jackson MARTINEZ~3~2~0~@Andre AYEW~3~2~0~@Ahmed MUSA~4~2~0~@Clint DEMPSEY~4~2~0~@Miroslav KLOSE~5~2~0~@Bryan RUIZ~5~2~0~@Mario GOETZE~6~2~0~@Mats HUMMELS~6~2~0~@Juan CUADRADO~5~1~4~@Eduardo VARGAS~4~1~2~@Sofiane FEGHOULI~4~1~2~@Kevin DE BRUYNE~4~1~2~@Daley BLIND~7~1~2~@Klaas Jan HUNTELAAR~3~1~1~@LEE Keunho~3~1~1~@Yacine BRAHIMI~3~1~1~@Wayne ROONEY~3~1~1~@Miralem PJANIC~3~1~1~@Keisuke HONDA~3~1~1~@CRISTIANO RONALDO~3~1~1~@Oribe PERALTA~4~1~1~@Teofilo GUTIERREZ~4~1~1~@Mathieu VALBUENA~4~1~1~@Rafael MARQUEZ~4~1~1~@Edinson CAVANI~4~1~1~@Charles ARANGUIZ~4~1~1~@Georgios SAMARAS~4~1~1~@Olivier GIROUD~5~1~1~@Paul POGBA~5~1~1~@Sami KHEDIRA~5~1~1~@Joel CAMPBELL~5~1~1~@Thiago SILVA~6~1~1~@Wesley SNEIJDER~6~1~1~@Gonzalo HIGUAIN~7~1~1~@Mesut OEZIL~7~1~1~@Julian GREEN~1~1~0~@Leroy FER~1~1~0~@Juan MATA~1~1~0~@John BROOKS~1~1~0~@David VILLA~1~1~0~@Avdija VRSAJEVIC~1~1~0~@VARELA~2~1~0~@Jean BEAUSEJOUR~2~1~0~@Andreas SAMARIS~2~1~0~@Joel MATIP~2~1~0~@Blerim DZEMAILI~3~1~0~@Jorge VALDIVIA~3~1~0~@Juan QUINTERO~3~1~0~@Aleksandr KERZHAKOV~3~1~0~@Fernando TORRES~3~1~0~@Vedad IBISEVIC~3~1~0~@Xabi ALONSO~3~1~0~@Mario BALOTELLI~3~1~0~@Claudio MARCHISIO~3~1~0~@Carlo COSTLY~3~1~0~@Ivica OLIC~3~1~0~@SON Heungmin~3~1~0~@Shinji OKAZAKI~3~1~0~@Daniel STURRIDGE~3~1~0~@Edin DZEKO~3~1~0~@Alexander KOKORIN~3~1~0~@KOO Jacheol~3~1~0~@Mile JEDINAK~3~1~0~@Reza GHOOCHANNEJAD~3~1~0~@NANI~3~1~0~@Javier HERNANDEZ~4~1~0~@Marcos URENA~4~1~0~@Haris SEFEROVIC~4~1~0~@Romelu LUKAKU~4~1~0~@Moussa SISSOKO~4~1~0~@Peter ODEMWINGIE~4~1~0~@Giovani DOS SANTOS~4~1~0~@Granit XHAKA~4~1~0~@Divock ORIGI~5~1~0~@Dries MERTENS~5~1~0~@FERNANDINHO~5~1~0~@";
            if(type == BUTTON_ID_ALL)
                parseGoalResposne(allGoal);
            else
                parseGoalResposne(topGoal);
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
                        //UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
                        if (isSuccessful)
                        {
                            if (responseId == GOAL_REQUEST_ID)
                            {
                                parseGoalResposne(output);
                            }
                            else if (responseId == TOP_REQUEST_ID)
                            {
                                parseGoalResposne(output);
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
        
        private void parseGoalResposne(String output)
        {
            scorerVector.removeAllElements();
            int playerWidthMax = 0;
            
            Scorer maxScorer = new Scorer();
            
            //~BRA~CRO~3~1~27;Neymar;y@ 29;Neymar;g@ 71;Neymar;pen@ 88;Luiz Gustavo;y@ 90;Oscar;g@~11;Marcelo;wg@ 66;Vedran Corluka;y@ 69;Dejan Lovren;y@~~
            String values[] = Utils.split(output, "@");
            
            for(int i=0;i<values.length;i++)
            {
                String g[] = Utils.split(values[i],"~");
                int startIndex = 0;
                if(g.length > 4)startIndex++;
                Scorer s = new Scorer();
                s.playerName = g[startIndex++];
                s.matchPlayed = Integer.parseInt(g[startIndex++]);
                s.goalScored = Integer.parseInt(g[startIndex++]);
                s.assisted = Integer.parseInt(g[startIndex++]);
                scorerVector.addElement(s);
                
                int pW = Utils.getStringLength(s.playerName, Utils.getNormalFont(Utils.fontHeightText));
                if(pW > playerWidthMax)
                {
                    maxScorer = s;
                    playerWidthMax = pW;
                }
            }
            
            setColWidth(maxScorer);
            initialize(false);
        }
        
        class Scorer
        {
            public String playerName;
            public int matchPlayed;
            public int goalScored;
            public int assisted;
            Scorer(){}
        }
}


