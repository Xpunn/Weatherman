import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WeatherInfo } from '../weatherInfo';
import { environment } from 'src/environments/environment';
import { DatePipe } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class WeatherInfoService {
  private apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  public getWeatherInfo(
    lat: string,
    long: string,
    startDate: Date,
    endDate: Date
  ): Observable<WeatherInfo[][]> {
    const datePipe = new DatePipe('en-EU');
    let formattedStartDate = datePipe.transform(
      startDate,
      'yyyy-MM-ddTHH:mm:ss'
    );
    let formattedEndDate = datePipe.transform(endDate, 'yyyy-MM-ddTHH:mm:ss');
    return this.http.get<WeatherInfo[][]>(
      `${this.apiUrl}/${lat}/${long}/${formattedStartDate}/${formattedEndDate}`
    );
  }
}
