import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {SchedulerEntityModule} from './entities/entity.module';
import {AppRoutingModule} from './app-routing.module';
import {BlockService} from "./block/block.service";
import {BlockComponent} from "./block/block.component";
import {BlockUIModule} from "primeng/blockui";


@NgModule({
  declarations: [
    AppComponent,
    BlockComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    HttpClientModule,
    SchedulerEntityModule.forRoot(),
    AppRoutingModule,
    BlockUIModule

  ],
  providers: [BlockService],
  bootstrap: [AppComponent],
})
export class AppModule { }
