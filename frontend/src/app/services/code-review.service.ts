import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GitRepoModel } from '../models/git-repo.model';
import { ScanEntryModel } from '../models/scan-entry.model';
import { ScanResultModel } from '../models/scan-result.model';

@Injectable({
    providedIn: 'root'
})
export class CodeReviewService
{
    private readonly apiUrlScan;
    private readonly apiUrlReviewDataManagement;

    constructor(private readonly http: HttpClient) {
        this.apiUrlScan = 'http://localhost:8080/scan';
        this.apiUrlReviewDataManagement = 'http://localhost:8080/review';
    }

    sendRepositoryUrlToReview(gitRepo: GitRepoModel): Observable<ScanResultModel> {
        console.info('Sending POST ' + this.apiUrlScan + ' with url: ' + gitRepo.repoUrl);
        return this.http.post<ScanResultModel>(this.apiUrlScan, gitRepo);
    }

    getReviewEntryList(): Observable<ScanEntryModel[]> {
        console.info('Sending GET ' + this.apiUrlReviewDataManagement);
        return this.http.get<ScanEntryModel[]>(this.apiUrlReviewDataManagement);
    }

    getReviewEntry(id: string): Observable<ScanResultModel> {
        console.info('Sending GET ' + this.apiUrlReviewDataManagement + '/' + id);
        return this.http.get<ScanResultModel>(`${this.apiUrlReviewDataManagement}/${id}`);
    }

    deleteReviewEntry(id: string): Observable<void> {
        console.info('Sending DELETE ' + this.apiUrlReviewDataManagement + '/' + id);
        return this.http.delete<void>(`${this.apiUrlReviewDataManagement}/${id}`);
    }
}
