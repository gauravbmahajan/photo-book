import { Component, OnInit } from '@angular/core';
import { FileService } from '../../services/file.service';
import * as $ from 'jquery';
import { ToastConfigurationService } from '../../services/toast-configuration.service';
import { AlbumResponse } from '../../model/album-response';

@Component({
  selector: 'app-file-dashboard',
  templateUrl: './file-dashboard.component.html',
  styleUrls: ['./file-dashboard.component.css']
})
export class FileDashboardComponent implements OnInit {

  public albumResponse: AlbumResponse[];

  constructor(private fileService: FileService, private  toastService: ToastConfigurationService) { }

  ngOnInit() {
    this.getContent();
  }

  getContent() {
    this.fileService.getFiles('http://localhost:8080/dropbox/api').subscribe(
      (data) => {
        console.log('result', data);
        this.albumResponse = data;
      }, error => {
        this.toastService.showToaster(this.toastService.ERROR, 'Error while getting the images');
         console.log('Error while getting images', error);
      })
  }

  opnenFileExplorer() {
    $('#file').click();
  }

  uplaodFiles(event) {
    console.log('event', event);

    const fileList: FileList = event.target.files;
    if (fileList.length > 0) {
      var file: File = fileList[0];
      var fileName = file.name.toLowerCase();
      var extension = fileName.substring(fileName.lastIndexOf('.') + 1);
      if (extension == 'jpeg' || extension == 'jpg' || extension == 'png') {
        var formData = new FormData();
        formData.append(file.name, file, file.name);
        const url = 'http://localhost:8080/dropbox/api/';
        this.fileService.uploadFile(formData, url).subscribe(
          (response) => {
            this.toastService.showToaster(this.toastService.SUCCESS, 'Image uplaoded successfully!');
            // this.getContent();
            console.log('uplaod response', response);
          }, error => {
            this.toastService.showToaster(this.toastService.ERROR, 'Failed to upload Image!');
            console.log('error while upload', error);
          }, () => {
            location.reload();
          });
      }else {
        var msg: string = extension + ' not supported !';
        this.toastService.showToaster(this.toastService.WARN, msg);
      }
    }
  }

  deleteFile(fileName: string) {
    const url = 'http://localhost:8080/dropbox/api?fileName=' + fileName;
    this.fileService.deleteFile(url).subscribe(
      (response) => {
        this.toastService.showToaster(this.toastService.SUCCESS, 'Image deleted successfully!');
        // this.getContent();
        console.log('delete file', response);
      }, error => {
        this.toastService.showToaster(this.toastService.ERROR, 'Failed to delete Image!');
        console.log('error while delete', error);
      }, () => {
        location.reload();
      });
  }
}
