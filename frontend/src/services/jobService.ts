import api from './api';
import type { JobRequest, JobResponse } from '@/types';

export const getAllJobs = async (): Promise<JobResponse[]> => {
  const response = await api.get<JobResponse[]>('/jobs');
  return response.data;
};

export const createJob = async (data: JobRequest): Promise<JobResponse> => {
  const response = await api.post<JobResponse>('/jobs', data);
  return response.data;
};

export const updateJob = async (
  id: number,
  data: JobRequest
): Promise<JobResponse> => {
  const response = await api.put<JobResponse>(`/jobs/${id}`, data);
  return response.data;
};

export const deleteJob = async (id: number): Promise<void> => {
  await api.delete(`/jobs/${id}`);
};
