import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { WeatherInfoService } from './services/weather-info.service';
import { WeatherInfo } from './weatherInfo';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  weatherInfos: WeatherInfo[][] = [];
  // dbWeatherInfos: WeatherInfo[] = [];
  // params!: string;

  constructor(private weatherInfoService: WeatherInfoService) {}

  public getWeather($event: string): void {
    this.weatherInfoService.getWeatherInfo($event).subscribe({
      next: (response: WeatherInfo[][]) => (this.weatherInfos = response),
      error: (error: HttpErrorResponse) => alert(error.message),
    });
  }

  // public getWeatherFromDb(): void {
  //   this.weatherInfoService.getFromDbByDate(this.params).subscribe({
  //     next: (response: WeatherInfo[]) => (this.dbWeatherInfos = response),
  //     error: (error: HttpErrorResponse) => alert(error.message),
  //   });
  // }

  ngOnInit(): void {}
}
