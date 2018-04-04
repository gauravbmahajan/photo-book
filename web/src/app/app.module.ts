import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module'
import { HttpModule } from '@angular/http';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';

import { AppComponent } from './app.component';
import { FileDashboardComponent } from './component/file-dashboard/file-dashboard.component';
import { FileService } from './services/file.service';
import { ToastConfigurationService } from './services/toast-configuration.service';


@NgModule({
  declarations: [
    AppComponent,
    FileDashboardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    CommonModule
  ],
  providers: [FileService, ToastConfigurationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
