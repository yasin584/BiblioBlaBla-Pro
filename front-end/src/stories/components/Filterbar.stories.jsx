import React, { useState } from 'react';
import FilterBar from '../../components/FilterBar';

export default {
  title: 'Bibliotheek/FilterBar',
  component: FilterBar,
};

const defaultFilters = {
  titel: '',
  auteur: '',
  genre: 'ALLE GENRES',
  start: '',
};

export const Leeg = () => {
  const [filters, setFilters] = useState(defaultFilters);

  return (
    <FilterBar filters={filters} setFilters={setFilters} />
  );
};