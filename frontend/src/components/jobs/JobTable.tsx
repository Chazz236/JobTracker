import type { JobResponse } from "@/types";
import DataTable from "./data-table";
import columns from "./columns";

interface JobTableProps {
    jobs: JobResponse[];
    onDelete: (id: number) => void;
    onEdit: (job: JobResponse) => void;
}

const JobTable = ({ jobs, onDelete, onEdit }: JobTableProps) => {
    return (
        <DataTable columns={columns({ onEdit, onDelete })} data={jobs} />
    );
};

export default JobTable;