import Dashboard from './pages/Dashboard';
import { useState, useEffect } from 'react';
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
import Analytics from './pages/Analytics';

const App = () => {
  const [jobs, setJobs] = useState<JobResponse[]>([]);
  const [edit, setEdit] = useState<JobResponse | null>(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  useEffect(() => {
    const getJobs = async () => {
      try {
        const data = await getAllJobs();
        setJobs(data);
      } catch (e) {
        console.error("Can't get jobs:", e);
      }
    };
    getJobs();
  }, []);

  const onAdd = () => {
    setEdit(null);
    setIsDialogOpen(true);
  };

  const onEdit = (job: JobResponse) => {
    setEdit(job);
    setIsDialogOpen(true);
  };

  const onSave = async (job: JobRequest) => {
    try {
      if (edit) {
        const updatedJob = await updateJob(edit.id, job);
        setJobs((prevJobs) =>
          prevJobs.map((j) => (j.id === edit.id ? updatedJob : j))
        );
        setEdit(null);
      } else {
        const newJob = await createJob(job);
        setJobs((prevJobs) => [...prevJobs, newJob]);
      }
      setIsDialogOpen(false);
    } catch (e) {
      console.error("Can't save job:", e);
    }
  };

  const onDelete = async (id: number) => {
    try {
      await deleteJob(id);
      setJobs((prevJobs) => prevJobs.filter((job) => job.id !== id));
    } catch (e) {
      console.error("Can't delete job:", e);
    }
  };

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
