import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import 'rxjs/Rx';

@Injectable()
export class FileService {

  public header: Headers;

  constructor(private http: Http) {
    this.header = new Headers();
    this.header.append('Content-Type', 'application/json');
    this.header.append('Accept', 'appliaction/json');
    this.header.append('Access-Control-Allow-Origin', '*')
  }

  getFiles(url: string): Observable<any[]> {
    this.header.append('Content-Type', 'application/json')
    return this.http.get(url)
      .map(response => response.json())
      .catch(this.handleError)
  }

  uploadFile(formData: FormData, url: string): Observable<any> {
    let header = new Headers();
    header.append('Accept', 'application/json');
    const options = new RequestOptions({ })
    return this.http.post(url, formData, {headers: header})
      .map(res => res)
      .catch(error => Observable.throw(error));
  }

  deleteFile(url: string) {
    return this.http.delete(url)
      .map(response => response)
      .catch(this.handleError);
  }

  public handleError(error: Response) {
    console.error('from handleError: ', error);
    return Observable.throw(error);
  }
}
