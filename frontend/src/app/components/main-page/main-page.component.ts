import { NgOptimizedImage } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, interval, Subject, takeUntil } from 'rxjs';
import { GitRepoModel } from '../../models/git-repo.model';
import { ScanResultModel } from '../../models/scan-result.model';
import { CodeReviewService } from '../../services/code-review.service';
import { NavigationService } from '../../services/navigation.service';
import { LoadingModalComponent } from '../loading-modal/loading-modal.component';
import { NavBarComponent } from '../nav-bar/nav-bar.component';

@Component({
    standalone: true,
    /*
        Komponentu można używać w innym przez znacznik <app-main-page></app-main-page>.
        Konwencja, aby zaczynać od app i nie kolidować ze znacznikami html, technicznie nie potrzebny,
          jeśli tworzysz komponent tylko po to, żeby używać go w RouterOutlet.
    */
    selector: 'app-main-page',
    imports: [ FormsModule, NgOptimizedImage, LoadingModalComponent, NavBarComponent ],
    templateUrl: './main-page.component.html',
    styleUrl: './main-page.component.css'
})
// export <- dzięki temu słowu można to zaimportować w innych plikach, nie tylko używać w tym samym
export class MainPageComponent implements OnInit, OnDestroy
{
    repoInput: GitRepoModel = {
        repoUrl: '',
        mode: 'one_prompt'
    };

    private readonly phrases = [ 'Code Quality', 'Code Security', 'Code Performance', 'Code Readability' ];
    currentPhrase: string = this.phrases[0];

    errorMessage: string = '';
    isFading = false;
    isShowingSettings: boolean = false;
    isLoading = false;

    /*
        Konwencja, na końcu jest $, co oznacza, że to Observable/Subject, czyli strumień,
          a nie standardowa flaga czy coś.
    */
    private readonly destroyed$ = new Subject<void>();

    constructor(private readonly navigation: NavigationService,
                private readonly codeReviewService: CodeReviewService) { }

    ngOnInit() {
        let index = 0;
        /*
            Tworzy observable/stream emitujący liczby {0, 1, 2, ...} co 2,5 sekundy.
            takeUntil -> Subskrybuj się do strumienia, dopóki destroyed$ nie wyemituje czegoś.
        */
        interval(2500)
            // .pipe <- tworzy nowy strumień, pozwala używać operacji filter, map
            .pipe(takeUntil(this.destroyed$))
            .subscribe(() => {
                this.isFading = true;
                setTimeout(() => {
                    index = (index + 1) % this.phrases.length;
                    this.currentPhrase = this.phrases[index];
                    this.isFading = false;
                }, 500);
            });
    }

    ngOnDestroy() {
        /*
            Anuluje subskrypcję. Nie ma wycieków pamięci, gdy mimo zniszczonego komponentu
              subskrypcje nadal pozostaną aktywne.
        */
        this.destroyed$.next(); // wysyła wartość, inaczej sygnalizuje coś wszystkim z takeUntil
        this.destroyed$.complete(); // sygnalizuje, że strumień nie będzie już emitował kolejnych wartości
    }

    reviewRepository(): void {
        this.errorMessage = ''; // Wyczyść poprzedni error
        this.isLoading = true;

        this.codeReviewService.sendRepositoryUrlToReview(this.repoInput)
            .pipe(finalize(() => this.isLoading = false))
            .subscribe({
                next: (result: ScanResultModel) => this.handleReviewSuccess(result),
                error: (error) => this.handleReviewError(error)
            });
    }

    private handleReviewSuccess(result: ScanResultModel) {
        this.navigateTo('/review', { state: { scanResult: result } });
    }

    private handleReviewError(error: any) {
        console.error('Unexpected error occurred during review process!');
        this.errorMessage = error?.error?.message || 'Unexpected error occurred.';
    }

    navigateTo(path: string, extras?: any): void {
        this.navigation.executeNavigation(path, extras);
    }

    toggleSettings(): void {
        this.isShowingSettings = !this.isShowingSettings;
    }
}
