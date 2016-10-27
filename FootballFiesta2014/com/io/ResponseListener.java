/*
 * ResponseListener.java
 */

package com.io;

/**
 * 
 * @author Bikas
 */

public interface ResponseListener
{
        public void responseReceived(boolean isSuccessful, String resultString,
                        int responseId, Object object);
}
