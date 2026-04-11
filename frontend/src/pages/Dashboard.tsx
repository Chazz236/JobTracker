import type { Job } from "../types";
import JobTable from "../components/JobTable";
import JobForm from "../components/JobForm";
import { useState } from "react";

const Dashboard = () => {
    const [jobs, setJobs] = useState<Job[]>([
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
    ]);

    const onAdd = (job: Job) => {
        const jobWithId = {...job, id: jobs.length > 0 ? Math.max(...jobs.map(j => j.id || 0)) + 1 : 1};
        setJobs((prevJobs) => [...prevJobs, jobWithId]);
    };

    return (
        <main>
            <h1>Job Applications</h1>
            <JobForm onAdd={onAdd}/>
            <JobTable jobs={jobs} />
        </main>
    );
};

export default Dashboard;