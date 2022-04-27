import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { WeatherInfoService } from 'src/app/services/weather-info.service';
import { WeatherInfo } from 'src/app/weatherInfo';

@Component({
  selector: 'app-weather-table',
  templateUrl: './weather-table.component.html',
  styleUrls: ['./weather-table.component.css'],
})
export class WeatherTableComponent implements OnInit {
  @Input() weatherInfos: WeatherInfo[][] = [];
  // @Input() dbWeatherInfos: WeatherInfo[] = [];
  // @Output() onGetFromDb: EventEmitter<any> = new EventEmitter();
  weatherInfosFromDb: WeatherInfo[] = [];

  constructor(private weatherInfoService: WeatherInfoService) {}

  ngOnInit(): void {}

  saveToTb(): void {
    this.weatherInfoService.saveToDb(this.weatherInfos).subscribe({
      next: (response: WeatherInfo[]) => console.log(response),
      error: (error: HttpErrorResponse) => alert(error.message),
    });
  }

  // onGet(): void {
  //   this.onGetFromDb.emit();
  // }
}
