import { Injectable } from '@angular/core';
import { ToastrService, GlobalConfig } from 'ngx-toastr';

@Injectable()
export class ToastConfigurationService {
  SUCCESS = 'success';
  ERROR = 'error';
  WARN = 'warning';
  INFO = 'info';

  config: GlobalConfig;

  constructor(private toastr: ToastrService) {
    this.config = toastr.toastrConfig;
    this.config.progressBar = true;
  }

  showToaster(type: string, msg: string) {
    this.toastr.show(msg, type.toLocaleUpperCase() , this.config, this.config.iconClasses[type])
  }

}
