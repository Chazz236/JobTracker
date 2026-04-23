import type { JobResponse } from "../types";
import JobTable from "@/components/jobs/JobTable";

interface DashboardProps {
    jobs: JobResponse[];
    onEdit: (job: JobResponse) => void;
    onDelete: (id: number) => void;
}

const Dashboard = ({ jobs, onEdit, onDelete }: DashboardProps) => {
    return (
        <div>
            <h1>Job Applications</h1>
            <JobTable jobs={jobs} onDelete={onDelete} onEdit={onEdit} />
        </div>
    );
};

export default Dashboard;