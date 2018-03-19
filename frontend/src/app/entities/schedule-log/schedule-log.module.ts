import {CommonModule} from '@angular/common';
import {TableModule} from 'primeng/table';
import {RouterModule, Routes} from '@angular/router';
import {DialogModule} from 'primeng/dialog';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {CalendarModule, DropdownModule, InputTextModule, PaginatorModule} from 'primeng/primeng';
import {ButtonModule} from 'primeng/button';
import {CheckboxModule} from 'primeng/checkbox';
import {InputSwitchModule} from 'primeng/inputswitch';
import {CronJobsModule} from 'ngx-cron-jobs';

import {InputTextareaModule} from 'primeng/inputtextarea';
import {NotificationsModule} from '../notifications/notification.module';
import {ScheduleLogComponent} from './schedule-log.component';
import {ScheduleLogService} from './schedule-log.service';
import {NgModule} from '@angular/core';
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ScheduleLogEditComponent} from "./schedule-log-edit.component";

@NgModule({
  imports: [
    NotificationsModule,
    CommonModule,
    TableModule,
    RouterModule,
    BrowserAnimationsModule,
    DialogModule,
    InputSwitchModule,
    CheckboxModule,
    BrowserModule,
    FormsModule,
    InputTextModule,
    ButtonModule,
    DialogModule,
    CommonModule,
    CalendarModule,
    InputTextareaModule,
    DropdownModule,
    CronJobsModule,
    ConfirmDialogModule,
    PaginatorModule,

  ],
  declarations: [
    ScheduleLogComponent, ScheduleLogEditComponent
  ],

  providers: [
    ScheduleLogService,
  ],
  exports: [ScheduleLogComponent, ScheduleLogEditComponent]
})
export class SchedulerScheduleLogModule {
}
