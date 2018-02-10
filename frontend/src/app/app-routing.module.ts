import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ScheduleComponent} from './entities/schedule/schedule.component';

const routes: Routes = [
  {path: 'schedule_detail', component: ScheduleComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes

   /* ,{ enableTracing: true }*/

    )],
  exports: [RouterModule]
})
export class AppRoutingModule { }
