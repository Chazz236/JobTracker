import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog";
import JobForm from "./JobForm";
import type { JobRequest, JobResponse } from "@/types";

interface JobDialogProps {
    onSave: (job: JobRequest) => void;
    edit: JobResponse | null;
    open: boolean;
    onOpenChange: (open: boolean) => void;
}

const JobDialog = ({ onSave, edit, open, onOpenChange }: JobDialogProps) => {
    return (
        <Dialog open={open} onOpenChange={onOpenChange} >
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>{edit ? 'Edit Job' : 'Add New Job'}</DialogTitle>
                    <DialogDescription>
                        {edit ? 'Update job application details' : 'Add job application details'}
                    </DialogDescription>
                </DialogHeader>
                <JobForm
                    edit={edit}
                    onSave={(data) => {
                        onSave(data);
                        onOpenChange(false);
                    }}
                />
            </DialogContent>
        </Dialog>
    );
};

export default JobDialog;