import { Request, Response } from "express";
import { NotificationEvent } from "../models/NotificationEvent";
import { client } from "../../app";

export async function postNotification(
  req: Request,
  res: Response
): Promise<void> {
  try {
    var notificationEvent: NotificationEvent = req.body as NotificationEvent;

    client.notify(notificationEvent);

    const serverResponse: string = await client.onReceiveData();

    res.status(200).send(JSON.parse(serverResponse));
  } catch (error) {
    if (error instanceof Error) {
      console.error(error.message);
    }

    client.onSocketError();
    res.status(500).send({ error: "Internal server error" });
  }
}
