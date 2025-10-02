import express from "express";
import cors from "cors";
import cookieParser from "cookie-parser";
import { jwtService } from "./jwtService";

const app = express();
const port = 8080;

app.use(cookieParser());
app.use(
  cors({
    credentials: true,
    origin: "http://localhost:5173",
  })
);
app.use(express.json());

app.post("/api/user/login", (req, res) => {
  const { username, password } = req.body as {
    username: string;
    password: string;
  };
  if (password != "hola123") {
    res.status(400).json({ message: "no se puede" });
    return;
  }
  const token = jwtService.generar({ username, authority: "USER" });
  res.cookie("mitoken", token, {
    httpOnly: true,
    secure: false,
    sameSite: "strict",
    maxAge: 3600 * 1000,
    path: "/",
  });

  res.json({ jwt: token });
});

app.get("/api/dato", async (req, res) => {
  const token = req.headers.authorization as string;
  if (!token || !token.trim().startsWith('Bearer ')) {
    res.sendStatus(403);
    return;
  }
  const view = jwtService.validation(token.replace("Bearer ", ""));
  if (view.authority !== "USER") {
    res.sendStatus(403);
    return;
  }
  res.json({
    message: "Si se pudo",
    timestamp: new Date(),
  });
});
app.get("/api/dato2", async (req, res) => {
  const token = req.cookies.mitoken as string;
  if (!token) {
    res.sendStatus(403);
    return;
  }
  const view = jwtService.validation(token);
  if (view.authority !== "USER") {
    res.sendStatus(403);
    return;
  }
  res.json({
    message: "Si se pudo",
    timestamp: new Date(),
  });
});

app.post("/api/dato2", (req, res) => {
  const token = req.cookies.mitoken as string;
  console.log(token);
  if (!token) {
    res.sendStatus(403);
    return;
  }
  const view = jwtService.validation(token);
  if (view.authority !== "USER") {
    res.sendStatus(403);
    return;
  }
  res.cookie("mitoken", "", {
    httpOnly: true,
    secure: false,
    sameSite: "strict",
    maxAge: 0,
    path: "/",
  });
  res.json({
    message: "Si se pudo",
    timestamp: new Date(),
  });
});

app.listen(port, () => {
  console.log(`Port: ${port}`);
});
