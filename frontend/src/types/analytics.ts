import type { JobStatus } from './jobs';

export interface DashboardAnalyticsResponse {
  totalApps: number;
  countByStatus: Record<JobStatus, number>;
}
