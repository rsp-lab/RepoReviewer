export interface IssueModel
{
    filePath: string;
    lineNumber: number;
    severity: 'LOW' | 'MEDIUM' | 'CRITICAL';
    issueDescription: string;
    suggestedAction: string;
}
