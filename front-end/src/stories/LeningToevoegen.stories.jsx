import React from 'react';
import { MemoryRouter } from 'react-router-dom';
import LeningToevoegen from '../LeningToevoegen.jsx';
import { within, userEvent, waitFor } from '@storybook/testing-library';

export default {
  title: 'Pagina/LeningToevoegen',
  component: LeningToevoegen,
  decorators: [
    (Story) => (
      <MemoryRouter>
        <Story />
      </MemoryRouter>
    ),
  ],
};

// Een super simpele mock die altijd "Succes" geeft
const setupSimpeleMock = () => {
  window.fetch = () =>
    Promise.resolve({
      ok: true,
      json: () => Promise.resolve(['Boek 1']),
      text: () => Promise.resolve("Succes"),
    });
};

export const AutomatischeTest = {
  render: () => {
    setupSimpeleMock();
    return <LeningToevoegen toonLayout={false} />;
  },
  play: async ({ canvasElement }) => {
    const canvas = within(canvasElement);

    // 1. Typ de titel
    await userEvent.type(canvas.getByLabelText(/Titel/i), "Mijn Boek");

    // 2. Typ de auteur
    await userEvent.type(canvas.getByLabelText(/Auteur/i), "Jan Jansen");

    // 3. Vul de datum in (simpelste manier)
    const datumVeld = canvas.getByLabelText(/Inleverdatum/i);
    await userEvent.type(datumVeld, '2026-12-31');

    // 4. Klik op de knop
    await userEvent.click(canvas.getByRole("button", { name: /Lenen/i }));

    // 5. Wacht even tot de tekst verschijnt
    await waitFor(() => {
      const bericht = canvas.queryByText(/Lening succesvol toegevoegd/i);
      if (!bericht) throw new Error("Bericht nog niet gevonden");
    });
  },
};