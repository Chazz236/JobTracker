import api from './api';
import type { DashboardAnalyticsResponse } from '@/types';

export const getDashboardAnalytics =
  async (): Promise<DashboardAnalyticsResponse> => {
    const response = await api.get<DashboardAnalyticsResponse>(
      '/analytics/dashboard'
    );
    return response.data;
  };
