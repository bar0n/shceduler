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
@NgModule({
  imports: [NotificationsModule,
    CommonModule,
    TableModule,
    RouterModule,
    BrowserAnimationsModule,
    DialogModule,

    CheckboxModule,
    BrowserModule,
    FormsModule,
    InputTextModule,
    ButtonModule,
    DialogModule,
    CommonModule,
    CalendarModule,
    DropdownModule

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
