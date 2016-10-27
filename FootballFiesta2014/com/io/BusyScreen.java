/*
* @author Bikas
*/

package com.io;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.system.KeyListener;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.*;
import net.rim.device.api.ui.XYEdges;
import com.api.ui.custom.AnimatedGIFField;
import com.api.ui.custom.CustomLabelField;

import com.utils.Utils;

public class BusyScreen extends PopupScreen implements KeyListener
{

        private BitmapField bitmapField;
        private int positionX = Display.getWidth() / 2;
        private int positionY = Display.getHeight() / 2;
        private boolean positionCalled = false;

        CustomLabelField loadingLabel;
        HorizontalFieldManager hfm;

        private Response response = null;
        private boolean isCloseScreen = true;
        CustomLabelField secondLabel;
        private String secondString = "";
        private int textColor = Color.BLACK;
        private int backgroundColor = 0xFAFAFA;//Utils.bgColor;//public static int bgColor = 0xEAEAEA;
        Font font = Utils.fontText;
        int fontHeight = Utils.fontHeightText;
        
        public BusyScreen(Response response, boolean isCloseScreen,
                        String secondString)
        {
            super(new VerticalFieldManager());
            //setBackground(BackgroundFactory.createSolidTransparentBackground(Utils.bgColor, 150));
            
            setTransparency();
            
            this.secondString = secondString;
            this.response = response;
            this.isCloseScreen = isCloseScreen;
            
            this.setFont(font);
            
            AnimatedGIFField animatedField = getAnimatedGIFFieldWithLoadingImage("loading.bin");
            loadingLabel = new CustomLabelField("Please Wait...", fontHeight, textColor);

            if (secondString.equals("") == false)
                    secondLabel = new CustomLabelField(secondString, fontHeight, textColor);

            hfm = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
            hfm.add(animatedField);
            int leftEmptySpaceImage = (this.getWidth() - animatedField
                            .getPreferredWidth()) / 2;

            animatedField.setMargin(0, 0, 0, leftEmptySpaceImage);
            this.add(hfm);
            // this.add( loadingLabel );

            if (secondString.equals("") == false)
            {
                int left = (secondLabel.getPreferredWidth() - loadingLabel
                                .getPreferredWidth()) / 2;
                loadingLabel.setMargin(0, 0, 0, left);
                this.add(loadingLabel);
                this.add(secondLabel);
            }
            else
            {
                this.add(loadingLabel);
            }

        }

        public BusyScreen(Response response, boolean isCloseScreen)
        {
            this(response, isCloseScreen, "");
        }

        public BusyScreen(Response response)
        {
                this(response, true);
        }

        String msg = "";

        public BusyScreen(String msg, Response response)
        {
                this(msg, response, true);
        }
        public BusyScreen(String msg, Response response, boolean isCloseScreen)
        {
                super(new VerticalFieldManager());
                
                setTransparency();
                
                this.response = response;
                this.msg = msg;
                this.secondString = "";
                this.isCloseScreen = isCloseScreen;

                AnimatedGIFField animatedField = getAnimatedGIFFieldWithLoadingImage("loading.bin");
                loadingLabel = new CustomLabelField(msg, fontHeight,
                                textColor);

                hfm = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
                hfm.add(animatedField);
                int leftEmptySpaceImage = (this.getWidth() - animatedField
                                .getPreferredWidth()) / 2;

                animatedField.setMargin(0, 0, 0, leftEmptySpaceImage);
                this.add(hfm);
                this.add(loadingLabel);
        }
        
        private InterruptListener interruptListener = null;
        public BusyScreen(String msg, InterruptListener interruptListener)
        {
                super(new VerticalFieldManager());
            
                setTransparency();
                
                this.response = null;
                this.interruptListener = interruptListener;
                this.msg = msg;
                this.secondString = "";
                this.isCloseScreen = true;

                AnimatedGIFField animatedField = getAnimatedGIFFieldWithLoadingImage("loading.bin");
                loadingLabel = new CustomLabelField(msg, fontHeight,
                                textColor);

                hfm = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
                hfm.add(animatedField);
                int leftEmptySpaceImage = (this.getWidth() - animatedField
                                .getPreferredWidth()) / 2;
                animatedField.setMargin(0, 0, 0, leftEmptySpaceImage);
                this.add(hfm);
                this.add(loadingLabel);
        }
        
        public void setTransparency()
        {
            setBackground(BackgroundFactory.createSolidTransparentBackground(backgroundColor, 0));
            setBorder(BorderFactory.createSimpleBorder(new XYEdges(),Border.STYLE_TRANSPARENT));
        }

        public void sublayout(int width, int height)
        {
            int w = hfm.getPreferredWidth() + loadingLabel.getPreferredWidth();
            int h = hfm.getPreferredHeight() + loadingLabel.getPreferredHeight();

            if (this.secondString.equals("") == false)
            {
                    w = hfm.getPreferredWidth() + secondLabel.getPreferredWidth() + 10;
                    h = hfm.getPreferredHeight() + loadingLabel.getPreferredHeight()
                                    + secondLabel.getPreferredHeight() + 10;
            }

            int leftGap = (Display.getWidth() - w) / 2;
            int topGap = (Display.getHeight() - h) / 2;
            
            if (positionCalled == false)
            {
                    positionX = positionX;
                    positionY = positionY;
            }
            //super.sublayout(width, height);
            //setPosition(leftGap, topGap);
            
            setExtent( w, h);
            setPosition(leftGap, topGap);
            layoutDelegate(w, h);
        }

        public void setScreenPosition(int x, int y)
        {
                this.positionCalled = true;
                this.positionX = x;
                this.positionY = y;
                invalidate();
        }

        /*
         * protected void paintBackground(Graphics graphics) {
         * 
         * int alpha = graphics.getGlobalAlpha(); graphics.setGlobalAlpha(0x4F);
         * graphics.setBackgroundColor(Color.DARKGRAY); graphics.clear();
         * graphics.setGlobalAlpha(alpha);
         * 
         * 
         * graphics.setBackgroundColor(Color.BLACK); graphics.clear();
         * 
         * super.paint(graphics);
         * 
         * }
         */
        public void closeScreen()
        {
                this.close();
        }

        // ///////////////////////////////////
        // / implementation of Keylistener
        // ///////////////////////////////////
        public boolean keyChar(char key, int status, int time)
        {
                boolean retval = false;
                try
                {
                        switch (key)
                        {
                        case Characters.ENTER:
                                break;
                        case Characters.ESCAPE:
                                if (isCloseScreen == true)
                                {
                                        closeScreen();
                                        if (response != null)
                                        {
                                                this.response.interrupt();
                                        }
                                        if (interruptListener != null)
                                        {
                                                interruptListener.interrupt();
                                        }
                                }
                                break;
                        default:
                                retval = super.keyChar(key, status, time);
                        }
                }
                catch (Exception e)
                {
                        System.out.println(e.toString());
                }
                return retval;
        }

        /** Implementation of KeyListener.keyDown */
        public boolean keyDown(int keycode, int time)
        {
                return false;
        }

        /** Implementation of KeyListener.keyRepeat */
        public boolean keyRepeat(int keycode, int time)
        {
                return false;
        }

        /** Implementation of KeyListener.keyStatus */
        public boolean keyStatus(int keycode, int time)
        {
                return false;
        }

        /** Implementation of KeyListener.keyUp */
        public boolean keyUp(int keycode, int time)
        {
                return false;
        }

        public static AnimatedGIFField getAnimatedGIFFieldWithLoadingImage(
                        String imageName)
        {
            GIFEncodedImage image;
            EncodedImage encodedImage = EncodedImage
                            .getEncodedImageResource(imageName);
            byte data[] = new byte[30000];
            data = encodedImage.getData();
            image = (GIFEncodedImage) EncodedImage.createEncodedImage(data, 0,
                            data.length);
            AnimatedGIFField animatedGIF = new AnimatedGIFField(image);
            return animatedGIF;
        }
}
