import { Injectable } from '@angular/core';
import html2canvas from 'html2canvas';
import { lineHeight } from 'html2canvas/dist/types/css/property-descriptors/line-height';
import JsPDF from 'jspdf';
import { IssueModel } from '../models/issue.model';
import { ScanResultModel } from '../models/scan-result.model';
import { CssVarUtil } from '../utility/css-var.util';

@Injectable({
    providedIn: 'root'
})
export class PdfGeneratorService
{
    readonly EXTRA_LEFT_MARGIN = 42;
    readonly LINE_HEIGHT = 6;
    readonly FONT_NAME = 'helvetica';
    readonly FONT_SIZE = 12;

    async generateCodeReviewPdf(scanResult: ScanResultModel, doughnutChartSelector: string, issues?: IssueModel[]):
        Promise<JsPDF> {
        console.log('Exporting code review to pdf.');

        const margin = 10;
        const pdf = new JsPDF('p', 'mm', 'a4');
        const pdfWidth = pdf.internal.pageSize.getWidth() - 2 * margin;
        const pdfHeight = pdf.internal.pageSize.getHeight() - 2 * margin;
        let yOffset = margin;

        // Funkcje pomocnicze
        const createTempDiv = (html: string): HTMLDivElement => {
            const tempDiv = document.createElement('div');
            tempDiv.style.width = '800px';
            tempDiv.style.position = 'absolute';
            tempDiv.style.left = '-9999px';
            tempDiv.style.top = '0';
            tempDiv.innerHTML = html;
            document.body.appendChild(tempDiv);
            return tempDiv;
        };

        const addHtmlToPdf = async (html: string): Promise<void> => {
            const tempDivForImage = createTempDiv(html);
            const canvas = await html2canvas(tempDivForImage, { scale: 3 });
            const imgData = canvas.toDataURL('image/png');
            const imgProps = pdf.getImageProperties(imgData);
            const imgHeight = (imgProps.height * pdfWidth) / imgProps.width;

            if (yOffset + imgHeight > pdfHeight + margin) {
                pdf.addPage();
                yOffset = margin;
            }

            pdf.addImage(imgData, 'PNG', margin, yOffset, pdfWidth, imgHeight);
            yOffset += imgHeight + 3;

            document.body.removeChild(tempDivForImage);
        };

        if (scanResult) {
            const summaryHtml = `
                <h1 class="my-3 text-center">Code review report</h1>
                <hr class="wide-line"/>
                <div class="d-flex justify-content-center mt-3">
                    <div style="font-size: 20px;">
                        <div class="stat-line mb-2">
                            <img src="/images/stat_web.png" alt="App Icon" width="32" height="32">
                            <strong>Repository URL:</strong>  ${scanResult.repositoryUrl}
                        </div>
                        <div class="stat-line mb-2">
                            <img src="/images/stat_calendar.png" alt="App Icon" width="32" height="32">
                            <strong>Date:</strong>  ${scanResult.scanDateTimeUtc0}
                        </div>
                        <div class="stat-line mb-2">
                            <img src="/images/stat_file.png" alt="App Icon" width="32" height="32">
                            <strong>Total analyzed files:</strong>  ${scanResult.analyzedFilesCount}
                        </div>
                        <div class="stat-line mb-2">
                            <img src="/images/stat_lightning.png" alt="App Icon" width="32" height="32">
                            <strong>Total issues:</strong>  ${scanResult.totalIssueCount}
                        </div>
                        <div class="stat-line mb-2">
                            <img src="/images/stat_timer.png" alt="App Icon" width="32" height="32">
                            <strong>Review time:</strong>  ${scanResult.codeReviewTimeInMs} ms
                        </div>
                        <div class="stat-line mb-2">
                            <img src="/images/stat_data.png" alt="App Icon" width="32" height="32">
                            <strong>Repository size:</strong>  ${scanResult.totalRepoSizeInKB} KB
                        </div>
                    </div>
                </div>
                <hr class="wide-line mb-5 mt-3"/>
            `;
            await addHtmlToPdf(summaryHtml);
        }

        if (scanResult.summary) {
            const extraLeftMargin = this.EXTRA_LEFT_MARGIN;
            const lineHeight = this.LINE_HEIGHT;
            const fontName = this.FONT_NAME;
            const fontSize = this.FONT_SIZE;

            const valueX = margin;
            const valueWidth = pdf.internal.pageSize.getWidth() - valueX - margin;

            pdf.setFont(fontName, 'bold');
            pdf.setFontSize(fontSize);
            pdf.text('Review summary:', margin, yOffset);
            yOffset += lineHeight + 3;

            pdf.setFont(fontName, 'normal');

            const paragraphs = scanResult.summary.split('\n');

            for (const paragraph of paragraphs) {
                const lines = pdf.splitTextToSize(paragraph, valueWidth);

                if (yOffset + lines.length * lineHeight > pdf.internal.pageSize.getHeight() - margin) {
                    pdf.addPage();
                    yOffset = margin;
                }

                pdf.text(lines, valueX, yOffset, { maxWidth: valueWidth, align: 'justify' });

                yOffset += lines.length * (lineHeight - 1);
            }
        }

        if (scanResult.summary) {
            pdf.setDrawColor(CssVarUtil.getCssVariable('--color-text-dark'));
            pdf.setLineWidth(0.5);
            pdf.line(margin, yOffset, pdf.internal.pageSize.getWidth() - margin, yOffset);
            yOffset += 1;
        }
        yOffset += 3;

        if (doughnutChartSelector) {
            const chartCanvas = document.querySelector(doughnutChartSelector) as HTMLCanvasElement;
            if (chartCanvas) {
                const chartImg = chartCanvas.toDataURL('image/png');
                const chartWidth = scanResult.summary ? '50%' : '70%';
                const chartHtml = `
                    <div style="display:flex; justify-content:center">
                        <img src="${chartImg}" style="width:${chartWidth}; height:auto"/>
                    </div>
                `;

                await addHtmlToPdf(chartHtml);
            }
        }

        if (!scanResult.summary) {
            pdf.addPage();
            yOffset = margin;
        }

        issues = issues ?? scanResult.issues;
        for (const issue of issues) {
            const extraLeftMargin = this.EXTRA_LEFT_MARGIN;
            const lineHeight = this.LINE_HEIGHT;
            const fontName = this.FONT_NAME;
            const fontSize = this.FONT_SIZE;

            pdf.setFontSize(fontSize);

            // Przewidujemy wysokość issue (w przybliżeniu)
            const valueX = margin + extraLeftMargin;
            // Szerokość dostępna do prawej krawędzi
            const valueWidth = pdf.internal.pageSize.getWidth() - valueX - margin;

            const descriptionLines = pdf.splitTextToSize(issue.issueDescription, valueWidth);
            const actionLines = pdf.splitTextToSize(issue.suggestedAction, valueWidth);
            const descriptionHeight = descriptionLines.length * (lineHeight - 1);
            const actionHeight = actionLines.length * (lineHeight - 1);

            const issueHeight = 3 * lineHeight + descriptionHeight + actionHeight;
            if (yOffset + issueHeight > pdf.internal.pageSize.getHeight() - margin) {
                pdf.addPage();
                yOffset = margin;
            }

            // Separator
            pdf.setDrawColor(CssVarUtil.getCssVariable('--color-text-dark'));
            pdf.setLineWidth(0.5);
            pdf.line(margin, yOffset, pdf.internal.pageSize.getWidth() - margin, yOffset);
            yOffset += 8;

            let severityColor = '#000';
            if (issue.severity === 'LOW') severityColor = CssVarUtil.getCssVariable('--color-secondary-dark');
            if (issue.severity === 'MEDIUM') severityColor = CssVarUtil.getCssVariable('--color-extra');
            if (issue.severity === 'CRITICAL') severityColor = CssVarUtil.getCssVariable('--color-primary-light');

            // Severity
            pdf.setFont(fontName, 'bold');
            pdf.setTextColor(severityColor);
            pdf.text(`Severity:`, margin, yOffset);
            pdf.text(`${issue.severity}`, margin + extraLeftMargin, yOffset);
            yOffset += lineHeight;

            pdf.setFont(fontName, 'normal');
            pdf.setTextColor(0, 0, 0);

            // File Path
            pdf.setFont(fontName, 'bold');
            pdf.text(`File Path:`, margin, yOffset);
            pdf.setFont(fontName, 'normal');
            pdf.text(`${issue.filePath}`, margin + extraLeftMargin, yOffset);
            yOffset += lineHeight;

            // Line Number
            pdf.setFont(fontName, 'bold');
            pdf.text(`Line Number:`, margin, yOffset);
            pdf.setFont(fontName, 'normal');
            pdf.text(`${issue.lineNumber}`, margin + extraLeftMargin, yOffset);
            yOffset += lineHeight;

            // Description
            pdf.setFont(fontName, 'bold');
            pdf.text(`Description:`, margin, yOffset);
            pdf.setFont(fontName, 'normal');
            pdf.text(descriptionLines, margin + extraLeftMargin, yOffset, {
                maxWidth: valueWidth,  // szerokość dostępna dla tekstu
                align: 'justify'
            });
            yOffset += descriptionHeight;

            // Suggested Action
            pdf.setFont(fontName, 'bold');
            pdf.text(`Suggested Action:`, margin, yOffset);
            pdf.setFont(fontName, 'normal');
            pdf.text(actionLines, margin + extraLeftMargin, yOffset, {
                maxWidth: valueWidth,
                align: 'justify'
            });
            yOffset += actionHeight;
        }

        return pdf;
    }
}
