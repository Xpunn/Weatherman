package com.example.weatherman.service;

import com.example.weatherman.model.WeatherInfo;
import com.example.weatherman.repository.WeatherInfoRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class WeatherInfoService {

    private final WeatherInfoRepo repository;

    public WeatherInfo addWeatherInfo(WeatherInfo weatherInfo) {
        return repository.save(weatherInfo);
    }

    public List<WeatherInfo> getAllWeatherInfos() {
        return repository.findAll();
    }

    public List<WeatherInfo> findWeatherInfoByDate(LocalDate date) {
        return repository.findWeatherInfoByDate(date);
    }
}
