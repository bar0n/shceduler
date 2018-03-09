import {CommonModule} from '@angular/common';
import {TableModule} from 'primeng/table';
import {RouterModule, Routes} from '@angular/router';
import {DialogModule} from 'primeng/dialog';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {CalendarModule, DropdownModule, InputTextModule} from 'primeng/primeng';
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
    ConfirmDialogModule
  ],
  declarations: [
    ScheduleLogComponent,
  ],

  providers: [
    ScheduleLogService,
  ],
  exports: [ScheduleLogComponent,]
})
export class SchedulerScheduleLogModule {
}
