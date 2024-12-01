import express, { Application } from "express";
import Server from "./src/server";
import Client from "./src/client";

const app: Application = express();
const server: Server = new Server(app);
export const client: Client = new Client();

app.listen(8080, () => {
  console.log("Backend is running on port 8080");
});

