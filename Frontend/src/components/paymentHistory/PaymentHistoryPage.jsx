import React,{useState, useEffect } from 'react';
import { Header } from '../header/Header';
import { Footer } from '../footer/Footer';
import { GetData } from '../../auth/GetData';

function PaymentHistoryPage(props) {
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
          const response = await fetch('http://localhost:8080/api/students/paidbills', {
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
            setLoading(false);
        }
    };
  }
    fetchUnpaidBills();
}, [isDataFetched]);
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
      
      backgroundImage: `url("/bg_img.svg")`, // Correct path with leading /
      backgroundSize: 'cover',              // Optional: Adjusts background image size
      backgroundPosition: 'center',         // Optional: Centers the background

  }}>
            <div className="container min-vh-100 " >
  <h2 style={{paddingTop:50}}>Payment History</h2>
  <p>All the payments done by you are below. <br/><small><strong>**Note: Click on row to view more details</strong></small></p>  
  <div style={{overflowX: 'auto'}}>          
  <table className="table table-hover bg-light" >
    <thead>
    <tr  >
        <td><b>Payment ID</b></td>
        <td><b>Amount</b></td>
        <td><b>date</b></td>
        <td><b>Action</b></td>
      </tr>
    </thead>
    <tbody>
    {
        unpaidBills.map((doc,index)=>{
            return(
                <RowStructureIterator
                pid={doc.billId}
                amount={doc.amount}
                date={doc.deadline}
                bid={doc.billId}
                remainingAmount={doc.remainingBalance}
                key={index}
                index={index}
                description={doc.description}
                
                
                />
            )
        })
    }
     
    
    </tbody>

  </table>
  </div>
</div>

        </div>
<Footer/>
        </>
    );
}

export default PaymentHistoryPage;


const RowStructureIterator = (props) => {
return (
    <>
    <tr   data-bs-toggle="collapse" data-bs-target={"#row"+props.index} aria-expanded="false"  style={{cursor: 'pointer'}}>
    <td><b>{props.pid}</b></td>
    <td>&#8377; {props.amount}/-</td>
    <td>{props.date}</td>
    <td><span style={{color:'#0d6efd'}}><u>More</u></span></td>
  </tr>
  <tr id={'row'+props.index} class="collapse">
  <td colspan="4">
        <table class="table">
          <tr>
            <td>Payment ID</td>
            <td>:&nbsp;&nbsp; {props.pid}</td>
          </tr>
          <tr>
            <td>Amount</td>
            <td>:&nbsp;&nbsp; &#8377; {props.amount}/-</td>
          </tr>
          <tr>
            <td>Payment Date</td>
            <td>:&nbsp;&nbsp; {props.date}</td>
          </tr>
          <tr>
            <td>Bill ID</td>
            <td>:&nbsp;&nbsp; {props.bid}</td>
          </tr>
          <tr>
            <td>Remaining Amount</td>
            <td>:&nbsp;&nbsp;  &#8377; {props.remainingAmount}/-</td>
          </tr>
          <tr>
            <td>Description</td>
            <td>:&nbsp;&nbsp; {props.description}</td>
          </tr>
        </table>
      </td>
  </tr>
  </>
)
}

