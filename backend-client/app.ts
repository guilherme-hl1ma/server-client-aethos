import express, { Application } from "express";
import Server from "./src/server";
import Client from "./src/client";

const app: Application = express();
const server: Server = new Server(app);
export const client: Client = new Client();

app.listen(8080, () => {
  console.log("Backend is running on port 8080");
});

client.onSocketClose();
client.onSocketError();

// Ouça eventos de encerramento do processo
process.on("SIGINT", () => {
  client.onCloseConnection();
  process.exit(0); // Encerra o processo
});

process.on("SIGTERM", () => {
  client.onCloseConnection();
  process.exit(0); // Encerra o processo
});
