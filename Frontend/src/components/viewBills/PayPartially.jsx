
import React, { useState,useEffect } from 'react';
import { GetData } from '../../auth/GetData';

const PayPartially = (props) => {
  const [amount, setAmount] = useState(props.type===1?props.price:0);
  const [error, setError] = useState('');
  const [userData,setUserData] = useState([]);
  const [isDataFetched, setIsDataFetched] = useState(null); // null = loading state



  const handleAmountChange = (event) => {
    const inputAmount = event.target.value;
    
    // Validate if amount is greater than the supplied price
    if (parseFloat(inputAmount) > props.price) {
      setError('Amount cannot be greater than the remaining amount');
    }else if(parseFloat(inputAmount) <0){
        setError('Amount cannot be less than the 0.');

    } else {
      setError('');
    }

    setAmount(inputAmount);
  };

const token = localStorage.getItem('authToken');
    useEffect(() => {
        if (token) {
            GetData(token,setIsDataFetched,setUserData);
        } else {
            setIsDataFetched(false);
        }
    }, [token]);


    // Show loading indicator while validating the token
    if (isDataFetched === null) {
        return <div>Loading...</div>;
    }







  const payFeesNow = async () => {
    const inputAmount = parseFloat(amount);

    if (isNaN(inputAmount) || inputAmount <= 0 || inputAmount > props.price) {
      alert("Please Enter a Correct Amount");
      return;
    }

  const formData = {
    id: null, 
    description:props.type===1?'Full Payment':'Partial Payment',
    amount: inputAmount,
    student_id:userData['id'],
    bill_id:props.billId    
}

try {
    const response = await fetch("http://localhost:8080/api/students/payFees", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            'Authorization': `Bearer ${token}`,
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

console.log("Payment successful for amount:", inputAmount);
  };














  return (
    <div>
      <h3>{props.type===1?"Full Payment":"Partial Payment"}</h3>
      <label for="enterAmount"><b>Enter Amount</b></label>
      <input
  id="enterAmount"
  type="number"
  value={props.type === 1 ? props.price : amount}
  onChange={handleAmountChange}
  placeholder="Enter amount"
  readOnly={props.type === 1}
/>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <p>Remainaing Amount :  &#8377; {props.price-amount}</p>
      <table>
        <tbody>
            <tr>
                <td>
                <button type="button" class="btn btn-primary" onClick={payFeesNow}>PAY</button>   

                </td>
                <td>
      <button type="button" class="btn btn-danger" onClick={()=>props.setShowPaymentPage(0)}>CANCEL</button>   
                    
                </td>
            </tr>
        </tbody>
      </table>
    </div>
  );
};

export default PayPartially;