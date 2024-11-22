import React,{useState, useEffect } from 'react';
import { Header } from '../header/Header';
import { Footer } from '../footer/Footer';
import PayPartially from './PayPartially';
import { GetData } from '../../auth/GetData';

function BillPayments(props) {
  const [unpaidBills, setUnpaidBills] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const token = localStorage.getItem('authToken'); // Retrieve JWT from localStorage
  const [showLoginBT,setShowLoginBT] = useState(false)
  const [showPaymentPage,setShowPaymentPage] = useState(0); // for payment component
  const [userData,setUserData] = useState([]);
  const [isDataFetched, setIsDataFetched] = useState(null); // null = loading state
  useEffect(() => {
    // Function to fetch unpaid bills from the backend
    const fetchUnpaidBills = async () => {
      GetData(token,setIsDataFetched,setUserData);

      if(isDataFetched){
        try {
          const response = await fetch('http://localhost:8080/api/students/unpaidbills', {
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
      backgroundImage: `url("/bg_img.svg")`, 
      backgroundSize: 'cover',             
      backgroundPosition: 'center',        
  }}>
      <div className="container min-vh-100 " >
  <h2 style={{paddingTop:50}}>Pending Bills</h2>
  <p>All the Pending bills are as follows. <br/><small><strong>**Note: Click on row to view more details</strong></small></p>  
  <div style={{overflowX: 'auto'}}>          
  <table className="table table-hover bg-light" >
    <thead>
    <tr  >
        <td><b>Payment ID</b></td>
        <td><b>Amount</b></td>
        <td><b>Remaining</b></td>
        <td><b>Deadline</b></td>
        <td><b>Pay</b></td>
      </tr>
    </thead>
    <tbody>

 { 
    unpaidBills.map((doc,index)=>{
        return(
        <ShowBills
        dataforPayment={doc}
        bill_id={doc.billId}
        description={doc.description}
        amount={doc.amount}
        deadline={doc.deadline}
        remainingAmount={doc.remainingBalance}
        paid={doc.totalPaid}
        key={index}
        index={index}
        parts={doc.parts}
        showPaymentPage={showPaymentPage}
        setShowPaymentPage={setShowPaymentPage}
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

export default BillPayments;


const ShowBills = (props) => {
    return (
        <>
        <tr   data-bs-toggle="collapse" data-bs-target={"#row"+props.index} aria-expanded="false"  style={{cursor: 'pointer'}}>
        <td><b>{props.bill_id}</b></td>
        <td>&#8377; {props.amount}/-</td>
        <td>{props.remainingAmount}</td>
        <td>{props.deadline}</td>
        <td><button type="button" class="btn btn-primary">Pay</button></td>
      </tr>
      <tr id={'row'+props.index} class="collapse">
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

            { props.showPaymentPage===0?<tr>
                <td>
                <button type="button" class="btn btn-primary"  onClick={() => props.setShowPaymentPage(1)} >Pay Full</button>
                </td>
                <td>
                <button type="button" class="btn btn-warning"  onClick={() => props.setShowPaymentPage(2)}  >Pay Partially</button>
                </td>
              </tr>:<></>}
              <tr >
                <td colSpan={2}>
                {
                     props.showPaymentPage!==0?<PayPartially billId={props.bill_id} price={props.remainingAmount} type={props.showPaymentPage} setShowPaymentPage={props.setShowPaymentPage}/>:<></> 
                }
               </td>
              </tr>
             
          
            </table>
          </td>
   
    </tr>

      </>
    )
    }

