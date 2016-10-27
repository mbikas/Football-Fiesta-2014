/*
 * HttpReader.java
 */

package com.io;

import java.io.InputStream;

/**
 * 
 * @author Bikas
 */

interface HttpReader
{
        public void read(InputStream inputStream);
        //public void readImage(InputStream inputStream);
        public void failed(Exception exception);
}
