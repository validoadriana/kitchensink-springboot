import React, { useState } from 'react';
import { TextField, Button, Box, Typography, Alert } from '@mui/material';
import { memberService } from '../services/memberService';
interface MemberFormProps {
  onMemberAdded: () => void;
}
export const MemberForm: React.FC<MemberFormProps> = ({onMemberAdded}) => {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        phoneNumber: ''
    });
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await memberService.create(formData);
            setFormData({ name: '', email: '', phoneNumber: '' });
            setError(null);
            setSuccess(true);
            onMemberAdded();
        } catch (err: any) {
            setError(JSON.stringify(err.response?.data) || 'Error registering member');
            setSuccess(false);
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ maxWidth: 400, mx: 'auto', mt: 4 }}>
            <Typography variant="h4" component="h1" gutterBottom>
                Member Registration
            </Typography>

            {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
            {success && <Alert severity="success" sx={{ mb: 2 }}>Member registered successfully!</Alert>}

            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <TextField
                    label="Name"
                    value={formData.name}
                    onChange={e => setFormData({...formData, name: e.target.value})}
                    required
                    fullWidth
                />

                <TextField
                    label="Email"
                    type="email"
                    value={formData.email}
                    onChange={e => setFormData({...formData, email: e.target.value})}
                    required
                    fullWidth
                />

                <TextField
                    label="Phone Number"
                    value={formData.phoneNumber}
                    onChange={e => setFormData({...formData, phoneNumber: e.target.value})}
                    required
                    fullWidth
                />

                <Button 
                    type="submit" 
                    variant="contained" 
                    color="primary"
                    fullWidth
                >
                    Register
                </Button>
            </Box>
        </Box>
    );
};
