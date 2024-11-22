import { jwtDecode } from 'jwt-decode';
export const GetData = async (token, setIsDataFetched,setUserData) => {
    try {
        // Decode the JWT token
        const decoded = jwtDecode(token);
        
        // Extract data from the decoded JWT
        const userId = decoded.id;
        const userFirstName = decoded.firstName;
        const userLastName = decoded.lastName;
        const userEmail = decoded.email;
        const userRollNo = decoded.rollNo;

        // Store data in state
        setUserData({
            id: userId,
            firstName: userFirstName,
            lastName: userLastName,
            email: userEmail,
            rollNo: userRollNo,
        });

        // Set authentication status to true
        setIsDataFetched(true);
    } catch (error) {
        console.error("Invalid or expired token", error);
        setIsDataFetched(false);  // In case the token is invalid
    }
};