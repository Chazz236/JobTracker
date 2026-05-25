import type { ColumnDef } from '@tanstack/react-table';
import type { JobResponse } from '@/types';
import { Button } from '@/components/ui/button';
import { SortableHeader } from '../ui/sortable-header';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '@/components/ui/tooltip';

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
    cell: ({ row }) => {
      const title = row.getValue('jobTitle') as string;
      return (
        <TooltipProvider>
          <Tooltip>
            <TooltipTrigger asChild>
              <div className="max-w-[150px] truncate cursor-help">{title}</div>
            </TooltipTrigger>
            <TooltipContent>
              <p className="max-w-[300px] break-words">{title}</p>
            </TooltipContent>
          </Tooltip>
        </TooltipProvider>
      );
    },
    sortingFn: 'alphanumeric',
  },
  {
    id: 'companyName',
    accessorFn: (row) => `${row.company.name}`,
    header: ({ column }) => <SortableHeader column={column} title="Company" />,
    cell: ({ row }) => {
      const name = row.original.company.name;
      const link = row.original.company.jobPageLink;

      const cellContent = () => {
        if (!link) {
          return <span>{name}</span>;
        }
        const linkFix = link.startsWith('http') ? link : `https://${link}`;
        return (
          <a href={linkFix} target="_blank" rel="noopener noreferrer">
            {name}
          </a>
        );
      };

      return (
        <TooltipProvider>
          <Tooltip>
            <TooltipTrigger asChild>
              <div className="max-w-[150px] truncate cursor-help">
                {cellContent()}
              </div>
            </TooltipTrigger>
            <TooltipContent>
              <p className="max-w-[300px] break-words">{name}</p>
            </TooltipContent>
          </Tooltip>
        </TooltipProvider>
      );
    },
    sortingFn: 'alphanumeric',
  },
  {
    accessorKey: 'location',
    header: ({ column }) => <SortableHeader column={column} title="Location" />,
    cell: ({ row }) => {
      const location = row.original.location;
      return (
        <TooltipProvider>
          <Tooltip>
            <TooltipTrigger asChild>
              <div className="max-w-[150px] truncate cursor-help">
                {location}
              </div>
            </TooltipTrigger>
            <TooltipContent>
              <p className="max-w-[300px] break-words">{location}</p>
            </TooltipContent>
          </Tooltip>
        </TooltipProvider>
      );
    },
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
