import { AppSidebar } from './components/layout/AppSidebar';
import Dashboard from './pages/Dashboard';
import {
  SidebarProvider,
  SidebarTrigger,
  SidebarInset,
} from '@/components/ui/sidebar';
import { useState, useEffect } from 'react';
import { JobDialog } from './components/jobs/JobDialog';
import type { JobRequest, JobResponse } from '@/types';
import {
  getAllJobs,
  createJob,
  updateJob,
  deleteJob,
} from './services/jobService';

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
    <SidebarProvider className="bg-muted/40">
      <AppSidebar onAdd={onAdd} />
      <SidebarInset className="bg-background">
        <header className="flex h-16 shrink-0 items-center gap-2 px-4 md:px-8 border-b border-border">
          <SidebarTrigger className="-ml-1" />
          <div className="h-4 w-px bg-border mx-2" />
          <h2 className="text-sm font-semibold uppercase tracking-wider text-muted-foreground">
            Dashboard
          </h2>
        </header>
        <div className="flex flex-1 flex-col gap-4 p-4 md:gap-8 md:p-8">
          <Dashboard jobs={jobs} onEdit={onEdit} onDelete={onDelete} />
        </div>
      </SidebarInset>
      <JobDialog
        onSave={onSave}
        edit={edit}
        open={isDialogOpen}
        onOpenChange={setIsDialogOpen}
      />
    </SidebarProvider>
  );
};

export default App;
