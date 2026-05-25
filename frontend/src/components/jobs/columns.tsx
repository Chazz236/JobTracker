import type { ColumnDef } from '@tanstack/react-table';
import type { JobResponse } from '@/types';
import { Button } from '@/components/ui/button';
import { SortableHeader } from '../ui/sortable-header';

interface ColumnProps {
  onEdit: (job: JobResponse) => void;
  onDelete: (id: number) => void;
}

export const columns = ({
  onEdit,
  onDelete,
}: ColumnProps): ColumnDef<JobResponse>[] => [
    {
      accessorKey: 'jobTitle',
      header: ({ column }) => (
        <SortableHeader column={column} title="Job Title" />
      ),
      sortingFn: 'alphanumeric',
    },
    {
      id: 'companyName',
      accessorFn: row => `${row.company.name}`,
      header: ({ column }) => <SortableHeader column={column} title="Company" />,
      cell: ({ row }) => {
        const name = row.original.company.name;
        const link = row.original.company.jobPageLink;

        if (!link) {
          return <span>{name}</span>;
        }

        const linkFix = link.startsWith('http') ? link : `https://${link}`;

        return (
          <a href={linkFix} target="_blank" rel="noopener noreferrer">
            {name}
          </a>
        );
      },
      sortingFn: 'alphanumeric',
    },
    {
      accessorKey: 'location',
      header: ({ column }) => <SortableHeader column={column} title="Location" />,
      sortingFn: 'alphanumeric',
    },
    {
      accessorKey: 'appliedDate',
      header: ({ column }) => (
        <SortableHeader column={column} title="Applied Date" />
      ),
      sortingFn: 'alphanumeric',
    },
    {
      accessorKey: 'status',
      header: ({ column }) => (
        <SortableHeader column={column} title="Application Status" />
      ),
      cell: ({ row }) => {
        const status = row.getValue('status') as string;
        return <span>{status.charAt(0) + status.slice(1).toLowerCase()}</span>;
      },
      sortingFn: 'alphanumeric',
    },
    {
      id: 'actions',
      header: 'Actions',
      cell: ({ row }) => {
        const job = row.original;
        return (
          <div className="flex gap-2 items-center">
            <Button variant="outline" onClick={() => onEdit(job)}>
              Edit
            </Button>
            <Button variant="destructive" onClick={() => onDelete(job.id)}>
              Delete
            </Button>
          </div>
        );
      },
    },
  ];
