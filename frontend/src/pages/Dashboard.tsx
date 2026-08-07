import type { JobResponse, JobStatus } from '../types';
import { JobTable } from '@/components/jobs/JobTable';
import { useAnalyticsSummary } from '@/hooks/useAnalytics';
import { TrendingUp, TrendingDown } from 'lucide-react';

interface DashboardProps {
  jobs: JobResponse[];
  onEdit: (job: JobResponse) => void;
  onDelete: (id: number) => void;
}

const statusPriority: Record<JobStatus, number> = {
  OFFERED: 1,
  INTERVIEWING: 2,
  APPLIED: 3,
  ACCEPTED: 4,
  REJECTED: 5,
};

const Dashboard = ({ jobs, onEdit, onDelete }: DashboardProps) => {
  const { summary, isPending: isSummaryLoading } = useAnalyticsSummary();

  const appliedThisWeek = summary?.appliedThisWeek ?? 0;
  const averageApplicationsPerWeek = summary?.averageApplicationsPerWeek ?? 0;
  const weeklyAverageChange =
    averageApplicationsPerWeek === 0
      ? appliedThisWeek > 0
        ? 100
        : 0
      : ((appliedThisWeek - averageApplicationsPerWeek) /
          averageApplicationsPerWeek) *
        100;

  const jobsToWatch = [...jobs]
    .filter((job) => job.status !== 'ACCEPTED' && job.status !== 'REJECTED')
    .sort((a, b) => {
      const statusDifference =
        statusPriority[a.status] - statusPriority[b.status];

      if (statusDifference !== 0) {
        return statusDifference;
      }

      return (
        new Date(b.appliedDate).getTime() - new Date(a.appliedDate).getTime()
      );
    })
    .slice(0, 9);

  return (
    <div className="flex flex-col gap-4 md:gap-6">
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <div className="rounded-xl border bg-card p-6 shadow-sm">
          <p className="text-sm font-medium text-muted-foreground">
            Total Applications
          </p>
          <h3 className="text-2xl font-bold mt-1">
            {isSummaryLoading ? '...' : (summary?.totalApplications ?? 0)}
          </h3>
        </div>
        <div className="rounded-xl border bg-card p-6 shadow-sm">
          <p className="text-sm font-medium text-muted-foreground">
            Applied This Week
          </p>
          <h3 className="text-2xl font-bold text-slate-600 mt-1">
            {isSummaryLoading ? '...' : (summary?.appliedThisWeek ?? 0)}
          </h3>
          {!isSummaryLoading && summary?.daysSinceLastApplication != null && (
            <p
              className={`mt-2 flex items-center gap-1 text-sm ${weeklyAverageChange >= 0 ? 'text-green-600' : 'text-red-600'}`}
            >
              {weeklyAverageChange >= 0 ? (
                <TrendingUp className="h-4 w-4" />
              ) : (
                <TrendingDown className="h-4 w-4" />
              )}
              {Math.abs(weeklyAverageChange).toFixed(1)}% vs 8-week average
            </p>
          )}
        </div>
        <div className="rounded-xl border bg-card p-6 shadow-sm">
          <p className="text-sm font-medium text-muted-foreground">
            Days Since Last Application
          </p>
          <h3 className="text-2xl font-bold text-yellow-400 mt-1">
            {isSummaryLoading
              ? '...'
              : (summary?.daysSinceLastApplication ?? '-')}
          </h3>
        </div>
        <div className="rounded-xl border bg-card p-6 shadow-sm">
          <p className="text-sm font-medium text-muted-foreground">Accepted</p>
          <h3 className="text-2xl font-bold text-emerald-600 mt-1">
            {isSummaryLoading ? '...' : (summary?.countByStatus.ACCEPTED ?? 0)}
          </h3>
        </div>
      </div>
      <div className="rounded-xl border bg-card text-card-foreground shadow-sm p-6">
        <JobTable
          jobs={jobsToWatch}
          onDelete={onDelete}
          onEdit={onEdit}
          showTableControls={false}
        />
      </div>
    </div>
  );
};

export default Dashboard;
