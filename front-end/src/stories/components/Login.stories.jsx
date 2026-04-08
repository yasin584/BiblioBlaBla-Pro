import Login from "../../components/Login.jsx";
import axios from "axios";
import { expect, within, userEvent, waitFor } from "storybook/test";

export default {
  title: "Pages/Login",
  component: Login,
  parameters: {
    layout: "centered",
  },
};

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

export const Default = {};

export const SuccesvolleLogin = {
  render: () => {
    mockAxios(200, { token: "fake-token" });
    return <Login />;
  },
  play: async ({ canvasElement }) => {
    const canvas = within(canvasElement);

    const emailInput = await canvas.findByLabelText(/email/i);
    const passwordInput = await canvas.findByLabelText(/wachtwoord/i);
    const loginButton = await canvas.findByRole("button", {
      name: /inloggen/i,
    });

    await userEvent.type(emailInput, "test@mail.com");
    await userEvent.type(passwordInput, "1234");

    await userEvent.click(loginButton);

    await waitFor(() =>
      expect(localStorage.getItem("token")).toBe("fake-token")
    );
  },
};