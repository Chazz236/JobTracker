import type { Job } from "../types";

interface JobTableProps {
    jobs: Job[];
}

const JobTable = ({ jobs }: JobTableProps) => {
    return (
        <table>
            <thead>
                <tr>
                    <th>Job</th>
                    <th>Company</th>
                    <th>Location</th>
                    <th>Date Applied</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                {jobs.map((job) => (
                    <tr key={job.id}>
                        <td>{job.jobTitle}</td>
                        <td>{job.company}</td>
                        <td>{job.location}</td>
                        <td>{job.appliedDate}</td>
                        <td>{job.status}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
};

export default JobTable;