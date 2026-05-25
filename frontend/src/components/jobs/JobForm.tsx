import { useEffect, useState } from 'react';
import { type JobRequest, type JobResponse, JobStatus } from '@/types';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectGroup,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

interface JobFormProps {
  onSave: (job: JobRequest) => void;
  edit: JobResponse | null;
}

export const JobForm = ({ onSave, edit }: JobFormProps) => {
  const getTodayString = () => new Date().toISOString().split('T')[0];

  const resetJob: JobRequest = {
    jobTitle: '',
    companyName: '',
    companyJobPageLink: '',
    location: '',
    appliedDate: getTodayString(),
    status: JobStatus.APPLIED,
  };

  const [jobData, setJobData] = useState<JobRequest>(resetJob);

  const onSubmit = (e: React.SubmitEvent) => {
    e.preventDefault();
    onSave(jobData);
  };

  const onChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement> //const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  ) => {
    setJobData({ ...jobData, [e.target.id]: e.target.value });
  };

  useEffect(() => {
    if (edit) {
      setJobData({
        jobTitle: edit.jobTitle,
        companyName: edit.company.name,
        companyJobPageLink: edit.company.jobPageLink || '',
        location: edit.location,
        appliedDate: edit.appliedDate,
        status: edit.status
      });
    } else {
      setJobData(resetJob);
    }
  }, [edit]);

  return (
    <form onSubmit={onSubmit} className="space-y-4 py-4">
      <div className="grid gap-1.5">
        <Label htmlFor="jobTitle">Job Title</Label>
        <Input
          id="jobTitle"
          type="text"
          value={jobData.jobTitle}
          onChange={onChange}
          required
        />
      </div>
      <div className="grid gap-1.5">
        <Label htmlFor="companyName">Company Name</Label>
        <Input
          id="companyName"
          type="text"
          value={jobData.companyName}
          onChange={onChange}
          required
        />
      </div>
      <div className="grid gap-1.5">
        <Label htmlFor="companyJobPageLink">
          Company Job Page Link (Optional)
        </Label>
        <Input
          id="companyJobPageLink"
          type="text"
          value={jobData.companyJobPageLink}
          onChange={onChange}
        />
      </div>
      <div className="grid gap-1.5">
        <Label htmlFor="location">Location</Label>
        <Input
          id="location"
          type="text"
          value={jobData.location}
          onChange={onChange}
          required
        />
      </div>
      <div className="grid gap-1.5">
        <Label htmlFor="appliedDate">Applied Date</Label>
        <Input
          id="appliedDate"
          type="date"
          value={jobData.appliedDate}
          onChange={onChange}
          required
        />
      </div>
      <div className="grid gap-1.5">
        <Label htmlFor="status">Application Status</Label>
        <Select
          key={jobData.status}
          value={jobData.status}
          onValueChange={(value) =>
            setJobData({ ...jobData, status: value as JobStatus })
          }
        >
          <SelectTrigger id="status" className="w-full">
            <SelectValue />
          </SelectTrigger>
          <SelectContent>
            <SelectGroup>
              {Object.values(JobStatus).map((status) => (
                <SelectItem key={status} value={status}>
                  {status.charAt(0) + status.slice(1).toLowerCase()}
                </SelectItem>
              ))}
            </SelectGroup>
          </SelectContent>
        </Select>
      </div>
      <Button type="submit" className="w-full mt-4">
        {edit ? 'Update Job' : 'Add Job'}
      </Button>
    </form>
  );
};
