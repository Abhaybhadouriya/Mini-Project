export const ValidateToken = async (token, setIsAuthenticated) => {
    try {
        const response = await fetch('http://localhost:8080/api/auth/checkValidation', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, // Include JWT in the Authorization header
            },
        });
        console.log(response.status)
        if (response.ok) {
            setIsAuthenticated(true); // Token is valid
        } else {
            setIsAuthenticated(false); // Token is not valid
        }
    } catch (error) {
        console.error('Token validation failed:', error);
        setIsAuthenticated(false); // Token is not valid
    }
};