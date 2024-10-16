import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { NlpProcessComponent } from './components/nlp-process/nlp-process.component';
import { FileListComponent } from './components/file-list/file-list.component';

export const routes: Routes = [
    { path: '', redirectTo: '/nlp', pathMatch: 'full' },
    { path: 'file-upload', component: FileListComponent },
    { path: 'nlp', component: NlpProcessComponent }
];
