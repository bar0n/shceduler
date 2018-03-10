import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {ScheduleService} from './schedule.service';
import {Schedule} from '../../shared/model/schedule.model';
import {NotificationsService} from '../notifications/notifications.service';
import {Observable} from 'rxjs/Observable';
import {EventService} from "./event.service";


@Component({
  selector: 'schedule',
  styleUrls: ['./schedule.component.css'],
  templateUrl: './schedule.component.html'
})
export class ScheduleComponent implements OnInit, OnDestroy {
  events: any[];
  schedules: Schedule[];
  error: any;
  success: any;
  links: any;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  displayDialog: boolean;
  cronVisible = false;
  schedule: Schedule = {};

  selectedSchedule: Schedule[] = [];
  periodTxt;
  newSchedule: boolean;
  us;
  periods;

  constructor(
    private scheduleService: ScheduleService,
    // private parseLinks: JhiParseLinks,
    private notificationsService: NotificationsService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private eventService: EventService,
  ) {
    this.itemsPerPage = 20;
    this.us = [
      {name: 'Dima', code: 'Dima'},
      {name: 'Tania', code: 'Tania'},
    ];

    this.periods = [

      {
        code: '0 */10 * ? * *',
        name: 'Every 10 minutes'
      },
      {
        code: '0 0 */12 ? * *',
        name: 'Every twelve hours'
      },
      {
        code: '0 0 0 * * ?',
        name: 'Every day at midnight - 12am'
      },
      {
        code: '0 0 1 * * ?',
        name: 'Every day at 1am'
      },
      {
        code: '0 0 6 * * ?',
        name: 'Every day at 6am'
      },
      {
        code: '0 0 12 ? * ?',
        name: 'Every day at noon - 12pm'
      },
      {
        code: '0 0 12 ? * ?',
        name: 'Every day at noon - 12pm'
      },
      {
        code: '0 0 12 ? * SUN',
        name: 'Every Sunday at noon'
      },
      {
        code: '0 0 12 ? * MON',
        name: 'Every Monday at noon'
      },
      {
        code: '0 0 12 ? * TUE',
        name: 'Every Tuesday at noon'
      },
      {
        code: '0 0 12 ? * WED',
        name: 'Every Wednesday at noon'
      },
      {
        code: '0 0 12 ? * THU',
        name: 'Every Thursday at noon'
      },
      {
        code: '0 0 12 ? * FRI',
        name: 'Every Friday at noon'
      },
      {
        code: '0 0 12 ? * SAT',
        name: 'Every Saturday at noon'
      },
      {
        code: '0 0 12 ? * MON-FRI',
        name: 'Every Weekday at noon'
      },
      {
        code: '0 0 12 ? * SUN,SAT',
        name: 'Every Saturday and Sunday at noon'
      },
      {
        code: '0 0 12 */7 * ?',
        name: 'Every 7 days at noon'
      },
      {
        code: '0 0 12 1 * ?',
        name: 'Every month on the 1st, at noon'
      },
      {
        code: '0 0 12 2 * ?',
        name: 'Every month on the 2nd, at noon'
      },
      {
        code: '0 0 12 15 * ?',
        name: 'Every month on the 15th, at noon'
      },
      {
        code: '0 0 12 1/2 * ?',
        name: 'Every 2 months on the 1st, at noon'
      },
      {
        code: '0 0 12 1/4 * ?',
        name: 'Every 4 months on the 1st, at noon'
      },
      {
        code: '0 0 12 L * ?',
        name: 'Every month on the last day of the month, at noon'
      },
      {
        code: '0 0 12 L-2 * ?',
        name: 'Every month on the second to last day of the month, at noon'
      },
      {
        code: '0 0 12 LW * ?',
        name: 'Every month on the last weekday, at noon'
      },
      {
        code: '0 0 12 1L * ?',
        name: 'Every month on the last Sunday, at noon'
      },
      {
        code: '0 0 12 2L * ?',
        name: 'Every month on the last Monday, at noon'
      },
      {
        code: '0 0 12 6L * ?',
        name: 'Every month on the last Friday, at noon'
      },
      {
        code: '0 0 12 1W * ?',
        name: 'Every month on the nearest Weekday to the 1st of the month, at noon'
      },
      {
        code: '0 0 12 15W * ?',
        name: 'Every month on the nearest Weekday to the 15th of the month, at noon'
      },
      {
        code: '0 0 12 ? * 2#1',
        name: 'Every month on the first Monday of the Month, at noon'
      },
      {
        code: '0 0 12 ? * 6#1',
        name: 'Every month on the first Friday of the Month, at noon'
      },
      {
        code: '0 0 12 ? * 2#2',
        name: 'Every month on the second Monday of the Month, at noon'
      },
      {
        code: '0 0 12 ? * 5#3',
        name: 'Every month on the third Thursday of the Month, at noon - 12pm'
      },
      {
        code: '0 0 12 ? JAN *',
        name: 'Every day at noon in January only'
      },
      {
        code: '0 0 12 ? JUN *',
        name: 'Every day at noon in June only'
      },
      {
        code: '0 0 12 ? JAN,JUN *',
        name: 'Every day at noon in January and June'
      },
      {
        code: '0 0 12 ? DEC *',
        name: 'Every day at noon in December only'
      },
      {
        code: '0 0 12 ? JAN,FEB,MAR,APR *',
        name: 'Every day at noon in January, February, March and April'
      },
      {
        code: '0 0 12 ? 9-12 *',
        name: 'Every day at noon between September and December'
      }
    ];

  }

  loadAll() {
    this.scheduleService.query({
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort()
    }).subscribe(
      (res: HttpResponse<Schedule[]>) => this.onSuccess(res.body, res.headers),
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/schedule'], {
      queryParams:
        {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate(['/schedule', {
      page: this.page,
      sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
    }]);
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
  }

  ngOnDestroy() {
  }

  trackId(index: number, item: Schedule) {
    return item.id;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  private onSuccess(data, headers) {
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    this.schedules = data;
  }

  private onError(error) {
    this.notificationsService.notify('error', error, error.message);
  }


  save() {
    if (this.schedule.id !== undefined) {
      this.subscribeToSaveResponse(
        this.scheduleService.update(this.schedule));
    } else {
      this.subscribeToSaveResponse(
        this.scheduleService.create(this.schedule));
    }
    this.displayDialog = false;
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<Schedule>>) {
    result.subscribe((res: HttpResponse<Schedule>) =>
      this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError(res));
  }

  private onSaveSuccess(result: Schedule) {
    this.notificationsService.notify('success', '', ' saved');
    this.schedule = result;
    this.loadAll();
  }

  private onSaveError(error) {
    this.notificationsService.notify('error', error, error.message);
  }

  delete() {
    if (this.schedule && this.schedule.id) {
      this.scheduleService.delete(this.schedule.id).subscribe(() => {
          this.notificationsService.notify('success', '', ' deleted ');
          this.loadAll();
          this.schedule = new Schedule();
        }
      );

    }

    this.schedule = new Schedule();
    this.displayDialog = false;
  }


  onItemClick(data) {
    this.newSchedule = false;
    this.schedule = this.cloneSchedule(data);
    this.displayDialog = true;
    const crone = this.schedule.cron;
    setTimeout(() => {
      this.schedule.cron = crone;
    }, 1000);

  }

  onRowSelect(event) {
    this.newSchedule = false;
    this.schedule = this.cloneSchedule(event.data);
    this.displayDialog = true;
    const crone = this.schedule.cron;
    setTimeout(() => {
      this.schedule.cron = crone;
    }, 1000);

  }

  cloneSchedule(c: Schedule): Schedule {
    let schedule = new Schedule();
    for (let prop in c) {
      schedule[prop] = c[prop];
    }

    return schedule;
  }

  showDialogToAdd2() {
    this.router.navigate(['/scheduleAdd']);
  }

  showDialogToAdd() {
    this.newSchedule = true;
    this.schedule = new Schedule();
    this.displayDialog = true;
  }

  loadEvents(event) {
    let start = event.view.start;
    let end = event.view.end;
    this.eventService.getAllEvents(start, end, this.selectedSchedule).subscribe(events => {
      this.events = events;
    });
  }


  showCron() {
    this.cronVisible = true;
  }

  closeCron() {
    this.cronVisible = false;
  }
}
