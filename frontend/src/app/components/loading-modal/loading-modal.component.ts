import { Component, Input } from '@angular/core';

@Component({
    standalone: true,
    selector: 'app-loading-modal',
    templateUrl: './loading-modal.component.html',
    styleUrl: './loading-modal.component.css'
})
export class LoadingModalComponent
{
    @Input() text: string = 'Loading...';
    @Input() isShowing: boolean = false;
}
