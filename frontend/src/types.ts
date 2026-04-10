export type JobStatus = 'APPLIED' | 'INTERVIEWING' | 'OFFERED' | 'ACCEPTED' | 'REJECTED';

export interface Job {
    id?: number;
    jobTitle: string;
    company: string;
    location: string;
    appliedDate: string;
    status: JobStatus;
}