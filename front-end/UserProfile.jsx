import React, { useEffect, useState } from 'react';
import { getAuth, onAuthStateChanged, signOut } from 'firebase/auth';

const UserProfile = () => {
  const [user, setUser] = useState(null);
  const auth = getAuth();

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (currentUser) => {
      if (currentUser) {
        setUser(currentUser); // Gebruiker is ingelogd
      } else {
        setUser(null); // Geen gebruiker ingelogd
      }
    });

    return () => unsubscribe(); // Cleanup
  }, [auth]);

  const handleLogout = () => {
    signOut(auth)
      .then(() => {
        alert('Uitgelogd!');
      })
      .catch((error) => {
        console.error('Fout bij uitloggen:', error);
      });
  };

  if (!user) {
    return <p>Niet ingelogd</p>;
  }

  return (
    <div>
      <h1>Welkom, {user.email}</h1>
      <button onClick={handleLogout}>Uitloggen</button>
    </div>
  );
};

export default UserProfile;