import { DatePipe } from '@angular/common';
import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { UiService } from 'src/app/services/ui.service';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css'],
})
export class FormComponent implements OnInit {
  @Output() onSearch: EventEmitter<string> = new EventEmitter();
  latitude!: string;
  longitude!: string;
  startDate!: Date;
  endDate!: Date;
  showSearch!: boolean;
  subscription!: Subscription;
  showMap: boolean = true;

  constructor(private uiService: UiService) {
    this.subscription = this.uiService
      .onToggle()
      .subscribe(
        (value) => ((this.showSearch = value), (this.showMap = value))
      );
  }

  updateCoords($event: number[]): void {
    this.latitude = $event[0].toFixed(4) as string;
    this.longitude = $event[1].toFixed(4) as string;
  }

  ngOnInit(): void {}

  onSubmit() {
    this.uiService.toggleSearch();
    const datePipe = new DatePipe('en-US');
    let formattedStartDate = datePipe.transform(
      this.startDate,
      'yyyy-MM-ddTHH:mm:ss'
    );
    let formattedEndDate = datePipe.transform(
      this.endDate,
      'yyyy-MM-ddTHH:mm:ss'
    );
    let params: string = `${this.latitude}/${this.longitude}/${formattedStartDate}/${formattedEndDate}`;
    this.onSearch.emit(params);
  }
}
