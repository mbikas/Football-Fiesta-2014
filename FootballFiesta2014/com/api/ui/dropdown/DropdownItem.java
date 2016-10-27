/*
 * DropdownItem.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.api.ui.dropdown;


import net.rim.device.api.io.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;


public class DropdownItem 
{
    Bitmap mBitmap;
    String mName;
    public DropdownItem(Bitmap bitmap, String name) 
    {
        this.mBitmap = bitmap;
        this.mName = name;
 }
}
