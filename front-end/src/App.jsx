import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./Login";
import LeningToevoegen from "./LeningToevoegen";
import ProtectedRoute from "./components/ProtectedRoute";

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login />} />

        <Route
          path="/lenenToevoegen" 
          element={
            <ProtectedRoute>
              <LeningToevoegen />
            </ProtectedRoute>
          }
        />


        <Route
          path="/leesgeschiedenis"
          element={
            <ProtectedRoute>
              {/* <Leesgeschiedenis /> */}
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
};

export default App;