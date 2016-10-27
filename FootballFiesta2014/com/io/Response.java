/*
 * Response.java
 */

package com.io;

import java.io.InputStream;

import com.io.threadpool.ThreadPool;
import com.utils.Utils;

/**
 * 
 * @author Bikas
 */
public abstract class Response implements HttpReader, InterruptListener
{
        public static final int SUCCESS = 0;
        public static final int FAILED = 1;

        private GetResponseListener responseListener;
        private boolean interrupted;
        HttpConnector connector = null;

        private int status;
        private String errorMessage = "";

        public Response(GetResponseListener responseListener)
        {
                this.responseListener = responseListener;
                this.interrupted = false;

                status = SUCCESS;
        }

        protected abstract void createResponse(String data);

        /**
         * @return the status
         */
        public int getStatus()
        {
                return status;
        }

        /**
         * @param status
         *            the status to set
         */
        public void setStatus(int status)
        {
                this.status = status;
        }

        /**
         * @return the errorMessage
         */
        public String getErrorMessage()
        {
                return errorMessage;
        }

        /**
         * @param errorMessage
         *            the errorMessage to set
         */
        public void setErrorMessage(String errorMessage)
        {
                this.errorMessage = errorMessage;
        }

        protected void send(HttpUrl url)
        {
                connector = new HttpConnector(this, url);
                ThreadPool.getInstance().addTask(connector);
        }

        protected void send(HttpUrl url, String[][] requestProperty)
        {
                connector = new HttpConnector(this, url, requestProperty);
                ThreadPool.getInstance().addTask(connector);
        }

        public void read(InputStream inputStream)
        {
                try
                {
                        
                        StringBuffer sb = new StringBuffer();
                        int ch;

                        while ((ch = inputStream.read()) != -1 && !interrupted)
                        {
                                sb.append((char) ch);
                        }

                        // System.out.println("Response: " + sb.toString());
                        if (sb.toString().trim().equalsIgnoreCase(""))
                        {
                                createResponse(sb.toString());
                                responseListener.responseReceived(this);
                                return;
                        }

                        if (!interrupted)
                        {
                                String[] tokens = Utils.split(sb.toString(), "|");

                                if (tokens[0].equalsIgnoreCase("ERROR"))
                                {
                                        failed(new Exception(tokens[1]));
                                }
                                else
                                {
                                        createResponse(sb.toString());
                                        responseListener.responseReceived(this);
                                }
                        }
                }
                catch (Exception e)
                {
                        failed(e);
                }
        }

        public void failed(Exception exception)
        {
                setStatus(Response.FAILED);
                String errorMsg = exception.getMessage();

                if (errorMsg.startsWith("Permission"))
                        setErrorMessage("Failed to use network and send or receive data. Permission required.");
                else
                        setErrorMessage(exception.getMessage());
                responseListener.responseReceived(this);
        }

        public void interrupt()
        {
                interrupted = true;
                connector.stop();
        }

        public boolean isInterrupted()
        {
                return interrupted;
        }
}
