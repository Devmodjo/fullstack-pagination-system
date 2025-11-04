import React, { useEffect, useState } from "react";
import { getPersons, addPerson } from "./api";

interface Person {
  pseudo: string;
  profession: string;
}

export default function App() {
  const [persons, setPersons] = useState<Person[]>([]);
  const [page, setPage] = useState(0);
  const [pseudo, setPseudo] = useState("");
  const [profession, setProfession] = useState("");

  const itemsPerPage = 5;
  const totalPages = Math.ceil(persons.length / itemsPerPage);

  useEffect(() => {
    getPersons().then((data) => {
      setPersons(data);
    });
  }, []);

  const handleAdd = async () => {
    if (!pseudo || !profession) return;
    await addPerson(pseudo, profession);
    setPseudo("");
    setProfession("");
    const data = await getPersons();
    setPersons(data);
  };

  // Découpe les données localement selon la page
  const start = page * itemsPerPage;
  const end = start + itemsPerPage;
  const currentPersons = persons.slice(start, end);

  return (
    <div style={{ padding: 20 }}>
      <h1>Pagination Front-End (React + Spring Boot)</h1>

      <div>
        <input
          placeholder="Pseudo"
          value={pseudo}
          onChange={(e) => setPseudo(e.target.value)}
        />
        <input
          placeholder="Profession"
          value={profession}
          onChange={(e) => setProfession(e.target.value)}
        />
        <button onClick={handleAdd}>Ajouter</button>
      </div>

      <ul>
        {currentPersons.map((p, key) => (
          <li key={key}>
            {p.pseudo} — {p.profession}
          </li>
        ))}
      </ul>

      <div>
        <button disabled={page === 0} onClick={() => setPage(page - 1)}>
          Précédent
        </button>
        <span>
          Page {page + 1}/{totalPages || 1}
        </span>
        <button
          disabled={page + 1 >= totalPages}
          onClick={() => setPage(page + 1)}
        >
          Suivant
        </button>
      </div>
    </div>
  );
}
