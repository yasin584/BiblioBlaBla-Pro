import React from 'react';

const DashboardStats = ({ stats }) => {
  return (
    <div className="flex gap-6 mb-8">
      <div className="bg-white p-6 rounded shadow border w-64 text-center">
        <h3 className="text-gray-600 text-sm font-semibold uppercase">Totaal geleende boeken</h3>
        <p className="text-5xl font-bold mt-2">{stats.totaalGeleend || 0}</p>
      </div>
      <div className="bg-white p-6 rounded shadow border w-64 text-center">
        <h3 className="text-gray-600 text-sm font-semibold uppercase">Top Genre</h3>
        <p className="text-4xl font-bold mt-2 text-pink-600">{stats.populairsteGenre || '-'}</p>
      </div>
    </div>
  );
};

export default DashboardStats;