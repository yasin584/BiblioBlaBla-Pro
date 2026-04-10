import React, { useState } from 'react';
import FilterBar from './components/FilterBar';

export default {
  title: 'Bibliotheek/FilterBar',
  component: FilterBar,
  parameters: {
    docs: {
      description: {
        component: 'Filterbalk om leningen te doorzoeken op titel, genre en datum.',
      },
    },
  },
};

const defaultFilters = {
  titel: '',
  genre: 'ALLE GENRES',
  start: '',
};

export const Leeg = () => {
  const [filters, setFilters] = useState(defaultFilters);
  return (
    <div>
      <FilterBar filters={filters} setFilters={setFilters} />
      <pre style={{ marginTop: '1rem', fontSize: '12px', background: '#f3f4f6', padding: '0.75rem', borderRadius: '6px' }}>
        {JSON.stringify(filters, null, 2)}
      </pre>
    </div>
  );
};

export const VooringevuldFilters = () => {
  const [filters, setFilters] = useState({
    titel: 'Harry Potter',
    genre: 'Fantasy',
    start: '2024-01-01',
  });
  return (
    <div>
      <FilterBar filters={filters} setFilters={setFilters} />
      <pre style={{ marginTop: '1rem', fontSize: '12px', background: '#f3f4f6', padding: '0.75rem', borderRadius: '6px' }}>
        {JSON.stringify(filters, null, 2)}
      </pre>
    </div>
  );
};

export const AutoFill = () => {
  const [filters, setFilters] = useState(defaultFilters);

  const mockFilters = [
    { titel: 'De Hobbit', genre: 'Fantasy', start: '2024-03-15' },
    { titel: 'Middernacht', genre: 'Romance', start: '2024-06-01' },
    { titel: '1984', genre: 'Dystopian', start: '2023-11-20' },
    { titel: 'Sherlock Holmes', genre: 'Mystery', start: '2024-01-08' },
    { titel: '', genre: 'Classic', start: '2024-09-01' },
  ];

  const handleAutoFill = () => {
    const picked = mockFilters[Math.floor(Math.random() * mockFilters.length)];
    setFilters(picked);
  };

  const handleReset = () => setFilters(defaultFilters);

  return (
    <div>
      <div style={{ display: 'flex', gap: '8px', marginBottom: '1rem' }}>
        <button
          onClick={handleAutoFill}
          style={{
            padding: '6px 14px',
            background: '#3b82f6',
            color: '#fff',
            border: 'none',
            borderRadius: '6px',
            cursor: 'pointer',
            fontSize: '13px',
          }}
        >
          ✨ Auto-fill filters
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
      <FilterBar filters={filters} setFilters={setFilters} />
      <pre style={{ marginTop: '1rem', fontSize: '12px', background: '#f3f4f6', padding: '0.75rem', borderRadius: '6px' }}>
        {JSON.stringify(filters, null, 2)}
      </pre>
    </div>
  );
};