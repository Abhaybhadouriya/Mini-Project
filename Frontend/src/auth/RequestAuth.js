import { Navigate, Outlet } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { ValidateToken } from '../auth/Validatetoken'; // Import validateToken function

export const RequestAuth = () => {
    const [isAuthenticated, setIsAuthenticated] = useState(null); // null = loading state
    const token = localStorage.getItem('authToken'); // Retrieve JWT from localStorage

    useEffect(() => {
      

        if (token) {
            ValidateToken(token, setIsAuthenticated); 
        } else {
            setIsAuthenticated(false); // No token found
        }
    }, [token]);

    // Show loading indicator while validating the token
    if (isAuthenticated === null) {
        return <div>Loading...</div>;
    }

    // Redirect to login if not authenticated
    return isAuthenticated ? <Outlet /> : <Navigate to="/login" />;
};
