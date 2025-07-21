import React, { useState } from 'react';
import ReactMarkdown from 'react-markdown';
import ChatForm from './ChatForm';
import './App.css';

const App = () => {
    const [result, setResult] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSearch = async ({ query, cvUrl }) => {
        try {
            setLoading(true);
            const params = new URLSearchParams();
            params.append('message', query);
            if (cvUrl) {
                params.append('cvUrl', cvUrl);
            }

            const response = await fetch(`http://localhost:8081/chat?${params.toString()}`);
            const data = await response.text();
            setResult(data);
        } catch (error) {
            console.error('Błąd podczas wysyłania zapytania do backendu:', error);
            setResult('Wystąpił błąd podczas komunikacji z backendem.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container my-5 py-5">
            <h1 className="mb-4 text-center">Analizator ofert i CV</h1>
            <ChatForm onSubmit={handleSearch} loading={loading} />
            {result && (
                <div className="mt-4 p-4 border rounded bg-light">
                    <div className="markdown-body">
                        <ReactMarkdown>{result}</ReactMarkdown>
                    </div>
                </div>
            )}
        </div>
    );
};

export default App;
