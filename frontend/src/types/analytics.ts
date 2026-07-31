import type { JobStatus } from './jobs';

export interface AnalyticsSummaryResponse {
  totalApplications: number;
  countByStatus: Record<JobStatus, number>;
  appliedThisWeek: number;
  averageApplicationsPerWeek: number;
  daysSinceLastApplication: number | null;
}