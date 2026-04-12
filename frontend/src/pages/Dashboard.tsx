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

    const [edit, setEdit] = useState<Job | null>(null);

    const onAdd = (job: Job) => {
        if (edit) {
            setJobs(jobs.map(j => j.id === edit.id ? { ...job, id: edit.id } : j));
            setEdit(null);
        }
        else {
            const jobWithId = { ...job, id: jobs.length > 0 ? Math.max(...jobs.map(j => j.id || 0)) + 1 : 1 };
            setJobs((prevJobs) => [...prevJobs, jobWithId]);
        }
    };

    const onDelete = (id: number) => {
        setJobs((prevJobs) => prevJobs.filter(job => job.id !== id));
    };

    return (
        <main>
            <h1>Job Applications</h1>
            <JobForm onAdd={onAdd} edit={edit} onCancel={() => setEdit(null)} />
            <JobTable jobs={jobs} onDelete={onDelete} onEdit={(job) => setEdit(job)} />
        </main>
    );
};

export default Dashboard;