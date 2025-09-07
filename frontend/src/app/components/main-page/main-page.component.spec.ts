import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ScanResultModel } from '../../models/scan-result.model';
import { CodeReviewService } from '../../services/code-review.service';
import { NavigationService } from '../../services/navigation.service';
import { MainPageComponent } from './main-page.component';

describe('MainPageComponent', () => {
    let component: MainPageComponent;
    let fixture: ComponentFixture<MainPageComponent>;
    // Partial<> <- Wszystkie pola i metody typu T są opcjonalne, TypeScript nie będzie narzekał jak nie znajdzie
    let codeReviewService: Partial<CodeReviewService>;
    let navigationService: Partial<NavigationService>;

    const mockScanResult: ScanResultModel = {
        id: 'UUID-1',
        repositoryUrl: 'http://github.com/mock-user/mock-repo',
        scanDateTimeUtc0: '2024-09-05 12:00 UTC',
        analyzedFilesCount: 10,
        totalIssueCount: 5,
        issueCountLow: 1,
        issueCountMedium: 2,
        issueCountCritical: 2,
        codeReviewTimeInMs: 1500,
        totalRepoSizeInKB: 500,
        summary: "some overview of the repository",
        issues: []
    };

    beforeEach(() => {
        // Mockujemy tylko niezbędne metody, serwisy są Partial<>
        codeReviewService = { sendRepositoryUrlToReview: jasmine.createSpy('sendRepositoryUrlToReview') };
        navigationService = { executeNavigation: jasmine.createSpy('executeNavigation') };

        TestBed.configureTestingModule({
            imports: [ MainPageComponent ],
            providers: [
                { provide: CodeReviewService, useValue: codeReviewService },
                { provide: NavigationService, useValue: navigationService },
                { provide: ActivatedRoute, useValue: { snapshot: {}, params: of({}) } }
            ]
        });

        fixture = TestBed.createComponent(MainPageComponent);
        component = fixture.componentInstance;
    });

    it('should create the component MainPageComponent', () => {
        expect(component).toBeTruthy();
    });

    it('should toggle flag isShowingSettings correctly', () => {
        expect(component.isShowingSettings).toBe(false);
        component.toggleSettings();
        expect(component.isShowingSettings).toBe(true);
        component.toggleSettings();
        expect(component.isShowingSettings).toBe(false);
    });

    describe('reviewRepository()', () => {
        it('should call sendRepositoryUrlToReview() and navigate on success', () => {
            // of(mockScanResult) jest wykonywane synchronicznie, więc od razu wykona finalize().
            (codeReviewService.sendRepositoryUrlToReview as jasmine.Spy)
                .and.returnValue(of(mockScanResult));

            component.reviewRepository();

            // Ponieważ działa to synchronicznie isLoading tutaj zawsze jest false po finalize().
            expect(component.isLoading).toBe(false);
            // Sprawdź, czy sendRepositoryUrlToReview został wywołany z konkretnymi argumentami
            expect(codeReviewService.sendRepositoryUrlToReview).toHaveBeenCalledWith(component.repoInput);
            expect(navigationService.executeNavigation).toHaveBeenCalledWith(
                '/review',
                { state: { scanResult: mockScanResult } }
            );
        });

        it('should call sendRepositoryUrlToReview() and set errorMessage on error', () => {
            const errorResponse = { error: { message: 'Network error' } };
            (codeReviewService.sendRepositoryUrlToReview as jasmine.Spy)
                .and.returnValue(throwError(() => errorResponse));

            component.reviewRepository();

            expect(component.isLoading).toBe(false);
            expect(component.errorMessage).toBe('Network error');
        });
    });

    it('should fade phrases correctly in ngOnInit()', fakeAsync(() => {
        component.ngOnInit();

        const testCycles = 5;
        let previousPhrase = component.currentPhrase;

        tick(2500);
        expect(component.isFading).toBe(true);

        for (let i = 0; i < testCycles; i++) {
            tick(500);
            expect(component.isFading).toBe(false);
            expect(component.currentPhrase).not.toBe(previousPhrase);
            previousPhrase = component.currentPhrase;

            tick(2000)
            expect(component.isFading).toBe(true);
        }
    }));
});
