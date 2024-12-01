import { Request, Response } from "express";
import { NotificationEvent } from "../models/NotificationEvent";
import { client } from "../../app";

export async function postNotification(
  req: Request,
  res: Response
): Promise<void> {
  try {
    if (!client.isServerConnected()) {
      res.status(503).send(JSON.parse(JSON.stringify({
        status: "error",
        message: "Servidor está atualmente indisponível. Tente Novamente mais tarde."
      })))
      return;
    }
    var notificationEvent: NotificationEvent = req.body as NotificationEvent;

    const response = client.notify(notificationEvent);

    if (!response.isValid) {
      res.status(500).send({
        status: "error",
        message: response.message,
      });
      return;
    }

    const serverResponse: string = await client.onReceiveData();

    res.status(200).send(JSON.parse(serverResponse));
  } catch (error) {
    if (error instanceof Error) {
      console.error(error.message);
    }

    client.onSocketError();
    res.status(500).send({
      status: "error",
      message: "Internal server error",
    });
  }
}
