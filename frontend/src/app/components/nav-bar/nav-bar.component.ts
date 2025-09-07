import { NgClass, NgOptimizedImage } from '@angular/common';
import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
    standalone: true,
    selector: 'app-nav-bar',
    imports: [ RouterLink, NgClass, NgOptimizedImage ],
    templateUrl: './nav-bar.component.html',
    styleUrl: './nav-bar.component.css'
})
export class NavBarComponent
{
    @Input() navbarClass: string = 'navbar-no-background';
    @Input() logoClass: string = 'text-logo-dark';
}
