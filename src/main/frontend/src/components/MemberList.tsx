import React, { useEffect, useState } from 'react';
import { 
    Table, 
    TableBody, 
    TableCell, 
    TableContainer, 
    TableHead, 
    TableRow, 
    Paper, 
    Typography,
    Box,
    Alert
} from '@mui/material';
import { Member } from '../types/Member';
import { memberService } from '../services/memberService';

interface MemberListProps {
  refreshTrigger: number;
}
export const MemberList: React.FC<MemberListProps> = ({refreshTrigger}) => {
    const [members, setMembers] = useState<Member[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        loadMembers();
    }, [refreshTrigger]);

    const loadMembers = async () => {
        try {
            const response = await memberService.getAll();
            setMembers(response.data);
            setError(null);
        } catch (err: any) {
            setError('Error loading members');
        }
    };

    return (
        <Box sx={{ mt: 4 }}>
            <Typography variant="h4" component="h2" gutterBottom>
                Registered Members
            </Typography>

            {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Id</TableCell>
                            <TableCell>Name</TableCell>
                            <TableCell>Email</TableCell>
                            <TableCell>Phone Number</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {members.map(member => (
                            <TableRow key={member.id}>
                                <TableCell>{member.id}</TableCell>
                                <TableCell>{member.name}</TableCell>
                                <TableCell>{member.email}</TableCell>
                                <TableCell>{member.phoneNumber}</TableCell>
                            </TableRow>
                        ))}
                        {members.length === 0 && (
                            <TableRow>
                                <TableCell colSpan={3} align="center">
                                    No members registered yet
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    );
};
