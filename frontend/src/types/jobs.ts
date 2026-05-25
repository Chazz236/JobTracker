import type { CompanyResponse } from './companies';

export const JobStatus = {
  APPLIED: 'APPLIED',
  INTERVIEWING: 'INTERVIEWING',
  OFFERED: 'OFFERED',
  ACCEPTED: 'ACCEPTED',
  REJECTED: 'REJECTED',
} as const;

export type JobStatus = (typeof JobStatus)[keyof typeof JobStatus];

export interface JobResponse {
  id: number;
  jobTitle: string;
  company: CompanyResponse;
  location: string;
  appliedDate: string;
  status: JobStatus;
}

export interface JobRequest {
  jobTitle: string;
  companyName: string;
  companyJobPageLink?: string;
  location: string;
  appliedDate: string;
  status: JobStatus;
}
