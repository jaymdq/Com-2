
/**
 * WebServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.monitors.ws;

    /**
     *  WebServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class WebServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public WebServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public WebServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for insertMultipleDeviceDTO method
            * override this method for handling normal response from insertMultipleDeviceDTO operation
            */
           public void receiveResultinsertMultipleDeviceDTO(
                    com.monitors.ws.WebServiceStub.InsertMultipleDeviceDTOResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertMultipleDeviceDTO operation
           */
            public void receiveErrorinsertMultipleDeviceDTO(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteFrames method
            * override this method for handling normal response from deleteFrames operation
            */
           public void receiveResultdeleteFrames(
                    com.monitors.ws.WebServiceStub.DeleteFramesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteFrames operation
           */
            public void receiveErrordeleteFrames(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertSingleDevice method
            * override this method for handling normal response from insertSingleDevice operation
            */
           public void receiveResultinsertSingleDevice(
                    com.monitors.ws.WebServiceStub.InsertSingleDeviceResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertSingleDevice operation
           */
            public void receiveErrorinsertSingleDevice(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getFrames method
            * override this method for handling normal response from getFrames operation
            */
           public void receiveResultgetFrames(
                    com.monitors.ws.WebServiceStub.GetFramesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getFrames operation
           */
            public void receiveErrorgetFrames(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertMultipleFrameDTO method
            * override this method for handling normal response from insertMultipleFrameDTO operation
            */
           public void receiveResultinsertMultipleFrameDTO(
                    com.monitors.ws.WebServiceStub.InsertMultipleFrameDTOResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertMultipleFrameDTO operation
           */
            public void receiveErrorinsertMultipleFrameDTO(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDevices method
            * override this method for handling normal response from getDevices operation
            */
           public void receiveResultgetDevices(
                    com.monitors.ws.WebServiceStub.GetDevicesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDevices operation
           */
            public void receiveErrorgetDevices(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertSingleFrame method
            * override this method for handling normal response from insertSingleFrame operation
            */
           public void receiveResultinsertSingleFrame(
                    com.monitors.ws.WebServiceStub.InsertSingleFrameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertSingleFrame operation
           */
            public void receiveErrorinsertSingleFrame(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertSingleFrameDTO method
            * override this method for handling normal response from insertSingleFrameDTO operation
            */
           public void receiveResultinsertSingleFrameDTO(
                    com.monitors.ws.WebServiceStub.InsertSingleFrameDTOResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertSingleFrameDTO operation
           */
            public void receiveErrorinsertSingleFrameDTO(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertMultipleDevices method
            * override this method for handling normal response from insertMultipleDevices operation
            */
           public void receiveResultinsertMultipleDevices(
                    com.monitors.ws.WebServiceStub.InsertMultipleDevicesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertMultipleDevices operation
           */
            public void receiveErrorinsertMultipleDevices(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteDevices method
            * override this method for handling normal response from deleteDevices operation
            */
           public void receiveResultdeleteDevices(
                    com.monitors.ws.WebServiceStub.DeleteDevicesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteDevices operation
           */
            public void receiveErrordeleteDevices(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertMultipleFrames method
            * override this method for handling normal response from insertMultipleFrames operation
            */
           public void receiveResultinsertMultipleFrames(
                    com.monitors.ws.WebServiceStub.InsertMultipleFramesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertMultipleFrames operation
           */
            public void receiveErrorinsertMultipleFrames(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertSingleDeviceDTO method
            * override this method for handling normal response from insertSingleDeviceDTO operation
            */
           public void receiveResultinsertSingleDeviceDTO(
                    com.monitors.ws.WebServiceStub.InsertSingleDeviceDTOResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertSingleDeviceDTO operation
           */
            public void receiveErrorinsertSingleDeviceDTO(java.lang.Exception e) {
            }
                


    }
    