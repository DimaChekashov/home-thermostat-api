package com.example.home_thermostat_api.config;

import java.util.logging.Logger;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;

@Component
public class LoginLogger implements ApplicationListener<AuthenticationSuccessEvent> {
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";

    @Override
    public void onApplicationEvent(@Nonnull AuthenticationSuccessEvent event) {
        String userName = event.getAuthentication().getName();
        Logger.getLogger("AuthenticationSuccessEvent")
                .info(ANSI_PURPLE + "User [" + userName + "] logged successfully" + ANSI_RESET);
    }

}
