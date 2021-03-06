import {Component, OnInit} from '@angular/core';
import {ScheduleService} from './entities/schedule';
import {Schedule} from './shared/model/schedule.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'app';
  schedules: Schedule[];

  constructor(private scheduleService: ScheduleService) {
  }

  ngOnInit(): void {

  }
}
