package com.app.waiter.Common;

import android.app.Application;

/**
 * Created by Javier on 05/06/2015.
 */
public class GlobalVars extends Application {
    private String serverIP;
    private String userServer;
    private String passServer;
    private String port;

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getUserServer() {
        return userServer;
    }

    public void setUserServer(String userServer) {
        this.userServer = userServer;
    }

    public String getPassServer() {
        return passServer;
    }

    public void setPassServer(String passServer) {
        this.passServer = passServer;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

}
