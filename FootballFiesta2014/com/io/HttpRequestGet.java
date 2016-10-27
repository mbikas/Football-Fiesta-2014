package com.io;

import net.rim.device.api.ui.UiApplication;

/*
@author Bikas
*/

public class HttpRequestGet implements GetResponseListener
{
        private int requestId;
        private Object object;
        private ResponseListener responseListener;
        private boolean isBusyScreenRequired = true;
        private HttpRequestGet thisClass;

        public HttpRequestGet(ResponseListener responseListener, String baseUrl,
                        String[] keys, String[] values, int requestId, Object object,
                        boolean isBusyScreenRequired, String busyScreenText)
        {

                this.thisClass = this;
                this.requestId = requestId;
                this.isBusyScreenRequired = isBusyScreenRequired;
                this.responseListener = responseListener;
                this.object = object;

                String[][] params = null;
                if (keys != null)
                {
                        params = new String[keys.length][2];
                        for (int i = 0; i < keys.length; ++i)
                        {
                                params[i][0] = keys[i];
                                params[i][1] = values[i];
                        }
                }

                String connectionString = "";
                try
                {
                        connectionString = HttpUtility.getConnectionString();
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
                //System.out.println(connectionString);
                //this line should be added during build
                //HttpUrl httpUrl = new HttpUrl(baseUrl + connectionString, params);
                HttpUrl httpUrl = new HttpUrl(baseUrl, params);
                System.out.println(httpUrl.toString());
                HttpRequest request = new HttpRequest(this, httpUrl, params);
                if (isBusyScreenRequired == true)
                {
                        BusyScreen lodingScreen = new BusyScreen(busyScreenText, request);
                        UiApplication.getUiApplication().pushScreen(lodingScreen);
                }
        }

        private class HttpRequest extends Response
        {

                private int requestId;
                private String responseString = "";

                public HttpRequest(GetResponseListener responseListener,
                                HttpUrl httpUrl, String[][] param)
                {
                        super(responseListener);
                        send(httpUrl, param);
                }

                public void createResponse(String data)
                {
                        this.responseString = data;
                        setStatus(Response.SUCCESS);
                }

                public String getResultString()
                {
                        return responseString;
                }

                public void setRequestId(int requestId)
                {
                        this.requestId = requestId;
                }

                public int getRequestId()
                {
                        return requestId;
                }
        }

        public void responseReceived(final Response response)
        {
                UiApplication.getUiApplication().invokeLater(new Runnable()
                {
                        public void run()
                        {
                                if (thisClass.isBusyScreenRequired)
                                {
                                        UiApplication.getUiApplication().popScreen(
                                                        UiApplication.getUiApplication().getActiveScreen());
                                }

                                if (response.isInterrupted())
                                {
                                        /*
                                         * String resultString = "Canceled by user.";
                                         * thisClass.responseListener .responseReceived(false,
                                         * resultString, thisClass.requestId, thisClass.object);
                                         */
                                }
                                if (response.getStatus() == Response.SUCCESS)
                                {
                                        String resultString = ((HttpRequest) response)
                                                        .getResultString();
                                        thisClass.responseListener
                                                        .responseReceived(true, resultString,
                                                                        thisClass.requestId, thisClass.object);
                                }
                                else
                                {
                                        String resultString = ((HttpRequest) response)
                                                        .getErrorMessage();
                                        thisClass.responseListener
                                                        .responseReceived(false, resultString,
                                                                        thisClass.requestId, thisClass.object);
                                }

                        }
                });
        }
}
