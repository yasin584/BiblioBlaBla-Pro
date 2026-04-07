import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./login";
import leesgeschiedenis from "./leesgeschiedenis";

const App = () => {
  return (
    <BrowserRouter>
      <Routes>

          {/*iedereen heeft toegang*/}
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="/login" element={<Login />} />
          <Route path="/logout" element={<Logout />} />
          <Route path="/unauthorized" element={<Unauthorized />} />


          {/* alleen ingelogde gebruiker heeft toegang*/}
              <Route path="/leesgeschiedenis" element={<leesgeschiedenis />} />      

      </Routes>
    </BrowserRouter>
  );
};

export default App
