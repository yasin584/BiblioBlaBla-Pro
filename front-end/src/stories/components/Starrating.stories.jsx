import React, { useState } from 'react';
import StarRating from './components/StarRating';

export default {
  title: 'Bibliotheek/StarRating',
  component: StarRating,
  parameters: {
    docs: {
      description: {
        component: 'Sterren-beoordelingscomponent. Klik op een ster om een rating te geven.',
      },
    },
  },
  argTypes: {
    rating: {
      control: { type: 'number', min: 0, max: 5 },
      description: 'Huidige beoordeling (0–5)',
    },
  },
};

export const Leeg = {
  args: { rating: 0, onRate: (r) => console.log('Rated:', r) },
};

export const DriesSterren = {
  args: { rating: 3, onRate: (r) => console.log('Rated:', r) },
};

export const Volledig = {
  args: { rating: 5, onRate: (r) => console.log('Rated:', r) },
};

export const Interactief = () => {
  const [rating, setRating] = useState(0);
  return (
    <div style={{ padding: '1rem' }}>
      <p style={{ marginBottom: '0.5rem', fontSize: '14px', color: '#666' }}>
        Klik op een ster om te beoordelen:
      </p>
      <StarRating rating={rating} onRate={setRating} />
      <p style={{ marginTop: '0.5rem', fontSize: '13px', color: '#888' }}>
        Huidige rating: <strong>{rating}</strong> / 5
      </p>
    </div>
  );
};

export const AutoFill = () => {
  const [rating, setRating] = useState(0);

  const handleAutoFill = () => {
    const randomRating = Math.floor(Math.random() * 5) + 1;
    setRating(randomRating);
  };

  return (
    <div style={{ padding: '1rem' }}>
      <button
        onClick={handleAutoFill}
        style={{
          marginBottom: '1rem',
          padding: '6px 14px',
          background: '#3b82f6',
          color: '#fff',
          border: 'none',
          borderRadius: '6px',
          cursor: 'pointer',
          fontSize: '13px',
        }}
      >
        ✨ Auto-fill (willekeurige rating)
      </button>
      <br />
      <StarRating rating={rating} onRate={setRating} />
      <p style={{ marginTop: '0.5rem', fontSize: '13px', color: '#888' }}>
        Rating: <strong>{rating}</strong> / 5
      </p>
    </div>
  );
};