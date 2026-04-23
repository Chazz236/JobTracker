import { useEffect, useState } from "react";
import type { JobRequest, JobResponse } from "../types";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectGroup, SelectTrigger, SelectValue } from "@/components/ui/select";

interface JobFormProps {
    onSave: (job: JobRequest) => void;
    edit: JobResponse | null;
}

const JobForm = ({ onSave, edit }: JobFormProps) => {
    const getTodayString = () => new Date().toISOString().split('T')[0];

    const resetJob: JobRequest = {
        jobTitle: '',
        company: '',
        location: '',
        appliedDate: getTodayString(),
        status: 'APPLIED'
    };

    const [jobData, setJobData] = useState<JobRequest>(resetJob);

    const onSubmit = (e: React.SubmitEvent) => {
        e.preventDefault();
        onSave(jobData);
    }

    const onChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setJobData({ ...jobData, [e.target.id]: e.target.value });
    }

    useEffect(() => {
        if (edit) {
            const { id, ...data } = edit;
            setJobData(data);
        }
        else {
            setJobData(resetJob);
        }
    }, [edit]);

    return (
        <form onSubmit={onSubmit} className='space-y-4 py-4'>
            <div className='grid gap-1.5'>
                <Label htmlFor='jobTitle'>Job Title</Label>
                <Input id='jobTitle' type='text' value={jobData.jobTitle} onChange={onChange} required />
            </div>
            <div className='grid gap-1.5'>
                <Label htmlFor='company'>Company</Label>
                <Input id='company' type='text' value={jobData.company} onChange={onChange} required />
            </div>
            <div className='grid gap-1.5'>
                <Label htmlFor='location'>Location</Label>
                <Input id='location' type='text' value={jobData.location} onChange={onChange} required />
            </div>
            <div className='grid gap-1.5'>
                <Label htmlFor='appliedDate'>Applied Date</Label>
                <Input id='appliedDate' type='date' value={jobData.appliedDate} onChange={onChange} required />
            </div>
            <div className='grid gap-1.5'>
                <Label htmlFor='status'>Application Status</Label>
                <Select value={jobData.status} onValueChange={(value) => setJobData({ ...jobData, status: value as JobRequest['status'] })}>
                    <SelectTrigger id='status' className='w-full'>
                        <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectGroup>
                            <SelectItem value='APPLIED'>Applied</SelectItem>
                            <SelectItem value='INTERVIEWING'>Interviewing</SelectItem>
                            <SelectItem value='OFFERED'>Offered</SelectItem>
                            <SelectItem value='ACCEPTED'>Accepted</SelectItem>
                            <SelectItem value='REJECTED'>Rejected</SelectItem>
                        </SelectGroup>
                    </SelectContent>
                </Select>
            </div>
            <Button type='submit' className='w-full mt-4'>
                {edit ? 'Update Job' : 'Add Job'}
            </Button>
        </form>
    );
};

export default JobForm;