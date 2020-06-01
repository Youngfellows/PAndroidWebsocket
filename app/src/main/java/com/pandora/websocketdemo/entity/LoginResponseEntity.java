/**
 * Copyright 2020 bejson.com
 */
package com.pandora.websocketdemo.entity;


public class LoginResponseEntity {

    private Parameters parameters;
    private Command command;

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

}