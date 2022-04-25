import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { WeatherInfoService } from 'src/app/services/weather-info.service';
import { WeatherInfo } from 'src/app/weatherInfo';

@Component({
  selector: 'app-weather-table',
  templateUrl: './weather-table.component.html',
  styleUrls: ['./weather-table.component.css'],
})
export class WeatherTableComponent implements OnInit {
  weatherInfos: WeatherInfo[][] = [];

  @Input() latitude: string | undefined;

  constructor(private weatherInfoService: WeatherInfoService) {}

  public getWeather(
    latitude: string,
    longitude: string,
    startDate: Date,
    endDate: Date
  ): void {
    this.weatherInfoService
      .getWeatherInfo(latitude, longitude, startDate, endDate)
      .subscribe(
        (response: WeatherInfo[][]) => {
          this.weatherInfos = response;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
      );
  }
  ngOnInit(): void {}
}
