import { type ColumnDef, flexRender, getCoreRowModel, useReactTable, type SortingState, getSortedRowModel, getFilteredRowModel, type ColumnFiltersState } from "@tanstack/react-table";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "../ui/table";
import { useState } from "react";
import { Input } from "../ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../ui/select";

interface DataTableProps<TData, TValue> {
    columns: ColumnDef<TData, TValue>[];
    data: TData[];
}

function DataTable<TData, TValue>({ columns, data }: DataTableProps<TData, TValue>) {
    const [sorting, setSorting] = useState<SortingState>([])
    const [globalFilter, setGlobalFilter] = useState('');
    const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);

    const table = useReactTable({
        data,
        columns,
        getCoreRowModel: getCoreRowModel(),
        onSortingChange: setSorting,
        getSortedRowModel: getSortedRowModel(),
        onGlobalFilterChange: setGlobalFilter,
        getFilteredRowModel: getFilteredRowModel(),
        onColumnFiltersChange: setColumnFilters,
        state: {
            sorting,
            globalFilter,
            columnFilters
        }
    });

    return (
        <div>
            <div className='flex items-center justify-between py-4 gap-2'>
                <Input placeholder='Search jobs, companies, or locations...' value={globalFilter ?? ''} onChange={(e) => setGlobalFilter(e.target.value)} className='max-w-sm' />
                <Select value={(table.getColumn('status')?.getFilterValue() as string) ?? 'all'} onValueChange={(value) => table.getColumn('status')?.setFilterValue(value === 'all' ? '' : value)}>
                    <SelectTrigger>
                        <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value='all'>All Statuses</SelectItem>
                        <SelectItem value='APPLIED'>Applied</SelectItem>
                        <SelectItem value='INTERVIEWING'>Interviewing</SelectItem>
                        <SelectItem value='OFFERED'>Offered</SelectItem>
                        <SelectItem value='ACCEPTED'>Accepted</SelectItem>
                        <SelectItem value='REJECTED'>Rejected</SelectItem>
                    </SelectContent>
                </Select>
            </div>
            <div className='rounded-md border'>
                <Table>
                    <TableHeader>
                        {table.getHeaderGroups().map((headerGroup) => (
                            <TableRow key={headerGroup.id}>
                                {headerGroup.headers.map((header) => {
                                    return (
                                        <TableHead key={header.id}>
                                            {header.isPlaceholder
                                                ? null
                                                : flexRender(
                                                    header.column.columnDef.header,
                                                    header.getContext()
                                                )}
                                        </TableHead>
                                    )
                                })}
                            </TableRow>
                        ))}
                    </TableHeader>
                    <TableBody>
                        {table.getRowModel().rows?.length ? (
                            table.getRowModel().rows.map((row) => (
                                <TableRow key={row.id}>
                                    {row.getVisibleCells().map((cell) => (
                                        <TableCell key={cell.id}>
                                            {flexRender(cell.column.columnDef.cell, cell.getContext())}
                                        </TableCell>
                                    ))}
                                </TableRow>
                            ))
                        ) : (
                            <TableRow>
                                <TableCell colSpan={columns.length} className='h-24 text-center'>
                                    No results.
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </div>
        </div>
    )
}

export default DataTable;