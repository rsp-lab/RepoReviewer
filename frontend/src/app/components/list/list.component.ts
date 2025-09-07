import { NgOptimizedImage } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ScanEntryModel } from '../../models/scan-entry.model';
import { ScanResultModel } from '../../models/scan-result.model';
import { CodeReviewService } from '../../services/code-review.service';
import { NavigationService } from '../../services/navigation.service';
import { sortByGenericField } from '../../utility/sort.util';
import { NavBarComponent } from '../nav-bar/nav-bar.component';

@Component({
    standalone: true,
    selector: 'app-list.component',
    imports: [ NgOptimizedImage, NavBarComponent ],
    templateUrl: './list.component.html',
    styleUrl: './list.component.css'
})
export class ListComponent implements OnInit
{
    private sortedScanList: ScanEntryModel[] = [];
    sortField: string = '';
    sortDirection: 'asc' | 'desc' = 'desc';

    private readonly pageSize: number = 8;
    currentPage: number = 1;
    totalPages: number = 1;

    constructor(private readonly navigation: NavigationService,
                private readonly codeReviewService: CodeReviewService) { }

    ngOnInit(): void {
        this.sortByField('scanDateTimeUtc0');
        this.codeReviewService.getReviewEntryList()
            .subscribe({
                next: (result: ScanEntryModel[]) => this.handleFetchSuccess(result),
                error: (err) => console.error('Failed to fetch review list', err)
            });
    }

    private handleFetchSuccess(result: ScanEntryModel[]) {
        console.log('Fetching review list successfully.')
        this.sortedScanList = result;
        this.totalPages = Math.max(1, Math.ceil(this.sortedScanList.length / this.pageSize));

        this.sortByField('scanDateTimeUtc0');
        this.sortDirection = 'desc';
    }

    sortByField(field: keyof ScanEntryModel) {
        const result = sortByGenericField(this.sortedScanList, field, this.sortField as keyof ScanEntryModel, this.sortDirection);
        this.sortedScanList = result.sortedArray;
        this.sortDirection = result.newDirection;
        this.sortField = field;

        this.currentPage = 1;
    }

    get pagedScanEntries(): ScanEntryModel[] {
        if (!this.sortedScanList)
            return [];

        const startIndex = (this.currentPage - 1) * this.pageSize;
        return this.sortedScanList.slice(startIndex, startIndex + this.pageSize);
    }

    reloadList() {
        this.codeReviewService.getReviewEntryList()
            .subscribe({
                next: (result: ScanEntryModel[]) => this.handleFetchSuccess(result),
                error: (err) => console.error('Failed to fetch review list', err)
            });
    }

    showDetails(entry: ScanEntryModel): void {
        this.codeReviewService.getReviewEntry(entry.id)
            .subscribe({
                next: (result: ScanResultModel) => this.navigateTo('/review', { state: { scanResult: result } }),
                error: (err) => console.error('Failed to fetch review entry', err)
            });
    }

    deleteEntry(entry: ScanEntryModel): void {
        const previousPage = this.currentPage;

        this.codeReviewService.deleteReviewEntry(entry.id)
            .subscribe({
                next: () => {
                    console.log(`Entry ${entry.id} deleted`);

                    this.codeReviewService.getReviewEntryList()
                        .subscribe({
                            next: (result: ScanEntryModel[]) => {
                                this.handleFetchSuccess(result);

                                // Przywróć poprzednią stronę, jeśli to możliwe
                                if (previousPage > this.totalPages)
                                    this.currentPage = this.totalPages;
                                else
                                    this.currentPage = previousPage;
                            },
                            error: err => console.error('Failed to fetch review list after deletion', err)
                        });
                },
                error: err => console.error('Failed to delete review entry', err)
            });
    }

    navigateTo(path: string, extras?: any): void {
        this.navigation.executeNavigation(path, extras);
    }
}
