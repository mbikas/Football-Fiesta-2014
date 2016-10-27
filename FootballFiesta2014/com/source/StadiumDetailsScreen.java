/*
 * StadiumDetailsScreen.java
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
@Date 05-01-2014
*/


public class StadiumDetailsScreen extends MyMainScreen //implements FieldChangeListener
{

        private ScreenManager screenManager;
        public StadiumDetailsScreen thisClass;
        private int screenIndex = ScreenManager.STADIUM_DETAILS_SCREEN;
        
        public StadiumDetailsScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            //initialize();
        }   
        
        private static StadiumDetailsScreen stadiumDetailsScreen = null;
        synchronized public static StadiumDetailsScreen getInstances()
        {
            if (stadiumDetailsScreen == null)
                stadiumDetailsScreen = new StadiumDetailsScreen();
            return stadiumDetailsScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new StadiumDetailsScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        
        private Stadium1 stadium;
        public void setStadium(Stadium1 stadium)
        {
            this.stadium = stadium;
        }
        
        public void initialize()
        {
            this.deleteAll();
            
            
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager("Stadium");            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            
            Bitmap imageBig = Bitmap.getBitmapResource(stadium.imageNameBig);
            imageBig = adjustBigImageToDisplay(imageBig);
            BitmapField imageField = new BitmapField(imageBig, DrawStyle.HCENTER | BitmapField.USE_ALL_WIDTH);
            //imageField.setMargin(0,0,0,10);                
            //vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            //vfmMain.add(imageField);
            
            String titles[]  = {"Stadium Name:", "Stadium City:", "Established:","Capacity:", "Description:"};
            String values[] = {stadium.name, stadium.city, stadium.year, stadium.capacity+"", stadium.description};           
            for(int i=0;i<2;i++)
            {
                vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
                vfmMain.add(TeamScreen.getSubHeaderManager(titles[i]));
                vfmMain.add(getTextField(values[i]));                
            }
            
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(imageField);
            
            for(int i=2;i<titles.length;i++)
            {
                vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
                vfmMain.add(TeamScreen.getSubHeaderManager(titles[i]));
                vfmMain.add(getTextField(values[i]));                
            }
            
            //vfmMain.add(new NullField(Field.FOCUABLE));        
            
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }        
        
        public Bitmap adjustBigImageToDisplay(Bitmap imageBig)
        {
            int maxImageHeight = Utils.BODY_HEIGHT - 100;
            if(imageBig.getWidth() > Utils.displayWidth -10 || imageBig.getHeight() >  maxImageHeight)
            {   
                if(imageBig.getHeight() >  maxImageHeight) maxImageHeight = imageBig.getHeight();
                imageBig = Utils.resizeBitmap(imageBig, Utils.displayWidth-10, maxImageHeight);
            }
            return imageBig;
        }
        
        public static VerticalFieldManager getTextTitleField(String headerText, int fontSize, int textColor)
        {
            CustomLabelField lbl  = new CustomLabelField(headerText, fontSize, textColor);
            lbl.setFont(Utils.getBoldFont(fontSize));
            lbl.setMargin(0,0,0,10);
            
            VerticalFieldManager vfm = new VerticalFieldManager();
            vfm.add(lbl);
            return vfm; 
        }
        public static VerticalFieldManager getTextTitleField(String headerText)
        {
            return StadiumDetailsScreen.getTextTitleField(headerText, Utils.fontHeightText, Utils.colorTextTitle);
        }
        
        public static VerticalFieldManager getTextField(String text, int fontSize, int textColor)
        {            
            CustomRichTextField lbl  = new CustomRichTextField(text, textColor, RichTextField.FOCUSABLE);
            lbl.setFont(Utils.getNormalFont(fontSize));
            lbl.setMargin(0,0,0,10);
            
            VerticalFieldManager vfm = new VerticalFieldManager();
            vfm.add(lbl);
            return vfm; 
        }
        
        public static CustomRichTextField getRichTextField(String text, int fontSize, int textColor)
        {            
            CustomRichTextField lbl  = new CustomRichTextField(text, textColor, RichTextField.FOCUSABLE);
            lbl.setFont(Utils.getNormalFont(fontSize));
            lbl.setMargin(0,0,0,10);
            return lbl; 
        }
        
        public static VerticalFieldManager getTextField(String text)
        {            
            return StadiumDetailsScreen.getTextField(text, Utils.fontHeightText, Utils.colorText);            
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
