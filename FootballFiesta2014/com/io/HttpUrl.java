/*
 * HttpUrl.java
 */

package com.io;

/**
 * 
 * @author Bikas
 */

class HttpUrl
{
        private StringBuffer url;

        public HttpUrl(String serverAddress, String[][] params)
        {
                url = new StringBuffer();
                url.append(serverAddress);

                for (int i = 0; params != null && i < params.length; i++)
                {
                        url.append(i == 0 ? '?' : '&');
                        url.append(params[i][0]);
                        url.append('=');
                        url.append(encodeURL(params[i][1]));
                }
        }
        private String mark = " \"#%+<>[\\]^`{|}";

        private char toHexChar(int digitValue)
        {
                if (digitValue < 10)
                    return (char) ('0' + digitValue);
                else
                    return (char) ('A' + (digitValue - 10));
        }

        private String encodeURL(String url)
        {
                StringBuffer encodedUrl = new StringBuffer();

                for (int iIndex = 0; iIndex < url.length(); iIndex++)
                {
                        char c = url.charAt(iIndex);

                        if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z')
                                        || (c >= 'A' && c <= 'Z'))
                                encodedUrl.append(c);
                        else
                        {
                                int imark = mark.indexOf(c);
                                if (imark >= 0)
                                {
                                        encodedUrl.append('%');
                                        encodedUrl.append(toHexChar((c & 0xF0) >> 4));
                                        encodedUrl.append(toHexChar(c & 0x0F));
                                }
                                else
                                {
                                        encodedUrl.append(c);
                                }
                        }
                }
                return encodedUrl.toString();
        }

        public String toString()
        {
                return url.toString();
        }
}
