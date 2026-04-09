import React from 'react';

const StarRating = ({ rating, onRate }) => {
  return (
    <div className="flex text-2xl">
      {[1, 2, 3, 4, 5].map((star) => (
        <span
          key={star}
          className={`cursor-pointer ${star <= rating ? 'text-yellow-400' : 'text-gray-300'}`}
          onClick={() => onRate(star)}
        >
          ★
        </span>
      ))}
    </div>
  );
};

export default StarRating;