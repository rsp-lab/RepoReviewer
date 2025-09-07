import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Title } from '@angular/platform-browser';

@Component({
    standalone: true,
    selector: 'app-root',
    imports: [ RouterOutlet ],
    templateUrl: './app.component.html',
    styleUrls: [ './app.component.css' ]
})
export class AppComponent
{
    title: string = "RepoReviewer";

    constructor(private titleService: Title) {
        this.titleService.setTitle(this.title);
    }
}
