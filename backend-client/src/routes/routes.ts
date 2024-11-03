import { Application } from "express";
import NotificationRoutes from "./notificationRoutes";

export default class Routes {
  constructor(app: Application) {
    app.use("/", NotificationRoutes);
  }
}

