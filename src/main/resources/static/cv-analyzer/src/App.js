import React, { useState } from 'react';
import ReactMarkdown from 'react-markdown';
import ChatForm from './ChatForm';
import './App.css';

const App = () => {
    const [result, setResult] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSearch = async ({query, cvUrl}) => {
        try {
            setLoading(true);
            const params = new URLSearchParams();
            params.append('message', query);
            if (cvUrl) {
                params.append('cvUrl', cvUrl);
            }

            const response = await fetch(`https://cv-job-analyzer.gkowalczyk-dev-app.site/chat?${params.toString()}`, {
                // const response = await fetch(`http://localhost:8082/chat?${params.toString()}`, {
                method: 'GET',
                credentials: 'include'
            });
            const data = await response.text();
            setResult(data);
        } catch (error) {
            console.error('Błąd podczas wysyłania zapytania do backendu:', error);
            setResult('Wystąpił błąd podczas komunikacji z backendem.');
        } finally {
            setLoading(false);
        }
    };
    const handleSave = async () => {
        try {
            // const response = await fetch('http://localhost:8082/chat/save', {
            const response = await fetch('https://cv-job-analyzer.gkowalczyk-dev-app.site/chat/save', {
                method: 'POST',
                credentials: 'include'
            });

            if (response.ok) {
                alert('Dane zostały zapisane do bazy.');
            } else if (response.status === 204) {
                alert(' Brak danych do zapisania.');
            } else {
                alert(' Wystąpił błąd podczas zapisu.');
            }
        } catch (error) {
            console.error('Błąd podczas zapisu do bazy:', error);
            alert(' Wystąpił błąd połączenia z backendem.');
        }
    };
    return (
        <div className="container my-5 py-5">
            <h1 className="mb-4 text-center">Analizator ofert i CV</h1>
            <ChatForm onSubmit={handleSearch} loading={loading}/>

            {result && (
                <div className="mt-4 p-4 border rounded bg-light">
                    <div className="markdown-body mb-3">
                        <ReactMarkdown>{result}</ReactMarkdown>
                    </div>

                    <div className="d-flex justify-content-center">
                        <button className="btn btn-success px-4" onClick={handleSave}>
                            Zapisz dane do bazy
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default App;