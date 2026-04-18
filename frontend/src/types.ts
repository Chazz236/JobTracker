export type JobStatus = 'APPLIED' | 'INTERVIEWING' | 'OFFERED' | 'ACCEPTED' | 'REJECTED';

export interface JobResponse {
    id: number;
    jobTitle: string;
    company: string;
    location: string;
    appliedDate: string;
    status: JobStatus;
}

export type JobRequest = Omit<JobResponse, 'id'>;