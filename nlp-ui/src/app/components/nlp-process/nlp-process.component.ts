import { saveAs } from 'file-saver';
import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { NlpService } from '../../services/nlp.service';
import { FileRatings } from '../../models/FileRatings.model';
import { FormControl, FormGroup, FormsModule, Validators } from '@angular/forms';
import { MatProgressSpinner } from '@angular/material/progress-spinner';


@Component({
  selector: 'app-nlp-process',
  standalone: true,
  imports: [MatTableModule, MatCheckboxModule, FormsModule, MatProgressSpinner],
  templateUrl: './nlp-process.component.html',
  styleUrl: './nlp-process.component.css'
})
export class NlpProcessComponent implements OnInit {
  displayedColumns: string[] = ['select', 'fileName', 'fileRatings'];
  dataSource = new MatTableDataSource<FileRatings>();
  selection = new SelectionModel<FileRatings>(true, []);
  fileRatings: FileRatings[] = [];
  queryString = '';
  isLoadingResults = false;

  constructor(private nlpService: NlpService) { }

  ngOnInit(): void { }

  find() {
    console.log(this.queryString)
    if(this.queryString === null || this.queryString === '') {
      alert("Pass valid value in text box");
      return;
    }

    this.isLoadingResults = true;

    this.nlpService.processNLP(this.queryString).subscribe(
      (resp) => {
        this.fileRatings = resp;
        console.log(this.fileRatings);
        this.dataSource = new MatTableDataSource<FileRatings>(this.fileRatings);
        this.isLoadingResults = false;
      }, (error) => {
        console.log('Error occured', error.errror.message);
        alert(error);
        this.isLoadingResults = false;
      }
    );
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  toggleAllRows() {
    if (this.isAllSelected()) {
      this.selection.clear();
      return;
    }

    this.selection.select(...this.dataSource.data);
  }

  download() {
    console.log(this.selection.selected);
    if(this.selection.selected.length > 1) {
      alert('Select only 1 file for download');
      return;
    }

    this.nlpService.downloadFile(this.selection.selected[0].fileName).subscribe(
      (resp: any) => {
        saveAs(resp, this.selection.selected[0].fileName);
      }
    );

  }
  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: FileRatings): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.fileName + 1}`;
  }
}
