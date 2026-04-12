import { useEffect, useState } from "react";
import type { Job } from "../types";

interface JobFormProps {
    onAdd: (job: Job) => void;
    edit: Job | null;
    onCancel: () => void;
}

const JobForm = ({ onAdd, edit, onCancel }: JobFormProps) => {
    const getTodayString = () => new Date().toISOString().split('T')[0];

    const resetJob: Job = {
        jobTitle: '',
        company: '',
        location: '',
        appliedDate: getTodayString(),
        status: 'APPLIED'
    };

    const [jobData, setJobData] = useState<Job>(resetJob);

    const onSubmit = (e: React.SubmitEvent) => {
        e.preventDefault();
        onAdd(jobData);
        setJobData(resetJob);
    }

    const onChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setJobData({...jobData, [e.target.id]: e.target.value});
    }

    useEffect(() => {
        if (edit) {
            setJobData(edit);
        }
        else {
            setJobData(resetJob);
        }
    }, [edit]);

    return (
        <section>
            <h2>{edit ? 'Edit Job' : 'Add Job'}</h2>
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
                    {edit ? 'Edit Job' : 'Add Job'}
                </button>
                {edit && (
                    <button type='button' onClick={onCancel}>Cancel</button>
                )}
            </form>
        </section>
    );
};

export default JobForm;