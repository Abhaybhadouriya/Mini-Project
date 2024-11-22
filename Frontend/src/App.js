import './App.css';
import {Login} from './components/login/Login';
import { Home } from './components/mainScreen/Home';
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import PaymentHistoryPage from './components/paymentHistory/PaymentHistoryPage';
import BillPayments from './components/viewBills/BillPayments';
import AllTransactions from './components/payemntpage/AllTransactions';
import { RequestAuth } from './auth/RequestAuth';
function App() {
  return (
    <>
            <Router>
                <Routes>
                    <Route
                        
                        path="/login"
                        element={<Login />}
                    />
                    <Route element={<RequestAuth/>}>
                    <Route
                        exact
                        path="/"
                        element={<Home />}
                    />
                    
                    <Route
                        path="/history"
                        element={<PaymentHistoryPage/>}
                    />
                     <Route
                        path="/pay"
                        element={<BillPayments/>}
                    />
                     <Route
                        path="/AllTransactions"
                        element={<AllTransactions/>}
                    />
                    <Route
                        path="*"
                        element={<Navigate to="/" />}

                    />
                    </Route>
                </Routes>
            </Router>
        </>
  );
}

export default App;
