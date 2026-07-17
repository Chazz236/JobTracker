import type { JobResponse } from '../types';
import { JobTable } from '@/components/jobs/JobTable';
import { useDashboardAnalytics } from '@/hooks/useAnalytics';

interface DashboardProps {
  jobs: JobResponse[];
  onEdit: (job: JobResponse) => void;
  onDelete: (id: number) => void;
}

const Dashboard = ({ jobs, onEdit, onDelete }: DashboardProps) => {
  const { analytics, isPending: analyticsLoading } = useDashboardAnalytics();

  return (
    <div className="flex flex-col gap-4 md:gap-8">
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <div className="rounded-xl border bg-card p-6 shadow-sm">
          <p className="text-sm font-medium text-muted-foreground">Total Applications</p>
          <h3 className="text-2xl font-bold mt-1">
            {analyticsLoading ? "..." : analytics?.totalApps ?? 0}
          </h3>
        </div>
        <div className="rounded-xl border bg-card p-6 shadow-sm">
          <p className="text-sm font-medium text-muted-foreground">Applied</p>
          <h3 className="text-2xl font-bold text-slate-600 mt-1">
            {analyticsLoading ? "..." : analytics?.countByStatus['APPLIED'] ?? 0}
          </h3>
        </div>
        <div className="rounded-xl border bg-card p-6 shadow-sm">
          <p className="text-sm font-medium text-muted-foreground">Interviewing</p>
          <h3 className="text-2xl font-bold text-yellow-400 mt-1">
            {analyticsLoading ? "..." : analytics?.countByStatus['INTERVIEWING'] ?? 0}
          </h3>
        </div>
        <div className="rounded-xl border bg-card p-6 shadow-sm">
          <p className="text-sm font-medium text-muted-foreground">Accepted</p>
          <h3 className="text-2xl font-bold text-emerald-600 mt-1">
            {analyticsLoading ? "..." : analytics?.countByStatus['ACCEPTED'] ?? 0}
          </h3>
        </div>
      </div>
      <div className="rounded-xl border bg-card text-card-foreground shadow-sm p-6">
        <JobTable jobs={jobs} onDelete={onDelete} onEdit={onEdit} />
      </div>
    </div>
  );
};

export default Dashboard;
