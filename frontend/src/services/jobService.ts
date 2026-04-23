import api from "./api";
import type { JobRequest, JobResponse } from "@/types";

const jobService = {
    async getAll(): Promise<JobResponse[]> {
        const response = await api.get<JobResponse[]>('/jobs');
        return response.data;
    },

    async create(data: JobRequest): Promise<JobResponse> {
        const response = await api.post<JobResponse>('/jobs', data);
        return response.data;
    },

    async update(id: number, data: JobRequest): Promise<JobResponse> {
        const response = await api.put<JobResponse>(`/jobs/${id}`, data);
        return response.data;
    },

    async delete(id: number): Promise<void> {
        await api.delete(`/jobs/${id}`);
    }
};

export default jobService;