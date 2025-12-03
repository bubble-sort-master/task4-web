package com.innowise.taxi.command;

import com.innowise.taxi.command.impl.RegisterCommand;
import com.innowise.taxi.command.impl.DefaultCommand;
import com.innowise.taxi.command.impl.LoginCommand;
import com.innowise.taxi.command.impl.LogoutCommand;

public enum CommandType {
    REGISTER(new RegisterCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    DEFAULT(new DefaultCommand());

    final Command command;

    CommandType(Command command) {
        this.command = command;
    }

    //TODO: handle default command
    public static Command parse(String commandStr){
        CommandType type = CommandType.valueOf(commandStr.toUpperCase());
        return type.command;
    }

}
