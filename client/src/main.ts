const formulario = document.querySelector("form")!;
const getbutton = document.querySelector("#elget") as HTMLButtonElement;
const inputs = document.querySelectorAll("input");
const textarea: HTMLTextAreaElement = document.querySelector("#uno")!;
const textarea2: HTMLTextAreaElement = document.querySelector("#dos")!;
const textarea3: HTMLTextAreaElement = document.querySelector("#tres")!;
let token = document.querySelector("p")!;
const urlbase = "http://localhost:8080/api";
const logout: HTMLButtonElement = document.querySelector("#logout")!;

async function formdata(e: SubmitEvent) {
  e.preventDefault();
  const username = inputs[0].value;
  const password = inputs[1].value;
  const ft = await fetch(`${urlbase}/user/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify({ username, password }),
  });
  if (!ft.ok) {
    token.textContent = "no se pudo";
    return;
  }
  const res = (await ft.json()) as { jwt: string };
  token.textContent = res.jwt;
}

formulario.addEventListener("submit", formdata);

const uno = async () => {
  if (!!token.textContent.trim()) {
    const ft = await fetch(`${urlbase}/dato`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token.textContent}`,
      },
    });
    console.log("hola");
    if (!ft.ok) {
      textarea.value = "no se pudo";
      return;
    }

    textarea.value = await ft.text();
  }
};

const dos = async () => {
  const ft2 = await fetch(`${urlbase}/dato2`, {
    method: "GET",
    credentials: "include",
  });

  if (!ft2.ok) {
    textarea2.value = "No se puede";
    return;
  }
  textarea2.value = await ft2.text();
};

getbutton.onclick = async () => {
  uno();
  dos();
};

logout.onclick = async () => {
  const ft2 = await fetch(`${urlbase}/dato2`, {
    method: "POST",
    credentials: "include",
  });

  if (!ft2.ok) {
    textarea3.value = "No se puede";
    return;
  }

  textarea3.value = await ft2.text();
};
