import React from 'react';

const FilterBar = ({ filters, setFilters }) => {
  const handleChange = (e) => {
    setFilters({ ...filters, [e.target.name]: e.target.value });
  };
  

  return (
    <div className="bg-gray-100 p-4 rounded-t-lg flex gap-4 items-end border-b">
      <div className="flex-1">
        <label className="block text-xs font-bold uppercase mb-1">Zoeken</label>
        <input 
          name="titel"
          className="w-full p-2 border rounded" 
          placeholder="Titel, Auteur" 
          onChange={handleChange}
        />
      </div>
      <div className="w-48">
        <label className="block text-xs font-bold uppercase mb-1">Genre</label>
        <select name="genre" className="w-full p-2 border rounded" onChange={handleChange}>
          <option value="ALLE GENRES">ALLE GENRES</option>
            <option value="Romance">Romance</option>
            <option value="Classic">Classic</option>
            <option value="Fantasy">Fantasy</option>
            <option value="Mystery">Mystery</option>
            <option value="Dystopian">Dystopian</option>
            <option value="History">History</option>
          
        </select>
      </div>
      <div className="w-48">
        <label className="block text-xs font-bold uppercase mb-1">Datum</label>
        <input name="start" type="date" className="p-2 border rounded text-sm" onChange={handleChange} />
      </div>
    </div>
  );
};

export default FilterBar;