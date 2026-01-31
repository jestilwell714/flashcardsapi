import { useEffect, useParams, useState } from "react";
import { useNavigate } from 'react-router-dom';
import CreateDeckOrFolder from "./CreateDeckOrFolder";


export default function FileExplorer( {onSelectFlashCard} ) {
    const { type, id } = useParams();
    const [content,setContent] = useState(null);
    const [isCreate, setIsCreate] = useState(false);
    const [createType, setCreateType] = useState(null);
    const [showDropdown, setShowDropdown] = useState(false);
    
    const fetchContentsUrl = type === "root" ? '' : `${type}/${id}`;


    useEffect(() => {
        fetch(fetchContentsUrl)
        .then(response => response.json())
        .then(data => { 
                setContent(data);
            }
        ).catch(err => console.error("Fetch failed:", err));
    }, [fetchContentsUrl]); 

    const navigate = useNavigate();

    function handleClick(item) {
        let mode = "preview";
        if(type === "flashcard") {
            mode = "edit";
        } else {
            navigate(`/explorer/${item.type}/${item.id}}`);
        }
        onSelectFlashCard(item, item.type, mode);
    }

    function handleCreate(type ) {
            setShowDropdown(false);
            setIsCreate(true);
            setCreateType(type);
    }

    function handleSubmit() {
        setIsCreate(false);
    }

    return (
            <ul>
                <li>
                    <nav>
                        <button onClick={() => setShowDropdown(!showDropdown)}>Create</button>
                        {showDropdown && (
                            <ul>
                                <li onClick={() => handleCreate("folder")}>
                                    <h4>Folder</h4>
                                </li>
                                <li onClick={() => handleCreate("deck")}>
                                    <h4>Deck</h4>
                                </li>
                            </ul>
                        )}
                    </nav>
                </li>
                {isCreate ? <CreateDeckOrFolder type={createType} onSubmit={handleSubmit}/> : ''}
                {content.map((item) => (
                    <li key={item.id} onClick={() => handleClick(item)}>
                        <span className="icon">
                                {item.type === 'folder' ? '📁' : ''}
                                {item.type === 'deck' ? '🎴' : ''}
                        </span>
                        <h3>{item.name}</h3>
                    </li>
                ))}
            </ul>
        )
}