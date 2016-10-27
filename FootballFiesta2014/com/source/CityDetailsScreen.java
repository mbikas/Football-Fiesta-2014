/*
 * CityDetailsScreen.java
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


public class CityDetailsScreen extends MyMainScreen //implements FieldChangeListener
{

        private ScreenManager screenManager;
        public CityDetailsScreen thisClass;
        private int screenIndex = ScreenManager.CITY_DETAILS_SCREEN;
        
        public CityDetailsScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            //initialize();
        }   
        
        private static CityDetailsScreen cityDetailsScreen = null;
        synchronized public static CityDetailsScreen getInstances()
        {
            if (cityDetailsScreen == null)
                cityDetailsScreen = new CityDetailsScreen();
            return cityDetailsScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new CityDetailsScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        
        private City1 city;
        public void setCity(City1 city)
        {
            this.city = city;
        }
        
        public void initialize()
        {
            this.deleteAll();
            
            
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager("City");            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            
            Bitmap imageBig = Bitmap.getBitmapResource(city.imageNameBig);
            imageBig = adjustBigImageToDisplay(imageBig);
            BitmapField imageField = new BitmapField(imageBig, DrawStyle.HCENTER | BitmapField.USE_ALL_WIDTH);
            
            String titles[]  = {"City Name:", "Stadium:", "Clubs:","History:", "Weather:"};
            String values[] = {city.name, city.stadium, "Clubs", city.history, city.weather};
            
            for(int i=0;i<1;i++)
            {
                vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
                vfmMain.add(TeamScreen.getSubHeaderManager(titles[i]));
                vfmMain.add(StadiumDetailsScreen.getTextField(values[i])); 
            } 
            
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(imageField);
                      
            for(int i=1;i<titles.length;i++)
            {
                if(values[i].equalsIgnoreCase("") == true)continue;
                vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
                vfmMain.add(TeamScreen.getSubHeaderManager(titles[i]));
                if(values[i].equalsIgnoreCase("Clubs") == true)
                {   
                    for(int j=0;j<city.clubVector.size();j++)
                    {         
                        String str = (String)city.clubVector.elementAt(j);
                        vfmMain.add(StadiumDetailsScreen.getTextField(str)); 
                        vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
                    }
                }
                else
                    vfmMain.add(StadiumDetailsScreen.getTextField(values[i])); 
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
