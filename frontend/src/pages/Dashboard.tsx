import type { Job } from "../types";
import JobTable from "../components/JobTable";

const Dashboard = () => {
    const jobs: Job[] = [
        {
            id: 1,
            jobTitle: 'Junior Developer',
            company: '123 Computers',
            location: 'Brampton, ON',
            appliedDate: '2026-04-01',
            status: 'APPLIED'
        },
        {
            id: 2,
            jobTitle: 'Junior Engineer',
            company: 'Rogers',
            location: 'Toronto, ON',
            appliedDate: '2026-04-02',
            status: 'REJECTED'
        }
    ];

    return (
        <main>
            <h1>Job Applications</h1>
            <JobTable jobs={jobs} />
        </main>
    );
};

export default Dashboard;