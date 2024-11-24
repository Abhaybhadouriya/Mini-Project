import React,{useState, useEffect } from 'react';
import { Footer } from '../footer/Footer';
import { Header } from '../header/Header';
import { GetData } from '../../auth/GetData';

function AllTransactions(props) {
  const [unpaidBills, setUnpaidBills] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const token = localStorage.getItem('authToken'); // Retrieve JWT from localStorage
  const [showLoginBT,setShowLoginBT] = useState(false)
  const [userData,setUserData] = useState([]);
  const [isDataFetched, setIsDataFetched] = useState(null); // null = loading state

  useEffect(() => {
    // Function to fetch unpaid bills from the backend
   
      
   
    
    const fetchUnpaidBills = async () => {
      
        GetData(token,setIsDataFetched,setUserData);

        if(isDataFetched){
          try {

          const response = await fetch('http://localhost:8080/api/students/viewbills', {
            method: 'POST', // Change from GET to POST
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, 
            },
            body: JSON.stringify({
                studentId: userData['id']
            })
        });
            
            if (!response.ok) {
                throw new Error('Failed to fetch unpaid bills');
            }

            const data = await response.json();
            setUnpaidBills(data);
            console.table(data);
            setLoading(false);
            console.log(data);
        } catch (error) {
            setError(error.message);
            alert("Invalid Session");
            localStorage.removeItem('authToken'); 
            window.location.reload();
            setLoading(false);
        }
    };
    }
    
    fetchUnpaidBills();
}, [isDataFetched]);

    // Show loading indicator while validating the token
    if (isDataFetched === null) {
      return <div>Loading...</div>;
  }



if (loading) {
  return <div>Loading...</div>;
}

if (error) {
  return <div>Error: {error}</div>;
}
    return (
        <>
        <Header loginBT={showLoginBT}/>
        <div  className="min-vh-100 " style={{
      backgroundImage: `url("/bg_img.svg")`, 
      backgroundSize: 'cover',             
      backgroundPosition: 'center',        
  }}>
      <div className="container min-vh-100 " >
  <h2 style={{paddingTop:50}}>All Bills</h2>
  <p>All the bills are as follows. <br/><small><strong>**Note: Click on row to view more details</strong></small></p>  
  <div style={{overflowX: 'auto'}}>          
  <table className="table table-hover bg-light" >
    <thead>
    <tr  >
        <td><th>Payment ID</th></td>
        <td><th>Amount</th></td>
        <td><th>Remaining</th></td>
        <td><th>Deadline</th></td>
        <td><th>View</th></td>
      </tr>
    </thead>
    <tbody>

 {
    unpaidBills.map((doc,index)=>{
        return(
        <ShowBills
        bill_id={doc.billId}
     
        amount={doc.amount}
     
        deadline={doc.deadline}
        remainingAmount={doc.remaining}
          paid={doc.paid}
          description={doc.description}
        key={index}
        index={index}
        parts={doc.parts}
      
        />
    )
    })

 }  
       </tbody>
       </table></div></div></div>
       <Footer/>
       </>
    );
}

export default AllTransactions;

const ShowBills = (props) => {
    return (
        <>
        <tr   data-bs-toggle="collapse" data-bs-target={"#row"+props.bill_id} aria-expanded="false"  style={{cursor: 'pointer'}}>
        <td><b>{props.bill_id}</b></td>
        <td>&#8377; {props.amount}/-</td>
        <td>{props.remainingAmount}</td>
        <td>{props.deadline}</td>
        <td><button type="button" class="btn btn-primary">View</button></td>
      </tr>
      <tr id={'row'+props.bill_id} class="collapse">
      <td colspan="5">
            <table class="table">
              <tr>
                <td>Bill ID</td>
                <td>:&nbsp;&nbsp; {props.bill_id}</td>
              </tr>
              <tr>
                <td>Amount</td>
                <td>:&nbsp;&nbsp; &#8377; {props.amount}/-</td>
              </tr>
              <tr>
                <td>Due Date</td>
                <td>:&nbsp;&nbsp; {props.deadline}</td>
              </tr>
             
              
              <tr>
                <td>Remaining Amount</td>
                <td>:&nbsp;&nbsp;  &#8377; {props.remainingAmount}/-</td>
              </tr>
              <tr>
                <td>Paid Amount</td>
                <td>:&nbsp;&nbsp;  &#8377; {props.paid}/-</td>
              </tr>
              <tr>
                <td>Description</td>
                <td>:&nbsp;&nbsp; {props.description}</td>
              </tr>
             
               {props.parts.length==0?<></>:<><tr><strong>Payment History</strong></tr>{props.parts.map((data,index)=>{
                return (<>
                  <tr>
                    <th>Payement ID</th><th>Amount</th><th>Payment Date</th><th>Description</th>
                  </tr>
                  <tr>
                    <td>
                      {data.paymentId}
                    </td> 
                    <td>
                      {data.amount}
                    </td>
                    <td>
                      {data.paymentDate}
                    </td>
                    <td>
                      {data.description}
                    </td>
                  </tr></>
                 )
               })}</>}
               
          
            </table>
          </td>
   
    </tr>
      </>
    )
    }
