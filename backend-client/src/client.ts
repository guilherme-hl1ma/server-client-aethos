import net from "net";
import { NotificationEvent } from "./models/NotificationEvent";

/**
 * Classe que representa o Cliente da arquitetura Cliente-Servidor.
 */
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

  /**
   * Método responsável por enviar para o Servidor Java um objeto de notificação.
   * @param {NotificationEvent} notificationEvent um objeto que representa um evento de notificação.
   */
  notify(notificationEvent: NotificationEvent) {
    if (
      notificationEvent.userFromDetails.uid.length != 28 ||
      notificationEvent.uidUserTo.length != 28
    ) {
      return false;
    } else if (
      !notificationEvent.comment &&
      !notificationEvent.follow &&
      !notificationEvent.like
    ) {
      return false;
    } else {
      const notificationEventJson = JSON.stringify(notificationEvent);
      this.client.write(notificationEventJson + "\n");
      return true;
    }
  }

  /**
   * Método que representa um evento de recibo de dados do Servidor Java.
   * @returns {Promise<string>} retorna uma Promise. Tenta capturar os dados e retorná-los, caso contrário, retornará um erro.
   */
  onReceiveData(): Promise<string> {
    return new Promise<string>((resolve, reject) => {
      this.client.on("data", (data) => {
        try {
          resolve(data.toString());
        } catch (error) {
          reject(error);
        }
      });

      this.client.on("error", (err) => {
        reject(err);
      });
    });
  }

  /**
   * Método que representa um evento de erro na conexão Socket.
   */
  onSocketError = () => {
    this.client.on("error", function (err) {
      console.log("Error:", err.message);
    });
  };

  /**
   * Método que representa um evento de fechamento da conexão Socket.
   */
  onSocketClose = () => {
    this.client.on("close", function () {
      console.log("Connection closed");
    });
  };

  /**
   * Método que realiza o fechamento da conexão Socket com o Servidor Java.
   */
  closeConnection = () => {
    console.log("Desconectando cliente");
    this.client.end();
  };
}
