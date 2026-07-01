import Dashboard from './pages/Dashboard';
import { useState } from 'react';
import { JobDialog } from './components/jobs/JobDialog';
import type { JobRequest, JobResponse } from '@/types';
import { useJobs } from './hooks/useJobs';
import { Layout } from './components/layout/Layout';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Analytics from './pages/Analytics';

const App = () => {
  const [edit, setEdit] = useState<JobResponse | null>(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  const { jobs, isPending, error, saveJob, deleteJob } = useJobs();

  const onAdd = () => {
    setEdit(null);
    setIsDialogOpen(true);
  };

  const onEdit = (job: JobResponse) => {
    setEdit(job);
    setIsDialogOpen(true);
  };

  const onSave = (job: JobRequest) => {
    saveJob(job, edit?.id, {
      onSuccess: () => {
        setEdit(null);
        setIsDialogOpen(false);
      }
    });
  };

  const onDelete = (id: number) => {
    deleteJob(id);
  };

  if (isPending) {
    return (
      <div className="flex h-screen items-center justify-center">
        Loading...
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex h-screen items-center justify-center text-red-500">
        An error has occurred: {error.message}
      </div>
    );
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
