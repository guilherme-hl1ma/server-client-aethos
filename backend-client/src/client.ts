import net from "net";
import { NotificationEvent } from "./models/NotificationEvent";

/**
 * Classe que representa o Cliente da arquitetura Cliente-Servidor.
 */
export default class Client {
  static readonly DEFAULT_HOST: string = "localhost";
  static readonly DEFAULT_PORT: number = 3000;
  private client: net.Socket = new net.Socket();
  private isConnected: boolean = false;
  private reconnectInterval: NodeJS.Timeout | null = null;

  constructor(
    host: string = Client.DEFAULT_HOST,
    port: number = Client.DEFAULT_PORT
  ) {
    this.connectSocket(port, host);
  }

  private connectSocket(port: number, host: string) {
    this.client = new net.Socket();
    this.client.connect(port, host, () => {
      console.log(`Connected to the socket at port: ${port}\n`);
      this.isConnected = true;
      if (this.reconnectInterval) {
        clearInterval(this.reconnectInterval); // Cancela tentativas de reconexão, se existirem
        this.reconnectInterval = null;
      }
    });

    this.onSocketError();
    this.onSocketClose(port, host);
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
      return {
        isValid: false,
        message:
          "O UID tanto do usuário da ação quanto do que recebe deve ter 28 caracteres.",
      };
    } else if (
      !notificationEvent.comment &&
      !notificationEvent.follow &&
      !notificationEvent.like
    ) {
      return {
        isValid: false,
        message: "A notificação deve ter pelos menos uma ação como true",
      };
    }

    const notificationEventJson = JSON.stringify(notificationEvent);
    this.client.write(notificationEventJson + "\n");
    return {
      isValid: true,
      message: "",
    };
  }

  private reconnect(port: number, host: string) {
    if (!this.reconnectInterval) {
      this.reconnectInterval = setInterval(() => {
        console.warn("Attempting to reconnect to the server...\n");
        this.connectSocket(port, host);
      }, 5000);
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
    this.client.on("error", (err) => {
      this.isConnected = false;
    });
  };

  /**
   * Método que representa um evento de fechamento da conexão Socket.
   */
  onSocketClose = (port: number, host: string) => {
    this.client.on("close", () => {
      console.warn("Connection closed. Reconnecting...\n");
      this.isConnected = false;
      this.reconnect(port, host);
    });
  };

  /**
   * Método que realiza o fechamento da conexão Socket com o Servidor Java.
   */
  closeConnection = () => {
    console.log("Desconectando cliente");
    this.client.end();
  };

  isServerConnected(): boolean {
    return this.isConnected;
  }
}
