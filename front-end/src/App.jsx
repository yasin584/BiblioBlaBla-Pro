import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./Login";
//import Leesgeschiedenis from "./leesgeschiedenis"; 
import ProtectedRoute from "./components/Protectedrout";

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        {/* Iedereen heeft toegang */}
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login />} />
        
        {/* Alleen ingelogde gebruikers hebben toegang */}
        <Route 
          path="/leesgeschiedenis" 
          element={
            <ProtectedRoute>
              <Leesgeschiedenis />
            </ProtectedRoute>
          } 
        />
        
      </Routes>
    </BrowserRouter>
  );
};

export default App;
