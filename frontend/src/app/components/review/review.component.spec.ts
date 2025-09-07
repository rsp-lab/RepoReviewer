import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { ScanResultModel } from '../../models/scan-result.model';
import { NavigationService } from '../../services/navigation.service';
import { ReviewComponent } from './review.component';

describe('ReviewComponent', () => {
    let component: ReviewComponent;
    let fixture: ComponentFixture<ReviewComponent>;

    const mockScanResult: ScanResultModel = {
        id: 'UUID-1',
        repositoryUrl: 'https://github.com/mock-user/mock-project',
        scanDateTimeUtc0: '2025-09-05 12:00 UTC+00',
        analyzedFilesCount: 3,
        totalIssueCount: 18,
        issueCountLow: 7,
        issueCountMedium: 5,
        issueCountCritical: 6,
        codeReviewTimeInMs: 1000,
        totalRepoSizeInKB: 500,
        summary: "some overview of the repository",
        issues: [
            { filePath: 'file1.ts', lineNumber: 11, severity: 'LOW', issueDescription: 'desc1', suggestedAction: 'action1' },
            { filePath: 'file1.ts', lineNumber: 12, severity: 'LOW', issueDescription: 'desc2', suggestedAction: 'action2' },
            { filePath: 'file1.ts', lineNumber: 13, severity: 'MEDIUM', issueDescription: 'desc3', suggestedAction: 'action3' },
            { filePath: 'file1.ts', lineNumber: 14, severity: 'CRITICAL', issueDescription: 'desc4', suggestedAction: 'action4' },
            { filePath: 'file1.ts', lineNumber: 15, severity: 'CRITICAL', issueDescription: 'desc5', suggestedAction: 'action5' },
            { filePath: 'file2.ts', lineNumber: 21, severity: 'MEDIUM', issueDescription: 'desc6', suggestedAction: 'action6' },
            { filePath: 'file2.ts', lineNumber: 22, severity: 'MEDIUM', issueDescription: 'desc7', suggestedAction: 'action7' },
            { filePath: 'file2.ts', lineNumber: 23, severity: 'LOW', issueDescription: 'desc8', suggestedAction: 'action8' },
            { filePath: 'file2.ts', lineNumber: 24, severity: 'LOW', issueDescription: 'desc9', suggestedAction: 'action9' },
            { filePath: 'file2.ts', lineNumber: 25, severity: 'LOW', issueDescription: 'desc10', suggestedAction: 'action10' },
            { filePath: 'file2.ts', lineNumber: 26, severity: 'CRITICAL', issueDescription: 'desc11', suggestedAction: 'action11' },
            { filePath: 'file2.ts', lineNumber: 27, severity: 'CRITICAL', issueDescription: 'desc12', suggestedAction: 'action12' },
            { filePath: 'file3.ts', lineNumber: 31, severity: 'CRITICAL', issueDescription: 'desc13', suggestedAction: 'action13' },
            { filePath: 'file3.ts', lineNumber: 32, severity: 'MEDIUM', issueDescription: 'desc14', suggestedAction: 'action14' },
            { filePath: 'file3.ts', lineNumber: 33, severity: 'CRITICAL', issueDescription: 'desc15', suggestedAction: 'action15' },
            { filePath: 'file3.ts', lineNumber: 34, severity: 'MEDIUM', issueDescription: 'desc16', suggestedAction: 'action16' },
            { filePath: 'file3.ts', lineNumber: 35, severity: 'LOW', issueDescription: 'desc17', suggestedAction: 'action17' },
            { filePath: 'file3.ts', lineNumber: 36, severity: 'LOW', issueDescription: 'desc18', suggestedAction: 'action18' },
        ]
    };

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [ ReviewComponent ],
            providers: [
                { provide: NavigationService, useValue: {} },
                { provide: ActivatedRoute, useValue: { snapshot: {}, params: of({}) } }
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(ReviewComponent);
        component = fixture.componentInstance;
    });

    it('should create component ReviewComponent', () => {
        expect(component).toBeTruthy();
    });

    it('should initialize totalPages and doughnutChartData correctly in ngOnInit()', () => {
        Object.defineProperty(window.history, 'state', {
            writable: true,
            value: { scanResult: mockScanResult },
        });

        component.ngOnInit();

        expect(component.totalPages).toBe(2);
        expect(component.doughnutChartData?.datasets[0].data).toEqual([ 7, 5, 6 ]);
    });

    it('should return paginated issues correctly', () => {
        component['sortedIssueList'] = mockScanResult.issues;
        component.currentPage = 1;

        const paged = component.pagedIssues;
        expect(paged.length).toBe(15);
        expect(paged[0].filePath).toBe('file1.ts');
    });

    it('should toggle issue expansion correctly', () => {
        const issue = mockScanResult.issues[0];

        expect(component.expandedIssues.has(issue)).toBeFalse();
        component.toggleIssue(issue);
        expect(component.expandedIssues.has(issue)).toBeTrue();
        component.toggleIssue(issue);
        expect(component.expandedIssues.has(issue)).toBeFalse();
    });

    it('should toggle full expansion correctly', () => {
        component['sortedIssueList'] = mockScanResult.issues;

        expect(component.isFullExpanded).toBe(false);

        component.toggleFullExpand();
        expect(component.expandedIssues.size).toBe(18);
        expect(component.isFullExpanded).toBe(true);

        component.toggleFullExpand();
        expect(component.expandedIssues.size).toBe(0);
        expect(component.isFullExpanded).toBe(false);
    });
});
