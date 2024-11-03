import express, { Application } from "express";
import cors, { CorsOptions } from "cors";
import Routes from "./routes/routes";

/*const app = express();

const route = Router();

app.use(express.json());

route.get("/validateSignUp", async (req: Request, res: Response) => {
  try {
    const userForm: UserForm = req.body as UserForm;
    var operationValidation: OperationValidation = {
      operation: "signup",
      object: userForm,
    };

    validateSignUp(operationValidation);

    const data: string = await onReceiveData();

    res.send(JSON.parse(data));
  } catch (error) {
    if (error instanceof Error) {
      console.log(error.message);
    }
    onSocketError();
    res.status(500).send({ error: "Internal server error" });
  }
});


app.use(route);

app.listen(8080, () => {
  console.log("Backend running on port 3333");
  connectSocket(3000, "localhost");
}); */

export default class Server {
  constructor(app: Application) {
    this.config(app);
    new Routes(app);
  }

  private config(app: Application): void {
    const corsOptions: CorsOptions = {
      origin: "http://localhost:8080",
    };

    app.use(cors(corsOptions));
    app.use(express.json());
  }
}
