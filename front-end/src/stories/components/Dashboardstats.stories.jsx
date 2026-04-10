import React, { useState } from 'react';
import DashboardStats from './components/DashboardStats';

export default {
  title: 'Bibliotheek/DashboardStats',
  component: DashboardStats,
  parameters: {
    docs: {
      description: {
        component: 'Toont statistieken van de ingelogde gebruiker: totaal geleende boeken en populairste genre.',
      },
    },
  },
  argTypes: {
    stats: {
      control: 'object',
      description: 'Stats object met totaalGeleend en populairsteGenre',
    },
  },
};

export const Leeg = {
  args: {
    stats: {},
  },
};

export const MetData = {
  args: {
    stats: {
      totaalGeleend: 24,
      populairsteGenre: 'Fantasy',
    },
  },
};

export const AutoFill = () => {
  const [stats, setStats] = useState({});

  const mockData = [
    { totaalGeleend: 7, populairsteGenre: 'Romance' },
    { totaalGeleend: 42, populairsteGenre: 'Fantasy' },
    { totaalGeleend: 13, populairsteGenre: 'Classic' },
    { totaalGeleend: 5, populairsteGenre: 'Mystery' },
    { totaalGeleend: 31, populairsteGenre: 'Dystopian' },
  ];

  const handleAutoFill = () => {
    const picked = mockData[Math.floor(Math.random() * mockData.length)];
    setStats(picked);
  };

  return (
    <div style={{ padding: '1rem' }}>
      <button
        onClick={handleAutoFill}
        style={{
          marginBottom: '1.5rem',
          padding: '6px 14px',
          background: '#3b82f6',
          color: '#fff',
          border: 'none',
          borderRadius: '6px',
          cursor: 'pointer',
          fontSize: '13px',
        }}
      >
        ✨ Auto-fill stats
      </button>
      <DashboardStats stats={stats} />
    </div>
  );
};