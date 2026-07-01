import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import {
    getAllJobs,
    createJob,
    updateJob,
    deleteJob,
} from '@/services/jobService';
import type { JobRequest, JobResponse } from '@/types';

export const useJobs = () => {
    const queryClient = useQueryClient();

    const { data: jobs = [], isPending, error } = useQuery<JobResponse[]>({
        queryKey: ['jobs'],
        queryFn: getAllJobs
    })

    const saveMutation = useMutation({
        mutationFn: async ({ id, data }: { id?: number; data: JobRequest }) => {
            if (id) {
                return await updateJob(id, data);
            }
            else {
                return await createJob(data);
            }
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['jobs'] });
            queryClient.invalidateQueries({ queryKey: ['companies'] });
        },
        onError: (e) => {
            console.error("Can't save job:", e);
        }
    });

    const deleteMutation = useMutation({
        mutationFn: (id: number) => deleteJob(id),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['jobs'] });
        },
        onError: (e) => {
            console.error("Can't delete job:", e);
        }
    });

    return {
        jobs,
        isPending,
        error,
        saveJob: (data: JobRequest, id?: number, options?: { onSuccess?: () => void; onError?: (e: unknown) => void }) => saveMutation.mutate({ id, data }, options),
        deleteJob: (id: number) => deleteMutation.mutate(id),
        isSaving: saveMutation.isPending,
        isDeleting: deleteMutation.isPending,
    }
}