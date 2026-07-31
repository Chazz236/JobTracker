import { useQuery } from '@tanstack/react-query';
import { getAnalyticsSummary } from '@/services/analyticsService';
import type { AnalyticsSummaryResponse } from '@/types';

export const useAnalyticsSummary = () => {
    const { data: summary, isPending, error } = useQuery<AnalyticsSummaryResponse>({
        queryKey: ['analytics', 'summary'],
        queryFn: getAnalyticsSummary,
    });

    return {
        summary,
        isPending,
        error,
    };
}