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
    <>
      <Layout onAdd={onAdd} title="Dashboard">
        <Dashboard jobs={jobs} onEdit={onEdit} onDelete={onDelete} />
      </Layout>
      <JobDialog
        onSave={onSave}
        edit={edit}
        open={isDialogOpen}
        onOpenChange={setIsDialogOpen}
      />
    </>
  );
};

export default App;
