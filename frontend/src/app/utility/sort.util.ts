export function sortByGenericField<T>(array: T[], field: keyof T, currentSortField?: keyof T, currentSortDirection?: 'asc' | 'desc'):
    { sortedArray: T[]; newDirection: 'asc' | 'desc' }
{
    let newDirection: 'asc' | 'desc' = 'asc';

    if (currentSortField === field)
        newDirection = currentSortDirection === 'asc' ? 'desc' : 'asc';

    const sortedArray = array.sort((elem1, elem2) => {
        const value1 = elem1[field];
        const value2 = elem2[field];

        if (field === 'severity') {
            const severityOrder: Record<string, number> = {
                LOW: 1,
                MEDIUM: 2,
                CRITICAL: 3
            };

            return newDirection === 'asc'
                ? severityOrder[String(value1)] - severityOrder[String(value2)]
                : severityOrder[String(value2)] - severityOrder[String(value1)];
        }

        if (field === 'scanDateTimeUtc0') {
            const dateA = new Date(String(value1).replace(' UTC', ''));
            const dateB = new Date(String(value2).replace(' UTC', ''));
            return newDirection === 'asc' ? dateA.getTime() - dateB.getTime() : dateB.getTime() - dateA.getTime();
        }

        if (typeof value1 === 'string' && typeof value2 === 'string') {
            return newDirection === 'asc' ? value1.localeCompare(value2) : value2.localeCompare(value1);
        }

        if (typeof value1 === 'number' && typeof value2 === 'number') {
            return newDirection === 'asc' ? value1 - value2 : value2 - value1;
        }

        return 0;
    });

    return { sortedArray, newDirection };
}
