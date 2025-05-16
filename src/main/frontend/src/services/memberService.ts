import axios from 'axios';
import { Member } from '../types/Member';

const API_URL = '/rest/members';

export const memberService = {
    getAll: () => axios.get<Member[]>(API_URL),
    create: (member: Omit<Member, 'id'>) => axios.post<Member>(API_URL, member),
    getById: (id: number) => axios.get<Member>(`${API_URL}/${id}`)
};
