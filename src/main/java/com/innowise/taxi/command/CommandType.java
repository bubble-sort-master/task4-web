package com.innowise.taxi.command;

import com.innowise.taxi.command.impl.*;

public enum CommandType {
    REGISTER(new RegisterCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    DEFAULT(new DefaultCommand()),
    SHOW_USERS(new ShowUsersCommand());

    final Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public static Command parse(String commandStr) {
        try {
            CommandType type = CommandType.valueOf(commandStr.toUpperCase());
            return type.command;
        } catch (IllegalArgumentException | NullPointerException e) {
            return DEFAULT.command;
        }
    }


}
