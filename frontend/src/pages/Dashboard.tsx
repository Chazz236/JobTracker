import type { JobResponse } from '../types';
import { JobTable } from '@/components/jobs/JobTable';

interface DashboardProps {
  jobs: JobResponse[];
  onEdit: (job: JobResponse) => void;
  onDelete: (id: number) => void;
}

const Dashboard = ({ jobs, onEdit, onDelete }: DashboardProps) => {
  return (
    <div className="grid gap-4 md:gap-8">
      <div className="rounded-xl border bg-card text-card-foreground shadow-sm p-6">
        <JobTable jobs={jobs} onDelete={onDelete} onEdit={onEdit} />
      </div>
    </div>
  );
};

export default Dashboard;
