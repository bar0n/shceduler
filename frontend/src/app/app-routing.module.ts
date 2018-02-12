import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ScheduleComponent} from './entities/schedule/schedule.component';
import {ScheduleLogComponent} from "./entities/schedule-log";

const routes: Routes = [
  {path: 'schedule', component: ScheduleComponent},
  {path: 'scheduleLog', component: ScheduleLogComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes

   /* ,{ enableTracing: true }*/

    )],
  exports: [RouterModule]
})
export class AppRoutingModule { }
