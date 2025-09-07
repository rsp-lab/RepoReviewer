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
    selector: 'app-main-page',
    imports: [ FormsModule, NgOptimizedImage, LoadingModalComponent, NavBarComponent ],
    templateUrl: './main-page.component.html',
    styleUrl: './main-page.component.css'
})
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

    private readonly destroyed$ = new Subject<void>();

    constructor(private readonly navigation: NavigationService,
                private readonly codeReviewService: CodeReviewService) { }

    ngOnInit() {
        let index = 0;
        interval(2500)
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
        this.destroyed$.next();
        this.destroyed$.complete();
    }

    reviewRepository(): void {
        this.errorMessage = '';
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
