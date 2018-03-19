import {Component, OnInit, OnDestroy} from '@angular/core';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';

import {ScheduleLog} from '../../shared/model/schedule-log.model';
import {ScheduleLogService} from './schedule-log.service';
import {NotificationsService} from '../notifications';
import {ConfirmationService} from "primeng/api";
import {Schedule} from "../../shared/model/schedule.model";
import {Location} from "@angular/common";
import {BlockService} from "../../block/block.service";
import {ScheduleService} from "../schedule/schedule.service";
import {Observable} from "rxjs/Observable";
import {EventService} from "../schedule/event.service";

@Component({
  selector: 'jhi-schedule-log-edit',
  styles: [`

  `],
  templateUrl: './schedule-log-edit.component.html'
})
export class ScheduleLogEditComponent implements OnInit, OnDestroy {

  schedule: Schedule = new Schedule();
  scheduleLog: ScheduleLog = new ScheduleLog();
  private subscription: Subscription;

  title = "Edit Schedule Log";


  ngOnInit(): void {
    this.subscription = this.activatedRoute.params.subscribe((params) => {
      this.loadAll(params['id']);
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  constructor(
    private scheduleService: ScheduleService,
    private notificationsService: NotificationsService,
    public activatedRoute: ActivatedRoute,
    public router: Router,
    private scheduleLogService: ScheduleLogService,
    public confirmationService: ConfirmationService,
    public eventService: EventService,
    public blockService: BlockService,
    private location: Location
  ) {

  }


  previousState() {
    window.history.back();
  }

  loadAll(id) {

    if (id) {
      this.scheduleLogService.find(id).subscribe(x => {
        this.scheduleLog = x.body;
        this.schedule = x.body.schedule;
      });
    }
  }

   save() {

    if (this.scheduleLog.id !== undefined) {
      this.scheduleLogService.update(this.scheduleLog).subscribe(x => this.router.navigate(['/scheduleLogEdit', x.body.id]));
      this.notificationsService.notify('success', '', ' saved');
    } else {
      this.subscribeToSaveResponse(
        this.scheduleLogService.create(this.scheduleLog));
    }
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<ScheduleLog>>) {
    result.subscribe((res: HttpResponse<ScheduleLog>) =>
      this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError(res));
  }

  private onSaveSuccess(result: ScheduleLog) {
    this.scheduleLog = result;
    this.notificationsService.notify('success', '', ' saved');
  }

  private onSaveError(error) {
    this.notificationsService.notify('error', error, error.message);
  }

  delete() {

    this.confirmationService.confirm({
      message: 'Are you sure that you want to perform this action?',
      accept: () => {
        if (this.scheduleLog && this.scheduleLog.id) {
          this.scheduleLogService.delete(this.scheduleLog.id).subscribe(() => {
              this.notificationsService.notify('success', '', ' deleted ');
              this.router.navigate(["scheduleLog"]);
            }
          );
        }
      }
    });

  }

 }
