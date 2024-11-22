// @flow 
import React,{useEffect,useState} from 'react';
import { Header } from '../header/Header';
import { Footer } from '../footer/Footer';
import { Outlet, Link } from "react-router-dom";
import { GetData } from '../../auth/GetData';
const dataArrary = [
    {
    imgSrc:"https://img.freepik.com/premium-vector/woman-with-coins-transparent-glass-jar-saving-education-studying-economics-money-concept-vector-illustration_620585-1708.jpg?w=900",
    text:"Bills",
    content:"Check Your Bills",
    buttonText : "View",
    url:"/AllTransactions"
    },
    {
        imgSrc:"https://img.freepik.com/free-vector/debt-academic-loan_603843-1557.jpg?t=st=1731590877~exp=1731594477~hmac=4c8c744eef0b1c7ef67fd61a08f6b25e9df247f23653ebd1cd209b20fdca1db3&w=996",
        text:"View and Pay Fees",
        content:"Check Your pending Fees",
        buttonText : "Pay Now",
        url:"/pay"
    },
    {
     imgSrc:"https://img.freepik.com/free-vector/flat-receipt-concept_23-2147915073.jpg?t=st=1731591264~exp=1731594864~hmac=89e5682a99212bbf9f69d7be6b6cd0b27ef9769564df7e9584f97dfcb738ac5a&w=900",
     text:"View Payment History",
     content:"Check Your Payment History",
     buttonText : "View History",
     url:"/history"

    }
]


export const Home = () => {

    const [userData,setUserData] = useState([]);
    const [isDataFetched, setIsDataFetched] = useState(null); // null = loading state
    const [showLoginBT,setShowLoginBT] = useState(false)

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
    return (
        <div style={{
      
            backgroundImage: `url("/bg_img.svg")`, // Correct path with leading /
            backgroundSize: 'cover',              // Optional: Adjusts background image size
            backgroundPosition: 'center',         // Optional: Centers the background

        }}>
        <Header loginBT={showLoginBT}/>
        <h2 style={{textAlign:'center',width:'100%',margin:'15px 0px 5px 0px'}}> College Payment Page</h2>
        <div className="userDetialsContainer" style={{width:'100%',display:'flex',flexDirection:'row',justifyContent:'center', paddingTop:15}}> 

        <div className="card" style={{width:"400px"}}>
            <div className="card-body">
                <h4 className="card-title">{'Hello! '+userData['firstName']+' '+userData['lastName']}</h4>
                <p className="card-text">{'Roll No : '+userData['rollNo']}</p>
                <p className="card-text">{'Email ID : '+userData['email']}</p>

            </div>
            </div>
        </div>
        <div style={{
                    display: 'flex',
                    flexDirection: 'row',
              
                    flexWrap: 'wrap',
                    width: '100%',
                    justifyContent: 'center',
                    margin: '5% 0px 10% 0px'
                }}>
       
        {
            dataArrary.map((doc, index) => 
                {   
                    return(
                        <CenterBoxes imgSrc={doc.imgSrc} text={doc.text} content={doc.content} buttonText={doc.buttonText}  key={index} targetUrl={doc.url}/>
                    )
                    
                })
                
            }
            </div>
        
        <Footer/>
        </div>
    );
};



const CenterBoxes = (props)=> {
    return(
        <div className="card" style={{width:"400px", margin:5}}>
  <img className="card-img-top" src={props.imgSrc} alt="Cardimage"/>
  <div className="card-body" style={{}}>
    <h4 className="card-title">{props.text}</h4>
    <p className="card-text">{props.content}</p>
    <Link className="btn btn-primary"   to={props.targetUrl}>{props.buttonText}</Link>
  </div>
</div>
    )
}

