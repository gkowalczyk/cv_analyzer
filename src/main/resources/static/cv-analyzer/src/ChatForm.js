import React, { useState } from 'react';

const ChatForm = ({ onSubmit, loading }) => {
    const [query, setQuery] = useState('');
    const [cvUrl, setCvUrl] = useState('');

    const handleClick = () => {
        if (query.trim()) {
            onSubmit({ query, cvUrl });
        }
    };

    return (
        <div className="card p-4 shadow-sm">
            <div className="mb-3">
                <label className="form-label">Link do CV (opcjonalny)</label>
                <input
                    type="text"
                    placeholder="Wklej link do CV"
                    value={cvUrl}
                    onChange={(e) => setCvUrl(e.target.value)}
                    className="form-control"
                />
            </div>
            <div className="mb-3">
                <label className="form-label">Zapytanie do modelu AI</label>
                <textarea
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    className="form-control"
                    rows="3"
                    placeholder="Napisz zapytanie (np. 'Oferty we Wrocławiu dla Junior Java Developera')"
                />
            </div>
            <button className="btn btn-primary w-100" onClick={handleClick} disabled={loading}>
                {loading ? (
                    <>
                        <span className="spinner-border spinner-border-sm me-2" role="status" />
                        Przetwarzanie...
                    </>
                ) : (
                    'Analizuj'
                )}
            </button>
        </div>
    );
};

export default ChatForm;
