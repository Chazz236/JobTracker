import type { Column } from '@tanstack/react-table';
import { ArrowUpDown, ArrowUp, ArrowDown } from 'lucide-react';
import { Button } from './button';

interface SortableHeaderProps<TData, TValue> {
  column: Column<TData, TValue>;
  title: string;
  enableSorting: boolean;
}

export const SortableHeader = <TData, TValue>({
  column,
  title,
  enableSorting,
}: SortableHeaderProps<TData, TValue>) => {
  if (!enableSorting) {
    return <div className="font-medium">{title}</div>;
  }

  const isSorted = column.getIsSorted();

  const Icon =
    isSorted === 'desc'
      ? ArrowDown
      : isSorted === 'asc'
        ? ArrowUp
        : ArrowUpDown;

  return (
    <div className="flex items-center space-x-2">
      <Button variant="ghost" onClick={() => column.toggleSorting()}>
        <span>{title}</span>
        <Icon className="ml-2 h-4 w-4" />
      </Button>
    </div>
  );
};
