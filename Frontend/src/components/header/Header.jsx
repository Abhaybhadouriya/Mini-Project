// @flow
import * as React from 'react';
import "./header.css"
import { Outlet, Link } from "react-router-dom";
export function Header(props) {
    const logout = () => {
        localStorage.removeItem('authToken'); 
        window.location.reload();
      };

  return (
    <nav className="navbar navbar-expand-lg navbar-light  bg-white shadow-sm">
    <div className="container">
      
        <Link className="navbar-brand brand-name"   to="/">Mini Project</Link>
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse justify-content-end" id="navbarNav">
            <ul className="navbar-nav">
                
            {props.loginBT ? (
  <></>
) : (
  <>
    <li className="nav-item">
      <a className="nav-link mx-2" href="/">HOME</a>
    </li>
    <li className="nav-item">
      <Link className="nav-link mx-2" to="/AllTransactions">Bills</Link>
    </li>
    <li className="nav-item">
      <Link className="nav-link mx-2" to="/pay">Pay</Link>
    </li>
    <li className="nav-item">
      <Link className="nav-link mx-2" to="/History">History</Link>
    </li>
    <li className="nav-item">
      <button className="nav-link mx-2" onClick={logout}>Logout</button>
    </li>
  </>
)}

                
            </ul>
        </div>
    </div>
</nav>
  );
};