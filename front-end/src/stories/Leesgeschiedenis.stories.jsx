import React, { useState } from 'react';
import DashboardStats from './components/DashboardStats';
import FilterBar from './components/FilterBar';
import StarRating from './components/StarRating';

export default {
  title: 'Bibliotheek/Leesgeschiedenis',
  parameters: {
    layout: 'fullscreen',
    docs: {
      description: {
        component:
          'Volledige Leesgeschiedenis-pagina met mock data. Gebruikt geen echte API — ideaal voor UI-review en design feedback.',
      },
    },
  },
};

const MOCK_LENINGEN = [
  {
    id: 1,
    titel: 'De Hobbit',
    auteurNaam: 'J.R.R. Tolkien',
    genre: 'Fantasy',
    ingeleverd: true,
    inleverdatum: '2024-03-10',
    beoordeling: 5,
    avgRating: 4.7,
  },
  {
    id: 2,
    titel: '1984',
    auteurNaam: 'George Orwell',
    genre: 'Dystopian',
    ingeleverd: true,
    inleverdatum: '2024-01-22',
    beoordeling: 4,
    avgRating: 4.5,
  },
  {
    id: 3,
    titel: 'Middernacht in Parijs',
    auteurNaam: 'Amélie Fontaine',
    genre: 'Romance',
    ingeleverd: false,
    inleverdatum: null,
    beoordeling: 0,
    avgRating: 3.8,
  },
  {
    id: 4,
    titel: 'Sherlock Holmes',
    auteurNaam: 'Arthur Conan Doyle',
    genre: 'Mystery',
    ingeleverd: true,
    inleverdatum: '2024-05-14',
    beoordeling: 3,
    avgRating: 4.2,
  },
  {
    id: 5,
    titel: 'Anna Karenina',
    auteurNaam: 'Leo Tolstoy',
    genre: 'Classic',
    ingeleverd: false,
    inleverdatum: null,
    beoordeling: 0,
    avgRating: 4.0,
  },
];

const MOCK_STATS = {
  totaalGeleend: 5,
  populairsteGenre: 'Fantasy',
};

const defaultFilters = { titel: '', genre: 'ALLE GENRES', start: '' };

const LeesgeschiedenisUI = ({ leningen, stats, onInleveren, onRate }) => {
  const [filters, setFilters] = useState(defaultFilters);

  const gefilterd = leningen.filter((l) => {
    const titelMatch =
      filters.titel === '' ||
      l.titel.toLowerCase().includes(filters.titel.toLowerCase()) ||
      l.auteurNaam.toLowerCase().includes(filters.titel.toLowerCase());
    const genreMatch =
      filters.genre === 'ALLE GENRES' || l.genre === filters.genre;
    const datumMatch =
      filters.start === '' ||
      (l.inleverdatum && l.inleverdatum >= filters.start);
    return titelMatch && genreMatch && datumMatch;
  });

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <div style={{ marginLeft: '256px' }}>
        <DashboardStats stats={stats} />

        <div className="bg-white rounded-lg shadow-sm border mt-8">
          <FilterBar filters={filters} setFilters={setFilters} />

          <table className="w-full border-collapse text-sm">
            <thead>
              <tr className="border-b border-gray-200">
                {['Boek', 'Genre', 'Status', 'Ingeleverd', 'Rating', 'Avg Rating'].map((h) => (
                  <th
                    key={h}
                    className="text-left text-xs font-medium uppercase tracking-wide text-gray-400 px-4 py-3"
                  >
                    {h}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {gefilterd.length === 0 && (
                <tr>
                  <td colSpan={6} className="px-4 py-8 text-center text-gray-400 text-sm">
                    Geen leningen gevonden voor deze filters.
                  </td>
                </tr>
              )}
              {gefilterd.map((lening) => (
                <tr
                  key={lening.id}
                  className="border-b border-gray-100 last:border-0 hover:bg-gray-50"
                >
                  <td className="px-4 py-4">
                    <div className="font-medium text-gray-900">{lening.titel}</div>
                    <div className="text-xs text-gray-400">{lening.auteurNaam}</div>
                  </td>

                  <td className="px-4 py-4 text-gray-600">{lening.genre}</td>

                  <td className="px-4 py-4">
                    {lening.ingeleverd ? (
                      <span className="inline-block text-xs font-medium px-3 py-1 rounded-full bg-green-100 text-green-800 border border-green-300">
                        Ingeleverd
                      </span>
                    ) : (
                      <span className="inline-block text-xs font-medium px-3 py-1 rounded-full bg-red-100 text-red-800 border border-red-300">
                        Niet ingeleverd
                      </span>
                    )}
                  </td>

                  <td className="px-4 py-4 text-gray-600">
                    {lening.ingeleverd ? (
                      new Date(lening.inleverdatum).toLocaleDateString('nl-NL', {
                        day: 'numeric',
                        month: 'short',
                        year: 'numeric',
                      })
                    ) : (
                      <button
                        onClick={() => onInleveren(lening.id)}
                        className="text-xs px-3 py-1 border border-gray-300 rounded hover:bg-gray-100"
                      >
                        Inleveren
                      </button>
                    )}
                  </td>

                  <td className="px-4 py-4">
                    <StarRating
                      rating={lening.beoordeling}
                      onRate={(r) => onRate(lening.id, r)}
                    />
                  </td>

                  <td className="px-4 py-4 font-medium text-gray-900">
                    {lening.avgRating?.toFixed(1) || '0.0'}{' '}
                    <span className="text-amber-400">★</span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export const Leeg = () => (
  <LeesgeschiedenisUI
    leningen={[]}
    stats={{}}
    onInleveren={() => {}}
    onRate={() => {}}
  />
);

export const MetData = () => {
  const [leningen, setLeningen] = useState(MOCK_LENINGEN);

  const handleInleveren = (id) => {
    setLeningen((prev) =>
      prev.map((l) =>
        l.id === id
          ? { ...l, ingeleverd: true, inleverdatum: new Date().toISOString().slice(0, 10) }
          : l
      )
    );
  };

  const handleRate = (id, rating) => {
    setLeningen((prev) =>
      prev.map((l) => (l.id === id ? { ...l, beoordeling: rating } : l))
    );
  };

  return (
    <LeesgeschiedenisUI
      leningen={leningen}
      stats={MOCK_STATS}
      onInleveren={handleInleveren}
      onRate={handleRate}
    />
  );
};

export const AutoFill = () => {
  const [leningen, setLeningen] = useState([]);
  const [stats, setStats] = useState({});
  const [filled, setFilled] = useState(false);

  const handleAutoFill = () => {
    setLeningen(MOCK_LENINGEN);
    setStats(MOCK_STATS);
    setFilled(true);
  };

  const handleReset = () => {
    setLeningen([]);
    setStats({});
    setFilled(false);
  };

  const handleInleveren = (id) => {
    setLeningen((prev) =>
      prev.map((l) =>
        l.id === id
          ? { ...l, ingeleverd: true, inleverdatum: new Date().toISOString().slice(0, 10) }
          : l
      )
    );
  };

  const handleRate = (id, rating) => {
    setLeningen((prev) =>
      prev.map((l) => (l.id === id ? { ...l, beoordeling: rating } : l))
    );
  };

  return (
    <div>
      <div
        style={{
          display: 'flex',
          gap: '8px',
          padding: '1rem',
          background: '#f8fafc',
          borderBottom: '1px solid #e2e8f0',
        }}
      >
        <button
          onClick={handleAutoFill}
          disabled={filled}
          style={{
            padding: '6px 16px',
            background: filled ? '#94a3b8' : '#3b82f6',
            color: '#fff',
            border: 'none',
            borderRadius: '6px',
            cursor: filled ? 'default' : 'pointer',
            fontSize: '13px',
          }}
        >
          ✨ Auto-fill met mock data
        </button>
        <button
          onClick={handleReset}
          style={{
            padding: '6px 14px',
            background: '#e5e7eb',
            color: '#374151',
            border: 'none',
            borderRadius: '6px',
            cursor: 'pointer',
            fontSize: '13px',
          }}
        >
          Reset
        </button>
      </div>

      <LeesgeschiedenisUI
        leningen={leningen}
        stats={stats}
        onInleveren={handleInleveren}
        onRate={handleRate}
      />
    </div>
  );
};