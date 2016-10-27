/*
 * TicketScreen.java
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

/*
@author Bikas
@Date 30-01-2014
*/


public class TicketScreen extends MyMainScreen implements FieldChangeListener
{
        private ScreenManager screenManager;
        public TicketScreen thisClass;
        private int screenIndex = ScreenManager.TICKET_SCREEN;
        
        public TicketScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            initialize();
        }   
        
        private static TicketScreen ticketScreen = null;
        synchronized public static TicketScreen getInstances()
        {
            if (ticketScreen == null)
                ticketScreen = new TicketScreen();
            return ticketScreen;
        }
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new TicketScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        
        public void initialize()
        {
            this.deleteAll();  
            
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager("Ticket Prices");            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            
            /////////////////////////
            //INDIVIDUAL MATCH TICKET PRICES -- INTERNATIONAL
            String str1[] = {"Opening Match(No1)", "Group Match(No2-48)", "Round of 16(No49-56)", "Quarter-Finals(No57-60)", "Semi-Finals(No61&62)","3rd Place Match(No63)","The Final(No64)"};
            String cat1[] = {"$495", "$175", "$220", "$330", "$660","$330","$990"};
            String cat2[] = {"$330", "$135", "$165", "$220", "$440","$220","$660"};
            String cat3[] = {"$220", "$90", "$110", "$165", "$275","$165","$440"};
            String dat1[] = {"$220", "$90", "$110", "$165", "$275","$165","$440"};            
            int colWidthFull[] = {Utils.displayWidth - 10};
            String temp1[] = {"Quarter-Finals(No57-60)X", "$999X", "$999X", "$999X", "$999X"};
            int colWidths1[] = getColWidth(temp1);
            VerticalFieldManager vfm = TeamScreen.getSubHeaderManager("INDIVIDUAL MATCH TICKET PRICES");
            vfmMain.add(vfm);
            String s2[] = {"For General International Public"};
            vfmMain.add(getInformationManager(Utils.getNormalFont(Utils.fontHeightText), colWidthFull, Utils.colorTeamCoach, s2));
            vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
            String s1[] = {"MATCH", "CAT1","CAT 2","CAT3","DAT"};
            vfmMain.add(getInformationManager(Utils.getNormalFont(fontHeight), colWidths1, Utils.colorTeamName, s1));
            vfmMain.add(new SpacerField(Utils.displayWidth-10, 2, Utils.colorSpacer));
            for(int i=0;i<str1.length;i++)
            {
                String s[] = {str1[i], cat1[i], cat2[i], cat3[i], dat1[i]};
                vfmMain.add(getInformationManager(Utils.getNormalFont(fontHeight), colWidths1, Utils.colorText, s));
                vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
            }
            String s3[] = {"Prices are in US Dollars + applicable Brazilian Taxes"};
            vfmMain.add(getInformationManager(Utils.getNormalFont(Utils.fontHeightText), colWidthFull, Utils.colorTeamCoach, s3));
            vfmMain.add(new SpacerField(Utils.displayWidth-10, 2, Utils.colorSpacer));            
            ///////////////////
            
            
            /////////////////////////
            //INDIVIDUAL MATCH TICKET PRICES -- DOMESTIC
            String cat11[] = {"R$990", "R$350", "R$440", "R$660", "R$1320","R$660","R$1980"};
            String cat21[] = {"R$660", "R$270", "R$330", "R$440", "R$880","R$440","R$1320"};
            String cat31[] = {"R$440", "R$180", "R$220", "R$330", "R$550","R$330","R$880"};
            String cat41[] = {"R$160", "R$60", "R$110", "R$170", "R$220","R$170","R$330"};
            String cat51[] = {"R$80", "R$30", "R$55", "R$85", "R$110","R$85","R$165"};
            String cat61[] = {"R$440", "R$180", "R$220", "R$330", "R$550","R$330","R$880"};
            
            String temp2[] = {"Quarter-Finals(No57-60)X", "R$1980X", "R$1980X", "R$1980X", "R$1980X","R$1980X","R$1980X"};
            int colWidths2[] = getColWidth(temp2);
            VerticalFieldManager vfm1 = TeamScreen.getSubHeaderManager("INDIVIDUAL MATCH TICKET PRICES");
            vfmMain.add(vfm1);
            String s21[] = {"For Domestic Public"};
            vfmMain.add(getInformationManager(Utils.getNormalFont(Utils.fontHeightText), colWidthFull, Utils.colorTeamCoach, s21));
            vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
            String s31[] = {"MATCH", "CAT1","CAT2","CAT3","CAT4","CAT4(DIS)","DAT"};
            vfmMain.add(getInformationManager(Utils.getNormalFont(fontHeight), colWidths2, Utils.colorTeamName, s31));
            vfmMain.add(new SpacerField(Utils.displayWidth-10, 2, Utils.colorSpacer));
            for(int i=0;i<str1.length;i++)
            {
                String s[] = {str1[i], cat11[i], cat21[i], cat31[i], cat41[i], cat51[i], cat61[i]};
                vfmMain.add(getInformationManager(Utils.getNormalFont(fontHeight), colWidths2, Utils.colorText, s));
                vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
            }
            String s41[] = {"Prices are in Brazilian Reais + applicable Brazilian Taxes"};
            vfmMain.add(getInformationManager(Utils.getNormalFont(Utils.fontHeightText), colWidthFull, Utils.colorTeamCoach, s41));
            vfmMain.add(new SpacerField(Utils.displayWidth-10, 2, Utils.colorSpacer));            
            ///////////////////
            
            /////////////////////////
            //VENUE SPECIFIC TICKET PRICES -- INTERNATIONAL
            String str12[] = {"Belo Horizonte", "Brasilia", "Cuiaba", "Curitiba", "Fortaleza","Manaus","Natal","Porto Alegre","Recife","Rio de Janeiro","Salvador","Sao Paulo"};
            String cat12[] = {"$920", "$1250", "$700", "$700", "$920","$700","$700","$920","$920","$920","$920","$745"};
            String cat22[] = {"$705", "$925", "$540", "$540", "$705","$540","$540","$705","$705","$705","$705","$570"};
            String cat32[] = {"$470", "$635", "$360", "$360", "$470","$360","$360","$470","$470","$470","$470","$380"};
            String cat42[] = {"$470", "$635", "$360", "$360", "$470","$360","$360","$470","$470","$470","$470","$380"};
            String temp3[] = {"Belo HorizonteX", "$920X", "$920X", "$920X", "$920X"};
            int colWidths3[] = getColWidth(temp3);
            VerticalFieldManager vfm12 = TeamScreen.getSubHeaderManager("VENUE SPECIFIC TICKET PRICES");
            vfmMain.add(new TranslatorField(Utils.displayWidth, 2));
            vfmMain.add(vfm12);
            String s22[] = {"For General International Public"};
            vfmMain.add(getInformationManager(Utils.getNormalFont(Utils.fontHeightText), colWidthFull, Utils.colorTeamCoach, s22));
            vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
            
            int colWidth22[] = {colWidths3[1]*2, Utils.displayWidth - (colWidths3[1]*2)};
            String s32[] = {"VENUE", "COST PER VENUE SPECIFIC TICKET"};
            vfmMain.add(getInformationManager(Utils.getNormalFont(fontHeight-5), colWidth22, Utils.colorTeamName, s32));
            String s42[] = {"", "CAT1","CAT2","CAT3","DAT"};
            vfmMain.add(getInformationManager(Utils.getNormalFont(fontHeight), colWidths3, Utils.colorTeamName, s42));
            vfmMain.add(new SpacerField(Utils.displayWidth-10, 2, Utils.colorSpacer));
            for(int i=0;i<str12.length;i++)
            {
                String s[] = {str12[i], cat12[i], cat22[i], cat32[i], cat42[i]};
                vfmMain.add(getInformationManager(Utils.getNormalFont(fontHeight), colWidths3, Utils.colorText, s));
                vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
            }
            String s52[] = {"Prices are in US Dollars + applicable Brazilian Taxes"};
            vfmMain.add(getInformationManager(Utils.getNormalFont(Utils.fontHeightText), colWidthFull, Utils.colorTeamCoach, s52));
            vfmMain.add(new SpacerField(Utils.displayWidth-10, 2, Utils.colorSpacer));            
            ///////////////////
            
            /////////////////////////
            //VENUE SPECIFIC TICKET PRICES -- DOMESTIC
            String cat13[] = {"R$1840", "R$2500", "R$1400", "R$1400", "R$1840","R$1400","R$1400","R$1840","R$1840","R$1840","R$1840","R$1490"};
            String cat23[] = {"R$1410", "R$1850", "R$1080", "R$1080", "R$1410","R$1080","R$1080","R$1410","R$1410","R$1410","R$1410","R$1140"};
            String cat33[] = {"R$940", "R$1270", "R$720", "R$720", "R$940","R$720","R$720","R$940","R$940","R$940","R$940","R$760"};
            String cat43[] = {"R$350", "R$520", "R$240", "R$240", "R$350","R$240","R$240","R$350","R$350","R$350","R$350","R$290"};
            String cat53[] = {"R$175", "R$260", "R$120", "R$120", "R$175","R$120","R$120","R$175","R$175","R$175","R$175","R$145"};
            String cat63[] = {"R$940", "R$1270", "R$720", "R$720", "R$940","R$720","R$720","R$940","R$940","R$940","R$940","R$760"};
            String temp4[] = {"Belo HorizonteX", "R$1890X", "R$1890X", "R$1890X", "R$1890X", "R$1890X", "R$1890X"};
            int colWidths4[] = getColWidth(temp4);
            vfmMain.add(new TranslatorField(Utils.displayWidth, 2));
            vfmMain.add( TeamScreen.getSubHeaderManager("VENUE SPECIFIC TICKET PRICES"));
            String s23[] = {"For Domestic Public"};
            vfmMain.add(getInformationManager(Utils.getNormalFont(Utils.fontHeightText), colWidthFull, Utils.colorTeamCoach, s23));
            vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
            
            int colWidth23[] = {colWidths4[1]*2, Utils.displayWidth - (colWidths4[1]*2)};
            String s33[] = {"VENUE", "COST PER VENUE SPECIFIC TICKET"};
            vfmMain.add(getInformationManager(Utils.getNormalFont(fontHeight), colWidth23, Utils.colorTeamName, s33));
            String s43[] = {"", "CAT1","CAT2","CAT3","CAT4","CAT4(DIS)","DAT"};
            vfmMain.add(getInformationManager(Utils.getNormalFont(fontHeight), colWidths4, Utils.colorTeamName, s43));
            vfmMain.add(new SpacerField(Utils.displayWidth-10, 2, Utils.colorSpacer));
            for(int i=0;i<str12.length;i++)
            {
                String s[] = {str12[i], cat13[i], cat23[i], cat33[i], cat43[i], cat53[i], cat63[i]};
                vfmMain.add(getInformationManager(Utils.getNormalFont(fontHeight), colWidths4, Utils.colorText, s));
                vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
            }
            String s53[] = {"Prices are in Brazilian Reais + applicable Brazilian Taxes"};
            vfmMain.add(getInformationManager(Utils.getNormalFont(Utils.fontHeightText), colWidthFull, Utils.colorTeamCoach, s53));
            vfmMain.add(new SpacerField(Utils.displayWidth-10, 2, Utils.colorSpacer));            
            ///////////////////
            
            
            String temp = "Please check the official website for more information.";
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(StadiumDetailsScreen.getTextField(temp));
            vfmMain.add(new TranslatorField(Utils.displayWidth, 10));
            vfmMain.add(new NullField(Field.FOCUSABLE));
                        
            /*
            HyperlinkButtonField btnFanGuide = getHyperLinkButton("Ticketing Fan Guide (pdf)", "pdf");
            HyperlinkButtonField btnMore = getHyperLinkButton("More Information", "more");
            HyperlinkButtonField btnApply = getHyperLinkButton("Apply for Tickets", "apply");
            //vfmMain.add(new TranslatorField(Utils.displayWidth, 5));
            //vfmMain.add(btnFanGuide);   
            vfmMain.add(new TranslatorField(Utils.displayWidth, 5));
            vfmMain.add(btnMore);
            vfmMain.add(new TranslatorField(Utils.displayWidth, 5));
            vfmMain.add(btnApply);
            vfmMain.add(new TranslatorField(Utils.displayWidth, 10));
            vfmMain.add(new NullField(Field.FOCUSABLE));
            */
            
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }
        
        private int fontHeight = Utils.fontHeightText+5;
        private int[] getColWidth(String[] str)
        {
            fontHeight = Utils.fontHeightText+5;
            int colWidths[];
            int stringLength = 0;
            for(int i=0;i<str.length;i++)
                stringLength += Utils.getStringLength(str[i], Utils.getNormalFont(fontHeight));
            stringLength += 10;
            
            while(stringLength > Utils.displayWidth)
            {
                fontHeight--; 
                stringLength = 0;
                for(int i=0;i<str.length;i++)
                    stringLength += Utils.getStringLength(str[i], Utils.getNormalFont(fontHeight));
                stringLength += 10;
                if(fontHeight <= Utils.fontHeightTextMinimum)break;
            }
            
            colWidths = new int [str.length];
            int length = 0;
            for(int i=1;i<str.length;i++)
            {
                stringLength = Utils.getStringLength(str[i], Utils.getNormalFont(fontHeight));
                colWidths[i] = stringLength;
                length += stringLength;                
            }
            colWidths[0] = Utils.displayWidth - length - 6;       
            return colWidths;       
        } 
               
        
        private HyperlinkButtonField getHyperLinkButton(String text, String buttonName)
        {
            HyperlinkButtonField btnApply = new HyperlinkButtonField(text, Utils.colorTeamName, Utils.colorTextFocus, Utils.bgColor, 0, 0);
            btnApply.setFont(Utils.getBoldFont(Utils.fontHeightText));
            btnApply.setChangeListener(this);
            btnApply.setButtonName(buttonName);
            return btnApply;
        }
        
        public CustomGridFieldManager getInformationManager(Font font, int colWidths[], int fontColor, String s[])
        {
            CustomGridFieldManager grid = new CustomGridFieldManager(colWidths, 0);
            
            long style =  RichTextField.USE_TEXT_WIDTH | DrawStyle.HCENTER | RichTextField.FOCUSABLE;
            CustomRichTextField lblTitle = new CustomRichTextField(s[0], fontColor, style);
            lblTitle.setFont(font);
            grid.add(lblTitle);
            
            for(int i=1;i<s.length;i++)
            {
                CustomLabelField lbl = new CustomLabelField(s[i], Utils.fontHeightText, fontColor);
                lbl.setFont(font);
                grid.add(lbl);
            }
            return grid;            
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
            
            /*
            if(text.equalsIgnoreCase("more")==true)
            {
                Utils.openBrowser("http://www.fifa.com/worldcup/organisation/ticketing/prices-matches/index.html");
            }
            else if(text.equalsIgnoreCase("pdf")==true)
            {
                String pdfUrl = "http://www.fifa.com/mm/document/tournament/ticketing/02/15/43/02/2014fwc_ticketingfanguide_en_update_neutral.pdf";
                String url = "http://docs.google.com/gview?embedded=true&url="+pdfUrl;                
                //add(new BrowserField(url));
                Utils.openBrowser(url);
            }
            else if(text.equalsIgnoreCase("apply")==true)
            {
                Utils.openBrowser("http://www.fifa.com/worldcup/organisation/ticketing/apply-for-tickets/index.html");
            }
            */
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


