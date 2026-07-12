import api from './api';
import type {LoginRequest} from "../types/auth.ts";
import type  {RegisterRequest} from "../types/auth.ts";
import type {AuthResponse} from "../types/auth.ts";

export const login = async (data: LoginRequest): Promise<AuthResponse>  => {
    const response = await api.post('/auth/login', data);
    localStorage.setItem('token', response.data.token);
    return response.data;
};

export const register = async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/register', data);
    localStorage.setItem('token', response.data.token);
    return response.data;
};