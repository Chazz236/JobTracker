import Dashboard from './pages/Dashboard';
import { useState } from 'react';
import { JobDialog } from './components/jobs/JobDialog';
import type { JobRequest, JobResponse } from '@/types';
import {
  getAllJobs,
  createJob,
  updateJob,
  deleteJob,
} from './services/jobService';
import { Layout } from './components/layout/Layout';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import Analytics from './pages/Analytics';

const App = () => {
  const [edit, setEdit] = useState<JobResponse | null>(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  const queryClient = useQueryClient();

  const { isPending, error, data: jobs = [] } = useQuery<JobResponse[]>({
    queryKey: ['jobs'],
    queryFn: getAllJobs,
  });

  const onAdd = () => {
    setEdit(null);
    setIsDialogOpen(true);
  };

  const onEdit = (job: JobResponse) => {
    setEdit(job);
    setIsDialogOpen(true);
  };

  const saveMutation = useMutation({
    mutationFn: async (job: JobRequest) => {
      if (edit) {
        return await updateJob(edit.id, job);
      } else {
        return await createJob(job);
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['jobs'] });
      queryClient.invalidateQueries({ queryKey: ['companies'] });

      setEdit(null);
      setIsDialogOpen(false);
    },
    onError: (e) => {
      console.error("Can't save job:", e);
    }
  });

  const onSave = (job: JobRequest) => {
    saveMutation.mutate(job);
  };

  const deleteMutation = useMutation({
    mutationFn: (id: number) => deleteJob(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['jobs'] });
    },
    onError: (e) => {
      console.error("Can't delete job:", e);
    }
  });

  const onDelete = (id: number) => {
    deleteMutation.mutate(id);
  };

  if (isPending) {
    return <div className="flex h-screen items-center justify-center">Loading...</div>;
  }

  if (error) {
    return <div className="flex h-screen items-center justify-center text-red-500">An error has occurred: {error.message}</div>;
  }

  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout onAdd={onAdd} />}>
          <Route
            path="/dashboard"
            element={
              <Dashboard jobs={jobs} onEdit={onEdit} onDelete={onDelete} />
            }
          />
          <Route path="/analytics" element={<Analytics />} />
        </Route>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
      </Routes>
      <JobDialog
        onSave={onSave}
        edit={edit}
        open={isDialogOpen}
        onOpenChange={setIsDialogOpen}
      />
    </BrowserRouter>
  );
};

export default App;
