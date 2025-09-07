export class CssVarUtil
{
    static getCssVariable(name: string): string {
        return getComputedStyle(document.documentElement).getPropertyValue(name).trim();
    }

    static getSeverityColors(): { [key: string]: string } {
        return {
            LOW: this.getCssVariable('--color-secondary-dark') || '#000',
            MEDIUM: this.getCssVariable('--color-extra') || '#000',
            CRITICAL: this.getCssVariable('--color-primary-light') || '#000'
        };
    }
}
