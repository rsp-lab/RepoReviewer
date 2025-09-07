import { NgOptimizedImage, NgStyle } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ChartData, ChartOptions } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { IssueModel } from '../../models/issue.model';
import { ScanResultModel } from '../../models/scan-result.model';
import { NavigationService } from '../../services/navigation.service';
import { PdfGeneratorService } from '../../services/pdf-generator.service';
import { CssVarUtil } from '../../utility/css-var.util';
import { sortByGenericField } from '../../utility/sort.util';
import { LoadingModalComponent } from '../loading-modal/loading-modal.component';
import { NavBarComponent } from '../nav-bar/nav-bar.component';

@Component({
    standalone: true,
    selector: 'app-review',
    imports: [ BaseChartDirective, NgOptimizedImage, NgStyle, NavBarComponent, LoadingModalComponent ],
    templateUrl: './review.component.html',
    styleUrl: './review.component.css'
})
export class ReviewComponent implements OnInit
{
    private readonly pdfFilename = 'Code review report.pdf';
    scanResult?: ScanResultModel;

    private sortedIssueList: IssueModel[] = [];
    sortField?: keyof IssueModel;
    sortDirection: 'asc' | 'desc' = 'asc';

    private readonly pageSize: number = 15;
    currentPage: number = 1;
    totalPages: number = 1;

    expandedIssues: Set<IssueModel> = new Set();
    isFullExpanded: boolean = false;

    isLoading = false;

    severityColors = CssVarUtil.getSeverityColors();

    constructor(private readonly navigation: NavigationService,
                private readonly pdfService: PdfGeneratorService) { }

    doughnutChartData?: ChartData<'doughnut'>;
    doughnutChartOptions: ChartOptions<'doughnut'> = {
        cutout: '60%',
        plugins: {
            tooltip: {
                enabled: false,
                bodyFont: { size: 18 },
                titleFont: { size: 18 },
                padding: 12
            },
            legend: {
                display: true,
                position: 'bottom',
                labels: {
                    font: { size: 20 },
                    padding: 20,
                    usePointStyle: true,
                    pointStyle: 'circle',

                    generateLabels: (chart) => {
                        const labels = (chart.data.labels ?? []) as string[];
                        const datasets = chart.data.datasets ?? [];

                        if (labels.length && datasets.length) {
                            const dataset = datasets[0];
                            const dataArray = (dataset.data ?? []) as number[];
                            const colors = (dataset.backgroundColor ?? []) as string[];

                            return labels.map((label, i) => {
                                const value = dataArray[i] ?? 0;
                                const backgroundColor = colors[i] ?? '#000';

                                return {
                                    text: `${label} (${value})`,
                                    fillStyle: backgroundColor,
                                    lineWidth: 0, // Grubość obramowania w legendzie
                                    hidden: !chart.getDataVisibility(i),
                                    index: i
                                };
                            });
                        }
                        return [];
                    }
                }
            }
        }
    };

    ngOnInit(): void {
        this.scanResult = history.state.scanResult;
        console.log('Received scanResult:', this.scanResult);

        if (this.scanResult) {
            this.sortedIssueList = [ ...this.scanResult.issues ];

            this.totalPages = Math.max(1, Math.ceil(this.sortedIssueList.length / this.pageSize));

            this.doughnutChartData = {
                labels: [ 'Minor', 'Major', 'Critical' ],
                datasets: [ {
                        data: [
                            this.scanResult.issueCountLow,
                            this.scanResult.issueCountMedium,
                            this.scanResult.issueCountCritical
                        ],
                        backgroundColor: [
                            CssVarUtil.getCssVariable('--color-secondary-dark'),
                            CssVarUtil.getCssVariable('--color-extra'),
                            CssVarUtil.getCssVariable('--color-primary-light'),
                        ],
                        hoverOffset: 20
                    } ],
            };
        }
    }

    sortByField(field: keyof IssueModel) {
        console.log('Sorting by field:', field);

        const result = sortByGenericField(this.sortedIssueList, field, this.sortField, this.sortDirection);
        this.sortedIssueList = result.sortedArray;
        this.sortDirection = result.newDirection;
        this.sortField = field;

        this.currentPage = 1;
    }

    get pagedIssues(): IssueModel[] {
        if (!this.sortedIssueList)
            return [];

        const startIndex = (this.currentPage - 1) * this.pageSize;
        return this.sortedIssueList.slice(startIndex, startIndex + this.pageSize);
    }

    async exportToPdf(): Promise<void> {
        if (!this.scanResult)
            return;

        this.isLoading = true;

        const pdf = await this.pdfService.generateCodeReviewPdf(
            this.scanResult,
            'canvas[baseChart]',
            this.sortedIssueList
        );
        pdf.save(this.pdfFilename);
        this.isLoading = false;
    }

    toggleIssue(issue: IssueModel) {
        if (this.expandedIssues.has(issue))
            this.expandedIssues.delete(issue);
        else
            this.expandedIssues.add(issue);
    }

    toggleFullExpand(): void {
        if (this.isFullExpanded)
            this.expandedIssues.clear();
        else
            this.sortedIssueList.forEach(issue => this.expandedIssues.add(issue));
        this.isFullExpanded = !this.isFullExpanded;
    }

    navigateTo(path: string, extras?: any): void {
        this.navigation.executeNavigation(path, extras);
    }
}
