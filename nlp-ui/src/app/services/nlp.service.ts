import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FileRatings } from '../models/FileRatings.model';

@Injectable({
  providedIn: 'root'
})
export class NlpService {

  private apiURL = "http://localhost:8080/api/v1";

  constructor(private httpClient: HttpClient) { }

  /**
   * File Upload to Backend
   */
  fileUpload(file: File) {
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'application/octet-stream');

    let formData: FormData = new FormData();
    formData.set("file", file);
    return this.httpClient.post(this.apiURL + '/nlp/upload', formData);
  }

  getFilesList() {
    return this.httpClient.get(this.apiURL + '/nlp/list/files');
  }

  processNLP(queryString: string) : Observable<FileRatings[]> {
    return this.httpClient.get<FileRatings[]>(this.apiURL + '/nlp/query/' + queryString);
  }
}
