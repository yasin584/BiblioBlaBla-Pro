import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from "react-router-dom";
import Sidebar from "./components/Sidbar";
import Header from "./components/Header";

const LeningToevoegen = () => {
    const navigate = useNavigate();
    const [titel, setTitel] = useState('');
    const [auteur, setAuteur] = useState('');
    const [genre, setGenre] = useState('');
    const [datum, setDatum] = useState('');

    const [titelSuggesties, setTitelSuggesties] = useState([]);
    const [auteurSuggesties, setAuteurSuggesties] = useState([]);

    const [foutmelding, setFoutmelding] = useState('');
    const [succesBericht, setSuccesBericht] = useState('');
    const inputRef = useRef(null);

    useEffect(() => {
        inputRef.current?.focus();
    }, []);

    // Suggesties voor titels
    useEffect(() => {
        const token = localStorage.getItem("userToken");
        fetch(`http://localhost:8080/leningen/suggesties/titels?titel=${(titel)}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        })
            .then(res => res.json())
            .then(data => setTitelSuggesties(data))
            .catch(err => console.error(err));
    }, [titel]);

    // Suggesties voor auteurs
    useEffect(() => {
        const token = localStorage.getItem("userToken");
        fetch(`http://localhost:8080/leningen/suggesties/auteurs?auteur=${(auteur)}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        })
            .then(res => res.json())
            .then(data => setAuteurSuggesties(data))
            .catch(err => console.error(err));
    }, [auteur]);

    const handleTitelInput = (waarde) => {
        setTitel(waarde);
        const token = localStorage.getItem("userToken");
        fetch(`http://localhost:8080/leningen/check-genre?titelBoek=${(waarde)}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        })
            .then(res => res.ok ? res.text() : "")
            .then(gevondenGenre => setGenre(gevondenGenre))
            .catch(err => console.error(err));
    };

    const opslaanLening = (e) => {
        e.preventDefault();
        setFoutmelding('');
        setSuccesBericht('');

        if (titel.length < 3 || titel.length > 30) {
            setFoutmelding("De titel moet tussen de 3 en 30 karakters lang zijn.");
            return;
        }
        const titelRegex = /^[a-zA-ZÀ-ÿ\s-]+$/;
        if (!titelRegex.test(titel)) {
            setFoutmelding("De titel mag alleen letters, spaties en koppeltekens bevatten.");
            return;
        }

        const token = localStorage.getItem("userToken");

        const leningData = {
            titel: titel,
            auteur: auteur,
            genre: genre,
            inleverdatum: datum
        };

        fetch('http://localhost:8080/leningen/lenen', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(leningData),
        })
            .then(async (res) => {
                const tekst = await res.text();

                if (!res.ok) {
                    if (res.status === 500) {
                        setFoutmelding("De auteursnaam komt niet overeen met de titel.");
                    } else {
                        setFoutmelding(tekst || "Er is iets misgegaan.");
                    }
                } else {
                    setSuccesBericht("Lening succesvol toegevoegd!");
                    setTitel(''); setAuteur(''); setGenre(''); setDatum('');
                    navigate("/leesgeschiedenis");
                }
            })
            .catch(() => {
                setFoutmelding("De server is onbereikbaar.");
            });
    };

    return (
        <>
            <Header />
            <Sidebar />
            <div className="p-8 max-w-2xl mx-auto mt-10">
                <h1 className="text-3xl font-bold mb-6 text-black">Boek lenen</h1>

                <div className="border border-gray-300 p-8 bg-white mb-6">
                    <form onSubmit={opslaanLening} className="flex flex-col gap-6">
                        <div>
                            <label htmlFor="titel-input" className="block text-sm font-black mb-1 uppercase">Titel</label>
                            <input
                                id="titel-input"
                                list="lijst-titels"
                                className="w-full border border-gray-300 p-2"
                                value={titel}
                                onChange={(e) => handleTitelInput(e.target.value)}
                                ref={inputRef}
                            />
                            <datalist id="lijst-titels">
                                {titelSuggesties.map((suggestie, index) => (
                                    <option key={index} value={suggestie} />))}
                            </datalist>
                        </div>

                        <div>
                            <label htmlFor="auteur-input" className="block text-sm font-black mb-1 uppercase">Auteur</label>
                            <input
                                id="auteur-input"
                                list="lijst-auteurs"
                                className="w-full border border-gray-300 p-2"
                                value={auteur}
                                onChange={(e) => setAuteur(e.target.value)}
                            />
                            <datalist id="lijst-auteurs">
                                {auteurSuggesties.map((auteurNaam, index) => (
                                    <option key={index} value={auteurNaam} />
                                ))}
                            </datalist>
                        </div>

                        <div>
                            <label htmlFor="genre-input" className="block text-sm font-black mb-1 uppercase">Genre</label>
                            <input
                                id="genre-input"
                                className="w-full border border-gray-300 p-2 bg-gray-50"
                                value={genre}
                                onChange={(e) => setGenre(e.target.value)}
                            />
                        </div>

                        <div>
                            <label htmlFor="datum-input" className="block text-sm font-black mb-1 uppercase">Inleverdatum</label>
                            <input
                                id="datum-input"
                                type="date"
                                className="w-48 border border-gray-300 p-2"
                                value={datum}
                                onChange={(e) => setDatum(e.target.value)}
                                required
                            />
                        </div>

                        <div className="flex space-x-4">
                            <button type="submit" className="w-fit bg-[#2b58a1] text-white px-8 py-2 font-bold uppercase hover:bg-blue-800 transition-colors">
                                Lenen
                            </button>
                            <a href="/Leesgeschiedenis" className="w-fit bg-[#2b58a1] text-white px-8 py-2 font-bold uppercase hover:bg-blue-800 transition-colors">
                                Annuleren
                            </a>
                        </div>
                    </form>
                </div>
                {foutmelding && <p className="text-red-600 font-bold">{foutmelding}</p>}
                {succesBericht && <p className="text-green-600 font-bold">{succesBericht}</p>}
            </div>
        </>
    );
};

export default LeningToevoegen;