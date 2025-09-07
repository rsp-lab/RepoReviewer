import { IssueModel } from './issue.model';

export interface ScanResultModel
{
    id: string,
    repositoryUrl: string,
    scanDateTimeUtc0: string,
    analyzedFilesCount: number;
    totalIssueCount: number;
    issueCountLow: number;
    issueCountMedium: number;
    issueCountCritical: number;
    codeReviewTimeInMs: number;
    totalRepoSizeInKB: number;
    summary: string,
    issues: IssueModel[];
}
