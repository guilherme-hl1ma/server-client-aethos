import { Router } from "express";
import { postNotification } from "../controllers/notificationController";

class NotificationRoutes {
  router = Router();

  constructor() {
    this.post();
  }

  post() {
    this.router.post("/notify", postNotification);
  }
}

export default new NotificationRoutes().router;
