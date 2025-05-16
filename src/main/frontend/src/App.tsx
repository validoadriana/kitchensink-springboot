import React, { useState } from 'react';
import { Container, CssBaseline, ThemeProvider, createTheme } from '@mui/material';
import { MemberForm } from './components/MemberForm';
import { MemberList } from './components/MemberList';

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#2196f3',
    },
    secondary: {
      main: '#f50057',
    },
  },
});

function App() {
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const handleMemberAdded = () => {
    setRefreshTrigger(prev => prev + 1);
  };
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Container maxWidth="md">
        <MemberForm onMemberAdded={handleMemberAdded}/>
        <MemberList refreshTrigger={refreshTrigger}/>
      </Container>
    </ThemeProvider>
  );
}

export default App;
