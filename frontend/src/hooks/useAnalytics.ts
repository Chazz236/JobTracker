import { useQuery } from '@tanstack/react-query';
import { getDashboardAnalytics } from '@/services/analyticsService';
import type { DashboardAnalyticsResponse } from '@/types';

export const useDashboardAnalytics = () => {
    const { data: analytics, isPending, error } = useQuery<DashboardAnalyticsResponse>({
        queryKey: ['analytics', 'dashboard'],
        queryFn: getDashboardAnalytics,
    });

    return {
        analytics,
        isPending,
        error,
    };
}