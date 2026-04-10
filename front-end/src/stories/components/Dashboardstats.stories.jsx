import React from 'react';
import DashboardStats from '../../components/DashboardStats';

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