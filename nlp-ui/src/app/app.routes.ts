import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { NlpProcessComponent } from './components/nlp-process/nlp-process.component';
import { FileListComponent } from './components/file-list/file-list.component';

export const routes: Routes = [
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent },
    { path: 'file-upload', component: FileListComponent },
    { path: 'nlp', component: NlpProcessComponent }
];
