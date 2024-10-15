import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { NlpService } from '../../services/nlp.service';
import { FileRatings } from '../../models/FileRatings.model';
import { FormControl, FormGroup, FormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-nlp-process',
  standalone: true,
  imports: [MatTableModule, MatCheckboxModule, FormsModule],
  templateUrl: './nlp-process.component.html',
  styleUrl: './nlp-process.component.css'
})
export class NlpProcessComponent implements OnInit {
  displayedColumns: string[] = ['select', 'fileName', 'fileRatings'];
  dataSource = new MatTableDataSource<FileRatings>();
  selection = new SelectionModel<FileRatings>(true, []);
  fileRatings: FileRatings[] = [];
  queryString = '';

  constructor(private nlpService: NlpService) { }

  ngOnInit(): void { }

  find() {
    console.log(this.queryString)
    this.nlpService.processNLP(this.queryString).subscribe(
      (resp) => {
        this.fileRatings = resp;
        console.log(this.fileRatings);
        this.dataSource = new MatTableDataSource<FileRatings>(this.fileRatings);

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
  }
  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: FileRatings): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.fileName + 1}`;
  }
}
