import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css'],
})
export class FormComponent implements OnInit {
  constructor() {}

  latitude: string = '';
  longitude: string = '';
  startDate: Date = new Date();
  endDate: Date = new Date();

  ngOnInit(): void {}
}
