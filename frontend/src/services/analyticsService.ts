import api from './api';
import type { AnalyticsSummaryResponse } from '@/types';

export const getAnalyticsSummary =
  async (): Promise<AnalyticsSummaryResponse> => {
    const response = await api.get<AnalyticsSummaryResponse>(
      '/analytics/summary'
    );
    return response.data;
  };
