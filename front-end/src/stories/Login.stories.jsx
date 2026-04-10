import Login from '../Login.jsx';
import axios from "axios";
import { expect } from '@storybook/jest';
import { within, userEvent, waitFor } from '@storybook/testing-library';
import { BrowserRouter } from 'react-router-dom'; 
import '../../src/index.css';

export default {
  title: "Pages/Login",
  component: Login,
  parameters: {
    layout: "fullscreen",
  },
  decorators: [
    (Story) => (
      <BrowserRouter>
        <Story />
      </BrowserRouter>
    ),
  ],
};
const mockAxios = (status = 200, response = {}) => {
  axios.post = async () => {
    if (status !== 200) {
      const error = new Error("Request failed");
      error.response = { status }; 
      throw error;
    }
    return { status, data: response };
  };
};

// 1. Succes story
export const SuccesvolleLogin = {
  render: () => {
    mockAxios(200, { token: "fake-token" });
    return <Login />;
  },
  play: async ({ canvasElement }) => {
    const canvas = within(canvasElement);
    // Gebruik findBy om er zeker van te zijn dat elementen geladen zijn
    await userEvent.type(canvas.getByLabelText(/email/i), "test@mail.com");
    await userEvent.type(canvas.getByLabelText(/wachtwoord/i), "1234");
    await userEvent.click(canvas.getByRole("button", { name: /inloggen/i }));

    await waitFor(() => expect(localStorage.getItem("token")).toBe("fake-token"));
  },
};

// 2. Foutmelding story
export const VerkeerdeInloggegevens = {
  render: () => {
    mockAxios(401);
    return <Login />;
  },
  play: async ({ canvasElement }) => {
    const canvas = within(canvasElement);
    await userEvent.type(canvas.getByLabelText(/email/i), "verkeerd@mail.com");
    await userEvent.type(canvas.getByLabelText(/wachtwoord/i), "fout");
    await userEvent.click(canvas.getByRole("button", { name: /inloggen/i }));

    await waitFor(() => {
      expect(canvas.getByText(/gebruikersnaam of wachtwoord is onjuist/i)).toBeInTheDocument();
    });
  },
};