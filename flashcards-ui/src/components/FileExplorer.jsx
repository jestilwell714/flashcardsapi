import { useEffect, useState } from "react";
import { useNavigate, useParams } from 'react-router-dom';
import CreateDeckOrFolder from "./CreateDeckOrFolder";


export default function FileExplorer( {onSelectItem} ) {
    const { type, id } = useParams();
    const [content,setContent] = useState([]);
    const [isCreate, setIsCreate] = useState(false);
    const [createType, setCreateType] = useState(null);
    const [showDropdown, setShowDropdown] = useState(false);
    const [refreshKey, setRefreshKey] = useState(0);
    
    let fetchContentsUrl = type === "deck" ? `http://localhost:8080/api/decks/${id}/flashcards` : (type === "root" ? `http://localhost:8080/api/content` : `http://localhost:8080/api/content/${id}`);

    useEffect(() => {
        fetch(fetchContentsUrl, {
            headers: {
                'Content-Type': 'application/json',
                'X-User-ID': '1'
            }
        })
        .then(response => response.json())
        .then(data => { 
                if (Array.isArray(data)) {
                setContent(data);
            } else {
                setContent([data]);
            }
            }
        ).catch(err => console.error("Fetch failed:", err));
    }, [fetchContentsUrl,refreshKey]); 

    const navigate = useNavigate();

    function handleClick(item) {
        let mode = "preview";
        if(type == null) {
            mode = "edit";
        } else {
            navigate(`/explorer/${item.type}/${item.id}`);
        }
        onSelectItem(item, item.type, mode);
    }

    function handleCreate(type ) {
            setShowDropdown(false);
            setIsCreate(true);
            setCreateType(type);
    }

    function handleSubmit() {
        setIsCreate(false);
        setRefreshKey(prev => prev +1);
    }

    function handleDelete(e,item) {
        e.stopPropagation();
        const itemType = item.type || "flashcard";
        const confirmed = window.confirm(`Are you sure you want to delete this ${item.type}?`);

        if (confirmed) {
            executeDelete(item.id, itemType);
        }
    }

    const executeDelete = (itemId, itemType) => {
        
        fetch(`http://localhost:8080/api/${itemType}s/${itemId}`, {
        method: 'DELETE',
        headers: {
            'X-User-ID': '1'
        }
        })
        .then(response => {
            if (response.ok) {
                setRefreshKey(prev => prev + 1);
            }
        })
        .catch(err => console.error("Delete error:", err));
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
                {isCreate ? <CreateDeckOrFolder parentId={type === "root" ? null : id} initialData={null} type={createType} onSubmit={handleSubmit}/> : ''}
                {content.map((item) => (
                    <li key={`${item.type}-${item.id}`} onClick={() => handleClick(item)}>
                        <span className="icon">
                                {item.type === "folder" ? '📁' : ''}
                                {item.type === "deck" ? '🎴' : ''}
                        </span>
                        <h3>{item.type == null ? item.question : item.name}</h3>
                        <button onClick={(e) => handleDelete(e,item) }>Delete</button>
                    </li>
                ))}
            </ul>
        )
}