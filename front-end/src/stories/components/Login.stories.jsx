 import Login from '../../Login.jsx';
import axios from "axios";
import { expect, within, userEvent, waitFor } from "storybook/test";
import '../../index.css';


export default {
  title: "Pages/Login",
  component: Login,
  parameters: {
    layout: "fullscreen", // Gebruik fullscreen voor de juiste Tailwind weergave
  },
};

// De centrale mock functie
const mockAxios = (status = 200, response = {}) => {
  axios.post = async () => {
    if (status !== 200) {
      throw new Error("Request failed");
    }
    return {
      status,
      data: response,
    };
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
    await userEvent.type(await canvas.findByLabelText(/email/i), "test@mail.com");
    await userEvent.type(await canvas.findByLabelText(/wachtwoord/i), "1234");
    await userEvent.click(await canvas.findByRole("button", { name: /inloggen/i }));

    await waitFor(() => expect(localStorage.getItem("token")).toBe("fake-token"));
  },
};

// 2. Foutmelding story:
export const VerkeerdeInloggegevens = {
  render: () => {
    mockAxios(401); // Geen data nodig, de catch in Login.jsx handelt het af
    return <Login />;
  },
  play: async ({ canvasElement }) => {
    const canvas = within(canvasElement);
    await userEvent.type(await canvas.findByLabelText(/email/i), "verkeerd@mail.com");
    await userEvent.type(await canvas.findByLabelText(/wachtwoord/i), "fout");
    await userEvent.click(await canvas.findByRole("button", { name: /inloggen/i }));

    await waitFor(() => {
      expect(canvas.getByText(/gebruikersnaam of wachtwoord is onjuist/i)).toBeInTheDocument();
    });
  },
};