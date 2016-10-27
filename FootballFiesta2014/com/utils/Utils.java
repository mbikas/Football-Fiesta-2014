/*
 * Utils.java
 */

package com.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.io.*;

import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;
import net.rim.blackberry.api.invoke.CameraArguments;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;
import net.rim.blackberry.api.mail.ServiceConfiguration;
import net.rim.blackberry.api.mail.Session;
import net.rim.blackberry.api.mail.Store;
import net.rim.blackberry.api.phone.Phone;
import net.rim.device.api.math.Fixed32;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Background;
import java.io.IOException;

import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import java.util.Vector;

import com.api.ui.component.*;
import com.api.ui.custom.*;
import com.source.StartScreen;
import com.source.HomeScreen;

import net.rim.device.api.i18n.Locale;
import net.rim.device.api.i18n.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.source.*;
/**
 * @author Bikas this class will implements all the common definatins , methods,
 *         classes that will be used from anywhere and more than once
 * 
 */
public class Utils
{
        public static final boolean DEBUG = false;
        
        public static int displayWidth = Display.getWidth();//480
        public static int displayHeight = Display.getHeight();//360
        
        public static final int SCREEN_RES0LUTION_HORIZONTAL_BOLD = 9708;//9708;
        public static final int SCREEN_RES0LUTION_VERTICAL_BOLD = 9708;//8547;
        public static final int SCREEN_RESOLUTION_HORIZONTAL = Display.getHorizontalResolution(); 
        public static final int SCREEN_RESOLUTION_VERTICAL = Display.getVerticalResolution();
        
        public static boolean  SCORE_UPDATED = false;
        public static boolean  POINTTABLE_UPDATED = false;
        public static boolean  FIRST_RUN = false;
        
        //public static final Bitmap BACKGROUND_IMAGE = Bitmap.getBitmapResource("background.png");
        public static final Bitmap GOAL_IMAGE = Bitmap.getBitmapResource("goal.png");
        public static final Bitmap YELLOW_CARD_IMAGE = Bitmap.getBitmapResource("yellowcard.png");
        public static final Bitmap RED_CARD_IMAGE = Bitmap.getBitmapResource("redcard.png");
        public static final Bitmap PENALTY_MISS = Bitmap.getBitmapResource("pm.jpg");
        
        public static final int RELOAD_TIME = 1 * 60 * 1000;
                
        public static String statusMessage = "";
        public static String authenticationHash = "";
    
        public static final String SERVER_URL = "http://www.bikas.me/FootballFiesta/";
        //public static final String SERVER_URL = "http://127.0.0.1/FootballFiesta/";
        
        public static final String EXIT_APPLICATION_TEXT = "Exit Application?";
        public static final String APPLICATION_NAME = "Football Fiesta 2014";
        public static final String BACK_TEXT = "Back";
        
        //FootballFiesta2014 Constants
        public static int bgColor = 0x454968;
        public static int HEADER_HEIGHT = 50;
        public static int BODY_HEIGHT = 380;
        public static int TEXT_TOP_GAP = 5;
        public static int TEXT_MID_GAP = 2;
            
        
        public static int colorText = Color.WHITE;
        public static int colorTextTitle = 0x8AA679;
        public static int colorTextFocus = Color.RED;
        public static int colorTextHeader = 0x84DA86;
        public static int colorTextSubHeader = 0x546C95;
        public static int colorGroupBackground = 0x041824;
        
        public static int colorTeamName = 0xFDF79D;        
        public static int colorTeamCoach = 0xCAFD9D;
        public static int colorTeamTextTitle = Utils.colorTextTitle;
        public static int colorTeamText = Utils.colorText;
        public static int colorSpacer = Color.BLACK;
        public static int headerSpacerColor = Color.WHITE;
        public static int headerSpacerHeight = 0;
        
        
        public static Bitmap headerImage = null;
        public static Background BACKGROUNG_WITH_COLOR = null;
        public static Background BACKGROUNG_WITH_IMAGE = null;
        
        public static int fontHeightHeaderText = Utils.getVerticalMeasurement(26);
        public static int fontHeightText = Utils.getVerticalMeasurement(25);
        public static int fontHeightTextMinimum = Utils.getVerticalMeasurement(18);
        public static Font fontText = Utils.getNormalFont(fontHeightText);
        public static Font fontTextBold = Utils.getBoldFont(fontHeightText);
        public static int fontHeightButton = Utils.getVerticalMeasurement(25);
        public static Font fontButton = Utils.getNormalFont(fontHeightButton);
        
        //Vector for xml files
        public static Vector groupVector = new Vector();
        public static Vector teamVector = new Vector();
        public static Vector stadiumVector = new Vector();
        public static Vector cityVector = new Vector();
        public static Vector historyVector = new Vector();
        public static Vector matchVector = new Vector();
        public static Vector squadVector = new Vector();
        public static Vector pointTableVector = new Vector();
        public static Vector remainingmatchVector = new Vector();
        
        
        public static final int SPLASH_TIME = 500;
        public static boolean INITIALIZATION_COMPLETE = false;
        
        
        public static final int BUTTON_DEFAULT_SIZE = Utils.getVerticalMeasurement(20);
        public static int btnFontHeight = BUTTON_DEFAULT_SIZE;
        public static int btnWidth = 30;
        
        
       
        //////////screen constants////////////
        public static Background bg = null;
        
        public static Bitmap headerImageHome = null;
        public static Bitmap homeImage = null;
        public static Bitmap footerImage = null;
        public static Bitmap lineImage = null;
        public static int DROP_DOWN_WIDTH = Utils.displayWidth - 30;
        public static int BUTTON_HEIGHT = 35;
        public static int DROP_DOWN_HEIGHT = BUTTON_HEIGHT;
        public static Bitmap dropdownImageNormal = null;
        public static Bitmap dropdownImageOver = null;
        public static Bitmap dropdownIcon = null;
        
        public static int BUTTON_WIDTH_HOME = 100;
        public static int BUTTON_HEIGHT_HOME = 100;
        public static Bitmap buttonImageNormalHome = null;
        public static Bitmap buttonImageOverHome = null;
        
        public static int lineColor = 0xDEDEDE;        
        
        /////////////////////////////////
        
        //hyperlink button theme
        public static final int HyperlinkButtonTextColorNormal = Color.BLUE;
        public static final int HyperlinkButtonTextColorFocus = Color.BLUE;
        public static final int HyperlinkButtonBackground = Color.BLACK;
        
        public static final int MAX_THREADS_IN_THREAD_POOL = 3;
        public static EncodedImage buttonImageNormnal = EncodedImage.getEncodedImageResource("buttonDefault.png");
        public static EncodedImage buttonImageOver = EncodedImage.getEncodedImageResource("buttonOver.png");
        
        
        
        public static void decreaseFont()
        {
            fontHeightHeaderText--;
            fontHeightText--;
            fontText = Utils.getNormalFont(fontHeightText);
            fontTextBold = Utils.getBoldFont(fontHeightText);
            fontHeightButton--;
            fontButton = Utils.getNormalFont(fontHeightButton);  
                    
        }
        public static void increaseFont()
        {
            fontHeightHeaderText++;
            fontHeightText++;
            fontText = Utils.getNormalFont(fontHeightText);
            fontTextBold = Utils.getBoldFont(fontHeightText);
            fontHeightButton++;
            fontButton = Utils.getNormalFont(fontHeightButton);
        }
        
        
        //public static Location location;

        public Utils()
        {
        }
        
        public static HorizontalFieldManager getTopImageAndButtonManagerHome(String title)
        {
            HorizontalFieldManager imageManager = Utils.getImageManager(Utils.displayWidth, Utils.HEADER_HEIGHT, Utils.headerImage);
            Bitmap imageName = Bitmap.getBitmapResource("name.jpg");
            
            if(imageName.getHeight() > Utils.HEADER_HEIGHT-5 || imageName.getWidth() > Utils.displayWidth - 10)
            {
                int w = imageName.getWidth();
                if(w > Utils.displayWidth - 10) w =  Utils.displayWidth - 10;
                int h = imageName.getHeight();
                if(h > Utils.HEADER_HEIGHT - 5) h =  Utils.HEADER_HEIGHT - 5;
                imageName = Utils.resizeBitmap(imageName, w, h);
            }
            
            BitmapField lbl = new BitmapField(imageName);
            int leftMargin = (Utils.displayWidth - imageName.getWidth())/2;            
            int topMargin = (Utils.HEADER_HEIGHT - imageName.getHeight())/2;
            lbl.setMargin(topMargin, 0, 0, leftMargin);
            HorizontalFieldManager labelManager = new HorizontalFieldManager();
            labelManager.add(lbl);
            imageManager.add(labelManager);
            return imageManager;
        }
        
        public static HorizontalFieldManager getTopImageAndButtonManager(String title)
        {
            HorizontalFieldManager imageManager = Utils.getImageManager(Utils.displayWidth, Utils.HEADER_HEIGHT, Utils.headerImage);
            
            
            int fontHeight = Utils.fontHeightHeaderText;
            int stringLength = Utils.getStringLength(title, Utils.getNormalFont(fontHeight));
            while(true)
            {
                if(stringLength <= Utils.displayWidth-20)break;
                fontHeight--;
                stringLength = Utils.getStringLength(title, Utils.getBoldFont(fontHeight));
            }
            
            CustomLabelField lbl = new CustomLabelField(title, fontHeight, Utils.colorTextHeader);
            
            int leftMargin = (Utils.displayWidth - stringLength)/2;            
            int height = Utils.HEADER_HEIGHT;
            if(lbl.getPreferredHeight() +10 < height)height = lbl.getPreferredHeight()+10;
            
            int topMargin = (height - lbl.getPreferredHeight())/2;
            lbl.setMargin(topMargin, 0, 0, leftMargin);
            HorizontalFieldManager labelManager = new HorizontalFieldManager();
            labelManager.add(lbl);
            imageManager.add(labelManager);
            return imageManager;
        }
        
        public static String DecimalToHexadecimal(int hexInt)
        {
                String hexString = Integer.toHexString(hexInt);
                return hexString;
        }
        

        public static int getStringLength(String str, int fontSize)
        {
                Font font = Font.getDefault();
                font = font.derive(font.getStyle(), fontSize);
                return font.getAdvance(str);
        }

        public static int getStringLength(String str)
        {
                Font font = Font.getDefault();
                return font.getAdvance(str);
        }

        public static int getStringLength(String str, Font font)
        {
                return font.getAdvance(str);
        }

        // Identifies the substrings in a given string that are delimited
        // by one or more characters specified in an array, and then
        // places the substrings into a String array.
        public static String[] split(String strString, String strDelimiter)
        {
                String[] strArray;
                int iOccurrences = 0;
                int iIndexOfInnerString = 0;
                int iIndexOfDelimiter = 0;
                int iCounter = 0;

                // Check for null input strings.
                if (strString == null)
                {
                        throw new IllegalArgumentException("Input string cannot be null.");
                }
                // Check for null or empty delimiter strings.
                if (strDelimiter.length() <= 0 || strDelimiter == null)
                {
                        throw new IllegalArgumentException(
                                        "Delimeter cannot be null or empty.");
                }

                // strString must be in this format: (without {} )
                // "{str[0]}{delimiter}str[1]}{delimiter} ...
                // {str[n-1]}{delimiter}{str[n]}{delimiter}"

                // If strString begins with delimiter then remove it in order
                // to comply with the desired format.

                if (strString.startsWith(strDelimiter))
                {
                        strString = strString.substring(strDelimiter.length());
                }

                // If strString does not end with the delimiter then add it
                // to the string in order to comply with the desired format.
                if (!strString.endsWith(strDelimiter))
                {
                        strString += strDelimiter;
                }

                // Count occurrences of the delimiter in the string.
                // Occurrences should be the same amount of inner strings.
                while ((iIndexOfDelimiter = strString.indexOf(strDelimiter,
                                iIndexOfInnerString)) != -1)
                {
                        iOccurrences += 1;
                        iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();
                }

                // Declare the array with the correct size.
                strArray = new String[iOccurrences];

                // Reset the indices.
                iIndexOfInnerString = 0;
                iIndexOfDelimiter = 0;

                // Walk across the string again and this time add the
                // strings to the array.
                while ((iIndexOfDelimiter = strString.indexOf(strDelimiter,
                                iIndexOfInnerString)) != -1)
                {
                        // Add string to array.
                        strArray[iCounter] = strString.substring(iIndexOfInnerString,
                                        iIndexOfDelimiter);

                        // Increment the index to the next character after
                        // the next delimiter.
                        iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();

                        // Inc the counter.
                        iCounter += 1;
                }

                return strArray;
        }

        
        
        public static HorizontalFieldManager getImageManager(final int width,
                        final int height, final Bitmap image)
        {
            HorizontalFieldManager imageManager = new HorizontalFieldManager()//HorizontalFieldManager.USE_ALL_WIDTH/* | HorizontalFieldManager.USE_ALL_HEIGHT*/)
            {
                //Override the paint method to draw the background image.
                public void paint(Graphics graphics)
                {
                        //Draw the background image and then call super.paint
                        graphics.drawBitmap(0, 0, width, height, image, 0, 0);
                        super.paint(graphics);
                }

                protected void sublayout(int maxWidth, int maxHeight)
                {
                        super.sublayout(width, height);
                        setExtent(width, height);
                }
            };
            return imageManager;
        }
        
        public static HorizontalFieldManager getFixedHorizontalManager(final int width,
                        final int height)
        {
            HorizontalFieldManager manager = new HorizontalFieldManager()//HorizontalFieldManager.USE_ALL_WIDTH/* | HorizontalFieldManager.USE_ALL_HEIGHT*/)
            {
                protected void sublayout(int maxWidth, int maxHeight)
                {
                        super.sublayout(width, height);
                        setExtent(width, height);
                }
            };
            return manager;
        }
        
        public static VerticalFieldManager getMainVerticalManager(final int width,
                        final int height, final int backgroundColor)
        {
                VerticalFieldManager verticalManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR)
                {
                        public void paint(Graphics graphics)
                        {
                                graphics.setBackgroundColor(backgroundColor);
                                graphics.clear();
                                super.paint(graphics);
                        }

                        protected void sublayout(int maxWidth, int maxHeight)
                        {
                                super.sublayout(width, height);
                                setExtent(width, height);
                        }
                };

                return verticalManager;
        }
        
        public static VerticalFieldManager getMainVerticalManager(final int width,
                        final int height, final int backgroundColor, long style)
        {
                VerticalFieldManager verticalManager = new VerticalFieldManager(style)
                {
                        public void paint(Graphics graphics)
                        {
                                graphics.setBackgroundColor(backgroundColor);
                                graphics.clear();
                                super.paint(graphics);
                        }

                        protected void sublayout(int maxWidth, int maxHeight)
                        {
                                super.sublayout(width, height);
                                setExtent(width, height);
                        }
                };

                return verticalManager;
        }
        
        public static VerticalFieldManager getMainVerticalManager(final int width,
                        final int height)
        {
                VerticalFieldManager verticalManager = new VerticalFieldManager()
                {
                       

                        protected void sublayout(int maxWidth, int maxHeight)
                        {
                                super.sublayout(width, height);
                                setExtent(width, height);
                        }
                };

                return verticalManager;
        }
        
        public static final int MARGIN_LEFT = 10;
        public static final int MARGIN_RIGHT = 10;
        
        public static Font getBoldFont(int fontHeight)
        {
                Font font = Font.getDefault();
                font = font.derive(font.getStyle() | Font.BOLD, fontHeight);
                return font;
        }
        public static Font getBoldFont(int fontHeight, int style)
        {
                Font font = Font.getDefault();
                font = font.derive(font.getStyle() | Font.BOLD | style, fontHeight);
                return font;
        }
        public static Font getNormalFont(int fontHeight)
        {
                Font font = Font.getDefault();
                font = font.derive(font.getStyle(), fontHeight);
                return font;
        }
        /*
        public static Font getNormalCrossFont(int fontHeight)
        {
                Font font = Font.getDefault();
                font = font.derive(font.getStyle() | Font.STRIKE_THROUGH, fontHeight);
                return font;
        }       
        */
        public static Font getUnderlinedBoldFont(int fontHeight)
        {
                Font font = Font.getDefault();
                font = font.derive(font.getStyle() | Font.BOLD | Font.UNDERLINED, fontHeight);
                return font;
        }       
        
        
        public static Font getDefaultFont()
        {
                // FontFamily fontfam[]=FontFamily.getFontFamilies();
                // Font font= fontfam[0].getFont(FontFamily.SCALABLE_FONT, fontHeight);
                Font font = Font.getDefault();

                font = font.derive(font.getStyle(), font.getHeight());
                return font;
        }

        private static int[] rescaleArray(int[] ini, int x, int y, int x2, int y2)
        {
                int out[] = new int[x2 * y2];
                for (int yy = 0; yy < y2; yy++)
                {
                        int dy = yy * y / y2;
                        for (int xx = 0; xx < x2; xx++)
                        {
                                int dx = xx * x / x2;
                                out[(x2 * yy) + xx] = ini[(x * dy) + dx];
                        }
                }
                return out;
        }
        
         /**
     * Generate and return a Bitmap Image scaled according to the specified width and height.
     * 
     * @param image     EncodedImage object
     * @param width     Intended width of the returned Bitmap object
     * @param height    Intended height of the returned Bitmap object
     * @return Bitmap object
     */
    public static Bitmap getScaledBitmapImage(EncodedImage image, int width, int height)
    {
        // Handle null image
        if (image == null)
        {
            return null;
        }
        
        //return bestFit(image.getBitmap(), width, height);
        
        int currentWidthFixed32 = Fixed32.toFP(image.getWidth());
        int currentHeightFixed32 = Fixed32.toFP(image.getHeight());
        
        int requiredWidthFixed32 = Fixed32.toFP(width);
        int requiredHeightFixed32 = Fixed32.toFP(height);
        
        int scaleXFixed32 = Fixed32.div(currentWidthFixed32, requiredWidthFixed32);
        int scaleYFixed32 = Fixed32.div(currentHeightFixed32, requiredHeightFixed32);
        
        image = image.scaleImage32(scaleXFixed32, scaleYFixed32);
        
        return image.getBitmap();
    }

    public static Bitmap resizeBitmap(Bitmap image, int width, int height)
    {
            if (image == null)
                    return new Bitmap(width, height);
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            // Need an array (for RGB, with the size of original image)
            int rgb[] = new int[imageWidth * imageHeight];

            // Get the RGB array of image into "rgb"
            image.getARGB(rgb, 0, imageWidth, 0, 0, imageWidth, imageHeight);

            // Call to our function and obtain rgb2
            int rgb2[] = rescaleArray(rgb, imageWidth, imageHeight, width, height);

            // Create an image with that RGB array
            Bitmap temp2 = new Bitmap(width, height);

            temp2.setARGB(rgb2, 0, width, 0, 0, width, height);

            return temp2;
    }
        
        public static String getImageNameFromPath(String imagePath)
        {
                String name = "";
                for (int i = 0; i < imagePath.length(); ++i)
                {
                        name += String.valueOf(imagePath.charAt(i));
                        if (imagePath.charAt(i) == '/')
                        {
                                name = "";
                        }
                }
                return name;
        }
        public static String getFileType(String fileName, boolean isImage)
        {
            String names[] = Utils.split(fileName, ".");
            if (names.length == 2)return names[1];
            if(isImage)return "jpg";
            return "3gp";
        }

        public static Bitmap readImage(String filePath)
        {
                try
                {
                        FileConnection fconn = (FileConnection) Connector.open(filePath,
                                        Connector.READ);
                        if (fconn.exists())
                        {
                                InputStream input = fconn.openInputStream();

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                int j = 0;
                                while ((j = input.read()) != -1)
                                {
                                        baos.write(j);
                                }
                                byte[] data = baos.toByteArray();

                                input.close();
                                fconn.close();
                                if (Utils.DEBUG)
                                System.out.println("data[].length:  " + data.length);
                                EncodedImage encodedImageMain = EncodedImage
                                                .createEncodedImage(data, 0, data.length);

                                Bitmap bitmapImageResized = Utils.getScaledBitmapImage(
                                                encodedImageMain, Display.getWidth() / 2, Display
                                                                .getHeight() / 2);
                                return bitmapImageResized;

                        }
                        else
                        {
                                fconn.close();
                                return null;
                        }

                }
                catch (Exception ioe)
                {

                        System.out.println("Error:   " + ioe.toString() + "\n"
                                        + ioe.getMessage());
                        return null;
                }
        }

        public static EncodedImage readImageFromPath(String filePath)
        {
                try
                {
                        FileConnection fconn = (FileConnection) Connector.open(filePath,
                                        Connector.READ);
                        if (fconn.exists())
                        {
                                InputStream input = fconn.openInputStream();

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                int j = 0;
                                while ((j = input.read()) != -1)
                                {
                                        baos.write(j);
                                }
                                byte[] data = baos.toByteArray();

                                input.close();
                                fconn.close();
                                if (Utils.DEBUG)
                                System.out.println("data[].length:  " + data.length);
                                EncodedImage encodedImageMain = EncodedImage
                                                .createEncodedImage(data, 0, data.length);

                                return encodedImageMain;

                        }
                        else
                        {
                                fconn.close();
                                return null;
                        }

                }
                catch (Exception ioe)
                {

                        System.out.println("Error:   " + ioe.toString() + "\n"
                                        + ioe.getMessage());
                        return null;
                }
        }
        
        public static void openBrowser(String url)
        {
                BrowserSession session;
                session = Browser.getDefaultSession();
                session.displayPage(url);
                session.showBrowser();
        }
        public static void invokeEMail(final String toAddress, final String subject, final String mailText)
        {

                UiApplication.getUiApplication().invokeLater(new Runnable()
                {
                        public void run()
                        {
                                Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES,
                                                new MessageArguments(MessageArguments.ARG_NEW, toAddress,
                                                                subject, mailText));
                        }
                });

        }
        
        /**
     * horizontal measurement based on screen pixel per meter(ppm)
     * here we considering curve device resulution(6410*6410) as standard  
     * @param int actual horizontal measurement(pixel)    
     * @return modified horizontal measurement
     */
    public static int getHorizontalMeasurement(int size)
    {   
        double ratio = (double) ((double)size / (double)Utils.SCREEN_RES0LUTION_HORIZONTAL_BOLD);  
        double adjustedSize = (double)(ratio  * (double)Utils.SCREEN_RESOLUTION_HORIZONTAL);
        int sz = (int)adjustedSize;
        if (sz == 0)sz = 1;
        return sz;
    }
    
    /**
     * vertical measurement based on screen pixel per meter(ppm)
     * here we considering curve device resulution(6410) as standard  
     * @param int actual vertical measurement(pixel)    
     * @return modified vertical measurement
     */
    public static int getVerticalMeasurement(int size)
    {   
        double ratio = (double) ((double)size / (double)SCREEN_RES0LUTION_VERTICAL_BOLD);  
        double adjustedSize = (double)(ratio  * (double)SCREEN_RESOLUTION_VERTICAL);
        int sz = (int)adjustedSize;
        if (sz == 0)sz = 1;
        return sz;
    }
    
    public static final byte [] hexStringToByteArray(final String hex) 
        {
            byte [] bytes = new byte[(hex.length() / 2)];
            int j = 0;
            for ( int i=0; i<bytes.length; i++ ) {
                j = i * 2;
                String hex_pair = hex.substring(j,j+2);
                byte b = (byte) (Integer.parseInt(hex_pair, 16) & 0xFF);
                bytes [i] = b;
            }
            return bytes;
        }
        static public String fromUTF8(byte [] bytesToConvert) 
        {
            String returnString = null;
            try {
                returnString = new String(bytesToConvert,"UTF-8");
            } catch (Exception e) {
                // Will never happen, UTF-8 is supported
            }
            return returnString;
        }
        
        public static String getImageNameWithoutExtension(String name)
        {
            if (name.lastIndexOf('.') != -1)
            {
                return name.substring(0,name.lastIndexOf('.'));
            }
            return name;
       }        
       public static void orientationChanged()
        {
            Utils.displayWidth = Display.getWidth();
            Utils.displayHeight = Display.getHeight();
            HomeScreen.setImages();
        }
        
        
        
        
        public static void backToPreviousScreen(int screenIndex, MainScreen currentScreen)
        {
            ScreenManager screenManager = ScreenManager.getInstance();
            int previousIndex = screenManager.previousScreenArray[screenIndex].pop();
            if (previousIndex == -1)
            {
                if (Dialog.ask(Dialog.D_YES_NO, Utils.EXIT_APPLICATION_TEXT) == Dialog.YES)
                    System.exit(0);
            }
            else
            {
                if(previousIndex == ScreenManager.CITY_DETAILS_SCREEN)
                    ((CityDetailsScreen)(screenManager.mainScreenArray[previousIndex])).checkOrientation();
                else if(previousIndex == ScreenManager.CITY_SCREEN)
                    ((CityScreen)(screenManager.mainScreenArray[previousIndex])).checkOrientation();
                else if(previousIndex == ScreenManager.COUNTDOWN_SCREEN)
                    ((CountDownScreen)(screenManager.mainScreenArray[previousIndex])).checkOrientation();
                else if(previousIndex == ScreenManager.HISTORY_DETAILS_SCREEN)
                    ((HistoryDetailsScreen)(screenManager.mainScreenArray[previousIndex])).checkOrientation();
                else if(previousIndex == ScreenManager.HISTORY_SCREEN)
                    ((HistoryScreen)(screenManager.mainScreenArray[previousIndex])).checkOrientation();
                else if(previousIndex == ScreenManager.HOME_SCREEN)
                    ((HomeScreen)(screenManager.mainScreenArray[previousIndex])).checkOrientation();
                else if(previousIndex == ScreenManager.MATCH_SCREEN)
                    ((MatchScreen)(screenManager.mainScreenArray[previousIndex])).checkOrientation();
                else if(previousIndex == ScreenManager.STADIUM_DETAILS_SCREEN)
                    ((StadiumDetailsScreen)(screenManager.mainScreenArray[previousIndex])).checkOrientation();
                else if(previousIndex == ScreenManager.STADIUM_SCREEN)
                    ((StadiumScreen)(screenManager.mainScreenArray[previousIndex])).checkOrientation();
                else if(previousIndex == ScreenManager.TEAM_DETAILS_SCREEN)
                    ((TeamDetailScreen)(screenManager.mainScreenArray[previousIndex])).checkOrientation();
                else if(previousIndex == ScreenManager.TEAM_SCREEN)
                    ((TeamScreen)(screenManager.mainScreenArray[previousIndex])).checkOrientation();
                else if(previousIndex == ScreenManager.POINT_TABLE_SCREEN)
                    ((PointTableScreen)(screenManager.mainScreenArray[previousIndex])).checkOrientation();
                else if(previousIndex == ScreenManager.TICKET_SCREEN)
                    ((PointTableScreen)(screenManager.mainScreenArray[previousIndex])).checkOrientation();
               
                UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[previousIndex]);
                currentScreen.close();
            }
        }
        
        public static String getButtonText(Field field)
        {
            if (field instanceof ButtonField)
            {
                ButtonField buttonField = (ButtonField) field;
                return buttonField.getLabel();
            }
            /*if (field instanceof BitmapButtonField1)
            {
                BitmapButtonField1 buttonField = (BitmapButtonField1) field;
                return buttonField.getButtonText();
            }*/
            if (field instanceof CustomButtonField)
            {
                CustomButtonField buttonField = (CustomButtonField) field;
                return buttonField.getText();
            }
            /*if (field instanceof BitmapButtonField)
            {
                BitmapButtonField buttonField = (BitmapButtonField) field;
                return buttonField.getButtonText();
            }*/
            if (field instanceof HyperlinkButtonField)
            {
                HyperlinkButtonField buttonField = (HyperlinkButtonField) field;
                return buttonField.getButtonName();
            }
            return "";
        }
        
        public static int getButtonId(Field field)
        {
            if (field instanceof HyperlinkButtonField)
            {
                HyperlinkButtonField buttonField = (HyperlinkButtonField) field;
                return buttonField.getButtonId();
            }
            if (field instanceof CustomButtonField)
            {
                CustomButtonField buttonField = (CustomButtonField) field;
                return buttonField.getButtonId();
            }
            /*
            if (field instanceof BitmapButtonField)
            {
                BitmapButtonField buttonField = (BitmapButtonField) field;
                return buttonField.getButtonId();
            }
            if (field instanceof BitmapButtonField1)
            {
                BitmapButtonField1 buttonField = (BitmapButtonField1) field;
                return buttonField.getButtonId();
            }
            */
            return 0;
        }
        
        
        public static void deleteFile(String filePath)
        {
            FileConnection fc = null;
                
            try 
            {
                fc = (FileConnection)Connector.open(filePath);
                if (fc.exists())fc.delete();
            } 
            catch (Exception ex) 
            {
                Dialog.alert("Unable to delete file or directory: " + filePath);
            } 
            finally 
            {
                try 
                {
                    if (fc != null) 
                    {
                        fc.close();
                        fc = null;
                    }
                } 
                catch (Exception ioex) 
                {
                }
            }
        }
        
        public static byte[] getBytesFromFile(String filename) throws IOException 
        {
            FileConnection fconn = null;
            InputStream is = null;
            try {
                fconn = (FileConnection) Connector.open(filename, Connector.READ);
                is = fconn.openInputStream();
    
                return IOUtilities.streamToBytes(is);
            } finally {
                if (is != null) {
                    is.close();
                }
                if (fconn != null) {
                    fconn.close();
                }
            }
        }
        
        
        public static String getCurrentDate()
        {
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            return date.format(c.getTime());
        }
        public static boolean equalDouble(double double1, double double2)
        {
                double precision = 0.0000000001;
                return (Math.abs(double1 - double2) <= precision);
        }
        
        
        //GPS information
        public static final boolean isGPSFetchingEnabled = false;
        public static final double LONGITUDE_DEFAULT = -73.9869510;//LONGITUDE_NEWYORK
        public static final double LATITUDE_DEFAULT = 40.7560540;//LATITUDE_NEWYORK
        public static double longitude = 0.0;
        public static double latitude = 0.0;
        public static final double getLongitude()
        {
            if (!Utils.equalDouble(Utils.longitude, 0.0) && !Utils.equalDouble(Utils.latitude, 0.0))
            {
                return Utils.longitude;
            }
            return Utils.LONGITUDE_DEFAULT;
        }
        public static final double getLatitude()
        {
            if (!Utils.equalDouble(Utils.longitude, 0.0) && !Utils.equalDouble(Utils.latitude, 0.0))
            {
                return Utils.latitude;
            }
            return Utils.LATITUDE_DEFAULT;
        }
        ////////////////////////
        
        
        /**
    * 
    * Check if string is numeric
    * 
    * @param str
    * @return boolean
    */
   public static boolean isNumeric(String str)
   {
      if(str.trim().equals("")) return false;

      try
      {
         Integer.valueOf(str.trim());
      }
      catch(Exception e)
      {
         return false;
      }
      return true;
   }
   
   public static Squad getSquadFromId(String id)
   {
        Squad squad = null;
        for(int i=0;i<Utils.squadVector.size();i++)
        {
            squad = (Squad)Utils.squadVector.elementAt(i);
            if(squad.team_id.equalsIgnoreCase(id) == true)
                return squad;
        }
        return null;
   }
    
   
   public static Team1 getTeamNameFromId(String id)
   {
        Team1 team = null;
        for(int i=0;i<Utils.teamVector.size();i++)
        {
            team = (Team1)Utils.teamVector.elementAt(i);
            if(team.id.equalsIgnoreCase(id) == true)
                return team;
        }
        return null;
   }
   
   //Not sure this function will work with all the team names
   public static Team1 getTeamIdFromName(String teamName)
   {
        Team1 team = null;
        for(int i=0;i<Utils.teamVector.size();i++)
        {
            team = (Team1)Utils.teamVector.elementAt(i);
            if(team.name.equalsIgnoreCase(teamName) == true)
                return team;
        }
        return null;
   }
   
   public static Stadium1 getStadiumFromID(String id)
   {
        Stadium1 stadium = null;
        for(int i=0;i<Utils.stadiumVector.size();i++)
        {
            stadium = (Stadium1)Utils.stadiumVector.elementAt(i);
            if(stadium.id.equalsIgnoreCase(id) == true)
                return stadium;
        }
        return null;
   }
   
   public static City1 getCityFromId(String id)
   {
        City1 city = null;
        for(int i=0;i<Utils.cityVector.size();i++)
        {
            city = (City1)Utils.cityVector.elementAt(i);
            if(city.id.equalsIgnoreCase(id) == true)
                return city;
        }
        return null;
   }
   
   public static History1 getHistoryFromYear(String year)
   {
        History1 history = null;
        for(int i=0;i<Utils.historyVector.size();i++)
        {
            history = (History1)Utils.historyVector.elementAt(i);
            if(history.year.equalsIgnoreCase(year) == true)
                return history;
        }
        return null;
   }
   
   public static Match1 getMatchFromID(int id)
   {
        Match1 match = null;
        for(int i=0;i<Utils.matchVector.size();i++)
        {
            match = (Match1)Utils.matchVector.elementAt(i);
            if(match.id == id)
                return match;
        }
        for(int i=0;i<Utils.remainingmatchVector.size();i++)
        {
            match = (Match1)Utils.remainingmatchVector.elementAt(i);
            if(match.id == id)
                return match;
        }
        return null;
   }
   
    //com.bikas.utils.XmlInformation
    //0xe1a44d655fd49befL
    public static long PERSISTABLE_XML_INFO = 0xe1a44d655fd49befL;
    public static boolean saveXmlInformation(/*XmlInformation1 xmlInformation*/)
    {
        XmlInformation1 xmlInformation = new XmlInformation1();
        xmlInformation.cityVector = Utils.cityVector;
        xmlInformation.groupVector = Utils.groupVector;
        xmlInformation.teamVector = Utils.teamVector;
        xmlInformation.stadiumVector = Utils.stadiumVector;
        xmlInformation.historyVector = Utils.historyVector;
        xmlInformation.matchVector = Utils.matchVector;
        xmlInformation.remainingmatchVector = Utils.remainingmatchVector;
        xmlInformation.squadVector = Utils.squadVector;
        xmlInformation.pointTableVector = Utils.pointTableVector;
        
        PersistentObject xmlStore;   
        xmlStore = PersistentStore.getPersistentObject( Utils.PERSISTABLE_XML_INFO);      
        synchronized (xmlStore) 
        {          
            xmlStore.setContents(xmlInformation);
            xmlStore.commit();
        }            
        xmlStore = null;
        return true;
    }
    
    public static void getXmlInformation()
    {
        PersistentObject xmlInfo = PersistentStore.getPersistentObject(Utils.PERSISTABLE_XML_INFO);
        XmlInformation1 xml = (XmlInformation1)xmlInfo.getContents();
        
        if(xml == null)
        {
        }
        else
        {
            Utils.cityVector = xml.cityVector;
            Utils.groupVector = xml.groupVector;
            Utils.teamVector = xml.teamVector;
            Utils.stadiumVector = xml.stadiumVector;
            Utils.historyVector = xml.historyVector;
            Utils.matchVector = xml.matchVector;
            Utils.remainingmatchVector = xml.remainingmatchVector;
            Utils.squadVector = xml.squadVector;
            Utils.pointTableVector = xml.pointTableVector;
        }
        
        xmlInfo = null;
    }
    
    
    public static String getPrintableTitleFromKey(String key)
    {
        if(key.equalsIgnoreCase("final")) return "Final";
        if(key.equalsIgnoreCase("round16")) return "Round of 16";
        if(key.equalsIgnoreCase("quarter")) return "Quarter Final";
        if(key.equalsIgnoreCase("semi")) return "Semi Final";
        if(key.equalsIgnoreCase("third")) return "Third Place";
        return key;
    }
    
        
   
}
