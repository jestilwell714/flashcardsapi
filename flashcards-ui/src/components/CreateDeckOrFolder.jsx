import { useState } from 'react';

export default function CreateDeckOrFolder(initialData, type, onSubmit) {
    const isEdit = !!initialData;
    const createDeckUrl = 'https://localhost:8080/decks';
    const createFolderUrl = 'https://localhost:8080/folders';

    const url = type === "folder" ? (isEdit ?  createFolderUrl+'/'+ initialData.id : createFolderUrl) : (isEdit ?  createDeckUrl + '/'+ initialData.id : createDeckUrl);
    const [formData, setFormData] = useState(initialData || {name: ''});

    function handleSubmit(e) {
        e.preventDefault();
        fetch(url, {
            method: isEdit ? 'PUT' : 'POST',
            headers: { 'Content-Type': 'application/json',
                       'X-User-ID': '1'
            },
            body: JSON.stringify(formData),
        })
        .then(response => {
            if (!response.ok) console.error("Database didn't create/edit deck");
            onSubmit();
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