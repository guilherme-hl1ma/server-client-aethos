import net, { createConnection, Socket } from "net";
import { NotificationEvent } from "./models/NotificationEvent";

/* export const validateSignUp = (operationValidation: OperationValidation) => {
  const operationJson = JSON.stringify(operationValidation);
  console.log(operationJson);
  client.write(operationJson + "\n");
}; */

export default class Client {
  static readonly DEFAULT_HOST: string = "localhost";
  static readonly DEFAULT_PORT: number = 3000;
  private client: net.Socket = new net.Socket();

  constructor(
    host: string = Client.DEFAULT_HOST,
    port: number = Client.DEFAULT_PORT
  ) {
    this.client = new net.Socket();
    try {
      this.client.connect(port, host);
    } catch (error) {
      if (error instanceof Error)
        console.error(
          `Failed to connect to socket on port ${port}: ${error.message}`
        );
    }
  }

  private connectSocker(port: number, host: string) {
    this.client.connect(port, host, () => {
      try {
        console.log(`Connected to the socket at the port: ${port}`);
      } catch (error) {
        if (error instanceof Error)
          throw new Error(
            `Failed to connect to socket on port ${port}: ${error.message}`
          );
      }
    });
  }

  notify(notificationEvent: NotificationEvent) {
    const notificationEventJson = JSON.stringify(notificationEvent);
    this.client.write(notificationEventJson + "\n");
  }

  onReceiveData() {
    return new Promise<string>((resolve, reject) => {
      this.client.on("data", (data) => {
        try {
          console.log("\nReceived from Java Server:", data.toString() + "\n");
          resolve(data.toString());
        } catch (error) {
          console.log("Error parsing response:", error);
          reject(error);
        }
      });

      this.client.on("error", (err) => {
        reject(err);
      });
    });
  }

  onSocketError = () => {
    this.client.on("error", function (err) {
      console.log("Error:", err.message);
    });
  };

  onSocketClose = () => {
    this.client.on("close", function () {
      console.log("Connection closed");
    });
  };

  onCloseConnection = () => {
    this.client.end();
  };
}
