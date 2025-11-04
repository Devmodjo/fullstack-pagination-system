export async function getPersons() {
  const response = await fetch("http://localhost:8080/api/person");
  const data = await response.json();
  return data;
}

export async function addPerson(pseudo: string, profession: string) {
  await fetch("http://localhost:8080/api/person", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ pseudo, profession }),
  });
}
