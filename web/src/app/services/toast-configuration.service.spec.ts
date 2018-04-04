import { TestBed, inject } from '@angular/core/testing';

import { ToastConfigurationService } from './toast-configuration.service';

describe('ToastConfigurationService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ToastConfigurationService]
    });
  });

  it('should be created', inject([ToastConfigurationService], (service: ToastConfigurationService) => {
    expect(service).toBeTruthy();
  }));
});
