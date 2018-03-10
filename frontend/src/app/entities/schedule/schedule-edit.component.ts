import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {ScheduleService} from './schedule.service';
import {Schedule} from '../../shared/model/schedule.model';
import {NotificationsService} from '../notifications';
import {Observable} from 'rxjs/Observable';
import {Subscription} from "rxjs/Subscription";
import {ConfirmationService} from 'primeng/api';
import {EventService} from "./event.service";

@Component({
  selector: 'schedule-edit',
  styles: [` .wid150 {
    width: 150px;
  }`],

  templateUrl: './schedule-edit.component.html'
})
export class ScheduleEditComponent implements OnInit, OnDestroy {

  schedule: Schedule = new Schedule();

  periodTxt;

  events: any[];
  cron: string;
  cronVisible = false;
  uk: any;
  private subscription: Subscription;
  title = "New Schedule";

  ngOnInit(): void {
    this.subscription = this.activatedRoute.params.subscribe((params) => {
      this.loadAll(params['id']);
    });
    this.uk = 'ru';
    /*{
          locale: 'en'
        };*/

  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  constructor(
    private scheduleService: ScheduleService,
    private notificationsService: NotificationsService,
    public activatedRoute: ActivatedRoute,
    public router: Router,
    public confirmationService: ConfirmationService,
    public eventService: EventService,
  ) {

  }

  loadEvents(event) {
    let start = event.view.start;
    let end = event.view.end;
    this.eventService.getEvents(start, end, this.cron).subscribe(events => {

      this.events = events.map(x => this.eventObj(x));
    });
  }

  private eventObj(x: any) {
    return {
      "title": this.schedule.name,
      "start": x
    }
  }

  showCron() {
    this.cron = this.schedule.cron;
    this.cronVisible = true;
  }

  showCronLog() {
    this.cron = this.schedule.cronLog;
    this.cronVisible = true;
  }

  closeCron() {
    this.cronVisible = false;
  }

  previousState() {
    window.history.back();
  }

  loadAll(id) {

    if (id) {
      this.scheduleService.find(id).subscribe(x => {
        this.schedule = x.body;
        this.title = "Edit Schedule";
        this.selectedPerson = x.body.person.split(",").map(x => x.trim());
      });
    }

  }

  save() {
    if (this.schedule.cronLog == null || this.schedule.cronLog == '' || this.schedule.cronLog) {
      this.cronKeyUp({});
    }
    if (this.schedule.id !== undefined) {
      this.notificationsService.notify('success', '', ' saved');
      this.scheduleService.update(this.schedule).subscribe(x => this.router.navigate(['/scheduleEdit', x.body.id]));
    } else {
      this.subscribeToSaveResponse(
        this.scheduleService.create(this.schedule));
    }
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<Schedule>>) {
    result.subscribe((res: HttpResponse<Schedule>) =>
      this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError(res));
  }

  private onSaveSuccess(result: Schedule) {
    this.notificationsService.notify('success', '', ' saved');
    this.schedule = result;

  }

  private onSaveError(error) {
    this.notificationsService.notify('error', error, error.message);
  }

  delete() {

    this.confirmationService.confirm({
      message: 'Are you sure that you want to perform this action?',
      accept: () => {
        if (this.schedule && this.schedule.id) {
          this.scheduleService.delete(this.schedule.id).subscribe(() => {
              this.notificationsService.notify('success', '', ' deleted ');
              this.schedule = new Schedule();
              this.router.navigate(["schedule"]);
            }
          );
        }
      }
    });

  }


  cloneSchedule(c: Schedule): Schedule {
    let schedule = new Schedule();
    for (let prop in c) {
      schedule[prop] = c[prop];
    }

    return schedule;
  }

  cronKeyUp(event) {
    let splitted = this.schedule.cron.split(" ", 3);
    if (splitted.length == 3) {
      this.schedule.cronLog = splitted.join(" ") + " 1/1 * ? *";
    }
  }

  selectedPerson = [];
  usItem = [
    {label: 'Dima', code: "Dima", value: {id: 'Dima', name: 'Dima', email: 'berkbach@gmail.com'}},
    {label: 'Tania', code: "Tania", value: {id: 'Tania', name: 'Tania', email: 'buxauto888@gmail.com'}},

  ];

  onChangePerson(event) {
    /* console.log(JSON.stringify(event.value));*/
    let email = event.value.map(x => x.email);
    let names = event.value.map(x => x.name);
    this.schedule.email = email.join(",")
    this.schedule.person = names.join(",")
  }

  us = [
    {name: 'Dima', code: 'Dima'},
    {name: 'Tania', code: 'Tania'},
  ];

  periods = [

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
