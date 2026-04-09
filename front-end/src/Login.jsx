import { useState } from "react";
import { useEffect, useRef } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function Login() {
    const [email, setEmail] = useState('');
    const [wachtwoord, setwachtwoord] = useState('');
    const [error, setError] = useState('');
    const inputRef = useRef(null);
    const navigate = useNavigate();

    useEffect(() => {
        inputRef.current.focus();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post("http://localhost:8080/auth/login", {
                email,
                wachtwoord,
            });

            const token = response.data.token;
            localStorage.setItem("userToken", token);


            setError("");
            // window.location.href = "/leningen/mijn-overzicht";
            setError("");
            navigate("/lenenToevoegen");


        } catch (err) {
            setError("Gebruikersnaam of wachtwoord is onjuist");
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center">
            <div className="bg-white p-6 rounded shadow-md">
                <h2 className="text-2xl font-semibold mb-2">Welkom Terug</h2>
                <p className="text-base mb-4">
                    Bekijk je lesgeschiedenis en beoordelingen
                </p>

                <form onSubmit={handleSubmit}>
                    {error ? <p className="text-red-600 font-medium">{error}</p> : null}

                    <div className="flex flex-col">
                        <label htmlFor="email">Email</label>
                        <input
                            id="email"
                            type="email"
                            required
                            ref={inputRef}
                            onChange={(e) => {
                                setEmail(e.target.value);
                                setError("");
                            }}
                            className="my-2 h-10 border border-gray-400 rounded px-2"
                        />

                        <label htmlFor="wachtwoord">Wachtwoord</label>
                        <input
                            id="wachtwoord"
                            type="password"
                            required
                            onChange={(e) => {
                                setwachtwoord(e.target.value);
                                setError("");
                            }}
                            className="my-2 h-10 border border-gray-400 rounded px-2"
                        />                    </div>

                    <button
                        type="submit"
                        className="bg-blue-600 text-white font-bold w-full hover:bg-blue-800 mt-4 py-2 rounded text-xl"
                    >
                        Inloggen
                    </button>
                </form>
            </div>
        </div>
    );
}

export default Login;