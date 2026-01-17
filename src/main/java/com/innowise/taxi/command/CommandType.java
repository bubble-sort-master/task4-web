package com.innowise.taxi.command;

import com.innowise.taxi.command.impl.*;

public enum CommandType {
    REGISTER(new RegisterCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    DEFAULT(new DefaultCommand()),
    SHOW_USERS(new ShowUsersCommand()),
    SHOW_CARS(new ShowCarsCommand()),
    DRIVER_SHIFT(new DriverShiftCommand()),
    CLIENT_ORDER(new ClientOrderCommand()),
    DRIVER_ORDER(new DriverOrderCommand()),
    USER_STATUS(new UserStatusCommand()); // ← новая команда

    final Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public static Command parse(String commandStr) {
        try {
            CommandType type = CommandType.valueOf(commandStr.toUpperCase());
            return type.command;
        } catch (IllegalArgumentException e) {
            return DEFAULT.command;
        }
    }
}
