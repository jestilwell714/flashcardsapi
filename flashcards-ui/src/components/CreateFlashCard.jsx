import { useState } from "react";

export default function CreateFlashCard({ initialData }) {
    const isEdit = !!initialData;
    const createFlashCardUrl = '';
    const editFlashCardUrl = '';
    const [formData, setFormData] = useState(initialData || { question: '', answer: ''});

    function handleSubmit(e) {
        e.preventDefault();
        fetch(isEdit ?  editFlashCardUrl : createFlashCardUrl, {
            method: isEdit ? 'PUT' : 'POST',
            headers: { 'Content-Type': 'application/json'},
            body: JSON.stringify(formData),
        })
        .then(response => {
            if (!response.ok) console.error("Database didn't create/edit flashcard");
        })
        .catch(error => console.error("Connection error", error));
    }

    function handleChange(e) {
        setFormData({...formData, [e.target.name]: e.target.value});
    }

    return (
        <div>
        <form onSubmit={handleSubmit}>
            <label>Front</label>
            <textarea 
                name="question"
                value={formData.question}
                onChange={handleChange}
            />

            <label>Back</label>
            <textarea
                name="answer"
                value={formData.answer}
                onChange={handleChange}
            />
            <button type="submit">{isEdit ? "Edit" : "Create"}</button>
        </form>
        </div>
    )
}