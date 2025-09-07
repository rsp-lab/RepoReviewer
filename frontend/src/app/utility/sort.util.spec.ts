import { sortByGenericField } from './sort.util';

describe('sortByGenericField()', () => {
    const baseData = [
        { severity: 'LOW', lineNumber: 2, filePath: 'b.txt', repositoryUrl: 'http://b.com', scanDateTimeUtc0: '2000-02-02 10:00 UTC+00' },
        { severity: 'CRITICAL', lineNumber: 6, filePath: 'a.txt', repositoryUrl: 'http://a.com', scanDateTimeUtc0: '2000-01-01 10:00 UTC+00' },
        { severity: 'MEDIUM', lineNumber: 4, filePath: 'c.txt', repositoryUrl: 'http://c.com', scanDateTimeUtc0: '2000-02-02 15:00 UTC+00' },
    ];

    it('should sort severity ascending', () => {
        const { sortedArray, newDirection } = sortByGenericField([...baseData], 'severity', undefined, 'asc');
        expect(newDirection).toBe('asc');
        expect(sortedArray.map(i => i.severity)).toEqual(['LOW', 'MEDIUM', 'CRITICAL']);
    });

    it('should toggle severity sort direction', () => {
        const { sortedArray, newDirection } = sortByGenericField([...baseData], 'severity', 'severity', 'asc');
        expect(newDirection).toBe('desc');
        expect(sortedArray.map(i => i.severity)).toEqual(['CRITICAL', 'MEDIUM', 'LOW']);
    });

    it('should sort numbers ascending', () => {
        const { sortedArray } = sortByGenericField([...baseData], 'lineNumber', undefined, 'asc');
        expect(sortedArray.map(i => i.lineNumber)).toEqual([2, 4, 6]);
    });

    it('should toggle numbers sort direction', () => {
        const { sortedArray } = sortByGenericField([...baseData], 'lineNumber', 'lineNumber', 'asc');
        expect(sortedArray.map(i => i.lineNumber)).toEqual([6, 4, 2]);
    });

    it('should sort strings ascending', () => {
        const { sortedArray } = sortByGenericField([...baseData], 'filePath', undefined, 'asc');
        expect(sortedArray.map(i => i.filePath)).toEqual(['a.txt', 'b.txt', 'c.txt']);
    });

    it('should toggle strings sort direction', () => {
        const { sortedArray } = sortByGenericField([...baseData], 'filePath', 'filePath', 'asc');
        expect(sortedArray.map(i => i.filePath)).toEqual(['c.txt', 'b.txt', 'a.txt']);
    });

    it('should sort dates ascending', () => {
        const { sortedArray } = sortByGenericField([...baseData], 'scanDateTimeUtc0', undefined, 'asc');
        expect(sortedArray.map(i => i.scanDateTimeUtc0)).toEqual([
            '2000-01-01 10:00 UTC+00',
            '2000-02-02 10:00 UTC+00',
            '2000-02-02 15:00 UTC+00'
        ]);
    });

    it('should toggle dates sort descending', () => {
        const { sortedArray } = sortByGenericField([...baseData], 'scanDateTimeUtc0', 'scanDateTimeUtc0', 'asc');
        expect(sortedArray.map(i => i.scanDateTimeUtc0)).toEqual([
            '2000-02-02 15:00 UTC+00',
            '2000-02-02 10:00 UTC+00',
            '2000-01-01 10:00 UTC+00'
        ]);
    });
});
