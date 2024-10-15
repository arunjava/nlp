import { Component, ViewChild } from '@angular/core';
import { NlpService } from '../../services/nlp.service';
import { DatePipe } from '@angular/common';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule, SortDirection } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-file-list',
  standalone: true,
  imports: [MatProgressSpinnerModule, MatTableModule, MatSortModule, MatPaginatorModule, DatePipe],
  templateUrl: './file-list.component.html',
  styleUrl: './file-list.component.css'
})
export class FileListComponent {
  selectedFile: File | null = null;
  displayedColumns: string[] = ['file_name'];
  dataSource = new MatTableDataSource<string[]>();
  paginatorPageSize: number = 5;
  paginatorPageSizeOptions: number[] = [5, 10, 20];

  data: any;

  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private nlpService: NlpService) {
    this.getFileList();
  }

  // This function gets triggered when the user selects a file
  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  getFileList() {
    this.nlpService.getFilesList().subscribe(
      (resp) => {
        this.data = resp;
        this.isLoadingResults = false;
        this.dataSource = new MatTableDataSource<string[]>(this.data);
        this.dataSource.paginator = this.paginator;
      }
    );
  }

  pageEvent(event: any) {
    this.paginatorPageSize = event.pageSize;
  }

  // This function gets triggered when the user clicks the 'Upload' button
  onUpload() {
    if (this.selectedFile) {

      if (!this.selectedFile.name.includes('txt')) {
        alert(`Invalid file ${this.selectedFile?.name} has been selected. Only text files are allowed ending .txt`);
        return;
      }

      // You can now handle the file upload logic here, like sending the file to a server
      console.log('Uploading file:', this.selectedFile);

      // Create a FormData object to send the file with a POST request
      const formData = new FormData();
      formData.append('file', this.selectedFile);

      this.nlpService.fileUpload(this.selectedFile).subscribe(
        (resp) => {
          console.log(resp);
          alert(`File ${this.selectedFile?.name} has been uploaded.`);
          this.selectedFile = null;
          this.getFileList();//Update the file list
        }, (error) => {
          console.log(error);
          alert(`File has been uploaded.`);
          this.selectedFile = null;
          this.getFileList();//Update the file list
        },
      );
    } else {
      alert('Please select a file before uploading.');
    }
  }
}
