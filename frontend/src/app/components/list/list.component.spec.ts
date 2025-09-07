import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { ScanEntryModel } from '../../models/scan-entry.model';
import { CodeReviewService } from '../../services/code-review.service';
import { ListComponent } from './list.component';

describe('ListComponent', () => {
    let component: ListComponent;
    let fixture: ComponentFixture<ListComponent>;
    let codeReviewService: jasmine.SpyObj<CodeReviewService>;

    const mockScanEntries: ScanEntryModel[] = [
        {
            id: 'UUID-1',
            repositoryUrl: 'https://github.com/mock-user/mock-project1',
            scanDateTimeUtc0: '2025-09-05 18:30 UTC+00'
        },
        {
            id: 'UUID-2',
            repositoryUrl: 'https://github.com/mock-user/mock-project2',
            scanDateTimeUtc0: '2025-09-05 14:00 UTC+00'
        },
        {
            id: 'UUID-3',
            repositoryUrl: 'https://github.com/mock-user/mock-project3',
            scanDateTimeUtc0: '2025-09-05 22:00 UTC+00'
        },
        {
            id: 'UUID-4',
            repositoryUrl: 'https://github.com/mock-user/mock-project4',
            scanDateTimeUtc0: '2025-09-05 12:00 UTC+00'
        },
        {
            id: 'UUID-5',
            repositoryUrl: 'https://github.com/mock-user/mock-project5',
            scanDateTimeUtc0: '2025-09-05 20:55 UTC+00'
        },
        {
            id: 'UUID-6',
            repositoryUrl: 'https://github.com/mock-user/mock-project6',
            scanDateTimeUtc0: '2025-09-05 16:00 UTC+00'
        },
        {
            id: 'UUID-7',
            repositoryUrl: 'https://github.com/mock-user/mock-project7',
            scanDateTimeUtc0: '2025-09-06 16:00 UTC+00'
        },
        {
            id: 'UUID-8',
            repositoryUrl: 'https://github.com/mock-user/mock-project8',
            scanDateTimeUtc0: '2025-09-06 14:14 UTC+00'
        },
        {
            id: 'UUID-9',
            repositoryUrl: 'https://github.com/mock-user/mock-project9',
            scanDateTimeUtc0: '2025-09-06 10:00 UTC+00'
        },
        {
            id: 'UUID-10',
            repositoryUrl: 'https://github.com/mock-user/mock-project10',
            scanDateTimeUtc0: '2025-09-06 10:11 UTC+00'
        }
    ];

    beforeEach(async () => {
        const codeReviewSpy = jasmine.createSpyObj('CodeReviewService', [
            'getReviewEntryList', 'getReviewEntry', 'deleteReviewEntry'
        ]);

        await TestBed.configureTestingModule({
            imports: [ ListComponent ], // standalone component
            providers: [
                { provide: CodeReviewService, useValue: codeReviewSpy },
                { provide: ActivatedRoute, useValue: { snapshot: {}, params: of({}) } }
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(ListComponent);
        component = fixture.componentInstance;

        codeReviewService = TestBed.inject(CodeReviewService) as jasmine.SpyObj<CodeReviewService>;

        component.currentPage = 1;
    });

    it('should create component ListComponent', () => {
        expect(component).toBeTruthy();
    });

    it('should initialize totalPages correctly in ngOnInit()', () => {
        codeReviewService.getReviewEntryList.and.returnValue(of(mockScanEntries));

        component.ngOnInit();

        expect(codeReviewService.getReviewEntryList).toHaveBeenCalled();
        expect(component.totalPages).toBe(2);
        expect((component as any).sortedScanList.length).toBe(10);
    });

    describe('deleteEntry', () => {
        it('should keep currentPage when totalPages >= previousPage', () => {
            let entryList: ScanEntryModel[] = [ ...mockScanEntries ];
            codeReviewService.deleteReviewEntry.and.returnValue(of(undefined));
            codeReviewService.getReviewEntryList.and.returnValue(of(entryList));

            component.deleteEntry(entryList[0]);

            expect(component.totalPages).toBe(2);
            expect(component.currentPage).toBe(1);
        });

        it('should adjust currentPage correctly when totalPages < previousPage', () => {
            let entryList: ScanEntryModel[] = mockScanEntries.slice(0, 8).map(entry => ({ ...entry }));
            codeReviewService.deleteReviewEntry.and.returnValue(of(undefined));
            codeReviewService.getReviewEntryList.and.returnValue(of(entryList));

            component.currentPage = 2;
            component.deleteEntry(entryList[0]);

            expect(component.totalPages).toBe(1);
            expect(component.currentPage).toBe(1);
        });
    });

    it('should handleFetchSuccess correctly', () => {
        (component as any).handleFetchSuccess(mockScanEntries);

        expect((component as any).sortedScanList[0].id).toBe('UUID-4');
        expect(component.totalPages).toBe(2);
        expect(component.sortDirection).toBe('desc');
    });

    it('should return paginated entries correctly', () => {
        component['sortedScanList'] = mockScanEntries;
        const paged = component.pagedScanEntries;
        expect(paged.length).toBe(8);
    });
});
