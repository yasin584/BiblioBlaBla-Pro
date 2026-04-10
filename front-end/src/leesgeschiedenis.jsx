import React, { useState, useEffect, useCallback } from 'react';
import DashboardStats from './components/DashboardStats';
import FilterBar from './components/FilterBar';
import StarRating from './components/StarRating';
import Header from './components/Header';
import Sidebar from './components/Sidbar';

const Leesgeschiedenis = () => {
  const [leningen, setLeningen] = useState([]);
  const [stats, setStats] = useState({});
  const [filters, setFilters] = useState({
    titel: '',
    genre: 'ALLE GENRES',
    start: ''
  });
  const [loading, setLoading] = useState(true);

  const loadData = useCallback(() => {
    setLoading(true);
    const token = localStorage.getItem("userToken");
    const query = `?titel=${filters.titel}&genre=${filters.genre}&start=${filters.start}`;

    fetch(`http://localhost:8080/leningen/mijn-overzicht${query}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
      .then(res => res.json())
      .then(data => setLeningen(Array.isArray(data) ? data : []))
      .catch(err => {
        console.error("Fout bij ophalen leningen:", err);
        setLeningen([]);
      });

    fetch(`http://localhost:8080/leningen/stats/mij`, {
      headers: { Authorization: `Bearer ${token}` }
    })
      .then(res => res.json())
      .then(data => setStats(data))
      .catch(err => console.error("Fout bij ophalen stats:", err))
      .finally(() => setLoading(false));
  }, [filters]);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const handleInleveren = (id) => {
    const token = localStorage.getItem("userToken");

    fetch(`http://localhost:8080/leningen/inleveren/${id}`, {
      method: "PATCH",
      headers: { Authorization: `Bearer ${token}` }
    })
      .then(() => loadData())
      .catch(() => alert("Er ging iets mis bij het inleveren."));
  };


  const handleRate = (id, rating) => {
  const token = localStorage.getItem("userToken");

  fetch(`http://localhost:8080/leningen/beoordeel/${id}?rating=${rating}`, {
    method: "POST",
    headers: { Authorization: `Bearer ${token}` }
  })
  .then(() => {
    setLeningen(prev => {
      return prev.map(lening => {
        if (lening.id === id) {
          return Object.assign({}, lening, { beoordeling: rating });
        } else {
          return lening;
        }
      });
    });
  })
  .catch(err => console.error("Fout:", err));
};

  return (
    <>
      <Sidebar />
      <Header />
      <div className="p-8 bg-gray-50 min-h-screen">
        <div className="ml-64 flex-1">
          <DashboardStats stats={stats} />

          <div className="bg-white rounded-lg shadow-sm border mt-8">
            <FilterBar filters={filters} setFilters={setFilters} />

            <table className="w-full border-collapse text-sm">
              <thead>
                <tr className="border-b border-gray-200">
                  {['Boek', 'Genre', 'Status', 'Ingeleverd', 'Rating', 'Avg Rating'].map(h => (
                    <th key={h} className="text-left text-xs font-medium uppercase tracking-wide text-gray-400 px-4 py-3">
                      {h}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {leningen.map(lening => (
                  <tr key={lening.id} className="border-b border-gray-100 last:border-0 hover:bg-gray-50">
                    <td className="px-4 py-4">
                      <div className="flex items-center gap-3">
                        <div>
                          <div className="font-medium text-gray-900">{lening.titel}</div>
                          <div className="text-xs text-gray-400">{lening.auteurNaam}</div>
                        </div>
                      </div>
                    </td>

                    <td className="px-4 py-4 text-gray-600">{lening.genre}</td>

                    <td className="px-4 py-4">
                      {lening.ingeleverd
                        ? <span className="inline-block text-xs font-medium px-3 py-1 rounded-full bg-green-100 text-green-800 border border-green-300">Ingeleverd</span>
                        : <span className="inline-block text-xs font-medium px-3 py-1 rounded-full bg-red-100 text-red-800 border border-red-300">Niet ingeleverd</span>}
                    </td>

                    <td className="px-4 py-4 text-gray-600">
                      {lening.ingeleverd
                        ? new Date(lening.inleverdatum).toLocaleDateString('nl-NL', { day: 'numeric', month: 'short', year: 'numeric' })
                        : <button onClick={() => handleInleveren(lening.id)} className="text-xs px-3 py-1 border border-gray-300 rounded hover:bg-gray-100">Inleveren</button>}
                    </td>

                    <td className="px-4 py-4">
                      <StarRating rating={lening.beoordeling} onRate={(r) => handleRate(lening.id, r)} />
                    </td>

                    <td className="px-4 py-4 font-medium text-gray-900">
                      {lening.avgRating?.toFixed(1) || '0.0'} <span className="text-amber-400">★</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </>
  );
};

export default Leesgeschiedenis;