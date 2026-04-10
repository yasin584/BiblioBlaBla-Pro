import React, { useState } from 'react';
import FilterBar from '../../components/FilterBar';

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