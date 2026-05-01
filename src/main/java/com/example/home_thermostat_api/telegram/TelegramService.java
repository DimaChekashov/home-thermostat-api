package com.example.home_thermostat_api.telegram;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.model.Home;
import com.example.home_thermostat_api.model.Room;
import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.model.Thermostat;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.repository.TemperatureReadingRepository;
import com.example.home_thermostat_api.service.HomeService;
import com.example.home_thermostat_api.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class TelegramService {
    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userService;

    @Autowired
    private TemperatureReadingRepository temperatureReadingRepository;

    @Transactional
    public String getUserHomes(String username) {
        User user = userService.getUserByUsername(username);
        List<Home> homes = homeService.getUserHomes(user);

        if (homes.isEmpty()) {
            return "У вас пока нет домов. Создайте их в приложении.";
        } else {
            StringBuilder sb = new StringBuilder("🏠 Ваши дома:\n\n");
            for (int i = 0; i < homes.size(); i++) {
                Home home = homes.get(i);
                sb.append(i + 1).append(". ").append(home.getName());
                if (home.getAddress() != null) {
                    sb.append(" (").append(home.getAddress()).append(")");
                }
                sb.append("\n");
            }

            return sb.toString();
        }
    }

    @Transactional
    public String getTemperatures(String username) {
        User user = userService.getUserByUsername(username);
        List<Home> homes = homeService.getUserHomes(user);

        if (homes.isEmpty()) {
            return "🏠 У вас пока нет домов. Создайте их в приложении.";
        }

        StringBuilder sb = new StringBuilder("🌡️ Температура:\n\n");
        boolean hasAnyRoom = false;

        for (Home home : homes) {
            sb.append("🏠 ").append(home.getName()).append(":\n");

            if (home.getRooms() == null || home.getRooms().isEmpty()) {
                sb.append("  • Нет комнат\n");
                continue;
            }

            for (Room room : home.getRooms()) {
                hasAnyRoom = true;
                Thermostat thermostat = room.getThermostat();

                sb.append("  • ").append(room.getName()).append(": ");

                if (thermostat == null) {
                    sb.append("нет термостата\n");
                } else {
                    Double currentTemp = temperatureReadingRepository
                            .findTopByThermostatOrderByTimestampDesc(thermostat)
                            .map(TemperatureReading::getValue)
                            .orElse(null);

                    if (currentTemp != null) {
                        sb.append(currentTemp).append("°C");
                    } else {
                        sb.append("нет данных");
                    }
                    sb.append("\n");
                }
            }
            sb.append("\n");
        }

        if (!hasAnyRoom) {
            return "🏠 У вас есть дома, но пока нет комнат. Добавьте комнаты в приложении.";
        }

        return sb.toString();
    }
}
