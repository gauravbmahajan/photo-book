import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import { FileDashboardComponent } from './component/file-dashboard/file-dashboard.component';

const routes: Routes = [
  {path: 'files', component: FileDashboardComponent},
  {path: '**', redirectTo: '/', pathMatch: 'full'}
]

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
