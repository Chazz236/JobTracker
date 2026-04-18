import type { JobRequest, JobResponse } from "../types";
import JobTable from "../components/JobTable";
import JobForm from "../components/JobForm";
import { useState, useEffect } from "react";
import jobService from "../services/jobService";

const Dashboard = () => {
    const [jobs, setJobs] = useState<JobResponse[]>([]);
    const [edit, setEdit] = useState<JobResponse | null>(null);

    useEffect(() => {
        const getJobs = async () => {
            try {
                const data = await jobService.getAll();
                setJobs(data);
            } catch (e) {
                console.error('Can\'t get jobs:', e);
            }
        };
        getJobs();
    }, []);

    const onSave = async (job: JobRequest) => {
        try {
            if (edit) {
                const updatedJob = await jobService.update(edit.id, job);
                setJobs(prevJobs => prevJobs.map(j => j.id === edit.id ? updatedJob : j));
                setEdit(null);
            }
            else {
                const newJob = await jobService.create(job);
                setJobs(prevJobs => [...prevJobs, newJob]);
            }
        } catch (e) {
            console.error('Can\'t save job:', e);
        }
    };

    const onDelete = async (id: number) => {
        try {
            await jobService.delete(id);
            setJobs(prevJobs => prevJobs.filter(job => job.id !== id));
        } catch (e) {
            console.error('Can\'t delete job:', e);
        }
    };

    return (
        <main>
            <h1>Job Applications</h1>
            <JobForm onSave={onSave} edit={edit} onCancel={() => setEdit(null)} />
            <JobTable jobs={jobs} onDelete={onDelete} onEdit={job => setEdit(job)} />
        </main>
    );
};

export default Dashboard;