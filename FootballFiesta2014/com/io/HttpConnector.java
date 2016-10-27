package com.io;

import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import com.utils.Utils;

/**
 * 
 * @author Bikas
 */

class HttpConnector implements Runnable
{
        private HttpUrl httpUrl;
        private HttpReader httpReader;
        private boolean stopped;
        private HttpConnection httpConnection = null;
        private DataInputStream dataInputStream = null;
        
        
        public HttpConnector(HttpReader httpReader, HttpUrl httpUrl)
        {
                this.httpReader = httpReader;
                this.httpUrl = httpUrl;
                stopped = false;
        }

        private String[][] requestPropertyParams;

        public HttpConnector(HttpReader httpReader, HttpUrl httpUrl,
                        String[][] requestPropertyParams)
        {
                this.httpReader = httpReader;
                this.httpUrl = httpUrl;
                this.requestPropertyParams = requestPropertyParams;
                stopped = false;
        }

        public void run()
        {
                try
                {
                        if (!stopped)
                        {

                                int responseCode = 200;
                                int requestCount = 1;
                                while (requestCount > 0)
                                {
                                        String url = httpUrl.toString();
                                        url += HttpUtility.getConnectionString();
                                        //Utils.requestUrl = url;
                                        if (Utils.DEBUG)
                                            System.out.println(url);
                                        httpConnection = (HttpConnection) Connector.open(url);
                                        httpConnection.setRequestMethod(HttpConnection.GET);
                                        if (requestPropertyParams != null)
                                        {
                                                int requestPropertyCount = requestPropertyParams.length;
                                                for (int i = 0; requestPropertyParams != null
                                                                && i < requestPropertyCount; i++)
                                                {
                                                        httpConnection.setRequestProperty(
                                                                        requestPropertyParams[i][0],
                                                                        requestPropertyParams[i][1]);
                                                }
                                        }
                                        responseCode = httpConnection.getResponseCode();
                                        requestCount--;
                                        if (responseCode == 200)
                                        {
                                            break;
                                        }
                                }
                                if (responseCode != 200)
                                {
                                        
                                        if (responseCode == 413)
                                            httpReader.failed(new Exception(""+responseCode));
                                        else
                                            httpReader.failed(new Exception("Error During Connection.\nPlease Check your Network Connectivity."+ responseCode));
                                }
                                else
                                {
                                        dataInputStream = httpConnection.openDataInputStream();
                                        if (!stopped)
                                        {
                                                httpReader.read(dataInputStream);
                                        }
                                }
                        }
                }
                catch (Exception exception)
                {
                        httpReader.failed(exception);
                }
                finally
                {
                        closeConnection();
                }
        }

        private void closeConnection()
        {
                if (dataInputStream != null)
                {
                        try
                        {
                                dataInputStream.close();
                        }
                        catch (IOException ioe)
                        {
                        }
                        dataInputStream = null;
                }

                if (httpConnection != null)
                {
                        try
                        {
                                httpConnection.close();
                        }
                        catch (IOException ioe)
                        {
                        }
                        httpConnection = null;
                }
                System.gc();
        }

        public void stop()
        {
                stopped = true;
        }
}
