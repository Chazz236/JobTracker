import type { JobResponse } from '@/types';
import { DataTable } from './data-table';
import { columns } from './columns';

interface JobTableProps {
  jobs: JobResponse[];
  onDelete: (id: number) => void;
  onEdit: (job: JobResponse) => void;
  showTableControls: boolean;
}

export const JobTable = ({
  jobs,
  onDelete,
  onEdit,
  showTableControls,
}: JobTableProps) => {
  return (
    <DataTable
      columns={columns({ onEdit, onDelete, enableSorting: showTableControls })}
      data={jobs}
      showTableControls={showTableControls}
    />
  );
};
