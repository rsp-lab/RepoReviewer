import { Routes } from '@angular/router';
import { MainPageComponent } from './components/main-page/main-page.component';
import { ReviewComponent } from './components/review/review.component';
import { ListComponent } from './components/list/list.component';

export const routes: Routes = [
    { path: '', component: MainPageComponent },
    { path: 'review', component: ReviewComponent },
    { path: 'list', component: ListComponent }
];
