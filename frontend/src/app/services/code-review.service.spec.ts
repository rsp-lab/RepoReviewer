import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { GitRepoModel } from '../models/git-repo.model';
import { ScanEntryModel } from '../models/scan-entry.model';
import { ScanResultModel } from '../models/scan-result.model';
import { CodeReviewService } from './code-review.service';

describe('CodeReviewService', () => {
    let service: CodeReviewService;
    let httpMock: HttpTestingController;

    const mockScanResult: ScanResultModel = {
        id: 'UUID-1',
        repositoryUrl: 'http://github.com/mock-user1/mock-repo1',
        scanDateTimeUtc0: '2024-04-22 19:23 UTC+00',
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

    const mockScanEntries: ScanEntryModel[] = [
        {
            id: 'UUID-1',
            repositoryUrl: 'http://github.com/mock-user1/mock-repo1',
            scanDateTimeUtc0: '2024-04-22 19:23 UTC+00'
        },
        {
            id: 'UUID-2',
            repositoryUrl: 'http://github.com/mock-user2/mock-repo2',
            scanDateTimeUtc0: '2024-09-05 12:00 UTC+00'
        }
    ]

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [ CodeReviewService, provideHttpClient(), provideHttpClientTesting() ]
        });

        service = TestBed.inject(CodeReviewService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpMock.verify();
    });

    it('should POST with repository url to review', () => {
        const gitRepo: GitRepoModel = { repoUrl: 'http://github.com/mock-user/mock-repo', mode: 'prompt_per_class' } as GitRepoModel;

        service.sendRepositoryUrlToReview(gitRepo).subscribe(result => {
            expect(result).toEqual(mockScanResult);
        });

        const req = httpMock.expectOne('http://localhost:8080/scan');
        expect(req.request.method).toBe('POST');
        expect(req.request.body).toEqual(gitRepo);

        req.flush(mockScanResult);
    });

    it('should GET a review entry list', () => {
        service.getReviewEntryList().subscribe(result => {
            expect(result).toEqual(mockScanEntries);
        });

        const req = httpMock.expectOne('http://localhost:8080/review');
        expect(req.request.method).toBe('GET');

        req.flush(mockScanEntries);
    });

    it('should GET a single review entry', () => {
        service.getReviewEntry('UUID-1').subscribe(result => {
            expect(result).toEqual(mockScanResult);
        });

        const req = httpMock.expectOne('http://localhost:8080/review/UUID-1');
        expect(req.request.method).toBe('GET');

        req.flush(mockScanResult);
    });

    it('should DELETE a review entry', () => {
        service.deleteReviewEntry('UUID-2').subscribe(result => { });

        const req = httpMock.expectOne('http://localhost:8080/review/UUID-2');
        expect(req.request.method).toBe('DELETE');

        req.flush(null);
    });
});
