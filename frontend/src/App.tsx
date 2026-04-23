import { AppSidebar } from "./components/AppSidebar";
import Dashboard from "./pages/Dashboard";
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { useState, useEffect } from "react";
import JobDialog from "./components/JobDialog";
import type { JobRequest, JobResponse } from "./types";
import jobService from "./services/jobService";

function App() {
    const [jobs, setJobs] = useState<JobResponse[]>([]);
    const [edit, setEdit] = useState<JobResponse | null>(null);
    const [isDialogOpen, setIsDialogOpen] = useState(false);

    useEffect(() => {
        const getJobs = async () => {
            try {
                const data = await jobService.getAll();
                setJobs(data);
            } catch (e) {
                console.error('Can\'t get jobs:', e);
            }
        };
        getJobs();
    }, []);

    const onAdd = () => {
        setEdit(null);
        setIsDialogOpen(true);
    }

    const onEdit = (job: JobResponse) => {
        setEdit(job);
        setIsDialogOpen(true);
    }

    const onSave = async (job: JobRequest) => {
        try {
            if (edit) {
                const updatedJob = await jobService.update(edit.id, job);
                setJobs(prevJobs => prevJobs.map(j => j.id === edit.id ? updatedJob : j));
                setEdit(null);
            }
            else {
                const newJob = await jobService.create(job);
                setJobs(prevJobs => [...prevJobs, newJob]);
            }
            setIsDialogOpen(false);
        } catch (e) {
            console.error('Can\'t save job:', e);
        }
    }

    const onDelete = async (id: number) => {
        try {
            await jobService.delete(id);
            setJobs(prevJobs => prevJobs.filter(job => job.id !== id));
        } catch (e) {
            console.error('Can\'t delete job:', e);
        }
    };

    return (
        <SidebarProvider>
            <AppSidebar onAdd={onAdd} />
            <main>
                <header>
                    <SidebarTrigger />
                </header>

                <div>
                    <Dashboard jobs={jobs} onEdit={onEdit} onDelete={onDelete} />
                </div>

                <JobDialog
                    onSave={onSave}
                    edit={edit}
                    open={isDialogOpen}
                    onOpenChange={setIsDialogOpen}
                />
            </main>
        </SidebarProvider>
    );
}

export default App;
