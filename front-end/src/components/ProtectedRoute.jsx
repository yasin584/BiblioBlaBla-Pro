import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
  // Hier moet je checken of de gebruiker is ingelogd. 
  // Dit kan via een AuthContext, Redux, of simpelweg localStorage.
  const isAuthenticated = localStorage.getItem("userToken"); // Voorbeeld check

  if (!isAuthenticated) {
    // Niet ingelogd? Stuur ze naar de login pagina
    return <Navigate to="/login" replace />;
  }

  // Wel ingelogd? Laat de gevraagde pagina zien
  return children;
};

export default ProtectedRoute;