import api from './api';
import type { CompanyResponse } from '@/types';

export const getAllCompanies = async (): Promise<CompanyResponse[]> => {
    const response = await api.get<CompanyResponse[]>('/companies');
    return response.data;
}