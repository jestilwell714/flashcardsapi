import { useState } from 'react';

export default function CreateDeck(initialData) {
    const isEdit = !!initialData;
    const createDeckUrl = '';
    const editDeckUrl = '';
    const [formData, setFormData] = useState(initialData || {name: ''});

    function handleSubmit(e) {
        e.preventDefault();
        fetch(isEdit ?  editDeckUrl : createDeckUrl, {
            method: isEdit ? 'PUT' : 'POST',
            headers: { 'Content-Type': 'application/json'},
            body: JSON.stringify(formData),
        })
        .then(response => {
            if (!response.ok) console.error("Database didn't create/edit deck");
        })
        .catch(error => console.error("Connection error", error));
    }

    function handleChange(e) {
        setFormData({...formData, [e.target.name]: e.target.value});
    }

    return (
        <li>
            <form onSubmit={handleSubmit}>
                <input
                    name="name"
                    placeholder="name"
                    value={formData}
                    onChange={handleChange}>
                </input>
            </form>
        </li>
    )
}