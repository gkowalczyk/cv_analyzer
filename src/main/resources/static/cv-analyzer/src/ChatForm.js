import React, { useState } from 'react';

const ChatForm = ({ onSubmit, loading }) => {
    const [query, setQuery] = useState('');

    const handleClick = () => {
        if (query.trim()) {
            onSubmit({ query});
        }
    };

    return (
        <div className="card p-4 shadow-sm">

            <div className="mb-3">
                <label className="form-label">Zapytanie do modelu AI</label>
                <textarea
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    className="form-control"
                    rows="3"
                    placeholder="Napisz zapytanie (np. 'Oferty we Wrocławiu dla Junior Java Developera
                    dopasowane do mojego CV https://moja-strona/cv.pdf')"
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
