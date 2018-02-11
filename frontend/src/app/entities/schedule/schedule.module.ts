import {NgModule} from '@angular/core';

import {ScheduleService} from './';
import {NotificationsModule} from "../notifications/notification.module";
import {CommonModule} from "@angular/common";
import {ScheduleComponent} from "./schedule.component";
import {TableModule} from 'primeng/table';
import {RouterModule, Routes} from '@angular/router';
import {DialogModule} from 'primeng/dialog';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {CalendarModule, DropdownModule, InputTextModule} from "primeng/primeng";
import {ButtonModule} from "primeng/button";
import {CheckboxModule} from 'primeng/checkbox';
import {InputSwitchModule} from 'primeng/inputswitch';
import { CronJobsModule } from 'ngx-cron-jobs';

import {InputTextareaModule} from 'primeng/inputtextarea';
@NgModule({
  imports: [NotificationsModule,
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

  ],
  declarations: [
    //ScheduleComponent,
    ScheduleComponent
    //    ScheduleDialogComponent,
    //   ScheduleDeleteDialogComponent,
    //   SchedulePopupComponent,
    //   ScheduleDeletePopupComponent,
  ],

  providers: [
    ScheduleService,
    //SchedulePopupService,
    //   ScheduleResolvePagingParams,
  ],
  exports: [ScheduleComponent]
})
export class SchedulerScheduleModule {
}