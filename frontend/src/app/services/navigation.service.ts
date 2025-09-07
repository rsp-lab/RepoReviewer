import { Injectable } from '@angular/core';
import { NavigationExtras, Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class NavigationService
{
    constructor(private readonly router: Router) { }

    executeNavigation(path: string, extras?: NavigationExtras): void {
        console.log('Navigating to ' + path + ' with extras:', extras);
        this.router.navigate([ path ], extras)
            .then(success => {
                if (!success)
                    console.warn(`Navigation to '${path}' with extras:`, extras, 'failed!');
            });
    }
}
