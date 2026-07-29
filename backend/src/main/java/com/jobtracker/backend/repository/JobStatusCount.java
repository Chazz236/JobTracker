package com.jobtracker.backend.repository;

import com.jobtracker.backend.model.JobStatus;

public interface JobStatusCount {
    JobStatus getStatus();
    Long getCount();
}
