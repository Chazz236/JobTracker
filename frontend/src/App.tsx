import Dashboard from './pages/Dashboard';
import { JobDialog } from './components/jobs/JobDialog';
import { useJobs } from './hooks/useJobs';
import { useJobDialog } from './hooks/useJobDialog'; // Import your new hook
import { Layout } from './components/layout/Layout';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Analytics from './pages/Analytics';
import type { JobRequest } from '@/types';

const App = () => {
  const { jobs, isPending, error, saveJob, deleteJob } = useJobs();
  const { edit, isOpen, setIsOpen, openAdd, openEdit, close } = useJobDialog();

  const onSave = (job: JobRequest) => {
    saveJob(job, edit?.id, {
      onSuccess: () => {
        close();
      },
    });
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
        <Route element={<Layout onAdd={openAdd} />}>
          <Route
            path="/dashboard"
            element={
              <Dashboard jobs={jobs} onEdit={openEdit} onDelete={deleteJob} />
            }
          />
          <Route path="/analytics" element={<Analytics />} />
        </Route>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
      </Routes>
      <JobDialog
        onSave={onSave}
        edit={edit}
        open={isOpen}
        onOpenChange={setIsOpen}
      />
    </BrowserRouter>
  );
};

export default App;
