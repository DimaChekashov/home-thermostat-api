package com.example.home_thermostat_api.telegram;

public enum UserState {
    START,
    AWAITING_USERNAME,
    AWAITING_PASSWORD,
    AWAITING_REGISTER_USERNAME,
    AWAITING_REGISTER_EMAIL,
    AWAITING_REGISTER_PASSWORD,
    LOGGED_IN
}
