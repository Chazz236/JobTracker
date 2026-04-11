import { useState } from "react";
import type { Job } from "../types";

interface JobFormProps {
    onAdd: (job: Job) => void;
}

const JobForm = ({ onAdd }: JobFormProps) => {
    const getTodayString = () => new Date().toISOString().split('T')[0];

    const [jobData, setJobData] = useState<Job>({
        jobTitle: '',
        company: '',
        location: '',
        appliedDate: getTodayString(),
        status: 'APPLIED'
    });

    const onSubmit = (e: React.SubmitEvent) => {
        e.preventDefault();
        onAdd(jobData);
        setJobData({
            jobTitle: '',
            company: '',
            location: '',
            appliedDate: getTodayString(),
            status: 'APPLIED'
        });
    }

    const onChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setJobData({...jobData, [e.target.id]: e.target.value});
    }


    return (
        <section>
            <h2>Add Job</h2>
            <form onSubmit={onSubmit}>
                <label htmlFor='jobTitle'>Job Title</label>
                <input id='jobTitle' type='text' value={jobData.jobTitle} onChange={onChange} required />
                <label htmlFor='company'>Company</label>
                <input id='company' type='text' value={jobData.company} onChange={onChange} required />
                <label htmlFor='location'>Location</label>
                <input id='location' type='text' value={jobData.location} onChange={onChange} required />
                <label htmlFor='appliedDate'>Applied Date</label>
                <input id='appliedDate' type='date' value={jobData.appliedDate} onChange={onChange} required />
                <label htmlFor='status'>Application Status</label>
                <select id='status' value={jobData.status} onChange={onChange} required>
                    <option value='APPLIED'>Applied</option>
                    <option value='INTERVIEWING'>Interviewing</option>
                    <option value='OFFERED'>Offered</option>
                    <option value='ACCEPTED'>Accepted</option>
                    <option value='REJECTED'>Rejected</option>
                </select>
                <button type='submit'>
                    Add Job
                </button>
            </form>
        </section>
    );
};

export default JobForm;