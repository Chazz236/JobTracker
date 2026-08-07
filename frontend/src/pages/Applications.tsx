import type { JobResponse } from '../types';
import { JobTable } from '@/components/jobs/JobTable';

interface ApplicationsProps {
  jobs: JobResponse[];
  onEdit: (job: JobResponse) => void;
  onDelete: (id: number) => void;
}

const Applications = ({ jobs, onEdit, onDelete }: ApplicationsProps) => {
  return (
    <div className="rounded-xl border bg-card text-card-foreground shadow-sm p-6">
      <JobTable
        jobs={jobs}
        onDelete={onDelete}
        onEdit={onEdit}
        showTableControls={true}
      />
    </div>
  );
};

export default Applications;
