// @flow
import React,{ useEffect, useState } from 'react';
import "./Login.css"
import { Footer } from '../footer/Footer';
import { Header } from '../header/Header';
import { ValidateToken } from '../../auth/Validatetoken';
import { Navigate } from 'react-router-dom';
export function Login() {

  const [isLoginActive, setIsLoginActive] = useState(true);
  const [showLoginBT,setShowLoginBT] = useState(true)
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
 
    
  return (
    isAuthenticated?<Navigate to="/"/>:<>
        <Header loginBT={showLoginBT}/>
        <div className='bodyLoginBody' style={{
      
      backgroundImage: `url("/bg_img.svg")`, // Correct path with leading /
      backgroundSize: 'cover',              // Optional: Adjusts background image size
      backgroundPosition: 'center',         // Optional: Centers the background

      }}>

    
       <div className="signup-container">
      
        {!isLoginActive?<SignUpPage isLoginActive={isLoginActive} setIsLoginActive={setIsLoginActive}/>:<LoginPage  isLoginActive={isLoginActive} setIsLoginActive={setIsLoginActive}/>}
    </div>
    </div>
    <Footer/></>
  );
};

const handleRegisterSubmit = async (event) => {
  event.preventDefault(); 

  const formData = {
      id: null, 
      first_name: event.target.elements[0].value,
      last_name: event.target.elements[1].value,
      email: event.target.elements[2].value,
      roll_number: event.target.elements[3].value,
      photograph_path: event.target.elements[4].value,
      cgpa: parseFloat(event.target.elements[5].value),
      total_credits: parseFloat(event.target.elements[6].value),
      graduation_year: parseInt(event.target.elements[7].value),
      password: event.target.elements[8].value,
      confirmPassword: event.target.elements[9].value, // Used only for validation
      domain: null, // Set optional fields to null if not provided
      placement_id: null,
      specialisation: null,
  };
  if (formData.password !== formData.confirmPassword) {
      alert("Passwords do not match.");
      return;
  }
  try {
      const response = await fetch("http://localhost:8080/api/students/register", {
          method: "POST",
          headers: {
              "Content-Type": "application/json",
          },
          body: JSON.stringify(formData),
      });
      if (!response.ok) {
        const errorData = await response.json(); // Parse JSON response body
        console.error("Validation Error:", errorData.message);  // Log the error message
        throw new Error(errorData.message || "Something went wrong");
      }
      
          alert(`Account created successfully! ID: ${response.status}`);
          window.location.reload();
      
  } catch (error) {
      console.error("Error submitting form:", error.message);
      alert(error.message);
  }
};
const handleLoginSubmit = async (event) => {
  event.preventDefault(); // Prevent default form submission behavior

  const formData = {
      email: event.target.elements[0].value,
      password: event.target.elements[1].value,
  };

  try {
      const response = await fetch("http://localhost:8080/api/auth/login", {
          method: "POST",
          headers: {
              "Content-Type": "application/json",
          },
          body: JSON.stringify(formData),
      });

      if (!response.ok) {
          // If response is not OK (status code >= 400)
          const errorData = await response.json(); // Parse JSON response body
          console.error("Login Error:", errorData.message); // Log the error message
          throw new Error(errorData.message || "Something went wrong during login");
      }

      const result = await response.json();

      // Save JWT token to localStorage
      if (result.token) {
          localStorage.setItem("authToken", result.token); // Storing JWT token
          alert("Login successful!");
          window.location.href = "/"; // Redirect to dashboard or other page
      } else {
          throw new Error("Token not received from server.");
      }

  } catch (error) {
      console.error("Error during login:", error.message);
      alert(error.message);
  }
};

function SignUpPage(props) {
  return(
    <>
      <h1 className="signup-title">Sign up</h1>
        <form  onSubmit={handleRegisterSubmit}>
            <input type="text" className="form-control" placeholder="First Name" required/>
            <input type="text" className="form-control" placeholder="last Name" />
            <input type="email" className="form-control" placeholder="Email" required/>
            <input type="text" className="form-control" placeholder="Roll no" required/>
            <input type="text" className="form-control" placeholder="Photo File Path" required/>
            <input type="text" className="form-control" placeholder="CGPA" required/>
            <input type="text" className="form-control" placeholder="Total Credit" required/>
            <input type="text" className="form-control" placeholder="Graduation Year" required/>
            <input type="password" className="form-control" placeholder="Password" required/>
            <input type="password" className="form-control" placeholder="Confirm Password" required/>
            <button type="submit" className="btn btn-primary btn-create">Create account</button>
            <p className="login-text">Already have an account? <button type="button" class="btn btn-primary" onClick={()=>{props.setIsLoginActive(!props.isLoginActive)}}>Log in</button></p>
        </form>
      </>
  )
}

function LoginPage(props) {
  return(
    <>
      <h1 className="signup-title">Login</h1>
        <form onSubmit={handleLoginSubmit}>
          
            <input type="email" className="form-control" placeholder="Email" required/>
            <input type="password" className="form-control" placeholder="Password" required/>
            <button type="submit" className="btn btn-primary btn-create">Login</button>
            <p className="login-text">Don't have an account? <button type="button" class="btn btn-primary" onClick={()=>{props.setIsLoginActive(!props.isLoginActive)}}>Sign in</button></p>
        </form>
      </>
  )
}