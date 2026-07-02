import { getAllCompanies } from '@/services/companyService'
import { useQuery } from '@tanstack/react-query'
import type { CompanyResponse } from '@/types';

export const useCompanies = () => {
    const { data: companies = [], isPending, error } = useQuery<CompanyResponse[]>({
        queryKey: ['companies'],
        queryFn: getAllCompanies
    });

    return {
        companies,
        isPending,
        error
    }
}