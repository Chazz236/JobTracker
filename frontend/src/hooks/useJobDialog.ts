import { useState } from 'react';
import type { JobResponse } from '@/types';

export const useJobDialog = () => {
  const [edit, setEdit] = useState<JobResponse | null>(null);
  const [isOpen, setIsOpen] = useState(false);

  const openAdd = () => {
    setEdit(null);
    setIsOpen(true);
  };

  const openEdit = (job: JobResponse) => {
    setEdit(job);
    setIsOpen(true);
  };

  const close = () => {
    setEdit(null);
    setIsOpen(false);
  };

  return {
    edit,
    isOpen,
    setIsOpen,
    openAdd,
    openEdit,
    close,
  };
};
