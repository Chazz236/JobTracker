import type { JobResponse } from "../types";

interface JobTableProps {
    jobs: JobResponse[];
    onDelete: (id: number) => void;
    onEdit: (job: JobResponse) => void;
}

const JobTable = ({ jobs, onDelete, onEdit }: JobTableProps) => {
    return (
        <table>
            <thead>
                <tr>
                    <th>Job</th>
                    <th>Company</th>
                    <th>Location</th>
                    <th>Date Applied</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                {jobs.length === 0 ? (
                    <tr>
                        <td colSpan={6} style={{ textAlign: 'center' }}>No applications found</td>
                    </tr>
                ) : (
                    jobs.map((job) => (
                        <tr key={job.id}>
                            <td>{job.jobTitle}</td>
                            <td>{job.company}</td>
                            <td>{job.location}</td>
                            <td>{job.appliedDate}</td>
                            <td>{job.status}</td>
                            <td>
                                <button onClick={() => onEdit(job)}>Edit</button>
                                <button onClick={() => onDelete(job.id)}>Delete</button>
                            </td>
                        </tr>
                    ))
                )}
            </tbody>
        </table>
    );
};

export default JobTable;